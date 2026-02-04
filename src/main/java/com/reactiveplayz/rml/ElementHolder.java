package com.reactiveplayz.rml;

import java.util.*;
import java.util.regex.Pattern;

/**
 * An ElementHolder is a container for {@link Element}s excluding {@link Section}s
 *
 * <p>Internally uses an {@link ArrayList} to hold Elements in order to guarantee
 * order</p>
 */
public class ElementHolder implements Iterable<Element> {
    private final ArrayList<Element> elements = new ArrayList<>();
    /**
     * The {@link KeyValueElement}s that are present in this ElementHolder
     * and their Keys
     * <p>
     * <strong>LinkedHashMap Key</strong>: {@code key} of the KeyValueElement,
     * <br>
     * <strong>Value</strong>: the {@link KeyValueElement}
     * </p>
     */
    private final LinkedHashMap<String, KeyValueElement> keyValues = new LinkedHashMap<>();

    /**
     * Returns the element at the specified position in this ElementHolder
     * 
     * @param index index of the Element to return
     * @return The element at the specified position in this ElementHolder
     * @throws IndexOutOfBoundsException If the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    public Element get(int index) {
        Objects.checkIndex(index, elements.size());
        return elements.get(index);
    }

    /**
     * Gets the first Element in this ElementHolder
     * 
     * @return The retrieved Element
     * @throws NoSuchElementException If the ElementHolder is empty
     */
    public Element getFirst() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("The ElementHolder is empty");
        }
        return elements.getFirst();
    }

    /**
     * Gets the last Element in this ElementHolder
     * 
     * @return The retrieved Element
     * @throws NoSuchElementException If the ElementHolder is empty
     */
    public Element getLast() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("The ElementHolder is empty");
        }
        return elements.getLast();
    }

    /**
     * Returns the Elements stored in this ElementHolder as an unmodifiable
     * {@link Collection}
     * 
     * @return The Elements stored in this ElementHolder as an unmodifiable
     *         {@link Collection}
     */
    public Collection<Element> getElementsAsCollection() {
        return Collections.unmodifiableCollection(this.elements);
    }

    /**
     * Returns the Elements stored in this ElementHolder as an unmodifiable {@link List}
     * 
     * @return The Elements stored in this ElementHolder as an unmodifiable {@link List}
     */
    public List<Element> getElementsAsList() {
        return Collections.unmodifiableList(this.elements);
    }

    /**
     * Returns the number of Elements in this ElementHolder
     * 
     * @return The number of Elements in this ElementHolder
     */
    public int size() {
        return elements.size();
    }

    /**
     * Returns {@code true} if this ElementHolder contains no Elements
     * 
     * @return {@code true} if this ElementHolder contains no Elements
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Adds an array of {@link Element}s to this ElementHolder
     * <p>
     * Note that each Element is going through
     * {@link #add(Element, boolean)} where the boolean value is {@code false}
     * </p>
     * 
     * @param elements The Elements to add
     * @throws IllegalArgumentException If a {@link Section} is attempted to be
     *                                  added or a {@code null} Element is attempted
     *                                  to be added
     */
    public void add(Element[] elements) {
        for (Element e : elements) {
            add(e, false);
        }
    }

    /**
     * Adds an {@link Element} to this ElementHolder.
     * <p>
     * Note that, {@link #add(Element, boolean)} is being called
     * where the first argument is the {@code element} and the second
     * argument is {@code false}.
     * </p>
     * 
     * @param element The Element to add
     * @throws IllegalArgumentException If a {@link Section} is attempted to be
     *                                  added or a {@code null} Element is attempted
     *                                  to be added
     */
    public void add(Element element) {
        add(element, false);
    }

    /**
     * Adds an {@link Element} to this ElementHolder
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
     * <hr>
     * If there are no duplicate keys for the {@code element} being added (Given
     * that it is a {@link KeyValueElement}) then {@code replace} is ignored
     *
     * @param element The {@link Element} to add to this ElementHolder
     * @param replace If the given Element is a {@link KeyValueElement}, and it's
     *                Key is already present; Should the current index of the key
     *                be replaced with the new one at the same index? If not,
     *                then the duplicate is removed and the given KeyValueElement
     *                will be appended instead
     * @throws IllegalArgumentException If a {@link Section} is attempted to be
     *                                  added or a {@code null} Element is attempted
     *                                  to be added
     */
    public void add(Element element, boolean replace) {
        if (element == null || element instanceof Section) {
            throw new IllegalArgumentException();
        }
        if (element instanceof KeyValueElement elementKv) {
            if (keyValues.containsKey(elementKv.getKey())) {
                int elementKvPos = getKeyPos(elementKv.getKey());
                if (replace) {
                    elements.remove(elementKvPos);
                    elements.add(elementKvPos, elementKv);
                    keyValues.remove(elementKv.getKey());
                    keyValues.put(elementKv.getKey(), elementKv);
                    return;
                }
                elements.remove(elementKvPos);
                keyValues.remove(elementKv.getKey());
            }
            keyValues.put(elementKv.getKey(), elementKv);
        }
        elements.add(element);
    }

    /**
     * Use {@link #containsKey(String)} first to check if the Key of
     * a KeyValueElement is present in this ElementHolder
     */
    public KeyValueElement getKey(String key) {
        return keyValues.get(key);
    }

    /**
     * A helper method for the query methods
     * @param element The Element's value to check
     * @param value Checks if an Element matches a value or not
     * @return {@code true} if the value matches an Element
     */
    private boolean matchesValue(Element element, String value) {
        if (element instanceof KeyValueElement kv &&
                kv.getValue().contains(new RMLString(value))) {
            return true;
        } else if (element instanceof SubSection sub && sub.getName().raw().equals(value)) {
            return true;
        } else if (element instanceof RMLList list && list.contains(new RMLString(value))) {
            return true;
        } else if (element instanceof Comment comment && comment.contains(value)) {
            return true;
        }
        return false;
    }

    /**
     * A helper method for the query methods
     * @param element The Element's value to check
     * @param regex Checks if an Element matches a RegEx or not
     * @return {@code true} if the value matches an Element
     */
    private boolean matchesRegex(Element element, String regex) {
        Pattern pattern = Pattern.compile(regex);
        if (element instanceof KeyValueElement kv) {
            for (RMLType v : kv) {
                if (pattern.matcher(v.toString()).find()) {
                    return true;
                }
            }
        } else if (element instanceof SubSection sub
                && pattern.matcher(sub.getName().raw()).find()) {
            return true;
        } else if (element instanceof RMLList list) {
            for (RMLType item : list) {
                if (pattern.matcher(item.toString()).find()) {
                    return true;
                }
            }
        } else if (element instanceof Comment comment) {
            for (RMLString line : comment) {
                if (pattern.matcher(line.raw()).find()) {
                    return true;
                }
            }
        }
        return false;
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
        for (Element e : elements) {
            if (matchesValue(e, value)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
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
        if (!regex) {
            return getFirstElementValueMatch(value);
        }
        for (Element e : elements) {
            if (matchesRegex(e, value)) {
                return Optional.of(e);
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
     *         When there are no matches, an empty List is returned
     *         </p>
     */
    public List<Element> getElementValueMatches(String value) {
        List<Element> returnElements = new ArrayList<>();
        for (Element e : elements) {
            if (matchesValue(e, value)) {
                returnElements.add(e);
            }
        }
        return returnElements;
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
     *         When there are no matches, an empty List is returned
     *         </p>
     */
    public List<Element> getElementValueMatches(String value, boolean regex) {
        if (!regex) {
            return getElementValueMatches(value);
        }
        List<Element> returnElements = new ArrayList<>();
        for (Element e : elements) {
            if (matchesRegex(e, value)) {
                returnElements.add(e);
            }
        }
        return returnElements;
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
        return elements.indexOf(keyValues.get(key));
    }

    /**
     * Returns {@code true} if this ElementHolder contains a
     * {@link KeyValueElement} with the specified key
     * 
     * @param key The key to test and see if a matching
     *            KeyValueElement is present or not
     *            within this ElementHolder
     * @return {@code true} if this ElementHolder contains a
     *         {@link KeyValueElement} with the specified key
     */
    public boolean containsKey(String key) {
        return keyValues.containsKey(key);
    }

    /**
     * Returns {@code true} if this ElementHolder contains a specific
     * {@link KeyValueElement}
     * 
     * @param keyValueElement The KeyValueElement to test and see if it is
     *                        present or not in this ElementHolder
     * @return {@code true} if this ElementHolder contains a specific
     *         {@link KeyValueElement}
     */
    public boolean containsKey(KeyValueElement keyValueElement) {
        return keyValues.containsValue(keyValueElement);
    }

    /**
     * Removes the Element at the specified position in this ElementHolder.
     * Shifts any subsequent Elements to the left (subtracts one
     * from their indices)
     * 
     * @param index The index of the Element to be removed
     * @return The element that was removed from this ElementHolder
     * @throws IndexOutOfBoundsException If the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    public Element remove(int index) {
        Objects.checkIndex(index, elements.size());
        if (elements.get(index) instanceof KeyValueElement kv) {
            keyValues.remove(kv.getKey());
        }
        return elements.remove(index);
    }

    /**
     * Returns {@code true} if this ElementHolder contained the specified element
     * (or equivalently, if this ElementHolder changed as a result of the call)
     *
     * @param element The Element to be removed from this ElementHolder, if present
     * @return {@code true} if this ElementHolder contained the specified element
     *         (or equivalently, if this ElementHolder changed as a result of the call)
     */
    public boolean remove(Element element) {
        boolean removeOperation = elements.remove(element);
        if (removeOperation && element instanceof KeyValueElement kv) {
            keyValues.remove(kv.getKey());
        }
        return removeOperation;
    }

    /**
     * Constructs a new ElementHolder with no values
     */
    public ElementHolder() {
    }

    /**
     * Constructs a new ElementHolder with an initial value
     */
    public ElementHolder(Element initialValue) {
        add(initialValue);
    }

    /**
     * Constructs a new ElementHolder from a {@link List}
     */
    public ElementHolder(List<Element> initialValues) {
        initialValues.forEach(this::add);
    }

    /**
     * Iterator for all {@link Element}s in this ElementHolder
     */
    @Override
    public Iterator<Element> iterator() {
        return getElementsAsList().iterator();
    }

    /**
     * Iterator for all {@link KeyValueElement}s in this ElementHolder
     */
    public Iterator<KeyValueElement> keyValueElementIterator() {
        return Collections.unmodifiableCollection(this.keyValues.values()).iterator();
    }

}