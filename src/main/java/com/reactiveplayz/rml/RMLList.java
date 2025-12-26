package com.reactiveplayz.rml;

/**
 * A RMLList is an {@link Element} that contains a non-empty list
 * of {@link RMLType}s
 * <p>(It has a syntax of {@code - listItem} in RML)</p>
 */
public class RMLList extends Element {

    private final RMLValue<RMLType> list = new RMLValue<>();

    public RMLValue<RMLType> getList() {
        return list;
    }

    public RMLList(RMLType firstItem) {
        this.list.add(firstItem);
    }
}
