package com.atbuys.runmawi;

import static com.atbuys.runmawi.SignupActivityNew.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cinetpay.androidsdk.CinetPayActivity;
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
import com.atbuys.runmawi.ipmodel.IP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupSubscribeActivity extends AppCompatActivity implements PaymentResultListener {


    Button paynow;
    String user_id, theme;
    paymentresponse signUpResponsesData;
    private Object Context;
    private RecyclerView recyclerView;
    private ArrayList<plans> data;
    private ArrayList<respond> razordata;
    private ArrayList<payment_settings> payment_settingslist;
    private ArrayList<active_payment_settings> active_payment_settingsList;
    private PlansAdapter adapter;
    private PlanidApopter planidApopter;
    Payment_settingsAdopter payment_settingsAdopter;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;
    registerresponse registerresponse1;
    RelativeLayout pro;
    LinearLayout page;
    ImageView logo;
    String plan;
    CheckBox checkBox;
    String subscribeid;
    String x, y, z;
    String username, email, password, pic, mobile, ccode, ref_code, token;
    IsPayment isPayment;
    // ImageView logo1;
    private ArrayList<Site_theme_setting> site_theme_setting;
    private ArrayList<plan> plan_id_list;
    IpService mService;
    String ipAddress;
    ImageView imageee;
    RequestBody requestFile;
    MultipartBody.Part body2;
    ProgressDialog progressDialog;
    BottomSheetDialog paydunya_dialog;
    CardView paydunya_pay_now;
    EditText paydunya_email_address;
    PaydunyaSetup setup;
    PaydunyaCheckoutStore store;
    PaydunyaCheckoutInvoice invoice;
    CardView pay_now;
    TextView skipbutton;

    String planid;
    String skip = "1";
    String planamt, planname, planidd, currency;
    public static TextView payments;
    int click_count;
    registerresponse regrespone1;
    int PAYPAL_REQUEST_CODE = 7777;
    public static PayPalConfiguration config;
    String credentials, accessToken;


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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();


        Intent in = getIntent();

        username = in.getStringExtra("username");
        email = in.getStringExtra("email");
        password = in.getStringExtra("password");
        ccode = in.getStringExtra("ccode");
        mobile = in.getStringExtra("mobile");
        ref_code = in.getStringExtra("ref_code");
        token = in.getStringExtra("token");

        config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).clientId(Config.PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


        plan_id_list = new ArrayList<plan>();
        payment_settingslist = new ArrayList<payment_settings>();
        active_payment_settingsList = new ArrayList<active_payment_settings>();
        razordata = new ArrayList<respond>();

        mService = Common.getIpService();

        getIpAddress();

        initViews();

        pro = (RelativeLayout) findViewById(R.id.pro);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        page = (LinearLayout) findViewById(R.id.page);
        pay_now = (CardView) findViewById(R.id.proceedtopay);
        logo = (ImageView) findViewById(R.id.logo);
        skipbutton = (TextView) findViewById(R.id.skipp);
        payments = (TextView) findViewById(R.id.payment1);
        imageee = (ImageView) findViewById(R.id.imageeee);

        if (pic == null) {

        } else {
            Picasso.get().load(pic).into(imageee);

            uploadFile(Uri.parse(pic), "Mymage");

        }


        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                site_theme_setting = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = site_theme_setting.get(0).getImage_url();
                Picasso.get().load(x).into(logo);

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });


        skipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog.setMessage("Please Wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.show();


                ref_code = "";

                Api.getClient().getSignupWithSkip(username, email, password, ccode, mobile, skip, ref_code, "default", "",
                        new Callback<registerresponse>() {
                            @Override
                            public void success(registerresponse signUpResponse, Response response) {
                                registerresponse1 = signUpResponse;
                                if (registerresponse1.getStatus().equalsIgnoreCase("true")) {

                                    Toast.makeText(getApplicationContext(), "Registered Successfully. You Can Login Using Your Credentials ", Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                    startActivity(in);
                                    progressDialog.hide();

                                } else if (registerresponse1.getStatus().equalsIgnoreCase("false")) {
                                    if (registerresponse1.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                        Intent in = new Intent(SignupSubscribeActivity.this, SigninActivity.class);
                                        Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                        startActivity(in);
                                        progressDialog.hide();
                                    } else {
                                        Intent in = new Intent(SignupSubscribeActivity.this, SignupActivity.class);
                                        //mobile.setError("Username already taken. Please choose aother username");
                                        Toast.makeText(getApplicationContext(), "Username already taken. Please choose aother username", Toast.LENGTH_LONG).show();
                                        startActivity(in);
                                        progressDialog.hide();
                                    }
                                } else {
                                    progressDialog.hide();
                                    Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.hide();
                                Toast.makeText(SignupSubscribeActivity.this, "Check Your Internet Connection" + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

/*
                                else {

                                    RequestBody usernamee = RequestBody.create(MediaType.parse("text/plain"), username);
                                    RequestBody emaill = RequestBody.create(MediaType.parse("text/plain"), email);
                                    RequestBody passwordd = RequestBody.create(MediaType.parse("text/plain"), password);
                                    RequestBody codee = RequestBody.create(MediaType.parse("text/plain"), ccode);
                                    RequestBody mobilee = RequestBody.create(MediaType.parse("text/plain"), mobile);
                                    RequestBody skipp = RequestBody.create(MediaType.parse("text/plain"), skip);
                                    RequestBody ref_codee = RequestBody.create(MediaType.parse("text/plain"), ref_code);
                                    RequestBody token1 = RequestBody.create(MediaType.parse("text/plain"), token);


                                    Gson gson = new GsonBuilder()
                                            .setLenient()
                                            .create();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(Apiimage.BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                            .build();

                                    Apiimage api = retrofit.create(Apiimage.class);

                                    Call<MyResponse> call = api.getSignupWithSkip(body2, usernamee, emaill, passwordd, codee,mobilee, skipp,ref_codee);

                                    call.enqueue(new retrofit2.Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {

                                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                                Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                                startActivity(in);

                                            } else if (response.body().getStatus().equalsIgnoreCase("false")) {
                                                if (response.body().getMessage().equalsIgnoreCase("Email id Already Exists")) {
                                                    Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                                                } else {


                                                    Toast.makeText(getApplicationContext(), "Username already taken. Please choose another username", Toast.LENGTH_LONG).show();

                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });


                                }
*/


            }
        });


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        final String user_role = prefs.getString(sharedpreferences.role, null);
        theme = prefs.getString(sharedpreferences.theme, null);


     /*   if( theme.equalsIgnoreCase("1")) {


            setTheme(R.style.AppTheme);



        }

        else {


            setTheme(R.style.darktheme);

        }
*/

    }

    private void getIpAddress() {
        mService.getIP().enqueue(new retrofit2.Callback<IP>() {
            @Override
            public void onResponse(Call<IP> call, retrofit2.Response<IP> response) {

                ipAddress = response.body().getIp();

            }

            @Override
            public void onFailure(Call<IP> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                dialog.dismiss();
            }
        });

    }

    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadJSON();
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
                currency = jsonResponse.getCurrency_Symbol();
                data = new ArrayList<>(Arrays.asList(jsonResponse.getPlans()));
                adapter = new PlansAdapter(data, currency, "off", SignupSubscribeActivity.this);
                progressDialog.hide();
                recyclerView.setAdapter(adapter);

                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(SignupSubscribeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
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

                            progressDialog.setMessage("Please Wait");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCancelable(false);
                            progressDialog.setMax(100);
                            progressDialog.show();


                            dialog = new Dialog(SignupSubscribeActivity.this);
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
                                    wes.setLayoutManager(new GridLayoutManager(SignupSubscribeActivity.this, 1));

                                    active_payment_settingsList = new ArrayList<>(Arrays.asList(jsonResponse.getActive_payment_settings()));
                                    active_payment_settingsAdopter = new Active_Payment_settingsAdopter(active_payment_settingsList);
                                    wes.setAdapter(active_payment_settingsAdopter);

                                }

                                @Override
                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                    Log.d("Error1", t.getMessage());
                                }
                            });


                            wes.addOnItemTouchListener(
                                    new RecyclerItemClickListener(SignupSubscribeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            if (active_payment_settingsList.size() > position) {
                                                if (active_payment_settingsList.get(position) != null) {

                                                    payments.setText(active_payment_settingsList.get(position).getPayment_type());
                                                }

                                            }
                                        }
                                    }));

