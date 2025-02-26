package com.ecs160.hw3;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ModerationController {
    private final ModerationHttpClient httpClientService = ModerationHttpClient.getMainHttpClient();
    private static final JsonUtils jsonUtils = new JsonUtils();
    private static final String FAILED_KEYWORD = "FAILED";
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
        String jsonBody = jsonUtils.createJson(postContent);
        return this.httpClientService.sendRequest(jsonBody);
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
