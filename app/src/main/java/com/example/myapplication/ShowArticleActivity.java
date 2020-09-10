package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Parser.ParserHelper;

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
        ParserHelper.attachCompoent(linearLayout,s,c);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
