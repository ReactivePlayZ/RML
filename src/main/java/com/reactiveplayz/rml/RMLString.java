package com.reactiveplayz.rml;

/**
 * A {@link RMLType} subclass to represent Strings
 * <p>
 * It represents an immutable String value
 * and returns it upon calling {@link #raw()}
 * </p>
 */
public final class RMLString extends RMLType {

    private final String val;

    /**
     * Returns the String value
     * @return The String value
     */
    public String raw() {
        return val;
    }

    /**
     * Creates a RMLString that represents a specified {@code String}
     * @param value The {@code String} to assign to this RMLString
     */
    public RMLString(String value) {
        this.val = value;
    }

    /**
     * Returns the stored String
     * @return The stored String
     * <p>Same as calling {@link #raw()}</p>
     */
    @Override
    public String toString() {
        return val;
    }
}
