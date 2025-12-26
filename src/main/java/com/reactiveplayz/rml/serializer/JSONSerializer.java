package com.reactiveplayz.rml.serializer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.reactiveplayz.rml.RMLFile;
import com.reactiveplayz.rml.RMLString;
import com.reactiveplayz.rml.Section;

/**
 * Uses {@code Gson} to serialize a stored {@link RMLFile} into JSON
 */
public class JSONSerializer {
    private JsonObject root = new JsonObject();
    private JsonArray file_header = new JsonArray();
    private JsonArray sections = new JsonArray();
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();;
    private final RMLFile rmlFile;

    public RMLFile getRmlFile() {
        return rmlFile;
    }

    /**
     * Serializes a {@link RMLFile} into JSON
     * <p>
     * Any changes made to the RMLFile will not update
     * the JSON. Use {@link #updateJSON()} to update the JSON after
     * changes are made to the RMLFile
     * </p>
     *
     * @param rmlFile The RMLFile to serialize into JSON
     */
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

    /**
     * Clears the existing JSON and then adds the {@code file_header} and
     * {@code sections} to the JSON
     */
    public void updateJSON() {
        file_header = new JsonArray();
        sections = new JsonArray();
        root = new JsonObject();
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

    /**
     * Returns the RMLFile in its JSON representation
     * <p>
     * Note: Call {@link #updateJSON()} if there were any changes made to
     * the RMLFile
     * </p>
     * 
     * @return The RMLFile in its JSON representation as a {@code JsonObject}
     */
    public JsonObject getAsJSON() {
        return root;
    }

    /**
     * Creates a JSON file from the JSON stored of the {@link RMLFile}
     * <p>
     * Call {@link #updateJSON()} to update the JSON stored
     * </p>
     */
    public void writeJsonFile() {
        writeJsonFile(false);
    }

    /**
     * Creates a JSON file from the JSON stored of the {@link RMLFile}
     * <p>
     * Call {@link #updateJSON()} to update the JSON stored
     * </p>
     * 
     * @param prettyPrinting If true then uses pretty printing when writing to the
     *                       file;
     *                       Otherwise writes as is
     */
    public void writeJsonFile(boolean prettyPrinting) {
        if (prettyPrinting) {
            gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rmlFile.getName() + " rml.json"))) {
            gson.toJson(getAsJSON(), writer);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the FileWriter\n\n" + e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
