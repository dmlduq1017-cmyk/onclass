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
import android.widget.Toast;

import java.util.ArrayList;
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

        adapter = new PostAdapter(new ArrayList<>(), post -> {
            String documentId = post.getDocumentId();
            if (documentId == null) {
                return;
            }
            dbHelper.increaseViews(documentId, new FirestoreCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    // no-op
                }

                @Override
                public void onError(Exception e) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "조회수 업데이트 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Intent intent = new Intent(getContext(), PostDetailActivity.class);
            intent.putExtra("postId", documentId);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        loadPosts();

        return view;
    }

    private void loadPosts() {
        if (getContext() == null) {
            return;
        }

        FirestoreCallback<List<Post>> callback = new FirestoreCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> result) {
                if (isAdded()) {
                    adapter.updatePosts(result);
                }
            }

            @Override
            public void onError(Exception e) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "게시글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        if ("HOT".equalsIgnoreCase(category)) {
            dbHelper.getAllPostsByViews(callback);
        } else {
            dbHelper.getPostsByCategory(category, callback);
        }
    }
}
