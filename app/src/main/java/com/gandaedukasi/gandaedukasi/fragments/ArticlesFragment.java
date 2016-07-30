package com.gandaedukasi.gandaedukasi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.adapter.ArticleAdapter;
import com.gandaedukasi.gandaedukasi.models.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends Fragment {
    private List<Article> articles;
    protected RecyclerView mRecyclerView;
    protected ArticleAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list_articles, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvArticle);
        mAdapter = new ArticleAdapter(getActivity().getApplicationContext(),articles);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }
    private void initializeData(){
        articles = new ArrayList<>();
        articles.add(new Article("Emma Wilson", "23 years old", "http://gandaedukasi.esy.es/images/article/image1.png"));
        articles.add(new Article("Lavery Maiss", "25 years old", "http://gandaedukasi.esy.es/images/article/image1.png"));
        articles.add(new Article("Lillie Watts", "35 years old", "http://gandaedukasi.esy.es/images/article/image1.png"));
    }

}
