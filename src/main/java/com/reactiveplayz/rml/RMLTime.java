package com.reactiveplayz.rml;

import java.time.LocalTime;

public final class RMLTime extends RMLType {

    private final LocalTime val;

    public LocalTime raw() {
        return val;
    }

    RMLTime(LocalTime value) {
        this.val = value;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
