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
import static java.lang.foreign.ValueLayout.OfByte;
import static java.lang.foreign.ValueLayout.OfInt;

/**
 * {@snippet lang=c :
 * struct {
 *     int gRdB;
 *     unsigned char LNAstate;
 *     unsigned char syncUpdate;
 *     sdrplay_api_MinGainReductionT minGr;
 *     sdrplay_api_GainValuesT gainVals;
 * }
 * }
 */
public class sdrplay_api_GainT {

    sdrplay_api_GainT() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        sdrplay_api_h.C_INT.withName("gRdB"),
        sdrplay_api_h.C_CHAR.withName("LNAstate"),
        sdrplay_api_h.C_CHAR.withName("syncUpdate"),
        MemoryLayout.paddingLayout(2),
        sdrplay_api_h.C_INT.withName("minGr"),
        sdrplay_api_GainValuesT.layout().withName("gainVals")
    ).withName("$anon$60:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt gRdB$LAYOUT = (OfInt)$LAYOUT.select(groupElement("gRdB"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int gRdB
     * }
     */
    public static final OfInt gRdB$layout() {
        return gRdB$LAYOUT;
    }

    private static final long gRdB$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int gRdB
     * }
     */
    public static final long gRdB$offset() {
        return gRdB$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int gRdB
     * }
     */
    public static int gRdB(MemorySegment struct) {
        return struct.get(gRdB$LAYOUT, gRdB$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int gRdB
     * }
     */
    public static void gRdB(MemorySegment struct, int fieldValue) {
        struct.set(gRdB$LAYOUT, gRdB$OFFSET, fieldValue);
    }

    private static final OfByte LNAstate$LAYOUT = (OfByte)$LAYOUT.select(groupElement("LNAstate"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned char LNAstate
     * }
     */
    public static final OfByte LNAstate$layout() {
        return LNAstate$LAYOUT;
    }

    private static final long LNAstate$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned char LNAstate
     * }
     */
    public static final long LNAstate$offset() {
        return LNAstate$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned char LNAstate
     * }
     */
    public static byte LNAstate(MemorySegment struct) {
        return struct.get(LNAstate$LAYOUT, LNAstate$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned char LNAstate
     * }
     */
    public static void LNAstate(MemorySegment struct, byte fieldValue) {
        struct.set(LNAstate$LAYOUT, LNAstate$OFFSET, fieldValue);
    }

    private static final OfByte syncUpdate$LAYOUT = (OfByte)$LAYOUT.select(groupElement("syncUpdate"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned char syncUpdate
     * }
     */
    public static final OfByte syncUpdate$layout() {
        return syncUpdate$LAYOUT;
    }

    private static final long syncUpdate$OFFSET = 5;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned char syncUpdate
     * }
     */
    public static final long syncUpdate$offset() {
        return syncUpdate$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned char syncUpdate
     * }
     */
    public static byte syncUpdate(MemorySegment struct) {
        return struct.get(syncUpdate$LAYOUT, syncUpdate$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned char syncUpdate
     * }
     */
    public static void syncUpdate(MemorySegment struct, byte fieldValue) {
        struct.set(syncUpdate$LAYOUT, syncUpdate$OFFSET, fieldValue);
    }

    private static final OfInt minGr$LAYOUT = (OfInt)$LAYOUT.select(groupElement("minGr"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * sdrplay_api_MinGainReductionT minGr
     * }
     */
    public static final OfInt minGr$layout() {
        return minGr$LAYOUT;
    }

    private static final long minGr$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * sdrplay_api_MinGainReductionT minGr
     * }
     */
    public static final long minGr$offset() {
        return minGr$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * sdrplay_api_MinGainReductionT minGr
     * }
     */
    public static int minGr(MemorySegment struct) {
        return struct.get(minGr$LAYOUT, minGr$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * sdrplay_api_MinGainReductionT minGr
     * }
     */
    public static void minGr(MemorySegment struct, int fieldValue) {
        struct.set(minGr$LAYOUT, minGr$OFFSET, fieldValue);
    }

    private static final GroupLayout gainVals$LAYOUT = (GroupLayout)$LAYOUT.select(groupElement("gainVals"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * sdrplay_api_GainValuesT gainVals
     * }
     */
    public static final GroupLayout gainVals$layout() {
        return gainVals$LAYOUT;
    }

    private static final long gainVals$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * sdrplay_api_GainValuesT gainVals
     * }
     */
    public static final long gainVals$offset() {
        return gainVals$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * sdrplay_api_GainValuesT gainVals
     * }
     */
    public static MemorySegment gainVals(MemorySegment struct) {
        return struct.asSlice(gainVals$OFFSET, gainVals$LAYOUT.byteSize());
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * sdrplay_api_GainValuesT gainVals
     * }
     */
    public static void gainVals(MemorySegment struct, MemorySegment fieldValue) {
        MemorySegment.copy(fieldValue, 0L, struct, gainVals$OFFSET, gainVals$LAYOUT.byteSize());
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

