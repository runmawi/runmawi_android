package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    CardView verify;
    EditText otp1,otp2,otp3,otp4;
    ImageView back_arrow;
    TextView via;
    String resemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        verify=(CardView) findViewById(R.id.verify);
        otp1=(EditText) findViewById(R.id.otp1);
        otp2=(EditText) findViewById(R.id.otp2);
        otp3=(EditText) findViewById(R.id.otp3);
        otp4=(EditText) findViewById(R.id.otp4);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        via=(TextView)findViewById(R.id.via);

        Intent in=getIntent();
        resemail=in.getStringExtra("email1");
        otp1.requestFocus();
        String split[]=resemail.split("");

        via.setText(split[0]+split[1]+split[2]+split[3]+"******"+split[split.length-3]+split[split.length-2]+split[split.length-1]);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otp1.getText().toString()==null||otp2.getText().toString()==null||otp3.getText().toString()==null||otp4.getText().toString()==null||otp1.getText().toString().isEmpty()||otp2.getText().toString().isEmpty()||otp3.getText().toString().isEmpty()||otp4.getText().toString().isEmpty())
                {
                    Toast.makeText(VerifyActivity.this, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                } else{
                    String verificationcode=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();

                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().verifyResetpassword(resemail,verificationcode);
                    call.enqueue(new Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                            JSONResponse jsonResponse= response.body();

                            if (response.body() != null) {

                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(VerifyActivity.this, NewPasswordActivity.class);
                                intent.putExtra("email",resemail);
                                startActivity(intent);
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



        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp1.getText().toString().length()==1){
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp2.getText().toString().length()==1){
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp3.getText().toString().length()==1){
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}