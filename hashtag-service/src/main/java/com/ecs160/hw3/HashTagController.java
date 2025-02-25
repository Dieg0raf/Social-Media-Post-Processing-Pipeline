package com.ecs160.hw3;

import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.response.OllamaResult;
import io.github.ollama4j.utils.OptionsBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HashTagController {
    private static final String DEFAULT_HASHTAG = "#bskypost";
    private static final String OLLAMA_URL = "http://localhost:11434";
    private static final String MODEL_NAME = "llama3.2";

    public static class MyRequest {
        private final String postContent;

        public MyRequest(String postContent) {
            this.postContent = postContent;
        }

        public String getPostContent() {
            return postContent;
        }
    }

    @PostMapping("/hash-tag")
    public String hashTag(@RequestBody MyRequest request) {
        try {
            OllamaAPI ollamaAPI = new OllamaAPI(OLLAMA_URL);
            String prompt = createPrompt(request.getPostContent());
            OllamaResult result = generateHashtag(ollamaAPI, prompt);

            return extractHashtag(result.getResponse());
        } catch (OllamaBaseException | IOException | InterruptedException e) {
            logError(e);
            return DEFAULT_HASHTAG;
        }
    }

    private String createPrompt(String postContent) {
        return "Please generate a hashtag for this social media post and just return one hashtag: " + postContent;
    }

    private OllamaResult generateHashtag(OllamaAPI ollamaAPI, String prompt)
            throws OllamaBaseException, IOException, InterruptedException {
        return ollamaAPI.generate(MODEL_NAME, prompt, false, new OptionsBuilder().build());
    }

    private String extractHashtag(String response) {
        return response.contains("#") ? response : DEFAULT_HASHTAG;
    }

    private void logError(Exception e) {
        System.out.println("Error has occurred when hash tagging: " + e);
    }
}