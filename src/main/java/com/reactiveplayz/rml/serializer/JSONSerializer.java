package com.reactiveplayz.rml.serializer;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.reactiveplayz.rml.RMLFile;
import com.reactiveplayz.rml.RMLString;
import com.reactiveplayz.rml.Section;

public class JSONSerializer {
    private JsonObject root = new JsonObject();
    private JsonArray file_header = new JsonArray();
    private JsonArray sections = new JsonArray();
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();;
    private final RMLFile rmlFile;

    public JSONSerializer(RMLFile rmlFile) {
        this.rmlFile = rmlFile;
        root.add("file_header", file_header);
        root.add("sections", sections);
        for (RMLString s : rmlFile.file_header) {
            appendToFileHeader(s.raw());
        }
        for (Section s : rmlFile.sections) {
            appendToSections(s);
        }
    }

    private void appendToFileHeader(String text) {
        file_header.add(text);
    }

    private void appendToSections(Section section) {
        sections.add(ElementJSON.toJson(section));
    }

    public JsonObject getAsJSON() {
        return root;
    }

    public void write() {
        write(false);
    }

    public void write(boolean prettyPrinting) {
        if (prettyPrinting) {
            gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rmlFile.getName() + " rml.json"))) {
            gson.toJson(getAsJSON(), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
