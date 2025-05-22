package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.view.StripeEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BecomeSubscriberPaymentActivity extends AppCompatActivity {

    private Stripe stripe;
    PaymentMethodCreateParams params;
    Button paynow;
    ProgressBar payment_progress;
    CardMultilineWidget cardMultilineWidget;
    String username, email, password, country, state, city, plan,price;
    registerresponse regrespone1;
    String py_id;
    String user_id, user_role, theme;
    private ArrayList<user_details> user_detailsdata;
    CheckBox check;
    ProgressDialog progressDialog;
    TextView term;
    private ArrayList<payment_settings> payment_settingslist;
    String key,currency;
    Button apply;
    ProgressBar apply_progress;
    LinearLayout promo_layout;
    TextView payable_amount,deducted_amount;
    StripeEditText promo_code;
    TextView terms,plan_name,plan_price;
    ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.darktheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        theme = prefs.getString(sharedpreferences.theme, null);

        if (theme.equalsIgnoreCase("1")) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.darktheme);
        }
        Intent in = getIntent();
        plan = in.getStringExtra("plan");
        price = in.getStringExtra("price");

        progressDialog = new ProgressDialog(this);
        payable_amount=(TextView)findViewById(R.id.payable_amount);
        deducted_amount=(TextView)findViewById(R.id.deducted_amount);
        promo_code=(StripeEditText)findViewById(R.id.promo_code);
        promo_layout=(LinearLayout)findViewById(R.id.promo_layout);
        apply=(Button)findViewById(R.id.apply);
        apply_progress=(ProgressBar)findViewById(R.id.apply_progress);
        terms = (TextView) findViewById(R.id.terms);
        plan_name = (TextView) findViewById(R.id.plan_name);
        plan_price = (TextView) findViewById(R.id.plan_price);
        cardMultilineWidget = findViewById(R.id.card_input_widget);
        check = (CheckBox) findViewById(R.id.check);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> callca = request.getStripeOnetime();
        callca.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                currency = response.body().getCurrency_Symbol();
                plan_price.setText(price+" "+currency);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });

        plan_name.setText(in.getStringExtra("plan_name"));
        payable_amount.setText(price);


        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));

                if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                    key = payment_settingslist.get(0).getTest_publishable_key();
                    //Toast.makeText(BecomeSubscriberPaymentActivity.this, "test: "+key, Toast.LENGTH_SHORT).show();
                } else {
                    key = payment_settingslist.get(0).getLive_publishable_key();
                    //Toast.makeText(BecomeSubscriberPaymentActivity.this, "live: "+key, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        paynow = (Button) findViewById(R.id.save_payment);
        payment_progress = (ProgressBar) findViewById(R.id.payment_progress);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://runmawi.com/page/terms-and-conditions"));
                    startActivity(i);
                } catch (Exception e) {
                }

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!promo_code.getText().toString().isEmpty()){

                    Call<JSONResponse> code_api=ApiClient.getInstance1().getApi().verifyCoupon(promo_code.getText().toString(),price);
                    code_api.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                            JSONResponse jsonResponse= response.body();
                            if (jsonResponse.getStatus().equalsIgnoreCase("true")){
                                deducted_amount.setText(jsonResponse.getPromo_code_amt());
                                promo_layout.setVisibility(View.VISIBLE);
                                payable_amount.setText(jsonResponse.getDiscount_amt());
                                Toast.makeText(getApplicationContext(), "Promo code applied successfully!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                promo_code.setError(jsonResponse.getMessage());
                                promo_layout.setVisibility(View.GONE);
                                payable_amount.setText(price);
                            }
                        }
                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                        }
                    });
                }else {
                    //Toast.makeText(getApplicationContext(), "Invalid promo code. Please try again.", Toast.LENGTH_SHORT).show();
                    promo_code.setError("Invalid promo code. Please try again.");
                    promo_layout.setVisibility(View.GONE);
                    payable_amount.setText(price);
                }
            }
        });

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paynow.setVisibility(View.GONE);
                payment_progress.setVisibility(View.VISIBLE);

                if (!check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Accept our Terms of Use and Privacy Policy", Toast.LENGTH_LONG).show();
                    paynow.setVisibility(View.VISIBLE);
                    payment_progress.setVisibility(View.GONE);
                } else {
                    params = cardMultilineWidget.getPaymentMethodCreateParams();

                    if (params == null) {
                        return;
                    }

                    stripe = new Stripe(getApplicationContext(), key);

                    stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {

                        @Override
                        public void onError(@NotNull Exception e) {
                            paynow.setVisibility(View.VISIBLE);
                            payment_progress.setVisibility(View.GONE);
                            Toast.makeText(BecomeSubscriberPaymentActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(@NotNull PaymentMethod paymentMethod) {

                            progressDialog.setMessage("Please Wait");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.setMax(100);
                            progressDialog.show();

                            py_id = paymentMethod.id;

                            progressDialog.hide();
                            Log.d("sdfwewdf", key + py_id);
                            //Toast.makeText(BecomeSubscriberPaymentActivity.this, " py-> "+py_id, Toast.LENGTH_LONG).show();

                            final Call<JSONResponse> bannerreq = ApiClient.getInstance1().getApi().getStripeBecomeSubcriber(user_id, py_id, plan,promo_code.getText().toString(),"Android");
                            bannerreq.enqueue(new retrofit2.Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                    JSONResponse jsonResponse = response.body();
                                    if (jsonResponse.getStatus().equalsIgnoreCase("true")){
                                        Toast.makeText(BecomeSubscriberPaymentActivity.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                        editor.putBoolean(sharedpreferences.login, true);
                                        editor.putString(sharedpreferences.user_id, user_id);
                                        editor.putString(sharedpreferences.role, jsonResponse.getUsers_role());
                                        editor.apply();
                                        editor.commit();
                                        Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                        startActivity(in);
                                    }else {
                                        paynow.setVisibility(View.VISIBLE);
                                        payment_progress.setVisibility(View.GONE);
                                        //paynow.setText(jsonResponse.getMessage());
                                        Toast.makeText(BecomeSubscriberPaymentActivity.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                    Log.d("Error", t.getMessage());
                                    paynow.setVisibility(View.VISIBLE);
                                    payment_progress.setVisibility(View.GONE);
                                    Toast.makeText(BecomeSubscriberPaymentActivity.this, "Error: "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                    });
                }
            }
        });

    }

    public void terms(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setTitle("Title here");

        WebView wv = new WebView(getApplicationContext());
        wv.loadUrl("https://runmawi.com/page/terms-and-conditions");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}
