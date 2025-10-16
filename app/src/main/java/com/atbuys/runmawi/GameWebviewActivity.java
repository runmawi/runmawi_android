package com.atbuys.runmawi;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem; // Import for onOptionsItemSelected
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar; // Import for ActionBar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// IMPORTANT: This is the correct import for WebViewCompat
import androidx.webkit.WebViewCompat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GameWebviewActivity extends AppCompatActivity {

    private WebView myWebView;
    private static final String GAME_URL = "https://10869.play.gamezop.com/";
    // Define a constant for the title
    private static final String ACTION_BAR_TITLE = "Runmawi Games";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Assuming R.layout.activity_game_webview is the correct name for your layout
        setContentView(R.layout.activity_game_webview);

        // --- Action Bar Setup ---
        // Get a reference to the Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 1. Set the Title
            actionBar.setTitle(ACTION_BAR_TITLE);
            // 2. Enable the Up button (back arrow)
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // -------------------------

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize WebView
        // This is the line that was throwing the NullPointerException if R.id.webview was not found.
        myWebView = findViewById(R.id.web_view);

        // **CRITICAL FIX: Check if myWebView is null before proceeding**
        if (myWebView == null) {
            // Handle the error, for example, by showing a Toast and finishing the activity
            Toast.makeText(this, "WebView not found in layout!", Toast.LENGTH_LONG).show();
            // Optional: Log an error or finish the activity to prevent the crash.
            // finish();
            return; // Stop onCreate execution to prevent NullPointerException
        }

        // 2. Configure WebView Settings
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

        // 3. Fix for setRequestHeaderOriginAllowList using WebViewCompat (still commented out)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Ensure you have the 'androidx.webkit:webkit' dependency
//            WebViewCompat.setRequestHeaderOriginAllowList(
//                    myWebView,
//                    Arrays.asList("*")
//            );
//        }

        // 4. Set the custom WebViewClient and load URL
        myWebView.setWebViewClient(new CustomWebViewClient());
        myWebView.loadUrl(GAME_URL);
    }

    // --- Action Bar Back Arrow Handler ---
    /**
     * This method is called whenever an item in your options menu is selected.
     * We use it to handle the press of the Up (back) button.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if the selected item is the Up button (home as up)
        if (item.getItemId() == android.R.id.home) {
            // Perform the same action as a regular back press
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // -------------------------------------

    // Handle back button press for WebView navigation
    @Override
    public void onBackPressed() {
        if (myWebView != null && myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            // The default behavior when the WebView can't go back is to finish the activity
            super.onBackPressed();
        }
    }

    /**
     * Custom WebViewClient to handle URL loading.
     * Links to gamezop.com stay in the WebView, all others open externally.
     */
    public class CustomWebViewClient extends WebViewClient {

        private static final String GAMEZOP_URL = "gamezop.com";
        private static final String NO_APPLICATION_ERROR = "You do not have an application to run this.";

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
                // If it's a GameZop URL, load it inside this WebView
                view.loadUrl(url);
            } else {
                // Otherwise, try to load it outside the WebView
                loadOutsideWebView(view, url);
            }
            return true;
        }

        private void loadOutsideWebView(WebView view, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            PackageManager packageManager = view.getContext().getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            if (!activities.isEmpty()) {
                // If an application can handle the intent, start the activity
                view.getContext().startActivity(intent);
            } else {
                // No application found
                Toast.makeText(view.getContext(), NO_APPLICATION_ERROR, Toast.LENGTH_LONG).show();
            }
        }
    }
}