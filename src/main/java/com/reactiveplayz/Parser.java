package com.reactiveplayz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Parser {

    // Currently only used to track the previous line's type
    // to decide for continuation (multi-line elements)
    public enum LineType {
        SECTION,
        SUBSECTION,
        KEYVALUE,
        LIST,
        COMMENT
    }

    public static void Parse(File rmlFile) {
        String line;

        // tracking states
        String currentSection = null;
        String currentSubSection = null;
        LineType prevLineType = null; // helps with continuation/multi-line elements

        // linked hashmaps (to preserve order) with the section name
        // and a Section Object
        LinkedHashMap<String, Section> sections = new LinkedHashMap<>();
        LinkedHashMap<String, SubSection> subsections = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rmlFile))) {
            while ((line = reader.readLine()) != null) {
                if (Identifier.isLineBreak(line)) {
                    // if the current line is a line break, reset subsections
                    // and multi-line element continuation
                    currentSubSection = null;
                    prevLineType = null;
                    continue;
                }
                if (currentSection == null && !Identifier.isSection(line)) {
                    // if we haven't reached a section yet and the current line is also not a
                    // section then the line must be part of the file header (out of section/top of
                    // file)
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
                        // section definition may contain a comment
                        // in that case, set it as the Section comment
                        sections.get(currentSection).setComment(Identifier.commentText(line));
                    }
                }
                if (Identifier.isSubSection(line)) {
                    prevLineType = LineType.SUBSECTION;

                    currentSubSection = Identifier.subSectionName(line);
                    subsections.put(currentSubSection, new SubSection(currentSubSection, sections.get(currentSection)));
                    // adding the subsection as an element to the current section:
                    sections.get(currentSection).getElements().add(subsections.get(currentSubSection));

                    if (Identifier.commentText(line) != null) {
                        // sub section definition may contain a comment
                        // in that case, set it as the SubSection comment
                        subsections.get(currentSubSection).setComment(Identifier.commentText(line));
                    }
                }
                if (currentSubSection == null) {
                    // the file reader is not in a sub section
                    if (Identifier.isKeyValue(line)) {
                        // add the key-value to the current section
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
                                // adding the current line's comment text
                                // to the previous Comment Element's ArrayList
                                ((Comment) lastElement).getComment().addLast(Identifier.commentText(line));
                                continue;
                            }
                        }

                        // previous line is not a comment so add a new independent Comment Element
                        // in the current section
                        sections.get(currentSection).getElements().add(
                                new Comment(Identifier.commentText(line)));
                        prevLineType = LineType.COMMENT;
                    }
                } else {
                    // same as when not in a subsection
                    // except everything gets added to the current subsection
                    // instead of the current section
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
                                ((Comment) lasElement).getComment().add(Identifier.commentText(line));
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
            // looping through the HashMap and getting all the Section objects
            // then passing them to the Converter's appendToSections() function
            Converter.appendToSections(s.getValue());
        }
    }
}
