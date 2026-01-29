package com.reactiveplayz.rml;

import java.util.*;

/**
 * A RMLFile instance holds the file's {@code name},
 * the {@code file_header} ({@link RMLFileHeader}), and
 * all of it's {@code sections} in a {@code LinkedHashSet}
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
     * @return An {@link ArrayList} of all the found {@link KeyValueElement} with
     *         a matching {@code key}.
     *         If no match is found, then the ArrayList will be empty
     */
    public ArrayList<KeyValueElement> getAllKeyMatch(String key) {
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
     * if enabled
     * @param value The value to check for / The RegEx, if enabled
     * @param regex Should RegEx be used?
     * @return The first Element which has a value that matches a RegEx.
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
     * if enabled
     * @param value The value to check for / The RegEx, if enabled
     * @param regex Should RegEx be used?
     * @return The first Element which has a value that matches a RegEx.
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

    public Collection<Element> getAll(String value) {
        for (Map.Entry<String, Section> entry : sections.entrySet()) {
            ArrayList<Element> match =
                    new ArrayList<>(entry.getValue().getElementValueMatches(value));
            if (!match.isEmpty()) {
                return match;
            }
        }
        return null;
    }

    public void addSection(Section section) {
        sections.putLast(section.getName().raw(), section);
    }

    public void removeSection(String name) {
        sections.remove(name);
    }

    public void removeSection(RMLString name) {
        sections.remove(name.raw());
    }

    public Section getSection(String name) {
        return sections.get(name);
    }

    public Section getSection(RMLString name) {
        return sections.get(name.raw());
    }

    public Iterator<Section> getSectionsIterator() {
        return sections.values().iterator();
    }

    public RMLFileHeader getFileHeader() {
        return this.file_header;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Use {@link #RMLFile(String)} to provide a name for the file.
     * <p>A name isn't necessary and is only
     * used for serializing into other formats (e.g. JSON)
     * or writing to a file</p>
     */
    public RMLFile() {
        this.name = null;
    }

    /**
     * @param name The name of the RMLFile
     */
    public RMLFile(String name) {
        this.name = name;
    }

    @Override
    public Iterator<Section> iterator() {
        return sections.values().iterator();
    }


    /**
     * The header section of a {@link RMLFile} that might contain
     * file information and metadata
     * <p>Only appending ({@link #append(String)}) is allowed for now</p>
     */
    public static final class RMLFileHeader implements Iterable<RMLString> {
        private final RMLValue<RMLString> value = new RMLValue<>();

        public void append(String text) {
            value.add(new RMLString(text));
        }

        @Override
        public Iterator<RMLString> iterator() {
            return value.iterator();
        }
    }
}
