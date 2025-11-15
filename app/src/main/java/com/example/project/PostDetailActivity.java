package com.example.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private TextView titleTextView, contentTextView, dateTextView, viewsTextView;
    private EditText commentEditText;
    private Button commentButton;
    private RecyclerView commentRecyclerView;

    private PostDatabaseHelper dbHelper;
    private CommentDatabaseHelper commentDbHelper;
    private CommentAdapter commentAdapter;

    private String postDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        titleTextView = findViewById(R.id.detail_title);
        contentTextView = findViewById(R.id.detail_content);
        dateTextView = findViewById(R.id.detail_date);
        viewsTextView = findViewById(R.id.detail_views);
        commentEditText = findViewById(R.id.edit_comment);
        commentButton = findViewById(R.id.btn_submit_comment);
        commentRecyclerView = findViewById(R.id.recycler_view_comments);

        dbHelper = new PostDatabaseHelper(this);
        commentDbHelper = new CommentDatabaseHelper(this);

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(new ArrayList<>());
        commentRecyclerView.setAdapter(commentAdapter);

        postDocumentId = getIntent().getStringExtra("postId");
        if (postDocumentId == null) {
            Toast.makeText(this, "게시글 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper.increaseViews(postDocumentId, new FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                loadPost(true);
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(PostDetailActivity.this, "조회수 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show());
                loadPost(false);
            }
        });

        commentButton.setOnClickListener(v -> {
            String commentText = commentEditText.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            Comment comment = new Comment(postDocumentId, commentText, now);
            commentButton.setEnabled(false);

            commentDbHelper.insertComment(postDocumentId, comment, new FirestoreCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    runOnUiThread(() -> {
                        commentEditText.setText("");
                        commentButton.setEnabled(true);
                        Toast.makeText(PostDetailActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        loadComments();
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        commentButton.setEnabled(true);
                        Toast.makeText(PostDetailActivity.this, "댓글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        loadComments();

        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(v -> finish());
    }

    private void loadPost(boolean incremented) {
        dbHelper.getPostById(postDocumentId, new FirestoreCallback<Post>() {
            @Override
            public void onSuccess(Post post) {
                runOnUiThread(() -> {
                    if (post == null) {
                        Toast.makeText(PostDetailActivity.this, "게시글을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    titleTextView.setText(post.getTitle());
                    contentTextView.setText(post.getContent());
                    dateTextView.setText(post.getDate());

                    int views = post.getViews();
                    if (incremented) {
                        views += 1;
                    }
                    viewsTextView.setText("조회수: " + views);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(PostDetailActivity.this, "게시글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadComments() {
        commentDbHelper.getCommentsByPostId(postDocumentId, new FirestoreCallback<java.util.List<Comment>>() {
            @Override
            public void onSuccess(java.util.List<Comment> result) {
                runOnUiThread(() -> commentAdapter.updateComments(result));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> Toast.makeText(PostDetailActivity.this, "댓글을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
