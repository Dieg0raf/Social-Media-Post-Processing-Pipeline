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
    private static final String DEFAULT_HASHTAG = "#bskypost";
    private static final String HASHTAG_SERVICE_URL = "http://localhost:30002/hash-tag";
    private static ModerationHttpClient mainHttpClient = null;
    private static HttpClient client = null;

    private ModerationHttpClient () {
        client = HttpClient.newHttpClient();
    }

    public static ModerationHttpClient getMainHttpClient() {
        if (mainHttpClient == null) {
            mainHttpClient = new ModerationHttpClient();
        }
        return mainHttpClient;
    }

    public String sendRequest(String jsonBody) {
        try {
            HttpRequest serviceRequest = createHttpRequest(jsonBody);
            HttpResponse<String> response = sendRequest(serviceRequest);
            return response.body(); // return response from hashtag service
        } catch (IOException|InterruptedException e) {
            return DEFAULT_HASHTAG; // return default if http client fails
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
