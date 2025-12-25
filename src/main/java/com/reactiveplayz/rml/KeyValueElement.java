package com.reactiveplayz.rml;

public class KeyValueElement extends Element {

    private final String key;
    private final RMLValue<RMLType> value = new RMLValue<>();
    private final Comment comment = new Comment();

    public String getKey() {
        return key;
    }

    public RMLValue<RMLType> getValue() {
        return value;
    }

    public Comment getComment() {
        return comment;
    }

    KeyValueElement() {
        this.key = null;
    }

    KeyValueElement(String key, RMLString value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    KeyValueElement(String key, RMLNumber value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    KeyValueElement(String key, RMLDate value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    KeyValueElement(String key, RMLString value, String comment) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    KeyValueElement(String key, RMLNumber value, String comment) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    KeyValueElement(String key, RMLDate value, String comment) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    /**
     * @param key   String
     * @param value RMLType
     */
    KeyValueElement(String key, RMLType value) {
        this.key = key;
        if (value instanceof RMLString) {
            if (((RMLString) value).raw() != null && !((RMLString) value).raw().isBlank()) {
                this.value.add(((RMLString) value));
            }
            return;
        } else if (value instanceof RMLNumber) {
            if (((RMLNumber) value).raw() != null) {
                this.value.add(((RMLNumber) value));
            }
            return;
        } else if (value instanceof RMLDate) {
            if (((RMLDate) value).raw() != null) {
                this.value.add(((RMLDate) value));
            }
            return;
        }
        this.value.add(value);
    }

    /**
     * @param key     String
     * @param value   RMLType
     * @param comment String
     */
    KeyValueElement(String key, RMLType value, String comment) {
        this(key, value);
        if (comment != null) {
            this.comment.getCommentValue().add(new RMLString(comment.strip()));
        }
    }

    /**
     * @param key     String
     * @param value   RMLType
     * @param comment RMLString
     */
    KeyValueElement(String key, RMLType value, RMLString comment) {
        this(key, value);
        if (comment.raw() != null) {
            this.comment.getCommentValue().add(comment);
        }
    }

}
