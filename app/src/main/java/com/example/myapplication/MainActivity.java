package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.skydoves.transformationlayout.TransformationLayout;

import java.util.ArrayList;
import java.util.List;

//기사의 화면을 보여주는 액티비티
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private long pressedTime = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton menu;
    private TransformationLayout transformationLayout;
    private boolean expand = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(expand){
                transformationLayout.finishTransform();
                expand = !expand;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(receiver,new IntentFilter("com.example.Expand_collapse"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> article_list = (ArrayList<String>) getIntent().getSerializableExtra("article_list");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        transformationLayout = (TransformationLayout) findViewById(R.id.translayout);
        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand)
                    transformationLayout.startTransform();

                expand = !expand;
            }
        });

        init(article_list);
    }


    //초기 설정
    private void init(List<String> article_list) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);

        for (String title : article_list) {
            int logo = getLogoImage(title);
            if (logo == -1) {
                //todo 에러 처리
            } else
                viewPagerAdapter.addFragment(logo, title, new ArticleFragment(title));
        }

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentVo(i).getIcon());
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if(expand){
            transformationLayout.finishTransform();
            expand = !expand;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        if(expand){
            transformationLayout.finishTransform();
            expand = !expand;
        }

        if (pressedTime == 0) {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            long end = System.currentTimeMillis();
            if (end - pressedTime > 2000) { //누른지 2초가 지난 후,
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                unregisterReceiver(receiver);
                pressedTime = end;
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }

    //todo 개선 필요
    private int getLogoImage(String title) {
        if (title.equals("google"))
            return R.drawable.google;
        else if (title.equals("donga"))
            return R.drawable.donga;
        else if (title.equals("joongang"))
            return R.drawable.joongang;
        else if (title.equals("ytn"))
            return R.drawable.ytn;
        else if (title.equals("chosun"))
            return R.drawable.chosun;
        else if (title.equals("kukmin"))
            return R.drawable.kukmin;
        else if (title.equals("khan"))
            return R.drawable.khan_logo;

        return -1;
    }
}