package com.example.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

  private static final String PRODUCT_KEY = "product";
  private static final String PRODUCT_ID_KEY = "product_id";
  private static final String PRODUCT_DESCRIPTION_KEY = "product_description";
  private static final String PRODUCT_NAME_KEY = "title";
  private static final String AVAILABLE_TO_PROMISE_KEY = "available_to_promise_network";
  private static final String ITEM_KEY = "item";

  public JsonObject parseStringToJsonObject(String rawJson) {
    return JsonParser.parseString(rawJson).getAsJsonObject();
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new JsonParseException(e);
    }
  }

  public String getIdFromProductJson(JsonObject jsonObject) throws JsonParseException {
    if (!jsonObject.has(PRODUCT_KEY)) {
      throw new JsonParseException("Invalid Product Json");
    }

    JsonObject productJsonObject = jsonObject.get(PRODUCT_KEY).getAsJsonObject();
    return productJsonObject
        .get(AVAILABLE_TO_PROMISE_KEY)
        .getAsJsonObject()
        .get(PRODUCT_ID_KEY)
        .getAsString();
  }

  public String getNameFromProductJson(JsonObject jsonObject) throws JsonParseException {
    if (!jsonObject.has(PRODUCT_KEY)) {
      throw new JsonParseException("Invalid Product Json");
    }
    JsonObject productJsonObject = jsonObject.get(PRODUCT_KEY).getAsJsonObject();
    return productJsonObject
        .get(ITEM_KEY)
        .getAsJsonObject()
        .get(PRODUCT_DESCRIPTION_KEY)
        .getAsJsonObject()
        .get(PRODUCT_NAME_KEY)
        .getAsString();
  }
}
