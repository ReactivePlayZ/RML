package com.reactiveplayz.rml;

/**
 * A {@link RMLType} subclass to represent boolean
 * <p>
 * It represents a {@code true} or {@code false} value
 * and returns it upon calling {@link #raw()}
 * </p>
 */
public final class RMLBoolean extends RMLType {

    private final boolean val;

    /**
     * Returns the stored {@code boolean} value
     * @return The stored {@code boolean} value
     */
    public boolean raw() {
        return val;
    }

    /**
     * Creates a {@link RMLBoolean} from a {@code boolean}
     * value where the value can either be {@code true} or {@code false}
     * @param value The value to assign which is {@code boolean}
     */
    public RMLBoolean(Boolean value) {
        this.val = value;
    }

    /**
     * Returns the stored {@code boolean} as a String
     * @return The stored {@code boolean} as a String
     */
    @Override
    public String toString() {
        return ((Boolean) val).toString();
    }
}
