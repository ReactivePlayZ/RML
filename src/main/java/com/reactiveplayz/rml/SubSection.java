package com.reactiveplayz.rml;

/**
 * A SubSection is also an {@link Element} except it extends {@link Section}
 * <p>(SubSections have a syntax of {@code (subsection name)} in RML)</p>
 * <p>It additionally has a {@code parentSection}</p>
 */
public class SubSection extends Section {
    private Section parentSection;

    public SubSection(String name, Section parentSection) {
        super(name);
        this.parentSection = parentSection;
    }

    public SubSection(RMLString name, Section parentSection) {
        super(name);
        this.parentSection = parentSection;
    }

    public Section getParrentSection() {
        return parentSection;
    }

    public void setParentSection(Section newSection) {
        parentSection = newSection;
    }
}
