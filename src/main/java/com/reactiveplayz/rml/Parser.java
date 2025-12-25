package com.reactiveplayz.rml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

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

    private static int lineNo = 0; // The Line Number the reader is at. Useful for errors

    public static int getLineNum() {
        return lineNo;
    }

    public static void Parse(File rmlFile, RMLFile outputObj) {
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
                lineNo++;
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
                    outputObj.file_header.append(line);
                    continue;
                }
                if (Identifier.isPlainText(line)) {
                    continue;
                }
                if (Identifier.isSection(line)) {
                    prevLineType = LineType.SECTION;

                    currentSection = Identifier.sectionName(line).raw();
                    sections.put(currentSection, new Section(currentSection));
                    currentWorkingSection = sections.get(currentSection);
                    if (Identifier.commentText(line) != null) {
                        // section definition may contain a comment
                        // in that case, set it as the Section comment
                        currentWorkingSection.getComment().add(Identifier.commentText(line));
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
                        // subsection definition may contain a comment
                        // in that case, set it as the SubSection comment
                        currentWorkingSection.getComment().add(Identifier.commentText(line));
                    }
                }
                Element lastElement = null;
                if (!currentWorkingSection.getElements().isEmpty()) {
                    lastElement = currentWorkingSection.getElements().getLast();
                }
                if (Identifier.isKeyValue(line) && prevLineType != LineType.LIST) {
                    currentWorkingSection.getElements().add(
                            Parser.asKeyValueElement(line));
                    prevLineType = LineType.KEYVALUE;
                    continue;
                }
                if (Identifier.isContinuationLine(line) && prevLineType == LineType.KEYVALUE) {
                    ((KeyValueElement) lastElement).getValue().add(Identifier.continuationLineValue(line));
                    if (Identifier.continuationLineComment(line).raw() != null) {
                        ((KeyValueElement) lastElement).getComment().getCommentValue()
                                .add(Identifier.continuationLineComment(line));
                    }
                    continue;
                }
                if (Identifier.isComment(line)) {
                    if (prevLineType == LineType.COMMENT && lastElement instanceof Comment) {
                        // adding the current line's comment text
                        // to the previous Comment Element's ArrayList
                        ((Comment) lastElement).getCommentValue().add(Identifier.commentText(line));
                        continue;
                    }
                    if (prevLineType == LineType.SECTION || prevLineType == LineType.SUBSECTION) {
                        currentWorkingSection.getComment().add(Identifier.commentText(line));
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
                    if (prevLineType == LineType.LIST && lastElement instanceof RMLList) {
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineNo = 0;

        for (Map.Entry<String, Section> s : sections.entrySet()) {
            // looping through the HashMap and getting all the Section objects
            // then passing them to the RMLFile outputObj's LinkedHashSet of Sections
            outputObj.sections.add(s.getValue());
        }
    }

    /**
     * Turns a rml Key-Value line into a KeyValueElement Object
     *
     * @param line The line to check for and turn into a KeyValueElement Object
     * @return KeyValueElement Object
     */
    public static KeyValueElement asKeyValueElement(String line) {
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(" //")));
        Matcher matcher = Identifier.KEYVALUE_PATTERN.matcher(splitLine.getFirst());
        if (splitLine.size() == 1) {
            // There are no double forward slashes (//) in the line
            // So, the only value should be set to null
            // As it is used to return the comment text
            splitLine.set(0, null);
        }
        String jointComment = "";
        if (splitLine.size() >= 2) {
            /*
             * If there are multiple double forward slashes (//)
             * within a comment, the line gets split into many groups
             * the first group is definitely the value, so we can start the loop
             * at index 1. The rest of the groups need to be joined with // added
             * at the end, except for the last group.
             */
            for (int i = 1; i < splitLine.size(); i++) {
                if (i == splitLine.size() - 1) {
                    // last index doesn't need a // at the end
                    jointComment += splitLine.get(i);
                    break;
                }
                jointComment += splitLine.get(i).strip() + "//";
            }
            splitLine.set(splitLine.size() - 1, jointComment.strip());
        }
        if (matcher.find()) {
            if (Identifier.isBoolean(matcher.group(2))) {
                return new KeyValueElement(matcher.group(1), Identifier.booleanValue(matcher.group(2)),
                        splitLine.getLast());
            }
            if (Identifier.isNum(matcher.group(2))) {
                return new KeyValueElement(matcher.group(1), Identifier.numValue(matcher.group(2)),
                        splitLine.getLast());
            }
            if (Identifier.isDate(matcher.group(2))) {
                return new KeyValueElement(matcher.group(1), Identifier.dateValue(matcher.group(2)),
                        splitLine.getLast());
            }
            return new KeyValueElement(matcher.group(1), new RMLString(matcher.group(2).strip()),
                    splitLine.getLast());
        }
        return new KeyValueElement();
    }

}
