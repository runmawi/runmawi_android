package com.atbuys.runmawi;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class EncodedWebViewActivity extends AppCompatActivity {

    String url;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoded_web_view);

        url = getIntent().getStringExtra("url");
        //url = "https://dtglxyty91gn0.cloudfront.net/ap-south-1/4b7ee71f-c308-4568-a11d-559832ff46a6/out.m3u8";
        webView = findViewById(R.id.webview);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle back arrow click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack(); // Navigate back in WebView history
                } else {
                    finish(); // Close the activity if no more history
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        // Handle device back button
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}