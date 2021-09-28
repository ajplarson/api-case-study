package com.example.demo.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {

    public JsonObject parseStringToJsonObject(String rawJson) {
        return JsonParser.parseString(rawJson).getAsJsonObject();
    }
}
