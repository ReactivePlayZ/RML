package com.reactiveplayz;

import com.google.gson.JsonObject;

public class Comment extends Element {

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String newComment) {
        this.comment = newComment;
    }

    public JsonObject toJson() {
        JsonObject commentObj = new JsonObject();
        commentObj.addProperty("comment", this.comment);
        return commentObj;
    }

    Comment() {

    }

    Comment(String comment) {
        this.comment = comment;
    }
}
