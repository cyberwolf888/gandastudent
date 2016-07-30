package com.gandaedukasi.gandaedukasi.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.models.Article;

import java.util.List;

/**
 * Created by Karen on 7/31/2016.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView articleTitle;
        TextView articleDescription;
        ImageView articleImage;
        public ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article feedItem = articles.get(getPosition());
                    Log.d("Item Clicked", "Element " + feedItem.title + " clicked.");
                }
            });
            cv = (CardView)itemView.findViewById(R.id.cvArticle);
            articleTitle = (TextView)itemView.findViewById(R.id.articleTitle);
            articleDescription = (TextView)itemView.findViewById(R.id.articleDescription);
            articleImage = (ImageView)itemView.findViewById(R.id.articleImage);
        }
    }

    List<Article> articles;
    private Context mContext;
    public ArticleAdapter(Context context, List<Article> articles){
        this.articles = articles;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_article, viewGroup, false);
        ArticleViewHolder avh = new ArticleViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder articleViewHolder, int i) {
        articleViewHolder.articleTitle.setText(articles.get(i).title);
        articleViewHolder.articleDescription.setText(articles.get(i).content);
        articleViewHolder.articleImage.setImageResource(R.drawable.logo);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
