package com.gandaedukasi.gandaedukasi.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.R;
import com.gandaedukasi.gandaedukasi.adapter.ArticleAdapter;
import com.gandaedukasi.gandaedukasi.models.Article;
import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends Fragment {
    private List<Article> articles;
    protected RecyclerView mRecyclerView;
    protected ArticleAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog pDialog;
    public JsonArray data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list_articles, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvArticle);
        mLayoutManager = new LinearLayoutManager(getActivity());
        if(isNetworkAvailable()) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            articles = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "article";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("request", true);

            Ion.with(getActivity().getApplicationContext())
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                //Log.d("Response",">"+result);
                                if (status.equals("1")) {
                                    initializeData(result.getAsJsonArray("data"));
                                    //articles.add(new Article("4", "Emma Wilson", "23 years old", "http://gandaedukasi.esy.es/images/article/image1.png"));

                                    mAdapter = new ArticleAdapter(getActivity().getApplicationContext(), articles, getActivity().getSupportFragmentManager().beginTransaction());
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Data article kosong!", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }
    public void initializeData(JsonArray result){
        Log.d("Response Data",">"+result);

        for (int i=0; i<result.size(); i++){
            JsonObject objData = result.get(i).getAsJsonObject();
            //Log.d("Response objData",">"+objData.get("judul").getAsString());
            String cover = "";
            if(!objData.get("cover").isJsonNull()){
                cover = objData.get("cover").getAsString();
            }
            articles.add(new Article(
                    objData.get("id").getAsString(),
                    objData.get("judul").getAsString(),
                    objData.get("content").getAsString(),
                    cover,
                    objData.get("created_at").getAsString()
            ));
        }

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


}
