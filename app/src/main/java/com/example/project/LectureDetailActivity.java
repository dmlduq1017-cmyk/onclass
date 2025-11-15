package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

public class LectureDetailActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView lectureTitle, textRating, textReviewCount;
    private RatingBar ratingBar;
    private Button buttonRate, buttonNextLecture, goLectureList;
    private EditText editComment, editNote;
    private RatingDatabaseHelper ratingDb;

    private int currentLecture = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        // View 바인딩
        videoView = findViewById(R.id.videoView);
        lectureTitle = findViewById(R.id.lectureTitle);
        ratingBar = findViewById(R.id.ratingBar);
        buttonRate = findViewById(R.id.buttonRate);
        buttonNextLecture = findViewById(R.id.buttonNextLecture);
        goLectureList = findViewById(R.id.goLectureList);
        editComment = findViewById(R.id.editComment);
        editNote = findViewById(R.id.editNote);
        textRating = findViewById(R.id.textRating);
        textReviewCount = findViewById(R.id.textReviewCount);

        ratingDb = new RatingDatabaseHelper(this);

        // 인텐트 데이터
        Intent intent = getIntent();
        currentLecture = intent.getIntExtra("lectureNumber", 1);
        String title = intent.getStringExtra("title");
        int videoResId = intent.getIntExtra("videoResId", getVideoResId(currentLecture));

        String titleText = (title != null) ? title : "제 " + currentLecture + "강";
        lectureTitle.setText(titleText);

        // 영상 설정
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResId);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(this));
        videoView.start();

        videoView.setOnClickListener(v -> {
            Intent fullIntent = new Intent(this, FullScreenVideoActivity.class);
            fullIntent.putExtra("videoResId", videoResId);
            startActivity(fullIntent);
        });

        // 강의 리스트로 이동
        goLectureList.setOnClickListener(v -> {
            Intent it = new Intent(LectureDetailActivity.this, BeomguActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(it);
            finish();
        });


        // 별점 등록
        buttonRate.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            ratingDb.saveRating(currentLecture, rating);

            float avgRating = ratingDb.getAverageRating(currentLecture);
            int count = ratingDb.getRatingCount(currentLecture);

            Toast.makeText(this, "별점 " + rating + "점이 등록되었습니다!", Toast.LENGTH_SHORT).show();

            // 텍스트뷰 업데이트
            TextView textRating = findViewById(R.id.textRating);
            TextView textReviewCount = findViewById(R.id.textReviewCount);
            textRating.setText("강의 평점 " + String.format("%.1f", avgRating));
            textReviewCount.setText("(" + count + ")");
        });


        // 다음 강의로 이동
        buttonNextLecture.setOnClickListener(v -> {
            if (currentLecture < 12) {
                Intent nextIntent = new Intent(this, LectureDetailActivity.class);
                nextIntent.putExtra("lectureNumber", currentLecture + 1);
                nextIntent.putExtra("title", "제 " + (currentLecture + 1) + "강");
                nextIntent.putExtra("videoResId", getVideoResId(currentLecture + 1));
                startActivity(nextIntent);
            } else {
                Toast.makeText(this, "마지막 강의입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 뒤로가기 버튼
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(v -> finish());
    }


    private int getVideoResId(int lectureNumber) {
        return R.raw.vedio1;
    }
}
