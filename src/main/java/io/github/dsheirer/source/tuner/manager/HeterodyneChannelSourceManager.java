/*
 * *****************************************************************************
 * Copyright (C) 2014-2024 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * ****************************************************************************
 */
package io.github.dsheirer.source.tuner.manager;

import io.github.dsheirer.buffer.NativeSampleDelayBuffer;
import io.github.dsheirer.controller.channel.event.ChannelStopProcessingRequest;
import io.github.dsheirer.dsp.filter.design.FilterDesignException;
import io.github.dsheirer.eventbus.MyEventBus;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.source.SourceEvent;
import io.github.dsheirer.source.SourceException;
import io.github.dsheirer.source.tuner.TunerController;
import io.github.dsheirer.source.tuner.channel.ChannelSpecification;
import io.github.dsheirer.source.tuner.channel.HalfBandTunerChannelSource;
import io.github.dsheirer.source.tuner.channel.TunerChannel;
import io.github.dsheirer.source.tuner.channel.TunerChannelSource;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Channel provider for heterodyne and decimate method of channel provisioning.
 */
public class HeterodyneChannelSourceManager extends ChannelSourceManager
{
    private final static Logger mLog = LoggerFactory.getLogger(HeterodyneChannelSourceManager.class);

    private final static int DELAY_BUFFER_DURATION_MILLISECONDS = 2000;

    private List<HalfBandTunerChannelSource> mChannelSources = new CopyOnWriteArrayList<>();
    private SortedSet<TunerChannel> mTunerChannels = new TreeSet<>();
    private TunerController mTunerController;
    private ChannelSourceEventProcessor mChannelSourceEventProcessor = new ChannelSourceEventProcessor();
    private NativeSampleDelayBuffer mSampleDelayBuffer;


    private boolean mRunning = true;


    public HeterodyneChannelSourceManager(TunerController tunerController, String tunerId)
    {
        mTunerId = tunerId;
        mTunerController = tunerController;
        mTunerController.addListener(this);
    }

    @Override
    public String getStateDescription()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Heterodyne Channel Source Manager Providing [").append(mTunerChannels.size()).append("] Channels");
        sb.append("\n\tTuner Controller Frequency: ").append(mTunerController.getFrequency());
        for(HalfBandTunerChannelSource channelSource: mChannelSources)
        {
            sb.append("\n\tChannel [").append(channelSource.getTunerChannel())
                    .append("] Frequency [").append(channelSource.getFrequency())
                    .append("] Mixer [").append(channelSource.getMixerFrequency())
                    .append("]");
            sb.append(" HASH:").append(Integer.toHexString(channelSource.hashCode()).toUpperCase());
        }

