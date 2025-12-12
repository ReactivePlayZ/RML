package com.reactiveplayz;

import com.google.gson.JsonObject;

abstract class Element {

    public JsonObject toJson() {
        return new JsonObject();
    }
}
