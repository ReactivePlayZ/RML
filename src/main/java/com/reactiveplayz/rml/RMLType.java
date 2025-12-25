package com.reactiveplayz.rml;

public sealed abstract class RMLType permits RMLString, RMLNumber, RMLBoolean, RMLDate, RMLTime, RMLDateTime {

    public abstract String toString();
}
