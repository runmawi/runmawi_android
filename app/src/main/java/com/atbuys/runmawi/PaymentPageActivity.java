package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.android.view.StripeEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentPageActivity extends AppCompatActivity {

    private Stripe stripe;
    PaymentMethodCreateParams params;
    Button paynow;
    CardMultilineWidget cardMultilineWidget;
    String username,email,password,pic,plan,ccode,mobile,ref_code,price;
    registerresponse regrespone1;
    CheckBox check;
    String skip="0",py_id,token;
    RequestBody requestFile;
    MultipartBody.Part body2;
    ImageView imgpayment;
    ProgressDialog progressDialog;
    TextView terms,plan_name,plan_price;
    private ArrayList<payment_settings> payment_settingslist;
    String key,currency;
    Button apply;
    ProgressBar apply_progress;
    LinearLayout promo_layout;
    TextView payable_amount, deducted_amount;
    StripeEditText promo_code;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        cardMultilineWidget = findViewById(R.id.card_input_widget);
        terms = (TextView) findViewById(R.id.terms);
        plan_name = (TextView) findViewById(R.id.plan_name);
        plan_price = (TextView) findViewById(R.id.plan_price);
        payable_amount = (TextView) findViewById(R.id.payable_amount);
        deducted_amount = (TextView) findViewById(R.id.deducted_amount);
        promo_code = (StripeEditText) findViewById(R.id.promo_code);
        promo_layout = (LinearLayout) findViewById(R.id.promo_layout);
        apply = (Button) findViewById(R.id.apply);
        apply_progress = (ProgressBar) findViewById(R.id.apply_progress);
        progressDialog = new ProgressDialog(this);
        payment_settingslist =new ArrayList<payment_settings>();



        Intent in=getIntent();
        username =in.getStringExtra("username");
        email =in.getStringExtra("email");
        password =in.getStringExtra("password");
        plan =in.getStringExtra("plan");
        price = in.getStringExtra("price");
        ccode =in.getStringExtra("ccode");
        mobile =in.getStringExtra("mobile");
        ref_code =in.getStringExtra("ref_code");
        token = in.getStringExtra("token");

        paynow =(Button)findViewById(R.id.save_payment) ;
        check =(CheckBox) findViewById(R.id.check);
        imgpayment=(ImageView)findViewById(R.id.imgpayment12);

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

                if( payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0)))
                {

                    key=payment_settingslist.get(0).getTest_publishable_key();
                }
                else
                {
                    key=payment_settingslist.get(0).getLive_publishable_key();

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://runmawi.com/page/terms-and-conditions"));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "unable to open website", Toast.LENGTH_SHORT).show();
                }

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!promo_code.getText().toString().isEmpty()) {

                    Call<JSONResponse> code_api = ApiClient.getInstance1().getApi().verifyCoupon(promo_code.getText().toString(), price);
                    code_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                deducted_amount.setText(jsonResponse.getPromo_code_amt());
                                promo_layout.setVisibility(View.VISIBLE);
                                payable_amount.setText(jsonResponse.getDiscount_amt());
                                Toast.makeText(getApplicationContext(), "Promo code applied successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                promo_code.setError(jsonResponse.getMessage());
                                promo_layout.setVisibility(View.GONE);
                                payable_amount.setText(price);
                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                        }
                    });
                } else {
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


                if (!check.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Accept our Terms of Use and Privacy Policy", Toast.LENGTH_LONG).show();
                } else {

                    progressDialog.setMessage("Please Wait");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    params = cardMultilineWidget.getPaymentMethodCreateParams();

                    if (params == null) {
                        return;
                    }
                    stripe = new Stripe(getApplicationContext(), key);

                    stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                        @Override
                        public void onSuccess(@NotNull PaymentMethod paymentMethod) {

                            py_id = paymentMethod.id;


                            Api.getClient().getSignup1(username, email, password,  py_id, plan,ccode,mobile, skip,ref_code,"default", "","recurring","","" ,promo_code.getText().toString(),new Callback<registerresponse>() {
                                @Override
                                public void success(registerresponse regresponse, retrofit.client.Response response) {
                                    regrespone1 = regresponse;
                                    progressDialog.hide();

                                    if (regresponse.getStatus().equalsIgnoreCase("true")) {

                                        Toast.makeText(getApplicationContext(),"Your Payment Is Complete. You Can Login Using Your Credentials", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                        startActivity(in);

                                    } else if (regresponse.getStatus().equalsIgnoreCase("false")) {
                                        if (regresponse.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                            Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                                    progressDialog.hide();
                                }
                            });

                        }



                        @Override
                        public void onError(@NotNull Exception e) {
                            Toast.makeText(getApplicationContext(), "false"+e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });


    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback());
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


    private static final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Log.d("", "Payment completed");
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                Log.d("", "Payment failed");
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.e("", "Error" +  e.toString());
        }
    }
}
