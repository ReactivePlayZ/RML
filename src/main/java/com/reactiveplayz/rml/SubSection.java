package com.reactiveplayz.rml;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A SubSection is also an {@link Element} except it extends {@link Section}
 * <p>It additionally has a {@code parentSection}</p>
 * (SubSections have a syntax of {@code (subsection name)} in RML)
 */
public class SubSection extends Section {

    /**
     * A SubSection can't hold other instances of {@link Section}.
     * Therefore, a {@link NoSuchElementException} is thrown if this
     * method is called
     */
    @Override
    public SubSection getSubSection(String name) {
        throw new NoSuchElementException("A SubSection can't hold any SubSections, therefore none can be returned.");
    }

    /**
     * A SubSection doesn't contain any instances of {@link Section}s. Therefore,
     * this method is redundant and throws a {@link UnsupportedOperationException}
     * @throws UnsupportedOperationException This method isn't meant to be called
     *                                       by any child class of {@link Section}
     */
    @Override
    public Iterator<SubSection> subSectionIterator() {
        throw new UnsupportedOperationException("SubSection class can't contain SubSections, therefore there isn't an iterator for SubSections");
    }

    /**
     * <strong>Always throws</strong> {@link UnsupportedOperationException}
     * because {@code getAnyFirstMatchingKey} belongs to {@link Section} only
     * ({@link Section#getAnyFirstMatchingKey}) and there is no further depth
     * to search through
     * @param key <p></p>
     * @return <p></p>
     */
    @Override
    public KeyValueElement getAnyFirstMatchingKey(String key) {
        throw new UnsupportedOperationException("Can't search any further as SubSections can't contain SubSections");
    }

    /**
     * <strong>Always throws</strong> {@link UnsupportedOperationException}
     * because {@code getAnyFirstElementValueMatch} belongs to {@link Section} only
     * ({@link Section#getAnyFirstElementValueMatch(String)}) and there is no further
     * depth to search through
     * @param value <p></p>
     * @return <p></p>
     */
    @Override
    public Element getAnyFirstElementValueMatch(String value) {
        throw new UnsupportedOperationException("Can't search any further as SubSections can't contain SubSections");
    }

    /**
     * <strong>Always throws</strong> {@link UnsupportedOperationException}
     * because {@code getAnyFirstElementValueMatch} belongs to {@link Section} only
     * ({@link Section#getAnyFirstElementValueMatch(String, boolean)}) and there is no further
     * depth to search through
     * @param value <p></p>
     * @param regex <p></p>
     * @return <p></p>
     */
    @Override
    public Element getAnyFirstElementValueMatch(String value, boolean regex) {
        throw new UnsupportedOperationException("Can't search any further as SubSections can't contain SubSections");
    }

    /**
     * <strong>Always throws</strong> {@link UnsupportedOperationException}
     * because {@code getAnyElementValueMatches} belongs to {@link Section} only
     * ({@link Section#getAnyElementValueMatches(String)}) and there is no further
     * depth to search through
     * @param value <p></p>
     * @return <p></p>
     */
    @Override
    public List<Element> getAnyElementValueMatches(String value) {
        throw new UnsupportedOperationException("Can't search any further as SubSections can't contain SubSections");
    }

    /**
     * <strong>Always throws</strong> {@link UnsupportedOperationException}
     * because {@code getAnyElementValueMatches} belongs to {@link Section} only
     * ({@link Section#getAnyElementValueMatches(String, boolean)}) and there is no further
     * depth to search through
     * @param value <p></p>
     * @param regex <p></p>
     * @return <p></p>
     */
    @Override
    public List<Element> getAnyElementValueMatches(String value, boolean regex) {
        throw new UnsupportedOperationException("Can't search any further as SubSections can't contain SubSections");
    }

    /**
     * Always returns {@code false} as a SubSection can't contain SubSections
     * @return {@code false} as a SubSection can't contain SubSections
     */
    @Override
    public boolean containsSubSection(String name) {
        return false;
    }

    /**
     * Always returns {@code false} as a SubSection can't contain SubSections
     * @return {@code false} as a SubSection can't contain SubSections
     */
    public boolean containsSubSection(SubSection subSection) {
        return false;
    }

    /**
     * Adds an {@link Element} to this SubSection
     * @param element The Element to add
     * @throws IllegalArgumentException If another instance of a Section is
     *                                  added then this exception is thrown
     *                                  as Section instances can not be added
     *                                  to a Section
     */
    @Override
    public void add(Element element) {
        if (element instanceof Section) {
            throw new IllegalArgumentException("Sections can't go within Sections");
        }
        super.add(element);
    }

    public SubSection(String name, Section parentSection) {
        super(name);
        super.setParentSection(parentSection);
    }

    public SubSection(RMLString name, Section parentSection) {
        super(name);
        super.setParentSection(parentSection);
    }

    /**
     * Returns the String representation of this SubSection
     * in RML as a String
     * @return The String representation of this SubSection
     *         in RML as a String
     */
    @Override
    public String toString() {
        return "(" + super.getName().raw() + ")";
    }
}
