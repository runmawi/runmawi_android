package com.atbuys.runmawi;

import static android.content.Context.MODE_PRIVATE;
import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.cinetpay.androidsdk.CinetPayWebAppInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class MyCinetPayWebAppInterface extends CinetPayWebAppInterface {

    Context context;
    String transaction_id, amount, side;
    registerresponse regrespone1;
    Addpayperview addpayperview;
    String id, url, username, type, useremail, password, plan, ccode,season_id,series_id;
    String user_id;


    public MyCinetPayWebAppInterface(Context c, String apikey, String site_id, String transaction_id, String amount, String currency, String description, String id, String url, String type, String username, String useremail, String password, String plan, String ccode,String series_id, String season_id) {
        super(c, apikey, site_id, transaction_id, Integer.parseInt(amount), currency, description);
        this.context = c;
        this.transaction_id = transaction_id;
        this.amount = String.valueOf(amount);
        this.side = site_id;
        this.id = id;
        this.url = url;
        this.series_id=series_id;
        this.season_id=season_id;
        this.username = username;
        this.type = type;
        this.useremail = useremail;
        this.password = password;
        this.plan = plan;
        this.ccode = ccode;
    }

    public void handlePaymentSuccess() {

        SharedPreferences prefs = context.getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);

        if (type.equalsIgnoreCase("free")) {

            Call<JSONResponse> becomSubcriber = ApiClient.getInstance1().getApi().getBecomSubcriberViaCinetPay(user_id, plan, String.valueOf(amount), "", "", "","Android");
            becomSubcriber.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();

                    if (jsonResponse.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(context, " Your Payment Is Completed", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                        editor.putBoolean(sharedpreferences.login, true);
                        editor.putString(sharedpreferences.user_id, user_id);
                        editor.putString(sharedpreferences.role, "subscriber");
                        editor.putString(sharedpreferences.email, useremail);
                        editor.putString(sharedpreferences.username, username);
                        editor.apply();
                        Intent in = new Intent(context, HomePageActivitywithFragments.class);
                        context.startActivity(in);
                    } else {
                        Toast.makeText(context, "Error: " + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type.equalsIgnoreCase("pay")) {
            Api.getClient().getCinetpaySignup(username, useremail, password, "", plan, ccode, "", "0", "", "default", String.valueOf(amount), "recurring", "CinetPay", "","Android", new Callback<registerresponse>() {
                @Override
                public void success(registerresponse regresponse, retrofit.client.Response response) {
                    regrespone1 = regresponse;

                    if (regresponse.getStatus().equalsIgnoreCase("true")) {

                        Toast.makeText(context, "Your Payment Is Complete. You Can Login Using Your Credentials", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(context, SigninActivity.class);
                        context.startActivity(in);

                    } else if (regresponse.getStatus().equalsIgnoreCase("false")) {
                        if (regresponse.getMessage().equalsIgnoreCase("Email id Already Exists")) {
                            Toast.makeText(context, "Email Id already Exists", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Error: " + regrespone1.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "You are already registered user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else if (type.equalsIgnoreCase("video_rent")) {
            Api.getClient().getAddPayperView1(user_id, id, "", "CinetPay", String.valueOf(amount),"Android", new Callback<Addpayperview>() {

                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Intent in = new Intent(context, OnlinePlayerActivity.class);
                        in.putExtra("id", id);
                        in.putExtra("url", url);
                        in.putExtra("suburl", "");
                        in.putExtra("data", "videos");
                        in.putExtra("ads", "");
                        in.putExtra("xtra", "");
                        context.startActivity(in);
                    } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } else if (type.equalsIgnoreCase("live_rent")) {
            Api.getClient().getAddPayperViewLive(user_id, id, "", "CinetPay", String.valueOf(amount),"Android", new Callback<Addpayperview>() {

                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Intent in = new Intent(context, LiveOnlinePlayerActivity.class);
                        in.putExtra("id", id);
                        in.putExtra("url", url);
                        in.putExtra("suburl", "");
                        in.putExtra("data", "");
                        in.putExtra("ads", "");
                        context.startActivity(in);
                    } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }else if (type.equalsIgnoreCase("season_rent")) {


         /*
            Api.getClient().getAddPayperViewSeason(user_id, series_id,season_id, "", "CinetPay", String.valueOf(amount),"Android", new Callback<Addpayperview>() {

                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Intent in = new Intent(context, OnlinePlayer11Activity.class);
                        in.putExtra("id", id);
                        in.putExtra("url", url);
                        in.putExtra("suburl", "");
                        in.putExtra("data", "");
                        in.putExtra("ads", "");
                        context.startActivity(in);
                    } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });*/
        }else if (type.equalsIgnoreCase("audio_rent")) {
            Api.getClient().getAddPayperViewAudio(user_id,id, "", "CinetPay", String.valueOf(amount),"Android", new Callback<Addpayperview>() {

                @Override
                public void success(Addpayperview addpayperview1, Response response) {

                    addpayperview = addpayperview1;
                    if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();

                        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, id);
                        res.enqueue(new retrofit2.Callback<JSONResponse>() {
                            @Override
                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                JSONResponse jsonResponse = response.body();

                                if (jsonResponse.getAudiodetail().length == 0 || jsonResponse.getAudiodetail() == null) {
                                    Toast.makeText(context, "Audio Not Available ", Toast.LENGTH_LONG).show();
                                } else {
                                   ArrayList<audiodetail> audiodetail = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                    SharedPreferences.Editor editor = context.getSharedPreferences(sharedpreferences.My_preference_name, context.MODE_PRIVATE).edit();
                                    editor.putBoolean(sharedpreferences.login, true);
                                    editor.putString(sharedpreferences.audioid, audiodetail.get(0).getId());
                                    editor.apply();
                                    editor.commit();

                                    Intent in = new Intent(context, MediaPlayerPageActivity.class);
                                    in.putExtra("id", audiodetail.get(0).getId());
                                    context.startActivity(in);
                                    mediaplayer.reset();
                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    try {
                                        mediaplayer.setDataSource(audiodetail.get(0).getMp3_url());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mediaplayer.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mediaplayer.start();
                                }
                            }

                            @Override
                            public void onFailure(Call<JSONResponse> call, Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });

                    } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                        Toast.makeText(context, addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    @Override
    @JavascriptInterface
    public void onResponse(String response) {
        Log.d("MyCinetPayWebApp", response);
        String status = "not started", message = null, desc = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (status.equalsIgnoreCase("ACCEPTED")) {//ACCEPTED
                handlePaymentSuccess();
            } else if (status.equalsIgnoreCase("REFUSED")) {
                Toast.makeText(context, status + ", Your payment failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error Status: " + status, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error to handle the payment status", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    @JavascriptInterface
    public void onError(String response) {
        Log.d("MyCinetPayWebApp", response);
        Toast.makeText(context, "Error: " + response, Toast.LENGTH_LONG).show();
    }
}