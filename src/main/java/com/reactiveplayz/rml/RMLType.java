package com.reactiveplayz.rml;

/**
 * An abstract class to group RMLTypes
 * <p>There are {@link RMLString}, {@link RMLNumber}, {@link RMLBoolean},
 * {@link RMLDate}, {@link RMLTime}, and {@link RMLDateTime}</p>
 * All RMLTypes are immutable
 */
public sealed abstract class RMLType permits RMLString, RMLNumber, RMLBoolean,
        RMLDate, RMLTime, RMLDateTime {

    public abstract String toString();
}
