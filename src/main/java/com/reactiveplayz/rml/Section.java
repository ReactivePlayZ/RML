package com.reactiveplayz.rml;

import java.util.*;

/**
 * A Section is a {@link Element} that stores elements
 * except for Sections themselves
 * <p>It contains a {@code name}, {@code elements}, and a {@code comment}</p>
 * (It has a syntax of {@code = section name =} in RML)
 */
public class Section extends Element {
    private final RMLString name;
    private final ArrayList<Element> elements = new ArrayList<>();
    private final HashMap<String, KeyValueElement> keyValues = new HashMap<>();
    private final HashMap<String, SubSection> subSections = new HashMap<>();
    private final RMLValue<RMLString> comment = new RMLValue<>();

    public Element get(int index) {
        return elements.get(index);
    }

    public Element getFirst() {
        return elements.getFirst();
    }

    public Element getLast() {
        return elements.getLast();
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public void add(Element element) {
        add(element, false);
    }

    /**
     * If the {@code element}'s key is already
     * in the section and {@code override} is true, then {@code element}
     * replaces the already existing {@link KeyValueElement} in the same index
     * with the {@code element}. If {@code override} is false, then {@code element} is
     * added to the end and the already existing KeyValueElement is removed so that
     * there are no duplicate keys.
     * <p>
     * e.g. If a {@link KeyValueElement} with a key of "foo"
     * already exists in the Section, and a new KeyValueElement
     * with a key of "foo" is added, then it removes the existing occurrence
     * and adds the new one to the end if {@code override} is false.
     * </p>
     * However, if {@code override} is true, then it replaces the existing occurrence
     * instead of removing it with {@code element}.
     * <p>This happens because Element positions are preserved and maintained
     * but KeyValueElement Keys must be unique per Section and not contain duplicate keys</p>
     * <hr>
     * Note that, if the KeyValueElement {@code element}'s key is not a duplicate/existing
     * one in this Section, then it will get added as normal and the {@code override}
     * parameter is ignored
     *
     * @param element The {@link KeyValueElement} to add to the Section
     * @param override Should the {@code element} override an already existing
     *                 KeyValueElement with the same key? If not then add to the end
     *                 and remove the already existing one, if any.
     */
    public void add(Element element, boolean override) {
        if (element instanceof Section && !(element instanceof SubSection)) {
            throw new IllegalArgumentException("Sections can't go within Sections");
        }
        if (element instanceof KeyValueElement elementKv) {
            if (keyValues.containsKey(elementKv.getKey())) {
                int elementKvPos = getKeyPos(elementKv.getKey());
                if (override) {
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
        if (element instanceof SubSection subSection) {
            if (subSections.containsKey(subSection.getName().raw())) {
                int subSectionPos = getSubSectionPos(subSection.getName().raw());
                elements.remove(subSectionPos);
                subSections.remove(subSection.getName().raw());
            }
            subSections.put(subSection.getName().raw(), subSection);
        }
        elements.add(element);
    }

    /** Use {@link #containsKey(String)} first to check if the Key is in this Section */
    public KeyValueElement getKey(String key) {
        return keyValues.get(key);
    }

    /** Use {@link #containsSubSection(String)} first to check if the SubSection is in this Section */
    public SubSection getSubSection(String name) {
        return subSections.get(name);
    }

    /** Returns -1 if Key doesn't exist in this Section */
    public int getKeyPos(String key) {
        for (Element e : elements) {
            if (e instanceof KeyValueElement &&
                    ((KeyValueElement) e).getKey().equals(key)) {
                return elements.lastIndexOf(e);
            }
        }
        throw new NoSuchElementException();
    }

    public boolean containsKey(String key) {
        return keyValues.containsKey(key);
    }

    public boolean containsKey(KeyValueElement keyValueElement) {
        return keyValues.containsValue(keyValueElement);
    }

    /** Returns -1 if the SubSection name doesn't exist in this Section */
    public int getSubSectionPos(String name) {
        for (Element e : elements) {
            if (e instanceof SubSection &&
                    ((SubSection) e).getName().raw().equals(name)) {
                return elements.lastIndexOf(e);
            }
        }
        throw new NoSuchElementException();
    }

    public boolean containsSubSection(String name) {
        return subSections.containsKey(name);
    }

    public boolean containsSubSection(SubSection subSection) {
        return subSections.containsValue(subSection);
    }

    public Iterator<Element> iterator() {
        return elements.iterator();
    }

    public RMLString getName() {
        return name;
    }

    public RMLValue<RMLString> getComment() {
        return comment;
    }

    public void remove(int index) {
        if (elements.get(index) instanceof KeyValueElement kv) {
            keyValues.remove(kv.getKey());
        }
        if (elements.get(index) instanceof SubSection sub) {
            subSections.remove(sub.getName().raw());
        }
        elements.remove(index);
    }

    public void remove(Element element) {
        if (element instanceof KeyValueElement kv) {
            keyValues.remove(kv.getKey());
        }
        if (element instanceof SubSection sub) {
            subSections.remove(sub.getName().raw());
        }
        elements.remove(element);
    }

    public Section(String name) {
        this.name = new RMLString(name);
    }

    public Section(RMLString name) {
        this.name = name;
    }

}