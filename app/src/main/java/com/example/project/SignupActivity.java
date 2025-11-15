package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new UserDatabaseHelper(this);

        EditText nameEditText = findViewById(R.id.editTextName);
        EditText idEditText = findViewById(R.id.editTextEmail_signup);
        EditText passwordEditText = findViewById(R.id.editTextPassword_signup);
        EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        EditText birthEditText = findViewById(R.id.editTextBirth);
        RadioGroup genderGroup = findViewById(R.id.radioGroupGender);
        CheckBox termsCheckBox = findViewById(R.id.checkBoxTerms);
        Button signupButton = findViewById(R.id.buttonSignup);

        signupButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String id = idEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            String birth = birthEditText.getText().toString().trim();
            String gender = (genderGroup.getCheckedRadioButtonId() == R.id.radioMale) ? "남자" : "여자";

            if (id.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birth.isEmpty()) {
                Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!termsCheckBox.isChecked()) {
                Toast.makeText(this, "약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.isUserIdExists(id)) {
                Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.insertUser(id, name, password, birth, gender);
            if (success) {
                Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "회원가입 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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
