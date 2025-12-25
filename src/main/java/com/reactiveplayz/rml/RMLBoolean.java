package com.reactiveplayz.rml;

public final class RMLBoolean extends RMLType {

    private final Boolean val;

    public boolean raw() {
        return val;
    }

    RMLBoolean(Boolean value) {
        this.val = value;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
