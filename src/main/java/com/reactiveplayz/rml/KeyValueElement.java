package com.reactiveplayz.rml;

/**
 * A KeyValueElement is an {@link Element}
 * containing a {@code key}, {@code value} and a {@code comment}
 * <p>
 * The {@code value} and {@code comment} can be empty,
 * contain a single value, or contain multiple values
 * </p>
 * <p>The {@code key} is immutable</p>
 * <p>The {@code value} is a {@link RMLValue}{@code <}{@link RMLType}{@code >}
 * and the {@code comment} is a {@link Comment}</p>
 * (It has a syntax of {@code - key: value} in RML)
 */
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

    /**
     * Creates a blank KeyValueElement with a {@code null} key
     * (not recommended). Use {@linkplain #KeyValueElement(String)} instead.
     */
    KeyValueElement() {
        this.key = null;
    }

    /**
     * Creates a KeyValueElement with only a {@code key}
     * @param key The key to refer to
     */
    public KeyValueElement(String key) {
        this.key = key;
    }

    /**
     * Creates a KeyValueElement with a {@code key}
     * and an initial {@link RMLString} {@code value}
     * @param key The key to refer to
     * @param value The first value to store
     */
    public KeyValueElement(String key, RMLString value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key}
     * and an initial {@link RMLNumber} {@code value}
     * @param key The key to refer to
     * @param value The first value to store
     */
    public KeyValueElement(String key, RMLNumber value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key}
     * and an initial {@link RMLBoolean} {@code value}
     * @param key The key to refer to
     * @param value The first value to store
     */
    public KeyValueElement(String key, RMLBoolean value) {
        this.key = key;
        this.value.add(value);
    }

    /**
     * Creates a KeyValueElement with a {@code key}
     * and an initial {@link RMLDate} {@code value}
     * @param key The key to refer to
     * @param value The first value to store
     */
    public KeyValueElement(String key, RMLDate value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key}
     * and an initial {@link RMLTime} {@code value}
     * @param key The key to refer to
     * @param value The first value to store
     */
    public KeyValueElement(String key, RMLTime value) {
        this.key = key;
        if (value.raw() != null) {
            this.value.add(value);
        }
    }

    public KeyValueElement(String key, RMLString value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    public KeyValueElement(String key, RMLNumber value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    public KeyValueElement(String key, RMLBoolean value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    public KeyValueElement(String key, RMLDate value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    public KeyValueElement(String key, RMLTime value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.getCommentValue().add(new RMLString(comment));
        }
    }

    public KeyValueElement(String key, RMLType value) {
        this.key = key;
        if (value instanceof RMLString) {
            if (((RMLString) value).raw() != null && !((RMLString) value).raw().isBlank()) {
                this.value.add((value));
            }
            return;
        } else if (value instanceof RMLNumber) {
            if (((RMLNumber) value).raw() != null) {
                this.value.add((value));
            }
            return;
        } else if (value instanceof RMLDate) {
            if (((RMLDate) value).raw() != null) {
                this.value.add((value));
            }
            return;
        } else if (value instanceof RMLTime) {
            if (((RMLTime) value).raw() != null) {
                this.value.add(value);
            }
        }
        this.value.add(value);
    }

    public KeyValueElement(String key, RMLType value, String comment) {
        this(key, value);
        if (comment != null) {
            this.comment.getCommentValue().add(new RMLString(comment.strip()));
        }
    }

    public KeyValueElement(String key, RMLType value, RMLString comment) {
        this(key, value, comment.raw());
    }

}
