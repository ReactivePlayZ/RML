package com.reactiveplayz.rml;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A {@link RMLType} subclass to represent date
 * <p>
 * It represents an immutable {@link LocalDate}
 * and returns it upon calling {@link #raw()}
 * </p>
 */
public final class RMLDate extends RMLType {

    private final LocalDate val;

    /**
     * Returns the stored {@link LocalDate} value
     * @return The stored {@link LocalDate} value
     */
    public LocalDate raw() {
        return val;
    }

    /**
     * Creates a RMLDate with a {@link LocalDate}
     * @param value The {@link LocalDate} to store
     */
    public RMLDate(LocalDate value) {
        this.val = value;
    }

    /**
     * Returns the stored {@link LocalDate} in the ISO-8601 Date format as a String.
     * <p>Uses {@link DateTimeFormatter#ISO_DATE}</p>
     * @return The stored {@link LocalDate} in the ISO-8601 Date format as a String
     */
    @Override
    public String toString() {
        return val.format(DateTimeFormatter.ISO_DATE);
    }
}
