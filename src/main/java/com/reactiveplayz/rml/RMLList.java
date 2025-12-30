package com.reactiveplayz.rml;

/**
 * A RMLList is an {@link Element} that contains a non-empty list
 * of {@link RMLType}s
 * <p>(It has a syntax of {@code - listItem} in RML)</p>
 */
public class RMLList extends Element {

    private final RMLValue<RMLType> list = new RMLValue<>();
    private final Comment comment = new Comment();

    public RMLValue<RMLType> getList() {
        return list;
    }

    public Comment getComment() {
        return comment;
    }

    public RMLList(RMLType firstItem) {
        this.list.add(firstItem);
    }

    public RMLList(RMLType firstItem, RMLString firstComment) {
        this(firstItem);
        if (firstComment.raw() != null) {
            this.comment.getCommentValue().add(firstComment);
        }
    }
}
