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

    public articleFragment(String title) {
        this.title = title;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("test",title + " onResume....");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("test",title + " onDestroyView....");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test",title + " onDestroy....");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test",title + " onCreateView....");

        View view = inflater.inflate(R.layout.article_fragment, container, false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.fragment_container);
        init_progress = (ProgressBar) view.findViewById(R.id.init_progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(relativeLayout.getContext()));
        adapter = new RecycleAdapter(recyclerView);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadListener(new onLoadListener() {
            @Override
            public void onLoad(List<articleVO> list) {
                //데이터를 얻는다
                Log.d("test", title + " Down swipe");
                list.add(null);
                adapter.notifyItemInserted(list.size()-1);
                new Handler().postDelayed(new ImportData(list), 2000);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<articleVO> list = new ArrayList<>();
                for(int i = 0 ; i < 6 ; i++) {
                    String t = title + i;
                    list.add(new articleVO("", "", t, t + "~~~~~~"));
                }
                adapter.setList(list);
                init_progress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        }, 3000);

        recyclerView.addItemDecoration(new ItemDeco());
        return relativeLayout;
    }

    private class ItemDeco extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);


            outRect.set(20,20,20,20);
            view.setBackgroundColor(0xFFECE9E9);
            ViewCompat.setElevation(view,20.0F);
        }
    }

    private class ImportData implements Runnable {
        private List<articleVO> list;

        public ImportData(List<articleVO> l) {
            this.list = l;
        }

        @Override
        public void run() {
            list.remove(list.size()-1);
            adapter.notifyItemRemoved(list.size());

            int index = list.size();
            int end = index + 5;

            for (int i = index; i < end; i++) {
                String t = title + i;
                String content = t + "~~~~~~~~~~~~~~";

                list.add(new articleVO("", "", t, content));
            }

            adapter.notifyDataSetChanged();
            adapter.setLoad();

        }

    }

}