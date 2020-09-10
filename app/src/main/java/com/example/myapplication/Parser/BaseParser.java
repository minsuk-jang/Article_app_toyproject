package com.example.myapplication.Parser;

import android.widget.LinearLayout;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public abstract class BaseParser {
    protected LinearLayout linearLayout;
    protected String text;
    protected TagNode totalTagNode = null;
    protected HtmlCleaner cleaner;

    protected BaseParser(LinearLayout linearLayout, String text) {
        this.linearLayout = linearLayout;
        this.text = text;
        cleaner = new HtmlCleaner();
    }

    //초기 설정
    abstract void init();

    //레이아웃에 컴포넌트 붙이기
    abstract void addComponent(TagNode tagNode);
}
