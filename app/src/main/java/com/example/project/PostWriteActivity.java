package com.example.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostWriteActivity extends Activity {

    private EditText titleEditText, contentEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private PostDatabaseHelper dbHelper;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        titleEditText = findViewById(R.id.editTextTitle);
        contentEditText = findViewById(R.id.editTextContent);
        categorySpinner = findViewById(R.id.spinnerCategory);
        saveButton = findViewById(R.id.buttonSave);
        dbHelper = new PostDatabaseHelper(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.post_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // 뒤로가기 버튼 이벤트 추가
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티 종료
            }
        });

        // 저장 버튼 클릭 이벤트
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();
            String selected = categorySpinner.getSelectedItem().toString();
            String category;

            switch (selected) {
                case "자유":
                    category = "free";
                    break;
                case "스터디":
                    category = "study";
                    break;
                case "꿀팁":
                    category = "tip";
                    break;
                case "HOT":
                    category = "hot";
                    break;
                default:
                    category = "free";
                    break;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(PostWriteActivity.this, "제목과 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            Post post = new Post(0, title, content, category, date, 0, 0);
            boolean success = dbHelper.insertPost(post);

            if (success) {
                Toast.makeText(PostWriteActivity.this, "게시글이 저장되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(PostWriteActivity.this, "저장 실패. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
