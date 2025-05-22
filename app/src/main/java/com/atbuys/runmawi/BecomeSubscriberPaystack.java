package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Arrays;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BecomeSubscriberPaystack extends AppCompatActivity{

    private EditText mCardNumber;
    private EditText mCardExpiry;
    private EditText mCardCVV;
    TextView amountText;
    ImageView backarrow;
    String user_id,planid;
    Addpayperview addpayperview;
    Button button;
    Intent in;
    int amount;
    ProgressBar payment_progress;
    String username,email,password,pic,plan,ccode,mobile,ref_code,token,type,transaction_ref,subscription_code;
    registerresponse regrespone1;
    private String public_key,access_token;
    int formerLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        in=getIntent();
        username =in.getStringExtra("username");
        email =in.getStringExtra("email");
        password =in.getStringExtra("password");
        ccode =in.getStringExtra("ccode");
        mobile =in.getStringExtra("mobile");
        ref_code =in.getStringExtra("ref_code");
        token = in.getStringExtra("token");
        type=in.getStringExtra("type");
        amount =Integer.parseInt(in.getStringExtra("amount"));
        //amount *= 100;
        backarrow=findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setKey();
        //initializePaystack();
        //initializeFormVariables();
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
                          access_token=payment_settingslist.get(i).getPaystack_test_secret_key();
                      } else {
                          public_key=payment_settingslist.get(i).getLive_publishable_key();
                          access_token=payment_settingslist.get(i).getLive_secret_key();
                      }
                      //Toast.makeText(BecomeSubscriberPaystack.this, "key-> "+public_key, Toast.LENGTH_SHORT).show();
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
        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey(public_key);
    }

    private void initializeFormVariables() {
        mCardNumber = findViewById(R.id.credit_card_number);
        mCardExpiry = findViewById(R.id.credit_card_expiry);
        mCardCVV = findViewById(R.id.credit_card_ccv);
        payment_progress=findViewById(R.id.progress_bar);
        amountText=findViewById(R.id.amount);

        // this is used to add a forward slash (/) between the cards expiry month
        // and year (11/21). After the month is entered, a forward slash is added
        // before the year

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

        planid=in.getStringExtra("plan");

        //Toast.makeText(BecomeSubscriberPaystack.this, " "+planid+" "+in.getStringExtra("plan"), Toast.LENGTH_SHORT).show();


        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
        if (card.isValid()) {

        } else {

            Toast.makeText(BecomeSubscriberPaystack.this, "please provide valid card details", Toast.LENGTH_SHORT).show();
            button.setVisibility(View.VISIBLE);
            payment_progress.setVisibility(View.GONE);
        }

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);

        amount *= 100;
        Charge charge = new Charge();
        charge.setAmount(amount);
        charge.setEmail(email);
        charge.setCard(card);

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {

                transaction_ref=transaction.getReference();
                subscribere();
                //parseResponse(transaction.getReference());
                //Toast.makeText(BecomeSubscriberPaystack.this, transaction.getReference(), Toast.LENGTH_LONG).show();
                //Toast.makeText(BecomeSubscriberPaystack.this, "pl-> "+transaction.getReference()+" u-> "+username+" e-> "+email+" p-> "+password+" plan-> "+planid+" m-> "+mobile+" ref-> "+String.valueOf(amount)+" ", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void beforeValidate(Transaction transaction) {
                //Toast.makeText(BecomeSubscriberPaystack.this, "beforeValidate: " + transaction.getReference(), Toast.LENGTH_SHORT).show();
                Log.d("Main Activity", "beforeValidate: " + transaction.getReference());
            }
            @Override
            public void onError(Throwable error, Transaction transaction) {
                Log.d("Main Activity", "onError: " + error.getLocalizedMessage());
                Log.d("Main Activity", "onError: " + error);
                Toast.makeText(BecomeSubscriberPaystack.this, "Card issue: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                button.setVisibility(View.VISIBLE);
                payment_progress.setVisibility(View.GONE);
            }

        });
    }

    private void subscribere() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.paystack.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        CustomerId customerId=new CustomerId(email);
        Call<CustomerId> call = request.getCustomerId("Bearer "+access_token,customerId);
        call.enqueue(new retrofit2.Callback<CustomerId>() {
            @Override
            public void onResponse(Call<CustomerId> call, retrofit2.Response<CustomerId> response) {

                data data = response.body().getData();
                CustomerId customerId = new CustomerId(data.getCustomer_code(), planid);

                Call<CustomerId> sub = request.Subscription("Bearer "+access_token,customerId);
                sub.enqueue(new retrofit2.Callback<CustomerId>() {
                    @Override
                    public void onResponse(Call<CustomerId> call, retrofit2.Response<CustomerId> response) {

                        data data = response.body().getData();
                        subscription_code=data.getSubscription_code();
                        //CustomerId customerId = new CustomerId(email,String.valueOf(amount),planid);
                        //Toast.makeText(BecomeSubscriberPaystack.this, "code-> "+subscription_code, Toast.LENGTH_LONG).show();
                        parseResponse(subscription_code);

                       /* Call<CustomerId> ini = request.getReference("Bearer "+"sk_test_bc1e514f7430191bd2d7aa2601c54cf183c031f3",customerId);
                        ini.enqueue(new retrofit2.Callback<CustomerId>() {
                            @Override
                            public void onResponse(Call<CustomerId> call, retrofit2.Response<CustomerId> response) {

                                //data data = response.body().getData();
                                parseResponse(transaction_ref);
                            }

                            @Override
                            public void onFailure(Call<CustomerId> call, Throwable t) {

                            }

                        });*/

                    }

                    @Override
                    public void onFailure(Call<CustomerId> call, Throwable t) {

                    }

                });

            }
            @Override
            public void onFailure(Call<CustomerId> call, Throwable t) {
                Toast.makeText(BecomeSubscriberPaystack.this, "error: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        }

    private void parseResponse(String transactionReference) {

        if (type.equalsIgnoreCase("pay")){
            Api.getClient().getPaystackSignup(username, email, password,  transactionReference, planid,ccode,mobile, "0",ref_code,"default", String.valueOf(amount),"recurring","Paystack","" ,"Android",new Callback<registerresponse>() {
                @Override
                public void success(registerresponse regresponse, retrofit.client.Response response) {
                    regrespone1 = regresponse;

                    if (regresponse.getStatus().equalsIgnoreCase("true")) {

                        Toast.makeText(getApplicationContext(),"Your Payment Is Complete. You Can Login Using Your Credentials", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(BecomeSubscriberPaystack.this, SigninActivity.class);
                        startActivity(in);
                        button.setVisibility(View.VISIBLE);
                        payment_progress.setVisibility(View.GONE);

                    } else if (regresponse.getStatus().equalsIgnoreCase("false")) {
                        if (regresponse.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                            Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                            button.setVisibility(View.VISIBLE);
                            payment_progress.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(getApplicationContext(), "Error: "+regrespone1.getMessage(), Toast.LENGTH_LONG).show();
                            button.setVisibility(View.VISIBLE);
                            payment_progress.setVisibility(View.GONE);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
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
        }else if (type.equalsIgnoreCase("free")){

            final Call<JSONResponse> bannerreq = ApiClient.getInstance1().getApi().getBecomSubcriberPaystack(user_id,transactionReference,"Android");
            bannerreq.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();

                    if (jsonResponse.getStatus().equalsIgnoreCase("true")){
                        Toast.makeText(getApplicationContext(),"Your Payment Is Completed", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                        editor.putBoolean(sharedpreferences.login, true);
                        editor.putString(sharedpreferences.user_id, user_id);
                        editor.putString(sharedpreferences.role, "subscriber");
                        editor.putString(sharedpreferences.email, email);
                        editor.putString(sharedpreferences.username, username);
                        editor.apply();
                        Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                        startActivity(in);
                    }else {
                        Toast.makeText(getApplicationContext(),"Error: "+jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });

        }


    }
}