package com.atbuys.runmawi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button; // Import for Button
import android.widget.LinearLayout; // Import for LinearLayout
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.webkit.WebViewCompat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GameWebviewActivity extends AppCompatActivity {

    private WebView myWebView;
    private ProgressBar progressBar;
    private LinearLayout noInternetLayout; // Declare no internet layout
    private Button retryButton; // Declare retry button

    private static final String GAME_URL = "https://10869.play.gamezop.com/";
    private static final String ACTION_BAR_TITLE = "Runmawi Games";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_webview);

        // --- Action Bar Setup (Existing code) ---
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(ACTION_BAR_TITLE);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize Views
        myWebView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);
        noInternetLayout = findViewById(R.id.no_internet_layout); // Initialize layout
        retryButton = findViewById(R.id.btn_retry); // Initialize button

        if (myWebView == null) {
            Toast.makeText(this, "WebView not found in layout!", Toast.LENGTH_LONG).show();
            return;
        }

        // Set up retry button listener
        retryButton.setOnClickListener(v -> {
            loadGameUrl(); // Attempt to reload the URL
        });

        // Initial check and load
        loadGameUrl();
    }

    /**
     * Handles the logic for checking network and loading the WebView.
     */
    private void loadGameUrl() {
        if (!isNetworkAvailable()) {
            // No internet: Hide progress/web and show the error layout
            progressBar.setVisibility(View.GONE);
            myWebView.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            // Internet is available: Hide error layout and show progress
            noInternetLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            myWebView.setVisibility(View.GONE); // Ensure WebView is hidden until load finish

            // 2. Configure WebView Settings (Existing code)
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setGeolocationEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setSupportZoom(false);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

            // Other WebView configurations
            myWebView.setSoundEffectsEnabled(true);
            myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            myWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
            CookieManager.getInstance().setAcceptCookie(true);

            // 4. Set the custom WebViewClient and load URL
            myWebView.setWebViewClient(new CustomWebViewClient());
            myWebView.loadUrl(GAME_URL);
        }
    }

    /**
     * Checks if the device has an active network connection.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    // --- Action Bar Back Arrow Handler (Existing code) ---
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // -------------------------------------

    // Handle back button press for WebView navigation (Existing code)
    @Override
    public void onBackPressed() {
        if (myWebView != null && myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Custom WebViewClient to handle URL loading, progress bar, and errors.
     */
    public class CustomWebViewClient extends WebViewClient {

        private static final String GAMEZOP_URL = "gamezop.com";
        private static final String NO_APPLICATION_ERROR = "You do not have an application to run this.";

        // This method is called when the page finishes loading
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Hide the progress bar and show the WebView when loading is complete
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            view.setVisibility(View.VISIBLE);
            noInternetLayout.setVisibility(View.GONE); // Hide error view if it was showing
        }

        // This method is called when an error occurs during page loading
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            view.setVisibility(View.GONE); // Hide the broken WebView
            noInternetLayout.setVisibility(View.VISIBLE); // Show retry option

            Toast.makeText(GameWebviewActivity.this, "Error loading page: " + description, Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String domain = "";
            try {
                URL fullUrl = new URL(url);
                domain = fullUrl.getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (domain.contains(GAMEZOP_URL)) {
                view.loadUrl(url);
            } else {
                loadOutsideWebView(view, url);
            }
            return true;
        }

        private void loadOutsideWebView(WebView view, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            PackageManager packageManager = view.getContext().getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            if (!activities.isEmpty()) {
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), NO_APPLICATION_ERROR, Toast.LENGTH_LONG).show();
            }
        }
    }
}