package com.reactiveplayz.rml.serializer;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.reactiveplayz.rml.Comment;
import com.reactiveplayz.rml.Element;
import com.reactiveplayz.rml.KeyValueElement;
import com.reactiveplayz.rml.RMLBoolean;
import com.reactiveplayz.rml.RMLDate;
import com.reactiveplayz.rml.RMLList;
import com.reactiveplayz.rml.RMLNumber;
import com.reactiveplayz.rml.RMLString;
import com.reactiveplayz.rml.RMLType;
import com.reactiveplayz.rml.RMLValue;
import com.reactiveplayz.rml.Section;
import com.reactiveplayz.rml.SubSection;

public class ElementJSON {

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
     * Creates a JsonObject from the KeyValueElement Object
     * 
     * @return {@code JsonObject} with fields of:
     *         {@code key},
     *         {@code value},
     *         and {@code comment}.
     *         <p>
     *         Each field can be null as well and therefore ignored during
     *         conversion
     *         </p>
     *         Useful for empty comments
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
     * Returns the values of the {@code RMLValue} class's values as
     * a JsonElement ({@code JsonObject} or {@code JsonArray})
     * <p>
     * Returns a {@code JsonObject} if {@code values} only has one value
     * </p>
     * <p>
     * Otherwise returns a {@code JsonArray} when {@code values} has multiple values
     * </p>
     *
     * @return A JsonElement that is either {@code JsonObject} or {@code JsonArray}
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
                valObj.addProperty("value", ((RMLDate) val.getLast()).raw().toString());
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
            }
        }

        if (valArr.isEmpty()) {
            return null;
        }
        valObj.add("value", valArr);
        return valObj.get("value");
    }

    public static JsonObject toJson(Comment comment) {
        JsonObject commentObj = new JsonObject();

        commentObj.add("comment", toJson(comment.getCommentValue()));

        return commentObj;
    }

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
