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
import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.DataVO.FragmentVO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.skydoves.transformationlayout.TransformationLayout;

import java.util.ArrayList;
import java.util.List;

//기사의 화면을 보여주는 액티비티
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private long pressedTime = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton menu;
    private TransformationLayout transformationLayout;
    private boolean expand = false;
    private int total_count = 0, MAX_COUNT = 2;
    private ImageButton cancel, ok;
    private int id_list[] = {R.id.google, R.id.donga, R.id.kukmin, R.id.ytn, R.id.joongang, R.id.chosun, R.id.khan};
    private LinearLayout select_layout;
    private List<ImageButton> buttons;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (expand) {
                transformationLayout.finishTransform();
                expand = !expand;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(receiver, new IntentFilter("com.example.Expand_collapse"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> article_list = (ArrayList<String>) getIntent().getSerializableExtra("article_list");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        transformationLayout = (TransformationLayout) findViewById(R.id.translayout);
        select_layout = (LinearLayout) findViewById(R.id.cardview);
        select_layout.setOnClickListener(this);
        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand) {
                    transformationLayout.startTransform();

                }

                expand = !expand;
            }
        });

        init(article_list);
    }


    //초기 설정
    private void init(List<String> article_list) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
        for (String title : article_list) {
            int logo = getLogoImage(title);
            if (logo == -1) {
                //todo 에러 처리
            } else {
                viewPagerAdapter.addFragment(logo, title, new ArticleFragment(title));
            }
        }

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentVo(i).getIcon());
        }

        buttons = new ArrayList<>();

        for (int i = 0; i < id_list.length; i++) {
            ImageButton temp = (ImageButton) findViewById(id_list[i]);
            temp.setOnClickListener(this);

            buttons.add(temp);
        }

        ok = (ImageButton) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        cancel = (ImageButton) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        isSelected(article_list);
    }

    private void isSelected(List<String> list) {
        for (String s : list) {
            for (ImageButton ib : buttons) {
                String name = convertName(ib.getId());

                if (name.equals(s)) {
                    total_count++;
                    ib.setSelected(true);
                }
            }
        }
    }

    private String convertName(int id) {
        String ret = null;
        switch (id) {
            case R.id.google:
                ret = "google";
                break;
            case R.id.donga:
                ret = "donga";
                break;
            case R.id.chosun:
                ret = "chosun";
                break;
            case R.id.joongang:
                ret = "joongang";
                break;
            case R.id.khan:
                ret = "khan";
                break;
            case R.id.kukmin:
                ret = "kukmin";
                break;
            case R.id.ytn:
                ret = "ytn";
                break;
        }

        return ret;
    }

    @Override
    public void onClick(View v) {
        if (v == select_layout) {

        } else if (v == ok) {
            List<ImageButton> select_button = new ArrayList<>();

            //선택된 이미지 버튼을 추가
            for (ImageButton ib : buttons) {
                if (ib.isSelected()) {
                    select_button.add(ib);
                }
            }

            //비교
            for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
                String title = ((ViewPagerAdapter)viewPager.getAdapter()).getFragmentVo(i).getTitle();

                boolean change = false;
                for (ImageButton ib : select_button) {
                    String ib_title = convertName(ib.getId());

                    if (title.equals(ib_title)) {
                        //이미 있는 기사가 나올 경우
                        select_button.remove((ImageButton) ib);
                        change = false;
                        break;
                    } else {
                        change = true;
                    }
                }

                ((ViewPagerAdapter)viewPager.getAdapter()).getFragmentVo(i).getFragment().setFragmentChange(change);
            }

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 1);
            for(int i =0 ; i < viewPager.getAdapter().getCount() ; i++){
                ArticleFragment articleFragment = ((ViewPagerAdapter)viewPager.getAdapter()).getFragmentVo(i).getFragment();

                if(!articleFragment.getFragmentChange()){
                    String title = articleFragment.getTitle();
                    viewPagerAdapter.addFragment(getLogoImage(title),title,new ArticleFragment(title));
                }
            }
            for(ImageButton ib : select_button){
                String ib_title = convertName(ib.getId());
                viewPagerAdapter.addFragment(getLogoImage(ib_title),ib_title,new ArticleFragment(ib_title));
            }

            viewPager.setAdapter(viewPagerAdapter);
            for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
                tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentVo(i).getIcon());
            }

            transformationLayout.finishTransform();
            expand = !expand;
        } else if (v == cancel) {
            transformationLayout.finishTransform();
            expand = !expand;
        } else if (v.isSelected()) {
            v.setSelected(false);
            total_count--;
        } else {
            if (total_count < MAX_COUNT) {
                v.setSelected(true);
                total_count++;
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (expand) {
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
        if (expand) {
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