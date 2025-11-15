package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import java.util.ArrayList;

public class ClassquizActivity extends AppCompatActivity {

    private final String[] oxAnswers = {"X", "X"};
    private final int[] choiceAnswers = {2, 4, 3, 2, 2};
    private final String[] shortAnswers = {"sabbatical", "forbade", "compulsory"};
    private String[] choiceAnswersText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classquiz);

        setupSingleChoiceCheckboxes();

        choiceAnswersText = new String[5];
        for (int i = 0; i < 5; i++) {
            int correctIndex = choiceAnswers[i] - 1;
            int checkBoxId = getCheckBoxGroups()[i][correctIndex];
            AppCompatCheckBox cb = findViewById(checkBoxId);
            choiceAnswersText[i] = (cb != null) ? cb.getText().toString() : "(알 수 없음)";
        }

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> collectAnswersAndGoToResult());

        // 뒤로가기 버튼 이벤트 추가
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티 종료
            }
        });
    }

    private void setupSingleChoiceCheckboxes() {
        for (int[] group : getCheckBoxGroups()) {
            for (int id : group) {
                AppCompatCheckBox checkBox = findViewById(id);
                checkBox.setOnClickListener(v -> {
                    for (int otherId : group) {
                        if (otherId != id) {
                            ((AppCompatCheckBox) findViewById(otherId)).setChecked(false);
                        }
                    }
                });
            }
        }
    }

    private ArrayList<AppCompatCheckBox> getCheckBoxes(int[] ids) {
        ArrayList<AppCompatCheckBox> list = new ArrayList<>();
        for (int id : ids) {
            list.add(findViewById(id));
        }
        return list;
    }

    private void collectAnswersAndGoToResult() {
        ArrayList<String> userAnswers = new ArrayList<>();

        // OX 문제
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        RadioButton selectedButton1 = findViewById(radioGroup1.getCheckedRadioButtonId());
        userAnswers.add(selectedButton1 != null ? selectedButton1.getText().toString() : "미선택");

        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        RadioButton selectedButton2 = findViewById(radioGroup2.getCheckedRadioButtonId());
        userAnswers.add(selectedButton2 != null ? selectedButton2.getText().toString() : "미선택");

        // 객관식 문제
        for (int[] group : getCheckBoxGroups()) {
            String answer = "미선택";
            for (AppCompatCheckBox cb : getCheckBoxes(group)) {
                if (cb.isChecked()) {
                    answer = cb.getText().toString();
                    break;
                }
            }
            userAnswers.add(answer);
        }

        // 단답형 문제
        EditText editText8 = findViewById(R.id.editText8);
        EditText editText9 = findViewById(R.id.editText9);
        EditText editText10 = findViewById(R.id.editText10);

        userAnswers.add(editText8.getText().toString().trim());
        userAnswers.add(editText9.getText().toString().trim());
        userAnswers.add(editText10.getText().toString().trim());

        // 결과 화면으로 이동
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putStringArrayListExtra("userAnswers", userAnswers);
        intent.putExtra("oxAnswers", oxAnswers);
        intent.putExtra("choiceAnswersText", choiceAnswersText);
        intent.putExtra("shortAnswers", shortAnswers);
        startActivity(intent);
    }

    private int[][] getCheckBoxGroups() {
        return new int[][]{
                {R.id.checkBox3_1, R.id.checkBox3_2, R.id.checkBox3_3, R.id.checkBox3_4, R.id.checkBox3_5},
                {R.id.checkBox4_1, R.id.checkBox4_2, R.id.checkBox4_3, R.id.checkBox4_4, R.id.checkBox4_5},
                {R.id.checkBox5_1, R.id.checkBox5_2, R.id.checkBox5_3, R.id.checkBox5_4, R.id.checkBox5_5},
                {R.id.checkBox6_1, R.id.checkBox6_2, R.id.checkBox6_3, R.id.checkBox6_4, R.id.checkBox6_5},
                {R.id.checkBox7_1, R.id.checkBox7_2, R.id.checkBox7_3, R.id.checkBox7_4, R.id.checkBox7_5}
        };
    }
}
