package com.reactiveplayz.rml;

import java.util.ArrayList;
import java.util.Iterator;

public final class RMLValue<T extends RMLType> implements Iterable<T> {

    private final ArrayList<T> val = new ArrayList<>();

    public void add(T value) {
        this.val.addLast(value);
    }

    public void remove(int index) {
        this.val.remove(index);
    }

    public RMLType get(int index) {
        return this.val.get(index);
    }

    public RMLType getLast() {
        return this.val.getLast();
    }

    public RMLType getFirst() {
        return this.val.getFirst();
    }

    public boolean contains(Object o) {
        return this.val.contains(o);
    }

    public boolean isEmpty() {
        return this.val.isEmpty();
    }

    public int size() {
        return this.val.size();
    }

    @Override
    public Iterator<T> iterator() {
        return val.iterator();
    }

}
