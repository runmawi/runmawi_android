package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebView extends AppCompatActivity {

    protected android.webkit.WebView mWebView;
    String url,id,user_id,type,username,usermail,password,ccode,mobile;
    registerresponse regrespone1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        mWebView=findViewById(R.id.webView);

        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        id=intent.getStringExtra("id");
        type=intent.getStringExtra("type");
        username=intent.getStringExtra("username");
        usermail=intent.getStringExtra("email");
        password=intent.getStringExtra("password");
        ccode=intent.getStringExtra("ccode");
        mobile=intent.getStringExtra("mobile");

        mWebView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        //mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);

    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            // Check if the URL contains a success indicator
            if (url.contains("mode=member")) {
                // Payment success, handle accordingly
                //handlePaymentSuccess();

                if (type.equalsIgnoreCase("free")){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            Call<JSONResponse> subapi=ApiClient.getInstance1().getApi().getPayPalSubscription(user_id,id,"Android");
                            subapi.enqueue(new Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                                    if (response.isSuccessful()){
                                        JSONResponse jsonResponse=response.body();
                                        if (jsonResponse.getStatus().equalsIgnoreCase("true")){
                                            Toast.makeText(WebView.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.user_id, user_id);
                                            editor.putString(sharedpreferences.role, "subscriber");
                                            editor.apply();
                                            Intent in = new Intent(WebView.this, HomePageActivitywithFragments.class);
                                            startActivity(in);
                                        }else {
                                            Toast.makeText(WebView.this, "issue: "+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                    Toast.makeText(WebView.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 3000);
                } else if (type.equalsIgnoreCase("pay")) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            Api.getClient().getPaypalSignup(username, usermail, password,  id,ccode,mobile,"recurring","PayPal","Android" ,new retrofit.Callback<registerresponse>() {
                                @Override
                                public void success(registerresponse regresponse, retrofit.client.Response response) {
                                    regrespone1 = regresponse;

                                    if (regresponse.getStatus().equalsIgnoreCase("true")) {

                                        Toast.makeText(getApplicationContext(),"Your Payment Is Complete. You Can Login Using Your Credentials", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(WebView.this, SigninActivity.class);
                                        startActivity(in);

                                    } else if (regresponse.getStatus().equalsIgnoreCase("false")) {
                                        if (regresponse.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                            Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), "issue: "+regrespone1.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }, 3000);
                }

                return true;
            }

            // Allow the WebView to load other URLs
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            // Show loading indicator or perform other actions as needed
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            // Hide loading indicator or perform other actions as needed
        }
    }

}
