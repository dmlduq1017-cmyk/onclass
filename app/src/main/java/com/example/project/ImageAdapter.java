package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private int[] images;

    public ImageAdapter(int[] images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);

        if (position == 0) {
            // 첫 번째 이미지 클릭 -> BeomguActivity
            holder.imageView.setOnClickListener(v -> {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, BeomguActivity.class);
                context.startActivity(intent);
            });
        } else if (position == 2) {
            // 세 번째 이미지 클릭 -> FoodRadioActivity
            holder.imageView.setOnClickListener(v -> {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, FoodradioActivity.class);
                context.startActivity(intent);
            });
        } else {
            holder.imageView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageViewGallery);
        }
    }
}
