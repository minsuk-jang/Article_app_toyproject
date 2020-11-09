package com.example.myapplication.DataVO;

import androidx.fragment.app.Fragment;

import com.example.myapplication.ArticleFragment;

public class FragmentVO {
    String title;
    ArticleFragment fragment;
    int icon;

    public FragmentVO(String title, ArticleFragment fragment, int icon) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(String title){
        this.title =title;
    }

    public void setFragment(ArticleFragment fragment){
        this.fragment = fragment;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ArticleFragment getFragment() {
        return fragment;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
