package com.atbuys.runmawi;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class EncodedWebViewActivity extends AppCompatActivity {

    String embed_url, url;
    WebView webView;

    // --- Variables for Full-Screen Video Handling ---
    private View mCustomView;
    private FrameLayout mCustomViewContainer;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;
    // ------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoded_web_view);

        embed_url = getIntent().getStringExtra("url");

        boolean isYouTubeUrl = false;
        String embed_url = getIntent().getStringExtra("url");

        if (embed_url != null) {

            String urlLower = embed_url.toLowerCase();

            if (urlLower.contains("youtube.com") || urlLower.contains("youtu.be")) {
                isYouTubeUrl = true;
            }
        }

        if (isYouTubeUrl) {
            url="https://runmawi.com/yt_embed.php?url="+embed_url;
        } else {
            url=embed_url;
        }



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
                // Use the main onBackPressed logic
                onBackPressed();
            }
        });

        // WebView Settings
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Set the custom WebChromeClient to handle full-screen video
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);
    }

// -------------------------------------------------------------------------------------------------

    /**
     * Custom WebChromeClient to handle full-screen video playback.
     */
    public class MyWebChromeClient extends WebChromeClient {

        // Called when a video element requests full-screen mode
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            // If a custom view is already showing, hide it first
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            // 1. Save original state
            mCustomView = view;
            mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            mOriginalOrientation = getRequestedOrientation();
            mCustomViewCallback = callback;

            // 2. Hide the main content (WebView and Toolbar)
            webView.setVisibility(View.GONE);
            findViewById(R.id.toolbar).setVisibility(View.GONE);

            // 3. Create a new FrameLayout for the video and add it to the activity's root view
            mCustomViewContainer = new FrameLayout(EncodedWebViewActivity.this);
            mCustomViewContainer.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mCustomViewContainer.setBackgroundColor(Color.BLACK); // Video background
            mCustomViewContainer.addView(mCustomView);

            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(mCustomViewContainer, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            // 4. Set the activity to full screen (hide status/navigation bars) and force sensor landscape
            // Use IMMERSIVE_STICKY to keep the full-screen view even when the user swipes the navigation bar
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            // Use SCREEN_ORIENTATION_SENSOR_LANDSCAPE to allow both landscape modes
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

            // NOTE: Do NOT call any method on callback here. It is handled by the framework.
        }

        // Called when the video exits full-screen mode
        @Override
        public void onHideCustomView() {
            if (mCustomView == null) return;

            // 1. Show the main content (WebView and Toolbar)
            webView.setVisibility(View.VISIBLE);
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

            // 2. Remove the custom view container from the root view
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.removeView(mCustomViewContainer);
            mCustomViewContainer = null;
            mCustomView = null;

            // 3. Restore original system UI and orientation
            getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
            setRequestedOrientation(mOriginalOrientation);

            // Notify the video player that full-screen is hidden
            if (mCustomViewCallback != null) {
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;
            }
        }

        // Required for web features like Geolocation
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
    }

// -------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        // 1. Check if a custom video view is visible (i.e., we are in full-screen video mode)
        if (mCustomViewContainer != null) {
            // If yes, exit full-screen mode by calling onHideCustomView()
            // Ensure a valid WebChromeClient is set before casting
            if (webView.getWebChromeClient() instanceof MyWebChromeClient) {
                ((MyWebChromeClient) webView.getWebChromeClient()).onHideCustomView();
            } else {
                super.onBackPressed();
            }
        }
        // 2. Check for WebView history
        else if (webView.canGoBack()) {
            webView.goBack();
        }
        // 3. Default back behavior
        else {
            super.onBackPressed();
        }
    }
}