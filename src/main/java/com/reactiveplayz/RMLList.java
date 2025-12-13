package com.reactiveplayz;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RMLList extends Element {

    private ArrayList<Object> list = new ArrayList<>();

    public ArrayList<Object> getList() {
        return list;
    }

    public JsonObject toJson() {
        JsonObject listObj = new JsonObject();
        JsonArray listArr = new JsonArray();
        for (Object o : list) {
            if (o instanceof String) {
                listArr.add((String) o);
            }
        }
        listObj.add("list", listArr);
        return listObj;
    }

    RMLList() {
    }

    RMLList(Object firstItem) {
        this.list.add(firstItem);
    }
}
