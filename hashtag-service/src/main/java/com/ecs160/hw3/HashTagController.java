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

    public record MyRequest(String postContent) {
    }

    @PostMapping("/hash-tag")
    public String hashTag(@RequestBody MyRequest request) {
        try {
            OllamaAPI ollamaAPI = new OllamaAPI(OLLAMA_URL);
            String prompt = createPrompt(request.postContent());
            OllamaResult result = generateHashtag(ollamaAPI, prompt);

            return extractHashtag(result.getResponse()); // return response from LLM or default hashtag
        } catch (OllamaBaseException | IOException | InterruptedException e) {
            logError(e);
            return DEFAULT_HASHTAG; // return default hashtag if LLM fails
        }
    }

    private String createPrompt(String postContent) {
        return "Generate exactly one hashtag for this post: " + postContent + ". Respond with only the hashtag and nothing else.";
    }

    private OllamaResult generateHashtag(OllamaAPI ollamaAPI, String prompt)
            throws OllamaBaseException, IOException, InterruptedException {
        return ollamaAPI.generate(MODEL_NAME, prompt, false, new OptionsBuilder().build());
    }

    private String extractHashtag(String response) {
        return response.matches("#\\w+") ? response : DEFAULT_HASHTAG;
    }

    private void logError(Exception e) {
        System.out.println("Error has occurred when hash tagging: " + e);
    }
}