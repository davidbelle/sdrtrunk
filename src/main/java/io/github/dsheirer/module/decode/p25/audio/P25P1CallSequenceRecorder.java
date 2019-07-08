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

package io.github.dsheirer.module.decode.p25.audio;


import io.github.dsheirer.audio.codec.mbe.MBECallSequence;
import io.github.dsheirer.audio.codec.mbe.MBECallSequenceRecorder;
import io.github.dsheirer.bits.BinaryMessage;
import io.github.dsheirer.message.IMessage;
import io.github.dsheirer.module.decode.p25.phase1.message.P25Message;
import io.github.dsheirer.module.decode.p25.phase1.message.ldu.LDUMessage;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.MacMessage;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.MacStructure;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.EndPushToTalk;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.GroupVoiceChannelUserAbbreviated;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.GroupVoiceChannelUserExtended;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.PushToTalk;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.TelephoneInterconnectVoiceChannelUser;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.UnitToUnitVoiceChannelUserAbbreviated;
import io.github.dsheirer.module.decode.p25.phase2.message.mac.structure.UnitToUnitVoiceChannelUserExtended;
import io.github.dsheirer.preference.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * P25 Phase 1 IMBE Frame recorder generates P25 call sequence recordings containing JSON representations of audio
 * frames, optional encryption and call identifiers.
 */
public class P25P1CallSequenceRecorder extends MBECallSequenceRecorder
{
    private final static Logger mLog = LoggerFactory.getLogger(P25P1CallSequenceRecorder.class);

    private static final String PROTOCOL = "APCO25-PHASE1";

    private MBECallSequence mCallSequence;

    /**
     * Constructs a P25-Phase2 MBE call sequence recorder.
     *
     * @param userPreferences to obtain the recording directory
     * @param channelFrequency for the channel to record
     */
    public P25P1CallSequenceRecorder(UserPreferences userPreferences, long channelFrequency)
    {
        super(userPreferences, channelFrequency);
    }

    /**
     * Stops and flushes any partial frame sequence from the processors
     */
    @Override
    public void stop()
    {
        flush();
    }

    /**
     * Primary message interface for receiving frames and metadata messages to record
     */
    @Override
    public void receive(IMessage message)
    {
        if(message instanceof P25Message)
        {
            P25Message p25 = (P25Message)message;

            if(p25.isValid())
            {
                process(p25);
            }
        }
    }


    /**
     * Flushes any partial call sequence
     */
    public void flush()
    {
        if(mCallSequence != null)
        {
            writeCallSequence(mCallSequence);
            mCallSequence = null;
        }
    }

    /**
     * Processes any P25 Phase 1 message
     */
    public void process(P25Message message)
    {
        if(message instanceof LDUMessage)
        {
            process((LDUMessage)message);
        }
    }

    /**
     * Processes Voice LDU messages
     */
    private void process(LDUMessage lduMessage)
    {
        if(mCallSequence == null)
        {
            mCallSequence = new MBECallSequence(PROTOCOL);
        }

        List<byte[]> voiceFrames = lduMessage.getIMBEFrames();

        long baseTimestamp = lduMessage.getTimestamp();

        for(byte[] frame : voiceFrames)
        {
            BinaryMessage frameBits = BinaryMessage.from(frame);
            mCallSequence.addVoiceFrame(baseTimestamp, frameBits.toHexString());

            //Voice frames are 20 milliseconds each, so we increment the timestamp by 20 for each one
            baseTimestamp += 20;
        }
    }

    /**
     * Processes a P25 message
     */
    private void process(MacMessage macMessage)
    {
        switch(macMessage.getMacPduType())
        {
            case MAC_1_PTT:
            case MAC_4_ACTIVE:
                process(macMessage.getMacStructure(), true);
                break;
            case MAC_2_END_PTT:
            case MAC_6_HANGTIME:
                process(macMessage.getMacStructure(), false);
                break;
            case MAC_3_IDLE:
                flush();
                break;
        }

    }

