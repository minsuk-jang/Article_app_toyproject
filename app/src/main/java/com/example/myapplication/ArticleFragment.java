package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DataVO.ArticleVO;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment {
    public static final int VIEW_LIMIT = 5;
    private String title;
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    private ProgressBar init_progress;
    private RelativeLayout relativeLayout;
    private List<ArticleVO> list;
    private Parser parser;
    private boolean scrolling;

    //fragment는 생성자를 한번만 호출한다.
    public ArticleFragment(String title) {
        this.title = title;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("jms", title + " onResume....");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("jms", title + " onDestroyView....");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("jms", title + " onDestroy....");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("jms", title + " onCreateView....");

        View view = inflater.inflate(R.layout.article_fragment, container, false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_container);
        init_progress = (ProgressBar) view.findViewById(R.id.init_progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(relativeLayout.getContext()));

        adapter = new RecycleAdapter(this.getContext());
        recyclerView.setAdapter(adapter);

        //맨 처음 수행할 경우
        if (list == null)
            init();

        return relativeLayout;
    }

    private void init() {
        list = new ArrayList<>();
        parser = new Parser(title, getContext(), adapter, recyclerView, init_progress);
        parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0, list); //AsyncTask를 병렬로 수행
    }


}