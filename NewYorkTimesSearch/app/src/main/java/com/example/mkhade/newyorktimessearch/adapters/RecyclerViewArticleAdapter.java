package com.example.mkhade.newyorktimessearch.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mkhade.newyorktimessearch.R;
import com.example.mkhade.newyorktimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mkhade on 10/18/2016.
 */

/*
public class ArticleArrayAdapter extends ArrayAdapter<Article>{

    public ArticleArrayAdapter(Context context, ArrayList<Article> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article item = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        Log.d("DEEEBUGGG", item.getHeadline());
        tvTitle.setText(item.getHeadline());

        String thumbnail = item.getThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            //Picasso.with(getContext()).load(thumbnail).placeholder(R.drawable.ic_nocover).resize(200,0).into(imageView);
            Glide.with(getContext()).load(thumbnail).placeholder(R.drawable.ic_nocover).override(200, 200).into(imageView);
        }

        return convertView;
    }
}
*/


public class RecyclerViewArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Article> articles;
    private static Context mContext;

    private final int TEXTONLY = 0, THUMBNAIL = 1;

    public RecyclerViewArticleAdapter(Context context, ArrayList<Article> articles) {
        this.articles = articles;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }


    @Override
    public int getItemViewType(int position) {
        Article article = articles.get(position);
        if (TextUtils.isEmpty(article.getThumbnail())){
            return TEXTONLY;
        }
        return THUMBNAIL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case TEXTONLY:
                View view1 = inflater.inflate(R.layout.item_text_only_result,
                        viewGroup, false);
                viewHolder = new TextOnlyViewHolder(mContext, view1, articles);
                break;
            case THUMBNAIL:
                View view2 = inflater.inflate(R.layout.item_article_result,
                        viewGroup, false);
                viewHolder = new ThumbnailnTextViewHolder(mContext, view2, articles);
                break;
            default:
                view2 = inflater.inflate(R.layout.item_article_result,
                        viewGroup, false);
                viewHolder = new ThumbnailnTextViewHolder(mContext, view2, articles);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TEXTONLY:
                TextOnlyViewHolder vh1 = (TextOnlyViewHolder) viewHolder;
                configureTextArticleViewHolder(vh1, position);
                break;
            case THUMBNAIL:
                ThumbnailnTextViewHolder vh2 = (ThumbnailnTextViewHolder) viewHolder;
                configureThumbnailArticleViewHolder(vh2, position);
                break;
            default:
                ThumbnailnTextViewHolder vh = (ThumbnailnTextViewHolder) viewHolder;
                configureThumbnailArticleViewHolder(vh, position);
                break;
        }
    }

    private void configureTextArticleViewHolder(TextOnlyViewHolder viewHolder, int position) {
        Article article = articles.get(position);
        viewHolder.getTvTextOnlyArticle().setText(article.getHeadline());
    }


    private void configureThumbnailArticleViewHolder(ThumbnailnTextViewHolder viewHolder,
                                                     int position) {
        Article article = articles.get(position);
        //From Hints.
        viewHolder.getIvArticle().setImageResource(0);
        if (!TextUtils.isEmpty(article.getThumbnail())) {
            Glide.with(mContext).load(article.getThumbnail())
                    .placeholder(R.drawable.ic_nocover)
                    .into(viewHolder.getIvArticle());
        }
        viewHolder.getTvArticle().setText(article.getHeadline());
    }

    //TODO: test
/*    @Override public void onViewRecycled(RecyclerView.ViewHolder viewHolder){
        super.onViewRecycled(viewHolder);
        ThumbnailnTextViewHolder viewHolder1 = (ThumbnailnTextViewHolder) viewHolder;
        Glide.clear(viewHolder1.getIvArticle());
    }*/
}