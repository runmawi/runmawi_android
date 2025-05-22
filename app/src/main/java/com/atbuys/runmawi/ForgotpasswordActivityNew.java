package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import retrofit.Callback;
import retrofit.RetrofitError;


public class ForgotpasswordActivityNew extends AppCompatActivity {

    EditText email;
    Button submit;
    private String resemail;
    resetpass resetpass1;
    ImageView back_arrow;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpasswordnew);

        email=(EditText)findViewById(R.id.editText2);
        submit=(Button) findViewById(R.id.buttoncontinue);
        back_arrow=(ImageView)findViewById(R.id.forgot_backarrow);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().length() == 0) {
                    email.setError("Email not entered");
                    email.requestFocus();
                } else {
                    final String email1 = email.getText().toString();
                    Api.getClient().getRestpassword(email.getText().toString(), new Callback<resetpass>() {

                        @Override
                        public void success(resetpass resetpass, retrofit.client.Response response) {

                            resetpass1 = resetpass;

                            if (resetpass1.getStatus().equalsIgnoreCase("true")) {

                                Intent in = new Intent(getApplicationContext(), VerifyActivity.class);
                                in.putExtra("email1", resetpass1.getEmail());
                                startActivity(in);
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(),""+resetpass.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
    startActivity(intent);
    }
}
