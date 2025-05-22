package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import retrofit.Callback;
import retrofit.RetrofitError;


public class ForgotVerificationActivity extends AppCompatActivity {

    EditText verifycode;
    Button verifybutton;
    EditText password;
    private String resemail;
    resetpass resetpass1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_verification);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        final String userid = prefs.getString(sharedpreferences.user_id,null );
        final String emailid=prefs.getString(sharedpreferences.email,null);
        verifycode=(EditText)findViewById(R.id.verifycode);
        verifybutton=(Button)findViewById(R.id.submit);
        password=(EditText)findViewById(R.id.password1);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(in);
            }
        });



        Intent in=getIntent();
        resemail=in.getStringExtra("email1");


        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verifycode.getText().toString().length()==0){
                    verifycode.setError("verification code not entered");
                    verifycode.requestFocus();
                }

                else if(password.getText().toString().length()==0){
                    password.setError("Password not entered");
                    password.requestFocus();
                }
                else if (password.getText().toString().length() < 8) {
                    password.setError("Password must be at least 8 characters.");
                    password.requestFocus();
                }
                else {
                    final String verificationcode=verifycode.getText().toString();
                    final String pass=password.getText().toString();


                    Api.getClient().getRestpassword1(pass,resemail,verificationcode, new Callback<resetpass>() {

                        @Override
                        public void success(resetpass resetpass, retrofit.client.Response response) {

                            resetpass1 = resetpass;


                            if(resetpass.getStatus().equalsIgnoreCase("true")) {

                                Intent in=new Intent(getApplicationContext(), SigninActivity.class);
                                startActivity(in);
                            }
                            else {

                                Toast.makeText(getApplicationContext(),""+resetpass.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {


                        }});
                }
            }
        });

    }
}
