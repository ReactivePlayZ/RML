package com.reactiveplayz.rml;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A RMLList is an {@link Element} that contains a non-empty list
 * of {@link RMLType}s
 * <p>(It has a syntax of {@code - listItem} in RML)</p>
 */
public class RMLList extends Element implements Iterable<RMLType> {

    private final RMLValue<RMLType> list = new RMLValue<>();
    private final Comment comment = new Comment();

    /**
     * Returns {@code true} if this RMLList contains no items
     * @return {@code true} if this RMLList contains no items
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of items stored
     * @return The number of items stored
     */
    public int size() {
        return list.size();
    }

    /**
     * Appends the specified item to the end of this RMLList
     * @param item The item to be appended to this RMLList
     * @return {@code true} if this RMLList changed as a result
     */
    public boolean add(RMLType item) {
        return list.add(item);
    }

    /**
     * Adds an item as the first item of this RMLList (optional operation).
     * After this operation completes normally, the given item will be a member
     * of this RMLList, and it will be the first item in encounter order
     * @param item The item to be added
     */
    public void addFirst(RMLType item) {
        list.addFirst(item);
    }

    /**
     * Adds an item as the last item of this RMLList (optional operation).
     * After this operation completes normally, the given item will be a member
     * of this RMLList, and it will be the last item in encounter order
     * @param item The item to be added
     */
    public void addLast(RMLType item) {
        list.addLast(item);
    }

    /**
     * Replaces the item at the specified position in
     * this RMLList with the specified item
     * @param index Index of the item to replace
     * @param item Value to be stored at the specified position
     * @return The item previously at the specified position
     * @throws IndexOutOfBoundsException When the given {@code index} is
     *                                   either less than 0, or greater to
     *                                   or equal to the size of this RMLList
     */
    public RMLType set(int index, RMLType item) {
        Objects.checkIndex(index, list.size());
        return list.set(index, item);
    }

    /**
     * Removes the item at the specified position in this RMLList.
     * Shifts any subsequent items to the left (subtracts one from their indices).
     * @param index The index of the item to be removed
     * @return The item that was removed from the list
     * @throws IndexOutOfBoundsException When the given {@code index} is
     *                                   either less than 0, or greater to
     *                                   or equal to the size of this RMLList
     */
    public RMLType remove(int index) {
        Objects.checkIndex(index, list.size());
        return list.remove(index);
    }

    /**
     * Returns the item at the specified position in this RMLList
     * @param index The index of the item to return
     * @return The item at the specified position in this RMLList
     */
    public RMLType get(int index) {
        Objects.checkIndex(index, list.size());
        return list.get(index);
    }

    /**
     * Gets the first item of this RMLList
     * @return The retrieved item
     * @throws NoSuchElementException When this RMLList is empty
     */
    public RMLType getFirst() {
        if (list.isEmpty()) {
            throw new NoSuchElementException("The RMLList is empty");
        }
        return list.getFirst();
    }

    /**
     * Gets the last item of this RMLList
     * @return The retrieved item
     * @throws NoSuchElementException When this RMLList is empty
     */
    public RMLType getLast() {
        if (list.isEmpty()) {
            throw new NoSuchElementException("The RMLList is empty");
        }
        return list.getLast();
    }

    /**
     * Returns {@code true} if this RMLList contains the specified item
     * @param item Item whose presence in this RMLList is to be tested
     * @return {@code true} if this RMLList contains the specified item
     */
    public boolean contains(RMLType item) {
        return list.contains(item);
    }

    /**
     * Returns the index of the first occurrence of the specified item in
     * this RMLList, or -1 if this RMLList does not contain the item
     * @param item Item to search for
     * @return The index of the first occurrence of the specified item
     *         in this list, or -1 if this list does not contain the item
     */
    public int indexOf(RMLType item) {
        return list.indexOf(item);
    }

    /**
     * Returns the index of the last occurrence of the specified item in
     * this RMLList, or -1 if this RMLList does not contain the item
     * @param item Item to search for
     * @return The index of the last occurrence of the specified item
     *         in this list, or -1 if this list does not contain the item
     */
    public int lastIndexOf(RMLType item) {
        return list.lastIndexOf(item);
    }

    /**
     * Returns this RMLList's {@link RMLValue}
     * @return This RMLList's {@link RMLValue}
     */
    public RMLValue<RMLType> getList() {
        return list;
    }

    /**
     * Returns this RMLList's {@link Comment}
     * @return this RMLList's {@link Comment}
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Creates a RMLList with an initial item
     * @param firstItem The initial item
     */
    public RMLList(RMLType firstItem) {
        this.list.add(firstItem);
    }

    /**
     * Creates a RMLList with an initial item
     * and an initial Comment
     * @param firstItem The initial item
     * @param firstComment The initial comment ({@code null} is ignored)
     */
    public RMLList(RMLType firstItem, RMLString firstComment) {
        this(firstItem);
        if (firstComment.raw() != null) {
            this.comment.add(firstComment);
        }
    }

    /**
     * Returns the String representation of this RMLList
     * in RML as a String
     * <p>List items are separated by new lines ({@code \n})</p>
     * @return The String representation of this RMLList
     *         in RML as a String
     */
    @Override
    public String toString() {
        return list.toString("\n- ");
    }

    @Override
    public Iterator<RMLType> iterator() {
        return list.iterator();
    }
}
