package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataVO.ArticleVO;

public class ShowArticleActivity extends AppCompatActivity {
    TextView title, content;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_show_article);
        Intent intent = getIntent();

        String img_url = intent.getStringExtra("img_url");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        this.title = (TextView)findViewById(R.id.title);
        this.content =(TextView)findViewById(R.id.content);

        this.title.setText(title);
        this.content.setText(Html.fromHtml(content));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
