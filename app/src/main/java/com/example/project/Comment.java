package com.example.project;

public class Comment {
    private String documentId;
    private String postDocumentId;
    private String content;
    private String date;

    public Comment(String documentId, String postDocumentId, String content, String date) {
        this.documentId = documentId;
        this.postDocumentId = postDocumentId;
        this.content = content;
        this.date = date;
    }

    public Comment(String postDocumentId, String content, String date) {
        this(null, postDocumentId, content, date);
    }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public String getPostDocumentId() { return postDocumentId; }
    public String getContent() { return content; }
    public String getDate() { return date; }
}
