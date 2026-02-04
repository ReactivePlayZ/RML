package com.reactiveplayz.rml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Class to parse files into {@link RMLFile} instances
 */
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

    private static int lineNo = 0; // The Line Number the reader is at. Useful for error logging


    /**
     * The line number that this parser's reader is currently on
     * <p>(Note that this is only useful for error logging)</p>
     */
    public static int getLineNum() {
        return lineNo;
    }

    /**
     * Parses a file that has RML and returns a {@link RMLFile}
     * @return A {@link RMLFile} parsed from a {@link File}
     */
    public RMLFile Parse(File file) {
        RMLFile output = new RMLFile(file.getName());
        Parse(file, output);
        return output;
    }

    /**
     * Parses a file that has RML and places objects to a provided {@link RMLFile}
     */
    public void Parse(File rmlFile, RMLFile output) {
        String line;

        // tracking states
        String currentSection = null; // current section name; last added section name in LinkedHashMap
        Sections currentWorkingSection = null; // current working section (or subsection); scope of new elements
        LineType prevLineType = null; // helps with continuation/multi-line elements

        // linked hashmaps (to preserve order) with the section name
        // and a Section Object
        LinkedHashMap<String, Section> sections = new LinkedHashMap<>();

        lineNo = 0;
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
                    output.getFileHeader().append(line);
                    continue;
                }
                assert currentWorkingSection != null;

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
                    replace = false;
                }
                if (Identifier.isSubSection(line)) {
                    if (prevLineType == LineType.SUBSECTION) {
                        currentWorkingSection = sections.get(currentSection);
                    }
                    prevLineType = LineType.SUBSECTION;
                    // adding a new subsection as an element to the current working section:
                    currentWorkingSection.add(
                            new SubSection(Identifier.subSectionName(line), currentWorkingSection));

                    // changing the current working section to the newly added subsection
                    currentWorkingSection = (SubSection) currentWorkingSection.getLast();

                    if (Identifier.commentText(line) != null) {
                        // subsection definition may contain a comment
                        // in that case, set it as the SubSection comment
                        currentWorkingSection.getComment().add(Identifier.commentText(line));
                    }
                    replace = false;
                }
                Element lastElement = new Element() {
                    @Override
                    public String toString() {
                        return "";
                    }
                };
                if (!currentWorkingSection.isEmpty()) {
                    lastElement = currentWorkingSection.getLast();
                }
                if (Identifier.isKeyValue(line) && prevLineType != LineType.LIST) {
                    currentWorkingSection.add(
                            asKeyValueElement(line), isReplace());
                    prevLineType = LineType.KEYVALUE;
                    continue;
                }
                if (Identifier.isContinuationLine(line) && prevLineType == LineType.KEYVALUE) {
                    continuationLineValueAppend(line, ((KeyValueElement) lastElement));
                    if (Identifier.continuationLineComment(line).raw() != null) {
                        ((KeyValueElement) lastElement).getComment()
                                .add(Identifier.continuationLineComment(line));
                    }
                    continue;
                }
                if (Identifier.isComment(line)) {
                    if (prevLineType == LineType.COMMENT && lastElement instanceof Comment) {
                        // adding the current line's comment text
                        // to the previous Comment Element's ArrayList
                        ((Comment) lastElement).add(Identifier.commentText(line));
                        continue;
                    }
                    if (prevLineType == LineType.SECTION || prevLineType == LineType.SUBSECTION) {
                        currentWorkingSection.getComment().add(Identifier.commentText(line));
                        continue;
                    }

                    // previous line is not a comment so add a new independent Comment Element
                    // in the current working section
                    currentWorkingSection.add(
                            new Comment(Identifier.commentText(line)));
                    prevLineType = LineType.COMMENT;
                    continue;
                }
                if (Identifier.isList(line)) {
                    if (prevLineType == LineType.LIST && lastElement instanceof RMLList) {
                        ((RMLList) lastElement).getList().add(Identifier.listValue(line));
                        if (Identifier.listComment(line).raw() != null) {
                            ((RMLList) lastElement).getComment()
                                    .add(Identifier.listComment(line));
                        }
                        continue;
                    }

                    // previous line is not a list so add a new independent List Element
                    // in the current working section
                    currentWorkingSection.add(
                            new RMLList(Identifier.listValue(line),
                                    Identifier.listComment(line)));

                    prevLineType = LineType.LIST;
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The provided file couldn't be found\n\n" + e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lineNo = 0;
            replace = false;
        }

        for (Map.Entry<String, Section> s : sections.entrySet()) {
            // looping through the HashMap and getting all the Section objects
            // then passing them to the RMLFile outputObj's LinkedHashSet of Sections
            output.addSection(s.getValue());
        }
    }

    static void continuationLineValueAppend(String line, KeyValueElement appendTo) {
        String value = Identifier.continuationLineValue(line).raw();
        if (value == null || value.isBlank()) {
            return;
        }
        if (Identifier.isBoolean(value)) {
            appendTo.getValue().add(Identifier.booleanValue(value));
            return;
        } else if (Identifier.isNum(value)) {
            appendTo.getValue().add(Identifier.numValue(value));
            return;
        } else if (Identifier.isDate(value)) {
            appendTo.getValue().add(Identifier.dateValue(value));
            return;
        } else if (Identifier.isTime(value)) {
            appendTo.getValue().add(Identifier.timeValue(value));
            return;
        }
        appendTo.getValue().add(new RMLString(value));

    }


    /** Override state for {@link KeyValueElement}s */
    private boolean replace = false;

    /**
     * Returns true if the last added {@link KeyValueElement}'s value
     * started with {@code @replace} and then sets replace state to false
     * for the next KeyValueElement
     */
    private boolean isReplace() {
        if (replace) {
            replace = false;
            return true;
        }
        return false;
    }

    /**
     * Turns a RML Key/Value line into a {@link KeyValueElement} Object
     *
     * @param line The line to check for and turn into a KeyValueElement
     * @return {@link KeyValueElement} Object
     */
    KeyValueElement asKeyValueElement(String line) {
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
            String value = matcher.group(2);
            if (value.matches("^(?i)@replace[ \\t].*")) {
                value = value.replaceFirst("(?i)@replace[ \\t]", "");
                replace = true;
            }
            String key = matcher.group(1).strip();
            if (Identifier.isBoolean(value)) {
                return new KeyValueElement(key, Identifier.booleanValue(value),
                        splitLine.getLast());
            }
            if (Identifier.isNum(value)) {
                return new KeyValueElement(key, Identifier.numValue(value),
                        splitLine.getLast());
            }
            if (Identifier.isDate(value)) {
                return new KeyValueElement(key, Identifier.dateValue(value),
                        splitLine.getLast());
            }
            if (Identifier.isTime(value)) {
                return new KeyValueElement(key, Identifier.timeValue(value),
                        splitLine.getLast());
            }
            return new KeyValueElement(key, new RMLString(value.strip()),
                    splitLine.getLast());
        }
        return new KeyValueElement();
    }

}