        return sb.toString();
    }

    @Override
    public void stopAllChannels()
    {
        mRunning = false;

        List<TunerChannelSource> toStop = new ArrayList<>(mChannelSources);

        for(TunerChannelSource tunerChannelSource: toStop)
        {
            MyEventBus.getGlobalEventBus().post(new ChannelStopProcessingRequest(tunerChannelSource));
        }
    }

    @Override
    public SortedSet<TunerChannel> getTunerChannels()
    {
        return mTunerChannels;
    }

    @Override
    public int getTunerChannelCount()
    {
        return mTunerChannels.size();
    }

    @Override
    public TunerChannelSource getSource(TunerChannel tunerChannel, ChannelSpecification channelSpecification,
                                        String threadName)
    {
        if(!mRunning)
        {
            return null;
        }

        TunerChannelSource source = null;

        try
        {
            mTunerController.getLock().lock();
            if(CenterFrequencyCalculator.canTune(tunerChannel, mTunerController, mTunerChannels))
            {
                try
                {
                    //Attempt to create the channel source first, in case we get a filter design exception
                    HalfBandTunerChannelSource tunerChannelSource = new HalfBandTunerChannelSource(mChannelSourceEventProcessor,
                            tunerChannel, mTunerController.getSampleRate(), channelSpecification, threadName);

                    //Add to the list of channel sources so that it will receive the tuner frequency change
                    mChannelSources.add(tunerChannelSource);

                    //Set the current tuner frequency
                    tunerChannelSource.setFrequency(mTunerController.getFrequency());

                    //Add to the channel list and update the tuner center frequency as needed
                    mTunerChannels.add(tunerChannel);
                    updateTunerFrequency();

                    //Lock the tuner controller frequency and sample rate
                    mTunerController.setLockedSampleRate(true);

                    broadcast(SourceEvent.channelCountChange(getTunerChannelCount()));

                    tunerChannelSource.setTunerId(mTunerId);

                    source = tunerChannelSource;
                }
                catch(FilterDesignException fde)
                {
                    mLog.error("Error creating CIC tuner channel source - couldn't design cleanup filter", fde);
                }
            }
        }
        finally
        {
            mTunerController.getLock().unlock();
        }

        return source;
    }

    @Override
    public void setErrorMessage(String errorMessage)
    {
        for(TunerChannelSource tunerChannelSource: mChannelSources)
        {
            tunerChannelSource.setError(errorMessage);
        }
    }

    /**
     * Calculates a new center frequency and updates the tuner center frequency
     */
    private void updateTunerFrequency()
    {
        if(!mTunerController.isTunedFor(getTunerChannels()))
        {
            long centerFrequency = CenterFrequencyCalculator.getCenterFrequency(mTunerController, getTunerChannels());

            if(centerFrequency == CenterFrequencyCalculator.INVALID_FREQUENCY)
            {
                mLog.error("Couldn't calculate center frequency for tuner and tuner channels");
                return;
            }

            if(centerFrequency != mTunerController.getFrequency())
            {
                try
                {
                    mTunerController.setFrequency(centerFrequency);
                }
                catch(SourceException se)
                {
                    mLog.error("Couldn't update tuner center frequency to " + centerFrequency, se);
                }
            }
        }
    }

    @Override
    public void process(SourceEvent tunerSourceEvent) throws SourceException
    {
        switch(tunerSourceEvent.getEvent())
        {
            case NOTIFICATION_FREQUENCY_CHANGE:
                //Tuner center frequency has changed - update channels
                updateTunerFrequency(tunerSourceEvent.getValue().longValue());

                //Clear the delay buffer since any delayed samples will be centered on the previous frequency
                if(mSampleDelayBuffer != null)
                {
                    mSampleDelayBuffer.clear();
                }
                break;
            case NOTIFICATION_FREQUENCY_CORRECTION_CHANGE:
                //The tuner is self-correcting for PPM error - relay to channels
                broadcastToChannels(tunerSourceEvent);
                break;
            case NOTIFICATION_SAMPLE_RATE_CHANGE:
            case NOTIFICATION_FREQUENCY_AND_SAMPLE_RATE_LOCKED:
            case NOTIFICATION_FREQUENCY_AND_SAMPLE_RATE_UNLOCKED:
                //no-op
                break;
            default:
                mLog.info("Unrecognized Source Event received from tuner: " + tunerSourceEvent);
        }
    }

    /**
     * Broadcasts the source event to any channel
     */
    private void broadcastToChannels(SourceEvent sourceEvent)
    {
        for(HalfBandTunerChannelSource channelSource : mChannelSources)
        {
            try
            {
                channelSource.process(sourceEvent);
            }
            catch(Exception e)
            {
                mLog.error("Error broadcasting source event to channel: " + sourceEvent);
            }
        }
    }

    /**
     * Updates all channel sources to use the new tuner center frequency
     *
     * @param tunerFrequency in hertz
     */
    private void updateTunerFrequency(long tunerFrequency)
    {
        for(HalfBandTunerChannelSource channelSource : mChannelSources)
        {
            channelSource.setFrequency(tunerFrequency);
        }
    }

    /**
     * Creates a complex sample delay buffer and registers it with the tuner controller to start the flow
     * of complex sample buffers from the tuner.
     */
    private void startDelayBuffer()
    {
        if(mSampleDelayBuffer == null)
        {
            long bufferDuration = mTunerController.getBufferDuration();

            if(bufferDuration <= 0)
            {
                bufferDuration = 1;
            }

            int delayBufferSize = (int)(DELAY_BUFFER_DURATION_MILLISECONDS / bufferDuration);
            mSampleDelayBuffer = new NativeSampleDelayBuffer(delayBufferSize, mTunerController.getBufferDuration());
            mTunerController.addBufferListener(mSampleDelayBuffer);
        }
    }

    /**
     * De-registers the complex sample delay buffer and disposes of any queued reusable buffers.
     */
    private void stopDelayBuffer()
    {
        if(mSampleDelayBuffer != null && !mSampleDelayBuffer.hasListeners())
        {
            mTunerController.removeBufferListener(mSampleDelayBuffer);
            mSampleDelayBuffer.dispose();
            mSampleDelayBuffer = null;
        }
    }

    /**
     * Processes channel source events
     */
    public class ChannelSourceEventProcessor implements Listener<SourceEvent>
    {
        @Override
        public void receive(SourceEvent sourceEvent)
        {
            switch(sourceEvent.getEvent())
            {
//TODO: protect start/stop processing with a reentrant lock
                case REQUEST_START_SAMPLE_STREAM:
                    if(sourceEvent.getSource() instanceof HalfBandTunerChannelSource)
                    {
                        startDelayBuffer();

                        //The start sample stream request contains a start timestamp and the delay buffer
                        //will preload the channel with delayed sample buffers that either contain the
                        //timestamp or occur later/newer than the timestamp.
                        mSampleDelayBuffer.addListener((HalfBandTunerChannelSource)sourceEvent.getSource(),
                                sourceEvent.getValue().longValue());
                    }
                    break;
                case REQUEST_STOP_SAMPLE_STREAM:
                    if(sourceEvent.getSource() instanceof HalfBandTunerChannelSource halfBandSource)
                    {
                        mSampleDelayBuffer.removeListener(halfBandSource);
                        stopDelayBuffer();
                        mChannelSources.remove(halfBandSource);
                        mTunerChannels.remove(halfBandSource.getTunerChannel());
                        halfBandSource.dispose();

                        //Unlock the tuner controller if there are no more channels
                        if(getTunerChannelCount() == 0)
                        {
                            mTunerController.setLockedSampleRate(false);
                        }
                        broadcast(SourceEvent.channelCountChange(getTunerChannelCount()));
                    }
                    break;
                case NOTIFICATION_MEASURED_FREQUENCY_ERROR_SYNC_LOCKED:
                    //Rebroadcast so that the tuner source can process this event
                    broadcast(sourceEvent);
                    break;
                case NOTIFICATION_CHANNEL_COUNT_CHANGE:
                    //Lock the tuner controller frequency & sample rate when we're processing channels
                    break;
                default:
                    mLog.info("Unrecognized Source Event received from channel: " + sourceEvent);
                    break;
            }
        }
    }

    public void setTunerName(String name){
        mTunerId = name;
    }
}
