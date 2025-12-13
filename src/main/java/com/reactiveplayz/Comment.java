package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Comment extends Element {

    private ArrayList<String> comment = new ArrayList<>();

    public ArrayList<String> getComment() {
        return comment;
    }

    public JsonObject toJson() {
        JsonObject commentObj = new JsonObject();
        if (comment.size() == 1) {
            commentObj.addProperty("comment", this.comment.getLast());
        }
        if (comment.size() > 1) {
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