    /**
     * Processes a mac structure message to obtain from/to identifiers and to optionally close the call sequence
     * when the MAC pdu indicates that the call sequence is no longer active
     *
     * @param mac
     * @param isActive
     */
    private void process(MacStructure mac, boolean isActive)
    {
        if(mCallSequence == null && isActive)
        {
            mCallSequence = new MBECallSequence(PROTOCOL);
        }

        if(mCallSequence != null)
        {
            switch(mac.getOpcode())
            {
                case PUSH_TO_TALK:
                    if(mac instanceof PushToTalk)
                    {
                        PushToTalk ptt = (PushToTalk)mac;

                        if(mCallSequence == null)
                        {
                            mCallSequence = new MBECallSequence(PROTOCOL);
                        }
                        mCallSequence.setFromIdentifier(ptt.getSourceAddress().toString());
                        mCallSequence.setToIdentifer(ptt.getGroupAddress().toString());

                        if(ptt.isEncrypted())
                        {
                            mCallSequence.setEncrypted(true);
                            mCallSequence.setEncryptionSyncParameters(ptt.getEncryptionSyncParameters());
                        }
                    }
                    else
                    {
                        mLog.warn("Expected push-to-talk structure but found: " + mac.getClass());
                    }
                    break;
                case END_PUSH_TO_TALK:
                    if(mac instanceof EndPushToTalk)
                    {
                        EndPushToTalk eptt = (EndPushToTalk)mac;

                        String source = eptt.getSourceAddress().toString();

                        if(source != null && !source.contentEquals("16777215"))
                        {
                            mCallSequence.setFromIdentifier(source);
                        }
                        mCallSequence.setToIdentifer(eptt.getGroupAddress().toString());
                        writeCallSequence(mCallSequence);
                        mCallSequence = null;
                    }
                    else
                    {
                        mLog.warn("Expected End push-to-talk structure but found: " + mac.getClass());
                    }
                    break;
                case TDMA_1_GROUP_VOICE_CHANNEL_USER_ABBREVIATED:
                    if(mac instanceof GroupVoiceChannelUserAbbreviated)
                    {
                        GroupVoiceChannelUserAbbreviated gvcua = (GroupVoiceChannelUserAbbreviated)mac;
                        mCallSequence.setFromIdentifier(gvcua.getSourceAddress().toString());
                        mCallSequence.setToIdentifer(gvcua.getGroupAddress().toString());
                        mCallSequence.setCallType(CALL_TYPE_GROUP);
                        if(gvcua.getServiceOptions().isEncrypted())
                        {
                            mCallSequence.setEncrypted(true);
                        }
                    }
                    else
                    {
                        mLog.warn("Expected group voice channel user abbreviated but found: " + mac.getClass());
                    }
                    break;
                case TDMA_2_UNIT_TO_UNIT_VOICE_CHANNEL_USER:
                    if(mac instanceof UnitToUnitVoiceChannelUserAbbreviated)
                    {
                        UnitToUnitVoiceChannelUserAbbreviated uuvcua = (UnitToUnitVoiceChannelUserAbbreviated)mac;
                        mCallSequence.setFromIdentifier(uuvcua.getSourceAddress().toString());
                        mCallSequence.setToIdentifer(uuvcua.getTargetAddress().toString());
                        mCallSequence.setCallType(CALL_TYPE_INDIVIDUAL);
                        if(uuvcua.getServiceOptions().isEncrypted())
                        {
                            mCallSequence.setEncrypted(true);
                        }
                    }
                    else
                    {
                        mLog.warn("Expected unit-2-unit voice channel user abbreviated but found: " + mac.getClass());
                    }
                    break;
                case TDMA_3_TELEPHONE_INTERCONNECT_VOICE_CHANNEL_USER:
                    if(mac instanceof TelephoneInterconnectVoiceChannelUser)
                    {
                        TelephoneInterconnectVoiceChannelUser tivcu = (TelephoneInterconnectVoiceChannelUser)mac;
                        mCallSequence.setToIdentifer(tivcu.getToOrFromAddress().toString());
                        mCallSequence.setCallType(CALL_TYPE_TELEPHONE_INTERCONNECT);
                        if(tivcu.getServiceOptions().isEncrypted())
                        {
                            mCallSequence.setEncrypted(true);
                        }
                    }
                    else
                    {
                        mLog.warn("Expected telephone interconnect voice channel user abbreviated but found: " + mac.getClass());
                    }
                    break;
                case TDMA_33_GROUP_VOICE_CHANNEL_USER_EXTENDED:
                    if(mac instanceof GroupVoiceChannelUserExtended)
                    {
                        GroupVoiceChannelUserExtended gvcue = (GroupVoiceChannelUserExtended)mac;
                        mCallSequence.setFromIdentifier(gvcue.getSourceAddress().toString());
                        mCallSequence.setToIdentifer(gvcue.getGroupAddress().toString());
                        mCallSequence.setCallType(CALL_TYPE_GROUP);
                        if(gvcue.getServiceOptions().isEncrypted())
                        {
                            mCallSequence.setEncrypted(true);
                        }
                    }
                    else
                    {
                        mLog.warn("Expected group voice channel user extended but found: " + mac.getClass());
                    }
                    break;
                case TDMA_34_UNIT_TO_UNIT_VOICE_CHANNEL_USER_EXTENDED:
                    if(mac instanceof UnitToUnitVoiceChannelUserExtended)
                    {
                        UnitToUnitVoiceChannelUserExtended uuvcue = (UnitToUnitVoiceChannelUserExtended)mac;
                        mCallSequence.setFromIdentifier(uuvcue.getSourceAddress().toString());
                        mCallSequence.setToIdentifer(uuvcue.getTargetAddress().toString());
                        mCallSequence.setCallType(CALL_TYPE_INDIVIDUAL);
                        if(uuvcue.getServiceOptions().isEncrypted())
                        {
                            mCallSequence.setEncrypted(true);
                        }
                    }
                    else
                    {
                        mLog.warn("Expected unit-2-unit voice channel user extended but found: " + mac.getClass());
                    }
                    break;
            }

            if(!isActive)
            {
                writeCallSequence(mCallSequence);
                mCallSequence = null;
            }
        }
    }
}
