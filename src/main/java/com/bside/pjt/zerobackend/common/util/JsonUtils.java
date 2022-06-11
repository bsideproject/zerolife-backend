package com.bside.pjt.zerobackend.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class JsonUtils {

    private static JsonUtils instance;

    private Gson gson;

    private Gson prettyGson;

    private static JsonUtils getInstance() {
        if (instance == null) {
            instance = new JsonUtils();
        }
        return instance;
    }

    private JsonUtils() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
        prettyGson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    private static Gson getGson() {
        return getInstance().gson;
    }

    private static Gson getPrettyGson() {
        return getInstance().prettyGson;
    }

    public static JsonElement parse(String jsonStr) {
        return JsonParser.parseString(jsonStr);
    }

    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        return getGson().fromJson(jsonStr, cls);
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        return getGson().fromJson(jsonStr, type);
    }

    public static String toPrettyJson(Object object) {
        return getPrettyGson().toJson(object);
    }
}
