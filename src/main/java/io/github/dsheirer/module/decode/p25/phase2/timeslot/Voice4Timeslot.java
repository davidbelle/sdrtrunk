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

package io.github.dsheirer.module.decode.p25.phase2.timeslot;

import io.github.dsheirer.bits.BinaryMessage;
import io.github.dsheirer.bits.CorrectedBinaryMessage;
import io.github.dsheirer.module.decode.p25.phase2.enumeration.ChannelNumber;
import io.github.dsheirer.module.decode.p25.phase2.enumeration.DataUnitID;

import java.util.ArrayList;
import java.util.List;

/**
 * Timeslot containing four voice frames and Encryption Sync Signalling (ESS-B) fragment.
 */
public class Voice4Timeslot extends AbstractVoiceTimeslot
{
    private static final int FRAME_LENGTH = 72;
    private static final int FRAME_1_START = 42;
    private static final int FRAME_2_START = 116;
    private static final int FRAME_3_START = 212;
    private static final int FRAME_4_START = 286;
    private static final int ESS_B_START = 188;
    private static final int ESS_B_LENGTH = 24;

    private List<BinaryMessage> mVoiceFrames;
    private BinaryMessage mEssB;

    /**
     * Constructs a 4-Voice timeslot
     *
     * @param message containing 320 scrambled bits for the timeslot
     * @param scramblingSequence to descramble the message
     * @param channelNumber of the timeslot
     * @param timestamp of the last transmitted bit
     */
    public Voice4Timeslot(CorrectedBinaryMessage message, BinaryMessage scramblingSequence, ChannelNumber channelNumber,
                          long timestamp)
    {
        super(message, DataUnitID.VOICE_4, scramblingSequence, channelNumber, timestamp);
    }

    /**
     * Voice frames contained in this timeslot
     */
    public List<BinaryMessage> getVoiceFrames()
    {
        if(mVoiceFrames == null)
        {
            mVoiceFrames = new ArrayList<>();
            mVoiceFrames.add(getMessage().getSubMessage(FRAME_1_START, FRAME_1_START + FRAME_LENGTH));
            mVoiceFrames.add(getMessage().getSubMessage(FRAME_2_START, FRAME_2_START + FRAME_LENGTH));
            mVoiceFrames.add(getMessage().getSubMessage(FRAME_3_START, FRAME_3_START + FRAME_LENGTH));
            mVoiceFrames.add(getMessage().getSubMessage(FRAME_4_START, FRAME_4_START + FRAME_LENGTH));
            return mVoiceFrames;
        }

        return mVoiceFrames;
    }

    /**
     * Encryption Synchronization Signaling (ESS-B) segment
     */
    public BinaryMessage getEssB()
    {
        if(mEssB == null)
        {
            mEssB = getMessage().getSubMessage(ESS_B_START, ESS_B_START + ESS_B_LENGTH);
        }

        return mEssB;
    }
}