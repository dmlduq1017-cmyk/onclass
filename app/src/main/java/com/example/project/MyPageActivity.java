package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MyPageActivity extends AppCompatActivity {

    private LinearLayout courseListLayout;
    private UserDatabaseHelper db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        courseListLayout = findViewById(R.id.courseList); // activity_mypage.xml에 정의
        db = new UserDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getString("user_id", "");

        loadCourses();
        //하단 네비게이션 버튼 동작
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_class) {
                Intent intent = new Intent(MyPageActivity.this, CategoryActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_community) {
                Intent intent = new Intent(MyPageActivity.this, CommunityActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_quiz) {
                Intent intent = new Intent(MyPageActivity.this, QuizActivity.class);
                startActivity(intent);
                return true;
            }else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(MyPageActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // 뒤로가기 버튼 이벤트 추가
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티 종료
            }
        });
    }

    private void loadCourses() {
        if (userId == null || userId.isEmpty()) {
            return;
        }

        db.getRegisteredCourses(userId, new FirestoreCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                runOnUiThread(() -> {
                    courseListLayout.removeAllViews();
                    for (String course : result) {
                        View courseCard = createCourseCard(course);
                        courseListLayout.addView(courseCard);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // 강의 목록을 불러오지 못한 경우 UI를 유지
            }
        });
    }

    private View createCourseCard(String courseName) {
        TextView card = new TextView(this);
        card.setText("📘 " + convertCourseTitle(courseName));
        card.setTextSize(18);
        card.setTextColor(Color.BLACK);
        card.setBackgroundColor(Color.parseColor("#FFFFFF"));
        card.setPadding(30, 30, 30, 30);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        card.setLayoutParams(params);

        // 클릭 시 강의 액티비티로 이동
        card.setOnClickListener(v -> {
            if (courseName.equals("beomgu")) {
                Intent intent = new Intent(MyPageActivity.this, BeomguActivity.class);
                startActivity(intent);
            }
            // 여기에 다른 강의 추가 가능 (예: if (courseName.equals("english")) { ... })
        });

        return card;
    }

    private String convertCourseTitle(String code) {
        switch (code) {
            case "beomgu":
                return "김범구의 See the Light";
            default:
                return code;
        }
    }
}
