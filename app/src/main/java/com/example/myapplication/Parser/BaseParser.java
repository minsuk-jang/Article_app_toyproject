package com.example.myapplication.Parser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public abstract class BaseParser {
    protected LinearLayout linearLayout;
    protected String text;
    protected TagNode totalTagNode = null;
    protected HtmlCleaner cleaner;
    protected Context context;

    protected BaseParser(LinearLayout linearLayout, String text) {
        this.linearLayout = linearLayout;
        this.text = text;
        this.context = linearLayout.getContext();
        cleaner = new HtmlCleaner();
    }

    //초기 설정
    abstract void init();

    //레이아웃에 컴포넌트 붙이기
    abstract void addComponent(TagNode tagNode);

    protected Drawable getDrawable(int id){
        return ResourcesCompat.getDrawable(context.getResources(),id,null);
    }

    protected TextView makeTextView(int left, int top , int right , int bottom, int textSize){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left,top,right,bottom);

        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        textView.setTextSize(textSize);
        return textView;
    }

    protected LinearLayout.LayoutParams getParams(int left ,int top ,int right, int bottom){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left,top,right,bottom);

        return params;
    }


    protected void addImageTagComponent(String src) {
        LinearLayout.LayoutParams params = getParams(0, 23, 0, 20);
        ImageView imageView = new ImageView(linearLayout.getContext());
        Glide.with(linearLayout.getContext())
                .load(src)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);

        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(params);

        linearLayout.addView(imageView);
    }

    protected LinearLayout makeLinearLayout(int left , int top ,int right , int bottom, int orientation){
        LinearLayout.LayoutParams params = getParams(left,top,right,bottom);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(orientation);

        return linearLayout;
    }
}
