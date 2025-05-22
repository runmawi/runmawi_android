package com.atbuys.runmawi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {

    CardView change_password;
    EditText pass, con_pass;
    TextInputLayout textInputLayout;
    String email;
    resetpass resetpass1;
    AlertDialog.Builder builder;
    ImageView back_arrow;
    boolean isPasswordVisible,isPasswordVisible1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        change_password = (CardView) findViewById(R.id.continue1);
        pass = (EditText) findViewById(R.id.password);
        con_pass = (EditText) findViewById(R.id.conform_password);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (pass.getRight() - 172)) {
                        int selection = pass.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvector, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvectorwhite, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        con_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (con_pass.getRight() - 172)) {
                        int selection = con_pass.getSelectionEnd();
                        if (isPasswordVisible1) {
                            // set drawable image
                            con_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvector, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            con_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible1 = false;
                        } else  {
                            // set drawable image
                            con_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.passwordvectorwhite, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            con_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible1 = true;
                        }
                        con_pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().length() == 0 || pass.getText().toString().equalsIgnoreCase(null)) {
                    Toast.makeText(NewPasswordActivity.this, "Password Not Entered", Toast.LENGTH_LONG).show();
                    pass.requestFocus();
                } else if (con_pass.getText().toString().length() == 0 || con_pass.getText().toString().equalsIgnoreCase(null)) {
                    Toast.makeText(NewPasswordActivity.this, "Conform Password Not Entered", Toast.LENGTH_LONG).show();
                    con_pass.requestFocus();
                } else if (!isValidPassword(pass.getText().toString().trim())) {
                    //user_password.setError("Password should be at least 8 characters in length and should include at least one upper case letter, one number, and one special character.");
                    pass.requestFocus();
                    Toast.makeText(NewPasswordActivity.this, "Password should have 8 char,1 upper,1 number and 1 special char", Toast.LENGTH_LONG).show();
                } else if (!(pass.getText().toString().equals(con_pass.getText().toString()))) {
                    Toast.makeText(NewPasswordActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                } else {

                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().updateResetpassword(email,pass.getText().toString());
                    call.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            JSONResponse jsonResponse= response.body();

                            if (response.body() != null) {

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(NewPasswordActivity.this);

                                final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert, null);
                                dialog.setView(customLayout);
                                final AlertDialog alert = dialog.create();
                                alert.show();

                                // Hide after some seconds
                                final Handler handler  = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (alert.isShowing()) {
                                            alert.dismiss();
                                            Toast.makeText(NewPasswordActivity.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                            Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                            startActivity(in);
                                        }
                                    }
                                };
                                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        handler.removeCallbacks(runnable);
                                    }
                                });
                                handler.postDelayed(runnable, 3000);
                            }else{
                                Toast.makeText(getApplicationContext(),""+response.message(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),""+t.getMessage(), Toast.LENGTH_LONG).show();
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
}