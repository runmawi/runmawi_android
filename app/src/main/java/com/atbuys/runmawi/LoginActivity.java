package com.atbuys.runmawi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    CardView sign_in_password, google_layout, facebook_layout, apple_layout;
    LoginButton login_button;
    TextView signup;
    ImageView back_arrow;
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private IntentIntegrator qrScan;
    String tkn, xx, xxVal = "10";
    AlertDialog.Builder builder;
    private CallbackManager callbackManager;
    List<socialsetting> socialsettingList;
    LinearLayout orContinue_layout;


    private ArrayList<user_details> userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_in_password = (CardView) findViewById(R.id.sign_in_password);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        orContinue_layout=(LinearLayout)findViewById(R.id.orContinue_layout);
        qrScan = new IntentIntegrator(this);
        google_layout = (CardView) findViewById(R.id.google_layout);
        facebook_layout = (CardView) findViewById(R.id.facebook_layout);
        apple_layout = (CardView) findViewById(R.id.apple_layout);
        login_button = (LoginButton) findViewById(R.id.login_button);
        signup = (TextView) findViewById(R.id.signup);


        userdata = new ArrayList<>();

       // tkn = FirebaseInstanceId.getInstance().getToken();
        callbackManager = CallbackManager.Factory.create();
        builder = new AlertDialog.Builder(this);


        userdata = new ArrayList<>();

       // tkn = FirebaseInstanceId.getInstance().getToken();
        callbackManager = CallbackManager.Factory.create();
        builder = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        Uri data = intent.getData();

        if (data != null) {
            String scheme = data.getScheme();        // "https"
            String host = data.getHost();            // "recordrebellion.com"
            String path = data.getPath();            // "/mytv/quick-response/345tj8/45TF"
            List<String> pathSegments = data.getPathSegments(); // ["mytv", "quick-response", "345tj8", "45TF"]

            // Extract specific values from the path segments
            if (pathSegments.size() >= 3) {
                String appId = pathSegments.get(2); // "345tj8"
                String value = pathSegments.get(3); // "45TF"
                // You can now use 'appId' and 'value' as needed in your app.

                Toast.makeText(getApplicationContext(), ""+appId, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
            }


        }


        sign_in_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SigninActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSociallogin();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse= response.body();

                socialsettingList=new ArrayList<>(Arrays.asList(jsonResponse.getSocialsetting()));

                if (socialsettingList.get(0).getFacebook().equalsIgnoreCase("0")){

                    facebook_layout.setVisibility(View.GONE);

                }else {
                    facebook_layout.setVisibility(View.VISIBLE);
                }

                if (socialsettingList.get(0).getGoogle().equalsIgnoreCase("0")){
                    google_layout.setVisibility(View.GONE);
                }else {
                    google_layout.setVisibility(View.VISIBLE);
                }
                if (socialsettingList.get(0).getGoogle().equalsIgnoreCase("0") && socialsettingList.get(0).getFacebook().equalsIgnoreCase("0")){
                    orContinue_layout.setVisibility(View.GONE);
                }else {
                    orContinue_layout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Quit").setTitle("Yes");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent a = new Intent(Intent.ACTION_MAIN);
                                a.addCategory(Intent.CATEGORY_HOME);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(R.string.app_name);
                alert.show();
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
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

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

        callbackManager.onActivityResult(requestCode, resultCode, data);
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


            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(account.getDisplayName(), account.getEmail(), xx, "google");
            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                    if (response.body() != null) {

                        if (response.body().getStatus().equalsIgnoreCase("true")) {


                            JSONResponse jsonResponse = response.body();
                            userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
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
                            Toast.makeText(getApplicationContext(),"Logged In With Gmail",Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                            startActivity(in);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                }

            });
            try {

                // Toast.makeText(getApplicationContext(),""+account.getPhotoUrl(),Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "image not found", Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(getApplicationContext(), "Sign in cancel" + result.getStatus(),
                    Toast.LENGTH_LONG).show();
        }


    }

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            //Toast.makeText(LoginActivity.this, "enter-> ", Toast.LENGTH_LONG).show();
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            //Toast.makeText(LoginActivity.this, "name-> "+first_name+" "+last_name, Toast.LENGTH_LONG).show();
                            String email = object.getString("email");
                            //String email = "kannankrishnan2002@gmial.com";
                            //Toast.makeText(LoginActivity.this, "name-> "+first_name+" "+last_name, Toast.LENGTH_LONG).show();
                            //String email = null;

                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            String usernamee = first_name;

                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(usernamee, email, image_url, "facebook");
                            call.enqueue(new Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                                    if (response.body() != null) {
                                        if (response.body().getStatus().equalsIgnoreCase("true")) {


                                            JSONResponse jsonResponse = response.body();
                                            userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
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

                                            Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                            startActivity(in);


                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
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

   /* @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        builder.setMessage("Quit").setTitle("Yes");

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        System.exit(0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(R.string.app_name);
        alert.show();
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