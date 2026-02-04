package com.reactiveplayz.rml;

import java.util.*;

public interface Sections extends Iterable<Element> {

    /**
     * Returns the name of this Section
     * @return The name of this Section
     */
    RMLString getName();

    /**
     * Returns the Element at the specified position in this Section
     *
     * @param index index of the Element to return
     * @return The element at the specified position in this Section
     * @throws IndexOutOfBoundsException If the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    Element get(int index);

    /**
     * Gets the first Element in this Section
     *
     * @return the retrieved Element
     * @throws NoSuchElementException When the Section is empty
     */
    Element getFirst();

    /**
     * Gets the last Element in this Section
     *
     * @return the retrieved Element
     * @throws NoSuchElementException When the Section is empty
     */
    Element getLast();

    /**
     * Returns the Elements stored in this Section as an unmodifiable
     * {@link Collection}
     *
     * @return the Elements stored in this Section as an unmodifiable
     *         {@link Collection}
     */
    Collection<Element> getElementsAsCollection();

    /**
     * Returns the Elements stored in this Section as an unmodifiable {@link List}
     *
     * @return the Elements stored in this Section as an unmodifiable {@link List}
     */
    List<Element> getElementsAsList();

    /**
     * Returns the number of Elements in this Section
     *
     * @return The number of Elements in this Section
     */
    int size();

    /**
     * Returns {@code true} if this Section contains no Elements
     *
     * @return {@code true} if this Section contains no Elements
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if this Section contains the specified Element
     * @param element The Element to test its presence in this Section
     * @return {@code true} if this Section contains the specified Element
     */
    boolean contains(Element element);

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
    boolean containsKey(String key);

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
    KeyValueElement getKey(String key);

    /**
     * Returns the first {@link Element} where it's {@link RMLValue}
     * contains a specific value
     *
     * @param value The specific value to look for
     * @return The first {@link Element} where it's {@link RMLValue}
     *         contains a specific value.
     */
    Optional<Element> getFirstElementValueMatch(String value);

    /**
     * Returns the first {@link Element} where it's {@link RMLValue}
     * properties match a Regular Expression if enabled
     *
     * @param value The value to look for, which can be RegEx
     * @param regex Should RegEx be used?
     * @return The first {@link Element} where it's {@link RMLValue}
     *         properties match a Regular Expression or a specific value
     */
    Optional<Element> getFirstElementValueMatch(String value, boolean regex);

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
    List<Element> getElementValueMatches(String value);

    /**
     * Returns all {@link Element}s where their {@link RMLValue}
     * properties match a Regular Expression if enabled
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
    List<Element> getElementValueMatches(String value, boolean regex);

    /**
     * Adds an array of {@link Element}s to this Section
     *
     * @param elements The Elements to add
     */
    void add(Element[] elements);

    /**
     * Adds an {@link Element} to this Section
     *
     * @param element The Element to add
     */
    void add(Element element);

    /**
     * Adds an {@link Element} to this Section
     * <hr>
     * If a {@link KeyValueElement} is added with a Key that is already
     * present in this Section, then by default, the old one is removed
     * and the new one is appended. However, if {@code replace} is {@code true}
     * then the new KeyValueElement should take the position of the old one
     *
     * @param element The Element to add
     * @param replace Given that a KeyValueElement with a duplicate key is being
     *                added, should it replace the old KeyValueElement's position?
     *                <p>This parameter is ignored if there are no duplicate keys
     *                   or the given Element is not a KeyValueElement</p>
     */
    void add(Element element, boolean replace);

    /**
     * Removes the first occurrence of the specified Element from this Section,
     * if it is present (optional operation). If this Section does not contain
     * the Element, it is unchanged
     *
     * @param element The Element to be removed from this Section, if present
     * @return {@code true} if this Section contained the specified Element
     *         (or equivalently, if this Section changed as a result of the call)
     */
    boolean remove(Element element);

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
    Element remove(int index);

    /**
     * Returns the {@link Comment} of this Section
     * @return The {@link Comment} of this Section
     */
    Comment getComment();

    /**
     * Returns an iterator over the Elements present in this Section
     * @return An iterator over the Elements present in this Section
     */
    @Override
    Iterator<Element> iterator();

}