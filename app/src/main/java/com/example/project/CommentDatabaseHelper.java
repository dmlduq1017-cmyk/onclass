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

public class CommentDatabaseHelper {

    private final FirebaseFirestore db;
    private static final String COLLECTION_POSTS = "posts";
    private static final String SUBCOLLECTION_COMMENTS = "coments"; // 스샷 기준

    public CommentDatabaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    // -----------------------------
    // 1) 댓글 등록
    // -----------------------------
    public void insertComment(int postId_UNUSED,  // 기존 시그니처 맞추기용 (안씀)
                              String postDocumentId,
                              Comment comment,
                              FirestoreCallback<Boolean> callback) {

        Map<String, Object> data = new HashMap<>();
        data.put("content", comment.getContent());
        data.put("date", FieldValue.serverTimestamp());

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
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(query -> {
                    List<Comment> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String content = doc.getString("content");
                        // date를 String 으로 쓰고 있다면 여기서 포맷팅해서 Comment에 넣어도 됨
                        Comment c = new Comment(
                                0,      // id (기존 int) 대신 Firestore ID를 따로 저장하는 걸 추천
                                0,      // post_id
                                content,
                                ""      // date 문자열 포맷이 필요하면 변환해서 넣기
                        );
                        list.add(c);
                    }
                    callback.onSuccess(list);
                })
                .addOnFailureListener(callback::onError);
    }
}
