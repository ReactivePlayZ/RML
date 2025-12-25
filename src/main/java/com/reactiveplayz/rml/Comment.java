package com.reactiveplayz.rml;

public class Comment extends Element {

    private final RMLValue<RMLString> comment = new RMLValue<>();

    /**
     * <p>
     * Note: comments are treated as a single String Object if there's only
     * one value during conversion.
     * </p>
     * <p>
     * However, it's converted to a JsonArray when there are more than one value.
     * </p>
     * See {@link #toJson()} for more info about Comment conversion to JSON.
     * 
     * @return Comment Object's comment {@code ArrayList<String>}
     */
    public RMLValue<RMLString> getCommentValue() {
        return comment;
    }

    Comment() {

    }

    Comment(RMLString comment) {
        this.comment.add(comment);
    }
}
