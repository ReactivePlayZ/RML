package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Section extends Element {
    private ArrayList<Element> elements = new ArrayList<>();
    private String name;
    private ArrayList<String> comment = new ArrayList<>();

    public ArrayList<Element> getElements() {
        return elements;
    }

    public JsonObject toJson() {
        JsonArray elements = new JsonArray();
        for (Element e : this.elements) {
            if (e instanceof Section && !(e instanceof SubSection)) {
                if (((Section) e).getName() == this.name) {
                    continue;
                    // will throw exception later or account for getElements() to not
                    // accept Section Element
                }
            }
            elements.add(e.toJson());
        }
        JsonObject section = new JsonObject();
        section.addProperty("section_name", name);

        if (comment.size() == 1) {
            section.addProperty("comment", this.comment.getLast());
        }
        if (comment.size() > 1) {
            // When there are more than 1 comment, they are treated as the section's comment
            // And therefore gets converted into a JsonArray
            JsonArray commentArr = new JsonArray();
            for (String s : comment) {
                commentArr.add(s);
            }
            section.add("comment", commentArr);

        }

        section.add("elements", elements);
        return section;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    Section() {
    }

    Section(String name) {
        this.name = name;
    }
}