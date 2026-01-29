package com.reactiveplayz.rml;

import java.math.BigDecimal;
import java.util.Iterator;

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
public class KeyValueElement extends Element implements Iterable<RMLType> {

    private final String key;
    private final RMLValue<RMLType> value = new RMLValue<>();
    private final Comment comment = new Comment();

    /**
     * Returns the {@code key} of this KeyValueElement
     * @return The {@code key} of this KeyValueElement
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the {@link RMLValue} stored in this KeyValueElement
     * @return The {@link RMLValue} stored in this KeyValueElement
     */
    public RMLValue<RMLType> getValue() {
        return value;
    }

    /**
     * Returns the {@link Comment} of this KeyValueElement
     * @return The {@link Comment} of this KeyValueElement
     */
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
     * and an initial {@link RMLNumber} {@code value}
     * from a {@link BigDecimal}
     * @param key The key to refer to
     * @param value The value to use
     */
    public KeyValueElement(String key, BigDecimal value) {
        this.key = key;
        if (value != null) {
            this.value.add(new RMLNumber(value));
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

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLString} {@code value},
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLString value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.add(new RMLString(comment));
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLNumber} {@code value},
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLNumber value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.add(new RMLString(comment));
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLBoolean} {@code value},
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLBoolean value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.add(new RMLString(comment));
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLDate} {@code value},
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLDate value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.add(new RMLString(comment));
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLTime} {@code value},
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLTime value, String comment) {
        this(key, value);
        if (comment != null && !comment.isBlank()) {
            this.comment.add(new RMLString(comment));
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key} and
     * an initial {@link RMLType} {@code value}
     * which is converted into a valid storable type
     * (e.g. {@link RMLString})
     * @param key The key to refer to
     * @param value The first value to store
     */
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

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLType} {@code value}
     * which is converted into a valid storable type
     * (e.g. {@link RMLString}),
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLType value, String comment) {
        this(key, value);
        if (comment != null) {
            this.comment.add(new RMLString(comment.strip()));
        }
    }

    /**
     * Creates a KeyValueElement with a {@code key},
     * an initial {@link RMLType} {@code value}
     * which is converted into a valid storable type
     * (e.g. {@link RMLString}),
     * and an initial Comment (Ignores {@code null})
     * @param key The key to refer to
     * @param value The first value to store
     * @param comment The first comment to store
     */
    public KeyValueElement(String key, RMLType value, RMLString comment) {
        this(key, value);
        if (comment != null && comment.raw() != null) {
            this.comment.add(comment);
        }
    }

    /**
     * Returns the String representation of this KeyValueElement
     * in RML as a String
     * <p>Multi-line values are separated by new lines ({@code \n})</p>
     * @return The String representation of this KeyValueElement
     *         in RML as a String
     */
    @Override
    public String toString() {
        return key + ": " + value.toString("\n| ");
    }

    @Override
    public Iterator<RMLType> iterator() {
        return value.iterator();
    }
}
