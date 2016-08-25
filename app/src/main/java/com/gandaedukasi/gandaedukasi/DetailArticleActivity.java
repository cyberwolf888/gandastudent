package com.gandaedukasi.gandaedukasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.koushikdutta.ion.Ion;

public class DetailArticleActivity extends AppCompatActivity {
    private String article_id,title,content,cover;
    private TextView tvJudul, tvContent;
    private ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article_id = getIntent().getStringExtra("id_article");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        cover = getIntent().getStringExtra("cover");

        setContentView(R.layout.activity_detail_article);

        tvJudul = (TextView) findViewById(R.id.tvJudul);
        tvContent = (TextView) findViewById(R.id.tvContent);
        imgCover = (ImageView) findViewById(R.id.imgCover);

        if(!cover.equals("")){
            Ion.with(DetailArticleActivity.this)
                    .load(new RequestServer().getImages_url()+"article/"+cover)
                    .withBitmap()
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .intoImageView(imgCover);
            imgCover.setVisibility(View.VISIBLE);
        }

        tvJudul.setText(title);
        tvContent.setText(content);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
