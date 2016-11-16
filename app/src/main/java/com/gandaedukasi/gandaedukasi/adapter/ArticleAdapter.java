package com.gandaedukasi.gandaedukasi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.DetailArticleActivity;
import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.models.Article;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 7/31/2016.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView articleTitle;
        //TextView articleDescription;
        ImageView articleImage;
        public ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Article feedItem = articles.get(getAdapterPosition());
                    Log.d("Item Clicked", "Element " + feedItem.title + " clicked.");
                    //Toast.makeText(mContext, "Element " + feedItem.id_article + " clicked.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(mContext, DetailArticleActivity.class);
                    i.putExtra("id_article", feedItem.id_article);
                    i.putExtra("title", feedItem.title);
                    i.putExtra("content", feedItem.content);
                    i.putExtra("cover", feedItem.cover);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);

                }
            });
            cv = (CardView)itemView.findViewById(R.id.cvArticle);
            articleTitle = (TextView)itemView.findViewById(R.id.articleTitle);
            //articleDescription = (TextView)itemView.findViewById(R.id.articleDescription);
            articleImage = (ImageView)itemView.findViewById(R.id.articleImage);
        }
    }

    List<Article> articles;
    private Context mContext;
    FragmentTransaction fragmentTransaction;
    public ArticleAdapter(Context context, List<Article> articles, FragmentTransaction fragmentTransaction){
        this.articles = articles;
        this.mContext = context;
        this.fragmentTransaction = fragmentTransaction;
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
        if(!articles.get(i).cover.equals("")){
            Log.d("iamge cover",new RequestServer().getImages_url()+"article/"+articles.get(i).cover);
            Ion.with(mContext)
                    .load(new RequestServer().getImages_url()+"article/"+articles.get(i).cover)
                    .withBitmap()
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .intoImageView(articleViewHolder.articleImage);
        }

        articleViewHolder.articleTitle.setText(articles.get(i).title);
        //articleViewHolder.articleDescription.setText(articles.get(i).created_at);
        //articleViewHolder.articleImage.setImageResource(R.drawable.logo);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
