package com.reactiveplayz.rml;

/**
 * A Comment is an {@link Element} and is similar to a {@link RMLList}
 * but it only stores {@link RMLString}
 * <p>(It has a syntax of {@code // comment} in RML)</p>
 */
public class Comment extends Element {

    private final RMLValue<RMLString> comment = new RMLValue<>();

    /**
     * @return Comment's {@link #comment} as a
     * {@link RMLValue}{@code <}{@link RMLString}{@code >}
     */
    public RMLValue<RMLString> getCommentValue() {
        return comment;
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
        this.comment.add(new RMLString(comment));
    }

    /**
     * Creates a Comment with an initial value
     * @param comment The initial value
     */
    public Comment(RMLString comment) {
        this.comment.add(comment);
    }
}
