package com.reactiveplayz.rml;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A Comment is an {@link Element} and is similar to a {@link RMLList}
 * but it only stores {@link RMLString}. {@code null} can't be stored
 * in a Comment
 * <p>(It has a syntax of {@code // comment} in RML)</p>
 */
public class Comment extends Element implements Iterable<RMLString> {

    private final RMLValue<RMLString> comment = new RMLValue<>();

    /**
     * Returns this {@link Comment}'s {@link RMLValue}
     * @return This {@link Comment}'s {@link RMLValue}
     */
    public RMLValue<RMLString> getCommentValue() {
        return comment;
    }

    /**
     * Appends the specified String to the end of this Comment
     * <p>The String is converted into a {@link RMLString} first</p>
     * @param commentLine The String to be appended
     * @return {@code true} if this Comment changed as a result
     */
    public boolean add(String commentLine) {
        return comment.add(new RMLString(commentLine));
    }

    /**
     * Appends the specified {@link RMLString} to the end of this Comment
     * @param commentLine The RMLString to be appended
     * @return {@code true} if this Comment changed as a result
     */
    public boolean add(RMLString commentLine) {
        return comment.add(commentLine);
    }

    /**
     * Adds a String as the last value of this Comment (optional operation).
     * After this operation completes normally, the given value will be a member
     * of this Comment, and it will be the last value in encounter order.
     * <p>The String is converted into a {@link RMLString} first</p>
     * @param commentLine The value to be added
     */
    public void addLast(String commentLine) {
        comment.addLast(new RMLString(commentLine));
    }

    /**
     * Adds a {@link RMLString} as the last value of this Comment (optional operation).
     * After this operation completes normally, the given value will be a member
     * of this Comment, and it will be the last value in encounter order.
     * @param commentLine The value to be added
     */
    public void addLast(RMLString commentLine) {
        comment.addLast(commentLine);
    }

    /**
     * Returns the value at the specified position in this Comment
     * @param index The index of the value to return
     * @return The value at the specified position in this Comment
     * @throws IndexOutOfBoundsException
     */
    public RMLString get(int index) {
        Objects.checkIndex(index, comment.size());
        return comment.get(index);
    }

    /**
     * Gets the first value of this Comment
     * @return THe retrieved value
     * @throws NoSuchElementException When this Comment is empty
     */
    public RMLString getFirst() {
        if (comment.isEmpty()) {
            throw new NoSuchElementException("The Comment is empty");
        }
        return comment.getFirst();
    }

    /**
     * Gets the last value of this Comment
     * @return THe retrieved value
     * @throws NoSuchElementException When this Comment is empty
     */
    public RMLString getLast() {
        if (comment.isEmpty()) {
            throw new NoSuchElementException("The Comment is empty");
        }
        return comment.getLast();
    }

    /**
     * Returns {@code true} if this Comment contains no {@link RMLString}s
     * @return {@code true} if this Comment contains no {@link RMLString}s
     */
    public boolean isEmpty() {
        return comment.isEmpty();
    }

    /**
     * Returns the index of the first occurrence of the specified
     * {@link RMLString} in this Comment, or -1 if this Comment does not contain the value
     * @param value Value to search for
     * @return The index of the first occurrence of the specified
     *         {@link RMLString} in this Comment, or -1 if this Comment does not contain the value
     */
    public int indexOf(RMLString value) {
        return comment.indexOf(value);
    }

    /**
     * Returns the index of the first occurrence of the specified
     * String (by converting it into a {@link RMLString}) in this Comment,
     * or -1 if this Comment does not contain the value
     * @param value Value to search for
     * @return The index of the first occurrence of the specified
     *         String (by converting it into a {@link RMLString}) in this Comment,
     *         or -1 if this Comment does not contain the value
     */
    public int indexOf(String value) {
        return comment.indexOf(new RMLString(value));
    }

    /**
     * Returns the index of the last occurrence of the specified
     * {@link RMLString} in this Comment, or -1 if this Comment does not contain the value
     * @param value Value to search for
     * @return The index of the last occurrence of the specified
     *         {@link RMLString} in this Comment, or -1 if this Comment does not contain the value
     */
    public int lastIndexOf(RMLString value) {
        return comment.lastIndexOf(value);
    }

    /**
     * Returns the index of the last occurrence of the specified
     * String (by converting it into a {@link RMLString}) in this Comment,
     * or -1 if this Comment does not contain the value
     * @param value Value to search for
     * @return The index of the last occurrence of the specified
     *         String (by converting it into a {@link RMLString}) in this Comment,
     *         or -1 if this Comment does not contain the value
     */
    public int lastIndexOf(String value) {
        return comment.lastIndexOf(new RMLString(value));
    }

    /**
     * Returns {@code true} if this Comment contains the specified {@link RMLString}
     * @param value Value whose presence in this Comment is to be tested
     * @return {@code true} if this Comment contains the specified {@link RMLString}
     */
    public boolean contains(RMLString value) {
        return comment.contains(value);
    }

    /**
     * Returns {@code true} if this Comment contains the specified
     * String (by converting it into a {@link RMLString})
     * @param value Value whose presence in this Comment is to be tested
     * @return {@code true} if this Comment contains the specified
     *         String (by converting it into a {@link RMLString})
     */
    public boolean contains(String value) {
        return comment.contains(new RMLString(value));
    }

    /**
     * Creates a Comment initially being empty
     */
    public Comment() {
    }

    /**
     * Same as constructing with {@link #Comment(RMLString)} except
     * it turns the provided {@code comment} String into a {@link RMLString}
     * @param comment The String to turn into {@link RMLString}
     */
    public Comment(String comment) {
        this(new RMLString(comment));
    }

    /**
     * Creates a Comment with an initial value
     * <p>{@code null} is ignored</p>
     * @param comment The initial value
     */
    public Comment(RMLString comment) {
        if (comment != null && comment.raw() != null) {
            this.comment.add(comment);
        }
    }

    /**
     * Returns the String representation of this Comment
     * in RML as a String
     * <p>Multi-line comments are separated by new lines ({@code \n})</p>
     * <p>Each line starts with a {@code //}</p>
     * @return The String representation of this Comment
     *         in RML as a String
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < comment.size(); i++) {
            String c = comment.get(i).raw();
            if (i==0) {
                out.append("// ").append(c);
                continue;
            }
            out.append("\n// ").append(c);
        }
        return out.toString();
    }

    @Override
    public Iterator<RMLString> iterator() {
        return comment.iterator();
    }
}
