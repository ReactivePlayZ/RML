package com.reactiveplayz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Converter {
    private static JsonObject root = new JsonObject();
    private static JsonArray file_header = new JsonArray();
    private static JsonArray sections = new JsonArray();
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static void appendToFileHeader(String line) {
        if (root.isEmpty()) {
            root.add("file_header", file_header);
        }
        file_header.add(line);
    }

    public static void appendToSections(Section section) {
        if (!root.has("sections")) {
            root.add("sections", sections);
        }
        sections.add(section.getSection());
    }

    public static void write(File rmlFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rmlFile.getName() + " rml.json"))) {
            gson.toJson(root, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
