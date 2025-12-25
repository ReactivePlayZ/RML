package com.reactiveplayz.rml;

import java.util.ArrayList;

public class Section extends Element {
    private ArrayList<Element> elements = new ArrayList<>();
    private RMLString name;
    private RMLValue<RMLString> comment = new RMLValue<>();

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

    Section() {
    }

    Section(String name) {
        this.name = new RMLString(name);
    }

    Section(RMLString name) {
        this.name = name;
    }

}