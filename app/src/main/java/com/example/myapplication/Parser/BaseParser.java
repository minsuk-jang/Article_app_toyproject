package com.example.myapplication.Parser;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseParser {
    protected LinearLayout linearLayout;
    protected String text;
    protected List<Map<String, String>> lists;

    protected BaseParser(LinearLayout linearLayout, String text) {
        this.linearLayout = linearLayout;
        this.text = text;
        lists = new ArrayList<>();

    }

    //초기 설정
    abstract void init();

    //파싱 및 컴포넌트 추가
    abstract void addComponent();

    //이미지 추가
    protected void addImageViewComponent(String src) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 5);

        ImageView imageView = new ImageView(linearLayout.getContext());
        Glide.with(linearLayout.getContext())
                .load(src)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);

        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(params);
        linearLayout.addView(imageView);
    }

    protected TextView makeTextViewComponent(int left, int right, int top, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);

        TextView textView = new TextView(linearLayout.getContext());
        textView.setLayoutParams(params);
        return textView;

    }
}
