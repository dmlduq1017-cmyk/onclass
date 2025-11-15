package com.example.project;

public class Comment {
    private int id;
    private int postId;
    private String content;
    private String date;

    public Comment(int id, int postId, String content, String date) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.date = date;
    }

    public Comment(int postId, String content, String date) {
        this.postId = postId;
        this.content = content;
        this.date = date;
    }

    public int getId() { return id; }
    public int getPostId() { return postId; }
    public String getContent() { return content; }
    public String getDate() { return date; }
}
