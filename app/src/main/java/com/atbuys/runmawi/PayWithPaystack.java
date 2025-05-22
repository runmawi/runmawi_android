/*
package com.atbuys.runmawi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.ephod.pentecost.library.paystack.PaymentView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayWithPaystack extends AppCompatActivity {

    PaymentView paymentView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_paystack);

        paymentView=findViewById(R.id.paymentView);
        //button = paymentView.getPayButton();

       */
/* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentView.showLoader();
            }
        });*//*


        String[] arraySpinner = new String[]{
                "Access Bank" , "Citibank", "Diamond Bank", "Dynamic Standard Bank", "Ecobank Nigeria", "Fidelity Bank Nigeria",
                "First Bank of Nigeria", "First City Monument Bank", "Guaranty Trust Bank", "Heritage Bank Plc","Jaiz Bank",
                "Keystone Bank Limited", "Providus Bank Plc", "Skye Bank", "Stanbic IBTC Bank Nigeria Limited", "Standard Chartered Bank",
                "Sterling Bank", "Suntrust Bank Nigeria Limited", "Union Bank of Nigeria", "United Bank for Africa", "Unity Bank Plc",
                "Wema Bank", "Zenith Bank"
        };

        //paymentView.setPAYMENT_FROM(PAYMENT_FORM_TYPE.CARD);
        paymentView.setBanksSpinner(arraySpinner);
        paymentView.setPentecostBackgroundColor(getResources().getColor(R.color.red));

        //paymentView.getHeaderContentView().setTextColor(getResources().getColor(R.color.cardview_dark_background));


        paymentView.setBillContent("₦1234");

        paymentView.setChargeListener(new PaymentView.ChargeListener() {
            @Override
            public void onChargeCard() {
                //Use Paystack's SDK chargeCard or whatever you want really.
                Toast.makeText(PayWithPaystack.this, "onChargeCard", Toast.LENGTH_SHORT).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.paystack.co/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestInterface request = retrofit.create(RequestInterface.class);

            }
            @Override
            public void onChargeBank() {
                //send the bank details to your server
                //which then sends to Paystack's pay_with_bank api
                Toast.makeText(PayWithPaystack.this, "onChargeBank", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(PayWithPaystack.this, "onSuccess", Toast.LENGTH_SHORT).show();
            }

        });
    }
}*/
