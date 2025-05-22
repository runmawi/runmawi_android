package com.atbuys.runmawi.Vdocipher;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.atbuys.runmawi.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview); // Reference your layout

        webView = findViewById(R.id.webview);

        Intent in=getIntent();
        url=in.getStringExtra("id");

        // Set a WebViewClient to ensure links are loaded inside the WebView
        webView.setWebViewClient(new WebViewClient());


        // Configure the WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false); // Allows video autoplay
        webSettings.setDomStorageEnabled(true);                 // Enable DOM storage for video players
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // Load a URL
        webView.loadUrl(url);
    }

    // Handle back button to prevent WebView from closing when navigating back
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

