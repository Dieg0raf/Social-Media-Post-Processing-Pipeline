package com.ecs160.hw3;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
    private static final Gson gson = new Gson();

    public String createJson(String postContent) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("postContent", postContent);
        return gson.toJson(jsonMap);
    }
}
