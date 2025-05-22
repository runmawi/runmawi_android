package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.cinetpay.androidsdk.CinetPayActivity;

public class MyCinetPayActivity extends AppCompatActivity {

    String api_key, site_id, transaction_id, currency, description, channels, amount;
    String plan, type, username, useremail, password, id, url, ccode,season_id,series_id;
    registerresponse regrespone1;
    Addpayperview addpayperview;
    Context context;
    protected WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinetpay);

        //Intent intent = getIntent();
        id = getIntent().getStringExtra("id");
        series_id = getIntent().getStringExtra("series_id");
        season_id = getIntent().getStringExtra("season_id");
        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        plan = getIntent().getStringExtra("plan");
        username = getIntent().getStringExtra("username");
        ccode = getIntent().getStringExtra("ccode");
        useremail = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        api_key = getIntent().getStringExtra(CinetPayActivity.KEY_API_KEY);
        site_id = getIntent().getStringExtra(CinetPayActivity.KEY_SITE_ID);
        transaction_id = getIntent().getStringExtra(CinetPayActivity.KEY_TRANSACTION_ID);
        amount = getIntent().getStringExtra(CinetPayActivity.KEY_AMOUNT);
        currency = getIntent().getStringExtra(CinetPayActivity.KEY_CURRENCY);
        description = getIntent().getStringExtra(CinetPayActivity.KEY_DESCRIPTION);
        channels = getIntent().getStringExtra(CinetPayActivity.KEY_CHANNELS);
        //Toast.makeText(MyCinetPayActivity.this, "cinetPay-> " +id+" "+ url + " " , Toast.LENGTH_SHORT).show();

        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        } else {
            cookieManager.setAcceptCookie(true);
        }
        mWebView.loadUrl("file:///android_asset/cinetpay.html");

        mWebView.addJavascriptInterface(
                        new MyCinetPayWebAppInterface(
                                MyCinetPayActivity.this,
                                api_key,        // obligatoire
                                site_id,        // obligatoire
                                transaction_id, // obligatoire
                                amount,         // obligatoire
                                currency,       // obligatoire
                                description,     // obligatoire
                                id, url, type, username, useremail, password, plan, ccode,series_id,season_id
                        )
                                .setChannels(channels)
                                // obligatoire pour les paiements par carte
                                .setCustomerName("Test")
                                // obligatoire pour les paiements par carte
                                .setCustomerSurname("Plan")
                                // obligatoire pour les paiements par carte
                                .setCustomerEmail("example@gmail.com")
                                // obligatoire pour les paiements par carte
                                .setCustomerAddress("east street")
                                // obligatoire pour les paiements par carte
                                .setCustomerPhoneNumber("1234567890")
                                // obligatoire pour les paiements par carte
                                .setCustomerCity("Chennai")
                                // obligatoire pour les paiements par carte (code ISO 2 chiffres: CI, BF, US...)
                                .setCustomerCountry("CI")
                                // obligatoire pour les paiements par carte
                                .setCustomerZipCode("123456")
                                // obligqtoire si le pays est US (Etats-Unis) ou CA (Canada)
                                .setCustomerState("A_STATE"), "Android");


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
