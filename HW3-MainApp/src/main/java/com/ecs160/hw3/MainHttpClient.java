package com.ecs160.hw3;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MainHttpClient {
    private static final String MODERATION_SERVICE_URL = "http://localhost:30001/moderate";
    private static HttpClient client;
    private static MainHttpClient mainHttpClient;

    // using singleton design pattern from lecture (seems like it would work for this case)
    private MainHttpClient () {
        client = HttpClient.newHttpClient();
    }

    public static MainHttpClient getMainHttpClient() {
        if (mainHttpClient == null) {
            mainHttpClient = new MainHttpClient();
        }
        return mainHttpClient;
    }

    public String sendRequest(String jsonBody) {
        try {
            HttpRequest serviceRequest = createHttpRequest(jsonBody);
            HttpResponse<String> response = sendRequest(serviceRequest);
            return response.body(); // return response moderation service
        } catch (IOException|InterruptedException e) {
            return "Error has occurred for HTTP Client: " + e;
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest serviceRequest) throws IOException, InterruptedException {
        return client.send(serviceRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest createHttpRequest(String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(MODERATION_SERVICE_URL))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }
}
