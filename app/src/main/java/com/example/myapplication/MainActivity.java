package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
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

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//기사의 화면을 보여주는 액티비티
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private long pressedTime = 0;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> article_list = (ArrayList<String>) getIntent().getSerializableExtra("article_list");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        editText = (EditText) findViewById(R.id.search);
        editText.clearFocus();
        editText.setTextIsSelectable(true);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    Toast.makeText(getApplicationContext(), "Enter pressed", Toast.LENGTH_SHORT).show();
                    if (v instanceof EditText) {
                        hideKeyBoard((EditText) v);
                    }
                    return true;
                }
                return false;
            }
        });
        init(article_list);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();

            //EditView를 눌렀는지 확인
            if (view instanceof EditText) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);

                //범위 내에 없을 경우
                if (!rect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    hideKeyBoard((EditText) view);
                } else {
                    showKeyBoard((EditText) view);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //키보드를 보여준다
    private void showKeyBoard(EditText editText) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.requestFocus();
    }

    //키보드를 숨긴다
    private void hideKeyBoard(EditText editText) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        editText.clearFocus();
    }

    //초기 설정
    private void init(List<String> article_list) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);

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

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++)
            tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentVo(i).getIcon());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_appbar_menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder menuBuilder =(MenuBuilder)menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sports:
                Toast.makeText(this,"Sport menu clicked" , Toast.LENGTH_SHORT).show();;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            long end = System.currentTimeMillis();
            if (end - pressedTime > 2000) { //누른지 2초가 지난 후,
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
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

        return -1;
    }
}