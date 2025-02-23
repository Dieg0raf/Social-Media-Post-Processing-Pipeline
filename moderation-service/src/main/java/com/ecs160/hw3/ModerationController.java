package com.ecs160.hw3;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ModerationController {
    // TODO: Change list (not sure what list e.g., txt file or in memory list)
    private static final List<String> BAD_WORDS = List.of(
            "badword1", "badword2", "curseword1", "offensive1", "explicit1"
    );

    public static class MyRequest {
        private final String postContent;
        private final String postId;

        public MyRequest(String postContent, String postId, int likes, String author) {
            this.postContent = postContent;
            this.postId = postId;
        }

        public String getPostContent() { return postContent; }
        public String getPostId() { return postId; }
    }

    @PostMapping("/moderate")
    public String moderate(@RequestBody MyRequest request) {
        if (hasBadWords(request.getPostContent())) {
            return "Post (" + request.getPostId() + ") content has bad words!";
        }
        return "Moving to next microservice";
    }

    private boolean hasBadWords(String postContent) {
        for(String badWord: ModerationController.BAD_WORDS) {
            if (postContent.contains(badWord)) {
                return true;
            }
        }
        return false;
    }
}
