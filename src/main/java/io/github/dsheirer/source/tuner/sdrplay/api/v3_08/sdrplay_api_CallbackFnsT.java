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

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.StructLayout;
import java.lang.invoke.VarHandle;

/**
 * {@snippet :
 * struct {
 *     sdrplay_api_StreamCallback_t StreamACbFn;
 *     sdrplay_api_StreamCallback_t StreamBCbFn;
 *     sdrplay_api_EventCallback_t EventCbFn;
 * };
 * }
 */
public class sdrplay_api_CallbackFnsT {

    static final StructLayout $struct$LAYOUT = MemoryLayout.structLayout(
        Constants$root.C_POINTER$LAYOUT.withName("StreamACbFn"),
        Constants$root.C_POINTER$LAYOUT.withName("StreamBCbFn"),
        Constants$root.C_POINTER$LAYOUT.withName("EventCbFn")
    );
    public static MemoryLayout $LAYOUT() {
        return sdrplay_api_CallbackFnsT.$struct$LAYOUT;
    }
    static final VarHandle StreamACbFn$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("StreamACbFn"));
    public static VarHandle StreamACbFn$VH() {
        return sdrplay_api_CallbackFnsT.StreamACbFn$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * sdrplay_api_StreamCallback_t StreamACbFn;
     * }
     */
    public static MemorySegment StreamACbFn$get(MemorySegment seg) {
        return (java.lang.foreign.MemorySegment)sdrplay_api_CallbackFnsT.StreamACbFn$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * sdrplay_api_StreamCallback_t StreamACbFn;
     * }
     */
    public static void StreamACbFn$set(MemorySegment seg, MemorySegment x) {
        sdrplay_api_CallbackFnsT.StreamACbFn$VH.set(seg, x);
    }
    public static MemorySegment StreamACbFn$get(MemorySegment seg, long index) {
        return (java.lang.foreign.MemorySegment)sdrplay_api_CallbackFnsT.StreamACbFn$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void StreamACbFn$set(MemorySegment seg, long index, MemorySegment x) {
        sdrplay_api_CallbackFnsT.StreamACbFn$VH.set(seg.asSlice(index*sizeof()), x);
    }
    public static sdrplay_api_StreamCallback_t StreamACbFn(MemorySegment segment, SegmentScope scope) {
        return sdrplay_api_StreamCallback_t.ofAddress(StreamACbFn$get(segment), scope);
    }
    static final VarHandle StreamBCbFn$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("StreamBCbFn"));
    public static VarHandle StreamBCbFn$VH() {
        return sdrplay_api_CallbackFnsT.StreamBCbFn$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * sdrplay_api_StreamCallback_t StreamBCbFn;
     * }
     */
    public static MemorySegment StreamBCbFn$get(MemorySegment seg) {
        return (java.lang.foreign.MemorySegment)sdrplay_api_CallbackFnsT.StreamBCbFn$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * sdrplay_api_StreamCallback_t StreamBCbFn;
     * }
     */
    public static void StreamBCbFn$set(MemorySegment seg, MemorySegment x) {
        sdrplay_api_CallbackFnsT.StreamBCbFn$VH.set(seg, x);
    }
    public static MemorySegment StreamBCbFn$get(MemorySegment seg, long index) {
        return (java.lang.foreign.MemorySegment)sdrplay_api_CallbackFnsT.StreamBCbFn$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void StreamBCbFn$set(MemorySegment seg, long index, MemorySegment x) {
        sdrplay_api_CallbackFnsT.StreamBCbFn$VH.set(seg.asSlice(index*sizeof()), x);
    }
    public static sdrplay_api_StreamCallback_t StreamBCbFn(MemorySegment segment, SegmentScope scope) {
        return sdrplay_api_StreamCallback_t.ofAddress(StreamBCbFn$get(segment), scope);
    }
    static final VarHandle EventCbFn$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("EventCbFn"));
    public static VarHandle EventCbFn$VH() {
        return sdrplay_api_CallbackFnsT.EventCbFn$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * sdrplay_api_EventCallback_t EventCbFn;
     * }
     */
    public static MemorySegment EventCbFn$get(MemorySegment seg) {
        return (java.lang.foreign.MemorySegment)sdrplay_api_CallbackFnsT.EventCbFn$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * sdrplay_api_EventCallback_t EventCbFn;
     * }
     */
    public static void EventCbFn$set(MemorySegment seg, MemorySegment x) {
        sdrplay_api_CallbackFnsT.EventCbFn$VH.set(seg, x);
    }
    public static MemorySegment EventCbFn$get(MemorySegment seg, long index) {
        return (java.lang.foreign.MemorySegment)sdrplay_api_CallbackFnsT.EventCbFn$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void EventCbFn$set(MemorySegment seg, long index, MemorySegment x) {
        sdrplay_api_CallbackFnsT.EventCbFn$VH.set(seg.asSlice(index*sizeof()), x);
    }
    public static sdrplay_api_EventCallback_t EventCbFn(MemorySegment segment, SegmentScope scope) {
        return sdrplay_api_EventCallback_t.ofAddress(EventCbFn$get(segment), scope);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment ofAddress(MemorySegment addr, SegmentScope scope) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, scope); }
}


