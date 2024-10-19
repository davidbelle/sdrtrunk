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

package io.github.dsheirer.source.tuner.sdrplay.api.v3_07;

import java.lang.foreign.*;
import java.util.function.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * struct {
 *     unsigned char hdrEnable;
 *     unsigned char biasTEnable;
 *     sdrplay_api_RspDx_AntennaSelectT antennaSel;
 *     unsigned char rfNotchEnable;
 *     unsigned char rfDabNotchEnable;
 * }
 * }
 */
public class sdrplay_api_RspDxParamsT {

    sdrplay_api_RspDxParamsT() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        sdrplay_api_h.C_CHAR.withName("hdrEnable"),
        sdrplay_api_h.C_CHAR.withName("biasTEnable"),
        MemoryLayout.paddingLayout(2),
        sdrplay_api_h.C_INT.withName("antennaSel"),
        sdrplay_api_h.C_CHAR.withName("rfNotchEnable"),
        sdrplay_api_h.C_CHAR.withName("rfDabNotchEnable"),
        MemoryLayout.paddingLayout(2)
    ).withName("$anon$31:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfByte hdrEnable$LAYOUT = (OfByte)$LAYOUT.select(groupElement("hdrEnable"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned char hdrEnable
     * }
     */
    public static final OfByte hdrEnable$layout() {
        return hdrEnable$LAYOUT;
    }

    private static final long hdrEnable$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned char hdrEnable
     * }
     */
    public static final long hdrEnable$offset() {
        return hdrEnable$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned char hdrEnable
     * }
     */
    public static byte hdrEnable(MemorySegment struct) {
        return struct.get(hdrEnable$LAYOUT, hdrEnable$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned char hdrEnable
     * }
     */
    public static void hdrEnable(MemorySegment struct, byte fieldValue) {
        struct.set(hdrEnable$LAYOUT, hdrEnable$OFFSET, fieldValue);
    }

    private static final OfByte biasTEnable$LAYOUT = (OfByte)$LAYOUT.select(groupElement("biasTEnable"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned char biasTEnable
     * }
     */
    public static final OfByte biasTEnable$layout() {
        return biasTEnable$LAYOUT;
    }

    private static final long biasTEnable$OFFSET = 1;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned char biasTEnable
     * }
     */
    public static final long biasTEnable$offset() {
        return biasTEnable$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned char biasTEnable
     * }
     */
    public static byte biasTEnable(MemorySegment struct) {
        return struct.get(biasTEnable$LAYOUT, biasTEnable$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned char biasTEnable
     * }
     */
    public static void biasTEnable(MemorySegment struct, byte fieldValue) {
        struct.set(biasTEnable$LAYOUT, biasTEnable$OFFSET, fieldValue);
    }

    private static final OfInt antennaSel$LAYOUT = (OfInt)$LAYOUT.select(groupElement("antennaSel"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_AntennaSelectT antennaSel
     * }
     */
    public static final OfInt antennaSel$layout() {
        return antennaSel$LAYOUT;
    }

    private static final long antennaSel$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_AntennaSelectT antennaSel
     * }
     */
    public static final long antennaSel$offset() {
        return antennaSel$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_AntennaSelectT antennaSel
     * }
     */
    public static int antennaSel(MemorySegment struct) {
        return struct.get(antennaSel$LAYOUT, antennaSel$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * sdrplay_api_RspDx_AntennaSelectT antennaSel
     * }
     */
    public static void antennaSel(MemorySegment struct, int fieldValue) {
        struct.set(antennaSel$LAYOUT, antennaSel$OFFSET, fieldValue);
    }

    private static final OfByte rfNotchEnable$LAYOUT = (OfByte)$LAYOUT.select(groupElement("rfNotchEnable"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned char rfNotchEnable
     * }
     */
    public static final OfByte rfNotchEnable$layout() {
        return rfNotchEnable$LAYOUT;
    }

    private static final long rfNotchEnable$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned char rfNotchEnable
     * }
     */
    public static final long rfNotchEnable$offset() {
        return rfNotchEnable$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned char rfNotchEnable
     * }
     */
    public static byte rfNotchEnable(MemorySegment struct) {
        return struct.get(rfNotchEnable$LAYOUT, rfNotchEnable$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned char rfNotchEnable
     * }
     */
    public static void rfNotchEnable(MemorySegment struct, byte fieldValue) {
        struct.set(rfNotchEnable$LAYOUT, rfNotchEnable$OFFSET, fieldValue);
    }

    private static final OfByte rfDabNotchEnable$LAYOUT = (OfByte)$LAYOUT.select(groupElement("rfDabNotchEnable"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned char rfDabNotchEnable
     * }
     */
    public static final OfByte rfDabNotchEnable$layout() {
        return rfDabNotchEnable$LAYOUT;
    }

    private static final long rfDabNotchEnable$OFFSET = 9;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned char rfDabNotchEnable
     * }
     */
    public static final long rfDabNotchEnable$offset() {
        return rfDabNotchEnable$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned char rfDabNotchEnable
     * }
     */
    public static byte rfDabNotchEnable(MemorySegment struct) {
        return struct.get(rfDabNotchEnable$LAYOUT, rfDabNotchEnable$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned char rfDabNotchEnable
     * }
     */
    public static void rfDabNotchEnable(MemorySegment struct, byte fieldValue) {
        struct.set(rfDabNotchEnable$LAYOUT, rfDabNotchEnable$OFFSET, fieldValue);
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

