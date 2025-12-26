package com.reactiveplayz.rml;

import java.util.Iterator;

/**
 * The header section of a {@link RMLFile} that might contain
 * file information and metadata
 * <p>Only appending ({@link #append(String)}) is allowed for now</p>
 * This class is likely to be replaced as a subclass of {@link RMLFile}
 */
public final class RMLFileHeader implements Iterable<RMLString> {
    private final RMLValue<RMLString> value = new RMLValue<>();

    public void append(String text) {
        value.add(new RMLString(text));
    }

    @Override
    public Iterator<RMLString> iterator() {
        return value.iterator();
    }

}
