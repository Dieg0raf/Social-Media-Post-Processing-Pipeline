package com.ecs160.hw3;

import java.io.IOException;
import java.util.PriorityQueue;

public class PostProcessor {
    private final JsonUtils jsonUtil;
    private final MainHttpClient httpClientService;
    private final PrintFormatter formatter;

    public PostProcessor(JsonUtils jsonUtil, MainHttpClient httpClientService, PrintFormatter formatter) {
        this.jsonUtil = jsonUtil;
        this.formatter = formatter;
        this.httpClientService = httpClientService;
    }

    public void processTopPosts(PriorityQueue<Post> topTenPosts) throws IOException, InterruptedException {
        int i = 1;
        while (!topTenPosts.isEmpty()) {
            Post nextPost = topTenPosts.poll();
            processPost(nextPost, i);
            i++;
        }
    }

    private void processPost(Post post, int index) throws IOException, InterruptedException {
        String jsonBody = jsonUtil.serializeToJson(post.getPostContent());
        String hashTagResponse = httpClientService.sendRequest(jsonBody);

        formatter.printFormattedResponse(index, 10, post.getPostContent(), hashTagResponse, false);

        processReplies(post);
    }

    private void processReplies(Post post) throws IOException, InterruptedException {
        int j = 1;
        int totalReplies = post.getRepliesSize();

        for (Post reply : post.getReplies()) {
            String replyJsonBody = jsonUtil.serializeToJson(reply.getPostContent());
            String replyHashTagResponse = httpClientService.sendRequest(replyJsonBody);
            formatter.printFormattedResponse(j, totalReplies, reply.getPostContent(), replyHashTagResponse, true);
            j++;
        }
    }



}
