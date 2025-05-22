package com.atbuys.runmawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class PayPerViewPaymentActivity extends AppCompatActivity {

    private Stripe stripe;
    PaymentMethodCreateParams params;
    Button paynow;
    CardMultilineWidget cardMultilineWidget;
    String username,email,password,country,state,city,plan;
    registerresponse regrespone1;
    String skip="0",py_id;
    String user_id,user_role,video_id,urll;
    Addpayperview addpayperview;
    CheckBox check;
    private ArrayList<payment_settings> payment_settingslist;
    String key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payperview_payment_page);

        cardMultilineWidget = findViewById(R.id.card_input_widget);

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);

        Intent in=getIntent();
        video_id =in.getStringExtra("id");
        urll = in.getStringExtra("url");




        paynow =(Button)findViewById(R.id.save_payment) ;

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));

                if( payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0)))
                {

                    key=payment_settingslist.get(0).getTest_publishable_key();



                }
                else
                {
                    key=payment_settingslist.get(0).getLive_publishable_key();

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Toast.makeText(getApplicationContext(),""+key, Toast.LENGTH_SHORT).show();
                params = cardMultilineWidget.getPaymentMethodCreateParams();

                if (params == null) {
                    return;
                }
                stripe = new Stripe(getApplicationContext(), key);

                stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                    @Override
                    public void onSuccess(@NotNull PaymentMethod paymentMethod) {


                        Log.d("pdfdwef",paymentMethod.id);
                        Log.d("pdfdwefsds",video_id);
                        Log.d("asdsdsdd",user_id);

                      py_id=paymentMethod.id;


                      Log.d("hbjhbhbhb",py_id);


                        Api.getClient().getAddPayperView(user_id, video_id, py_id,"stripe", new Callback<Addpayperview>() {

                            @Override
                            public void success(Addpayperview addpayperview1, Response response) {

                                addpayperview=addpayperview1;
                                Toast.makeText(getApplicationContext(),addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                                Intent in=new Intent(getApplicationContext(), OnlinePlayerActivity.class);
                                in.putExtra("id",video_id);
                                in.putExtra("url",urll);
                                in.putExtra("data","videos");
                                in.putExtra("ads","");
                                startActivity(in);

                            }

                            @Override
                            public void failure(RetrofitError error) {

                                Toast.makeText(getApplicationContext(),"check your internet connction", Toast.LENGTH_LONG).show();

                            }
                        });
                    }

                    @Override
                    public void onError(@NotNull Exception e) {
                        Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }
}
