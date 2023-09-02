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

// Generated by jextract

package io.github.dsheirer.source.tuner.sdrplay.api.v3_08;

import java.lang.foreign.*;

/**
 * {@snippet :
 * enum  (*sdrplay_api_SwapRspDuoMode_t)(struct * currDevice,struct ** deviceParams,enum  rspDuoMode,double sampleRate,enum  tuner,enum  bwType,enum  ifType,enum  tuner1AmPortSel);
 * }
 */
public interface sdrplay_api_SwapRspDuoMode_t {

    int apply(java.lang.foreign.MemorySegment currDevice, java.lang.foreign.MemorySegment deviceParams, int rspDuoMode, double sampleRate, int tuner, int bwType, int ifType, int tuner1AmPortSel);
    static MemorySegment allocate(sdrplay_api_SwapRspDuoMode_t fi, SegmentScope scope) {
        return RuntimeHelper.upcallStub(constants$6.sdrplay_api_SwapRspDuoMode_t_UP$MH, fi, constants$6.sdrplay_api_SwapRspDuoMode_t$FUNC, scope);
    }
    static sdrplay_api_SwapRspDuoMode_t ofAddress(MemorySegment addr, SegmentScope scope) {
        MemorySegment symbol = MemorySegment.ofAddress(addr.address(), 0, scope);
        return (java.lang.foreign.MemorySegment _currDevice, java.lang.foreign.MemorySegment _deviceParams, int _rspDuoMode, double _sampleRate, int _tuner, int _bwType, int _ifType, int _tuner1AmPortSel) -> {
            try {
                return (int)constants$6.sdrplay_api_SwapRspDuoMode_t_DOWN$MH.invokeExact(symbol, _currDevice, _deviceParams, _rspDuoMode, _sampleRate, _tuner, _bwType, _ifType, _tuner1AmPortSel);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


