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

package io.github.dsheirer.source.tuner.sdrplay.api.v3_15;

import java.lang.foreign.Arena;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.function.Consumer;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;
import static java.lang.foreign.ValueLayout.OfInt;

/**
 * {@snippet lang=c :
 * struct {
 *     sdrplay_api_RspDx_HdrModeBwT hdrBw;
 * }
 * }
 */
public class sdrplay_api_RspDxTunerParamsT {

    sdrplay_api_RspDxTunerParamsT() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        sdrplay_api_h.C_INT.withName("hdrBw")
    ).withName("$anon$41:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt hdrBw$LAYOUT = (OfInt)$LAYOUT.select(groupElement("hdrBw"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_HdrModeBwT hdrBw
     * }
     */
    public static final OfInt hdrBw$layout() {
        return hdrBw$LAYOUT;
    }

    private static final long hdrBw$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_HdrModeBwT hdrBw
     * }
     */
    public static final long hdrBw$offset() {
        return hdrBw$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_HdrModeBwT hdrBw
     * }
     */
    public static int hdrBw(MemorySegment struct) {
        return struct.get(hdrBw$LAYOUT, hdrBw$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_HdrModeBwT hdrBw
     * }
     */
    public static void hdrBw(MemorySegment struct, int fieldValue) {
        struct.set(hdrBw$LAYOUT, hdrBw$OFFSET, fieldValue);
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

