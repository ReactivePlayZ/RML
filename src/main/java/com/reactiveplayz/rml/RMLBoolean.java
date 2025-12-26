package com.reactiveplayz.rml;

/**
 * A {@link RMLType} subclass to represent boolean
 * <p>
 * It represents a {@code true} or {@code false} value
 * and returns it upon calling {@link #raw()}
 * </p>
 */
public final class RMLBoolean extends RMLType {

    private final Boolean val;

    /**
     * Returns the stored {@code boolean} value
     * @return The stored {@code boolean} value
     */
    public boolean raw() {
        return val;
    }

    public RMLBoolean(Boolean value) {
        this.val = value;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
