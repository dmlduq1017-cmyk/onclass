package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LanguageAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<LanguageItem> items;

    public LanguageAdapter(Context context, List<LanguageItem> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
        TextView subtitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_language, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.profile_image);
            holder.title = convertView.findViewById(R.id.title);
            holder.subtitle = convertView.findViewById(R.id.subtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LanguageItem item = items.get(position);
        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle);
        holder.image.setImageResource(item.imageResId);

        return convertView;
    }
}
