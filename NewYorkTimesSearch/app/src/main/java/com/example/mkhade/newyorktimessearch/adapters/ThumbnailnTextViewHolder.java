package com.example.mkhade.newyorktimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkhade.newyorktimessearch.R;
import com.example.mkhade.newyorktimessearch.activity.ArticleActivity;
import com.example.mkhade.newyorktimessearch.models.Article;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mkhade on 10/23/2016.
 */

public class ThumbnailnTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.ivImage) ImageView ivArticle;
    @BindView(R.id.tvTitle) TextView tvArticle;
    List<Article> articles;
    Context mContext;

    @Override
    public void onClick(View v) {
        int position = getLayoutPosition();
        Article article = articles.get(position);

        Intent i = new Intent(mContext, ArticleActivity.class);
        i.putExtra("url", article.getWeburl());
        //i.putExtras("article", Parcels);
        mContext.startActivity(i);
    }

    public ImageView getIvArticle() {
        return ivArticle;
    }

    public void setIvArticle(ImageView ivArticle) {
        this.ivArticle = ivArticle;
    }

    public TextView getTvArticle() {
        return tvArticle;
    }

    public void setTvArticle(TextView tvArticle) {
        this.tvArticle = tvArticle;
    }


    public ThumbnailnTextViewHolder(Context context, View view, List<Article> mArticles) {
        super(view);
        this.articles = mArticles;
        this.mContext = context;
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);
    }
}
