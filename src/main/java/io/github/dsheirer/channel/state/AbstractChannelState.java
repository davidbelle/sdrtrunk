/*
 *
 *  * ******************************************************************************
 *  * Copyright (C) 2014-2019 Dennis Sheirer
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *  * *****************************************************************************
 *
 *
 */

package io.github.dsheirer.channel.state;

import io.github.dsheirer.audio.squelch.ISquelchStateProvider;
import io.github.dsheirer.audio.squelch.SquelchState;
import io.github.dsheirer.channel.metadata.ChannelMetadata;
import io.github.dsheirer.controller.channel.Channel;
import io.github.dsheirer.controller.channel.ChannelEvent;
import io.github.dsheirer.controller.channel.IChannelEventProvider;
import io.github.dsheirer.identifier.IdentifierUpdateNotification;
import io.github.dsheirer.identifier.IdentifierUpdateProvider;
import io.github.dsheirer.module.Module;
import io.github.dsheirer.module.decode.event.IDecodeEvent;
import io.github.dsheirer.module.decode.event.IDecodeEventProvider;
import io.github.dsheirer.sample.IOverflowListener;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.source.ISourceEventProvider;
import io.github.dsheirer.source.SourceEvent;
import io.github.dsheirer.source.heartbeat.Heartbeat;
import io.github.dsheirer.source.heartbeat.IHeartbeatListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public abstract class AbstractChannelState extends Module implements IChannelEventProvider, IDecodeEventProvider, IDecoderStateEventProvider, ISourceEventProvider, IHeartbeatListener, ISquelchStateProvider, IdentifierUpdateProvider, IOverflowListener
{
    private final static Logger mLog = LoggerFactory.getLogger(AbstractChannelState.class);

    protected State mState = State.IDLE;
    protected Listener<ChannelEvent> mChannelEventListener;
    protected Listener<IDecodeEvent> mDecodeEventListener;
    protected Listener<DecoderStateEvent> mDecoderStateListener;
    protected Listener<SquelchState> mSquelchStateListener;
    protected Listener<SourceEvent> mExternalSourceEventListener;
    protected Channel mChannel;
    protected IdentifierUpdateNotificationProxy mIdentifierUpdateNotificationProxy = new IdentifierUpdateNotificationProxy();
    protected long mFadeTimeout;
    protected long mEndTimeout;
    protected boolean mSourceOverflow = false;
    private HeartbeatReceiver mHeartbeatReceiver = new HeartbeatReceiver();

    public AbstractChannelState(Channel channel)
    {
        mChannel = channel;
    }


    public abstract Collection<ChannelMetadata> getChannelMetadata();

    @Override
    public void setIdentifierUpdateListener(Listener<IdentifierUpdateNotification> listener)
    {
        mIdentifierUpdateNotificationProxy.setListener(listener);
    }

    @Override
    public void removeIdentifierUpdateListener()
    {
        mIdentifierUpdateNotificationProxy.removeListener();
    }

    public abstract State getState();

    /**
     * Sets the squelch state listener
     */
    @Override
    public void setSquelchStateListener(Listener<SquelchState> listener)
    {
        mSquelchStateListener = listener;
    }

    /**
     * Removes the squelch state listener
     */
    @Override
    public void removeSquelchStateListener()
    {
        mSquelchStateListener = null;
    }

    /**
     * Receiver inner class that implements the IHeartbeatListener interface to receive heartbeat messages.
     */
    @Override
    public Listener<Heartbeat> getHeartbeatListener()
    {
        return mHeartbeatReceiver;
    }

    /**
     * This method is invoked if the source buffer provider goes into overflow state.  Since this is an external state,
     * we use the mSourceOverflow variable to override the internal state reported to external listeners.
     *
     * @param overflow true to indicate an overflow state
     */
    @Override
    public void sourceOverflow(boolean overflow)
    {
        mSourceOverflow = overflow;
    }

    /**
     * Indicates if this channel's sample buffer is in overflow state, meaning that the inbound sample
     * stream is not being processed fast enough and samples are being thrown away until the processing can
     * catch up.
     *
     * @return true if the channel is in overflow state.
     */
    public boolean isOverflow()
    {
        return mSourceOverflow;
    }

    protected abstract void processFadeState();

    protected abstract void processIdleState();

    protected abstract void processTeardownState();

    @Override
    public void setChannelEventListener(Listener<ChannelEvent> listener)
    {
        mChannelEventListener = listener;
    }

    @Override
    public void removeChannelEventListener()
    {
        mChannelEventListener = null;
    }

    @Override
    public void addDecodeEventListener(Listener<IDecodeEvent> listener)
    {
        mDecodeEventListener = listener;
    }

    @Override
    public void removeDecodeEventListener(Listener<IDecodeEvent> listener)
    {
        mDecodeEventListener = null;
    }

    /**
     * Adds a decoder state event listener
     */
    @Override
    public void setDecoderStateListener(Listener<DecoderStateEvent> listener)
    {
        mDecoderStateListener = listener;
    }

    /**
     * Removes the decoder state event listener
     */
    @Override
    public void removeDecoderStateListener()
    {
        mDecoderStateListener = null;
    }

    /**
     * Registers the listener to receive source events from the channel state
     */
    @Override
    public void setSourceEventListener(Listener<SourceEvent> listener)
    {
        mExternalSourceEventListener = listener;
    }

    /**
     * De-Registers a listener from receiving source events from the channel state
     */
    @Override
    public void removeSourceEventListener()
    {
        mExternalSourceEventListener = null;
    }

    /**
     * Processes periodic heartbeats received from the processing chain to perform state monitoring and cleanup
     * functions.
     *
     * Monitors decoder state events to automatically transition the channel state to IDLE (standard channel) or to
     * TEARDOWN (traffic channel) when decoding stops or the monitored channel returns to a no signal state.
     *
     * Provides a FADE transition state to allow for momentary decoding dropouts and to allow the user access to call
     * details for a fade period upon call end.
     */
    public class HeartbeatReceiver implements Listener<Heartbeat>
    {
        @Override
        public void receive(Heartbeat heartbeat)
        {
            try
            {
                if(State.CALL_STATES.contains(mState) && mFadeTimeout <= System.currentTimeMillis())
                {
                    processFadeState();
                }
                else if(mState == State.FADE && mEndTimeout <= System.currentTimeMillis())
                {
                    if(mChannel.isTrafficChannel())
                    {
                        processTeardownState();
                    }
                    else
                    {
                        processIdleState();
                    }
                }
            }
            catch(Throwable e)
            {
                mLog.error("An error occurred while state monitor was running " +
                    "- state [" + getState() +
                    "] current [" + System.currentTimeMillis() +
                    "] mResetTimeout [" + mEndTimeout +
                    "] mFadeTimeout [" + mFadeTimeout +
                    "]", e);
            }
        }
    }

    /**
     * Proxy between the identifier collection and the external update notification listener.  This proxy enables
     * access to internal components to broadcast silent identifier update notifications externally.
     */
    public class IdentifierUpdateNotificationProxy implements Listener<IdentifierUpdateNotification>
    {
        private Listener<IdentifierUpdateNotification> mIdentifierUpdateNotificationListener;

        @Override
        public void receive(IdentifierUpdateNotification identifierUpdateNotification)
        {
            if(mIdentifierUpdateNotificationListener != null)
            {
                mIdentifierUpdateNotificationListener.receive(identifierUpdateNotification);
            }
        }

        public void setListener(Listener<IdentifierUpdateNotification> listener)
        {
            mIdentifierUpdateNotificationListener = listener;
        }

        public void removeListener()
        {
            mIdentifierUpdateNotificationListener = null;
        }
    }
}
