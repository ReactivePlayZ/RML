package com.reactiveplayz.rml;

import java.util.LinkedHashSet;

public final class RMLFile {

    public final String name;
    public final RMLFileHeader file_header = new RMLFileHeader();
    public final LinkedHashSet<Section> sections = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    public RMLFile(String name) {
        this.name = name;
    }
}
