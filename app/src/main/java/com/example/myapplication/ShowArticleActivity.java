package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.example.myapplication.Parser.ParserHelper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.w3c.dom.Text;

public class ShowArticleActivity extends AppCompatActivity {
    TextView title;
    LinearLayout linearLayout;
    private final int subtitle_size = 17, title_size = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_show_article);
        linearLayout = (LinearLayout) findViewById(R.id.container);

        Intent intent = getIntent();

        String s = intent.getStringExtra("subject");
        String t = intent.getStringExtra("title");
        String c = intent.getStringExtra("content");

        addTitleView(s, t);
        ParserHelper.attachCompoent(linearLayout, s, c);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void addTitleView(String s, String t) {
        if (s.equals("kukmin")) {
            LinearLayout.LayoutParams layout_params = makeParams(0, 3, 0, 10);

            LinearLayout temp_layout = new LinearLayout(this);
            temp_layout.setLayoutParams(layout_params);
            temp_layout.setOrientation(LinearLayout.VERTICAL);

            TextView main = makeTextView(0, 0, 0, 3, "#000000");
            TextViewCompat.setTextAppearance(main, android.R.style.TextAppearance_Material_Headline);
            main.setTextSize(title_size);
            main.setTypeface(null, Typeface.BOLD);

            String main_txt = Jsoup.parse(t).select("h3").text();
            main.setText(main_txt);

            temp_layout.addView(main);
            String sub_txt = Jsoup.parse(t).select("h4").text().trim();

            if (!sub_txt.isEmpty()) {
                TextView sub = makeTextView(0, 0, 0, 5, "#d36b1f");
                sub.setTextSize(subtitle_size);
                sub.setPadding(20, 0, 0, 0);
                sub.setBackground(getResources().getDrawable(R.drawable.kukmin_ab_sub_heading));
                sub.setText(sub_txt);
                sub.setTypeface(null, Typeface.BOLD);
                temp_layout.addView(sub);
            }

            linearLayout.addView(temp_layout);
        } else {
            TextView textView = makeTextView(0, 0, 0, 3, "#000000");
            TextViewCompat.setTextAppearance(textView, android.R.style.TextAppearance_Material_Headline);
            textView.setTextSize(title_size);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(Jsoup.clean(t, Whitelist.simpleText()));

            linearLayout.addView(textView);
        }
    }

    private TextView makeTextView(int left, int top, int right, int bottom, String color) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);

        TextView textView = new TextView(this);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.parseColor(color));

        return textView;
    }

    private LinearLayout.LayoutParams makeParams(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);

        return params;
    }

}
