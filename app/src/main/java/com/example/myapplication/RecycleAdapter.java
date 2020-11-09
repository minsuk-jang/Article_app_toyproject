package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataVO.ArticleVO;
import com.example.myapplication.Holder.ArticleHolder;

import org.jsoup.Jsoup;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ARTICLE = 1;
    private static final String DONGA = "donga", JOONGANG = "joongang", KUKMIN = "kukmin", KHAN = "khan", YTN = "ytn";

    private List<ArticleVO> list;
    private Context context;
    private int emptyImage;

    public RecycleAdapter(String title, Context context) {
        this.context = context;
        this.emptyImage = getImage(title);
    }



    private int getImage(String title) {
        int ret = 0;
        if (title.equals(YTN)) {
            ret = R.drawable.ytn;
        } else if (title.equals(KUKMIN))
            ret = R.drawable.kukmin;
        else if (title.equals(JOONGANG))
            ret = R.drawable.joongang;
        else if (title.equals(DONGA))
            ret = R.drawable.donga;
        else if (title.equals(KHAN))
            ret = R.drawable.khan_logo;

        return ret;
    }

    public void setList(List<ArticleVO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_article, parent, false);
        ArticleHolder articleHolder = new ArticleHolder(view);
        articleHolder.setOnItemClickListener(new onItemClickListener() {
            @Override
            public void onClickItem(int pos) {
                Intent intent = new Intent(context, ShowArticleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //액티비티 하나만 띠운다.
                intent.putExtra("subject", list.get(pos).getSubject());
                intent.putExtra("title", list.get(pos).getTitle());
                intent.putExtra("content", list.get(pos).getContent());

               context.startActivity(intent);

               Intent broadIntent = new Intent("com.example.Expand_collapse");
               context.sendBroadcast(broadIntent);

            }
        });
        return articleHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleHolder) {
            Glide.with(this.context).load(list.get(position).getImg_src()).placeholder(this.emptyImage).into(((ArticleHolder) holder).getImageView()); //이미지 넣기
            ((ArticleHolder) holder).getTitle().setText(Jsoup.parse(list.get(position).getTitle()).text());
            ((ArticleHolder) holder).getContent().setText(Jsoup.parse(list.get(position).getContent()).text());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_ARTICLE;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
