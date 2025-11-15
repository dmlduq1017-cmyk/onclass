package com.example.project;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDatabaseHelper {

    private final FirebaseFirestore db;
    private static final String COLLECTION_USERS = "users";

    public UserDatabaseHelper(Context context) {
        // context는 지금은 안 써도 됨 (기존 생성자 호환용)
        db = FirebaseFirestore.getInstance();
    }

    // -----------------------------
    // 1) 사용자 추가 (회원가입)
    // -----------------------------
    public void insertUser(String id,
                           String name,
                           String password,
                           String birth,
                           String gender,
                           FirestoreCallback<Boolean> callback) {

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("password", password);
        // Firestore 콘솔에서 오타(brith)가 있다면 birth로 맞추는 걸 추천
        user.put("birth", birth);
        user.put("gender", gender);
        user.put("registeredCourses", new ArrayList<String>());

        db.collection(COLLECTION_USERS)
                .document(id)      // userId = 기존 SQLite 의 id
                .set(user)
                .addOnSuccessListener(unused -> callback.onSuccess(true))
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 2) 로그인 체크 (id + password)
    // -----------------------------
    public void checkUser(String id,
                          String password,
                          FirestoreCallback<Boolean> callback) {

        db.collection(COLLECTION_USERS)
                .document(id)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        callback.onSuccess(false);
                        return;
                    }
                    String pw = doc.getString("password");
                    boolean ok = pw != null && pw.equals(password);
                    callback.onSuccess(ok);
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 3) 아이디 중복 확인
    // -----------------------------
    public void isUserIdExists(String id,
                               FirestoreCallback<Boolean> callback) {

        db.collection(COLLECTION_USERS)
                .document(id)
                .get()
                .addOnSuccessListener(doc -> callback.onSuccess(doc.exists()))
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 4) 수강 신청
    //    - 기존: 이미 신청된 강의면 false
    // -----------------------------
    public void registerCourse(String userId,
                               String courseName,
                               FirestoreCallback<Boolean> callback) {

        DocumentReference ref = db.collection(COLLECTION_USERS).document(userId);

        ref.get().addOnSuccessListener(doc -> {
            if (!doc.exists()) {
                // 유저가 없으면 신청 실패로 처리
                callback.onSuccess(false);
                return;
            }

            List<String> courses = (List<String>) doc.get("registeredCourses");
            if (courses == null) courses = new ArrayList<>();

            if (courses.contains(courseName)) {
                // 이미 신청한 경우
                callback.onSuccess(false);
            } else {
                // 새로 추가
                ref.update("registeredCourses", FieldValue.arrayUnion(courseName))
                        .addOnSuccessListener(unused -> callback.onSuccess(true))
                        .addOnFailureListener(callback::onError);
            }
        }).addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 5) 수강 신청 여부 확인
    // -----------------------------
    public void isCourseRegistered(String userId,
                                   String courseName,
                                   FirestoreCallback<Boolean> callback) {

        db.collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        callback.onSuccess(false);
                        return;
                    }
                    List<String> courses = (List<String>) doc.get("registeredCourses");
                    if (courses == null) courses = new ArrayList<>();
                    callback.onSuccess(courses.contains(courseName));
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 6) 수강 목록 가져오기 (기존 getUserCourses)
    // -----------------------------
    public void getUserCourses(String userId,
                               FirestoreCallback<List<String>> callback) {

        db.collection(COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    List<String> courses = (List<String>) doc.get("registeredCourses");
                    if (courses == null) courses = new ArrayList<>();
                    callback.onSuccess(courses);
                })
                .addOnFailureListener(callback::onError);
    }

    // -----------------------------
    // 7) getRegisteredCourses - 위와 동일 동작
    // -----------------------------
    public void getRegisteredCourses(String userId,
                                     FirestoreCallback<List<String>> callback) {
        getUserCourses(userId, callback);
    }
}
