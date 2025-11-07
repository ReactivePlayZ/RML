package com.reactiveplayz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier {
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^\\/\\/[ \\t]*(.*)$");
    private static final Pattern SECTION_PATTERN = Pattern
            .compile("^=+[ \\t]+(.+)[ \\t]=+(?:[ \\t]*\\/\\/[ \\t]*(.*))?[ \\t]*$");
    private static final Pattern SUBSECTION_PATTERN = Pattern
            .compile("^[ \\t]*\\((.+?)\\)(?:[ \\t]*\\/\\/[ \\t]*(.*))?[ \\t]*$");
    private static final Pattern KEYVALUE_PATTERN = Pattern
            .compile("^-[ \\t]+(.+)(:[ \\t]*| - )(.+?)(?:[ \\t]*\\/\\/[ \\t]*(.*))?$");
    private static final Pattern KEYVALUE_SEPARATOR_PATTERN = Pattern
            .compile("(:[ \\t]*| - )");

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

    public static String getLineValue(String line) {
        if (isComment(line)) {
            return commentText(line);
        } else if (isSection(line)) {
            return sectionName(line);
        } else if (isSubSection(line)) {
            return subSectionName(line);
        }
        return "";
    }

    /**
     * Checks whether a given line does <b><u>not</u></b> start with any identifiers
     * (including comments)
     * 
     * @param line The line to check
     * @return boolean {@code true/false}
     */
    public static boolean isPlainText(String line) {
        if (isSubSection(line) || isSection(line) || isKeyValue(line)) {
            return false;
        }
        return !isComment(line);
    }

    public static boolean isComment(String line) {
        return COMMENT_PATTERN.matcher(line).find();
    }

    /**
     * First checks if the line is a section or subsection line and if there are
     * valid comment groups in that line. Then returns the comment text.
     * <hr>
     * It then checks if it is a comment text and then returns the text only if it
     * is a comment text.
     * 
     * @param line
     * @return Comment text (Without //)
     */
    public static String commentText(String line) {
        if (isSection(line) && !SECTION_PATTERN.matcher(line).group(2).isEmpty()) {
            return SECTION_PATTERN.matcher(line).group(2);
        } else if (isSubSection(line) && !SUBSECTION_PATTERN.matcher(line).group(2).isEmpty()) {
            return SUBSECTION_PATTERN.matcher(line).group(2);
        } else if (isKeyValue(line) && keyValueGroups(line)[3] != null) {
            return keyValueGroups(line)[3];
        }
        Matcher matcher = COMMENT_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public static boolean isSection(String line) {
        if (isComment(line)) {
            return false;
        }
        return SECTION_PATTERN.matcher(line).find();
    }

    public static String sectionName(String line) {
        if (!isSection(line)) {
            return "";
        }
        Matcher matcher = SECTION_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static boolean isSubSection(String line) {
        if (isComment(line)) {
            return false;
        }
        return SUBSECTION_PATTERN.matcher(line).find();
    }

    public static String subSectionName(String line) {
        if (!isSubSection(line)) {
            return "";
        }
        Matcher matcher = SUBSECTION_PATTERN.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static boolean isSeparator(String line) {
        return KEYVALUE_SEPARATOR_PATTERN.matcher(line).find();
    }

    /**
     * Checks (using RegEx) if a given line matches the key-value format
     * ({@code - key: value}) or not
     * 
     * @param line The String to check
     * @return boolean {@code true/false}
     */
    public static boolean isKeyValue(String line) {
        if (isComment(line)) {
            return false;
        }
        return KEYVALUE_PATTERN.matcher(line).find();
    }

    /**
     * Gives elements of a key-value pair split in an array
     * Using {@code -key: value}
     * <table>
     * <thead>
     * <tr>
     * <th>Index</th>
     * <th>Stores</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>0</td>
     * <td>Key</td>
     * </tr>
     * <tr>
     * <td>1</td>
     * <td>Separator ({@code : } or {@code  - })</td>
     * </tr>
     * <tr>
     * <td>2</td>
     * <td>Value</td>
     * </tr>
     * <tr>
     * <td>3</td>
     * <td>Comment</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * If an index is null, then no match was found for it (e.g for
     * {@code comments})
     * </p>
     * <h3>Example</h3>
     * 
     * <pre>
     * String[] arr = Identifier.keyValueGroups("- Based on: Java // And some very descriptive comment");
     * return arr[0] == "Based on";
     * return arr[1] == ": ";
     * return arr[2] == "Java";
     * return arr[3] == "And some very descriptive comment";
     * // All of them would return true
     * 
     * </pre>
     * 
     * 
     * @param line The line to split into groups
     * @return
     *         <p>
     *         String array of split elements
     *         </p>
     *         Always returns an array with 4 indices
     *         </p>
     */
    public static String[] keyValueGroups(String line) {
        Matcher matcher = KEYVALUE_PATTERN.matcher(line);
        if (matcher.find()) {
            return new String[] { matcher.group(1), matcher.group(2),
                    matcher.group(3), matcher.group(4) };
        }
        return new String[] { null, null, null, null };
    }
}
