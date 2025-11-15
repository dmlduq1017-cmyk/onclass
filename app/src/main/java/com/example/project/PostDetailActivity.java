package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private TextView titleTextView, contentTextView, dateTextView, viewsTextView;
    private EditText commentEditText;
    private Button commentButton;
    private RecyclerView commentRecyclerView;

    private PostDatabaseHelper dbHelper;
    private CommentDatabaseHelper commentDbHelper;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 뷰 초기화
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

        // 게시글 ID 가져오기
        postId = getIntent().getIntExtra("postId", -1);
        if (postId != -1) {
            dbHelper.increaseViews(postId);
            Post post = dbHelper.getPostById(postId);
            if (post != null) {
                titleTextView.setText(post.getTitle());
                contentTextView.setText(post.getContent());
                dateTextView.setText(post.getDate());
                viewsTextView.setText("조회수: " + (post.getViews() + 1));
            }
        }

        // 댓글 등록 버튼
        commentButton.setOnClickListener(v -> {
            String commentText = commentEditText.getText().toString().trim();
            if (!commentText.isEmpty()) {
                String now = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Comment comment = new Comment(postId, commentText, now);
                boolean success = commentDbHelper.insertComment(comment);
                if (success) {
                    commentEditText.setText("");
                    Toast.makeText(this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    loadComments();
                }
            } else {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        loadComments();

        // 뒤로가기 버튼 이벤트 추가
        ImageButton backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 액티비티 종료
            }
        });
    }

    private void loadComments() {
        commentList = commentDbHelper.getCommentsByPostId(postId);
        commentAdapter = new CommentAdapter(commentList);
        commentRecyclerView.setAdapter(commentAdapter);
    }
}
