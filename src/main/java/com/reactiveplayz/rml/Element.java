package com.reactiveplayz.rml;

/**
 * An abstract class to create new Elements
 * <p>Native elements to RML are {@link Section}, {@link SubSection},
 * {@link KeyValueElement}, {@link RMLList}, and {@link Comment}</p>
 */
public abstract class Element {
    protected Section parentSection = null;

    /**
     * Returns a RML representation of this Element as a String
     * @return A RML representation of this Element as a String
     */
    abstract public String toString();

    /**
     * Returns the {@link Section} that this Element belongs to
     * @return the {@link Section} that this Element belongs to
     */
    public Section getParentSection() {
        return parentSection;
    }

    /**
     * Sets the {@link Section} that this Element should belong to
     * @param section The Section that this Element should belong to
     */
    protected void setParentSection(Section section) {
        parentSection = section;
    }
}
