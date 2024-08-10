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

// Generated by jextract

package io.github.dsheirer.source.tuner.sdrplay.api.v3_14;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.StructLayout;
import java.lang.invoke.VarHandle;
/**
 * {@snippet :
 * struct {
 *     float curr;
 *     float max;
 *     float min;
 * };
 * }
 */
public class sdrplay_api_GainValuesT {

    static final StructLayout $struct$LAYOUT = MemoryLayout.structLayout(
        Constants$root.C_FLOAT$LAYOUT.withName("curr"),
        Constants$root.C_FLOAT$LAYOUT.withName("max"),
        Constants$root.C_FLOAT$LAYOUT.withName("min")
    );
    public static MemoryLayout $LAYOUT() {
        return sdrplay_api_GainValuesT.$struct$LAYOUT;
    }
    static final VarHandle curr$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("curr"));
    public static VarHandle curr$VH() {
        return sdrplay_api_GainValuesT.curr$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * float curr;
     * }
     */
    public static float curr$get(MemorySegment seg) {
        return (float)sdrplay_api_GainValuesT.curr$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * float curr;
     * }
     */
    public static void curr$set(MemorySegment seg, float x) {
        sdrplay_api_GainValuesT.curr$VH.set(seg, x);
    }
    public static float curr$get(MemorySegment seg, long index) {
        return (float)sdrplay_api_GainValuesT.curr$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void curr$set(MemorySegment seg, long index, float x) {
        sdrplay_api_GainValuesT.curr$VH.set(seg.asSlice(index*sizeof()), x);
    }
    static final VarHandle max$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("max"));
    public static VarHandle max$VH() {
        return sdrplay_api_GainValuesT.max$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * float max;
     * }
     */
    public static float max$get(MemorySegment seg) {
        return (float)sdrplay_api_GainValuesT.max$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * float max;
     * }
     */
    public static void max$set(MemorySegment seg, float x) {
        sdrplay_api_GainValuesT.max$VH.set(seg, x);
    }
    public static float max$get(MemorySegment seg, long index) {
        return (float)sdrplay_api_GainValuesT.max$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void max$set(MemorySegment seg, long index, float x) {
        sdrplay_api_GainValuesT.max$VH.set(seg.asSlice(index*sizeof()), x);
    }
    static final VarHandle min$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("min"));
    public static VarHandle min$VH() {
        return sdrplay_api_GainValuesT.min$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * float min;
     * }
     */
    public static float min$get(MemorySegment seg) {
        return (float)sdrplay_api_GainValuesT.min$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * float min;
     * }
     */
    public static void min$set(MemorySegment seg, float x) {
        sdrplay_api_GainValuesT.min$VH.set(seg, x);
    }
    public static float min$get(MemorySegment seg, long index) {
        return (float)sdrplay_api_GainValuesT.min$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void min$set(MemorySegment seg, long index, float x) {
        sdrplay_api_GainValuesT.min$VH.set(seg.asSlice(index*sizeof()), x);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment ofAddress(MemorySegment addr, SegmentScope scope) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, scope); }
}


