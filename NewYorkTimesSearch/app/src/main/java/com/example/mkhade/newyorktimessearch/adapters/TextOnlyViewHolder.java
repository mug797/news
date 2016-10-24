package com.example.mkhade.newyorktimessearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkhade.newyorktimessearch.R;
import com.example.mkhade.newyorktimessearch.activity.ArticleActivity;
import com.example.mkhade.newyorktimessearch.models.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mkhade on 10/23/2016.
 */

public class TextOnlyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.tvTextOnlyTitle) TextView tvTextOnlyArticle;
    List<Article> articles;
    Context mContext;

    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
        Article article = articles.get(position);
        Intent i = new Intent(mContext, ArticleActivity.class);
        i.putExtra("url", article.getWeburl());
        mContext.startActivity(i);
    }

    public TextView getTvTextOnlyArticle() {
        return tvTextOnlyArticle;
    }

    public void setTvArticleText(TextView tvTextOnlyArticle) {
        this.tvTextOnlyArticle = tvTextOnlyArticle;
    }

    public TextOnlyViewHolder(Context context, View view, List<Article> mArticles) {
        super(view);
        this.articles = mArticles;
        this.mContext = context;
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);
    }
}
