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
            if (db.registerCourse(userId, "beomgu")) {
                Toast.makeText(this, "수강 신청이 완료되었습니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "이미 신청한 강의입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 강의 클릭 → 수강 여부 확인 후 이동
        TextView lecture1 = findViewById(R.id.lecture1);
        lecture1.setOnClickListener(v -> {
            if (db.isCourseRegistered(userId, "beomgu")) {
                Intent intent = new Intent(BeomguActivity.this, LectureDetailActivity.class);
                intent.putExtra("lectureNumber", 1);
                intent.putExtra("title", "제 1강 - 수동태는 관점의 차이다");
                intent.putExtra("videoResId", R.raw.vedio1);
                startActivity(intent);
            } else {
                Toast.makeText(this, "수강 신청 후 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
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
        float avgRating = ratingDb.getAverageRating(1);
        int count = ratingDb.getRatingCount(1);

        String ratingStr = String.format("평점 %.1f", avgRating);
        String countStr = "(" + count + ")";

        textRating.setText(ratingStr);
        textReviewCount.setText(countStr);
    }
}
