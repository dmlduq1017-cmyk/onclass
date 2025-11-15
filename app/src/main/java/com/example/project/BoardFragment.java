package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BoardFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private String category;

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private PostDatabaseHelper dbHelper;

    public static BoardFragment newInstance(String category) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new PostDatabaseHelper(getContext());

        List<Post> postList;
        if ("HOT".equalsIgnoreCase(category)) {

            postList = dbHelper.getAllPostsByViews();
        } else {

            postList = dbHelper.getPostsByCategory(category);
        }

        adapter = new PostAdapter(postList, post -> {
            dbHelper.increaseViews(post.getId());
            Intent intent = new Intent(getContext(), PostDetailActivity.class);
            intent.putExtra("postId", post.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
