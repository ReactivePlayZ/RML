package com.reactiveplayz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Parser {
    private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static void Parse(File rmlFile) {
        String line;
        String currentSection = rmlFile.getName();
        String currentSubSection = null;
        LinkedHashMap<String, Section> sections = new LinkedHashMap<>();
        LinkedHashMap<String, Section> subsections = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rmlFile))) {
            while ((line = reader.readLine()) != null) {
                if (Identifier.isLineBreak(line)) {
                    currentSubSection = null;
                }
                if (currentSection.equals(rmlFile.getName()) && !Identifier.isSection(line)) {
                    Converter.appendToFileHeader(line);
                    continue;
                }
                if (Identifier.isPlainText(line)) {
                    continue;
                }
                if (Identifier.isSection(line)) {
                    currentSection = Identifier.sectionName(line);
                    sections.put(currentSection, new Section(currentSection, false));
                    continue;
                }
                if (Identifier.isSubSection(line)) {
                    currentSubSection = Identifier.subSectionName(line);
                    subsections.put(currentSubSection, new Section(currentSubSection, true));
                    sections.get(currentSection).getElements().add(subsections.get(currentSubSection).getSection());
                    continue;
                }
                if (currentSubSection == null) {
                    if (Identifier.isKeyValue(line)) {
                        JsonObject kv = keyValueToJsonObj(line);
                        sections.get(currentSection).getElements().add(kv);
                    }
                    // will add other types (comments, lists, etc) later, otherwise nesting is
                    // useless and unreadable lol.
                } else {
                    if (Identifier.isKeyValue(line)) {
                        JsonObject kv = keyValueToJsonObj(line);
                        subsections.get(currentSubSection).getElements().add(kv);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Section> s : sections.entrySet()) {
            System.out.println(gson.toJson(s.getValue().getSection()));
            Converter.appendToSections(s.getValue());
        }
    }

    /**
     * Creates a JsonObject from a key-value line
     * 
     * @param line The String to turn into a JsonObject
     * @return {@code JsonObject} with fields of:
     *         {@code key},
     *         {@code separater},
     *         {@code value},
     *         and {@code comment}.
     *         <p>
     *         Each field can be null as well
     *         </p>
     */
    private static JsonObject keyValueToJsonObj(String line) {
        JsonObject kv = new JsonObject();
        kv.addProperty("key", Identifier.keyValueGroups(line)[0]);
        kv.addProperty("separater", Identifier.keyValueGroups(line)[1]);
        kv.addProperty("value", Identifier.keyValueGroups(line)[2]);
        kv.addProperty("comment", Identifier.keyValueGroups(line)[3]);
        return kv;
    }
}
