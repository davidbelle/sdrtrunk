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
package io.github.dsheirer.source.tuner.channel;

import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.sample.complex.ComplexSamples;
import io.github.dsheirer.source.ComplexSource;
import io.github.dsheirer.source.ISourceEventProcessor;
import io.github.dsheirer.source.SourceEvent;
import io.github.dsheirer.source.SourceEventListenerToProcessorAdapter;
import io.github.dsheirer.source.SourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TunerChannelSource extends ComplexSource implements ISourceEventProcessor
{
    private final static Logger mLog = LoggerFactory.getLogger(TunerChannelSource.class);
    protected static final long HEARTBEAT_INTERVAL_MS = 100;
    private SourceEventListenerToProcessorAdapter mConsumerSourceEventListenerAdapter;
    protected TunerChannel mTunerChannel;
    private Listener<SourceEvent> mProducerSourceEventListener;
    private Listener<SourceEvent> mConsumerSourceEventListener;

//    private ScheduledIntervalProcessor mScheduledIntervalProcessor = new ScheduledIntervalProcessor();

    protected String mThreadName;


    /**
     * Tuner Channel Source is a Digital Drop Channel (DDC) abstract class that defines the minimum functionality
     * requirements for a channel sample source.
     *
     * @param producerSourceEventListener to receive source event requests (e.g. start/stop sample stream)
     * @param tunerChannel describing the desired channel frequency and bandwidth/minimum sample rate
     * @param threadName for the channel's dispatcher
     */
    public TunerChannelSource(Listener<SourceEvent> producerSourceEventListener, TunerChannel tunerChannel,
                              String threadName)
    {
        mProducerSourceEventListener = producerSourceEventListener;
        mTunerChannel = tunerChannel;
        mConsumerSourceEventListenerAdapter = new SourceEventListenerToProcessorAdapter(this);
        mThreadName = threadName;
    }

    @Override
    public long getFrequency()
    {
        return mTunerChannel.getFrequency();
    }

    private String mTunerID = "";
    public void setTunerId(String tunerID) {
        mTunerID = tunerID;
    }
    public String getTunerId() {
        return mTunerID;
    }

    /**
     * Signals that this tuner channel source has an error state so that any channel processing can be shutdown.
     * @param errorMessage describing the error
     */
    public void setError(String errorMessage)
    {
        broadcastConsumerSourceEvent(SourceEvent.errorState(this, errorMessage));
    }

    /**
     * Sets the center frequency for the sample streaming being sent from the producer.
     * @param frequency in hertz
     */
    public abstract void setFrequency(long frequency);

    /**
     * Sets the sample rate of the incoming sample stream from the producer
     * @param sampleRate in hertz
     */
    protected abstract void setSampleRate(double sampleRate);

    /**
     * Sets the listener to receive the complex buffer sample output from this channel
     * @param complexSamplesListener to receive complex buffers
     */
    public abstract void setListener(Listener<ComplexSamples> complexSamplesListener);

    /**
     * Commands sub-class to process queued samples and distribute them to the consumer.  This method will be invoked
     * by an interval timer.
     */
//    protected abstract void processSamples();

    /**
     * Tuner channel for this tuner channel source
     */
    public TunerChannel getTunerChannel()
    {
        return mTunerChannel;
    }

    /**
     * Starts this tuner channel source producing sample stream.
     */
    public void start()
    {
        //Broadcast current frequency and sample rate so consumer can configure correctly
        broadcastConsumerSourceEvent(SourceEvent.frequencyChange(this, getFrequency(), "Startup"));
        broadcastProducerSourceEvent(SourceEvent.startSampleStreamRequest(this));
        // maybe we don't need this?
        broadcastConsumerSourceEvent(SourceEvent.createTunerId(this, mTunerID));
    }

    /**
     * Stops this tuner channel source from producing a sample stream.  Note: tuner channel sources are one-time usage
     * only.  Invoking this method will also tell the source manager to dispose of this source.
     */
    public void stop()
    {
        broadcastProducerSourceEvent(SourceEvent.stopSampleStreamRequest(this));
    }

    @Override
    public void reset()
    {
        //Reset is not valid for a tuner channel source - ignored
    }

    /**
     * This method is invoked after the buffer processor is completely shutdown so that this instance can perform any
     * cleanup operations needed to dispose of this instance.
     */
    protected void performDisposal()
    {
        mProducerSourceEventListener = null;
        mConsumerSourceEventListener = null;
        mConsumerSourceEventListenerAdapter.dispose();
    }

    /**
     * Processes source event notifications received from the producer/parent and source event requests received from
     * the consumer.
     *
     * @param sourceEvent to process
     * @throws SourceException if there is an error while processing the source event
     */
    @Override
    public void process(SourceEvent sourceEvent) throws SourceException
    {
        switch(sourceEvent.getEvent())
        {
            case NOTIFICATION_FREQUENCY_CHANGE:
            case NOTIFICATION_FREQUENCY_AND_SAMPLE_RATE_LOCKED:
            case NOTIFICATION_FREQUENCY_AND_SAMPLE_RATE_UNLOCKED:
            case NOTIFICATION_FREQUENCY_CORRECTION_CHANGE:
            case NOTIFICATION_PLL_FREQUENCY:
            case NOTIFICATION_STOP_SAMPLE_STREAM:
                //no-op
                break;
            case NOTIFICATION_SAMPLE_RATE_CHANGE:
                setSampleRate(sourceEvent.getValue().doubleValue());
                break;
            case NOTIFICATION_MEASURED_FREQUENCY_ERROR:
                //Ignore these raw frequency measurement errors.  We're only interested in the measurements
                //that occur when the channel state indicates that we're sync-locked.
                break;
            case NOTIFICATION_MEASURED_FREQUENCY_ERROR_SYNC_LOCKED:
                //Rebroadcast this measurement event so the producer can process it
                sourceEvent.setSource(this);
                broadcastProducerSourceEvent(sourceEvent);
                break;
            case NOTIFICATION_FREQUENCY_ROTATION_FAILURE:
            case NOTIFICATION_FREQUENCY_ROTATION_SUCCESS:
            case NOTIFICATION_CHANNEL_POWER:
            case NOTIFICATION_SQUELCH_THRESHOLD:
            case NOTIFICATION_SQUELCH_AUTO_TRACK:
            case REQUEST_CHANGE_SQUELCH_THRESHOLD:
            case REQUEST_CURRENT_SQUELCH_THRESHOLD:
            case NOTIFICATION_TUNER_ID:
            case REQUEST_CHANGE_SQUELCH_AUTO_TRACK:
            case REQUEST_CURRENT_SQUELCH_AUTO_TRACK:
                //Ignore
                break;
            default:
                mLog.error("Ignoring unrecognized source event: " + sourceEvent.getEvent() + " from [" +
                    (sourceEvent.hasSource() ? sourceEvent.getSource().getClass() : "") + "]");
                break;
        }
    }

    /**
     * Broadcasts the source event to the incoming sample stream producer.
     * @param sourceEvent to broadcast
     */
    protected void broadcastProducerSourceEvent(SourceEvent sourceEvent)
    {
        if(mProducerSourceEventListener != null)
        {
            mProducerSourceEventListener.receive(sourceEvent);
        }
    }

    /**
     * Broadcasts the source event to the outbound sample stream consumer.
     * @param sourceEvent to broadcast
     */
    protected void broadcastConsumerSourceEvent(SourceEvent sourceEvent)
    {
        if(mConsumerSourceEventListener != null)
        {
            mConsumerSourceEventListener.receive(sourceEvent);
        }
    }

    /**
     * Implements the ISourceEventListener interface for receiving source events (ie requests) from a consumer.
     *
     * Note: source events are simply transferred to the process() method in this class.
     */
    @Override
    public Listener<SourceEvent> getSourceEventListener()
    {
        return mConsumerSourceEventListenerAdapter;
    }

    /**
     * Implements the ISourceEventProvider interface to set the consumer listener for source events.
     * @param listener to receive consumer source events
     */
    @Override
    public void setSourceEventListener(Listener<SourceEvent> listener)
    {
        mConsumerSourceEventListener = listener;
    }

    /**
     * Implements the ISourceEventProvider interface to remove the consumer listener for source events.
     */
    @Override
    public void removeSourceEventListener()
    {
        mConsumerSourceEventListener = null;
    }

//    /**
//     * Processor to periodically invoke buffer sample processing on an interval timer.  At each interval this processor
//     * sends a heartbeat to the registered consumer and then commands the sub-class implementation to process any
//     * queued buffers and distribute complex buffer sample(s) to the registered consumer.
//     */
//    public class ScheduledIntervalProcessor implements Runnable
//    {
//        private ScheduledFuture<?> mScheduledFuture;
//        private boolean mStopped = false;
//
//        /**
//         * Commands this processor to do a shutdown at the end of this or the next iteration.  Once successfully
//         * shutdown, it will invoke the performDisposal() method to cleanup this instance.
//         */
//        public void stop()
//        {
//            mStopped = true;
//        }
//
//        /**
//         * Starts buffer processing on a fixed interval using a scheduled thread pool
//         */
//        public void start()
//        {
//            if(mScheduledFuture == null)
//            {
//                mScheduledFuture = ThreadPool.SCHEDULED.scheduleAtFixedRate(this, 0,
//                    BUFFER_PROCESSOR_RUN_INTERVAL_MILLISECONDS, TimeUnit.MILLISECONDS);
//            }
//        }
//
//        /**
//         * Implementation of the Runnable interface to periodically send a heartbeat and then process buffer samples.
//         */
//        @Override
//        public void run()
//        {
//            try
//            {
//                if(!mStopped)
//                {
//                    try
//                    {
//                        getHeartbeatManager().broadcast();
//                    }
//                    catch(Throwable t)
//                    {
//                        mLog.error("Error while sending heartbeat", t);
//                    }
//                }
//
//                if(!mStopped)
//                {
//                    try
//                    {
//                        processSamples();
//                    }
//                    catch(Throwable t)
//                    {
//                        mLog.error("Error while processing samples", t);
//                    }
//                }
//
//                if(mStopped)
//                {
//                    if(mScheduledFuture != null)
//                    {
//                        //Set may-interrupt to false so that we can complete this iteration
//                        mScheduledFuture.cancel(false);
//                    }
//
//                    mScheduledFuture = null;
//
//                    try
//                    {
//                        getHeartbeatManager().broadcast();
//                        performDisposal();
//                    }
//                    catch(Throwable t)
//                    {
//                        mLog.error("Error during final shutdown processing of samples", t);
//                    }
//                }
//            }
//            catch(Throwable t)
//            {
//                mLog.error("Error during heartbeat processing", t);
//            }
//        }
//    }
}
