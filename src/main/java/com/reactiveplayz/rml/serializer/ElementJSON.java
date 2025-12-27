package com.reactiveplayz.rml.serializer;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.reactiveplayz.rml.*;

/**
 * Contains methods that are used to convert RML Elements into JSON using {@code Gson}
 */
public class ElementJSON {

    /**
     * A general method to convert {@link Element}s that are native to RML
     * into JSON
     * <p>
     *     Note that this method calls other {@code toJson()} methods,
     *     and it is recommended to call them when the Element is known
     * </p>
     * @param element The {@link Element} to convert into JSON
     */
    public static JsonObject toJson(Element element) {
        if (element instanceof KeyValueElement) {
            return toJson((KeyValueElement) element);
        } else if (element instanceof RMLList) {
            return toJson((RMLList) element);
        } else if (element instanceof Comment) {
            return toJson((Comment) element);
        } else if (element instanceof Section) {
            return toJson((Section) element);
        }
        return new JsonObject();
    }

    /**
     * Creates a {@link JsonObject} from a {@link KeyValueElement}
     *
     * @return {@link JsonObject} with fields of:
     *         {@code key},
     *         {@code value},
     *         and {@code comment}.
     *         <p>
     *             If the {@code Key} of the KeyValueElement is {@code null},
     *             then an empty JsonObject is returned.
     *         </p>
     *         <p>
     *             If the {@code value} or {@code comment} of the KeyValueElement
     *             is empty, then they are omitted in the JsonObject
     *         </p>
     * @param KeyValueElement The {@link KeyValueElement} to convert into JSON
     */
    public static JsonObject toJson(KeyValueElement KeyValueElement) {
        String key = KeyValueElement.getKey();
        RMLValue<RMLType> value = KeyValueElement.getValue();
        Comment comment = KeyValueElement.getComment();

        JsonObject kv = new JsonObject();
        if (key == null) {
            return kv;
        }

        kv.addProperty("key", key.strip());
        kv.add("value", toJson(value));
        if (!comment.getCommentValue().isEmpty() && !toJson(comment).isEmpty()) {
            kv.add("comment", toJson(comment.getCommentValue()));
        }

        return kv;
    }

    /**
     * Returns the values of the {@code RMLValue} as
     * a JsonElement ({@link JsonObject} or {@link JsonArray})
     * <p>
     * Returns a {@link JsonObject} if {@code values} only contains one value
     * </p>
     * <p>
     * Otherwise returns a {@link JsonArray} when {@code values} contains multiple values
     * </p>
     *
     * @param val The {@link RMLValue} to convert into JSON
     * @return A JsonElement that is either {@link JsonObject} or {@link JsonArray}
     */
    public static JsonElement toJson(RMLValue<?> val) {
        JsonObject valObj = new JsonObject();
        if (val.size() == 1) {
            if (val.getLast() instanceof RMLString) {
                valObj.addProperty("value", ((RMLString) val.getLast()).raw());
            } else if (val.getLast() instanceof RMLBoolean) {
                valObj.addProperty("value", ((RMLBoolean) val.getLast()).raw());
            } else if (val.getLast() instanceof RMLNumber) {
                valObj.addProperty("value", ((RMLNumber) val.getLast()).raw());
            } else if (val.getLast() instanceof RMLDate) {
                valObj.addProperty("value", ((RMLDate) val.getLast()).toString());
            } else if (val.getLast() instanceof RMLTime) {
                valObj.addProperty("value", ((RMLTime) val.getLast()).toString());
            }
            return valObj.get("value");
        }

        JsonArray valArr = new JsonArray();
        for (RMLType s : val) {
            if (s instanceof RMLString) {
                if (((RMLString) s).raw() == null) {
                    continue;
                }
                valArr.add(((RMLString) s).raw());
            } else if (s instanceof RMLBoolean) {
                valArr.add(((RMLBoolean) s).raw());
            } else if (s instanceof RMLNumber) {
                if (((RMLNumber) s).raw() == null) {
                    continue;
                }
                valArr.add(((RMLNumber) s).raw());
            } else if (s instanceof RMLDate) {
                if (((RMLDate) s).raw() == null) {
                    continue;
                }
                valArr.add(((RMLDate) s).raw().toString());
            } else if (s instanceof RMLTime) {
                if (((RMLTime) s).raw() == null) {
                    continue;
                }
                valArr.add(((RMLTime) s).raw().toString());
            }
        }

        if (valArr.isEmpty()) {
            return null;
        }
        valObj.add("value", valArr);
        return valObj.get("value");
    }

    /**
     * Creates a {@link JsonObject} with key {@code comment} that contains
     * value(s) from the provided {@link Comment}
     * @param comment The {@link Comment} to convert into JSON
     * @return {@link JsonObject} with key {@code comment} and value(s) of the {@link Comment}
     */
    public static JsonObject toJson(Comment comment) {
        JsonObject commentObj = new JsonObject();

        commentObj.add("comment", toJson(comment.getCommentValue()));

        return commentObj;
    }

    /**
     * Creates a {@link JsonObject} from a {@link Section} with
     * keys: {@code section_name} with the value being the provided section's name,
     * {@code elements} where the value is a {@link JsonArray} with the stored
     * {@link Element}s and their JSON representation, and {@code comment}
     * with the section's comments (Omitted if empty)
     * @param section The {@link Section} to convert into JSON
     * @return {@link JsonObject} containing {@code section_name},
     *         {@code comment} (if not omitted), and {@code elements}
     */
    public static JsonObject toJson(Section section) {
        ArrayList<Element> section_elements = section.getElements();
        RMLValue<RMLString> comment = section.getComment();
        RMLString name = section.getName();

        JsonArray elements = new JsonArray();
        for (Element e : section_elements) {
            if (e instanceof Section && !(e instanceof SubSection)) {
                // will throw exception later or account for getElements() to not
                // accept Section Element
                continue;
            }
            elements.add(toJson(e));
        }
        JsonObject sectionObj = new JsonObject();
        sectionObj.addProperty("section_name", name.raw());
        if (!comment.isEmpty()) {
            sectionObj.add("comment", toJson(comment));
        }

        sectionObj.add("elements", elements);
        return sectionObj;
    }

    /**
     * Creates a {@link JsonObject} with a key of {@code list}
     * that contains a {@link JsonArray} from a given {@link RMLList}
     * @param list The {@link RMLList} to convert into JSON
     * @return {@link JsonObject} with {@code list} that contains a {@link JsonArray}
     */
    public static JsonObject toJson(RMLList list) {
        JsonElement listArr = toJson(list.getList());
        assert listArr != null;
        if (!listArr.isJsonArray()) {
            JsonArray objToArr = new JsonArray();
            objToArr.add(listArr);
            listArr = objToArr.getAsJsonArray();
        }
        JsonObject listObj = new JsonObject();
        listObj.add("list", listArr);
        return listObj;
    }

}
