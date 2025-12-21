package com.reactiveplayz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier {
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^[ \\t]*\\/\\/[ \\t]*(.*)$");
    private static final Pattern SECTION_PATTERN = Pattern
            .compile("^[ \\t]*=+[ \\t]+(.+)[ \\t]=+(?:[ \\t]+\\/\\/[ \\t]*(.*))?[ \\t]*$");
    private static final Pattern SUBSECTION_PATTERN = Pattern
            .compile("^[ \\t]*\\((.+?)\\)(?:[ \\t]+\\/\\/[ \\t]*(.*))?[ \\t]*$");
    private static final Pattern KEYVALUE_PATTERN = Pattern
            .compile("^[ \\t]*-[ \\t]+(.+)(?::[ \\t]+|-[ \\t]+)(.*?)(?:[ \\t]+\\/\\/[ \\t]*(.*))?$");
    private static final Pattern KEYVALUE_SEPARATOR_PATTERN = Pattern
            .compile("(:[ \\t]+| - )");
    private static final Pattern LINE_BREAK_PATTERN = Pattern.compile("^[ \\s]*$");
    private static final Pattern LIST_PATTERN = Pattern.compile("^[ \\t]*-[ \\t]+(.+?)(?:[ \\t]+\\/\\/[ \\t]*(.*))?$");
    private static final Pattern CONTINUATION_LINE_PATTERN = Pattern
            .compile("^[ \\t]*\\|(.+?)$");
    private static final Pattern BOOLEAN_TYPE_PATTERN = Pattern
            .compile("^(?:@boolean) (true|false)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern NUM_TYPE_PATTERN = Pattern
            .compile("^(?:@number)[ \\t]*([-]?\\d*[.]?\\d*)$", Pattern.CASE_INSENSITIVE);

    public static boolean isNum(String value) {
        String[] splitValue = value.split("^(?i)(?:@number )");
        // If the value is '@number %d' (where %d is any number including decimals or
        // commas) then the split value should only be 2.
        // Less or more is erroneous and therefore returns false.
        if (splitValue.length != 2) {
            return false;
        }
        value = value.replaceAll(",| ", "");
        Matcher matcher = NUM_TYPE_PATTERN.matcher(value);
        return matcher.find();
    }

    public static BigDecimal numValue(String value) {
        String[] splitValue = value.split("^(?i)(?:@number )");
        if (splitValue.length != 2) {
            return null;
        }
        value = splitValue[1].replaceAll(",| ", "");
        return new BigDecimal(value);
    }

    public static Pattern getBooleanTypePattern() {
        return BOOLEAN_TYPE_PATTERN;
    }

    /**
     * <p>
     * Checks whether a given value is in the format of {@code @boolean true/false}.
     * </p>
     * Note: Only the value of a key-value, continuation line, or list should be
     * used.
     * 
     * @param value the value of a {@code key-value}, {@code continuation line}, or
     *              {@code list}
     * @return boolean {@code true/false}
     */
    public static boolean isBoolean(String value) {
        return BOOLEAN_TYPE_PATTERN.matcher(value.strip()).find();
    }

    /**
     * Checks a {@code @boolean true/false} String and
     * returns true if it's {@code @boolean true}
     * and false if it's {@code @boolean false}
     * 
     * @param value the value of a {@code key-value}, {@code continuation line}, or
     *              {@code list}
     * @return boolean {@code true/false}
     */
    public static boolean booleanValue(String value) throws IllegalArgumentException {
        Matcher matcher = BOOLEAN_TYPE_PATTERN.matcher(value);
        if (matcher.find()) {
            return matcher.group(1).toLowerCase().equals("true");
        }
        throw new IllegalArgumentException("value is not a boolean type cast (@boolean true/false)");
    }

    public static Pattern getCommentPattern() {
        return COMMENT_PATTERN;
    }

    public static Pattern getSectionPattern() {
        return SECTION_PATTERN;
    }

    public static Pattern getSubSectionPattern() {
        return SUBSECTION_PATTERN;
    }

    public static Pattern getKeyValuePattern() {
        return KEYVALUE_PATTERN;
    }

    public static Pattern getLineBreakPattern() {
        return LINE_BREAK_PATTERN;
    }

    public static Pattern getListPattern() {
        return LIST_PATTERN;
    }

    public static Pattern getContinuationLinePattern() {
        return CONTINUATION_LINE_PATTERN;
    }

    public static String getLineValue(String line) {
        if (isComment(line)) {
            return commentText(line);
        } else if (isSection(line)) {
            return sectionName(line);
        } else if (isSubSection(line)) {
            return subSectionName(line);
        } else if (isList(line)) {
            return listValue(line);
        }
        return null;
    }

    public static boolean isList(String line) {
        return (LIST_PATTERN.matcher(line).find());
    }

    public static String listValue(String line) {
        Matcher matcher = LIST_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static boolean isContinuationLine(String line) {
        return CONTINUATION_LINE_PATTERN.matcher(line).find();
    }

    public static String continuationLineValue(String line) {
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(" //")));
        splitLine.set(0, splitLine.get(0).strip());
        splitLine.set(splitLine.size() - 1, splitLine.getLast().strip());
        Matcher matcher = CONTINUATION_LINE_PATTERN.matcher(splitLine.get(0));
        if (matcher.find()) {
            return matcher.group(1).strip();
        }
        return null;
    }

    public static String continuationLineComment(String line) {
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(" //")));
        splitLine.set(0, splitLine.get(0).strip());
        Matcher matcher = CONTINUATION_LINE_PATTERN.matcher(splitLine.get(0));
        if (splitLine.size() == 1) {
            // There are no double forward slashes (//) in the line
            // So, the only value should be set to null
            // As it is used to return the comment text
            splitLine.set(0, null);
        }
        String jointComment = "";
        if (splitLine.size() > 2) {
            /*
             * if there are multiple double forward slashes (//)
             * within a comment, the line gets split into many groups
             * the first group is definitely the value, so we can start the loop
             * at index 1. The rest of the groups need to be joined with // added
             * at the end, except for the last group.
             */
            for (int i = 1; i < splitLine.size(); i++) {
                if (i == splitLine.size() - 1) {
                    jointComment += splitLine.get(i);
                    break;
                }
                jointComment += splitLine.get(i) + "//";
            }
            splitLine.set(splitLine.size() - 1, jointComment.strip());
        }
        if (matcher.find()) {
            return splitLine.getLast();
        }
        return null;
    }

    /**
     * Checks whether a given line is an empty (separating) line or not
     * 
     * @param line
     * @return boolean {@code true/false}
     */
    public static boolean isLineBreak(String line) {
        return LINE_BREAK_PATTERN.matcher(line).find();
    }

    /**
     * Checks whether a given line does <b><u>not</u></b> start with any identifiers
     * (including comments)
     * <p>
     * Note: returns true even for line breaks (For line breaks use
     * {@link #isLineBreak(String)}
     * </p>
     * 
     * @param line The line to check
     * @return boolean {@code true/false}
     */
    public static boolean isPlainText(String line) {
        return !(isSubSection(line) || isSection(line) || isKeyValue(line) || isComment(line) || isList(line)
                || isContinuationLine(line));
    }

    public static boolean isComment(String line) {
        return COMMENT_PATTERN.matcher(line).find();
    }

    /**
     * First checks if the line has
     * valid comment groups (regex) in that line. Then returns the comment text.
     * <p>
     * Otherwise checks if it is a comment text and then returns the text only if it
     * is a comment text.
     * </p>
     * <p>
     * This function applies for all lines that have a valid in-line comment
     * (regex)
     * group or have their own comment text function.
     * </p>
     * 
     * @param line The String to check and return the comment text from
     * @return Comment text (Without //)
     */
    public static String commentText(String line) {
        Matcher matcher = CONTINUATION_LINE_PATTERN.matcher(line);
        if (matcher.find()) {
            if (continuationLineComment(line) != null) {
                return continuationLineComment(line);
            }
        }

        matcher = SECTION_PATTERN.matcher(line);
        if (matcher.find() && matcher.groupCount() >= 2) {
            return matcher.group(2);
        }

        matcher = SUBSECTION_PATTERN.matcher(line);
        if (matcher.find() && matcher.groupCount() >= 2) {
            return matcher.group(2);
        }

        if (isKeyValue(line) && KeyValueElement.asKeyValueElement(line).getComment() != null) {
            return (String) KeyValueElement.asKeyValueElement(line).getComment().getLast();
        }

        matcher = COMMENT_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static boolean isSection(String line) {
        return SECTION_PATTERN.matcher(line).find();
    }

    public static String sectionName(String line) {
        Matcher matcher = SECTION_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static boolean isSubSection(String line) {
        return SUBSECTION_PATTERN.matcher(line).find();
    }

    public static String subSectionName(String line) {
        Matcher matcher = SUBSECTION_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static boolean isSeparator(String line) {
        return KEYVALUE_SEPARATOR_PATTERN.matcher(line).find();
    }

    /**
     * Checks (using RegEx) if a given line matches the key-value format
     * ({@code - key: value} or {@code - key - value}) or not
     * 
     * @param line The String to check
     * @return boolean {@code true/false}
     */
    public static boolean isKeyValue(String line) {
        return KEYVALUE_PATTERN.matcher(line).find();
    }
}
