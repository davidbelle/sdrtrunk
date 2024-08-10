/*
 * *****************************************************************************
 * Copyright (C) 2014-2023 Dennis Sheirer
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

package io.github.dsheirer.module.decode.p25.audio;


import io.github.dsheirer.alias.Alias;
import io.github.dsheirer.alias.AliasList;
import io.github.dsheirer.audio.codec.mbe.MBECallSequence;
import io.github.dsheirer.audio.codec.mbe.MBECallSequenceRecorder;
import io.github.dsheirer.bits.BinaryMessage;
import io.github.dsheirer.identifier.Identifier;
import io.github.dsheirer.identifier.patch.PatchGroup;
import io.github.dsheirer.identifier.talkgroup.TalkgroupIdentifier;
import io.github.dsheirer.message.IMessage;
import io.github.dsheirer.module.decode.p25.phase1.message.P25Message;
import io.github.dsheirer.module.decode.p25.phase1.message.hdu.HDUMessage;
import io.github.dsheirer.module.decode.p25.phase1.message.hdu.HeaderData;
import io.github.dsheirer.identifier.encryption.EncryptionKeyIdentifier;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.LinkControlWord;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.motorola.LCMotorolaPatchGroupVoiceChannelUpdate;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.motorola.LCMotorolaPatchGroupVoiceChannelUser;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.standard.LCGroupVoiceChannelUpdateExplicit;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.standard.LCGroupVoiceChannelUser;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.standard.LCTelephoneInterconnectVoiceChannelUser;
import io.github.dsheirer.module.decode.p25.phase1.message.lc.standard.LCUnitToUnitVoiceChannelUser;
import io.github.dsheirer.module.decode.p25.phase1.message.ldu.EncryptionSyncParameters;
import io.github.dsheirer.module.decode.p25.phase1.message.ldu.LDU1Message;
import io.github.dsheirer.module.decode.p25.phase1.message.ldu.LDU2Message;
import io.github.dsheirer.module.decode.p25.phase1.message.ldu.LDUMessage;
import io.github.dsheirer.module.decode.p25.phase1.message.tdu.TDULinkControlMessage;
import io.github.dsheirer.module.decode.p25.phase1.message.tdu.TDUMessage;
import io.github.dsheirer.module.decode.p25.phase1.message.tsbk.TSBKMessage;
import io.github.dsheirer.preference.UserPreferences;
import io.github.dsheirer.preference.identifier.talkgroup.APCO25TalkgroupFormatter;
import io.github.dsheirer.sample.Listener;
import io.github.dsheirer.source.ISourceEventListener;
import io.github.dsheirer.source.SourceEvent;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDirectoryElement;


import java.util.ArrayList;
import java.util.List;


/**
 * P25 Phase 1 IMBE Frame recorder generates P25 call sequence recordings containing JSON representations of audio
 * frames, optional encryption and call identifiers.
 */
public class P25P1CallSequenceRecorder extends MBECallSequenceRecorder implements ISourceEventListener
{
    private final static Logger mLog = LoggerFactory.getLogger(P25P1CallSequenceRecorder.class);

    public static final String PROTOCOL = "APCO25-PHASE1";

    private MBECallSequence mCallSequence;

    /**
     * Constructs a P25-Phase2 MBE call sequence recorder.
     *
     * @param userPreferences to obtain the recording directory
     * @param channelFrequency for the channel to record
     * @param system defined by the user
     * @param site defined by the user
     */
    public P25P1CallSequenceRecorder(UserPreferences userPreferences, long channelFrequency, String system, String site)
    {
        super(userPreferences, channelFrequency, system, site);
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
            if (mTunerId != "") {
                mCallSequence.setTuner(mTunerId);
            }
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
        else if(message instanceof TDULinkControlMessage)
        {
            process((TDULinkControlMessage)message);
        }
        else if(message instanceof TDUMessage)
        {
            process((TDUMessage) message);
        }
        else if (message instanceof HDUMessage)
        {
            process((HDUMessage)message);
        }
    }


