package com.atbuys.runmawi;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.Checkout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupActivityNew extends AppCompatActivity {


    private ArrayList<settings> settings;
    private ArrayList<Site_theme_setting> site_theme_settings;
    ImageView signuplogo;
    EditText fullname, email, password, con_pass, mobile, ref_code;
    private String xx;
    ProgressDialog progressDialog;

    TextView signin;
    String free_registration;

    public static TextView countrycode;
    Button register, country, verifybutton;
    registerresponse regrespone1;
    List<Countrycode> movieList;

    final Context context = this;
    public static Dialog dialog;
    public static AlertDialog alertDialog;
    private static final String TAG = "mFirebaseIIDService";
    private List<country> myImageNameList;
    private List<states> statelistdata;
    private List<cities> citieslistdata;
    RecyclerView countryrecycle;
    registerresponse registerresponse1;

    private IsPayment resetpass1;
    private sendotp sendotp;
    private verifyotp verifyotp;
    Bitmap bitmap;
    CircleImageView userimage;
    Uri selectedImage;
    RequestBody requestFile;
    MultipartBody.Part body2;
    CheckBox check;
    String verify;
    String tkn;
    String payment_type;

    emailexists emailexistsresponse;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Object view;
    String username11,email11,password11,mobile11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        } else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupnew);


        signuplogo = findViewById(R.id.logo);
        settings = new ArrayList<settings>();
        site_theme_settings = new ArrayList<Site_theme_setting>();

        movieList = new ArrayList<>();

        fullname = (EditText) findViewById(R.id.fullname);
        password = (EditText) findViewById(R.id.password);
        con_pass = (EditText) findViewById(R.id.confirmpass);
        countrycode = (TextView) findViewById(R.id.countrycode);
        mobile = (EditText) findViewById(R.id.mobile);
        register = (Button) findViewById(R.id.signin);
        email = (EditText) findViewById(R.id.emailid);
        check = (CheckBox) findViewById(R.id.check);
      //  tkn = FirebaseInstanceId.getInstance().getToken();

        progressDialog = new ProgressDialog(this);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.backlogin);



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
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


        Call<JSONResponse> callimg1 = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg1.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                site_theme_settings = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = site_theme_settings.get(0).getImage_url();
                Picasso.get().load(x).into(signuplogo);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });



        /* Toast.makeText(getApplicationContext(),"your tokenis"+tkn,Toast.LENGTH_LONG).show();*/
        Log.i(TAG, "onTokenRefresh completed with token: " + tkn);


        countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(SignupActivityNew.this);

            }

        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String username1 = fullname.getText().toString().trim();
                String password1 = con_pass.getText().toString();
                String email1 = email.getText().toString();
                String mobile1 = mobile.getText().toString();
                String contry = countrycode.getText().toString();


                    if ((fullname.getText().toString().length() == 0 || fullname.getText().toString().trim().isEmpty())){
                        fullname.setError("Username not entered");
                        fullname.requestFocus();

                    }

                    else if (email.getText().toString().length() == 0|| email.getText().toString().trim().isEmpty()) {
                        email.setError("E-Mail not entered");
                        email.requestFocus();
                    }

               else  if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Enter valid Email address");
                }

               /*     else if (mobile.getText().toString().length() == 0) {
                        mobile.setError("Mobile Number Not Entered");
                        mobile.requestFocus();
                    }

                    else if (mobile.getText().toString().length() < 10 || mobile.getText().toString().length() > 10) {
                        mobile.setError("Mobile Number Must be 10 Letteres");
                        mobile.requestFocus();
                    }*/

                    else if (password.getText().toString().length() == 0) {
                        password.setError("Password not entered");
                        password.requestFocus();
                    } else if (password.getText().toString().length() < 8) {
                        password.setError("Password must be at least 8 characters.");
                        password.requestFocus();
                    }

                    else if (!isValidPassword(password.getText().toString().trim())) {
                        password.setError("Password should be at least 8 characters in length and should include at least one upper case letter, one number, and one special character.");
                    }
                    else if (con_pass.getText().toString().length() == 0) {
                        con_pass.setError("confirm password not entered");
                        con_pass.requestFocus();
                    }

                    else if (!password.getText().toString().equals(con_pass.getText().toString())) {
                        con_pass.setError("Password Not matched");
                        con_pass.requestFocus();
                    }
                    else if (!check.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                    } else {

                        progressDialog.setMessage("Please Wait");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        progressDialog.setMax(100);
                        progressDialog.show();

                        Api.getClient().getEmailExists(email1, username1, new Callback<emailexists>() {
                            @Override
                            public void success(emailexists emailexists, Response response) {
                                emailexistsresponse = emailexists;
                                if (emailexists.getStatus().equalsIgnoreCase("false")) {
                                    progressDialog.hide();
                                    Toast.makeText(getApplicationContext(), "" + emailexists.getMessage(), Toast.LENGTH_LONG).show();

                                } else {

                                    if(free_registration.equalsIgnoreCase("1"))
                                    {

                                        Api.getClient().getSignupWithSkip(username1, email1, password1, "", mobile1, "1", "", tkn, "",
                                                new Callback<registerresponse>() {
                                                    @Override
                                                    public void success(registerresponse signUpResponse, Response response) {
                                                        registerresponse1 = signUpResponse;
                                                        if (registerresponse1.getStatus().equalsIgnoreCase("true")) {

                                                            Toast.makeText(getApplicationContext(), "Registered Successfully. You Can Login Using Your Credentials ", Toast.LENGTH_LONG).show();
                                                            Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                                            startActivity(in);
                                                            progressDialog.hide();

                                                        } else if (registerresponse1.getStatus().equalsIgnoreCase("false")) {
                                                            if (registerresponse1.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                                                Intent in = new Intent(SignupActivityNew.this, SigninActivity.class);
                                                                Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                                                startActivity(in);
                                                                progressDialog.hide();
                                                            } else {
                                                                Intent in = new Intent(SignupActivityNew.this, SignupActivity.class);
                                                                //mobile.setError("Username already taken. Please choose aother username");
                                                                Toast.makeText(getApplicationContext(), "Username already taken. Please choose aother username", Toast.LENGTH_LONG).show();
                                                                startActivity(in);
                                                                progressDialog.hide();
                                                            }
                                                        } else {
                                                            progressDialog.hide();
                                                            Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        progressDialog.hide();
                                                        Toast.makeText(SignupActivityNew.this, "Check Your Internet Connection" + error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                    }

                                    else {

                                        // showPaymentoptDialog();
                                        Intent in = new Intent(SignupActivityNew.this, SignupSubscribeActivity.class);
                                        in.putExtra("username", username1);
                                        in.putExtra("email", email1);
                                        in.putExtra("password", password1);
                                        in.putExtra("ccode", contry);
                                        in.putExtra("mobile", mobile1);
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
                            //    Toast.makeText(getApplicationContext(), "weew" + error.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    }
            });


    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*{}?.,<>+=])(?=\\S+$).{4,}$";


        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    private void startrazor(String subscribeid, String displayName, String plan) {

        //final String email = in.getStringExtra("email");

     //   Toast.makeText(getApplicationContext(),""+subscribeid, Toast.LENGTH_LONG).show();
//        double a = Double.parseDouble(price);
        // double payment = a*100;
        final Activity activity = this;
        final Checkout co = new Checkout();

        try {

            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", "Runmawi Subscription");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "http://runmawi.com/content/uploads/settings/5f8d4d2f932bf-c-l.png");
            options.put("currency", "INR");
            options.put("subscription_id", subscribeid);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");

            options.put("prefill", preFill);

             Toast.makeText(this, "response "+preFill+ " "+options, Toast.LENGTH_LONG).show();

            co.open(activity, options);

        }
        catch (Exception e) {

            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    private void showDialog(SignupActivityNew signupActivityNew) {

        dialog = new Dialog(signupActivityNew);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_recycler);

        Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("http://runmawi.com/assets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebAPIService service1 = retrofit1.create(WebAPIService.class);
        Call<List<Countrycode>> jsonCall = service1.readJson();
        jsonCall.enqueue(new retrofit2.Callback<List<Countrycode>>() {
            @Override
            public void onResponse(Call<List<Countrycode>> call, retrofit2.Response<List<Countrycode>> response) {

                RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
                movieList = response.body();
                AdapterRe adapterRe = new AdapterRe(SignupActivityNew.this, movieList);
                recyclerView.setAdapter(adapterRe);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                recyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.show();
            }

            @Override
            public void onFailure(Call<List<Countrycode>> call, Throwable t) {
                Log.e("happy", t.toString());
              //  Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }


        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {


                }
                break;
        }
    }


    public void terms(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(SignupActivityNew.this);
        WebView wv = new WebView(SignupActivityNew.this);
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

}