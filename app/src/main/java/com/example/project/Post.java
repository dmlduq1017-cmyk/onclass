package com.example.project;

public class Post {
    private String documentId;
    private String title;
    private String content;
    private String category; // hot, free, study, tip
    private String date;
    private int views;
    private int likes;

    public Post(String documentId, String title, String content, String category, String date, int views, int likes) {
        this.documentId = documentId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
        this.views = views;
        this.likes = likes;
    }

    public Post(String title, String content, String category, String date) {
        this(null, title, content, category, date, 0, 0);
    }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public int getViews() { return views; }
    public int getLikes() { return likes; }

    public void setViews(int views) { this.views = views; }
    public void setLikes(int likes) { this.likes = likes; }
}
