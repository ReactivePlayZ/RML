package com.reactiveplayz.rml;

import java.util.*;
import java.util.regex.Pattern;

/**
 * A Section is a {@link Element} that stores elements
 * except for Sections themselves
 * <p>
 *     It contains a {@code name}, {@code elements}, and a {@code comment}
 * </p>
 * (It has a syntax of {@code = section name =} in RML)
 */
public class Section extends Element implements Sections, Iterable<Element> {
    /** A {@link Section}'s unique identifier in a {@link RMLFile} */
    private final RMLString name;
    private final ElementHolder elements = new ElementHolder();
    /**
     * The {@link SubSection}s that are present in this Section
     * and their unique identifiers (their name)
     * <p>
     * <strong>LinkedHashMap Key</strong>: {@code name} of the SubSection,
     * <br>
     * <strong>Value</strong>: The {@link SubSection}
     * </p>
     */
    private final LinkedHashMap<String, SubSection> subSections = new LinkedHashMap<>();
    private final Comment comment = new Comment();

    public Element get(int index) {
        Objects.checkIndex(index, elements.size());
        return elements.get(index);
    }

    public Element getFirst() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("The Section is empty");
        }
        return elements.getFirst();
    }

    public Element getLast() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("The Section is empty");
        }
        return elements.getLast();
    }

    public Collection<Element> getElementsAsCollection() {
        return elements.getElementsAsCollection();
    }

    public List<Element> getElementsAsList() {
        return elements.getElementsAsList();
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Adds an array of {@link Element}s to this Section
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
     * Adds an {@link Element} to this Section.
     * <p>
     * Note that, {@link #add(Element, boolean)} is being called
     * where the first argument is the {@code element} and the second
     * argument is {@code false}.
     * </p>
     * 
     * @param element The Element to add
     * @throws IllegalArgumentException When attempting to add an instance
     *                                  of a Section to this Section
     */
    public void add(Element element) {
        add(element, false);
    }

    /**
     * Adds an {@link Element} to this Section
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
     *     Section sectionA = new Section("Section A");
     *     sectionA.add(new KeyValueElement("key1"));
     *     sectionA.add(new KeyValueElement("key2"));
     *     System.out.println(sectionA.getElementsAsList()); // [key1: , key2: ]
     *     sectionA.add(new KeyValueElement("key1", new RMLString("abc")));
     *     System.out.println(sectionA.getElementsAsList()); // [key2: , key1: abc]
     *     sectionA.add(new KeyValueElement("key2", new RMLString("xyz")), true);
     *     System.out.println(sectionA.getElementsAsList()); // [key2: xyz, key1: abc]
     *
     * }</pre>
     * </p>
     * <hr>
     * If there are no duplicate keys for the {@code element} being added (Given
     * that it is a {@link KeyValueElement}) then {@code replace} is ignored
     *
     * @param element The {@link Element} to add to this Section
     * @param replace Should the {@code element} replace an already existing
     *                {@link KeyValueElement} with the same key in the same
     *                position? If not then append and remove the
     *                already existing one, given that there are duplicates
     * @throws IllegalArgumentException If another instance of a Section is
     *                                  added then this exception is thrown
     *                                  as Section instances can not be added
     *                                  to a Section
     */
    public void add(Element element, boolean replace) {
        if (element instanceof Section) {
            throw new IllegalArgumentException("Sections can't be added to Sections");
        }
        if (element instanceof KeyValueElement kv &&
                elements.containsKey(kv.getKey())) {
            elements.getKey(kv.getKey()).setParentSection(null);
        }
        if (element instanceof SubSection subSection) {
            if (subSections.containsKey(subSection.getName().raw())) {
                int subSectionPos = getSubSectionPos(subSection.getName().raw());
                elements.get(subSectionPos).setParentSection(null);
                elements.remove(subSectionPos);
                subSections.remove(subSection.getName().raw());
            }
            subSections.put(subSection.getName().raw(), subSection);
        }
        elements.add(element, replace);
        element.setParentSection(this);
    }

    /**
     * Returns the first key match that can be found including within
     * {@link SubSection}s.
     * <hr>
     * Note that this is a linear search on all Elements and {@link SubSection}
     * Elements to
     * check if there is a matching {@code key}. Use {@link #getKey(String)} if only
     * checking within this Section and there isn't a need for SubSection checking
     * 
     * @param key The key to use and find the first {@link KeyValueElement} with
     *            a matching key
     * @return The first key match that can be found including within
     *         {@link SubSection}s.
     */
    public KeyValueElement getAnyFirstMatchingKey(String key) {
        for (Element e : elements) {
            if (e instanceof KeyValueElement kv && kv.getKey().equals(key)) {
                return kv;
            }
            if (e instanceof SubSection ss && ss.containsKey(key)) {
                KeyValueElement ssKeyMatch = ss.getKey(key);
                if (ssKeyMatch != null) {
                    return ssKeyMatch;
                }
            }
        }
        return null;
    }

    /**
     * Returns all {@code key} matches that can be found
     * including within {@link SubSection}s
     * 
     * @param key The key to match with and find other {@link KeyValueElement}s
     *            with the same key
     * @return all {@code key} matches that can be found
     *         including within {@link SubSection}s in an {@link ArrayList}
     */
    public List<KeyValueElement> getAllMatchingKeys(String key) {
        ArrayList<KeyValueElement> matchingKeys = new ArrayList<>();

        for (Element e : elements) {
            if (e instanceof KeyValueElement kv && kv.getKey().equals(key)) {
                matchingKeys.addLast(kv);
            } else if (e instanceof SubSection ss && ss.containsKey(key)) {
                matchingKeys.addLast(ss.getKey(key));
            }
        }

        return matchingKeys;
    }

    /**
     * Returns a {@link KeyValueElement} with a specific Key that is
     * present in this Section
     * <hr>
     * <p>
     *     Use {@link #containsKey(String)} first to check if the Key of
     *     a KeyValueElement is present in this Section
     * </p>
     * @return A {@link KeyValueElement} with a specific Key that is
     *         present in this Section
     */
    public KeyValueElement getKey(String key) {
        return elements.getKey(key);
    }

    /**
     * Returns the first {@link Element} where it's {@link RMLValue}
     * contains a specific value
     * 
     * @param value The specific value to look for
     * @return The first {@link Element} where it's {@link RMLValue}
     *         contains a specific value
     */
    public Optional<Element> getFirstElementValueMatch(String value) {
        return elements.getFirstElementValueMatch(value);
    }

    /**
     * Returns the first {@link Element} where it's {@link RMLValue}
     * properties match a Regular Expression if enabled. If not enabled
     * then calls {@link #getFirstElementValueMatch(String)}
     * 
     * @param value The value to look for, which can be RegEx
     * @param regex Should RegEx be used?
     * @return The first {@link Element} where it's {@link RMLValue}
     *         properties match a Regular Expression or a specific value
     */
    public Optional<Element> getFirstElementValueMatch(String value, boolean regex) {
        return elements.getFirstElementValueMatch(value, regex);
    }

    /**
     * Returns the first {@link Element} where it's {@link RMLValue}
     * contains a specific value. Also checks within {@link SubSection}s
     * by calling the SubSection's
     * {@link SubSection#getFirstElementValueMatch(String)}
     * 
     * @param value The specific value to look for
     * @return The first {@link Element} where it's {@link RMLValue}
     *         contains a specific value.
     *         <p>
     *         When there is no match, {@code null} is returned
     *         </p>
     */
    public Optional<Element> getAnyFirstElementValueMatch(String value) {
        for (Element e : elements) {
            if (e instanceof KeyValueElement kv && kv.getValue().contains(new RMLString(value))) {
                return Optional.of(kv);
            }
            if (e instanceof RMLList list && list.getList().contains(new RMLString(value))) {
                return Optional.of(list);
            }
            if (e instanceof Comment note && note.contains(new RMLString(value))) {
                return Optional.of(note);
            }
            if (e instanceof SubSection sub) {
                if (sub.getName().raw().equals(value)) {
                    return Optional.of(sub);
                }
                Optional<Element> match = sub.getFirstElementValueMatch(value);
                if (match.isPresent()) {
                    return match;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the first {@link Element} where it's {@link RMLValue}
     * properties match a Regular Expression if enabled. If not enabled
     * then calls {@link #getAnyFirstElementValueMatch(String)}. Also checks
     * within {@link SubSection}s by calling the SubSection's
     * {@link SubSection#getFirstElementValueMatch(String, boolean)}
     * 
     * @param value The value to look for, which can be RegEx
     * @param regex Should RegEx be used?
     * @return The first {@link Element} where it's {@link RMLValue}
     *         properties match a Regular Expression or a specific value.
     *         <p>
     *         When there is no match, {@code null} is returned
     *         </p>
     */
    public Optional<Element> getAnyFirstElementValueMatch(String value, boolean regex) {
        if (!regex) {
            return getAnyFirstElementValueMatch(value);
        }
        Pattern pattern = Pattern.compile(value);
        for (Element e : elements) {
            if (e instanceof KeyValueElement kv) {
                for (RMLType f : kv) {
                    if (f instanceof RMLString g && pattern.matcher(g.raw()).find()) {
                        return Optional.of(kv);
                    }
                }
            }
            if (e instanceof RMLList list) {
                for (RMLType f : list) {
                    if (f instanceof RMLString g && pattern.matcher(g.raw()).find()) {
                        return Optional.of(list);
                    }
                }
            }
            if (e instanceof Comment note) {
                for (RMLString f : note) {
                    if (pattern.matcher(f.raw()).find()) {
                        return Optional.of(note);
                    }
                }
            }
            if (e instanceof SubSection sub) {
                if (pattern.matcher(sub.getName().raw()).find()) {
                    return Optional.of(sub);
                }
                Optional<Element> match = sub.getFirstElementValueMatch(value, true);
                if (match.isPresent()) {
                    return match;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns all {@link Element}s where their {@link RMLValue}
     * contains a specific value
     * 
     * @param value The specific value to look for
     * @return A {@link List} of all {@link Element}s where
     *         their {@link RMLValue} contains a specific value.
     *         <p>
     *         When there is no match, an empty List is returned
     *         </p>
     */
    public List<Element> getElementValueMatches(String value) {
        return elements.getElementValueMatches(value);
    }

    /**
     * Returns all {@link Element}s where their {@link RMLValue}
     * properties match a Regular Expression if enabled. If not enabled
     * then calls {@link #getElementValueMatches(String)}
     * 
     * @param value The value to look for, which can be RegEx
     * @param regex Should RegEx be used?
     * @return An {@link List} of all {@link Element}s
     *         where their {@link RMLValue} properties match a Regular Expression
     *         or a specific value.
     *         <p>
     *         When there is no match, an empty List is returned
     *         </p>
     */
    public List<Element> getElementValueMatches(String value, boolean regex) {
        return elements.getElementValueMatches(value, regex);
    }

    /**
     * Returns all {@link Element}s where their {@link RMLValue}
     * contains a specific value. Also checks within {@link SubSection}s
     * by calling the SubSection's {@link SubSection#getElementValueMatches(String)}
     * 
     * @param value The specific value to look for
     * @return An {@link List} of all {@link Element}s
     *         where their {@link RMLValue} contains a specific value.
     *         <p>
     *         When there is no match, an empty List is returned
     *         </p>
     */
    public List<Element> getAnyElementValueMatches(String value) {
        ArrayList<Element> returnElements = new ArrayList<>(getElementValueMatches(value));
        for (SubSection sub : subSections.values()) {
            returnElements.addAll(sub.getElementValueMatches(value));
        }
        return returnElements;
    }

    /**
     * Returns all {@link Element}s where their {@link RMLValue}
     * properties match a Regular Expression if enabled. If not enabled
     * then calls {@link #getAnyElementValueMatches(String)}. Also checks
     * within {@link SubSection}s by calling the SubSection's
     * {@link SubSection#getElementValueMatches(String, boolean)}
     * 
     * @param value The value to look for, which can be RegEx
     * @param regex Should RegEx be used?
     * @return An {@link List} of all {@link Element}s
     *         where their {@link RMLValue} properties match a Regular Expression
     *         or a specific value.
     *         <p>
     *         When there is no match, an empty List is returned
     *         </p>
     */
    public List<Element> getAnyElementValueMatches(String value, boolean regex) {
        if (!regex) {
            return getAnyElementValueMatches(value);
        }
        ArrayList<Element> returnElements = new ArrayList<>(getElementValueMatches(value, true));
        for (SubSection sub : subSections.values()) {
            returnElements.addAll(sub.getElementValueMatches(value, true));
        }
        return returnElements;
    }

    /**
     * Use {@link #containsSubSection(String)} first to check if the SubSection is
     * in this Section
     */
    public SubSection getSubSection(String name) {
        return subSections.get(name);
    }

    /**
     * Returns the index of a {@link KeyValueElement} by checking it's {@code key}
     * 
     * @param key The key to find a matching {@link KeyValueElement} with the same
     *            key
     * @return The index of a {@link KeyValueElement} by checking it's {@code key}
     *         and if no match is found, then -1 is returned instead
     */
    public int getKeyPos(String key) {
        return elements.getKeyPos(key);
    }

    /**
     * Returns {@code true} if this Section contains a
     * {@link KeyValueElement} with the specified key
     * 
     * @param key The key to test and see if a matching
     *            KeyValueElement is present or not
     *            within this Section
     * @return {@code true} if this Section contains a
     *         {@link KeyValueElement} with the specified key
     */
    public boolean containsKey(String key) {
        return elements.containsKey(key);
    }

    /**
     * Returns {@code true} if this Section contains a specific
     * {@link KeyValueElement}
     * 
     * @param keyValueElement The KeyValueElement to test and see if it is
     *                        present or not in this Section
     * @return {@code true} if this Section contains a specific
     *         {@link KeyValueElement}
     */
    public boolean containsKey(KeyValueElement keyValueElement) {
        return elements.containsKey(keyValueElement);
    }

    /**
     * Returns {@code true} if this Section contains the specified Element
     *
     * @param element The Element to test and see if it is
     *                present or not in this Section
     * @return {@code true} if this Section contains the specified Element
     */
    public boolean contains(Element element) {
        return elements.contains(element);
    }

    /**
     * Returns the index of a {@link SubSection} by checking it's {@code name}
     *
     * @param name The name to find a matching {@link SubSection} with the same name
     * @return The index of a {@link SubSection} by checking it's {@code name}
     *         and if no match is found, then -1 is returned instead
     */
    public int getSubSectionPos(String name) {
        return elements.indexOf(subSections.get(name));
    }

    /**
     * Returns the index of a {@link SubSection} by checking it's {@code name}
     *
     * @param name The name to find a matching {@link SubSection} with the same name
     * @return The index of a {@link SubSection} by checking it's {@code name}
     *         and if no match is found, then -1 is returned instead
     */
    public int getSubSectionPos(RMLString name) {
        return elements.indexOf(subSections.get(name.raw()));
    }

    /**
     * Returns {@code true} if this Section contains a
     * {@link SubSection} with a specific name
     * 
     * @param name The name to test and see if a matching
     *             SubSection is present or not within
     *             this Section
     * @return {@code true} if this Section contains a
     *         {@link SubSection} with a specific name
     */
    public boolean containsSubSection(String name) {
        return subSections.containsKey(name);
    }

    /**
     * Returns {@code true} if this Section contains a specific
     * {@link SubSection}
     * 
     * @param subSection The SubSection to test and see if it is
     *                   present or not in this Section
     * @return {@code true} if this Section contains a specific
     *         {@link SubSection}
     */
    public boolean containsSubSection(SubSection subSection) {
        return subSections.containsValue(subSection);
    }

    /**
     * Returns the name of this Section as a {@link RMLString}
     * 
     * @return The name of this Section as a {@link RMLString}
     */
    public RMLString getName() {
        return name;
    }

    /**
     * Returns the {@link Comment} stored in this Section
     * 
     * @return The {@link Comment} stored in this Section
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Removes the Element at the specified position in this Section.
     * Shifts any subsequent Elements to the left (subtracts one
     * from their indices)
     * 
     * @param index The index of the Element to be removed
     * @return The Element that was removed from this Section
     * @throws IndexOutOfBoundsException If the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    public Element remove(int index) {
        Objects.checkIndex(index, elements.size());
        elements.get(index).setParentSection(null);
        if (elements.get(index) instanceof SubSection sub) {
            subSections.remove(sub.getName().raw());
        }
        return elements.remove(index);
    }

    /**
     * Returns {@code true} if this Section contained the specified element
     * (or equivalently, if this Section changed as a result of the call)
     *
     * @param element The Element to be removed from this Section, if present
     * @return {@code true} if this Section contained the specified element
     *         (or equivalently, if this Section changed as a result of the call)
     */
    public boolean remove(Element element) {
        boolean removeOperation = elements.remove(element);
        if (removeOperation) {
            if (element instanceof SubSection sub) {
                subSections.remove(sub.getName().raw());
            }
            element.setParentSection(null);
        }
        return removeOperation;
    }

    /**
     * Constructs a new Section with a specific name as it's identifier
     * 
     * @param name The name for this Section
     */
    public Section(String name) {
        this.name = new RMLString(name);
    }

    /**
     * Constructs a new Section with a specific name as it's identifier
     * 
     * @param name The name for this Section
     */
    public Section(RMLString name) {
        this.name = name;
    }

    /**
     * Returns the String representation of this Section
     * in RML as a String
     * 
     * @return The String representation of this Section
     *         in RML as a String
     */
    @Override
    public String toString() {
        return "= " + this.name.raw() + " =";
    }

    /**
     * Iterator for all {@link Element}s in this Section. Includes
     * {@link SubSection} but not their Elements as Sections and SubSections
     * are Elements themselves that store other Elements
     * <p>Note that it is an iterator over an unmodifiable {@link List}</p>
     */
    @Override
    public Iterator<Element> iterator() {
        return this.elements.iterator();
    }

    /**
     * Iterator for all {@link SubSection}s in this Section
     * <p>Note that it is an iterator over an unmodifiable {@link Collection}
     * where the order is only guaranteed based on the values of {@link #subSections}</p>
     */
    public Iterator<SubSection> subSectionIterator() {
        return Collections.unmodifiableCollection(this.subSections.values()).iterator();
    }

    /**
     * Iterator for all {@link KeyValueElement}s in this Section
     * <p>Note that it is an iterator over an unmodifiable {@link List}</p>
     */
    public Iterator<KeyValueElement> keyValueElementIterator() {
        return this.elements.keyValueElementIterator();
    }

}