package com.reactiveplayz.rml;

import java.time.LocalDate;

public final class RMLDate extends RMLType {

    private final LocalDate val;

    public LocalDate raw() {
        return val;
    }

    RMLDate(LocalDate value) {
        this.val = value;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
