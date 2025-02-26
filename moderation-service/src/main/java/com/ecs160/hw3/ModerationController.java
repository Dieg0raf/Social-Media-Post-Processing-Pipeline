package com.ecs160.hw3;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ModerationController {
    private final ModerationHttpClient httpClientService = new ModerationHttpClient();
    private static final String FAILED_KEYWORD = "FAILED";
    private static final Gson gson = new Gson();
    private static final List<String> BANNED_WORDS = List.of(
            "illegal",
            "fraud",
            "scam",
            "exploit",
            "dox",
            "swatting",
            "hack",
            "crypto",
            "bots"
    );

    public record MyRequest(String postContent) {
    }

    @PostMapping("/moderate")
    public String moderate(@RequestBody MyRequest request) {
        String postContent = request.postContent();
        if (hasBannedWords(postContent)) {
            return FAILED_KEYWORD;
        }
        String jsonBody = createJson(postContent);
        return this.httpClientService.sendRequest(jsonBody);
    }

    private String createJson(String postContent) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("postContent", postContent);
        return gson.toJson(jsonMap);
    }

    private boolean hasBannedWords(String postContent) {
        for(String badWord: ModerationController.BANNED_WORDS) {
            if (postContent.contains(badWord)) {
                return true;
            }
        }
        return false;
    }
}
