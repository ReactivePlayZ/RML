package com.reactiveplayz.rml;

import java.util.Iterator;

/**
 * A RMLList is an {@link Element} that contains a non-empty list
 * of {@link RMLType}s
 * <p>(It has a syntax of {@code - listItem} in RML)</p>
 */
public class RMLList extends Element implements Iterable<RMLType> {

    private final RMLValue<RMLType> list = new RMLValue<>();
    private final Comment comment = new Comment();

    public RMLValue<RMLType> getList() {
        return list;
    }

    public Comment getComment() {
        return comment;
    }

    /**
     * Creates a RMLList with an initial value
     * @param firstItem The initial value
     */
    public RMLList(RMLType firstItem) {
        this.list.add(firstItem);
    }

    /**
     * Creates a RMLList with an initial value
     * and an initial Comment
     * @param firstItem The initial value
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
