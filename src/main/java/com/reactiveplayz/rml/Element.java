package com.reactiveplayz.rml;

/**
 * An abstract class to create new Elements
 * <p>Native elements to RML are {@link Section}, {@link SubSection},
 * {@link KeyValueElement}, {@link RMLList}, and {@link Comment}</p>
 */
public abstract class Element {
    protected Sections parentSection = null;

    /**
     * Returns a RML representation of this Element as a String
     * @return A RML representation of this Element as a String
     */
    abstract public String toString();

    /**
     * Returns the Section that this Element belongs to
     * @return the Section that this Element belongs to
     */
    public Sections getParentSection() {
        return parentSection;
    }

    /**
     * Sets the Section that this Element should belong to
     * @param section The Section that this Element should belong to
     */
    void setParentSection(Sections section) {
        parentSection = section;
    }
}
