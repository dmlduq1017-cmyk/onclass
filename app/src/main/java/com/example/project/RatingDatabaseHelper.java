package com.example.project;

import android.content.Context;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RatingDatabaseHelper {

    private final FirebaseFirestore db;
    private static final String COLLECTION_RATINGS = "ratings";

    public RatingDatabaseHelper(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    private DocumentReference ratingDoc(int lectureNumber) {
        return db.collection(COLLECTION_RATINGS)
                .document(String.valueOf(lectureNumber));
    }

    // -----------------------------
    // 1) 별점 저장 (합계 + 카운트 증가)
    // -----------------------------
    public void saveRating(int lectureNumber,
                           float rating,
                           FirestoreCallback<Void> callback) {

        DocumentReference ref = ratingDoc(lectureNumber);

        db.runTransaction(transaction -> {
                    DocumentSnapshot snap = transaction.get(ref);

                    double total = 0.0;
                    long count = 0L;

                    if (snap.exists()) {
                        Double t = snap.getDouble("totalRating");
                        Long c = snap.getLong("ratingCount");
                        if (t != null) total = t;
                        if (c != null) count = c;
                    }

                    total += rating;
                    count += 1;

                    Map<String, Object> data = new HashMap<>();
                    data.put("totalRating", total);
                    data.put("ratingCount", count);

                    transaction.set(ref, data, SetOptions.merge());
                    return null;
                }).addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 2) 평균 별점 가져오기
    // -----------------------------
    public void getAverageRating(int lectureNumber,
                                 FirestoreCallback<Float> callback) {

        ratingDoc(lectureNumber)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        callback.onSuccess(0f);
                        return;
                    }
                    Double total = doc.getDouble("totalRating");
                    Long count = doc.getLong("ratingCount");

                    if (total == null || count == null || count == 0L) {
                        callback.onSuccess(0f);
                    } else {
                        callback.onSuccess((float) (total / count));
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 3) ratingCount 가져오기
    // -----------------------------
    public void getRatingCount(int lectureNumber,
                               FirestoreCallback<Integer> callback) {

        ratingDoc(lectureNumber)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        callback.onSuccess(0);
                        return;
                    }
                    Long count = doc.getLong("ratingCount");
                    callback.onSuccess(count == null ? 0 : count.intValue());
                })
                .addOnFailureListener(callback::onError);
    }
}
