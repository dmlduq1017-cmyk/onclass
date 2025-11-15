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

public class JapaneseFragment extends Fragment {

    public JapaneseFragment() {
        // Required empty public constructor
    }

    ListView langList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language_jpn, container, false);
        langList = view.findViewById(R.id.langlist2);

        List<LanguageItem> items = new ArrayList<>();
        items.add(new LanguageItem("권혜영의 쉽게 배우는 일본어", "난이도 하 / 평점 4.5 (102)", R.drawable.tc11));
        items.add(new LanguageItem("사유리의 노빠꾸 일본어", "난이도 중 / 평점 4.7 (167)", R.drawable.tc12));
        items.add(new LanguageItem("카나의 일어 시험 마스터", "난이도 상 / 평점 4.4 (189)", R.drawable.tc13));
        items.add(new LanguageItem("일본어는 자신감, 김지남", "난이도 하 / 평점 4.6 (128)", R.drawable.tc14));
        items.add(new LanguageItem("황선아의 일본어 고민상담소", "난이도 하 / 평점 4.8 (183)", R.drawable.tc15));
        items.add(new LanguageItem("곤니찌와 티칭제이데스", "난이도 중 / 평점 4.8 (126)", R.drawable.tc16));
        items.add(new LanguageItem("오오기의 니혼 톡톡", "난이도 상 / 평점 4.7 (227)", R.drawable.tc17));
        items.add(new LanguageItem("리에하타의 Real Hot Tokyo", "난이도 하 / 평점 4.9 (198)", R.drawable.tc18));
        items.add(new LanguageItem("모모, 김영민의 모구모구 일어", "난이도 중 / 평점 4.8 (116)", R.drawable.tc19));
        items.add(new LanguageItem("다이스키, 다이치상", "난이도 하 / 평점 4.9 (286)", R.drawable.tc20));

        LanguageAdapter adapter = new LanguageAdapter(getContext(), items);
        langList.setAdapter(adapter);

        return view;
    }
}
