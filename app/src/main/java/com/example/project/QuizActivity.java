package com.example.project;

import android.content.Intent; // Intent 추가 (퀴즈 시작 시 필요)
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // 디버깅을 위한 Log 추가
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private UserDatabaseHelper dbHelper;
    // ▼▼▼ 클래스 멤버 변수 ▼▼▼
    private String currentUserId;

    private LinearLayout courseInfoLayout;
    private TextView courseTitle, courseDetail, noCourseMessage;
    private ImageView courseImage;
    private Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = new UserDatabaseHelper(this);

        // 🔑 SharedPreferences에서 현재 로그인한 사용자 ID 가져오기
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = preferences.getString("user_id", null);

        // UI 연결
        courseInfoLayout = findViewById(R.id.courseInfoLayout);
        courseTitle = findViewById(R.id.courseTitle);
        courseDetail = findViewById(R.id.courseDetail);
        courseImage = findViewById(R.id.courseImage);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        noCourseMessage = findViewById(R.id.noCourseMessage);

        loadQuizInfo();

        //플로팅 버튼 (홈버튼) 이벤트 동작
        FloatingActionButton fab_home = findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //하단 네비게이션 버튼 동작
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_class) {
                Intent intent = new Intent(QuizActivity.this, CategoryActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_community) {
                Intent intent = new Intent(QuizActivity.this, CommunityActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_quiz) {
                Intent intent = new Intent(QuizActivity.this, QuizActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(QuizActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }


    private void loadQuizInfo() {
        Log.d("QuizActivity_Debug", "loadQuizInfo - currentUserId: " + this.currentUserId);

        // 클래스 멤버 변수 this.currentUserId를 사용
        if (this.currentUserId == null || this.currentUserId.isEmpty()) {
            Log.d("QuizActivity_Debug", "currentUserId is null or empty. Showing no courses.");
            showNoCourses();
            return;
        }

        dbHelper.getRegisteredCourses(this.currentUserId, new FirestoreCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> userCourses) {
                runOnUiThread(() -> {
                    Log.d("QuizActivity_Debug", "User courses from DB: " + userCourses);

                    if (userCourses != null && !userCourses.isEmpty()) {
                        String courseName = userCourses.get(0);
                        Log.d("QuizActivity_Debug", "Course found: " + courseName + ". Showing course info.");
                        showCourseInfo(courseName);
                    } else {
                        Log.d("QuizActivity_Debug", "No courses found in DB for this user. Showing no courses message.");
                        showNoCourses();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Log.d("QuizActivity_Debug", "Failed to load courses: " + e.getMessage());
                    showNoCourses();
                });
            }
        });
    }

    private void showCourseInfo(String courseName) {
        courseInfoLayout.setVisibility(View.VISIBLE);
        btnStartQuiz.setVisibility(View.VISIBLE);
        noCourseMessage.setVisibility(View.GONE);

        courseTitle.setText(convertCourseTitle(courseName));
        courseDetail.setText("난이도 상 / 평점 4.9 (390)"); // 이 부분은 예시이며, 실제 데이터에 맞게 수정 필요

        // 강의 이미지 설정 (res/drawable 폴더에 tc1 이미지 파일이 있는지 확인)
        // 예시로 "beomgu" 강의일 경우 tc1 이미지를 사용합니다.
        if ("beomgu".equals(courseName)) {
            // R.drawable.tc1이 실제 존재하는 리소스인지 확인하세요.
            // 만약 tc1이 없다면, 다른 이미지 리소스로 대체하거나, 이미지 로딩 라이브러리 사용을 고려하세요.
            courseImage.setImageResource(R.drawable.tc1);
        } else {
            // 다른 강의에 대한 기본 이미지 또는 특정 이미지 설정
            // courseImage.setImageResource(R.drawable.default_course_image); // 예시
        }

        btnStartQuiz.setOnClickListener(v -> {
            // 퀴즈 시작 로직: ClassquizActivity.java로 이동
            Intent intent = new Intent(QuizActivity.this, ClassquizActivity.class);
            intent.putExtra("COURSE_NAME", courseName); // 어떤 강의의 퀴즈인지 정보 전달
            // 필요하다면 USER_ID도 전달할 수 있습니다.
            // intent.putExtra("USER_ID", this.currentUserId);
            startActivity(intent);
            Log.d("QuizActivity_Debug", "Start Quiz button clicked for course: " + courseName);
        });
    }

    private void showNoCourses() {
        courseInfoLayout.setVisibility(View.GONE);
        btnStartQuiz.setVisibility(View.GONE);
        noCourseMessage.setVisibility(View.VISIBLE);
        noCourseMessage.setText("수강 중인 강의가 없습니다."); // 텍스트를 명시적으로 다시 설정
        Log.d("QuizActivity_Debug", "showNoCourses() called. No courses message is visible.");
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