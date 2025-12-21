package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Comment extends Element {

    private ArrayList<String> comment = new ArrayList<>();

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
    public ArrayList<String> getComment() {
        return comment;
    }

    public JsonObject toJson() {
        JsonObject commentObj = new JsonObject();
        if (comment.size() == 1) {
            commentObj.addProperty("comment", this.comment.getLast().strip());
        }
        if (comment.size() > 1) {
            // When there are more than 1 comment, they are treated as multi-line
            // And therefore get converted into a JsonArray
            JsonArray commentArr = new JsonArray();
            for (String s : comment) {
                commentArr.add(s);
            }
            commentObj.add("comment", commentArr);

        }
        return commentObj;
    }

    Comment() {

    }

    Comment(String comment) {
        this.comment.add(comment);
    }
}
