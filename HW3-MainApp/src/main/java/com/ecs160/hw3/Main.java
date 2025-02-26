package com.ecs160.hw3;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final JsonUtils jsonUtil = new JsonUtils();
    private static final MainHttpClient httpClientService = MainHttpClient.getMainHttpClient();
    private static final PrintFormatter formatter = new PrintFormatter();
    private static final PostProcessor processor = new PostProcessor(jsonUtil, httpClientService, formatter);

    public static void main(String[] args){
        Main driver = new Main();
        driver.run(args);
    }

    private void run(String[] args) {
        String filePath = getFilePathFromArgs(args);
        List<Post> allPosts = jsonUtil.deserializeFromJson(filePath);
        PriorityQueue<Post> topTenPosts = createTopTenPosts(allPosts);

        try {
            processor.processTopPosts(topTenPosts);
        } catch (IOException | InterruptedException e) {
            System.out.println("Error occurred when processing Top ten posts: " + e);
        }
    }

    private static PriorityQueue<Post> createTopTenPosts(List<Post> allPosts) {
        PriorityQueue<Post> topTenPosts = new PriorityQueue<>(Comparator.comparingInt(Post::getLikeCount));
        for (Post post: allPosts) {
            if (topTenPosts.size() < 10) {
                topTenPosts.add(post);
            }
            else if (post.getLikeCount() > topTenPosts.peek().getLikeCount()) {
                topTenPosts.poll();
                topTenPosts.add(post);
            }
        }
        return topTenPosts;
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
