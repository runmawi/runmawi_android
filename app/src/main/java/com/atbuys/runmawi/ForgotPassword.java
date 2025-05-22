package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    CardView sendOtp, smsOtp;
    resetpass resetpass1;
    EditText mail;
    ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendOtp = (CardView) findViewById(R.id.send_otp);
        smsOtp = (CardView) findViewById(R.id.sms_otp);
        mail = (EditText) findViewById(R.id.mail);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);

        mail.requestFocus();
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mail.getText().toString().equalsIgnoreCase(null) || mail.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ForgotPassword.this, "Please enter mail", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString()).matches()) {
                    Toast.makeText(ForgotPassword.this, "Enter valid email address", Toast.LENGTH_SHORT).show();
                } else {
                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getRestpassword(mail.getText().toString());
                    call.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            JSONResponse jsonResponse= response.body();
                            if (response.body() != null) {
                                if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                                    Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(getApplicationContext(), VerifyActivity.class);
                                    in.putExtra("email1", jsonResponse.getEmail());
                                    startActivity(in);
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Please Enter Your Registered Email ID", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),""+t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}