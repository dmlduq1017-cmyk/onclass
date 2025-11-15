package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private final String[] questionTitles = {
            "Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7", "Q8", "Q9", "Q10"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LinearLayout resultContainer = findViewById(R.id.resultContainer);

        ArrayList<String> userAnswers = getIntent().getStringArrayListExtra("userAnswers");
        String[] oxAnswers = getIntent().getStringArrayExtra("oxAnswers");
        String[] choiceAnswersText = getIntent().getStringArrayExtra("choiceAnswersText");
        String[] shortAnswers = getIntent().getStringArrayExtra("shortAnswers");

        int correctCount = 0;

        for (int i = 0; i < 10; i++) {
            String user = userAnswers.get(i);
            String correct = "";

            if (i < 2) { // OX 문제
                correct = oxAnswers[i];
            } else if (i < 7) { // 객관식
                correct = choiceAnswersText[i - 2];
            } else { // 단답형
                correct = shortAnswers[i - 7];
            }

            boolean isCorrect = user.equalsIgnoreCase(correct);
            if (isCorrect) correctCount++;

            TextView resultText = new TextView(this);
            resultText.setText(String.format("%s\n내 답변: %s\n정답: %s\n%s\n",
                    questionTitles[i], user, correct, isCorrect ? "정답" : "오답"));
            resultText.setPadding(8, 8, 8, 8);
            resultContainer.addView(resultText);
        }

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("총 맞은 개수: " + correctCount + " / 10");

        // 퀴즈 목록으로 돌아가는 버튼
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            // 퀴즈 목록으로 이동 QuizActivity로 이동
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            // 이전 액티비티들을 모두 종료하고 퀴즈로 이동
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
