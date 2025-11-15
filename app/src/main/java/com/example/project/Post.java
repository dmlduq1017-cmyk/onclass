package com.example.project;

public class Post {
    private int id;
    private String title;
    private String content;
    private String category; // hot, free, study, tip
    private String date;
    private int views;
    private int likes;

    public Post(int id, String title, String content, String category, String date, int views, int likes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
        this.views = views;
        this.likes = likes;
    }

    public Post(String title, String content, String category, String date) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
        this.views = 0;
        this.likes = 0;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public int getViews() { return views; }
    public int getLikes() { return likes; }

    public void setViews(int views) { this.views = views; }
    public void setLikes(int likes) { this.likes = likes; }
}
