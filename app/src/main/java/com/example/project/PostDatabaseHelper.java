package com.example.project;

import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDatabaseHelper {

    private final FirebaseFirestore db;
    private static final String COLLECTION_POSTS = "posts";

    public PostDatabaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    // -----------------------------
    // 1) 게시글 등록
    // -----------------------------
    public void insertPost(Post post,
                           FirestoreCallback<Boolean> callback) {

        Map<String, Object> data = new HashMap<>();
        data.put("title", post.getTitle());
        data.put("content", post.getContent());
        data.put("category", post.getCategory());
        // 기존 SQLite는 date를 TEXT로 사용했으니, 그대로 문자열로 저장하는 게 무난
        data.put("date", post.getDate());
        data.put("views", post.getViews());
        data.put("likes", post.getLikes());

        db.collection(COLLECTION_POSTS)
                .add(data)
                .addOnSuccessListener(docRef -> {
                    // 필요하면 post.setDocumentId(docRef.getId()) 로 로컬 객체를 갱신할 수 있음
                    callback.onSuccess(true);
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 2) 카테고리별 게시글 목록
    // -----------------------------
    public void getPostsByCategory(String category,
                                   FirestoreCallback<List<Post>> callback) {

        db.collection(COLLECTION_POSTS)
                .whereEqualTo("category", category)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(query -> {
                    List<Post> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {

                        String id = doc.getId();
                        String title = doc.getString("title");
                        String content = doc.getString("content");
                        String cat = doc.getString("category");
                        String date = doc.getString("date");
                        Long views = doc.getLong("views");
                        Long likes = doc.getLong("likes");

                        if (views == null) views = 0L;
                        if (likes == null) likes = 0L;

                        // 기존 Post(int id, String title, ...)를 쓰고 있다면
                        // id는 Firestore의 자동 ID를 int로 만들 수 없으니
                        // Post 클래스에 String id 필드를 하나 추가하는 걸 추천
                        Post p = new Post(
                                id,
                                title,
                                content,
                                cat,
                                date,
                                views.intValue(),
                                likes.intValue()
                        );

                        list.add(p);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 3) 조회수 +1
    // -----------------------------
    public void increaseViews(String postDocumentId,
                              FirestoreCallback<Void> callback) {

        db.collection(COLLECTION_POSTS)
                .document(postDocumentId)
                .update("views", FieldValue.increment(1))
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 4) 게시글 1개 조회
    // -----------------------------
    public void getPostById(String postDocumentId,
                            FirestoreCallback<Post> callback) {

        db.collection(COLLECTION_POSTS)
                .document(postDocumentId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        callback.onSuccess(null);
                        return;
                    }

                    String title = doc.getString("title");
                    String content = doc.getString("content");
                    String category = doc.getString("category");
                    String date = doc.getString("date");
                    Long views = doc.getLong("views");
                    Long likes = doc.getLong("likes");

                    if (views == null) views = 0L;
                    if (likes == null) likes = 0L;

                    Post p = new Post(
                            doc.getId(),
                            title,
                            content,
                            category,
                            date,
                            views.intValue(),
                            likes.intValue()
                    );

                    callback.onSuccess(p);
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 5) 조회수 기준 상위 N개
    // -----------------------------
    public void getTopViewedPosts(int limit,
                                  FirestoreCallback<List<Post>> callback) {

        db.collection(COLLECTION_POSTS)
                .orderBy("views", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener(query -> {
                    List<Post> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String title = doc.getString("title");
                        String content = doc.getString("content");
                        String category = doc.getString("category");
                        String date = doc.getString("date");
                        Long views = doc.getLong("views");
                        Long likes = doc.getLong("likes");

                        if (views == null) views = 0L;
                        if (likes == null) likes = 0L;

                        Post p = new Post(
                                doc.getId(),
                                title,
                                content,
                                category,
                                date,
                                views.intValue(),
                                likes.intValue()
                        );
                        list.add(p);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 6) 전체 게시글을 조회수 기준 정렬해서 가져오기
    // -----------------------------
    public void getAllPostsByViews(FirestoreCallback<List<Post>> callback) {

        db.collection(COLLECTION_POSTS)
                .orderBy("views", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(query -> {
                    List<Post> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String title = doc.getString("title");
                        String content = doc.getString("content");
                        String category = doc.getString("category");
                        String date = doc.getString("date");
                        Long views = doc.getLong("views");
                        Long likes = doc.getLong("likes");

                        if (views == null) views = 0L;
                        if (likes == null) likes = 0L;

                        Post p = new Post(
                                doc.getId(),
                                title,
                                content,
                                category,
                                date,
                                views.intValue(),
                                likes.intValue()
                        );
                        list.add(p);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(callback::onError);
    }
}
