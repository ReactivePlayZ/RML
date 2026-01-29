package com.reactiveplayz.rml;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * A {@link RMLType} subclass to represent date and time together
 * <p>
 * It represents an immutable {@link LocalDateTime}
 * and returns it upon calling {@link #raw()}
 * </p>
 * <p>
 *     It also stores an immutable offset ({@link ZoneOffset})
 *     that defaults to {@code UTC}
 * </p>
 */
public final class RMLDateTime extends RMLType {

    private final LocalDateTime val;
    private final ZoneOffset offset;

    /**
     * Returns the stored {@link LocalDateTime} value
     * @return The stored {@link LocalDateTime} value
     */
    public LocalDateTime raw() {
        return val;
    }

    /**
     * Creates a RMLDateTime with a {@link LocalDateTime}
     * and a {@link ZoneOffset} of {@code UTC}
     * @param value The {@link LocalDateTime} to store
     */
    public RMLDateTime(LocalDateTime value) {
        this(value, ZoneOffset.UTC);
    }

    /**
     * Creates a RMLDateTime with a {@link LocalDateTime}
     * and a specified {@link ZoneOffset}
     * @param value The {@link LocalDateTime} to store
     * @param offset The offset to the time stored
     */
    public RMLDateTime(LocalDateTime value, ZoneOffset offset) {
        this.val = value;
        this.offset = offset;
    }

    /**
     * Creates a RMLDateTime from a {@link RMLDate}
     * and a {@link RMLTime} with the provided RMLTime's offset
     * @param rmlDate The {@link RMLDate} to use
     * @param rmlTime The {@link RMLTime} to use
     */
    public RMLDateTime(RMLDate rmlDate, RMLTime rmlTime) {
        this(LocalDateTime.of(rmlDate.raw(), rmlTime.raw()), rmlTime.getOffset());
    }

    /**
     * Creates a RMLDateTime from a {@link RMLDate}
     * and a {@link RMLTime} with a specified {@link ZoneOffset}
     * @param rmlDate The {@link RMLDate} to use
     * @param rmlTime The {@link RMLTime} to use
     * @param offset The offset to the time stored
     */
    public RMLDateTime(RMLDate rmlDate, RMLTime rmlTime, ZoneOffset offset) {
        this(LocalDateTime.of(rmlDate.raw(), rmlTime.raw()), offset);
    }

    /**
     * Returns the stored {@link ZoneOffset} value
     * @return The stored {@link ZoneOffset} value
     */
    public ZoneOffset getOffset() {
        return offset;
    }

    /**
     * Returns the stored {@link LocalDateTime} and {@link ZoneOffset}
     * as a {@link ZonedDateTime}
     * @return The stored {@link LocalDateTime} and {@link ZoneOffset}
     *         as a {@link ZonedDateTime}
     */
    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.of(val, offset);
    }

    /**
     * Returns the stored {@link LocalDateTime} as a {@link LocalDate}
     * @return The stored {@link LocalDateTime} as a {@link LocalDate}
     */
    public LocalDate getDate() {
        return val.toLocalDate();
    }

    /**
     * Returns the stored {@link LocalDateTime} as a {@link LocalTime}
     * @return The stored {@link LocalDateTime} as a {@link LocalTime}
     */
    public LocalTime getTime() {
        return val.toLocalTime();
    }

    /**
     * Returns the stored {@link LocalDateTime} as a {@link OffsetTime}
     * @return The stored {@link LocalDateTime} as a {@link OffsetTime}
     */
    public OffsetTime getTimeAsOffsetTime() {
        return OffsetTime.of(val.toLocalTime(), offset);
    }

    /**
     * Returns the stored {@link LocalDateTime} as a {@link RMLDate}
     * @return The stored {@link LocalDateTime} as a {@link RMLDate}
     */
    public RMLDate getDateAsRMLDate() {
        return new RMLDate(val.toLocalDate());
    }

    /**
     * Returns the stored {@link LocalDateTime} as a {@link RMLTime}
     * @return The stored {@link LocalDateTime} as a {@link RMLTime}
     */
    public RMLTime getTimeAsRMLTime() {
        return new RMLTime(val.toLocalTime(), offset);
    }

    /**
     * Returns the stored {@link LocalDateTime} and the stored offset in the
     * ISO-8601 extended offset date-time format.
     * <p>Uses {@link DateTimeFormatter#ISO_DATE_TIME}</p>
     * @return The stored {@link LocalDateTime} and the stored offset in the
     *         ISO-8601 extended offset date-time format
     */
    @Override
    public String toString() {
        return ZonedDateTime.of(val, offset).format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
