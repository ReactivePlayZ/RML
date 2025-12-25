package com.reactiveplayz.rml;

public final class RMLString extends RMLType {

    private final String val;

    public String raw() {
        return val;
    }

    RMLString(String value) {
        this.val = value;
    }

    @Override
    public String toString() {
        return val;
    }
}
