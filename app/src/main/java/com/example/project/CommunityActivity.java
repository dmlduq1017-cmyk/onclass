package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton fabWrite;

    private final String[] tabTitles = {"HOT게시판", "자유게시판", "스터디게시판", "꿀팁게시판"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        fabWrite = findViewById(R.id.fab_write);

        viewPager.setAdapter(new BoardPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        // 글쓰기 버튼 클릭 시 PostWriteActivity로 이동
        fabWrite.setOnClickListener(v -> {
            Intent intent = new Intent(CommunityActivity.this, PostWriteActivity.class);
            startActivity(intent);
        });



        // 하단 홈버튼
        FloatingActionButton fab_home = findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //하단 네비게이션 버튼 동작
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_class) {
                Intent intent = new Intent(CommunityActivity.this, CategoryActivity.class); //클래스 목록 이동
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_community) {
                Intent intent = new Intent(CommunityActivity.this, CommunityActivity.class); //커뮤니티 이동
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_quiz) {
                Intent intent = new Intent(CommunityActivity.this, QuizActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(CommunityActivity.this, MyPageActivity.class);
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

    private static class BoardPagerAdapter extends FragmentStateAdapter {
        public BoardPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return BoardFragment.newInstance("hot");
                case 1:
                    return BoardFragment.newInstance("free");
                case 2:
                    return BoardFragment.newInstance("study");
                case 3:
                    return BoardFragment.newInstance("tip");
                default:
                    return BoardFragment.newInstance("free");
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
