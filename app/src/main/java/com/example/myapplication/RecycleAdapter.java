package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataVO.ArticleVO;
import com.example.myapplication.Holder.ArticleHolder;
import com.example.myapplication.Holder.ProgressHolder;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_PROGRESS = 2;
    private final int VIEW_ARTICLE = 1;
    private List<ArticleVO> list;
    private Context context;

    public RecycleAdapter(Context context){
        this.context =context;
    }

    public void setList(List<ArticleVO> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_ARTICLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_article,parent,false);
            ArticleHolder articleHolder = new ArticleHolder(view);
            articleHolder.setOnItemClickListener(new onItemClickListener() {
                @Override
                public void onClickItem(int pos) {
                    Intent intent = new Intent(context,ShowArticleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티 하나만 띠운다.
                    intent.putExtra("subject",list.get(pos).getSubject());
                    intent.putExtra("title",list.get(pos).getTitle());
                    intent.putExtra("content",list.get(pos).getContent());

                    context.startActivity(intent);
                }
            });
            return articleHolder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_part,parent,false);
            return new ProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProgressHolder){
            ((ProgressHolder) holder).getProgressBar().setIndeterminate(true);
        }else if(holder instanceof ArticleHolder){
            Glide.with(this.context).load(list.get(position).getImg_src()).placeholder(R.drawable.ic_launcher_foreground).into(((ArticleHolder) holder).getImageView()); //이미지 넣기
            ((ArticleHolder) holder).getTitle().setText(list.get(position).getTitle());
            ((ArticleHolder) holder).getContent().setText(Jsoup.clean(list.get(position).getContent(), Whitelist.simpleText()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_PROGRESS : VIEW_ARTICLE;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


}
