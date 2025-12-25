package com.reactiveplayz.rml;

public class RMLList extends Element {

    private final RMLValue<RMLType> list = new RMLValue<>();

    public RMLValue<RMLType> getList() {
        return list;
    }

    RMLList(RMLType firstItem) {
        this.list.add(firstItem);
    }
}
