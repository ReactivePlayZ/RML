package com.reactiveplayz.rml;

import java.util.*;

/**
 * A SubSection is an {@link ElementHolder} that is meant to be
 * present in a {@link Section}. It is a one deep nesting of Elements in a RMLFile
 * <p>
 *     It additionally has a {@code parentSection}
 * </p>
 * (SubSections have a syntax of {@code (subsection name)} in RML)
 */
public class SubSection extends Element implements Sections, Iterable<Element> {
    private final RMLString name;
    private final ElementHolder elements = new ElementHolder();
    private final Comment comment = new Comment();

    /**
     * Returns the Element at the specified position in this SubSection
     *
     * @param index index of the Element to return
     * @return The element at the specified position in this SubSection
     * @throws IndexOutOfBoundsException If the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    public Element get(int index) {
        Objects.checkIndex(index, elements.size());
        return elements.get(index);
    }

    /**
     * Gets the first Element in this SubSection
     *
     * @return the retrieved Element
     * @throws NoSuchElementException When the SubSection is empty
     */
    public Element getFirst() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("The SubSection is empty");
        }
        return elements.getFirst();
    }

    /**
     * Gets the last Element in this SubSection
     *
     * @return the retrieved Element
     * @throws NoSuchElementException When the SubSection is empty
     */
    public Element getLast() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("The SubSection is empty");
        }
        return elements.getLast();
    }

    /**
     * Returns the Elements stored in this SubSection as an unmodifiable
     * {@link Collection}
     *
     * @return the Elements stored in this SubSection as an unmodifiable
     *         {@link Collection}
     */
    public Collection<Element> getElementsAsCollection() {
        return elements.getElementsAsCollection();
    }

    /**
     * Returns the Elements stored in this SubSection as an unmodifiable {@link List}
     *
     * @return the Elements stored in this SubSection as an unmodifiable {@link List}
     */
    public List<Element> getElementsAsList() {
        return elements.getElementsAsList();
    }

    /**
     * Returns the number of Elements in this SubSection
     *
     * @return The number of Elements in this SubSection
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns {@code true} if this SubSection contains no Elements
     *
     * @return {@code true} if this SubSection contains no Elements
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Element element) {
        return elements.contains(element);
    }

    @Override
    public boolean containsKey(String key) {
        return elements.containsKey(key);
    }

    @Override
    public KeyValueElement getKey(String key) {
        return elements.getKey(key);
    }

    @Override
    public Optional<Element> getFirstElementValueMatch(String value) {
        return elements.getFirstElementValueMatch(value);
    }

    @Override
    public Optional<Element> getFirstElementValueMatch(String value, boolean regex) {
        return elements.getFirstElementValueMatch(value, regex);
    }

    @Override
    public List<Element> getElementValueMatches(String value) {
        return elements.getElementValueMatches(value);
    }

    @Override
    public List<Element> getElementValueMatches(String value, boolean regex) {
        return elements.getElementValueMatches(value, regex);
    }

    /**
     * Adds an array of {@link Element}s to this SubSection
     * <p>
     * Note that each Element is going through
     * {@link #add(Element, boolean)} where the boolean value is {@code false}
     * </p>
     *
     * @param elements The Elements to add
     */
    public void add(Element[] elements) {
        for (Element e : elements) {
            add(e, false);
        }
    }

    /**
     * Adds an {@link Element} to this SubSection.
     * <p>
     * Note that, {@link #add(Element, boolean)} is being called
     * where the first argument is the {@code element} and the second
     * argument is {@code false}.
     * </p>
     *
     * @param element The Element to add
     * @throws IllegalArgumentException When attempting to add an instance
     *                                  of a SubSection to this SubSection
     */
    public void add(Element element) {
        add(element, false);
    }

    @Override
    public boolean remove(Element element) {
        return elements.remove(element);
    }

    @Override
    public Element remove(int index) {
        return elements.remove(index);
    }

    @Override
    public Comment getComment() {
        return comment;
    }

    /**
     * Adds an {@link Element} to this SubSection
     * <hr>
     * <strong>
     * The following Javadoc is for duplicate {@linkplain KeyValueElement}s:
     * </strong>
     * <br>
     * If the {@code element} is a {@link KeyValueElement} and its key is already
     * in the section and {@code replace} is true, then {@code element}
     * replaces the already existing {@link KeyValueElement} in the same index
     * with the {@code element}. If {@code replace} is false, then {@code element}
     * is
     * added to the end and the already existing KeyValueElement is removed so that
     * there are no duplicate keys.
     * <p>
     *     For example:
     * <pre>{@code
     *     SubSection subSectionA = new SubSection("SubSection A");
     *     subSectionA.add(new KeyValueElement("key1"));
     *     subSectionA.add(new KeyValueElement("key2"));
     *     System.out.println(subSectionA.getElementsAsList()); // [key1: , key2: ]
     *     subSectionA.add(new KeyValueElement("key1", new RMLString("abc")));
     *     System.out.println(subSectionA.getElementsAsList()); // [key2: , key1: abc]
     *     subSectionA.add(new KeyValueElement("key2", new RMLString("xyz")), true);
     *     System.out.println(subSectionA.getElementsAsList()); // [key2: xyz, key1: abc]
     *
     * }</pre>
     * </p>
     * <hr>
     * If there are no duplicate keys for the {@code element} being added (Given
     * that it is a {@link KeyValueElement}) then {@code replace} is ignored
     *
     * @param element The {@link Element} to add to this SubSection
     * @param replace Should the {@code element} replace an already existing
     *                {@link KeyValueElement} with the same key in the same
     *                position? If not then append and remove the
     *                already existing one, given that there are duplicates
     * @throws IllegalArgumentException If another instance of a SubSection is
     *                                  added then this exception is thrown
     *                                  as SubSection instances can not be added
     *                                  to a SubSection
     */
    public void add(Element element, boolean replace) {
        if (element instanceof SubSection) {
            throw new IllegalArgumentException("SubSections can't be added to SubSections");
        }
        if (element instanceof KeyValueElement kv &&
                elements.containsKey(kv.getKey())) {
            elements.getKey(kv.getKey()).setParentSection(null);
        }
        elements.add(element, replace);
        element.setParentSection(this);
    }

    public RMLString getName() {
        return name;
    }

    /**
     * Creates a SubSection with a name and a parent section
     * @param name The name to assign to this SubSection
     * @param parentSection The Section that this SubSection should belong to
     */
    public SubSection(String name, Section parentSection) {
        this(new RMLString(name), (Sections) parentSection);
    }

    /**
     * Creates a SubSection with a name and a parent section
     * @param name The name to assign to this SubSection
     * @param parentSection The Section that this SubSection should belong to
     */
    public SubSection(RMLString name, Section parentSection) {
        this(name, (Sections) parentSection);
    }

    /**
     * Creates a SubSection with a name and a parent section
     * @param name The name to assign to this SubSection
     * @param parentSection The Section that this SubSection should belong to
     */
    public SubSection(String name, Sections parentSection) {
        this(new RMLString(name), parentSection);
    }

    /**
     * Creates a SubSection with a name and a parent section
     * @param name The name to assign to this SubSection
     * @param parentSection The Section that this SubSection should belong to
     */
    public SubSection(RMLString name, Sections parentSection) {
        if (parentSection instanceof SubSection) {
            throw new IllegalArgumentException("SubSections can't be a parent section of another SubSection");
        }
        this.name = name;
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
        return "(" + name.raw() + ")";
    }

    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }
}
