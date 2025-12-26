package com.reactiveplayz.rml;

import java.util.ArrayList;

/**
 * A Section is a {@link Element} that stores elements
 * except for Sections themselves
 * <p>(It has a syntax of {@code = section name =} in RML)</p>
 * It contains a {@code name}, {@code elements}, and a {@code comment}
 */
public class Section extends Element {
    private final ArrayList<Element> elements = new ArrayList<>();
    private RMLString name;
    private final RMLValue<RMLString> comment = new RMLValue<>();

    public ArrayList<Element> getElements() {
        return elements;
    }

    public RMLString getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = new RMLString(newName);
    }

    public void setName(RMLString newName) {
        this.name = newName;
    }

    public RMLValue<RMLString> getComment() {
        return comment;
    }

    public Section(String name) {
        this.name = new RMLString(name);
    }

    public Section(RMLString name) {
        this.name = name;
    }

}