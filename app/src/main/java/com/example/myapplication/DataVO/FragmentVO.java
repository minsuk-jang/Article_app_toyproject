package com.example.myapplication.DataVO;

import androidx.fragment.app.Fragment;

public class FragmentVO {
    String title;
    Fragment fragment;
    int icon;

    public FragmentVO(String title, Fragment fragment, int icon) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(String title){
        this.title =title;
    }

    public void setFragment(Fragment fragment){
        this.fragment = fragment;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
