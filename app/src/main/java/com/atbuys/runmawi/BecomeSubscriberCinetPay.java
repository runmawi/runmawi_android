package com.atbuys.runmawi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cinetpay.androidsdk.CinetPayActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class BecomeSubscriberCinetPay extends AppCompatActivity {

    private TextInputEditText mAmountView;
    private Button mPayView;
    private AutoCompleteTextView mCurrencyView;
    private View mCardView;
    private TextInputEditText mNameView;
    private TextInputEditText mSurnameView;
    private TextInputEditText mEmailView;
    private TextInputEditText mAddressView;
    private TextInputEditText mPhoneView;
    private TextInputEditText mCityView;
    private TextInputEditText mZipCodeView;

    private String mCurrency,plan,type,amount,username,useremail,password,id,url,api_key,site_id,ccode;

    private List<String> mChannels = new ArrayList<>();
    String name = "";
    String surname = "";
    String email = "";
    String address = "";
    String phone = "";
    String city = "";
    String country = "CI"; // TODO
    String state = ""; // TODO
    String zip_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_become_subscriber_cinet_pay);

        mAmountView = findViewById(R.id.amount);
        mPayView = findViewById(R.id.pay);
        mCurrencyView = findViewById(R.id.currency);
        mCardView = findViewById(R.id.card);
        mNameView = findViewById(R.id.name);
        mSurnameView = findViewById(R.id.surname);
        mEmailView = findViewById(R.id.email);
        mAddressView = findViewById(R.id.address);
        mPhoneView = findViewById(R.id.phone);
        mCityView = findViewById(R.id.city);
        mZipCodeView = findViewById(R.id.zip_code);

        mCurrency = "";

        Intent intent=getIntent();
        plan=intent.getStringExtra("plan");
        amount=intent.getStringExtra("amount");
        username=intent.getStringExtra("username");
        useremail=intent.getStringExtra("email");
        password=intent.getStringExtra("password");
        ccode=intent.getStringExtra("ccode");
        type=intent.getStringExtra("type");
        id=intent.getStringExtra("id");
        url=intent.getStringExtra("url");
        mAmountView.setText(amount);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency, R.layout.currency_item);

        mCurrencyView.setAdapter(adapter);

        mCurrencyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrency = String.valueOf(adapter.getItem(position));
            }
        });

        mPayView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //String amount = mAmountView.getText().toString().replace(" ", "");

                if (mCurrency.isEmpty()) {
                    Toast.makeText(BecomeSubscriberCinetPay.this, "Please select currency", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mChannels.isEmpty()) {
                    Toast.makeText(BecomeSubscriberCinetPay.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mChannels.contains("CREDIT_CARD")) {
                    name = mNameView.getText().toString();
                    if (name.isEmpty()) {
                        mNameView.setError("Please enter an Name");
                        return;
                    }
                    surname = mSurnameView.getText().toString();
                    if (surname.isEmpty()) {
                        mSurnameView.setError("Please enter an Name");
                        return;
                    }
                    email = mEmailView.getText().toString();
                    if (email.isEmpty()) {
                        mEmailView.setError("Please enter an mail");
                        return;
                    }
                    address = mAddressView.getText().toString();
                    if (address.isEmpty()) {
                        mAddressView.setError("Please enter an address");
                        return;
                    }
                    phone = mPhoneView.getText().toString();
                    if (phone.isEmpty()) {
                        mPhoneView.setError("Please enter an mobile number");
                        return;
                    }
                    city = mCityView.getText().toString();
                    if (city.isEmpty()) {
                        mCityView.setError("Please enter an city");
                        return;
                    }
                    zip_code = mZipCodeView.getText().toString();
                    if (zip_code.isEmpty()) {
                        mZipCodeView.setError("Please enter an post code");
                        return;
                    }
                }
                Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                call.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();
                        ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));

                        for (int j=0;j<payment_settingslist.size();j++){
                            if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")){
                                api_key =payment_settingslist.get(j).getCinetPay_APIKEY();
                                site_id =payment_settingslist.get(j).getCinetPay_SITE_ID();
                                //Toast.makeText(BecomeSubscriberCinetPay.this, "sideid-> "+site_id, Toast.LENGTH_SHORT).show();

                                //String api_key = "17747086006413350a27b974.55294931"; // TODO A remplacer par votre clé API
                                //String site_id = "751850"; // TODO A remplacer par votre Site ID

                                String transaction_id = String.valueOf(new Date().getTime());
                                String description = "Purchase";

                                StringBuilder sb = new StringBuilder();

                                int i = 0;

                                for (String channel : mChannels) {
                                    sb.append(channel);
                                    if (i < mChannels.size() - 1) {
                                        sb.append(',');
                                    }
                                    i++;
                                }

                                String channels = sb.toString();


                                Intent intent = new Intent(BecomeSubscriberCinetPay.this, MyCinetPayActivity.class);

                                intent.putExtra(CinetPayActivity.KEY_API_KEY, api_key);
                                intent.putExtra(CinetPayActivity.KEY_SITE_ID, site_id);
                                intent.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, transaction_id);
                                intent.putExtra(CinetPayActivity.KEY_AMOUNT, Integer.valueOf(amount));
                                intent.putExtra(CinetPayActivity.KEY_CURRENCY, mCurrency);
                                intent.putExtra(CinetPayActivity.KEY_DESCRIPTION, description);
                                intent.putExtra(CinetPayActivity.KEY_CHANNELS, channels);
                                intent.putExtra("plan",plan);
                                intent.putExtra("username",username);
                                intent.putExtra("ccode",ccode);
                                intent.putExtra("email",useremail);
                                intent.putExtra("password",password);
                                intent.putExtra("type",type);
                                intent.putExtra("id", id);
                                intent.putExtra("url", url);

                                /*if (mChannels.contains("CREDIT_CARD")) {
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_NAME, name);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_SURNAME, surname);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_EMAIL, email);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_ADDRESS, address);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_PHONE_NUMBER, phone);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_CITY, city);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_COUNTRY, country);
                                    intent.putExtra(CinetPayActivity.KEY_CUSTOMER_ZIP_CODE, zip_code);
                                }*/

                                startActivity(intent);


                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                    }
                });

            }
        });

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox_mobile_money:
                if (checked) {
                    mChannels.add("MOBILE_MONEY");
                } else {
                    mChannels.remove("MOBILE_MONEY");
                }
                break;
            case R.id.checkbox_wallet:
                if (checked) {
                    mChannels.add("WALLET");
                } else {
                    mChannels.remove("WALLET");
                }
                break;
            case R.id.checkbox_card:
                if (checked) {
                    mChannels.add("CREDIT_CARD");
                    mCardView.setVisibility(View.GONE);
                } else {
                    mChannels.remove("CREDIT_CARD");
                    mCardView.setVisibility(View.GONE);
                }
                break;
        }
    }
}