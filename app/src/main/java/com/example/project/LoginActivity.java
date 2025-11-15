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

            loginButton.setEnabled(false);

            dbHelper.checkUser(id, password, new FirestoreCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    runOnUiThread(() -> {
                        loginButton.setEnabled(true);
                        if (Boolean.TRUE.equals(result)) {
                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("user_id", id);
                            editor.apply();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패. 정보 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        loginButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "로그인 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    });
                }
            });
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