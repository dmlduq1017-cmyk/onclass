package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    private List<Post> postList;
    private OnItemClickListener listener;

    public PostAdapter(List<Post> postList, OnItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.titleTextView.setText(post.getTitle());
        holder.contentTextView.setText(post.getContent());
        holder.viewsTextView.setText("조회수: " + post.getViews());
        holder.dateTextView.setText(post.getDate());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, viewsTextView, dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_post_title);
            contentTextView = itemView.findViewById(R.id.text_post_content);
            viewsTextView = itemView.findViewById(R.id.text_post_views);
            dateTextView = itemView.findViewById(R.id.text_post_date);
        }
    }
}
