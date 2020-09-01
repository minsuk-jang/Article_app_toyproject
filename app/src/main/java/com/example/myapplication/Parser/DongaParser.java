package com.example.myapplication.Parser;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

public class DongaParser {
    private Context context;
    private String text;
    private int idx;
    private ViewGroup viewGroup;

    public DongaParser(Context context, ViewGroup viewGroup, String text) {
        this.viewGroup = viewGroup;
        this.context = context;
        this.text = text;

        init();
    }

    private void init() {
        Document body = Jsoup.parse(text);
        body.select("div.reporter_view, div.txt_ban, div.article_relation, ul.relation_list, div#bestnews_layer").empty();
        body.select("div.reporter_view, span.name, div.txt_ban, div.article_relation, ul.relation_list, div#bestnews_layer").unwrap();

        text = Jsoup.clean(body.html(), Whitelist.relaxed().addAttributes("p","class")
        .addAttributes("span","class").addTags("strong")
        .addAttributes("img","data-src","src")
        .removeTags("div"));
        //text = text.replaceAll("<br>","\n");
        text = text.replaceAll("&nbsp","\n");
        text = text.replaceAll(";","");
        text = text.replaceAll("&amp","&");
    }


    public void parsing() {
        StringBuilder sb = new StringBuilder();
        while (idx < text.length()) {
            char current = text.charAt(idx);

            if(current == '<'){
                if(!sb.toString().isEmpty()){
                    //만들어진 텍스트가 있는 경우
                    viewGroup.addView(makeTextView(sb.toString().trim()));
                    sb.delete(0,sb.length());
                }

                String tag = makeTag();

                //닫는 태그인 경우,
                if(tag.indexOf("/") == 1) {
                    idx++;
                    continue;
                }

                if(tag.contains("img")){
                    //이미지 태그인 경우
                    viewGroup.addView(makeImageView(tag));
                } else if (tag.contains("span")) {
                    if(tag.contains("txt")){
                        viewGroup.addView(makeCaptionView());
                        continue;
                    }
                }
            }else
                sb.append(current);

            idx++;
        }
    }

    private String makeTag(){
        StringBuilder sb = new StringBuilder();
        while(true){
            char current = text.charAt(idx);

            if(current == '>'){
                sb.append(current);
                return sb.toString();
            }

            sb.append(current);
            idx++;
        }

    }

    private View makeTextView(String text){
        TextView view = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,5,0,5);
        view.setText(text);
        view.setTextSize(15);
        view.setTextColor(Color.parseColor("#000000"));
        view.setLayoutParams(params);

        return view;
    }

    //캡션 텍스트뷰를 만드는 메소드
    private View makeCaptionView(){
        StringBuilder sb = new StringBuilder();
        char current = ' ';

        while (true) {
            current = text.charAt(idx);

            if (current == '<') {
                break;
            }

            sb.append(current);
            idx++;
        }

        TextView view = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,2,0,5);
        view.setLayoutParams(params);
        view.setText(sb.toString().trim());
        view.setTextSize(10);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setTextColor(Color.parseColor("#aaaaaa"));

        return view;
    }

    private View makeImageView(String src) {
        int start = src.indexOf("src");
        start += 5;

        StringBuilder sb = new StringBuilder();
        char current = ' ';

        while (true) {
            current = src.charAt(start);

            if (current == '"')
                break;

            sb.append(current);
            start++;
        }

        ImageView view = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        view.setLayoutParams(params);
        view.setAdjustViewBounds(true);

        Glide.with(context).load(sb.toString()).placeholder(R.drawable.ic_launcher_foreground).fitCenter().into(view);
        return view;
    }
}
