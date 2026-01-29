package com.reactiveplayz.rml;

import java.math.BigDecimal;

/**
 * A {@link RMLType} subclass to represent numbers
 * <p>
 * It represents an immutable {@link BigDecimal} value
 * and returns it upon calling {@link #raw()}
 * </p>
 */
public final class RMLNumber extends RMLType {

    private final BigDecimal val;

    /**
     * Returns the stored {@link BigDecimal} value
     * @return The stored {@link BigDecimal} value
     */
    public BigDecimal raw() {
        return val;
    }

    /** Creates a {@link RMLNumber} with a specific {@link BigDecimal} value */
    public RMLNumber(BigDecimal val) {
        this.val = val;
    }

    /** Creates a {@link RMLNumber} from a {@code String} value
     * of numbers
     */
    public RMLNumber(String val) {
        this.val = new BigDecimal(val);
    }

    /** Creates a {@link RMLNumber} from a {@code int} value */
    public RMLNumber(int val) {
        this.val = new BigDecimal(val);
    }

    /** Creates a {@link RMLNumber} from a {@code float} value */
    public RMLNumber(float val) {
        this.val = new BigDecimal(val);
    }

    /** Creates a {@link RMLNumber} from a {@code double} value */
    public RMLNumber(double val) {
        this.val = BigDecimal.valueOf(val);
    }

    /** Creates a {@link RMLNumber} from a {@code long} value */
    public RMLNumber(long val) {
        this.val = new BigDecimal(val);
    }


    /**
     * Returns the stored BigDecimal as a String
     * @return The stored BigDecimal as a String
     */
    @Override
    public String toString() {
        return val.toString();
    }
}
