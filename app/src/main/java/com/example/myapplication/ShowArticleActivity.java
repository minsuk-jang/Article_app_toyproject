package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataVO.ArticleVO;
import com.example.myapplication.Parser.ParserHelper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ShowArticleActivity extends AppCompatActivity {
    TextView title;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_show_article);
        linearLayout = (LinearLayout) findViewById(R.id.container);

        Intent intent = getIntent();

        String s = intent.getStringExtra("subject");
        String t = intent.getStringExtra("title");
        String c = intent.getStringExtra("content");

        title = (TextView) findViewById(R.id.title);
        title.setText(t);

        ParserHelper.addComponent(linearLayout,this,s,c);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
