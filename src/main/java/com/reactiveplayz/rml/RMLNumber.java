package com.reactiveplayz.rml;

import java.math.BigDecimal;

public final class RMLNumber extends RMLType {

    private final BigDecimal val;

    public BigDecimal raw() {
        return val;
    }

    RMLNumber() {
        this.val = null;
    }

    RMLNumber(BigDecimal val) {
        this.val = val;
    }

    RMLNumber(String val) {
        this.val = new BigDecimal(val);
    }

    RMLNumber(int val) {
        this.val = new BigDecimal(val);
    }

    RMLNumber(float val) {
        this.val = new BigDecimal(val);
    }

    RMLNumber(double val) {
        this.val = new BigDecimal(val);
    }

    RMLNumber(long val) {
        this.val = new BigDecimal(val);
    }

    @Override
    public String toString() {
        assert val != null;
        return val.toString();
    }
}
