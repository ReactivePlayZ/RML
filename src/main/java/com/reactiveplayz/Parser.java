package com.reactiveplayz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Parser {

    // Currently only used to track the previous line's type
    // to decide for continuation (multi-line elements)
    private enum LineType {
        SECTION,
        SUBSECTION,
        KEYVALUE,
        LIST,
        COMMENT
    }

    private static int lineNo = 1; // The Line Number the reader is at. Useful for errors

    public static int getLineNum() {
        return lineNo;
    }

    public static void Parse(File rmlFile) {
        String line;

        // tracking states
        String currentSection = null; // current section name; last added section name in LinkedHashMap
        Section currentWorkingSection = null; // current working section (or subsection); scope of new elements
        LineType prevLineType = null; // helps with continuation/multi-line elements

        // linked hashmaps (to preserve order) with the section name
        // and a Section Object
        LinkedHashMap<String, Section> sections = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rmlFile))) {
            while ((line = reader.readLine()) != null) {
                if (Identifier.isLineBreak(line)) {
                    // if the current line is a line break, reset subsections
                    // and multi-line element continuation
                    currentWorkingSection = sections.get(currentSection);
                    prevLineType = null;
                    continue;
                }
                if (currentWorkingSection == null && !Identifier.isSection(line)) {
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
                    currentWorkingSection = sections.get(currentSection);
                    if (Identifier.commentText(line) != null) {
                        // section definition may contain a comment
                        // in that case, set it as the Section comment
                        currentWorkingSection.getComment().addLast(Identifier.commentText(line).strip());
                    }
                }
                if (Identifier.isSubSection(line)) {
                    prevLineType = LineType.SUBSECTION;
                    // adding a new subsection as an element to the current working section:
                    currentWorkingSection.getElements().add(
                            new SubSection(Identifier.subSectionName(line), currentWorkingSection));

                    // changing the current working section to the newly added subsection
                    currentWorkingSection = (Section) currentWorkingSection.getElements().getLast();

                    if (Identifier.commentText(line) != null) {
                        // sub section definition may contain a comment
                        // in that case, set it as the SubSection comment
                        currentWorkingSection.getComment().addLast(Identifier.commentText(line).strip());
                    }
                }
                Element lastElement = null;
                if (currentWorkingSection.getElements().size() > 0) {
                    lastElement = currentWorkingSection.getElements().getLast();
                }
                if (Identifier.isKeyValue(line) && prevLineType != LineType.LIST) {
                    currentWorkingSection.getElements().add(
                            KeyValueElement.asKeyValueElement(line));
                    prevLineType = LineType.KEYVALUE;
                    continue;
                }
                if (Identifier.isContinuationLine(line) && prevLineType == LineType.KEYVALUE) {
                    ((KeyValueElement) lastElement).getValue().add(Identifier.continuationLineValue(line));
                    if (Identifier.continuationLineComment(line) != null) {
                        ((KeyValueElement) lastElement).getComment()
                                .addLast(Identifier.continuationLineComment(line).strip());
                    }
                    continue;
                }
                if (Identifier.isComment(line)) {
                    if (prevLineType == LineType.COMMENT) {
                        // adding the current line's comment text
                        // to the previous Comment Element's ArrayList
                        ((Comment) lastElement).getComment().addLast(Identifier.commentText(line).strip());
                        continue;
                    }
                    if (prevLineType == LineType.SECTION || prevLineType == LineType.SUBSECTION) {
                        ((Section) currentWorkingSection).getComment().addLast(Identifier.commentText(line).strip());
                        continue;
                    }

                    // previous line is not a comment so add a new independent Comment Element
                    // in the current working section
                    currentWorkingSection.getElements().add(
                            new Comment(Identifier.commentText(line)));
                    prevLineType = LineType.COMMENT;
                    continue;
                }
                if (Identifier.isList(line)) {
                    if (prevLineType == LineType.LIST) {
                        ((RMLList) lastElement).getList().add(Identifier.listValue(line));
                        continue;
                    }

                    // previous line is not a list so add a new independent List Element
                    // in the current working section
                    currentWorkingSection.getElements().add(
                            new RMLList(Identifier.listValue(line)));
                    prevLineType = LineType.LIST;
                    continue;

                }
                lineNo++;

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
