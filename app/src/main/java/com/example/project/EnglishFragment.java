package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EnglishFragment extends Fragment {

    public EnglishFragment() {
        // Required empty public constructor
    }

    ListView langList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language_eng, container, false);
        langList = view.findViewById(R.id.langlist1);

        List<LanguageItem> items = new ArrayList<>();
        items.add(new LanguageItem("김범구의 See The Light", "난이도 상 / 평점 4.9 (390)", R.drawable.tc1));
        items.add(new LanguageItem("디바제시카의 토익 만점", "난이도 중 / 평점 5.0 (372)", R.drawable.tc2));
        items.add(new LanguageItem("조정식의 독해 0티어", "난이도 하 / 평점 4.2 (126)", R.drawable.tc3));
        items.add(new LanguageItem("대치동 맛보기, 스텔라T", "난이도 상 / 평점 4.5 (208)", R.drawable.tc4));
        items.add(new LanguageItem("강수정의 고시영어 마스터", "난이도 하 / 평점 4.8 (183)", R.drawable.tc5));
        items.add(new LanguageItem("김기훈, This is Talk", "난이도 중 / 평점 4.8 (126)", R.drawable.tc6));
        items.add(new LanguageItem("영어의 새로운 정의, 원정의", "난이도 상 / 평점 4.7 (227)", R.drawable.tc7));
        items.add(new LanguageItem("정승익, 영포자 탈출 챌린지", "난이도 하 / 평점 4.7 (183)", R.drawable.tc8));
        items.add(new LanguageItem("김보라의 수능 완성 VOCA", "난이도 중 / 평점 4.8 (186)", R.drawable.tc9));
        items.add(new LanguageItem("김유현의 지니어스 잉글리쉬", "난이도 상 / 평점 4.9 (218)", R.drawable.tc10));

        LanguageAdapter adapter = new LanguageAdapter(getContext(), items);
        langList.setAdapter(adapter);

        langList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), BeomguActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
