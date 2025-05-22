package com.atbuys.runmawi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPverifylogin_Activity extends AppCompatActivity {

    String id,acc_status,mobile,email,tkn,ccode;
    ImageView back_arrow;
    EditText otp1,otp2,otp3,otp4;
    TextView via,ResendCode,status, tvTimer, tvResendCode1, tvResendCode2;
    CardView verify;
    LinearLayout resend,tvResendCode;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverifylogin);

        Intent intent=getIntent();
        acc_status=intent.getStringExtra("acc_status");
        id=intent.getStringExtra("id");
        mobile=intent.getStringExtra("mobile");
        email=intent.getStringExtra("email");
        ccode=intent.getStringExtra("ccode");

        back_arrow=(ImageView) findViewById(R.id.back_arrow);
        via=(TextView)findViewById(R.id.via);
        tvTimer = findViewById(R.id.tvTimer); // Countdown TextView
        tvResendCode = findViewById(R.id.tvResendCode);
        tvResendCode1 = findViewById(R.id.tvResendCode1);
        tvResendCode2 = findViewById(R.id.tvResendCode2);
        resend=(LinearLayout)findViewById(R.id.resend);
        verify=(CardView)findViewById(R.id.verify);
        otp1=(EditText) findViewById(R.id.otp1);
        otp2=(EditText) findViewById(R.id.otp2);
        otp3=(EditText) findViewById(R.id.otp3);
        otp4=(EditText) findViewById(R.id.otp4);

        startResendCountdown();


        String[] digits = mobile.split("");
        via.setText(digits[0]+digits[1]+"*******"+digits[digits.length-1]);
        otp1.requestFocus();
    //    tkn = FirebaseInstanceId.getInstance().getToken();
        progressDialog=new ProgressDialog(OTPverifylogin_Activity.this);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvResendCode2.setOnClickListener(v -> {
            // Call the API to resend the OTP
            startResendCountdown(); // Restart the countdown after resending
            resend.setVisibility(View.GONE); // Hide the resend view
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp1.getText().toString()==null||otp2.getText().toString()==null||otp3.getText().toString()==null||otp4.getText().toString()==null||otp1.getText().toString().isEmpty()||otp2.getText().toString().isEmpty()||otp3.getText().toString().isEmpty()||otp4.getText().toString().isEmpty()) {
                    Toast.makeText(OTPverifylogin_Activity.this, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                } else{
                    progressDialog.setMessage("OTP Verification...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.show();
                    String verificationcode=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();
                    Log.w("runmawi","m: "+mobile+" cc: "+ccode+" id: "+id+" ver: "+verificationcode);

                    Call<JSONResponse> v_api = ApiClient.getInstance1().getApi().verifyOTP(mobile, ccode, id, verificationcode);
                    v_api.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse != null && jsonResponse.getOtp_status().equalsIgnoreCase("true")) {
                                if (acc_status.equalsIgnoreCase("mobile_number_exists")) {
                                    Call<JSONResponse> login_api = ApiClient.getInstance1().getApi().getSMSLogin(mobile, verificationcode, verificationcode);
                                    login_api.enqueue(new Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                                            progressDialog.hide();
                                            JSONResponse jsonResponse1 = response.body();
                                            if (jsonResponse1 != null && jsonResponse1.getStatus().equalsIgnoreCase("true")) {
                                                ArrayList<user_details> userdata = new ArrayList<>(Arrays.asList(jsonResponse1.getUser_details()));
                                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                editor.putBoolean(sharedpreferences.login, true);
                                                editor.putString(sharedpreferences.user_id, userdata.get(0).getUser_id());
                                                editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                                                editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                                                editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                                                editor.putString(sharedpreferences.profile, "10");
                                                editor.putString(sharedpreferences.fingerprint, "1");
                                                editor.putString(sharedpreferences.theme, "0");
                                                editor.apply();

                                                Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                                startActivity(in);
                                            } else {
                                                Toast.makeText(OTPverifylogin_Activity.this, "" + (jsonResponse1 != null ? jsonResponse1.getMessage() : "Error"), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            progressDialog.hide();
                                        }
                                    });
                                } else {
                                    progressDialog.hide();
                                    Intent intent = new Intent(getApplicationContext(), ExtraDataGetActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("mobile", mobile);
                                    intent.putExtra("ccode", ccode);
                                    startActivity(intent);
                                }
                            } else {
                                progressDialog.hide();
                                Toast.makeText(OTPverifylogin_Activity.this, "" + (jsonResponse != null ? jsonResponse.getMessage() : "Error"), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(OTPverifylogin_Activity.this, "eroor: "+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            progressDialog.hide();
                        }
                    });

                }


            }
        });
        tvResendCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend.setVisibility(View.VISIBLE);
                tvResendCode.setVisibility(View.GONE);
                startResendCountdown();

                Call<JSONResponse> send_otp_api = ApiClient.getInstance1().getApi()
                        .sendingOTP(id);

                send_otp_api.enqueue(new Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                        progressDialog.hide();
                        if (response.body() != null) {
                            JSONResponse jsonResponse1 = response.body();
                            if (jsonResponse1.getStatus().equalsIgnoreCase("true")) {

                            } else {
                                Toast.makeText(getApplicationContext(), jsonResponse1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Failed to send OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp1.getText().toString().length()==1){
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp2.getText().toString().length()==1){
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp3.getText().toString().length()==1){
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void startResendCountdown() {
        tvResendCode.setVisibility(View.GONE);
        resend.setVisibility(View.VISIBLE);

        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the countdown timer text
                long secondsRemaining = millisUntilFinished / 1000;
                tvTimer.setText("00:" + (secondsRemaining < 10 ? "0" + secondsRemaining : secondsRemaining));
            }

            @Override
            public void onFinish() {
                // When the timer finishes, show the resend option
                resend.setVisibility(View.GONE);
                tvResendCode.setVisibility(View.VISIBLE);
            }
        }.start();

    }
}