package com.reactiveplayz.rml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A Class to store a collection of {@link RMLType}'s child classes for {@link Element} values
 * @param <T> The type that should be stored (A child class of {@link RMLType})
 */
public final class RMLValue<T extends RMLType> implements Iterable<T> {

    private final ArrayList<T> val = new ArrayList<>();

    /**
     * Appends the specified value to the end of this RMLValue
     * @param value The value to be appended to this RMLValue
     * @return {@code true} if this RMLValue changed as a result
     */
    public boolean add(T value) {
        return this.val.add(value);
    }

    /**
     * Adds a value as the first value of this RMLValue (optional operation).
     * After this operation completes normally, the given value will be a member
     * of this RMLValue, and it will be the first value in encounter order.
     * @param value The value to be added
     */
    public void addFirst(T value) {
        this.val.addFirst(value);
    }

    /**
     * Adds a value as the last value of this RMLValue (optional operation).
     * After this operation completes normally, the given value will be a member
     * of this RMLValue, and it will be the last value in encounter order.
     * @param value The value to be added
     */
    public void addLast(T value) {
        this.val.addLast(value);
    }

    /**
     * Returns the index of the first occurrence of the specified value in
     * this RMLValue, or -1 if this RMLValue does not contain the value
     * @param value Value to search for
     * @return The index of the first occurrence of the specified value
     *         in this list, or -1 if this list does not contain the value
     */
    public int indexOf(T value) {
        return this.val.indexOf(value);
    }

    /**
     * Returns the index of the last occurrence of the specified value in
     * this RMLValue, or -1 if this RMLValue does not contain the value
     * @param value Value to search for
     * @return The index of the last occurrence of the specified value
     *         in this list, or -1 if this list does not contain the value
     */
    public int lastIndexOf(T value) {
        return this.val.lastIndexOf(value);
    }

    /**
     * Removes the value at the specified position in this RMLValue.
     * Shifts any subsequent values to the left (subtracts one from their indices).
     * @param index The index of the value to be removed
     * @return The element that was removed from the list
     * @throws IndexOutOfBoundsException When the given {@code index} is
     *                                   either less than 0, or greater to
     *                                   or equal to the size of this RMLValue
     */
    public T remove(int index) {
        Objects.checkIndex(index, val.size());
        return this.val.remove(index);
    }

    /**
     * Returns the value at the specified position in this RMLValue
     * @param index The index of the value to return
     * @return The value at the specified position in this RMLValue
     * @throws IndexOutOfBoundsException When the given {@code index} is
     *                                   either less than 0, or greater to
     *                                   or equal to the size of this RMLValue
     */
    public T get(int index) {
        Objects.checkIndex(index, val.size());
        return this.val.get(index);
    }

    /**
     * Replaces the value at the specified position in
     * this RMLValue with the specified value
     * @param index Index of the value to replace
     * @param value Value to be stored at the specified position
     * @return The value previously at the specified position
     * @throws IndexOutOfBoundsException When the given {@code index} is
     *                                   either less than 0, or greater to
     *                                   or equal to the size of this RMLValue
     */
    public T set(int index, T value) {
        Objects.checkIndex(index, val.size());
        return this.val.set(index, value);
    }

    /**
     * Gets the last value of this RMLValue
     * @return The retrieved value
     * @throws NoSuchElementException When this RMLValue is empty
     */
    public T getLast() {
        if (val.isEmpty()) {
            throw new NoSuchElementException("The RMLValue is empty");
        }
        return this.val.getLast();
    }

    /**
     * Gets the first value of this RMLValue
     * @return The retrieved value
     * @throws NoSuchElementException When this RMLValue is empty
     */
    public T getFirst() {
        if (val.isEmpty()) {
            throw new NoSuchElementException("The RMLValue is empty");
        }
        return this.val.getFirst();
    }

    /**
     * Returns {@code true} if this RMLValue contains the specified value
     * @param o Value whose presence in this RMLValue is to be tested
     * @return {@code true} if this RMLValue contains the specified value
     */
    public boolean contains(T o) {
        return this.val.contains(o);
    }

    /**
     * Returns {@code true} if this RMLValue contains no elements
     * @return {@code true} if this RMLValue contains no elements
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

    @Override
    public String toString() {
        return toString("\n");
    }

    public String toString(char lineTerminator) {
        return toString(String.valueOf(lineTerminator));
    }

    public String toString(String lineTerminator) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < val.size(); i++) {
            String v = val.get(i).toString();
            switch (val.get(i)) {
                case RMLNumber _ -> v = "@num " + v;
                case RMLBoolean _ -> v = "@bool " + v;
                case RMLDate _ -> v = "@date " + v;
                case RMLTime _ -> v = "@time " + v;

                default -> {}
            }
            if (i==0) {
                out.append(v);
                continue;
            }
            out.append(lineTerminator).append(v);
        }

        return out.toString();
    }
}
