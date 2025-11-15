package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.contentTextView.setText(comment.getContent());
        holder.dateTextView.setText(comment.getDate());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView, dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.text_comment_content);
            dateTextView = itemView.findViewById(R.id.text_comment_date);
        }
    }
}
