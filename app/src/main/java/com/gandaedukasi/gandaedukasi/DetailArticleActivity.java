package com.gandaedukasi.gandaedukasi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandaedukasi.utility.RequestServer;
import com.koushikdutta.ion.Ion;

public class DetailArticleActivity extends AppCompatActivity {
    private String article_id,title,content,cover;
    private TextView tvJudul, tvContent;
    private ImageView imgCover;
    private WebView myWebView;
    ProgressDialog prDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article_id = getIntent().getStringExtra("id_article");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        cover = getIntent().getStringExtra("cover");

        setContentView(R.layout.activity_detail_article);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());

        myWebView.loadUrl("http://api.edukezy.com/article/detail/"+article_id);

        /*
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
        */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(DetailArticleActivity.this);
            prDialog.setMessage("Please wait ...");
            prDialog.setCancelable(true);
            prDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(prDialog!=null){
                prDialog.dismiss();
            }
        }
    }

}
