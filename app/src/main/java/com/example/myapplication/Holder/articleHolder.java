package com.example.myapplication.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class articleHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView title,content;

    public articleHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView)itemView.findViewById(R.id.imageView);
        title = (TextView)itemView.findViewById(R.id.title);
        content = (TextView)itemView.findViewById(R.id.content);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getContent() {
        return content;
    }

    public TextView getTitle() {
        return title;
    }
}
