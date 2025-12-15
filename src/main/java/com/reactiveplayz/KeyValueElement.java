package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KeyValueElement extends Element {

    private String key;
    private String separater = ";";
    private ArrayList<String> value = new ArrayList<>();
    private ArrayList<String> comment = new ArrayList<>();

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

    public ArrayList<String> getValue() {
        return value;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    KeyValueElement() {
    }

    KeyValueElement(String key, String separater, String value) {
        this.key = key;
        this.separater = separater;
        this.value.add(value);
    }

    KeyValueElement(String key, String separater, String value, String comment) {
        this.key = key;
        this.separater = separater;
        this.value.add(value);
        this.comment.add(comment);
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
        kv.addProperty("separater", separater.strip());

        if (value.size() == 1) {
            kv.addProperty("value", value.getLast());
        }
        if (value.size() > 1) {
            JsonArray valueArr = new JsonArray();
            for (String s : value) {
                valueArr.add(s);
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
