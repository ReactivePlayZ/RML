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

    public RMLDate(LocalDate value) {
        this.val = value;
    }

    /**
     * Returns the stored LocalDate in the ISO Date format as a String
     * @return The stored LocalDate in the ISO Date format as a String
     */
    @Override
    public String toString() {
        return val.format(DateTimeFormatter.ISO_DATE);
    }
}
