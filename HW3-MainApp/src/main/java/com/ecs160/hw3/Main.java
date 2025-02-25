package com.ecs160.hw3;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Main driver = new Main();
        driver.run(args);
    }

    private void run(String[] args) throws IOException, InterruptedException {
        String filePath = getFilePathFromArgs(args);
        JsonParser parser = new JsonParser();
        List<Post> allPosts = parser.parseJson(filePath);
        for (Post post: allPosts) {
            System.out.println("ThreadPost: " + post.getParentPostId());
            System.out.println("Amount of likes " + post.getLikeCount());
            System.out.println("Amount of replies: " + post.getRepliesSize());
            System.out.println();
        }

        String postId = "45";
        String content = "This is a offensive dynamically generated post.";

        // Dynamically build the JSON string using String.format() or concatenation
        String jsonBody = String.format("{"
                + "\"postContent\": \"%s\","
                + "\"postId\": \"%s\""
                + "}", content, postId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:30001/moderate"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Response: " + response.body());
    }

    private String getFilePathFromArgs(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("file=")) {
                return arg.substring(5);
            }
        }
        return "input.json";  // default
    }
}
