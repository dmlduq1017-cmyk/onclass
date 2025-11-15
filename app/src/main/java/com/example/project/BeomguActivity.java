package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BeomguActivity extends AppCompatActivity {

    private TextView textRating;
    private TextView textReviewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beomguclass);

        // 뒤로가기 버튼
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(v -> finish());

        UserDatabaseHelper db = new UserDatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", "");

        // 수강 신청 버튼
        Button classRegBtn = findViewById(R.id.classregbtn);
        classRegBtn.setOnClickListener(v -> {
            if (userId == null || userId.isEmpty()) {
                Toast.makeText(this, "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            classRegBtn.setEnabled(false);
            db.registerCourse(userId, "beomgu", new FirestoreCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    runOnUiThread(() -> {
                        classRegBtn.setEnabled(true);
                        if (Boolean.TRUE.equals(result)) {
                            Toast.makeText(BeomguActivity.this, "수강 신청이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BeomguActivity.this, "이미 신청한 강의입니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        classRegBtn.setEnabled(true);
                        Toast.makeText(BeomguActivity.this, "신청 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        // 강의 클릭 → 수강 여부 확인 후 이동
        TextView lecture1 = findViewById(R.id.lecture1);
        lecture1.setOnClickListener(v -> {
            if (userId == null || userId.isEmpty()) {
                Toast.makeText(this, "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            db.isCourseRegistered(userId, "beomgu", new FirestoreCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean registered) {
                    runOnUiThread(() -> {
                        if (Boolean.TRUE.equals(registered)) {
                            Intent intent = new Intent(BeomguActivity.this, LectureDetailActivity.class);
                            intent.putExtra("lectureNumber", 1);
                            intent.putExtra("title", "제 1강 - 수동태는 관점의 차이다");
                            intent.putExtra("videoResId", R.raw.vedio1);
                            startActivity(intent);
                        } else {
                            Toast.makeText(BeomguActivity.this, "수강 신청 후 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> Toast.makeText(BeomguActivity.this, "수강 정보를 확인하지 못했습니다.", Toast.LENGTH_SHORT).show());
                }
            });
        });

        // 메인으로 이동 버튼
        Button buttonGoHome = findViewById(R.id.buttonGoHome);
        buttonGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(BeomguActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // 별점 UI
        textRating = findViewById(R.id.textRating);
        textReviewCount = findViewById(R.id.textReviewCount);

        RatingDatabaseHelper ratingDb = new RatingDatabaseHelper(this);
        ratingDb.getAverageRating(1, new FirestoreCallback<Float>() {
            @Override
            public void onSuccess(Float avgRating) {
                runOnUiThread(() -> textRating.setText(String.format("평점 %.1f", avgRating)));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> textRating.setText("평점 정보를 불러오지 못했습니다."));
            }
        });

        ratingDb.getRatingCount(1, new FirestoreCallback<Integer>() {
            @Override
            public void onSuccess(Integer count) {
                runOnUiThread(() -> textReviewCount.setText("(" + count + ")"));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> textReviewCount.setText("(0)"));
            }
        });
    }
}
