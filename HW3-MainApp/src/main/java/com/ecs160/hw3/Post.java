package com.ecs160.hw3;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private Integer postId;
    private Integer parentPostId;
    private Integer likeCount;
    private String createdAt;
    private String uri;
    private String postContent;
    private final List<Post> replies  = new ArrayList<>();

    public Post(Integer postId, Integer parentPostId, String createdAt, String uri, String postContent, Integer likeCount) {
        this.postId = postId;
        this.parentPostId = parentPostId;
        this.createdAt = createdAt;
        this.uri = uri;
        this.postContent = postContent;
        this.likeCount = likeCount;
    }

    public Post() {}

    // Setter methods
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setParentPostId(Integer parentPostId) {
        this.parentPostId = parentPostId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLikeCount (Integer likeCount) { this.likeCount = likeCount; }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    // Getter methods
    public List<Post> getReplies() {
        return this.replies;
    }

    public Integer getPostId() {
        return this.postId;
    }

    public Integer getParentPostId() {
        return this.parentPostId;
    }

    public String getCreatedTimeStamp() {
        return this.createdAt;
    }

    public String getUri() {
        return this.uri;
    }

    public Integer getLikeCount () {
        return this.likeCount;
    }

    public String getPostContent() {
        return this.postContent;
    }

    public int getRepliesSize() {
        return this.replies.size();
    }

    // Useful methods
    public void addReply(Post post) {
        this.replies.add(post);
    }

    public boolean hasContent() {
        return !this.postContent.isEmpty();
    }
}

