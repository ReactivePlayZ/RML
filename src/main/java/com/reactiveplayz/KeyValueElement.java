package com.reactiveplayz;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KeyValueElement extends Element {

    private final String key;
    private ArrayList<Object> value = new ArrayList<>();
    private ArrayList<String> comment = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public ArrayList<Object> getValue() {
        return value;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        if (value instanceof Float) {
            return new BigDecimal((Float) value);
        }
        if (value instanceof Double) {
            return new BigDecimal((Double) value);
        }
        return null;
    }

    KeyValueElement() {
        this.key = null;
    }

    /**
     * @param key   String
     * @param value String or primitive type
     */
    KeyValueElement(String key, Object value) throws IllegalArgumentException {
        this.key = key;
        if (toBigDecimal(value) != null) {
            value = toBigDecimal(value);
        }
        if (!(value instanceof String || value instanceof Boolean ||
                value instanceof BigDecimal || value instanceof LocalDate)) {
            throw new IllegalArgumentException(
                    "value is not a String or a primitive type but instead a " + value.getClass());
        }
        this.value.add(value);
    }

    /**
     * @param key     String
     * @param value   String or primitive type
     * @param comment String
     */
    KeyValueElement(String key, Object value, String comment) throws IllegalArgumentException {
        this(key, value);
        this.comment.add(comment);
    }

    /**
     * @param key     String
     * @param value   String or primitive type
     * @param comment String
     */
    KeyValueElement(String key, Object value, Comment comment) throws IllegalArgumentException {
        this(key, value);
        this.comment = comment.getComment();
    }

    /**
     * Turns a RML Key-Value line into a KeyValueElement Object
     * 
     * @param line The line to check for and turn into a KeyValueElement Object
     * @return KeyValueElement Object
     */
    public static KeyValueElement asKeyValueElement(String line) {
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(" //")));
        Matcher matcher = Identifier.KEYVALUE_PATTERN.matcher(splitLine.get(0));
        if (splitLine.size() == 1) {
            // There are no double forward slashes (//) in the line
            // So, the only value should be set to null
            // As it is used to return the comment text
            splitLine.set(0, null);
        }
        String jointComment = "";
        if (splitLine.size() > 2) {
            /*
             * If there are multiple double forward slashes (//)
             * within a comment, the line gets split into many groups
             * the first group is definitely the value, so we can start the loop
             * at index 1. The rest of the groups need to be joined with // added
             * at the end, except for the last group.
             */
            for (int i = 1; i < splitLine.size(); i++) {
                if (i == splitLine.size() - 1) {
                    // last index doesn't need a // at the end
                    jointComment += splitLine.get(i);
                    break;
                }
                jointComment += splitLine.get(i) + "//";
            }
            splitLine.set(splitLine.size() - 1, jointComment.strip());
        }
        if (matcher.find()) {
            if (Identifier.isBoolean(matcher.group(2))) {
                return new KeyValueElement(matcher.group(1), Identifier.booleanValue(matcher.group(2)),
                        splitLine.getLast());
            }
            if (Identifier.isNum(matcher.group(2))) {
                return new KeyValueElement(matcher.group(1), Identifier.numValue(matcher.group(2)),
                        splitLine.getLast());
            }
            if (Identifier.isDate(matcher.group(2))) {
                return new KeyValueElement(matcher.group(1), Identifier.dateValue(matcher.group(2)),
                        splitLine.getLast());
            }
            if (splitLine.getLast() != null) {
                return new KeyValueElement(matcher.group(1), matcher.group(2).strip(), splitLine.getLast().strip());
            }
            return new KeyValueElement(matcher.group(1), matcher.group(2).strip());
        }
        return new KeyValueElement();
    }

    /**
     * Creates a JsonObject from the KeyValueElement Object
     * 
     * @param line The String to turn into a JsonObject
     * @return {@code JsonObject} with fields of:
     *         {@code key},
     *         {@code value},
     *         and {@code comment}.
     *         <p>
     *         Each field can be null as well and therefore ignored during
     *         conversion
     *         </p>
     *         Useful for empty comments
     */
    public JsonObject toJson() {
        JsonObject kv = new JsonObject();
        if (key == null) {
            return kv;
        }
        kv.addProperty("key", key.strip());

        if (value.size() == 1) {
            if (value.getLast() instanceof String) {
                kv.addProperty("value", (String) value.getLast());
            } else if (value.getLast() instanceof Boolean) {
                kv.addProperty("value", ((boolean) value.getLast()));
            } else if (value.getLast() instanceof BigDecimal) {
                kv.addProperty("value", (BigDecimal) value.getLast());
            } else if (value.getLast() instanceof LocalDate) {
                kv.addProperty("value", ((LocalDate) value.getLast()).toString());
            }
        }
        if (value.size() > 1) {
            JsonArray valueArr = new JsonArray();
            for (Object s : value) {
                if (s instanceof String) {
                    valueArr.add(((String) s));
                } else if (s instanceof Boolean) {
                    valueArr.add((boolean) s);
                } else if (s instanceof BigDecimal) {
                    valueArr.add(((BigDecimal) s));
                } else if (s instanceof LocalDate) {
                    valueArr.add(((LocalDate) s).toString());
                }
            }
            kv.add("value", valueArr);
        }
        if (comment.size() == 1) {
            kv.addProperty("comment", comment.getLast());
        }
        if (comment.size() > 1) {
            JsonArray commentArr = new JsonArray();
            for (String s : comment) {
                commentArr.add(s);
            }
            kv.add("comment", commentArr);
        }

        return kv;
    }

}
