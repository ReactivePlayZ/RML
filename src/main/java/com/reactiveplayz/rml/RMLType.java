package com.reactiveplayz.rml;

/**
 * An abstract class to group RMLTypes
 * <p>There are {@link RMLString}, {@link RMLNumber}, {@link RMLBoolean},
 * {@link RMLDate}, {@link RMLTime}, and {@link RMLDateTime}</p>
 * All RMLTypes are immutable
 */
public sealed abstract class RMLType permits RMLString, RMLNumber, RMLBoolean,
        RMLDate, RMLTime, RMLDateTime {

    /**
     * Returns the RMLType as a String
     * @return The RMLType as a String
     */
    public abstract String toString();
}
