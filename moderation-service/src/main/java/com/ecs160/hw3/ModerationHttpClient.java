package com.ecs160.hw3;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ModerationHttpClient {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String HASHTAG_SERVICE_URL = "http://localhost:30002/hash-tag";

    public String sendRequest(String jsonBody) {
        try {
            HttpRequest serviceRequest = createHttpRequest(jsonBody);
            HttpResponse<String> response = sendRequest(serviceRequest);
            return response.body(); // return response from hashtag service
        } catch (IOException|InterruptedException e) {
            return "Error contacting moderation service";
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest serviceRequest) throws IOException, InterruptedException {
        return client.send(serviceRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest createHttpRequest(String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(HASHTAG_SERVICE_URL))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }
}
