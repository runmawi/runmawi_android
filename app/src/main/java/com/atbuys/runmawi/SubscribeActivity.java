package com.atbuys.runmawi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cinetpay.androidsdk.CinetPayActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;
import com.atbuys.runmawi.Config.Config;
import com.atbuys.runmawi.Remote.IpService;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscribeActivity extends AppCompatActivity implements PaymentResultListener {

    Button paynow;
    String user_id, theme;
    paymentresponse signUpResponsesData;
    private Object Context;
    private RecyclerView recyclerView;
    private ArrayList<plans> data;
    private ArrayList<respond> subresponse;
    private PlansAdapter adapter;
    public static Dialog dialog;
    BottomSheetDialog paydunya_dialog;
    CardView paydunya_pay_now;
    EditText paydunya_email_address;
    registerresponse registerresponse1;
    becomesub becomesub1;
    RelativeLayout pro;
    LinearLayout page;
    String subscriptionid;
    //  ImageView logo;
    Payment_settingsAdopter payment_settingsAdopter;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;
    String plan;
    CheckBox checkBox;
    String x, y, z;
    String username, email, password, state, city;
    IsPayment isPayment;
    ImageView logo1, backarrow;
    private ArrayList<Site_theme_setting> site_theme_setting;
    private ArrayList<user_details> user_detailsdata;
    private ArrayList<plan> plan_id_list;
    IpService mService;
    String ipAddress;
    CardView pay_now;
    ProgressDialog progressDialog;
    TextView skip;
    @SuppressLint("StaticFieldLeak")
    public static TextView paymenttype;
    String token;
    becomesub becomesub;
    String currencySymbol = "";
    String country = "", amount;
    String credentials, accessToken;
    ProgressBar plan_progress;

    String planamt, planname, planidd, currency;
    int click_count;
    private ArrayList<payment_settings> payment_settingslist;
    private ArrayList<active_payment_settings> active_payment_settingslist;
    private ArrayList<settings> settings;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private LocationAddressResultReceiver addressResultReceiver;
    private Location currentLocation;
    private LocationCallback locationCallback;
    int count = 0;
    int PAYPAL_REQUEST_CODE = 7777;
    public static PayPalConfiguration config;
    PaydunyaSetup setup;
    PaydunyaCheckoutStore store;
    PaydunyaCheckoutInvoice invoice;


   /* private String API_KEY = "API_KEY";
    private String SITE_ID = "SITE_ID";
    private MerchantService CINETPAY_MERCHANT = new MerchantService(API_KEY, SITE_ID);
    private MerchantService CINETPAY_MERCHANT_WITH_URL_NOTIFICATION =
            new MerchantService(API_KEY, SITE_ID, "https://www.url_notification.com/index.php", true);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_payment_);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(Config.PAYPAL_CLIENT_ID);
        progressDialog = new ProgressDialog(this);
        initViews(count);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        pro = (RelativeLayout) findViewById(R.id.pro);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        page = (LinearLayout) findViewById(R.id.page);
        pay_now = (CardView) findViewById(R.id.proceedtopay);
        skip = (TextView) findViewById(R.id.skipp);
        skip.setVisibility(View.GONE);
        logo1 = (ImageView) findViewById(R.id.logo);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        paymenttype = (TextView) findViewById(R.id.payment1);
        plan_progress = (ProgressBar) findViewById(R.id.plan_progress);


        //  planid =(TextView) findViewById(R.id.planid);

        site_theme_setting = new ArrayList<>();
        settings = new ArrayList<settings>();
        payment_settingslist = new ArrayList<payment_settings>();
        active_payment_settingslist = new ArrayList<active_payment_settings>();
        subresponse = new ArrayList<respond>();
        plan_id_list = new ArrayList<plan>();
        // token = FirebaseInstanceId.getInstance().getToken();

        Call<JSONResponse> currencyapi = ApiClient.getInstance1().getApi().getCurrencySetting();
        currencyapi.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                ArrayList<Currency_Setting> currencySettings = new ArrayList<>(Arrays.asList(jsonResponse.getCurrency_Setting()));

                if (currencySettings.get(0).getEnable_multi_currency().equalsIgnoreCase("1")) {
                    addressResultReceiver = new LocationAddressResultReceiver(new Handler());
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(SubscribeActivity.this);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            currentLocation = locationResult.getLocations().get(0);
                            getAddress();
                        }
                    };
                    startLocationUpdates();
                    //statusCheck();

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });


        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getthemeSettings();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                site_theme_setting = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = site_theme_setting.get(0).getImage_url();
                Picasso.get().load(x).into(logo1);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        Api.getClient().getIsPayment(
                new Callback<IsPayment>() {
                    @Override
                    public void success(IsPayment latestenablemovie1, Response response) {
                        isPayment = latestenablemovie1;
                        // checkBox.setVisibility(View.VISIBLE);
                        if (isPayment.getIs_payment().equalsIgnoreCase(String.valueOf(0))) {


                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(SubscribeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        final String user_role = prefs.getString(sharedpreferences.role, null);
        theme = prefs.getString(sharedpreferences.theme, null);

        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();
                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        username = user_detailsdata.get(0).getName();
                        email = user_detailsdata.get(0).getEmail();

                    }

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }

        });

    }

    private void initViews(int num) {

        if (num <= 1) {
            recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            loadJSON();
        }
        count++;

    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStripeOnetime();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getPlans()));
                plan_progress.setVisibility(View.GONE);
                if (data.size() == 0) {
                    Toast.makeText(SubscribeActivity.this, "Plans not found", Toast.LENGTH_SHORT).show();
                } else {
                    pay_now.setVisibility(View.VISIBLE);
                }
                currency = jsonResponse.getCurrency_Symbol();
                if (country.isEmpty() || country == null) {
                    adapter = new PlansAdapter(data, currency, "off", SubscribeActivity.this);
                    progressDialog.hide();
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter = new PlansAdapter(data, country, "on", SubscribeActivity.this);
                    progressDialog.hide();
                    recyclerView.setAdapter(adapter);
                }

                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(SubscribeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                if (data.size() > position) {
                                    if (data.get(position) != null) {

                                        planname = data.get(position).getPlans_name();
                                        planamt = data.get(position).getPrice();
                                        click_count = 1;
                                    }
                                }
                            }
                        }));


                pay_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (click_count == 1) {

                            /*progressDialog.setMessage("Please Wait");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.setMax(100);
                            progressDialog.show();*/

                            dialog = new Dialog(SubscribeActivity.this);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialogbox);
                            dialog.show();

                            RadioButton radioButton = (RadioButton) dialog.findViewById(R.id.termsandcon);
                            RadioButton radioButton1 = (RadioButton) dialog.findViewById(R.id.radiorazor);
                            RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.radiopaypal);
                            RadioButton radioButton3 = (RadioButton) dialog.findViewById(R.id.radiostripe);
                            RadioButton radioButton4 = (RadioButton) dialog.findViewById(R.id.termsandcons);
                            Button btndialog = (Button) dialog.findViewById(R.id.proceed);
                            RecyclerView wes = (RecyclerView) dialog.findViewById(R.id.wes);

                            Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                            callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                @Override
                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                    JSONResponse jsonResponse = response.body();

                                    wes.setHasFixedSize(true);
                                    wes.setLayoutManager(new GridLayoutManager(SubscribeActivity.this, 1));
                                    active_payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getActive_payment_settings()));
                                    active_payment_settingsAdopter = new Active_Payment_settingsAdopter(active_payment_settingslist);
                                    wes.setAdapter(active_payment_settingsAdopter);

                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                    Log.d("Error: ", t.getMessage());
                                }
                            });

                            wes.addOnItemTouchListener(
                                    new RecyclerItemClickListener(SubscribeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            if (active_payment_settingslist.size() > position) {
                                                if (active_payment_settingslist.get(position) != null) {
                                                    paymenttype.setText(active_payment_settingslist.get(position).getPayment_type());
                                                }

                                            }
                                        }
                                    }));


                            btndialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.cancel();
                                    progressDialog.setMessage("Please Wait");
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setMax(100);
                                    progressDialog.show();

                                    if (paymenttype.getText().toString().equalsIgnoreCase("stripe")) {

                                        progressDialog.hide();
                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "stripe");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse1 = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();

                                                Intent in = new Intent(getApplicationContext(), BecomeSubscriberPaymentActivity.class);
                                                in.putExtra("plan", planidd);
                                                in.putExtra("plan_name", planname);
                                                in.putExtra("price", plan_id_list.get(0).getPrice());
                                                startActivity(in);
                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Log.d("Error", t.getMessage());
                                                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });


                                    } else if (paymenttype.getText().toString().equalsIgnoreCase("Paydunya")) {

                                        progressDialog.hide();
                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "Paydunya");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse1 = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();
                                                View view = getLayoutInflater().inflate(R.layout.paydunya_payment_page, null);
                                                paydunya_dialog = new BottomSheetDialog(SubscribeActivity.this);
                                                paydunya_dialog.setContentView(view);
                                                paydunya_dialog.show();

                                                ImageView cross = view.findViewById(R.id.cross);
                                                TextView plan_name = view.findViewById(R.id.plan_name);
                                                TextView plan_price = view.findViewById(R.id.plan_price);
                                                paydunya_email_address = view.findViewById(R.id.email_address);
                                                paydunya_pay_now = view.findViewById(R.id.pay_now);
                                                cross.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        paydunya_dialog.cancel();
                                                    }
                                                });
                                                plan_name.setText(planname);
                                                plan_price.setText(planamt + " " + currency);
                                                paydunya_pay_now.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (paydunya_email_address.getText().toString().isEmpty() || paydunya_email_address.getText().toString().trim().isEmpty()) {
                                                            Toast.makeText(SubscribeActivity.this, "EMAIL ADDRESS not entered", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                    JSONResponse jsonResponse = response.body();
                                                                    ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));

                                                                    for (int i = 0; i < payment_settingslist.size(); i++) {
                                                                        if (payment_settingslist.get(i).getPayment_type().equalsIgnoreCase("Paydunya")) {

                                                                            if (payment_settingslist.get(i).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                                setup = new PaydunyaSetup();
                                                                                setup.setMasterKey(payment_settingslist.get(i).getPaydunya_masterkey());
                                                                                setup.setPrivateKey(payment_settingslist.get(i).getPaydunya_test_PrivateKey());
                                                                                setup.setPublicKey(payment_settingslist.get(i).getPaydunya_test_PublicKey());
                                                                                setup.setToken(payment_settingslist.get(i).getPaydunya_test_token());
                                                                                setup.setMode("test");
                                                                                store = new PaydunyaCheckoutStore();
                                                                                store.setName("Runmawi");
                                                                                invoice = new PaydunyaCheckoutInvoice(setup, store);
                                                                                invoice.addItem(planname, 1, Double.parseDouble(planamt), Double.parseDouble(planamt));
                                                                                invoice.setTotalAmount(Double.parseDouble(planamt));

                                                                                new MyTask().execute(paydunya_email_address.getText().toString(), planamt);

                                                                            } else {
                                                                                setup = new PaydunyaSetup();
                                                                                setup.setMasterKey(payment_settingslist.get(i).getPaydunya_masterkey());
                                                                                setup.setPrivateKey(payment_settingslist.get(i).getPaydunya_live_PrivateKey());
                                                                                setup.setPublicKey(payment_settingslist.get(i).getPaydunya_live_PublicKey());
                                                                                setup.setToken(payment_settingslist.get(i).getPaydunya_live_token());
                                                                                setup.setMode("live");
                                                                                store = new PaydunyaCheckoutStore();
                                                                                store.setName("Runmawi");
                                                                                invoice = new PaydunyaCheckoutInvoice(setup, store);
                                                                                invoice.addItem(planname, 1, Double.parseDouble(planamt), Double.parseDouble(planamt));
                                                                                invoice.setTotalAmount(Double.parseDouble(planamt));

                                                                                new MyTask().execute(paydunya_email_address.getText().toString(), planamt);
                                                                            }
                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Log.d("Error", t.getMessage());
                                                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } else if (paymenttype.getText().toString().equalsIgnoreCase("razorpay")) {

                                        progressDialog.hide();
                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "razorpay");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse1 = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();

                                                Log.w("Runmawi_test","planid: "+planidd+" plan: "+planname);
                                                Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getSubscriptionDeatails(planidd);
                                                callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();
                                                        subresponse = new ArrayList<>(Arrays.asList(jsonResponse.getRespond()));
                                                        subscriptionid = subresponse.get(0).getSubscriptionid();
                                                        startPayment(subscriptionid, "", plan);

                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                        Log.d("Error", t.getMessage());
                                                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Log.d("Error", t.getMessage());
                                                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });


                                    } else if (paymenttype.getText().toString().equalsIgnoreCase("paystack")) {

                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "paystack");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse1 = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();

                                                Intent in = new Intent(getApplicationContext(), BecomeSubscriberPaystack.class);
                                                in.putExtra("plan", planidd);
                                                in.putExtra("amount", plan_id_list.get(0).getPrice());
                                                in.putExtra("username", username);
                                                in.putExtra("email", email);
                                                in.putExtra("password", "");
                                                in.putExtra("ccode", "");
                                                in.putExtra("mobile", "");
                                                in.putExtra("ref_code", "");
                                                in.putExtra("token", token);
                                                in.putExtra("type", "free");
                                                startActivity(in);
                                                progressDialog.hide();
                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Log.d("Error", t.getMessage());
                                                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });


                                    } else if (paymenttype.getText().toString().equalsIgnoreCase("paypal")) {
                                        progressDialog.hide();
                                        //processPayment();
                                        //getPayment();
                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "paypal");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                                                JSONResponse jsonResponse = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();
                                                paypalSubscribe(planidd);
                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } else if (paymenttype.getText().toString().equalsIgnoreCase("cinetpay")) {

                                        progressDialog.hide();
                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "Cinetpay");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response1) {

                                                JSONResponse jsonResponse1 = response1.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));

                                                Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                        JSONResponse jsonResponse2 = response2.body();
                                                        ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                        for (int j = 0; j < payment_settingslist.size(); j++) {
                                                            if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {
                                                                //Toast.makeText(SubscribeActivity.this, "q->"+username+" "+email+plan_id_list.get(0).getPlan_id(), Toast.LENGTH_SHORT).show();

                                                                Intent in = new Intent(getApplicationContext(), MyCinetPayActivity.class);
                                                                in.putExtra("plan", plan_id_list.get(0).getPlan_id());
                                                                //in.putExtra("amount", plan_id_list.get(0).getPrice());
                                                                in.putExtra("username", username);
                                                                in.putExtra("email", email);
                                                                in.putExtra("password", "");
                                                                in.putExtra("ccode", "");
                                                                in.putExtra("ref_code", "");
                                                                in.putExtra("token", token);
                                                                in.putExtra("type", "free");
                                                                in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                                in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                                in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                                in.putExtra(CinetPayActivity.KEY_AMOUNT, plan_id_list.get(0).getPrice());
                                                                in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                                in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Subscription");
                                                                in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                                startActivity(in);

                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Log.d("Error", t.getMessage());
                                                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });


                                    }


                                }
                            });

                            progressDialog.hide();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SubscribeActivity.this);
                            builder.setTitle("Plan!");
                            builder.setMessage("Please select the plan..");
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                            builder.show();

                        }
                    }

                });
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }

        });
    }
    /*public static String createPaymentLink(String masterKey, String privateKey, String token, String mode) {
        try {
            // Paydunya API endpoint
            String apiUrl = mode.equals("live") ? "https://app.paydunya.com/api/v1/checkout-invoice/create" : "https://app.sandbox.paydunya.com/api/v1/checkout-invoice/create";

            // Payment details
            Map<String, String> params = new HashMap<>();
            params.put("invoice", "INVOICE-12345");
            params.put("total_amount", "10000"); // Amount in cents
            params.put("description", "Test Payment");
            params.put("callback_url", "YOUR_CALLBACK_URL");
            params.put("return_url", "YOUR_RETURN_URL");
            params.put("cancel_url", "YOUR_CANCEL_URL");
            params.put("name", "John Doe");
            params.put("email", "john@example.com");
            params.put("phone", "1234567890");

            // Construct payload
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(param.getKey());
                postData.append('=');
                postData.append(param.getValue());
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            // Set up HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((masterKey + ":" + privateKey).getBytes()));
            conn.setRequestProperty("token", token);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postDataBytes);

            // Read response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            wr.close();
            rd.close();

            // Extract payment link from response
            String jsonResponse = response.toString();
            // Parse JSON response and extract payment link
            // Example: JSONObject jsonObject = new JSONObject(jsonResponse);
            // String paymentLink = jsonObject.getString("response_data");

            // For now, return the JSON response as the payment link
            return jsonResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public void paypalSubscribe(String plan_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.paypal.com/")// for live-> https://api.paypal.com/  // for sandbox-> https://api.sandbox.paypal.com/

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);

        Call<JSONResponse> call_sec = ApiClient.getInstance1().getApi().getPaymentDetails();
        call_sec.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response1) {

                JSONResponse jsonResponse1 = response1.body();
                ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse1.getPayment_settings()));

                for (int i = 0; i < payment_settingslist.size(); i++) {
                    if (payment_settingslist.get(i).getPayment_type().equalsIgnoreCase("PayPal")) {
                        String clientId = payment_settingslist.get(i).getLive_paypal_username();
                        String clientSecret = payment_settingslist.get(i).getLive_paypal_password();
                        credentials = clientId + ":" + clientSecret;
                        //Toast.makeText(SubscribeActivity.this, "credentials: "+credentials, Toast.LENGTH_SHORT).show();

                        String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        RequestQueue queue = Volley.newRequestQueue(SubscribeActivity.this);
                        String url = "https://api-m.paypal.com/v1/oauth2/token";
                        //for sandbox-> https://api-m.sandbox.paypal.com/v1/oauth2/token
                        //for live-> https://api-m.paypal.com/v1/oauth2/token

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                response -> {
                                    // Parse the response to get the access token
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        accessToken = jsonResponse.getString("access_token");
                                        //Toast.makeText(SubscribeActivity.this, "deta> "+jsonResponse, Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(SubscribeActivity.this, "accessToken: "+accessToken, Toast.LENGTH_SHORT).show();

                                        PaypalSubscription paypalSubscription = new PaypalSubscription(plan_id);
                                        Call<PaypalSubscription> call_sub = request.PaypalSubscription("Bearer " + accessToken, paypalSubscription);
                                        call_sub.enqueue(new retrofit2.Callback<PaypalSubscription>() {
                                            @Override
                                            public void onResponse(Call<PaypalSubscription> call, retrofit2.Response<PaypalSubscription> response) {

                                                if (response.isSuccessful()) {

                                                    ArrayList<links> links = new ArrayList<>(Arrays.asList(response.body().getLinks()));

                                                    for (int i = 0; i < links.size(); i++) {
                                                        if (links.get(i).getRel().equalsIgnoreCase("approve")) {

                                                            Intent intent = new Intent(SubscribeActivity.this, WebView.class);
                                                            intent.putExtra("type", "free");
                                                            intent.putExtra("id", plan_id);
                                                            intent.putExtra("username", "");
                                                            intent.putExtra("email", "");
                                                            intent.putExtra("password", "");
                                                            intent.putExtra("ccode", "");
                                                            intent.putExtra("mobile", "");
                                                            intent.putExtra("url", links.get(i).getHref());
                                                            startActivity(intent);
                                                        }
                                                    }

                                                } else {
                                                    Toast.makeText(SubscribeActivity.this, "Token issue: " + response, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PaypalSubscription> call, Throwable t) {
                                                Toast.makeText(SubscribeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(SubscribeActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                error -> {
                                    // Handle error
                                    Log.e("AccessTokenError", "Error getting access token: " + error.toString());
                                    Toast.makeText(SubscribeActivity.this, "AccessTokenError: " + error.toString(), Toast.LENGTH_SHORT).show();
                                }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Authorization", "Basic " + base64Credentials);
                                headers.put("Content-Type", "application/x-www-form-urlencoded");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/x-www-form-urlencoded";
                            }

                            @Override
                            public byte[] getBody() {
                                return "grant_type=client_credentials".getBytes();
                            }
                        };
                        queue.add(stringRequest);


                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getPayment() {

        Call<JSONResponse> req = ApiClient.getInstance1().getApi().getSettings();
        req.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();

                settings = new ArrayList<>(Arrays.asList(jsonResponse.getSettings()));
                amount = settings.get(0).getPpv_price();
                PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(SubscribeActivity.this, PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            PaymentConfirmation confirm = null;
            try {
                confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            } catch (Exception e) {

            }

            if (confirm != null) {

                try {
                    String paymentDetail = confirm.toJSONObject().toString();
                    JSONObject payObj = new JSONObject(paymentDetail);
                    String payID = payObj.getJSONObject("response").getString("id");
                    String state = payObj.getJSONObject("response").getString("state");
                    Toast.makeText(SubscribeActivity.this, "Payment " + state + "\n with payment id is " + payID + "\n response  " + confirm.toJSONObject().toString(), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    Toast.makeText(SubscribeActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(SubscribeActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(SubscribeActivity.this, "Invalid Payment", Toast.LENGTH_LONG).show();
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(SubscribeActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
        } else if (requestCode == 200 || resultCode == 200) {
            startLocationUpdates();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startPayment(String price, String displayName, String plan) {
        Intent in = getIntent();
        final String username = in.getStringExtra("username");
        //final String email = in.getStringExtra("email");

//        double a = Double.parseDouble(price);
        // double payment = a*100;
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", price);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://runmawi.com/content/uploads/settings/5f8d4d2f932bf-c-l.png");
            options.put("currency", "INR");
            options.put("subscription_id", price);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", "");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void onPaymentSuccess(String razorpayPaymentID) {

        Api.getClient().getSub(subscriptionid, user_id, new Callback<becomesub>() {

            @Override
            public void success(becomesub becomesub1, Response response) {

                becomesub = becomesub1;

                Toast.makeText(getApplicationContext(), "" + becomesub1.getMessage(), Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                editor.putBoolean(sharedpreferences.login, true);
                editor.putString(sharedpreferences.user_id, user_id);
                editor.putString(sharedpreferences.role, "subscriber");
                editor.apply();
                editor.commit();
                Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                startActivity(in);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Payment Store Issue: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment Error: " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    private void startLocationUpdates() {
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayLocationSettingsRequest(SubscribeActivity.this);
        } else {

            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        if (!Geocoder.isPresent()) {
            Toast.makeText(SubscribeActivity.this, "Can't find current address, ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
    int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //startLocationUpdates();
                statusCheck();
            } else {
                Toast.makeText(this, "Location permission not granted, " + "restart the app if you want the feature", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == 0) {
                Log.d("Address", "Location null retrying");
                getAddress();
            }
            if (resultCode == 1) {
                //Toast.makeText(SubscribeActivity.this, "Address not found, ", Toast.LENGTH_SHORT).show();
            }
            String currentAdd = resultData.getString("address_result");
            showResults(currentAdd);
        }
    }


    private void showResults(String currentAdd) {
        //Toast.makeText(SubscribeActivity.this, "symbol: "+getSymbol(currentAdd), Toast.LENGTH_SHORT).show();
        //currencySymbol = getSymbol(currentAdd);
        country = currentAdd;
        initViews(count);
        //Toast.makeText(SubscribeActivity.this, "symbol: "+currentAdd, Toast.LENGTH_SHORT).show();
        //getSymbol(currentAdd);
    }

    public static String getSymbol(String countryCode) {
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (int i = 0; i < availableLocales.length; i++) {
            if (availableLocales[i].getCountry().equalsIgnoreCase(countryCode))
                return Currency.getInstance(availableLocales[i]).getSymbol();
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        } catch (Exception e) {

        }

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            displayLocationSettingsRequest(SubscribeActivity.this);

        } else {
            startLocationUpdates();
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SubscribeActivity.this, 200);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 || resultCode == 200) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Kindly enable the location if you want the feature", Toast.LENGTH_SHORT).show();
        }

    }*/

    private class MyTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... params) {

            String email_ou_numero_du_client_paydunya = params[0];
            Double montant_a_transferer = Double.valueOf(params[1]);
            try {
                PaydunyaDirectPay direct_pay = new PaydunyaDirectPay(setup);
                if (direct_pay.creditAccount(email_ou_numero_du_client_paydunya, montant_a_transferer)) {
                    Log.w("direct_pay", "if: " + direct_pay.getStatus() + " trac_ID: " + direct_pay.getTransactionId());
                    return direct_pay.getStatus() + ":" + direct_pay.getTransactionId();
                } else {
                    Log.w("direct_pay", "else: " + direct_pay.getStatus() + direct_pay.getResponseText());
                    return direct_pay.getStatus() + ":" + direct_pay.getResponseText();
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.exception = e;
                Log.w("direct_pay", "catch: " + e.getMessage());
                return "Error" + ":" + e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            paydunyaResponse(result);
        }

    }

    public void paydunyaResponse(String result) {

        String[] splited = result.split(":");

        if (splited[0].equalsIgnoreCase("success")) {
            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().paydunyaBecomeSubscriber(splited[1], user_id, planidd, "completed", "android mobile");
            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    user user = jsonResponse.getUser();
                    if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                        paydunya_dialog.cancel();
                        Toast.makeText(SubscribeActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                        editor.putBoolean(sharedpreferences.login, true);
                        editor.putString(sharedpreferences.user_id, user_id);
                        editor.putString(sharedpreferences.role, user.getRole());
                        editor.apply();
                        editor.commit();
                        Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                        startActivity(in);
                    } else {
                        Toast.makeText(SubscribeActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                }
            });
        } else if (splited[0].equalsIgnoreCase("fail")) {
            paydunya_email_address.setError(splited[1]);
        } else {
            paydunya_email_address.setError(result);
        }
    }

}