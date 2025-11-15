package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.btn_login);
        Button signupButton = findViewById(R.id.btn_signup);

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        signupButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}
