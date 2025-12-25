package com.reactiveplayz.rml;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Identifier {
    static final Pattern COMMENT_PATTERN = Pattern.compile("^[ \\t]*\\/\\/[ \\t]*(.*)$");
    static final Pattern SECTION_PATTERN = Pattern
            .compile("^[ \\t]*=+[ \\t]+(.+)[ \\t]=+(?:[ \\t]+\\/\\/[ \\t]*(.*))?[ \\t]*$");
    static final Pattern SUBSECTION_PATTERN = Pattern
            .compile("^[ \\t]*\\((.+?)\\)(?:[ \\t]+\\/\\/[ \\t]*(.*))?[ \\t]*$");
    static final Pattern KEYVALUE_PATTERN = Pattern
            .compile("^[ \\t]*-[ \\t]+(.+)(?::[ \\t]+|-[ \\t]+)(.*?)(?:[ \\t]+\\/\\/[ \\t]*(.*))?$");
    static final Pattern LINE_BREAK_PATTERN = Pattern.compile("^[ \\s]*$");
    static final Pattern LIST_PATTERN = Pattern.compile("^[ \\t]*-[ \\t]+(.+?)(?:[ \\t]+\\/\\/[ \\t]*(.*))?$");
    static final Pattern CONTINUATION_LINE_PATTERN = Pattern
            .compile("^[ \\t]*\\|(.+?)$");
    static final Pattern BOOLEAN_TYPE_PATTERN = Pattern
            .compile("^@boolean[ \\t]+(true|false)$", Pattern.CASE_INSENSITIVE);
    static final Pattern NUM_TYPE_PATTERN = Pattern
            .compile("^@number[ \\t]*(-?\\d*[.]?\\d*)$", Pattern.CASE_INSENSITIVE);
    static final Pattern DATE_TYPE_PATTERN = Pattern
            .compile("^@date[ \\t]+(\\d{4}-\\d{2}-\\d{2})$", Pattern.CASE_INSENSITIVE);

    static boolean isDate(String value) {
        String[] splitValue = value.split("^(?i)@date");
        if (splitValue.length != 2) {
            return false;
        }
        splitValue[1] = splitValue[1].strip().replaceAll("[_\\/\\. \\t]", "-");
        Matcher matcher = DATE_TYPE_PATTERN.matcher("@date " + splitValue[1]);
        return matcher.find();

    }

    static RMLDate dateValue(String value) {
        String[] split = value.split("^(?i)@date");
        if (split.length != 2) {
            return new RMLDate(null);
        }
        String date = split[1].strip();
        date = date.replaceAll("[_/\\. \\t]", "-");
        String[] splitDate = date.split("-");
        for (int i = 0; i < splitDate.length; i++) {
            // Adding any missing 0s for the correct format.
            switch (i) {
                case 0:
                    while (splitDate[i].length() != 4) {
                        splitDate[i] = "0" + splitDate[i];
                    }
                    break;
                case 1:
                    while (splitDate[i].length() != 2) {
                        splitDate[i] = "0" + splitDate[i];
                    }
                    if (Integer.parseInt(splitDate[i]) > 12 || Integer.parseInt(splitDate[i]) < 1) {
                        throw new IllegalArgumentException(
                                "\nMonth is not within 1 and 12.\n    "
                                        + value + "\n        at " + Main.getFile().getName() + ":"
                                        + Parser.getLineNum());
                    }
                    break;
                case 2:
                    while (splitDate[i].length() != 2) {
                        splitDate[i] = "0" + splitDate[i];
                    }
                    if (Integer.parseInt(splitDate[i]) > 31 || Integer.parseInt(splitDate[i]) < 1) {
                        throw new IllegalArgumentException(
                                "\nDate is not within 1 and 31.\n    "
                                        + value + "\n        at " + Main.getFile().getName() + ":"
                                        + Parser.getLineNum());
                    }
                    break;
            }
        }
        return new RMLDate(LocalDate.parse(splitDate[0] + "-" + splitDate[1] + "-" + splitDate[2]));
    }

    static boolean isNum(String value) {
        String[] splitValue = value.split("^(?i)@number ");
        // If the value is '@number %d' (where %d is any number including decimals or
        // commas) then the split value should only be 2.
        // Less or more is erroneous and therefore returns false.
        if (splitValue.length != 2) {
            return false;
        }
        value = value.replaceAll("[, ]", "");
        Matcher matcher = NUM_TYPE_PATTERN.matcher(value);
        return matcher.find();
    }

    static RMLNumber numValue(String value) {
        String[] splitValue = value.split("^(?i)@number ");
        if (splitValue.length != 2) {
            return null;
        }
        value = splitValue[1].replaceAll("[, ]", "");
        return new RMLNumber(value);
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
    static boolean isBoolean(String value) {
        return BOOLEAN_TYPE_PATTERN.matcher(value.strip()).find();
    }

    /**
     * Checks a {@code @boolean true/false} String and
     * returns RMLBoolean true if it's {@code @boolean true}
     * and RMLBoolean false if it's {@code @boolean false}
     * 
     * @param value the value of a {@code key-value}, {@code continuation line}, or
     *              {@code list}
     * @return RMLBoolean {@code true/false}
     */
    static RMLBoolean booleanValue(String value) throws IllegalArgumentException {
        Matcher matcher = BOOLEAN_TYPE_PATTERN.matcher(value);
        if (matcher.find()) {
            return new RMLBoolean(matcher.group(1).equalsIgnoreCase("true"));
        }
        throw new IllegalArgumentException("value is not a boolean type cast (@boolean true/false)");
    }

    static boolean isList(String line) {
        return (LIST_PATTERN.matcher(line).find());
    }

    static RMLString listValue(String line) {
        Matcher matcher = LIST_PATTERN.matcher(line);
        if (matcher.find()) {
            return new RMLString(matcher.group(1));
        }
        return null;
    }

    static boolean isContinuationLine(String line) {
        return CONTINUATION_LINE_PATTERN.matcher(line).find();
    }

    static RMLString continuationLineValue(String line) {
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(" //")));
        splitLine.set(0, splitLine.getFirst().strip());
        splitLine.set(splitLine.size() - 1, splitLine.getLast().strip());
        Matcher matcher = CONTINUATION_LINE_PATTERN.matcher(splitLine.getFirst());
        if (matcher.find()) {
            return new RMLString(matcher.group(1).strip());
        }
        return null;
    }

    static RMLString continuationLineComment(String line) {
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split(" //")));
        splitLine.set(0, splitLine.getFirst().strip());
        Matcher matcher = CONTINUATION_LINE_PATTERN.matcher(splitLine.getFirst());
        if (splitLine.size() == 1) {
            // There are no double forward slashes (//) in the line
            // So, the only value should be set to null
            // As it is used to return the comment text
            splitLine.set(0, null);
        }
        String jointComment = "";
        if (splitLine.size() >= 2) {
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
            return new RMLString(splitLine.getLast());
        }

        return new RMLString(null);
    }

    /**
     * Checks whether a given line is an empty (separating) line or not
     * 
     * @return boolean {@code true/false}
     */
    static boolean isLineBreak(String line) {
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
    static boolean isPlainText(String line) {
        return !(isSubSection(line) || isSection(line) || isKeyValue(line) || isComment(line) || isList(line)
                || isContinuationLine(line));
    }

    static boolean isComment(String line) {
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
    static RMLString commentText(String line) {
        Matcher matcher = CONTINUATION_LINE_PATTERN.matcher(line);
        if (matcher.find()) {
            if (continuationLineComment(line).raw() != null) {
                return continuationLineComment(line);
            }
        }

        matcher = SECTION_PATTERN.matcher(line);
        if (matcher.find() && matcher.groupCount() >= 2 && matcher.group(2) != null) {
            return new RMLString(matcher.group(2).strip());
        }

        matcher = SUBSECTION_PATTERN.matcher(line);
        if (matcher.find() && matcher.groupCount() >= 2 && matcher.group(2) != null) {
            return new RMLString(matcher.group(2).strip());
        }

        if (isKeyValue(line) && Parser.asKeyValueElement(line).getComment().getCommentValue().getLast() != null) {
            return (RMLString) Parser.asKeyValueElement(line).getComment().getCommentValue().getLast();
        }

        matcher = COMMENT_PATTERN.matcher(line);
        if (matcher.find()) {
            return new RMLString(matcher.group(1).strip());
        }
        return null;
    }

    static boolean isSection(String line) {
        return SECTION_PATTERN.matcher(line).find();
    }

    static RMLString sectionName(String line) {
        Matcher matcher = SECTION_PATTERN.matcher(line);
        if (matcher.find()) {
            return new RMLString(matcher.group(1));
        }
        return null;
    }

    static boolean isSubSection(String line) {
        return SUBSECTION_PATTERN.matcher(line).find();
    }

    static RMLString subSectionName(String line) {
        Matcher matcher = SUBSECTION_PATTERN.matcher(line);
        if (matcher.find()) {
            return new RMLString(matcher.group(1));
        }
        return null;
    }

    /**
     * Checks (using RegEx) if a given line matches the key-value format
     * ({@code - key: value} or {@code - key - value}) or not
     * 
     * @param line The String to check
     * @return boolean {@code true/false}
     */
    static boolean isKeyValue(String line) {
        return KEYVALUE_PATTERN.matcher(line).find();
    }
}
