package com.reactiveplayz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Parser {
    public enum LineType {
        SECTION,
        SUBSECTION,
        KEYVALUE,
        LIST,
        COMMENT
    }

    public static void Parse(File rmlFile) {
        String line;
        String currentSection = rmlFile.getName();
        String currentSubSection = null;
        LineType prevLineType = null;
        LinkedHashMap<String, Section> sections = new LinkedHashMap<>();
        LinkedHashMap<String, SubSection> subsections = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rmlFile))) {
            while ((line = reader.readLine()) != null) {
                if (Identifier.isLineBreak(line)) {
                    currentSubSection = null;
                    prevLineType = null;
                    continue;
                }
                if (currentSection.equals(rmlFile.getName()) && !Identifier.isSection(line)) {
                    Converter.appendToFileHeader(line);
                    continue;
                }
                if (Identifier.isPlainText(line)) {
                    continue;
                }
                if (Identifier.isSection(line)) {
                    prevLineType = LineType.SECTION;
                    currentSection = Identifier.sectionName(line);
                    sections.put(currentSection, new Section(currentSection));
                    if (Identifier.commentText(line) != null) {
                        sections.get(currentSection).setComment(Identifier.commentText(line));
                    }
                }
                if (Identifier.isSubSection(line)) {
                    prevLineType = LineType.SUBSECTION;
                    currentSubSection = Identifier.subSectionName(line);
                    subsections.put(currentSubSection, new SubSection(currentSubSection, sections.get(currentSection)));
                    sections.get(currentSection).getElements().add(subsections.get(currentSubSection));
                    if (Identifier.commentText(line) != null) {
                        subsections.get(currentSubSection).setComment(Identifier.commentText(line));
                    }
                }
                if (currentSubSection == null) {
                    if (Identifier.isKeyValue(line)) {
                        sections.get(currentSection).getElements().add(
                                new KeyValueElement(
                                        Identifier.keyValueGroups(line)[0],
                                        Identifier.keyValueGroups(line)[1],
                                        Identifier.keyValueGroups(line)[2],
                                        Identifier.keyValueGroups(line)[3]));
                    }
                    if (Identifier.isComment(line)) {
                        if (prevLineType == LineType.COMMENT) {
                            Element lastElement = sections.get(currentSection).getElements().getLast();
                            if (lastElement instanceof Comment) {
                                ((Comment) lastElement).getComment().addLast(Identifier.commentText(line));
                                continue;
                            }
                        }
                        sections.get(currentSection).getElements().add(
                                new Comment(Identifier.commentText(line)));
                        prevLineType = LineType.COMMENT;
                    }
                } else {
                    if (Identifier.isKeyValue(line)) {
                        subsections.get(currentSubSection).getElements().add(
                                new KeyValueElement(
                                        Identifier.keyValueGroups(line)[0],
                                        Identifier.keyValueGroups(line)[1],
                                        Identifier.keyValueGroups(line)[2],
                                        Identifier.keyValueGroups(line)[3]));
                    }
                    if (Identifier.isComment(line)) {
                        if (prevLineType == LineType.COMMENT) {
                            Element lasElement = subsections.get(currentSubSection).getElements().getLast();
                            if (lasElement instanceof Comment) {
                                ((Comment) lasElement).getComment().add(line);
                            }
                            continue;
                        }
                        subsections.get(currentSubSection).getElements().add(
                                new Comment(Identifier.commentText(line)));
                        prevLineType = LineType.COMMENT;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Section> s : sections.entrySet()) {
            Converter.appendToSections(s.getValue());
        }
    }
}
