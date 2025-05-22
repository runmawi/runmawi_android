package com.atbuys.runmawi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class otp_sign_in extends AppCompatActivity {

    ImageView iv1;
    EditText uname, emails;
    CheckBox c1;
    Button submit;
    ProgressDialog progressDialog;
    private ArrayList<user_details> userdata;
    String xxVal="10";
    public static String email,username,mobile;
    ImageView logo;
    private ArrayList<Site_theme_setting> settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_signup);


        logo = findViewById(R.id.signuplogo);
        uname = findViewById(R.id.username);
        emails = findViewById(R.id.email);
        c1 = findViewById(R.id.check);
        submit = findViewById(R.id.verifybutton1);

        settings = new ArrayList<Site_theme_setting>();


/*
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });*/


        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                settings = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = settings.get(0).getImage_url();
                Picasso.get().load(x).into(logo);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String user = uname.getText().toString();
                String email1 = emails.getText().toString();


                if (uname.getText().toString().length() == 0) {
                    uname.setError("Username not entered");
                    uname.requestFocus();
                } else if (emails.getText().toString().length() == 0) {
                    emails.setError("E-Mail not entered");
                    emails.requestFocus();
                } else if (!c1.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Please Agree to the terms and condition", Toast.LENGTH_LONG).show();
                } else {

                    userdata = new ArrayList<user_details>();

                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPhonenumber(user,email1);
                    call.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                          /*  progressDialog.hide();*/
                            if (response.body() != null) {
                                if (response.body().getStatus().equalsIgnoreCase("true")) {

                                    JSONResponse jsonResponse = response.body();
                                    userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                                   /* Toast.makeText(getApplicationContext(),""+userdata.get(0).getId(),Toast.LENGTH_LONG).show();*/
                                    SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                    editor.putBoolean(sharedpreferences.login, true);
                                    editor.putString(sharedpreferences.user_id, userdata.get(0).getId());
                                    editor.putString(sharedpreferences.role, userdata.get(0).getRole());
                                    editor.putString(sharedpreferences.username, userdata.get(0).getUsername());
                                    editor.putString(sharedpreferences.email, userdata.get(0).getEmail());
                                    /*editor.putString(sharedpreferences.mobile, userdata.get(0).getMobile());*/
                                    editor.putString(sharedpreferences.profile, xxVal);
                                    editor.apply();
                                    editor.commit();


                                    Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                    startActivity(in);


                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"please check your email.it is already exist", Toast.LENGTH_LONG).show();


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
}




