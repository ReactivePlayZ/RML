package com.reactiveplayz;

import com.google.gson.JsonObject;

public class KeyValueElement extends Element {

    private String key;
    private String separater = ";";
    private String value;
    private String comment = null;

    public String getKey() {
        return key;
    }

    public void setKey(String newKey) {
        key = newKey;
    }

    public String getSeparater() {
        return separater;
    }

    public void setSeparater(String newSeparater) {
        separater = newSeparater;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String newValue) {
        value = newValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String newComment) {
        comment = newComment;
    }

    KeyValueElement() {
    }

    KeyValueElement(String key, String separater, String value) {
        this.key = key;
        this.separater = separater;
        this.value = value;
    }

    KeyValueElement(String key, String separater, String value, String comment) {
        this.key = key;
        this.separater = separater;
        this.value = value;
        this.comment = comment;
    }

    /**
     * Creates a JsonObject from a key-value line
     * 
     * @param line The String to turn into a JsonObject
     * @return {@code JsonObject} with fields of:
     *         {@code key},
     *         {@code separater},
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
            return null;
        }
        kv.addProperty("key", key.strip());
        kv.addProperty("separater", separater.strip());
        kv.addProperty("value", value.strip());
        kv.addProperty("comment", comment);
        return kv;
    }

}
