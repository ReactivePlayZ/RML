package com.reactiveplayz.rml;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * A {@link RMLType} subclass to represent time
 * <p>
 * It represents an immutable {@link LocalTime}
 * and returns it upon calling {@link #raw()}
 * </p>
 * <p>
 *     It also stores an immutable offset ({@link ZoneOffset})
 *     that defaults to {@code UTC}
 * </p>
 */
public final class RMLTime extends RMLType {

    private final LocalTime val;
    private final ZoneOffset offset;

    /**
     * Returns the stored {@link LocalTime} value
     * @return The stored {@link LocalTime} value
     */
    public LocalTime raw() {
        return val;
    }

    public ZoneOffset getOffset() {
        return offset;
    }

    /**
     * Creates a RMLTime with a {@link LocalTime}
     * and a {@link ZoneOffset} of {@code UTC}
     * @param value The {@link LocalTime} to store
     */
    public RMLTime(LocalTime value) {
        this(value, ZoneOffset.UTC);
    }

    /**
     * Creates a RMLTime with a {@link LocalTime}
     * and a specified {@link ZoneOffset}
     * @param value The {@link LocalTime} to store
     * @param offset The offset to the time stored
     */
    public RMLTime(LocalTime value, ZoneOffset offset) {
        this.val = value;
        this.offset = offset;
    }

    /**
     * Returns the stored {@link LocalTime} and {@link ZoneOffset}
     * as an {@link OffsetTime}
     * @return The stored {@link LocalTime} and {@link ZoneOffset}
     *         as an {@link OffsetTime}
     */
    public OffsetTime toOffsetTime() {
        return OffsetTime.of(val, offset);
    }

    /**
     * Returns the stored LocalTime and the stored offset in the
     * ISO-8601 extended offset time format as a String
     * <p>Note that {@code UTC} becomes {@code Z} for the offset</p>
     * @return The stored LocalTime and offset in the ISO-8601 extended offset
     *         time format as a String
     */
    @Override
    public String toString() {
        return val.format(DateTimeFormatter.ISO_TIME) + offset.toString();
    }
}
