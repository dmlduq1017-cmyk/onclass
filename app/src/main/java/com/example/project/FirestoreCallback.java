package com.example.project;

// T : 콜백으로 돌려줄 결과 타입(제네릭)
public interface FirestoreCallback<T> {
    // 성공했을 때
    void onSuccess(T result);

    // 실패했을 때 (네트워크 오류, 권한 오류 등)
    void onError(Exception e);
}
