package com.example.project;

import android.content.Context;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CommentDatabaseHelper {

    private final FirebaseFirestore db;
    private static final String COLLECTION_POSTS = "posts";
    private static final String SUBCOLLECTION_COMMENTS = "coments"; // 스샷 기준
    private static final String FIELD_CREATED_AT = "createdAt";

    public CommentDatabaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    // -----------------------------
    // 1) 댓글 등록
    // -----------------------------
    public void insertComment(String postDocumentId,
                              Comment comment,
                              FirestoreCallback<Boolean> callback) {

        Map<String, Object> data = new HashMap<>();
        data.put("content", comment.getContent());
        data.put("date", comment.getDate());
        data.put(FIELD_CREATED_AT, FieldValue.serverTimestamp());

        db.collection(COLLECTION_POSTS)
                .document(postDocumentId)
                .collection(SUBCOLLECTION_COMMENTS)
                .add(data)
                .addOnSuccessListener(docRef -> callback.onSuccess(true))
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 2) 특정 글의 댓글 목록
    // -----------------------------
    public void getCommentsByPostId(String postDocumentId,
                                    FirestoreCallback<List<Comment>> callback) {

        db.collection(COLLECTION_POSTS)
                .document(postDocumentId)
                .collection(SUBCOLLECTION_COMMENTS)
                .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(query -> {
                    List<Comment> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String content = doc.getString("content");
                        String displayDate = doc.getString("date");
                        if (displayDate == null) {
                            Timestamp timestamp = doc.getTimestamp(FIELD_CREATED_AT);
                            if (timestamp != null) {
                                displayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                                        .format(timestamp.toDate());
                            } else {
                                displayDate = "";
                            }
                        }
                        Comment c = new Comment(
                                doc.getId(),
                                postDocumentId,
                                content == null ? "" : content,
                                displayDate
                        );
                        list.add(c);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(callback::onError);
    }
}
