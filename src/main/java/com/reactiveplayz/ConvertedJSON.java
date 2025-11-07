package com.reactiveplayz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ConvertedJSON {
    private JsonObject json = new JsonObject();
    private JsonArray fileHeaderText = new JsonArray();
    private JsonArray sections = new JsonArray();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void addToFileHeader(String value) {
        if (!json.has("file_header")) {
            json.add("file_header", fileHeaderText);
        }
        fileHeaderText.add(value); // Adds to file_header array

    }

    public void main(String[] args) {
        addToFileHeader("This file is something related to");
        addToFileHeader("something very descriptive.");
        addToFileHeader("Tho, I'm unsure to be honest what this");
        addToFileHeader("file is for.");

        Section sectA = new Section("Section A");
        Section.Element element1 = sectA.new Element();
        element1.setKey("So");
        element1.setSeparater(":");
        element1.setValue("values");
        element1.setComment("this comment isn't in the original file");
        element1.addProperties();
        sectA.elements.add(element1.obj);
        sections.add(sectA.section);
        System.out.println(element1);
        json.add("sections", sections);

        Section sectXYZ = new Section("XYZ");
        Section.Element element2 = sectXYZ.new Element();
        element2.setKey("The 404 Book");
        element2.setSeparater(":");
        element2.setValue("4th Vol. out on Nov 1st");
        element2.setComment("Very unnecessary and not helpful comment");
        element2.addProperties();
        sectXYZ.elements.add(element2.obj);
        sections.add(sectXYZ.section);

        // One last test for sub sections within elements.
        // ... tmrw
        System.out.println(gson.toJson(json));
        System.out.println("Unformatted: " + json);
    }
}

class Section {
    JsonObject section = new JsonObject();
    private String header_name;
    JsonArray elements = new JsonArray();

    public String getHeader() {
        return header_name;
    }

    public void setHeader(String header_name) {
        this.header_name = header_name;
    }

    Section() {
    }

    Section(String name) {
        header_name = name;
        section.addProperty("header_name", header_name);
        section.add("elements", elements);
    }

    class Element {
        private String key_name;
        private String separater;
        private String value;
        private String comment;
        JsonObject obj = new JsonObject();

        public String getKey() {
            return key_name;
        }

        public void setKey(String key) {
            this.key_name = key;
        }

        public String getSeparater() {
            return separater;
        }

        public void setSeparater(String separater) {
            this.separater = separater;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        void addProperties() {
            obj.addProperty("key_name", key_name);
            obj.addProperty("separater", separater);
            obj.addProperty("value", value);
            obj.addProperty("comment", comment);
        }

    }
}
