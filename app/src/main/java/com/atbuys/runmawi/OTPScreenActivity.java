package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.chaos.view.PinView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPScreenActivity extends AppCompatActivity {

     PinView pinView;
     Button go;
     String code , mobile;
    private ArrayList<user_data> userdata;
     String xxVal = "10";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {


            setTheme(R.style.darktheme);

        }

        else {


            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_screen);

      //  pinView = (PinView) findViewById(R.id.firstPinView);
        go = (Button) findViewById(R.id.go);

        Intent in = getIntent();
        code = in.getStringExtra("code");
        mobile = in.getStringExtra("mobile");

        userdata = new ArrayList<user_data>();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(pinView.getText().toString().equalsIgnoreCase(code))
                {

                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getMobileLogin(mobile);
                    call.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {


                            if (response.body() != null) {
                                if (response.body().getStatus().equalsIgnoreCase("true")) {

                                    JSONResponse jsonResponse = response.body();
                                    userdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_data()));

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
                                    editor.commit();

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

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incorrect OTP ", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}