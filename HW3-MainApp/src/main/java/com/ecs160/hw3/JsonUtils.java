package com.ecs160.hw3;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    static final private JsonParser parser = new JsonParser();
    static final private Gson gson = new Gson();

    public List<Post> deserializeFromJson(String filePath) {
        return parser.parseJson(filePath);
    }

    public String serializeToJson(String postContent) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("postContent", postContent);
        return gson.toJson(jsonMap);
    }

}
