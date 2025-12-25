package com.reactiveplayz.rml;

public class SubSection extends Section {
    private Section parentSection;

    SubSection() {
    }

    SubSection(String name) {
        super.setName(name);
    }

    SubSection(RMLString name) {
        super.setName(name);
    }

    SubSection(String name, Section parentSection) {
        this(new RMLString(name), parentSection);
    }

    SubSection(RMLString name, Section parentSection) {
        super.setName(name);
        this.parentSection = parentSection;
    }

    public Section getParrentSection() {
        return parentSection;
    }

    public void setParentSection(Section newSection) {
        parentSection = newSection;
    }
}
