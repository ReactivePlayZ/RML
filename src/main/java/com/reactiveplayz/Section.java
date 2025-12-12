package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Section extends Element {
    private ArrayList<Element> elements = new ArrayList<>();
    private String name;
    private String comment;

    public ArrayList<Element> getElements() {
        return elements;
    }

    public JsonObject toJson() {
        JsonArray elements = new JsonArray();
        for (Element e : this.elements) {
            elements.add(e.toJson());
        }
        JsonObject section = new JsonObject();
        section.addProperty("section_name", name);
        section.add("elements", elements);
        if (this.comment != null) {
            section.addProperty("comment", comment);
        }
        return section;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String newComment) {
        this.comment = newComment;
    }

    Section() {
    }

    Section(String name) {
        this.name = name;
    }
}