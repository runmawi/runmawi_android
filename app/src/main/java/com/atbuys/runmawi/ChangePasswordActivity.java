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

/*import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;*/


public class ChangePasswordActivity extends AppCompatActivity {


    Button verifybutton;
    EditText password,confirmpass,oldpass;
    private resetpass resetpass1;

    String theme;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);





       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        final String userid = prefs.getString(sharedpreferences.user_id,null );
        final String emailid=prefs.getString(sharedpreferences.email,null);
        theme = prefs.getString(sharedpreferences.theme,null);


        verifybutton=(Button)findViewById(R.id.submit);
        password=(EditText)findViewById(R.id.password1);
        confirmpass=(EditText)findViewById(R.id.confirmpassword1) ;
        oldpass=(EditText)findViewById(R.id.oldpass);


        if( theme.equalsIgnoreCase("1")) {


            setTheme(R.style.AppTheme);


        }

        else {


            setTheme(R.style.darktheme);

        }



        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });


        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(oldpass.getText().toString().length()==0){
                    oldpass.setError("Old password not entered");
                    oldpass.requestFocus();
                }

                else if(password.getText().toString().length()==0){
                    password.setError("Password not entered");
                    password.requestFocus();
                }
                else if(confirmpass.getText().toString().length()==0){
                    confirmpass.setError("Confirm password not entered");
                    confirmpass.requestFocus();
                }
                else if(!password.getText().toString().equals(confirmpass.getText().toString())){
                    confirmpass.setError("Password Not matched");
                    confirmpass.requestFocus();
                }
                else if (password.getText().toString().length() < 8) {
                    password.setError("Password must be at least 8 characters.");
                    password.requestFocus();
                }
                else {


                final String pass=password.getText().toString();
                final String oldpass1=oldpass.getText().toString();


                    Api.getClient().getChangepassword(pass,emailid,oldpass1, new Callback<resetpass>() {


                        @Override
                        public void success(resetpass resetpass, retrofit.client.Response response) {

                            resetpass1 = resetpass;

                            if(resetpass.getStatus().equalsIgnoreCase("true")) {


                                Intent in=new Intent(getApplicationContext(), MyAccountActivity.class);
                                startActivity(in);
                                Toast.makeText(getApplicationContext(),""+resetpass.getMessage(), Toast.LENGTH_LONG).show();

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

    @Override
    public void onBackPressed() {

      finish();
    }
}
