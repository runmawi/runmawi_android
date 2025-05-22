/*
package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumberLoginActivity extends AppCompatActivity {

    Button sendOtp;
    mobileOTP mobileOtp;

    EditText mobile,mobil11;
    TextView country_code;

    AlertDialog alert;

    TelephonyManager telephonyManager;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private static final int PHONE_NUMBER_HINT = 100;
    private final int PERMISSION_REQ_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        } else {


            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number_login);

        sendOtp = (Button) findViewById(R.id.sendotp);
        mobile = (EditText) findViewById(R.id.mobile);
        mobil11 = (EditText) findViewById(R.id.mobil11);

        country_code = (TextView) findViewById(R.id.countrycode);



        final HintRequest hintRequest =
                new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();

        try {
            final GoogleApiClient googleApiClient =
                    new GoogleApiClient.Builder(MobileNumberLoginActivity.this).addApi(Auth.CREDENTIALS_API).build();

            final PendingIntent pendingIntent =
                    Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);

            startIntentSenderForResult(
                    pendingIntent.getIntentSender(),
                    PHONE_NUMBER_HINT,
                    null,
                    0,
                    0,
                    0
            );
        } catch (Exception e) {
            e.printStackTrace();
        }



        mobil11.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View v) {




            }
        });



        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(mobile.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Your mobile number", Toast.LENGTH_LONG).show();

                }
                else {
                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getMobileLogin(mobile.getText().toString());
                    call.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {


                            if (response.body() != null) {
                                if (response.body().getStatus().equalsIgnoreCase("true")) {

                                    JSONResponse jsonResponse = response.body();

                                    final AlertDialog.Builder alert = new AlertDialog.Builder(MobileNumberLoginActivity.this);
                                    View mView = getLayoutInflater().inflate(R.layout.dialog_otp, null);
                                    final EditText txt_inputText = (EditText) mView.findViewById(R.id.txt_input);
                                    Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
                                    Button btn_okay = (Button) mView.findViewById(R.id.btn_okay);
                                    alert.setView(mView);
                                    final AlertDialog alertDialog = alert.create();
                                    alertDialog.setCanceledOnTouchOutside(false);

                                    txt_inputText.setInputType(InputType.TYPE_NULL);
                                    txt_inputText.setText(jsonResponse.getOtp());
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    btn_okay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent in = new Intent(getApplicationContext(), OTPScreenActivity.class);
                                            in.putExtra("code", jsonResponse.getOtp());
                                            in.putExtra("mobile", mobile.getText().toString());
                                            startActivity(in);


                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();


                                } else {
                                    Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();


                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                            Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });


                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHONE_NUMBER_HINT && resultCode == RESULT_OK) {
            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            final String phoneNumber = credential.getId().substring(3);

            mobile.setVisibility(View.VISIBLE);
            mobile.setText(phoneNumber);

        }


    }



}*/
