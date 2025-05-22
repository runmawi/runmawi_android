package com.atbuys.runmawi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    CardView google_layout, facebook_layout, apple_layout, request_otp;
    ImageView logo, back_arrow;
    CountryCodePicker country_picker;
    EditText mobile_number;
    LinearLayout orContinue_layout;
    LoginButton login_button;
    private CallbackManager callbackManager;
    String tkn, xx, xxVal = "10";
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient googleApiClient;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otplogin);

        request_otp = (CardView) findViewById(R.id.request_otp);
        google_layout = (CardView) findViewById(R.id.google_layout);
        facebook_layout = (CardView) findViewById(R.id.facebook_layout);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        logo = (ImageView) findViewById(R.id.logo);
        country_picker = (CountryCodePicker) findViewById(R.id.country_picker);
        mobile_number = (EditText) findViewById(R.id.mobile_number);
        orContinue_layout = (LinearLayout) findViewById(R.id.orContinue_layout);
         login_button =(LoginButton) findViewById(R.id.login_button);
        //tkn = FirebaseInstanceId.getInstance().getToken();
        callbackManager = CallbackManager.Factory.create();
        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(OTPLoginActivity.this);
        startResendCountdown();


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                ArrayList<Site_theme_setting> Site_theme_setting = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = Site_theme_setting.get(0).getImage_url();
                Picasso.get().load(x).into(logo);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSociallogin();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                ArrayList<socialsetting> socialsettingList = new ArrayList<>(Arrays.asList(jsonResponse.getSocialsetting()));

                if (socialsettingList.get(0).getFacebook().equalsIgnoreCase("0")){
                    facebook_layout.setVisibility(View.GONE);
                }else {
                    facebook_layout.setVisibility(View.VISIBLE);
                }
                if (socialsettingList.get(0).getGoogle().equalsIgnoreCase("0")) {
                    google_layout.setVisibility(View.GONE);
                } else {
                    google_layout.setVisibility(View.VISIBLE);
                }
                if (socialsettingList.get(0).getGoogle().equalsIgnoreCase("0") && socialsettingList.get(0).getFacebook().equalsIgnoreCase("0")) {
                    orContinue_layout.setVisibility(View.GONE);
                } else {
                    orContinue_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });

        // Function to get the length of the mobile number for a given country code and phone number


        request_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the mobile number field is not empty
                if (!mobile_number.getText().toString().isEmpty()) {
                    // Validate the phone number using PhoneNumberValidator
                    boolean isValid = PhoneNumberValidator.isValidPhoneNumber(
                            country_picker.getSelectedCountryNameCode(),
                            mobile_number.getText().toString()  // Fixed mobile_no to mobile_number
                    );

                    if ((isValid) || (mobile_number.getText().toString().equalsIgnoreCase("0987654321"))) {
                        // If valid, show the progress dialog and send OTP
                        progressDialog.setMessage("OTP Sending...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        progressDialog.setMax(100);
                        progressDialog.show();

                        mobile_number mobileNumber = new mobile_number(mobile_number.getText().toString(),"+" + country_picker.getSelectedCountryCode());
                       // ccode cc = new ccode("+" + country_picker.getSelectedCountryCode());
                        // Make API call to check if mobile number exists
                        Call<JSONResponse> verify_api = ApiClient.getInstance1().getApi().mobileExistVerify(mobileNumber);
                        verify_api.enqueue(new Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                                if (response.body() != null) {
                                    JSONResponse jsonResponse = response.body();
                                    user_detail userDetail = jsonResponse.getUser_detail();
                                    progressDialog.hide();

                                    // Now call the API to send OTP using userDetail
                                    Call<JSONResponse> send_otp_api = ApiClient.getInstance1().getApi().sendingOTP(userDetail.getId());
                                    send_otp_api.enqueue(new Callback<JSONResponse>() {
                                        @Override
                                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                                            progressDialog.hide();
                                            if (response.body() != null) {
                                                JSONResponse jsonResponse1 = response.body();
                                                if (jsonResponse1.getStatus().equalsIgnoreCase("true")) {

                                                    // OTP sent successfully, move to the OTP verification screen
                                                    Intent intent = new Intent(getApplicationContext(), OTPverifylogin_Activity.class);
                                                    intent.putExtra("acc_status", jsonResponse.getMobile_number_status());
                                                    intent.putExtra("id", userDetail.getId());
                                                    intent.putExtra("mobile", userDetail.getMobile());
                                                    intent.putExtra("email", userDetail.getEmail());
                                                    intent.putExtra("ccode", userDetail.getCcode());
                                                    startActivity(intent);
                                                } else {
                                                    // Display error message if OTP sending failed
                                                    Toast.makeText(OTPLoginActivity.this, jsonResponse1.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                            // Hide the progress dialog and show an error message if the OTP API call fails
                                            progressDialog.hide();
                                            Toast.makeText(OTPLoginActivity.this, "Failed to send OTP: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    progressDialog.hide();
                                    Toast.makeText(OTPLoginActivity.this, "Mobile number verification failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                // Hide the progress dialog and show error message if the verification API call fails
                                progressDialog.hide();
                                Toast.makeText(OTPLoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // If the phone number is invalid, show an error
                        mobile_number.setError("Phone number is invalid");
                        mobile_number.requestFocus();
                    }
                } else {
                    // If the mobile number field is empty, show an error
                    mobile_number.setError("Please enter a valid phone number");
                    mobile_number.requestFocus();
                }
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        google_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
          boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
            //Using Graph API
            //    loadUserProfile(AccessToken.getCurrentAccessToken());
        }
         login_button.setReadPermissions(Collections.singletonList("email, public_profile, user_friends"));

        checkLoginStatus();
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loadUserProfile(AccessToken.getCurrentAccessToken());
            }
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "error" + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startResendCountdown() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult resultqr = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultqr != null) {

            if (resultqr.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(resultqr.getContents());


                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putBoolean(sharedpreferences.login, true);
                    editor.putString(sharedpreferences.user_id, obj.getString("id"));
                    editor.putString(sharedpreferences.role, obj.getString("role"));
                    editor.putString(sharedpreferences.username, obj.getString("username"));
                    editor.putString(sharedpreferences.email, obj.getString("email"));
                    editor.putString(sharedpreferences.profile, xxVal);
                    editor.apply();

                    Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);

                    startActivity(in);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(this, resultqr.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                });
            }

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {


        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();


            assert account != null;
            if (account.getPhotoUrl() == null) {
                xx = "";
            } else {
                xx = String.valueOf(account.getPhotoUrl());
            }


            Log.w("Runmawii","name: "+account.getDisplayName()+" mail: "+account.getEmail()+" xx: "+xx);
            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(account.getDisplayName(), account.getEmail(), "", "google");
            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                    if (response.body() != null) {

                        if (response.body().getStatus().equalsIgnoreCase("true")) {


                            JSONResponse jsonResponse = response.body();
                            ArrayList<user_details> userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                            editor.putBoolean(sharedpreferences.login, true);
                            editor.putString(sharedpreferences.user_id, userdata.get(0).getId());
                            editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                            editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                            editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                            editor.putString(sharedpreferences.profile, xxVal);
                            editor.putString(sharedpreferences.fingerprint, "1");
                            editor.putString(sharedpreferences.theme, "0");
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Logged In With Gmail", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                            startActivity(in);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Api Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }

            });
        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel" + result.getStatus(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {


                        try {

                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");

                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            String usernamee = first_name;


                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(usernamee,email,image_url,"facebook");
                            call.enqueue(new Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                                    if (response.body() != null) {
                                        if (response.body().getStatus().equalsIgnoreCase("true")) {


                                            JSONResponse jsonResponse = response.body();
                                            ArrayList<user_details> userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
                                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.user_id, userdata.get(0).getId());
                                            editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                                            editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                                            editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                                            editor.putString(sharedpreferences.profile,xxVal);
                                            editor.putString(sharedpreferences.fingerprint,"1");
                                            editor.putString(sharedpreferences.theme,"0");
                                            editor.apply();

                                            Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                            startActivity(in);



                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                    Toast.makeText(getApplicationContext(),""+t.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "You are not link with email in facebook " + e.getMessage(), Toast.LENGTH_LONG).show();


                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

    @Override
    public void onBackPressed() {
        // Display a confirmation dialog before exiting the app
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the app completely
                        finishAffinity(); // This closes the app and all its tasks
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(this);
            googleApiClient.disconnect();
        }
    }

    public void onClick(View v) {
        if (v == facebook_layout) {
            login_button.performClick();
        }
    }
}