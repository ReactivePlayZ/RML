package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KeyValueElement extends Element {

    private String key;
    private ArrayList<Object> value = new ArrayList<>();
    private ArrayList<String> comment = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String newKey) {
        key = newKey;
    }

    public ArrayList<Object> getValue() {
        return value;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    KeyValueElement() {
    }

    KeyValueElement(String key, Object value) {
        this.key = key;
        this.value.add(value);
    }

    KeyValueElement(String key, Object value, String comment) {
        this.key = key;
        this.value.add(value);
        this.comment.add(comment);
    }

    /**
     * @param key   String
     * @param value String or primitive type
     */
    KeyValueElement(Object key, Object value) {
        this((String) key, value);
    }

    /**
     * @param key       String
     * @param separater String
     * @param value     String or primitive type
     * @param comment   String
     */
    KeyValueElement(Object key, Object value, Object comment) {
        this((String) key, value, (String) comment);
    }

    /**
     * Creates a JsonObject from the KeyValueElement Object
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

        if (value.size() == 1) {
            if (value.getLast() instanceof String) {
                kv.addProperty("value", (String) value.getLast());
            } else if (value.getLast() instanceof Boolean) {
                kv.addProperty("value", ((boolean) value.getLast()));
            }
        }
        if (value.size() > 1) {
            JsonArray valueArr = new JsonArray();
            for (Object s : value) {
                if (s instanceof String) {
                    valueArr.add((String) s);
                } else if (s instanceof Boolean) {
                    valueArr.add((boolean) s);
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
            kv.add("value", commentArr);
        }

        return kv;
    }

}
