package com.reactiveplayz.rml;

import java.util.LinkedHashSet;

/**
 * A RMLFile instance holds the file's {@code name},
 * the {@code file_header} ({@link RMLFileHeader}), and
 * all of it's {@code sections} in a {@code LinkedHashSet}
 */
public final class RMLFile {

    public final String name;
    public final RMLFileHeader file_header = new RMLFileHeader();
    public final LinkedHashSet<Section> sections = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    /**
     * Use {@link #RMLFile(String)} to provide a name for the file.
     * <p>A name isn't necessary and is only
     * used for serializing into other formats (e.g. JSON)
     * or writing to a file</p>
     */
    public RMLFile() {
        this.name = null;
    };

    /**
     * @param name The name of the RMLFile
     */
    public RMLFile(String name) {
        this.name = name;
    }
}
