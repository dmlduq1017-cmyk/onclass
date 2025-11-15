package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView, hotPostRecycler;
    TextView all_ct, all_com;
    ImageView btn2;
    private int[] adImages = {
            R.drawable.ad1,
            R.drawable.ad2,
            R.drawable.ad3
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //리사이클뷰
        recyclerView = findViewById(R.id.gallery1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ImageAdapter(adImages));

        //카테고리 버튼 어학
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LanguageActivity.class);
            startActivity(intent);
        });


        //클래스 전체보기
        all_ct = findViewById(R.id.all_ct);
        all_ct.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        //커뮤니티 전체보기
        all_com = findViewById(R.id.all_com);
        all_com.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
            startActivity(intent);
        });

        // 인기 게시글 표시
        PostDatabaseHelper dbHelper = new PostDatabaseHelper(this);
        List<Post> topPosts = dbHelper.getTopViewedPosts(3);
        hotPostRecycler = findViewById(R.id.recycler_view_hot_posts);
        hotPostRecycler.setLayoutManager(new LinearLayoutManager(this));
        PostAdapter adapter = new PostAdapter(topPosts, post -> {
            Intent intent = new Intent(HomeActivity.this, PostDetailActivity.class);
            intent.putExtra("postId", post.getId());
            startActivity(intent);
        });
        hotPostRecycler.setAdapter(adapter);

        //하단 네비게이션 버튼 동작
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_class) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_community) {
                Intent intent = new Intent(HomeActivity.this, CommunityActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_quiz) {
                Intent intent = new Intent(HomeActivity.this, QuizActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(HomeActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
