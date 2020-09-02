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

public class JoongangParser {
    private Context context;
    private String text;
    private int idx = -1;
    private ViewGroup viewGroup;

    public JoongangParser(Context context, ViewGroup viewGroup, String text) {
        this.viewGroup = viewGroup;
        this.context = context;
        this.text = text;

        init();
    }

    private void init() {
        Document body = Jsoup.parse(text);

        text = Jsoup.clean(body.html(), Whitelist.relaxed().addAttributes("p", "class")
                .addAttributes("span", "class").addTags("strong")
                .addAttributes("img", "data-src", "src")
                .addAttributes("strong","class")
                .removeTags("div"));

        text = text.replaceAll("<br>", "\n");
        text = text.replaceAll("&nbsp", " ");
        text = text.replaceAll(";", "");
        text = text.replaceAll("\n\n","\n");
        text = text.replaceAll("&amp", "&");
    }


    public void parsing() {
        StringBuilder sb = new StringBuilder();
        while (++idx < text.length()) {
            char current = text.charAt(idx);

            if (current == '<') {
                if (!sb.toString().trim().isEmpty()) {
                    //만들어진 텍스트가 있는 경우
                    viewGroup.addView(makeTextView(sb.toString().trim()));
                    sb.delete(0, sb.length());
                }

                String tag = makeTag();

                //닫는 태그인 경우,
                if (tag.indexOf("/") == 1) {
                    idx++;
                    continue;
                }

                if (tag.contains("img")) {
                    //이미지 태그인 경우
                    viewGroup.addView(makeImageView(tag));
                } else {
                    //나머지 태그들에서
                    TextView view = makeTagView();
                    view = adjustClass(tag, view);

                    if(view != null)
                        viewGroup.addView(view);

                }

            } else {
                if (idx - 2 >= 0 && !sb.toString().isEmpty()) {
                    if ( sb.charAt(sb.length() - 1) != '\n' || current != ' ')
                        sb.append(current);

                } else
                    sb.append(current);
            }

        }

        if (!sb.toString().isEmpty()) {
            //만들어진 텍스트가 있는 경우
            viewGroup.addView(makeTextView(sb.toString().trim()));
            sb.delete(0, sb.length());
        }
    }

    private String makeTag() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char current = text.charAt(idx);

            if (current == '>') {
                sb.append(current);
                return sb.toString();
            }

            sb.append(current);
            idx++;
        }

    }

    private TextView adjustClass(String tag, TextView view) {
        LinearLayout.LayoutParams params = null;
        if (tag.contains("caption")) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 2, 0, 5);
            view.setLayoutParams(params);
            view.setTextColor(Color.parseColor("#aaaaaa"));
            view.setTextSize(10);
            view.setGravity(Gravity.CENTER_HORIZONTAL);

            return view;
        } else if (tag.contains("sub_title")) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 8, 0, 8);
            view.setLayoutParams(params);
            view.setTextColor(Color.parseColor("#46B8FF"));
            view.setTextSize(15);

            return view;
        }

        return null;
    }

    private TextView makeTagView() {
        idx++;
        StringBuilder sb = new StringBuilder();
        char current = ' ';
        while (true) {
            current = text.charAt(idx);

            if (current == '<') {
                --idx;
                break;
            }

            sb.append(current);
            idx++;
        }

        int idx = 0;

        while((idx = sb.indexOf("\n",idx+1)) > -1){
            int left = idx -1;
            int right = idx +1;

            if(left >= 0){
                if(sb.charAt(left) == ' ')
                    sb.deleteCharAt(left);
            }

            if(right < sb.length()){
                if(sb.charAt(right) == ' ')
                    sb.deleteCharAt(right);
            }
        }

        TextView textView = new TextView(context);
        textView.setText(sb.toString());

        return textView;
    }

    private TextView makeTextView(String text) {
        TextView view = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 3, 0, 3);
        view.setText(text);
        view.setTextSize(15);
        view.setTextColor(Color.parseColor("#000000"));
        view.setLayoutParams(params);

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
        params.setMargins(0, 13, 0, 10);
        view.setLayoutParams(params);
        view.setAdjustViewBounds(true);

        Glide.with(context).load(sb.toString()).placeholder(R.drawable.ic_launcher_foreground).fitCenter().into(view);
        return view;
    }
}
