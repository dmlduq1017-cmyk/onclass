package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryActivity extends AppCompatActivity {
    GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classcategory);
        gv=(GridView)findViewById(R.id.gridView1);
        //GridView에 속성 설정 클래스 사용 BaseAdapter 클래스 사용 (내부클래스로 놓음)
        MyGridAdapter gAdapter = new MyGridAdapter(this); //내부 클래스는 인스턴스 생성 후
        gv.setAdapter(gAdapter);//그리드뷰에 아답터gAdapter(속성)를 설정하자

        //플로팅 버튼 (홈버튼) 이벤트 동작
        FloatingActionButton fab_home = findViewById(R.id.fab_home);
        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, HomeActivity.class);
                startActivity(intent);
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

        //하단 네비게이션 버튼 동작
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_class) {
                Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class); //클래스 목록 이동
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_community) {
                Intent intent = new Intent(CategoryActivity.this, CommunityActivity.class); //커뮤니티 이동
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_quiz) {
                Intent intent = new Intent(CategoryActivity.this, QuizActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_mypage) {
                Intent intent = new Intent(CategoryActivity.this, MyPageActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

    }//onCreate()

    public class MyGridAdapter extends BaseAdapter {
        Context context;
        Integer[] classID={R.drawable.ct1,R.drawable.ct2,R.drawable.ct3,R.drawable.ct4,R.drawable.ct5,R.drawable.ct6,R.drawable.ct7,R.drawable.ct8,R.drawable.last};
        public MyGridAdapter(Context c){
            context = c;
        }

        @Override
        public int getCount() { //항목의 갯수 리턴
            return classID.length;
        }

        @Override
        public Object getItem(int i) { //항목 리턴
            return null;
        }

        @Override
        public long getItemId(int i) { //항목의 아이디 리턴
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) { //그리드뷰 칸에 작은 이미지를 생성하고 클릭시 대화상자(큰그림)를 띄움
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(500,600));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5,10,5,20);
            imageView.setImageResource(classID[i]);
            final int pos = i;

            // 이미지 클릭 리스너 추가
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pos == 0) {
                        Intent intent = new Intent(context, LanguageActivity.class);
                        context.startActivity(intent);
                    } else {
                    }
                }
            });

            return imageView;
        }
    }//MyGridAdapter
}//MainActivity