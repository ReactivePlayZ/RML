package com.reactiveplayz.rml;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A Class to store {@link RMLType}'s child classes for {@link Element}s
 * @param <T> The type that should be stored (A child class of {@link RMLType})
 */
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

    /**
     * Returns {@code true} if this list contains no elements.
     * @return {@code true} if this list contains no elements.
     */
    public boolean isEmpty() {
        return this.val.isEmpty();
    }

    /**
     * Returns the number of values stored
     * @return the number of values stored
     */
    public int size() {
        return this.val.size();
    }

    @Override
    public Iterator<T> iterator() {
        return val.iterator();
    }

}
