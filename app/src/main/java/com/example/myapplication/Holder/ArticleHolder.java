package com.example.myapplication.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.onItemClickListener;

public class ArticleHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView title,content;
    private onItemClickListener onItemClickListener;

    public ArticleHolder(@NonNull final View itemView) {
        super(itemView);
        imageView = (ImageView)itemView.findViewById(R.id.imageView);
        title = (TextView)itemView.findViewById(R.id.title);
        content = (TextView)itemView.findViewById(R.id.content);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                onItemClickListener.onClickItem(pos);
            }
        });
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener =onItemClickListener;
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