    private void process(TDULinkControlMessage tdulc)
    {
        process(tdulc.getLinkControlWord());
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

        if(lduMessage instanceof LDU1Message)
        {
            process((LDU1Message)lduMessage);
        }
        else if(lduMessage instanceof LDU2Message)
        {
            process((LDU2Message)lduMessage);
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

    private void process(TDUMessage message){

        if (mCallSequence != null){;
            mCallSequence.setTerminated(true);
        }
        flush();
    }

    private void process(LinkControlWord lcw)
    {
        if(lcw.isValid() && mCallSequence != null)
        {
            switch(lcw.getOpcode())
            {
                case GROUP_VOICE_CHANNEL_USER:
                    LCGroupVoiceChannelUser gvcu = (LCGroupVoiceChannelUser)lcw;
                    mCallSequence.setFromIdentifier(gvcu.getSourceAddress().toString());
                    mCallSequence.setToIdentifier(gvcu.getGroupAddress().toString());
                    mCallSequence.setCallType(CALL_TYPE_GROUP);
                    break;
                case UNIT_TO_UNIT_VOICE_CHANNEL_USER:
                    LCUnitToUnitVoiceChannelUser uuvcu = (LCUnitToUnitVoiceChannelUser)lcw;
                    mCallSequence.setFromIdentifier(uuvcu.getSourceAddress().toString());
                    mCallSequence.setToIdentifier(uuvcu.getTargetAddress().toString());
                    mCallSequence.setCallType(CALL_TYPE_INDIVIDUAL);
                    break;
                case GROUP_VOICE_CHANNEL_UPDATE_EXPLICIT:
                    LCGroupVoiceChannelUpdateExplicit gvcue = (LCGroupVoiceChannelUpdateExplicit)lcw;
                    mCallSequence.setToIdentifier(gvcue.getGroupAddress().toString());
                    mCallSequence.setCallType(CALL_TYPE_GROUP);
                    break;
                case TELEPHONE_INTERCONNECT_VOICE_CHANNEL_USER:
                    LCTelephoneInterconnectVoiceChannelUser tivcu = (LCTelephoneInterconnectVoiceChannelUser)lcw;
                    mCallSequence.setToIdentifier(tivcu.getAddress().toString());
                    mCallSequence.setCallType(CALL_TYPE_TELEPHONE_INTERCONNECT);
                    break;
                case MOTOROLA_PATCH_GROUP_ADD:
                    mCallSequence.setToIdentifier(getPatchedTalkgroups(lcw));
                    break;
                case MOTOROLA_PATCH_GROUP_VOICE_CHANNEL_USER:
                    LCMotorolaPatchGroupVoiceChannelUser mpgvcu = (LCMotorolaPatchGroupVoiceChannelUser)lcw;
                    mCallSequence.setFromIdentifier(mpgvcu.getSourceAddress().toString());
                    // mCallSequence.setToIdentifierPatch(mpgvcu.getGroupAddress().toString());
                    mCallSequence.setToIdentifier(mpgvcu.getGroupAddress().toString());
                    mCallSequence.setCallType(CALL_TYPE_GROUP);
                    break;
                case MOTOROLA_PATCH_GROUP_VOICE_CHANNEL_UPDATE:
                    LCMotorolaPatchGroupVoiceChannelUpdate mpgvcup = (LCMotorolaPatchGroupVoiceChannelUpdate)lcw;
                    // mCallSequence.setToIdentifierPatch(mpgvcup.getPatchGroup().toString());
                    mCallSequence.setToIdentifier(mpgvcup.getPatchGroup().toString());
                    mCallSequence.setCallType(CALL_TYPE_GROUP);
                    break;
                case CALL_TERMINATION_OR_CANCELLATION:
                case MOTOROLA_TALK_COMPLETE:
                    if (mTunerId != "") {
                        mCallSequence.setTuner(mTunerId);
                        mCallSequence.setTerminated(true);
                    }
                    writeCallSequence(mCallSequence);
                    mCallSequence = null;
                    break;
                default:
                    //mLog.debug("Unrecognized lcw Opcode: " + lcw.getOpcode().name() + " VENDOR:" + lcw.getVendor() +
                    //        " OPCODE:" + lcw.getOpcodeNumber());
            }
        }
    }


    private String getPatchedTalkgroups(LinkControlWord lcw) {

        List<Identifier> list = lcw.getIdentifiers();
        if (list.size() > 2) {
            mLog.debug("list size has more than 2 identifiers, this is goooooood:" + list.size());
        } else {
             // mLog.debug("Normal list size detected:" + list.size());
        }

        return "";
    /*
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for(TalkgroupIdentifier patchedGroup: patchGroup.getPatchedGroupIdentifiers())
        {
            // sb.append(APCO25TalkgroupFormatter.format(patchedGroup, format, fixedWidth));
            sb.append(patchedGroup.getValue());
            if(counter++ < patchGroup.getPatchedGroupIdentifiers().size() - 1)
            {
                sb.append(",");
            }
        }
        return sb.toString();
        */

    }


    private void process(LDU1Message ldu1Message)
    {
        mCallSequence.setLowSpeedData(ldu1Message.getLowSpeedData());
        process(ldu1Message.getLinkControlWord());
    }

    private void process(LDU2Message ldu2Message)
    {
        mCallSequence.setLowSpeedData(ldu2Message.getLowSpeedData());


        EncryptionSyncParameters parameters = ldu2Message.getEncryptionSyncParameters();

        if(parameters.isValid() && parameters.isEncryptedAudio())
        {
            mCallSequence.setEncrypted(true);
            mCallSequence.setEncryptionSyncParameters(parameters);
        }
    }

    @Override
    public Listener<SourceEvent> getSourceEventListener() {

        return new Listener<SourceEvent>()
        {
            @Override
            public void receive(SourceEvent sourceEvent)
            {
                if (sourceEvent.getEvent() == SourceEvent.Event.NOTIFICATION_TUNER_ID) {
                    mTunerId = sourceEvent.getEventDescription();
                }
            }
        };
    }

    private void process(HDUMessage hduMessage)
    {
        if(mCallSequence == null)
        {
            mCallSequence = new MBECallSequence(PROTOCOL);
        }

        HeaderData hd = hduMessage.getHeaderData();

        if (hd.isEncryptedAudio())
        {
            mCallSequence.setEncrypted(true);
            Phase2EncryptionSyncParameters esp = new Phase2EncryptionSyncParameters((EncryptionKeyIdentifier)hd.getEncryptionKey(), hd.getMessageIndicator());
            mCallSequence.setEncryptionSyncParameters(esp);
        }
    }
}
