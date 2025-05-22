package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailLoginFragment extends Fragment {

    CardView google_layout, facebook_layout, apple_layout, sign_in;
    LoginButton login_button;
    SignInButton signInButton;
    TextView signup, forget;
    EditText emailandphone, userpassword;
    boolean isPasswordVisible;
    ImageView logo;
    private ArrayList<Site_theme_setting> Site_theme_setting;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private IntentIntegrator qrScan;
    String tkn, xx, xxVal = "10";
    AlertDialog.Builder builder;
    private CallbackManager callbackManager;

    TextInputLayout textInputLayout;
    ProgressDialog progressDialog;
    AlertDialog dialog;
    CheckBox check;
    List<socialsetting> socialsettingList;
    private ArrayList<user_details> userdata;
    LinearLayout orContinue_layout;
    emailexists emailexistsresponse;
    registerresponse registerresponse1;
    Context context;

    public MailLoginFragment(Context context) {
        this.context=context;
    }
    public MailLoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_mail_login, container, false);

        qrScan = new IntentIntegrator(getActivity());
        google_layout = (CardView) root.findViewById(R.id.google_layout);
        orContinue_layout = (LinearLayout) root.findViewById(R.id.orContinue_layout);
        facebook_layout = (CardView) root.findViewById(R.id.facebook_layout);
        apple_layout = (CardView) root.findViewById(R.id.apple_layout);
        login_button = (LoginButton) root.findViewById(R.id.login_button);
        signup = (TextView) root.findViewById(R.id.signup);
        sign_in = (CardView) root.findViewById(R.id.sign_in);
        emailandphone = (EditText) root.findViewById(R.id.emailandphone);
        userpassword = (EditText) root.findViewById(R.id.password);
        forget = (TextView) root.findViewById(R.id.forget);
        check = (CheckBox) root.findViewById(R.id.check);
        logo = (ImageView) root.findViewById(R.id.logo);
        Site_theme_setting = new ArrayList<Site_theme_setting>();

        userdata = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());

        //tkn = FirebaseInstanceId.getInstance().getToken();
        callbackManager = CallbackManager.Factory.create();
        builder = new AlertDialog.Builder(getContext());

        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                Site_theme_setting = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = Site_theme_setting.get(0).getImage_url();
                Picasso.get().load(x).into(logo);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSociallogin();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();

                socialsettingList = new ArrayList<>(Arrays.asList(jsonResponse.getSocialsetting()));

                if (socialsettingList.get(0).getFacebook().equalsIgnoreCase("0")) {

                    facebook_layout.setVisibility(View.GONE);

                } else {
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

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ForgotPassword.class));
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailandphone.getText().toString();
                String password = userpassword.getText().toString();

                if (emailandphone.getText().toString().length() == 0 || emailandphone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "E-Mail not entered", Toast.LENGTH_SHORT).show();
                    emailandphone.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailandphone.getText().toString()).matches()) {
                    Toast.makeText(getContext(), "Enter valid Email address", Toast.LENGTH_SHORT).show();
                    emailandphone.requestFocus();
                } else if (userpassword.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Password not entered", Toast.LENGTH_SHORT).show();
                    userpassword.requestFocus();
                } else if (!isValidPassword(userpassword.getText().toString().trim())) {
                    Toast.makeText(getContext(), "Password should have 8 char,1 upper,1 number and 1 special char", Toast.LENGTH_LONG).show();
                    userpassword.requestFocus();
                }  else {

                    progressDialog.setMessage("Please Wait");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    String name = emailandphone.getText().toString();
                    String[] splited = name.split("@");

                    Api.getClient().getEmailExists(email, splited[0], new retrofit.Callback<emailexists>() {
                        @Override
                        public void success(emailexists emailexists, retrofit.client.Response response) {

                            emailexistsresponse = emailexists;
                            if (emailexists.getStatus().equalsIgnoreCase("false")) {

                                Call<JSONResponse> call = ApiClient.getInstance1().getApi().getLogin(email, password, tkn);
                                call.enqueue(new Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                                        if (response.body() != null) {
                                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                                progressDialog.hide();
                                                JSONResponse jsonResponse = response.body();
                                                userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
                                                SharedPreferences.Editor editor = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                editor.putBoolean(sharedpreferences.login, true);
                                                editor.putString(sharedpreferences.user_id, userdata.get(0).getUser_id());
                                                editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                                                editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                                                editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                                                editor.putString(sharedpreferences.profile, xxVal);
                                                editor.putString(sharedpreferences.fingerprint, "1");
                                                editor.putString(sharedpreferences.theme, "0");
                                                editor.apply();
                                                Toast.makeText(getContext(), " " + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                                Intent in = new Intent(getContext(), HomePageActivitywithFragments.class);
                                                startActivity(in);

                                            } else if ((response.body().getStatus().equalsIgnoreCase("verifyemail"))) {
                                                progressDialog.hide();
                                                Toast.makeText(getContext(), "Please make sure to activate your account in your email before logging in", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    }
                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        progressDialog.hide();
                                        Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                });
                            } else {

                                Api.getClient().getSignupWithSkip(splited[0], email, password, "", "", "1", "", tkn, "",
                                        new retrofit.Callback<registerresponse>() {
                                            @Override
                                            public void success(registerresponse signUpResponse, retrofit.client.Response response) {
                                                registerresponse1 = signUpResponse;
                                                if (registerresponse1.getStatus().equalsIgnoreCase("true")) {

                                                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getLogin(email, password, tkn);
                                                    call.enqueue(new Callback<JSONResponse>() {
                                                        @Override
                                                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                                                            if (response.body() != null) {
                                                                if (response.body().getStatus().equalsIgnoreCase("true")) {

                                                                    progressDialog.hide();
                                                                    JSONResponse jsonResponse = response.body();
                                                                    userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
                                                                    SharedPreferences.Editor editor = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                                                    editor.putBoolean(sharedpreferences.login, true);
                                                                    editor.putString(sharedpreferences.user_id, userdata.get(0).getUser_id());
                                                                    editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                                                                    editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                                                                    editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                                                                    editor.putString(sharedpreferences.profile, xxVal);
                                                                    editor.putString(sharedpreferences.fingerprint, "1");
                                                                    editor.putString(sharedpreferences.theme, "0");
                                                                    editor.apply();
                                                                    Toast.makeText(getContext(), " " + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                                                    Intent in = new Intent(getContext(), HomePageActivitywithFragments.class);
                                                                    startActivity(in);

                                                                } else if ((response.body().getStatus().equalsIgnoreCase("verifyemail"))) {
                                                                    progressDialog.hide();
                                                                    Toast.makeText(getContext(), "Please make sure to activate your account in your email before logging in", Toast.LENGTH_LONG).show();
                                                                }
                                                            }

                                                        }
                                                        @Override
                                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                            progressDialog.hide();
                                                            Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                                        }

                                                    });

                                                } else if (registerresponse1.getStatus().equalsIgnoreCase("false")) {
                                                    progressDialog.hide();
                                                    if (registerresponse1.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                                        Toast.makeText(getContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Username already taken. Please choose aother username", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    progressDialog.hide();
                                                    Toast.makeText(getContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                progressDialog.hide();
                                                Toast.makeText(getContext(), "Check Your Internet Connection" + error.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            progressDialog.hide();

                        }
                    });


                }
            }
        });

        emailandphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (emailandphone.getText().toString().length() > 0) {
                    emailandphone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mailvectorwhite, 0, 0, 0);
                } else {
                    emailandphone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mailvector, 0, 0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (userpassword.getText().toString().trim().length() >= 8) {
                    forget.setVisibility(View.GONE);
                } else {
                    forget.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getContext())
                //.enableAutoManage(getActivity(), connectionResult -> {})
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

                Toast.makeText(getContext(), "error" + error, Toast.LENGTH_SHORT).show();


            }
        });

        userpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (userpassword.getRight() - 172)) {
                        int selection = userpassword.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            userpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvector, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            userpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else {
                            // set drawable image
                            userpassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvectorwhite, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            userpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        userpassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult resultqr = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultqr != null) {

            if (resultqr.getContents() == null) {
                Toast.makeText(getContext(), "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(resultqr.getContents());


                    SharedPreferences.Editor editor = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                    editor.putBoolean(sharedpreferences.login, true);
                    editor.putString(sharedpreferences.user_id, obj.getString("id"));
                    editor.putString(sharedpreferences.role, obj.getString("role"));
                    editor.putString(sharedpreferences.username, obj.getString("username"));
                    editor.putString(sharedpreferences.email, obj.getString("email"));
                    editor.putString(sharedpreferences.profile, xxVal);
                    editor.apply();

                    Intent in = new Intent(getContext(), HomePageActivitywithFragments.class);

                    startActivity(in);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getContext(), resultqr.getContents(), Toast.LENGTH_LONG).show();
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


            //Toast.makeText(this, "namr-> "+account.getDisplayName(), Toast.LENGTH_SHORT).show();
            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(account.getDisplayName(), account.getEmail(), xx, "google");
            call.enqueue(new Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                    if (response.body() != null) {

                        if (response.body().getStatus().equalsIgnoreCase("true")) {

                            JSONResponse jsonResponse = response.body();
                            userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
                            SharedPreferences.Editor editor = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                            editor.putBoolean(sharedpreferences.login, true);
                            editor.putString(sharedpreferences.user_id, userdata.get(0).getId());
                            editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                            editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                            editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                            editor.putString(sharedpreferences.profile, xxVal);
                            editor.putString(sharedpreferences.fingerprint, "1");
                            editor.putString(sharedpreferences.theme, "0");
                            editor.apply();
                            Toast.makeText(getContext(), "Logged In With Gmail", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getContext(), HomePageActivitywithFragments.class);
                            startActivity(in);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {

                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }

            });
            try {

                // Toast.makeText(getContext(),""+account.getPhotoUrl(),Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                Toast.makeText(getContext(), "image not found", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Sign in cancel" + result.getStatus(), Toast.LENGTH_LONG).show();
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


                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(usernamee, email, image_url, "facebook");
                            call.enqueue(new Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                                    if (response.body() != null) {
                                        if (response.body().getStatus().equalsIgnoreCase("true")) {


                                            JSONResponse jsonResponse = response.body();
                                            userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
                                            SharedPreferences.Editor editor = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                            editor.putBoolean(sharedpreferences.login, true);
                                            editor.putString(sharedpreferences.user_id, userdata.get(0).getId());
                                            editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                                            editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                                            editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                                            editor.putString(sharedpreferences.profile, xxVal);
                                            editor.putString(sharedpreferences.fingerprint, "1");
                                            editor.putString(sharedpreferences.theme, "0");
                                            editor.apply();

                                            Intent in = new Intent(getContext(), HomePageActivitywithFragments.class);
                                            startActivity(in);


                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "You are not link with email in facebook " + e.getMessage(), Toast.LENGTH_LONG).show();


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
    public void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            //googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    public void onClick(View v) {
        if (v == facebook_layout) {
            login_button.performClick();
        }
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*{}?.,<>+=])(?=\\S+$).{4,}$";


        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}