/*
                        wes.addOnItemTouchListener(
                                new RecyclerItemClickListener(SignupSubscribeActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        if (payment_settingslist.size() > position) {
                                            if (payment_settingslist.get(position) != null) {

                                                Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname,payment_settingslist.get(position).getPayment_type());
                                                callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();

                                                        plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse.getPlan()));
                                                        planidd = plan_id_list.get(0).getPlan_id();


                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                        Log.d("Error", t.getMessage());
                                                    }
                                                });




                                            }
                                        }
                                    }
                                }));
*/




                      /*  radioButton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                radioButton2.setChecked(false);
                                radioButton3.setChecked(false);
                                radioButton1.setChecked(true);
                            }
                        });*/

/*
                        radioButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname,"paypal");
                                callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();

                                        plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse.getPlan()));
                                        planidd = plan_id_list.get(0).getPlan_id();

                                    }

                                    @Override
                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                        Log.d("Error", t.getMessage());
                                    }
                                });

                                radioButton1.setChecked(false);
                                radioButton3.setChecked(false);
                                radioButton2.setChecked(true);
                            }
                        });
*/

/*

                        radioButton3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {



                                radioButton2.setChecked(false);
                                radioButton1.setChecked(false);
                                radioButton3.setChecked(true);

                            }
                        });
*/


                            btndialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    progressDialog.setMessage("Please Wait");
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setMax(100);
                                    progressDialog.show();

                                    progressDialog.hide();

                                    if (payments.getText().toString().equalsIgnoreCase("stripe")) {

                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "stripe");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse1 = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();

                                                Intent in = new Intent(getApplicationContext(), PaymentPageActivity.class);
                                                in.putExtra("username", username);
                                                in.putExtra("email", email);
                                                in.putExtra("password", password);
                                                in.putExtra("plan", planidd);
                                                in.putExtra("ccode", ccode);
                                                in.putExtra("mobile", mobile);
                                                in.putExtra("ref_code", "");
                                                in.putExtra("token", token);
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


                                    } else if (payments.getText().toString().equalsIgnoreCase("Paydunya")) {

                                        progressDialog.hide();
                                        View view = getLayoutInflater().inflate(R.layout.paydunya_payment_page, null);
                                        paydunya_dialog = new BottomSheetDialog(SignupSubscribeActivity.this);
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
                                                    Toast.makeText(SignupSubscribeActivity.this, "EMAIL ADDRESS not entered", Toast.LENGTH_SHORT).show();
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


                                    } else if (payments.getText().toString().equalsIgnoreCase("razorpay")) {

                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "razorpay");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {

                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                JSONResponse jsonResponse1 = response.body();
                                                plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                planidd = plan_id_list.get(0).getPlan_id();


                                                Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getSubscriptionDeatails(planidd);
                                                callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                                                        JSONResponse jsonResponse1 = response.body();
                                                        razordata = new ArrayList<>(Arrays.asList(jsonResponse1.getRespond()));
                                                        subscribeid = razordata.get(0).getSubscriptionid();
                                                        String displayName = razordata.get(0).getName();
                                                        startPayment(subscribeid, displayName, plan);
                                                        progressDialog.hide();

                                                    }

                                                    @Override
                                                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                        Log.d("Error", t.getMessage());
                                                        Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {

                                            }
                                        });

                                    } else if (payments.getText().toString().equalsIgnoreCase("paystack")) {

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
                                                in.putExtra("password", password);
                                                in.putExtra("ccode", ccode);
                                                in.putExtra("mobile", mobile);
                                                in.putExtra("ref_code", "");
                                                in.putExtra("token", token);
                                                in.putExtra("type", "pay");
                                                startActivity(in);
                                                progressDialog.hide();
                                            }

                                            @Override
                                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                Log.d("Error", t.getMessage());
                                                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    } else if (payments.getText().toString().equalsIgnoreCase("cinetpay")) {

                                        progressDialog.hide();
                                        Call<JSONResponse> callaudio = ApiClient.getInstance1().getApi().getPlanID(planname, "Cinetpay");
                                        callaudio.enqueue(new retrofit2.Callback<JSONResponse>() {
                                            @Override
                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response1) {
                                                Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                        JSONResponse jsonResponse1 = response1.body();
                                                        JSONResponse jsonResponse2 = response2.body();
                                                        plan_id_list = new ArrayList<>(Arrays.asList(jsonResponse1.getPlan()));
                                                        ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                        for (int j = 0; j < payment_settingslist.size(); j++) {
                                                            if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                                Intent in = new Intent(getApplicationContext(), MyCinetPayActivity.class);
                                                                in.putExtra("plan", plan_id_list.get(0).getPlan_id());
                                                                //in.putExtra("amount", plan_id_list.get(0).getPrice());
                                                                in.putExtra("username", username);
                                                                in.putExtra("email", email);
                                                                in.putExtra("password", password);
                                                                in.putExtra("ccode", ccode);
                                                                in.putExtra("type", "pay");
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

                                    } else if (payments.getText().toString().equalsIgnoreCase("payPal")) {
                                        progressDialog.hide();
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


                                    }

                                }

                            });


                            progressDialog.hide();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupSubscribeActivity.this);
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
                Log.d("Error", t.getMessage());
                progressDialog.hide();
            }
        });

    }

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
                        //Toast.makeText(SignupSubscribeActivity.this, "credentials: "+credentials, Toast.LENGTH_SHORT).show();

                        String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                        RequestQueue queue = Volley.newRequestQueue(SignupSubscribeActivity.this);
                        String url = "https://api-m.paypal.com/v1/oauth2/token";
                        //for sandbox-> https://api-m.sandbox.paypal.com/v1/oauth2/token
                        //for live-> https://api-m.paypal.com/v1/oauth2/token

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                response -> {
                                    // Parse the response to get the access token
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        accessToken = jsonResponse.getString("access_token");
                                        //Toast.makeText(SignupSubscribeActivity.this, "deta> "+jsonResponse, Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(SignupSubscribeActivity.this, "accessToken: "+accessToken, Toast.LENGTH_SHORT).show();

                                        PaypalSubscription paypalSubscription = new PaypalSubscription(plan_id);
                                        Call<PaypalSubscription> call_sub = request.PaypalSubscription("Bearer " + accessToken, paypalSubscription);
                                        call_sub.enqueue(new retrofit2.Callback<PaypalSubscription>() {
                                            @Override
                                            public void onResponse(Call<PaypalSubscription> call, retrofit2.Response<PaypalSubscription> response) {

                                                if (response.isSuccessful()) {

                                                    ArrayList<links> links = new ArrayList<>(Arrays.asList(response.body().getLinks()));

                                                    for (int i = 0; i < links.size(); i++) {
                                                        if (links.get(i).getRel().equalsIgnoreCase("approve")) {

                                                            Intent intent = new Intent(SignupSubscribeActivity.this, WebView.class);
                                                            intent.putExtra("type", "pay");
                                                            intent.putExtra("id", plan_id);
                                                            intent.putExtra("username", username);
                                                            intent.putExtra("email", email);
                                                            intent.putExtra("password", password);
                                                            intent.putExtra("ccode", ccode);
                                                            intent.putExtra("mobile", mobile);
                                                            intent.putExtra("url", links.get(i).getHref());
                                                            startActivity(intent);

                                                        }
                                                    }

                                                } else {
                                                    Toast.makeText(SignupSubscribeActivity.this, "Token issue: " + response, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PaypalSubscription> call, Throwable t) {
                                                Toast.makeText(SignupSubscribeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(SignupSubscribeActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                error -> {
                                    // Handle error
                                    Log.e("AccessTokenError", "Error getting access token: " + error.toString());
                                    Toast.makeText(SignupSubscribeActivity.this, "AccessTokenError: " + error.toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignupSubscribeActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(planamt)), "USD", "Purchase Goods", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(SignupSubscribeActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            PaymentConfirmation confirm = null;
            try {

            } catch (Exception e) {
                confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            }

            if (confirm != null) {

                try {
                    String paymentDetail = confirm.toJSONObject().toString();
                    JSONObject payObj = new JSONObject(paymentDetail);
                    String payID = payObj.getJSONObject("response").getString("id");
                    String state = payObj.getJSONObject("response").getString("state");
                    Toast.makeText(SignupSubscribeActivity.this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    Toast.makeText(SignupSubscribeActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(SignupSubscribeActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(SignupSubscribeActivity.this, "Invalid Payment", Toast.LENGTH_LONG).show();
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(SignupSubscribeActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
        }
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
            options.put("description", "Runmawi Subscription");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://runmawi.com/content/uploads/settings/5f8d4d2f932bf-c-l.png");
            options.put("currency", "INR");
            options.put("subscription_id", price);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "sample@gmail.com");
            preFill.put("contact", "");

            options.put("prefill", preFill);
            // Toast.makeText(this, "response "+preFill+ " "+options, Toast.LENGTH_LONG).show();

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }


    private void uploadFile(Uri selectedImage, String my_image) {

        File file = new File(getRealPathFromURI(selectedImage));
        requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)), file);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        body2 = MultipartBody.Part.createFormData("avatar", currentDateTimeString, requestFile);

    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPaymentSuccess(String s) {

        progressDialog.setMessage("Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.show();

        Api.getClient().getRazorSignup1(username, email, password, plan, ccode, mobile, "0", token, "recurring", "Razorpay", subscribeid, new Callback<registerresponse>() {
            @Override
            public void success(registerresponse regresponse, Response response) {
                regrespone1 = regresponse;
                progressDialog.hide();

                if (regresponse.getStatus().equalsIgnoreCase("true")) {

                    progressDialog.hide();

                    Toast.makeText(getApplicationContext(), "Your Payment Is Complete. You Can Login Using Your Credentials", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(in);

                } else if (regresponse.getStatus().equalsIgnoreCase("false")) {
                    if (regresponse.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Email Id already Exists", Toast.LENGTH_LONG).show();
                    }
                } else {

                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(), "You are already registered user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {

    }

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
            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().paydunyaSignup(username, email, password, plan, ccode, mobile, "", token, "recurring", "Paydunya", splited[1], "completed");
            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                    paydunya_dialog.cancel();
                    JSONResponse jsonResponse = response.body();
                    if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(getApplicationContext(), "Your Payment Is Complete. You Can Login Using Your Credentials", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                        startActivity(in);
                    } else {
                        Toast.makeText(SignupSubscribeActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
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