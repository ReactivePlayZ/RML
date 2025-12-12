package com.reactiveplayz;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Section {
    private JsonObject section = new JsonObject();
    private JsonArray elements = new JsonArray();
    private String name;
    private boolean subSection;

    public JsonObject getSection() {
        return section;
    }

    public boolean isSubSection() {
        return subSection;
    }

    public void setSubSection(boolean isSubSection) {
        this.subSection = isSubSection;
    }

    public JsonArray getElements() {
        return elements;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    Section() {
        section.addProperty("section_name", name);
        section.add("elements", elements);
    }

    Section(String name, boolean isSubSection) {
        this.name = name;
        this.subSection = isSubSection;
        section.addProperty("section_name", name);
        section.add("elements", elements);
    }
}