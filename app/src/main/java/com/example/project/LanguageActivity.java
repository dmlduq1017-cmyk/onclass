package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LanguageActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private String[] titles = {"어학 [영어]", "어학 [일본어]", "어학 [중국어]", "어학 [제2외국어]"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        toolbar = findViewById(R.id.toolbar1);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(titles[0]);  // 초기 타이틀

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new LanguagePagerAdapter(this));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                toolbarTitle.setText(titles[position]);
            }
        });

        FloatingActionButton fab_home = findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //하단 네비게이션 버튼 동작
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_class) {
                Intent intent = new Intent(LanguageActivity.this, CategoryActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_community) {
                Intent intent = new Intent(LanguageActivity.this, CommunityActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_quiz) {
                Intent intent = new Intent(LanguageActivity.this, QuizActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(LanguageActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
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

    private static class LanguagePagerAdapter extends FragmentStateAdapter {
        public LanguagePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new EnglishFragment();
                case 1:
                    return new JapaneseFragment();
                case 2:
                    return new ChineseFragment();
                case 3:
                    return new OtherLanguageFragment();
                default:
                    return new EnglishFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
