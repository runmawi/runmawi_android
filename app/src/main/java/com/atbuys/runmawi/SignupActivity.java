package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.hbb20.CountryCodePicker;
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

public class SignupActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    CardView google_layout, facebook_layout, apple_layout,sign_up;
    LoginButton login_button;
    SignInButton signInButton;
    emailexists emailexistsresponse;
    registerresponse registerresponse1;
    TextView signin,termsandcondition;
    EditText user_email,user_password,username,mobile_no;
    CountryCodePicker country_picker;
    ImageView back_arrow,logo;
    private ArrayList<Site_theme_setting> Site_theme_setting;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    private IntentIntegrator qrScan;
    String tkn, xx,xxVal="10";
    AlertDialog.Builder builder;
    private CallbackManager callbackManager;
    ProgressDialog progressDialog;
    String free_registration;
    private ArrayList<settings> settings;
    boolean isEmailValid, isPasswordValid, isPasswordVisible;

    private ArrayList<user_details> userdata;
    CheckBox check;
    List<socialsetting> socialsettingList;
    LinearLayout orContinue_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        qrScan = new IntentIntegrator(this);
        google_layout = (CardView) findViewById(R.id.google_layout);
        orContinue_layout=(LinearLayout)findViewById(R.id.orContinue_layout);
        facebook_layout = (CardView) findViewById(R.id.facebook_layout);
        apple_layout = (CardView) findViewById(R.id.apple_layout);
        login_button = (LoginButton) findViewById(R.id.login_button);
        signin=(TextView) findViewById(R.id.signin);
        sign_up=(CardView) findViewById(R.id.sign_up);
        user_email=findViewById(R.id.emailandphone);
        user_password=findViewById(R.id.password);
        username=findViewById(R.id.username);
        mobile_no=findViewById(R.id.mobile_num);
        country_picker=(CountryCodePicker)findViewById(R.id.county_picker);
        check=(CheckBox)findViewById(R.id.check);
        logo=(ImageView)findViewById(R.id.logo);
        Site_theme_setting = new ArrayList<Site_theme_setting>();
        settings = new ArrayList<settings>();
        termsandcondition=(TextView)findViewById(R.id.termsandcondition);

        userdata=new ArrayList<>();
        progressDialog=new ProgressDialog(SignupActivity.this);

      //  tkn = FirebaseInstanceId.getInstance().getToken();
      //  String token = FirebaseInstanceId.getInstance().getToken();

        callbackManager = CallbackManager.Factory.create();
        builder = new AlertDialog.Builder(this);


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

        Call<JSONResponse> settingsapi = ApiClient.getInstance1().getApi().getSettings();
        settingsapi.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                settings = new ArrayList<>(Arrays.asList(jsonResponse.getSettings()));

                free_registration = settings.get(0).getFree_registration();

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });

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
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check.setChecked(check.isChecked());
                final AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.show_termsandconditions, null);
                dialog.setView(customLayout);
                final AlertDialog alert = dialog.create();
                alert.setCancelable(false);

                if (check.isChecked()){
                    alert.show();
                }else {
                    alert.hide();
                }

                TextView terms=customLayout.findViewById(R.id.terms);
                CardView accept=customLayout.findViewById(R.id.accept);
                ProgressBar termsProgress=customLayout.findViewById(R.id.termsProgress);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        check.setChecked(true);
                    }
                });

                Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getCmsPage();
                callser.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        ArrayList<pages> pageslist = new ArrayList<>(Arrays.asList(jsonResponse.getPages()));

                        for (int i = 0; i < pageslist.size(); i++) {
                            if (pageslist.get(i).getSlug().equalsIgnoreCase("terms-and-conditions")) {
                                String body=pageslist.get(i).getBody();
                                String removeAllTag = body.replaceAll("\\<.*?\\>", "");
                                String withoutNbsp = removeAllTag.replaceAll("&nbsp;", " ");
                                termsProgress.setVisibility(View.GONE);
                                terms.setText(withoutNbsp);
                                accept.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                    }
                });



            }
        });
        termsandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check.setChecked(check.isChecked());
                final AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.show_termsandconditions, null);
                dialog.setView(customLayout);
                final AlertDialog alert = dialog.create();
                alert.setCancelable(false);
                alert.show();

                TextView terms=customLayout.findViewById(R.id.terms);
                CardView accept=customLayout.findViewById(R.id.accept);
                ProgressBar termsProgress=customLayout.findViewById(R.id.termsProgress);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        check.setChecked(true);
                    }
                });

                Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getCmsPage();
                callser.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        ArrayList<pages> pageslist = new ArrayList<>(Arrays.asList(jsonResponse.getPages()));

                        for (int i = 0; i < pageslist.size(); i++) {
                            if (pageslist.get(i).getSlug().equalsIgnoreCase("terms-and-conditions")) {
                                String body=pageslist.get(i).getBody();
                                String removeAllTag = body.replaceAll("\\<.*?\\>", "");
                                String withoutNbsp = removeAllTag.replaceAll("&nbsp;", " ");
                                termsProgress.setVisibility(View.GONE);
                                terms.setText(withoutNbsp);
                                accept.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                    }
                });

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
                }if (socialsettingList.get(0).getGoogle().equalsIgnoreCase("0")){
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

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (username.getText().toString().length()>0){
                    username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_person_24_white, 0,0, 0);
                } else{
                    username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_person_24, 0,0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        user_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (user_email.getText().toString().length()>0){
                    user_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mailvectorwhite, 0,0, 0);
                } else{
                    user_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mailvector, 0,0, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        user_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (user_password.getRight() - 172)) {
                        int selection = user_password.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            user_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvector, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            user_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            user_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvectorwhite, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            user_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        user_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = user_password.getText().toString();
                String email = user_email.getText().toString();
                String name=username.getText().toString();
                String mobile=mobile_no.getText().toString();

                if (user_email.getText().toString().length() == 0|| user_email.getText().toString().trim().isEmpty()) {
                    user_email.setError("E-Mail not entered");
                    user_email.requestFocus();
                } else  if (!Patterns.EMAIL_ADDRESS.matcher(user_email.getText().toString()).matches()) {
                    user_email.setError("Enter valid Email address");
                } else if (user_password.getText().toString().length() == 0) {
                    //user_password.setError("Password not entered");
                    Toast.makeText(SignupActivity.this, "Password not entered", Toast.LENGTH_LONG).show();
                    user_password.requestFocus();
                } else if (user_password.getText().toString().length() < 8) {
                    //user_password.setError("Password must be at least 8 characters.");
                    Toast.makeText(SignupActivity.this, "Password must be at least 8 characters.", Toast.LENGTH_LONG).show();
                    user_password.requestFocus();
                } else if (!isValidPassword(user_password.getText().toString().trim())) {
                    //user_password.setError("Password should be at least 8 characters in length and should include at least one upper case letter, one number, and one special character.");
                    Toast.makeText(SignupActivity.this, "Password should be at least 8 characters in length and should include at least one upper case letter, one number, and one special character.", Toast.LENGTH_LONG).show();
                } else if (username.getText().toString().trim().isEmpty()) {
                    username.setError("Name not entered");
                    username.requestFocus();
                }

                else if (!mobile_no.getText().toString().isEmpty()) {
                    boolean isValid = PhoneNumberValidator.isValidPhoneNumber(country_picker.getSelectedCountryNameCode(), mobile_no.getText().toString());

                    progressDialog.setMessage("Please Wait");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    if (isValid){
                        Api.getClient().getEmailExists(email, name, new retrofit.Callback<emailexists>() {
                            @Override
                            public void success(emailexists emailexists, retrofit.client.Response response) {

                                progressDialog.hide();
                                emailexistsresponse = emailexists;
                                if (emailexists.getStatus().equalsIgnoreCase("false")) {
                                    Toast.makeText(getApplicationContext(), "" + emailexists.getMessage(), Toast.LENGTH_LONG).show();

                                }

                                else {


                                    if(free_registration.equalsIgnoreCase("1")) {

                                        Api.getClient().getSignupWithSkip(name, email, password, country_picker.getSelectedCountryCode(), mobile, "1", "", tkn, "",
                                                new retrofit.Callback<registerresponse>() {
                                                    @Override
                                                    public void success(registerresponse signUpResponse, retrofit.client.Response response) {
                                                        registerresponse1 = signUpResponse;
                                                        if (registerresponse1.getStatus().equalsIgnoreCase("true")) {

                                                            Toast.makeText(getApplicationContext(), "Registered Successfully. You Can Login Using Your Credentials ", Toast.LENGTH_LONG).show();
                                                            Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                                            startActivity(in);

                                                        } else if (registerresponse1.getStatus().equalsIgnoreCase("false")) {
                                                            if (registerresponse1.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                                                Intent in = new Intent(SignupActivity.this, SigninActivity.class);
                                                                Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                                                startActivity(in);

                                                            } else {
                                                                Intent in = new Intent(SignupActivity.this, SignupActivity.class);
                                                                username.setError("Username already taken. Please choose other username");
                                                                Toast.makeText(getApplicationContext(), "Username already taken. Please choose aother username", Toast.LENGTH_LONG).show();
                                                                startActivity(in);
                                                            }
                                                        } else {

                                                            Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {

                                                        Toast.makeText(SignupActivity.this, "Check Your Internet Connection" + error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        // showPaymentoptDialog();
                                        Intent in = new Intent(SignupActivity.this, SignupSubscribeActivity.class);
                                        in.putExtra("username", name);
                                        in.putExtra("email", email);
                                        in.putExtra("password", password);
                                        in.putExtra("ccode", country_picker.getSelectedCountryCode());
                                        in.putExtra("mobile", mobile);
                                        in.putExtra("ref_code", "");
                                        in.putExtra("token", tkn);
                                        in.putExtra("payment_type", "");
                                        startActivity(in);
                                        progressDialog.hide();
                                    }
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.hide();

                            }
                        });
                    }

                    else{
                        mobile_no.requestFocus();
                        mobile_no.setError("Phone number is invalid");
                        progressDialog.hide();
                    }

                } else if (!check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                } else {

                    progressDialog.setMessage("Please Wait");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.show();

                    //String name =user_email.getText().toString();
                    //String[] splited = name.split("@");
                    //Toast.makeText(SignupActivity.this, "name-> "+name, Toast.LENGTH_SHORT).show();

                    Api.getClient().getEmailExists(email, name, new retrofit.Callback<emailexists>() {
                        @Override
                        public void success(emailexists emailexists, retrofit.client.Response response) {

                            progressDialog.hide();
                            emailexistsresponse = emailexists;
                            if (emailexists.getStatus().equalsIgnoreCase("false")) {
                                Toast.makeText(getApplicationContext(), "" + emailexists.getMessage(), Toast.LENGTH_LONG).show();

                            } else {

                                if(free_registration.equalsIgnoreCase("1")) {

                                    Api.getClient().getSignupWithSkip(name, email, password, country_picker.getSelectedCountryCode(), mobile, "1", "", tkn, "",
                                            new retrofit.Callback<registerresponse>() {
                                                @Override
                                                public void success(registerresponse signUpResponse, retrofit.client.Response response) {
                                                    registerresponse1 = signUpResponse;
                                                    if (registerresponse1.getStatus().equalsIgnoreCase("true")) {

                                                        Toast.makeText(getApplicationContext(), "Registered Successfully. You Can Login Using Your Credentials ", Toast.LENGTH_LONG).show();
                                                        Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                                        startActivity(in);

                                                    } else if (registerresponse1.getStatus().equalsIgnoreCase("false")) {
                                                        if (registerresponse1.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                                            Intent in = new Intent(SignupActivity.this, SigninActivity.class);
                                                            Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                                            startActivity(in);

                                                        } else {
                                                            Intent in = new Intent(SignupActivity.this, SignupActivity.class);
                                                            username.setError("Username already taken. Please choose other username");
                                                            Toast.makeText(getApplicationContext(), "Username already taken. Please choose aother username", Toast.LENGTH_LONG).show();
                                                            startActivity(in);
                                                        }
                                                    } else {

                                                        Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void failure(RetrofitError error) {

                                                    Toast.makeText(SignupActivity.this, "Check Your Internet Connection" + error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else {
                                    // showPaymentoptDialog();
                                    Intent in = new Intent(SignupActivity.this, SignupSubscribeActivity.class);
                                    in.putExtra("username", name);
                                    in.putExtra("email", email);
                                    in.putExtra("password", password);
                                    in.putExtra("ccode", country_picker.getSelectedCountryCode());
                                    in.putExtra("mobile", mobile);
                                    in.putExtra("ref_code", "");
                                    in.putExtra("token", tkn);
                                    in.putExtra("payment_type", "");
                                    startActivity(in);
                                    progressDialog.hide();
                                }
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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



            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getGooleclient(account.getDisplayName(),account.getEmail(), xx,"google");
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
                            editor.putString(sharedpreferences.profile,xxVal);
                            editor.putString(sharedpreferences.fingerprint,"1");
                            editor.putString(sharedpreferences.theme,"0");
                            editor.apply();
                            Toast.makeText(getApplicationContext(),"Logged In With Gmail",Toast.LENGTH_LONG).show();
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
            try{

                // Toast.makeText(getApplicationContext(),""+account.getPhotoUrl(),Toast.LENGTH_LONG).show();
            }
            catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found", Toast.LENGTH_LONG).show();
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
                                            userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));
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
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*{}?.,<>+=])(?=\\S+$).{4,}$";


        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

   /* @Override
    public void onClick(View v) {
        qrScan.initiateScan();
    }*/

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