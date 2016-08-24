package com.gandaedukasi.gandaedukasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DetailArticleActivity extends AppCompatActivity {
    private WebView mWebView;
    private TextView tvLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        tvLoading = (TextView) findViewById(R.id.tvLoading);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://beta.html5test.com/");
        mWebView.setWebViewClient(new WebViewClient());
    }
}
