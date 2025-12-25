package com.reactiveplayz.rml;

import java.util.Iterator;

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
