package com.example.myapplication;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DataVO.articleVO;

import java.util.ArrayList;
import java.util.List;

public class articleFragment extends Fragment {
    private String title;
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;
    private ProgressBar init_progress;
    private RelativeLayout relativeLayout;
    private final int VIEW_THRESHOLD = 1;
    private ArrayList<articleVO> list;
    private Parser parser;
    private boolean init = true;

    //fragment는 생성자를 한번만 호출한다.
    public articleFragment(String title) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("jms", title + " onCreateView....");

        View view = inflater.inflate(R.layout.article_fragment, container, false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_container);
        init_progress = (ProgressBar) view.findViewById(R.id.init_progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(relativeLayout.getContext()));

        adapter = new RecycleAdapter(recyclerView,getContext());
        adapter.setList(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadListener(new onLoadListener() {
            @Override
            public void onLoad(List<articleVO> list) {
                //데이터를 얻는다
                Log.d("jms", title + " Down swipe");
                list.add(null);
                adapter.notifyItemInserted(list.size() - 1);
                new Handler().postDelayed(new ImportData(list), 2000);
            }
        });

        if (list == null)
            init();
        else{
            adapter.notifyDataSetChanged();
            init_progress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        recyclerView.addItemDecoration(new ItemDeco());
        return relativeLayout;
    }

    private void init() {
        parser = new Parser(title,0,list,getContext(),adapter);
        parser.setInit(recyclerView,init_progress);
        parser.execute();
    }

    private class ItemDeco extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int totalCount = manager.getItemCount();
            int lastItem = manager.findLastVisibleItemPosition();

            outRect.set(20, 20, 20, 20);
            view.setBackgroundColor(0xFFEcE9E9);
            ViewCompat.setElevation(view, 20.0f);


        }
    }


    private class ImportData implements Runnable {
        private List<articleVO> list;

        public ImportData(List<articleVO> l) {
            this.list = l;
        }

        @Override
        public void run() {
            list.remove(list.size() - 1);
            adapter.notifyItemRemoved(list.size());

            int idx = list.size();
            int end = list.size() + 5;

            for (int i = idx; i < end; i++) {
                String t = title + i;
                list.add(new articleVO("", "", title, title + "~~~~~"));
            }

            adapter.notifyDataSetChanged();
            adapter.setLoad();
        }

    }

}