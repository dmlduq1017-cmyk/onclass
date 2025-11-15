package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDatabaseHelper(this);

        EditText idEditText = findViewById(R.id.editTextEmail_login);
        EditText passwordEditText = findViewById(R.id.editTextPassword_login);
        Button loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> {
            String id = idEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (dbHelper.checkUser(id, password)) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "로그인 실패. 정보 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

            if (dbHelper.checkUser(id, password)) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();

                // user_id 저장
                SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("user_id", id);
                editor.apply();

                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
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
}