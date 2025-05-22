package com.atbuys.runmawi;

import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class CheckoutActivity extends AppCompatActivity {

    private EditText mCardNumber;
    private EditText mCardExpiry;
    private EditText mCardCVV;
    TextView amountText;
    ImageView backarrow;
    String user_id,id,urlll,type,email,season_id,series_id;
    int amount;
    Intent intent;
    Addpayperview addpayperview;
    Button button;
    ProgressBar payment_progress;
    private String public_key;
    int formerLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        intent=getIntent();
        amount = Integer.parseInt(intent.getStringExtra("price"));
        id = intent.getStringExtra("id");
        series_id = intent.getStringExtra("series_id");
        season_id = intent.getStringExtra("season_id");
        urlll = intent.getStringExtra("url");
        type=intent.getStringExtra("type");
        backarrow=findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setKey();
    }
    private void setKey() {
        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));

                for (int i=0;i<payment_settingslist.size();i++){
                    if (payment_settingslist.get(i).getPayment_type().equalsIgnoreCase("Paystack")){

                        if( payment_settingslist.get(i).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                            public_key=payment_settingslist.get(i).getPaystack_test_publishable_key();
                        } else {
                            public_key=payment_settingslist.get(i).getLive_publishable_key();
                        }
                        initializePaystack();
                        initializeFormVariables();
                    }
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }
    private void initializePaystack() {
        PaystackSdk.setPublicKey(public_key);
    }


    private void initializeFormVariables() {
        mCardNumber = findViewById(R.id.credit_card_number);
        mCardExpiry = findViewById(R.id.credit_card_expiry);
        mCardCVV = findViewById(R.id.credit_card_ccv);
        payment_progress=findViewById(R.id.progress_bar);
        amountText=findViewById(R.id.amount);

        mCardExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                formerLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > formerLength){ //User is adding text
                    if(editable.length() == 2){
                        editable.append("/");
                    }
                } else {
                    if(editable.length() == 2){
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }

            }
        });

        button = findViewById(R.id.pay_button);
        amountText.setText("₦"+amount);
        button.setOnClickListener(v -> performCharge());
    }


    private void performCharge() {

        button.setVisibility(View.GONE);
        payment_progress.setVisibility(View.VISIBLE);

        String cardNumber = mCardNumber.getText().toString();
        String cardExpiry = mCardExpiry.getText().toString();
        String cvv = mCardCVV.getText().toString();

        String[] cardExpiryArray = cardExpiry.split("/");
        int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
        int expiryYear = Integer.parseInt(cardExpiryArray[1]);




        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
        if (card.isValid()) {

        } else {

            Toast.makeText(CheckoutActivity.this, "please provide valid card details", Toast.LENGTH_SHORT).show();
            button.setVisibility(View.VISIBLE);
            payment_progress.setVisibility(View.GONE);
        }

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        email = prefs.getString(sharedpreferences.email, null);

        amount *= 100;
        Charge charge = new Charge();
        charge.setAmount(amount);
        charge.setEmail(email);
        charge.setCard(card);


        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {

                parseResponse(transaction.getReference());
                //Toast.makeText(CheckoutActivity.this, "pl-> "+transaction.getReference(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                //Toast.makeText(CheckoutActivity.this, "beforeValidate: " + transaction.getReference(), Toast.LENGTH_SHORT).show();
                Log.d("Main Activity", "beforeValidate: " + transaction.getReference());
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                Log.d("Main Activity", "onError: " + error.getLocalizedMessage());
                Log.d("Main Activity", "onError: " + error);
                Toast.makeText(CheckoutActivity.this, "Card issue: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                button.setVisibility(View.VISIBLE);
                payment_progress.setVisibility(View.GONE);

            }

        });
    }

    private void parseResponse(String transactionReference) {

        if (type.equalsIgnoreCase("video_rent")) {
        Api.getClient().getPaystackppv(user_id, id, transactionReference,"Android", new Callback<Addpayperview>() {
            @Override
            public void success(Addpayperview addpayperview1, Response response) {


                addpayperview = addpayperview1;
                if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                    Intent in = new Intent(getApplicationContext(), OnlinePlayerActivity.class);
                    in.putExtra("id", id);
                    in.putExtra("url", urlll);
                    in.putExtra("suburl", "");
                    in.putExtra("data", "videos");
                    in.putExtra("ads", "");
                    in.putExtra("xtra", "");
                    startActivity(in);

                    button.setVisibility(View.VISIBLE);
                    payment_progress.setVisibility(View.GONE);
                } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                    payment_progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                button.setVisibility(View.VISIBLE);
                payment_progress.setVisibility(View.GONE);
            }
        });
    } else if (type.equalsIgnoreCase("live_rent")) {
            Api.getClient().getPaystackppvLive(user_id, id, transactionReference,"Android", new Callback<Addpayperview>() {
                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Intent in = new Intent(getApplicationContext(), LiveOnlinePlayerActivity.class);
                        in.putExtra("id", id);
                        in.putExtra("url", urlll);
                        in.putExtra("suburl", "");
                        in.putExtra("data", "");
                        in.putExtra("ads", "");
                        startActivity(in);

                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);
                    }else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                    payment_progress.setVisibility(View.GONE);
                }
            });

        }else if (type.equalsIgnoreCase("season_rent")) {

            Api.getClient().getPaystackppvSeason(user_id,series_id,season_id, transactionReference,"Android", new Callback<Addpayperview>() {
                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Intent in = new Intent(getApplicationContext(), OnlinePlayer11Activity.class);
                        in.putExtra("id", id);
                        in.putExtra("url", urlll);
                        in.putExtra("suburl", "");
                        in.putExtra("data", "");
                        in.putExtra("ads", "");
                        startActivity(in);

                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);
                    }else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(),addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                    payment_progress.setVisibility(View.GONE);
                }
            });

        } else if (type.equalsIgnoreCase("audio_rent")) {
            Api.getClient().getPaystackppvAudio(user_id,id, transactionReference,"Android", new Callback<Addpayperview>() {
                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, id);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();

                                if (jsonResponse.getAudiodetail().length == 0 || jsonResponse.getAudiodetail() == null) {
                                    Toast.makeText(CheckoutActivity.this, "Audio Not Available ", Toast.LENGTH_LONG).show();
                                } else {
                                    ArrayList<audiodetail> audiodetail = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                    editor.putBoolean(sharedpreferences.login, true);
                                    editor.putString(sharedpreferences.audioid, audiodetail.get(0).getId());
                                    editor.apply();
                                    editor.commit();

                                    Intent in = new Intent(CheckoutActivity.this, MediaPlayerPageActivity.class);
                                    in.putExtra("id", audiodetail.get(0).getId());
                                    startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    try {
                                        mediaplayer.setDataSource(audiodetail.get(0).getMp3_url());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mediaplayer.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mediaplayer.start();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);
                    }else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(),addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                    payment_progress.setVisibility(View.GONE);
                }
            });

        }


    }
}