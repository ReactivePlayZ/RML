package com.reactiveplayz.rml;

import java.util.*;

/**
 * A RMLFile instance holds the file's {@code name},
 * the {@code file_header} ({@link RMLFileHeader}), and
 * all of it's {@link Section}s in a {@link LinkedHashMap}
 */
public final class RMLFile implements Iterable<Section> {

    private final String name;
    private final RMLFileHeader file_header = new RMLFileHeader();
    private final LinkedHashMap<String, Section> sections = new LinkedHashMap<>();

    /**
     * Calls {@link Section#getAnyFirstMatchingKey(String)} on all Sections stored
     * in this RMLFile until a matching {@code key} is found and then returns it.
     * <br>If there isn't a match, then {@code null} is returned
     * @param key The key to match
     * @return The first matching {@link KeyValueElement} with the same {@code key}
     *         regardless of whether it's in a {@link Section} or {@link SubSection}.
     *         <br>If there isn't a match, then {@code null} is returned
     */
    public KeyValueElement getFirstKeyMatch(String key) {
        for (Map.Entry<String, Section> entry : sections.entrySet()) {
            KeyValueElement match = entry.getValue().getAnyFirstMatchingKey(key);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    /**
     * Calls {@link Section#getAllMatchingKeys(String)} on all the Sections
     * stored in this RMLFile
     * @param key The key to check for in all of the {@link Section}s and {@link SubSection}s
     * @return A {@link List} of all the found {@link KeyValueElement} with
     *         a matching {@code key}.
     *         If no match is found, then the {@link List} will be empty
     */
    public List<KeyValueElement> getAllKeyMatch(String key) {
        ArrayList<KeyValueElement> matchingKeys = new ArrayList<>();

        sections.values().forEach(
                (s) -> matchingKeys.addAll(s.getAllMatchingKeys(key))
        );

        return matchingKeys;
    }

    /**
     * Checks all of the {@link Section}s in this RMLFile and
     * returns the first Element which has a matching value
     * @param value The value to check for
     * @return The first Element which has a matching value.
     *         If no match is found, then {@code null} is returned
     */
    public Element getFirstValueMatch(String value) {
        return getFirstValueMatch(value, false);
    }

    /**
     * Checks all of the {@link Section}s in this RMLFile and
     * returns the first Element which has a value that matches a Regular Expressions
     * if enabled, otherwise has to match exactly
     * @param value The value to check for / The RegEx, if enabled
     * @param regex Should RegEx be used?
     * @return The first Element which has a value that matches a RegEx
     *         (if enabled, otherwise has to match exactly).
     *         If no match is found, then {@code null} is returned
     */
    public Element getFirstValueMatch(String value, boolean regex) {
        for (Map.Entry<String, Section> entry : sections.entrySet()) {
            Element firstMatch = entry.getValue().getFirstElementValueMatch(value, regex);
            if (firstMatch != null) {
                return firstMatch;
            }
        }
        return null;
    }

    /**
     * Checks all of the {@link Section}s and {@link SubSection}s in this RMLFile and
     * returns the first Element which has a matching value
     * @param value The value to check for
     * @return The first Element which has a matching value.
     *         If no match is found, then {@code null} is returned
     */
    public Element getAnyFirstValueMatch(String value) {
        return getAnyFirstValueMatch(value, false);
    }

    /**
     * Checks all of the {@link Section}s and {@link SubSection}s in this RMLFile and
     * returns the first Element which has a value that matches a Regular Expressions
     * if enabled, otherwise has to match exactly
     * @param value The value to check for / The RegEx, if enabled
     * @param regex Should RegEx be used?
     * @return The first Element which has a value that matches a RegEx
     *         (if enabled, otherwise has to match exactly).
     *         If no match is found, then {@code null} is returned
     */
    public Element getAnyFirstValueMatch(String value, boolean regex) {
        for (Map.Entry<String, Section> entry : sections.entrySet()) {
            Element firstMatch = entry.getValue().getAnyFirstElementValueMatch(value, regex);
            if (firstMatch != null) {
                return firstMatch;
            }
        }
        return null;
    }

    /**
     * Checks all of the {@link Section}s in this RMLFile and
     * returns all Elements which have a matching value
     * @param value The value to check for
     * @return All Elements in a {@link List} where each Element
     *         has a matching value.
     *         If no match is found, then an empty {@link List} is returned
     */
    public List<Element> getElementValueMatches(String value) {
        return getElementValueMatches(value, false);
    }

    /**
     * Checks all of the {@link Section}s in this RMLFile and
     * returns all Elements which have a value that matches a Regular Expressions
     * if enabled, otherwise has to match exactly
     * @param value The value to check for / The RegEx, if enabled
     * @param regex Should RegEx be used?
     * @return All Elements which have a matching RegEx value
     *         (if enabled, otherwise has to match exactly) in a {@link List}.
     *         If no match is found, then an empty {@link List} is returned
     */
    public List<Element> getElementValueMatches(String value, boolean regex) {
        for (Map.Entry<String, Section> entry : sections.entrySet()) {
            ArrayList<Element> match =
                    new ArrayList<>(entry.getValue().getElementValueMatches(value, regex));
            if (!match.isEmpty()) {
                return match;
            }
        }
        return null;
    }

    /**
     * Checks all of the {@link Section}s and {@link SubSection}s in this RMLFile and
     * returns the all Elements which have a matching value
     * @param value The value to check for
     * @return All Elements which have a matching value in a {@link List}.
     *         If no match is found, then an empty {@link List} is returned
     */
    public List<Element> getAnyElementValueMatches(String value) {
        return getAnyElementValueMatches(value, false);
    }

    /**
     * Checks all of the {@link Section}s and {@link SubSection}s in this RMLFile and
     * returns the all Elements which have a value that matches a Regular Expressions
     * if enabled, otherwise has to match exactly
     * @param value The value to check for / The RegEx, if enabled
     * @param regex Should RegEx be used?
     * @return All Elements which have a value that matches a RegEx
     *         (if enabled, otherwise has to match exactly) in a {@link List}.
     *         If no match is found, then an empty {@link List} is returned
     */
    public List<Element> getAnyElementValueMatches(String value, boolean regex) {
        for (Map.Entry<String, Section> entry : sections.entrySet()) {
            ArrayList<Element> match =
                    new ArrayList<>(entry.getValue().getAnyElementValueMatches(value, regex));
            if (!match.isEmpty()) {
                return match;
            }
        }
        return null;
    }

    /**
     * Appends a {@link Section} to the end of this RMLFile
     * @param section The {@link Section} to append
     */
    public void addSection(Section section) {
        sections.putLast(section.getName().raw(), section);
    }

    /**
     * Removes a {@link Section} present in this RMLFile with the same name
     * @param name The name to look for a matching {@link Section}
     */
    public void removeSection(String name) {
        sections.remove(name);
    }

    /**
     * Removes a {@link Section} present in this RMLFile with the same name
     * @param name The name to look for a matching {@link Section}
     */
    public void removeSection(RMLString name) {
        sections.remove(name.raw());
    }

    /**
     * Retrieves a {@link Section} that has a specified name
     * @param name The name to search for
     * @return A specific {@link Section} with a matching specified name
     */
    public Section getSection(String name) {
        return sections.get(name);
    }

    /**
     * Retrieves a {@link Section} that has a specified name
     * @param name The name to search for
     * @return A specific {@link Section} with a matching specified name
     */
    public Section getSection(RMLString name) {
        return sections.get(name.raw());
    }

    /**
     * Returns the {@link RMLFileHeader} of this RMLFile
     * @return The {@link RMLFileHeader} of this RMLFile
     */
    public RMLFileHeader getFileHeader() {
        return this.file_header;
    }

    /**
     * Returns the name of this RMLFile, if one was assigned when
     * this instance of this RMLFile was constructed.
     * Otherwise returns {@code null}
     * @return The name of this RMLFile
     */
    public String getName() {
        return this.name;
    }

    /**
     * Creates a RMLFile with no name ({@code null}).
     * <hr>
     * <p>
     * Use {@link #RMLFile(String)} to provide a name for the file.
     * </p>
     * A name isn't necessary and is only
     * used for serializing into other formats (e.g. JSON)
     * or writing to a file
     */
    public RMLFile() {
        this.name = null;
    }

    /**
     * Creates a RMLFile with an initial {@link Section}
     * @param initialSection The initial {@link Section}
     */
    public RMLFile(Section initialSection) {
        this.name = null;
        this.addSection(initialSection);
    }

    /**
     * Creates a RMLFile with a name
     * @param name The name of the RMLFile
     */
    public RMLFile(String name) {
        this.name = name;
    }

    /**
     * Creates a RMLFile with a name and an initial {@link Section}
     * @param initialSection The initial {@link Section}
     * @param name The name of the RMLFile
     */
    public RMLFile(String name, Section initialSection) {
        this.name = name;
        this.addSection(initialSection);
    }

    /**
     * Returns an iterator over the {@link Section}s in this RMLFile.
     * Order is guaranteed based on insertion order
     * @return An iterator over the {@link Section}s in this RMLFile
     */
    @Override
    public Iterator<Section> iterator() {
        return sections.values().iterator();
    }


    /**
     * The header section of a {@link RMLFile} that might contain
     * file information and metadata.
     * <p>Only appending ({@link #append(String)}) is allowed for now.</p>
     * <p><strong>Note that,</strong> this class might be removed in the future</p>
     */
    public static final class RMLFileHeader implements Iterable<RMLString> {
        private final RMLValue<RMLString> value = new RMLValue<>();

        /**
         * Appends to the file header with a specified text
         * @param text The specific text to append to the file header
         */
        public void append(String text) {
            value.add(new RMLString(text));
        }

        @Override
        public Iterator<RMLString> iterator() {
            return value.iterator();
        }
    }
}
