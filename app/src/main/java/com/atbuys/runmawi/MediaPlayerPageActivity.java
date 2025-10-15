package com.atbuys.runmawi;

import static com.atbuys.runmawi.UserHomeFragment.audio_id;
import static com.atbuys.runmawi.UserHomeFragment.mediaplayer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;
import com.atbuys.runmawi.Config.Config;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaPlayerPageActivity extends AppCompatActivity implements PaymentResultListener {

    TextView second;
    RecyclerView recommandedRecycler;
    LinearLayout recommand_laylot;
    RecommandedAdopter recommandedAdopter;
    private ArrayList<recomendedaudios> recomendedaudioslist;
    private ArrayList<audio> audiolist;
    private ArrayList<audiodetail> movie_detaildata;
    //private ArrayList<lyrics> lyricsArrayList;
    String before_lyrics = "";
    String after_lyrics = "";
    String current_lyrics = "";
    int j;

    private String album_id;
    public static Dialog dialogppv;
    Addpayperview addpayperview;
    private ArrayList<active_payment_settings> active_payment_settingsList;
    public static TextView pays;
    private ArrayList<payment_settings> payment_settingslist;
    String key, py_id;
    private Stripe stripe;
    PaymentMethodCreateParams params;
    Active_Payment_settingsAdopter active_payment_settingsAdopter;
    private static final int PAYPAL_REQUEST_CODE = 7777;
    String paypalamount;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
            .clientId(Config.PAYPAL_CLIENT_ID);

    SeekBar seekbar;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private LinearLayout equalizerlayot, wishlistlayout, favouritelayout, lyrics;
    private ImageView wishlist, wishlistadd, favourite, favouriteadd, add_playlist, show_lyrics, showed_lyrics, add_station;
    TextView tx1, tx2, audio_title, audio_description, start_lyrics, end_lyrics, syned_lyrics, title_playlist;
    private String[] songs = {"Aathma-raama", "Bones", "Dandelions", "Fake-love",
            "How-far-i'll-go", "How-you-like-that", "Little-do-you-know", "Love-is-gone",
            "On-my-way", "Perfect", "Undo", "Way-too-easy", "Wishlist"};
    String user_id, user_role, ppv_price, currency;
    ScrollView scrollView;
    TextView views, publish_date, album_name;
    private DownloadManager downloadManager;


    String nexturl, preurl;

    ImageView buttonpre, buttonnext, buttonpause1, buttonrepeat, buttonShuffle, buttonStart1, repeatonce, repeatmany, greenshuffle, buttonStart, downarrow;
    ImageView playbutton1, pausebutton1;
    boolean suffleMode = false;

    LinearLayout likee, dislike, unlikelayout, undislikelayout;
    ImageView like, liked, dislikee, dislikeed, audio_thumb, blur_image;
    ProgressBar music_progress;
    LinearLayout music_down, fav, audio_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_page);


        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        audio_id = prefs.getString(sharedpreferences.audioid, "");

        Intent in = getIntent();
        audio_id = in.getStringExtra("id");

       /* final Handler handler1 = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                audio_id = prefs.getString(sharedpreferences.audioid,null);



                if(mediaplayer.isPlaying())
                {


                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, audio_id);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();
                            movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                            if (jsonResponse.getAudiodetail().length == 0)
                            {

                            }

                            else
                            {


                            }


                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                }


            }

        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(Update);
            }
        }, 1000, 1000);
*/

        recomendedaudioslist = new ArrayList<recomendedaudios>();
        movie_detaildata = new ArrayList<audiodetail>();
        //lyricsArrayList = new ArrayList<>();
        audiolist = new ArrayList<audio>();

        scrollView = (ScrollView) findViewById(R.id.scroll);
        audio_thumb = (ImageView) findViewById(R.id.audio_thumb);
        recommand_laylot = (LinearLayout) findViewById(R.id.music_recomendedaudiolayout);

        recommandedRecycler = (RecyclerView) findViewById(R.id.music_recomendedrecycler);
        recommandedAdopter = new RecommandedAdopter(recomendedaudioslist, getApplicationContext());
        recommandedRecycler.setHasFixedSize(true);
        recommandedRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recommandedRecycler.setAdapter(recommandedAdopter);
        active_payment_settingsList = new ArrayList<active_payment_settings>();
        pays = (TextView) findViewById(R.id.pays);


        setMorelikethis();

     /*   final Handler handler223 = new Handler();
        final Runnable Update223 = new Runnable() {
            public void run() {
                Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getRecommendedAudio(audio_id);
                callser.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();

                        if (jsonResponse.getRecomendedaudios().length == 0) {
                            recommand_laylot.setVisibility(View.GONE);
                        } else {
                            recommand_laylot.setVisibility(View.VISIBLE);
                            recomendedaudioslist = new ArrayList<>(Arrays.asList(jsonResponse.getRecomendedaudios()));
                            recommandedAdopter = new RecommandedAdopter(recomendedaudioslist);
                            recommandedRecycler.setAdapter(recommandedAdopter);
                            recommandedAdopter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        };
        Timer swipeTime223 = new Timer();
        swipeTime223.schedule(new TimerTask() {
            @Override
            public void run() {
                handler223.post(Update223);
            }
        }, 1000, 5000);*/

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });


        seekbar = (SeekBar) findViewById(R.id.music_seekBar);
        tx1 = (TextView) findViewById(R.id.music_first);
        tx2 = (TextView) findViewById(R.id.music_last);
        audio_title = (TextView) findViewById(R.id.music_Audio_title);
        audio_description = (TextView) findViewById(R.id.music_audio_description);
        start_lyrics = (TextView) findViewById(R.id.start_lyrics);
        end_lyrics = (TextView) findViewById(R.id.end_lyrics);
        syned_lyrics = (TextView) findViewById(R.id.syned_lyrics);
        title_playlist = (TextView) findViewById(R.id.title_playlist);
        favouritelayout = (LinearLayout) findViewById(R.id.music_favoritelayout);
        wishlistlayout = (LinearLayout) findViewById(R.id.music_wishlistlayout);
        wishlist = (ImageView) findViewById(R.id.music_wishlist);
        wishlistadd = (ImageView) findViewById(R.id.music_wishlisted);
        favourite = (ImageView) findViewById(R.id.music_favourite);
        add_playlist = (ImageView) findViewById(R.id.add_playlist);
        lyrics = (LinearLayout) findViewById(R.id.lyrics);
        show_lyrics = (ImageView) findViewById(R.id.show_lyrics);
        showed_lyrics = (ImageView) findViewById(R.id.showed_lyrics);
        add_station = (ImageView) findViewById(R.id.add_station);
        favouriteadd = (ImageView) findViewById(R.id.music_favoriteadd);
        album_name = (TextView) findViewById(R.id.album_name);
        publish_date = (TextView) findViewById(R.id.publish_date);
        views = (TextView) findViewById(R.id.views);
        music_progress = (ProgressBar) findViewById(R.id.music_progress);
        music_down = (LinearLayout) findViewById(R.id.music_down);
        audio_page = (LinearLayout) findViewById(R.id.audio_page);
        blur_image = (ImageView) findViewById(R.id.blur_image);


        likee = (LinearLayout) findViewById(R.id.likeed);
        dislike = (LinearLayout) findViewById(R.id.dislike);
        unlikelayout = (LinearLayout) findViewById(R.id.unlikelayout);
        undislikelayout = (LinearLayout) findViewById(R.id.undislikelayout);

        like = (ImageView) findViewById(R.id.like);
        liked = (ImageView) findViewById(R.id.liked);
        dislikee = (ImageView) findViewById(R.id.dislikee);
        dislikeed = (ImageView) findViewById(R.id.dislikeed);


        equalizerlayot = (LinearLayout) findViewById(R.id.music_equalizer_layout);


        equalizerlayot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
                startActivityForResult(intent, 1);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getStripeOnetime();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                currency = response.body().getCurrency_Symbol();
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });

        /*final Handler handler224 = new Handler();
        final Runnable Update224 = new Runnable() {
            public void run() {//complete

                start_lyrics.setText("");
                end_lyrics.setText("");
                syned_lyrics.setText("");

                try {
                    int playerTime = mediaplayer.getCurrentPosition();


                    for (int i = 0; i < lyricsArrayList.size() && j < lyricsArrayList.size(); i++) {

                        if (playerTime >= lyricsArrayList.get(j).getTime()) {

                            current_lyrics = lyricsArrayList.get(j).getLine() + "\n";
                            j++;
                        }

                        before_lyrics = "";
                        after_lyrics = "";
                        try {
                            if (j == 0) {
                                        *//*before_lyrics += lyricsArrayList.get(0).getLine() + "\n";
                                        before_lyrics += lyricsArrayList.get(1).getLine() + "\n";
                                        before_lyrics += lyricsArrayList.get(2).getLine() + "\n";*//*
                                current_lyrics = "....";
                            } else if (j == 1) {
                                after_lyrics += lyricsArrayList.get(1).getLine() + "\n";
                                after_lyrics += lyricsArrayList.get(2).getLine() + "\n";

                            } else if (j == 2) {
                                before_lyrics += lyricsArrayList.get(0).getLine() + "\n";
                                after_lyrics += lyricsArrayList.get(2).getLine() + "\n";
                                after_lyrics += lyricsArrayList.get(3).getLine() + "\n";

                            }
                            for (int z = j - 2, count = 0; count < 2; z++) {
                                before_lyrics += lyricsArrayList.get(z - 1).getLine() + "\n";
                                count++;
                            }//z=6,z=5,
                            for (int k = lyricsArrayList.size() - 1, g = j, count = 0; k > j && count < 2; k--) {
                                after_lyrics += lyricsArrayList.get(g).getLine() + "\n";
                                g++;
                                count++;
                            }


                        } catch (Exception e) {
                        }


                        start_lyrics.setText(before_lyrics);
                        end_lyrics.setText(after_lyrics);
                        syned_lyrics.setText(current_lyrics);
                    }
                } catch (Exception e) {

                }


            }
        };
        Timer swipeTime224 = new Timer();
        swipeTime224.schedule(new TimerTask() {
            @Override
            public void run() {
                handler224.post(Update224);
            }
        }, 1000, 1000);*/

        /*Call<JSONResponse> date_api = ApiClient.getInstance1().getApi().getPlayedDate(user_id, audio_id);
        date_api.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

        Call<JSONResponse> count_api = ApiClient.getInstance1().getApi().getPlayCount(user_id, audio_id);
        count_api.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });*/

        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, audio_id);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber") || movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {//check this line

                    if (!(movie_detaildata.get(0).getPpv_status().equalsIgnoreCase("can_view"))) {
                        mediaplayer.pause();
                        pausebutton1.setVisibility(View.GONE);
                        playbutton1.setVisibility(View.GONE);
                        blur_image.setVisibility(View.VISIBLE);
                        audio_page.setVisibility(View.GONE);
                        AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                        AlertDialog dialog = alert.create();
                        dialog.setView(view);
                        dialog.show();

                        CardView subscribe = view.findViewById(R.id.subscribe);
                        CardView rent = view.findViewById(R.id.rent);
                        ImageView cross = view.findViewById(R.id.cancel);
                        TextView content = view.findViewById(R.id.content);
                        TextView desc_content = view.findViewById(R.id.desc_content);

                        if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                            content.setText("Subscription Content");
                            desc_content.setText("You are not a subscribed user, Buy subscription to get access for the content.!");
                            subscribe.setVisibility(View.VISIBLE);
                        } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {
                            content.setText("Pay-Per-View Content");
                            desc_content.setText("Pay for and watch specific content without a subscription.!");
                            subscribe.setVisibility(View.VISIBLE);
                            rent.setVisibility(View.VISIBLE);
                        }

                        cross.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                blur_image.setVisibility(View.GONE);
                                audio_page.setVisibility(View.VISIBLE);
                            }
                        });
                        subscribe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                blur_image.setVisibility(View.GONE);
                                audio_page.setVisibility(View.VISIBLE);
                                Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                startActivity(in);
                            }
                        });
                        rent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ppv_price = movie_detaildata.get(0).getPpv_price();

                                dialogppv = new Dialog(MediaPlayerPageActivity.this);
                                dialogppv.setCancelable(true);
                                dialogppv.setContentView(R.layout.dialogbox);
                                dialogppv.show();

                                RadioButton radioButton = (RadioButton) dialogppv.findViewById(R.id.termsandcon);
                                RadioButton radioButton1 = (RadioButton) dialogppv.findViewById(R.id.radiorazor);
                                RadioButton radioButton2 = (RadioButton) dialogppv.findViewById(R.id.radiopaypal);
                                RadioButton radioButton3 = (RadioButton) dialogppv.findViewById(R.id.radiostripe);
                                RadioButton radioButton4 = (RadioButton) dialogppv.findViewById(R.id.termsandcons);
                                Button btndialog = (Button) dialogppv.findViewById(R.id.proceed);
                                RecyclerView wes = (RecyclerView) dialogppv.findViewById(R.id.wes);

                                Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                                callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();

                                        wes.setHasFixedSize(true);
                                        wes.setLayoutManager(new GridLayoutManager(MediaPlayerPageActivity.this, 1));

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
                                        new RecyclerItemClickListener(MediaPlayerPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {

                                                if (active_payment_settingsList.size() > position) {
                                                    if (active_payment_settingsList.get(position) != null) {

                                                        pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                                    }

                                                }
                                            }
                                        }));
                                btndialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                            View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                            BottomSheetDialog dialog1 = new BottomSheetDialog(MediaPlayerPageActivity.this);
                                            dialog1.setContentView(view);

                                            CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                            Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                            ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                            CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                            paynow1.setText("Pay " + ppv_price + ".00");

                                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                @Override
                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                    JSONResponse jsonResponse = response.body();
                                                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                                    if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                        key = payment_settingslist.get(0).getTest_publishable_key();
                                                    } else {
                                                        key = payment_settingslist.get(0).getLive_publishable_key();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                }
                                            });
                                            paynow1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    paynow1.setVisibility(View.GONE);
                                                    payment_progress.setVisibility(View.VISIBLE);

                                                    if (!check.isChecked()) {
                                                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                                        paynow1.setVisibility(View.VISIBLE);
                                                        payment_progress.setVisibility(View.GONE);
                                                    } else {

                                                        params = cardMultilineWidget.getPaymentMethodCreateParams();

                                                        if (params == null) {
                                                            return;
                                                        }
                                                        stripe = new Stripe(getApplicationContext(), key);
                                                        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                                            @Override
                                                            public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                                py_id = paymentMethod.id;
                                                                Api.getClient().getAddPayperViewAudio(user_id, audio_id, py_id, "stripe", ppv_price,"Android", new Callback<Addpayperview>() {

                                                                    @Override
                                                                    public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                                                                        dialog1.cancel();
                                                                        addpayperview = addpayperview1;
                                                                        if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                            mediaplayer.start();
                                                                            dialog.cancel();
                                                                            dialogppv.dismiss();
                                                                            blur_image.setVisibility(View.GONE);
                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                            pausebutton1.setVisibility(View.VISIBLE);

                                                                        } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                            paynow1.setVisibility(View.VISIBLE);
                                                                            payment_progress.setVisibility(View.GONE);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void failure(RetrofitError error) {

                                                                        Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                        payment_progress.setVisibility(View.GONE);
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onError(@NotNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                                paynow1.setVisibility(View.VISIBLE);
                                                                payment_progress.setVisibility(View.GONE);

                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                            dialog1.show();
                                        } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                            startPayment();
                                            blur_image.setVisibility(View.GONE);
                                            audio_page.setVisibility(View.VISIBLE);
                                            dialog.cancel();
                                            dialogppv.dismiss();
                                        } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                            dialogppv.dismiss();
                                            dialog.cancel();
                                            blur_image.setVisibility(View.GONE);
                                            audio_page.setVisibility(View.VISIBLE);
                                            Intent intent = new Intent(MediaPlayerPageActivity.this, CheckoutActivity.class);
                                            intent.putExtra("price", ppv_price);
                                            intent.putExtra("id", audio_id);
                                            intent.putExtra("url", "");
                                            intent.putExtra("type", "audio_rent");
                                            startActivity(intent);

                                        } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                @Override
                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                    JSONResponse jsonResponse2 = response2.body();
                                                    ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                    for (int j = 0; j < payment_settingslist.size(); j++) {
                                                        if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                            dialogppv.dismiss();
                                                            dialog.cancel();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            Intent in = new Intent(MediaPlayerPageActivity.this, MyCinetPayActivity.class);
                                                            in.putExtra("id", audio_id);
                                                            in.putExtra("url", "");
                                                            in.putExtra("type", "audio_rent");
                                                            in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                            in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                            in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                            in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                            in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                            in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Audio Purchase");
                                                            in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                            startActivity(in);

                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                }
                                            });
                                        } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                            dialog.cancel();
                                            dialogppv.dismiss();
                                            blur_image.setVisibility(View.GONE);
                                            audio_page.setVisibility(View.VISIBLE);
                                            getPayment();
                                        }

                                    }

                                });

                            }
                        });
                    }else {
                        pausebutton1.setVisibility(View.VISIBLE);
                    }
                }else {
                    pausebutton1.setVisibility(View.VISIBLE);
                }
                title_playlist.setText("Playing From " + jsonResponse.getMain_genre());

                if (jsonResponse.getAudiodetail().length == 0) {

                } else {
                    audio_title.setText(movie_detaildata.get(0).getTitle());
                    audio_description.setText(movie_detaildata.get(0).getDescription());
                    views.setText(movie_detaildata.get(0).getViews() + " Views");

                    String name = movie_detaildata.get(0).getCreated_at();
                    String[] splited = name.split("T");

                    publish_date.setText(splited[0]);
                    Picasso.get().load(movie_detaildata.get(0).getImage()).into(audio_thumb);

                    album_name.setText(jsonResponse.getMain_genre());

                }

                if (jsonResponse.getWishlist().equalsIgnoreCase("true")) {

                    wishlistadd.setVisibility(View.VISIBLE);
                    wishlist.setVisibility(View.GONE);
                } else {
                    wishlistadd.setVisibility(View.GONE);
                    wishlist.setVisibility(View.VISIBLE);

                }

                if (jsonResponse.getFavorite().equalsIgnoreCase("true")) {

                    favouriteadd.setVisibility(View.VISIBLE);
                    favourite.setVisibility(View.GONE);
                } else {
                    favouriteadd.setVisibility(View.GONE);
                    favourite.setVisibility(View.VISIBLE);

                }

                if (jsonResponse.getLike().equalsIgnoreCase("true")) {

                    likee.setVisibility(View.GONE);
                    unlikelayout.setVisibility(View.VISIBLE);//
                } else {
                    unlikelayout.setVisibility(View.GONE);//
                    likee.setVisibility(View.VISIBLE);

                }

                if (jsonResponse.getDislike().equalsIgnoreCase("true")) {

                    dislike.setVisibility(View.GONE);
                    undislikelayout.setVisibility(View.VISIBLE);//
                } else {
                    dislike.setVisibility(View.VISIBLE);//
                    undislikelayout.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

        try {
            finalTime = mediaplayer.getDuration();
            startTime = mediaplayer.getCurrentPosition();
        } catch (Exception e) {
        }

        tx2.setText(String.format("%02d : %02d ",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );


        seekbar.setMax((int) finalTime);

        Runnable mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                try {
                    seekbar.setProgress(mediaplayer.getCurrentPosition());
                } catch (Exception e) {

                }

                myHandler.postDelayed(this, 50);
            }
        };


        myHandler.postDelayed(mUpdateSeekbar, 0);

        long xxy = 0;
        try {
            xxy = mediaplayer.getCurrentPosition();
        } catch (Exception e) {

        }


        seekbar.setProgress((int) xxy);
        myHandler.postDelayed(UpdateSongTime, 1000);

        seekbar.setClickable(true);
        seekbar.setFocusable(true);
        seekbar.setEnabled(true);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaplayer.seekTo(progressChangedValue);
            }
        });


        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(9000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = audio_description.getWidth();
                final float translationX = width * progress;
                audio_description.setTranslationX(translationX);

            }
        });
        animator.start();

        buttonpre = (ImageView) findViewById(R.id.music_previous);
        buttonnext = (ImageView) findViewById(R.id.music_next);
        repeatonce = findViewById(R.id.music_repeatmany);
        repeatmany = findViewById(R.id.music_repeatonce);//music_repeatmany
        buttonShuffle = (ImageView) findViewById(R.id.music_shuffle1);
        greenshuffle = (ImageView) findViewById(R.id.music_shufflevisible1);
        buttonrepeat = (ImageView) findViewById(R.id.music_repeat);
        fav = (LinearLayout) findViewById(R.id.fav);

        playbutton1 = findViewById(R.id.music_play);
        pausebutton1 = findViewById(R.id.music_pause);

        playbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausebutton1.setVisibility(View.VISIBLE);
                playbutton1.setVisibility(View.GONE);
                mediaplayer.start();
            }
        });

        pausebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                pausebutton1.setVisibility(View.GONE);
                playbutton1.setVisibility(View.VISIBLE);
                mediaplayer.pause();

            }
        });

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (repeatonce.getVisibility() == View.VISIBLE) {
                    mediaplayer.setLooping(true);
                }

                music_progress.setVisibility(View.VISIBLE);
                pausebutton1.setVisibility(View.GONE);
                playbutton1.setVisibility(View.GONE);

                if (suffleMode) {
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getSuffleAudios(audio_id);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                                ArrayList<audio_shufffle> suffle_audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio_shufffle()));

                                title_playlist.setText("Playing From " + jsonResponse.getMain_genre());
                                music_progress.setVisibility(View.GONE);
                                pausebutton1.setVisibility(View.VISIBLE);
                                playbutton1.setVisibility(View.GONE);

                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                //    editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.audioid, suffle_audiolist.get(0).getId());
                                editor.apply();
                                editor.commit();
                                audio_id = prefs.getString(sharedpreferences.audioid, "");

                                nexturl = suffle_audiolist.get(0).getMp3_url();
                                AudioDetails(suffle_audiolist.get(0).getId());

                                mediaplayer.stop();
                                mediaplayer = MediaPlayer.create(MediaPlayerPageActivity.this, Uri.parse(suffle_audiolist.get(0).getMp3_url()));

                                finalTime = mediaplayer.getDuration();
                                startTime = mediaplayer.getCurrentPosition();


                                tx2.setText(String.format("%02d : %02d ",
                                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                        finalTime)))
                                );

                                seekbar.setProgress((int) startTime);
                                myHandler.postDelayed(UpdateSongTime, 1000);
                                mediaplayer.start();
                                setMorelikethis();

                                if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                                } else if (suffle_audiolist.get(0).getAccess().equalsIgnoreCase("subscriber") || suffle_audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {//check this line

                                    if (!(suffle_audiolist.get(0).getPpv_status().equalsIgnoreCase("can_view"))) {
                                        mediaplayer.pause();
                                        pausebutton1.setVisibility(View.GONE);
                                        playbutton1.setVisibility(View.GONE);
                                        music_progress.setVisibility(View.GONE);
                                        blur_image.setVisibility(View.VISIBLE);
                                        audio_page.setVisibility(View.GONE);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                                        AlertDialog dialog = alert.create();
                                        dialog.setView(view);
                                        dialog.show();

                                        CardView subscribe = view.findViewById(R.id.subscribe);
                                        CardView rent = view.findViewById(R.id.rent);
                                        ImageView cross = view.findViewById(R.id.cancel);
                                        TextView content = view.findViewById(R.id.content);
                                        TextView desc_content = view.findViewById(R.id.desc_content);

                                        if (suffle_audiolist.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                                            content.setText("Subscription Content");
                                            desc_content.setText("You are not a subscribed user, Buy subscription to get access for the content.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                        } else if (suffle_audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                            content.setText("Pay-Per-View Content");
                                            desc_content.setText("Pay for and watch specific content without a subscription.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                            rent.setVisibility(View.VISIBLE);
                                        }

                                        cross.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        subscribe.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                                Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                                startActivity(in);
                                            }
                                        });
                                        rent.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ppv_price = suffle_audiolist.get(0).getPpv_price();

                                                dialogppv = new Dialog(MediaPlayerPageActivity.this);
                                                dialogppv.setCancelable(true);
                                                dialogppv.setContentView(R.layout.dialogbox);
                                                dialogppv.show();

                                                RadioButton radioButton = (RadioButton) dialogppv.findViewById(R.id.termsandcon);
                                                RadioButton radioButton1 = (RadioButton) dialogppv.findViewById(R.id.radiorazor);
                                                RadioButton radioButton2 = (RadioButton) dialogppv.findViewById(R.id.radiopaypal);
                                                RadioButton radioButton3 = (RadioButton) dialogppv.findViewById(R.id.radiostripe);
                                                RadioButton radioButton4 = (RadioButton) dialogppv.findViewById(R.id.termsandcons);
                                                Button btndialog = (Button) dialogppv.findViewById(R.id.proceed);
                                                RecyclerView wes = (RecyclerView) dialogppv.findViewById(R.id.wes);

                                                Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();

                                                        wes.setHasFixedSize(true);
                                                        wes.setLayoutManager(new GridLayoutManager(MediaPlayerPageActivity.this, 1));

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
                                                        new RecyclerItemClickListener(MediaPlayerPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(View view, int position) {

                                                                if (active_payment_settingsList.size() > position) {
                                                                    if (active_payment_settingsList.get(position) != null) {

                                                                        pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                                                    }

                                                                }
                                                            }
                                                        }));
                                                btndialog.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                                            View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                                            BottomSheetDialog dialog1 = new BottomSheetDialog(MediaPlayerPageActivity.this);
                                                            dialog1.setContentView(view);

                                                            CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                                            Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                                            ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                                            CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                                            paynow1.setText("Pay " + ppv_price + ".00");

                                                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                    JSONResponse jsonResponse = response.body();
                                                                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                                                    if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                        key = payment_settingslist.get(0).getTest_publishable_key();
                                                                    } else {
                                                                        key = payment_settingslist.get(0).getLive_publishable_key();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                }
                                                            });
                                                            paynow1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    paynow1.setVisibility(View.GONE);
                                                                    payment_progress.setVisibility(View.VISIBLE);

                                                                    if (!check.isChecked()) {
                                                                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                        payment_progress.setVisibility(View.GONE);
                                                                    } else {

                                                                        params = cardMultilineWidget.getPaymentMethodCreateParams();

                                                                        if (params == null) {
                                                                            return;
                                                                        }
                                                                        stripe = new Stripe(getApplicationContext(), key);
                                                                        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                                                            @Override
                                                                            public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                                                py_id = paymentMethod.id;
                                                                                Api.getClient().getAddPayperViewAudio(user_id, audio_id, py_id, "stripe", ppv_price,"Android",new Callback<Addpayperview>() {

                                                                                    @Override
                                                                                    public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                                                                                        dialog1.cancel();
                                                                                        addpayperview = addpayperview1;
                                                                                        if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            mediaplayer.start();
                                                                                            dialog.cancel();
                                                                                            blur_image.setVisibility(View.GONE);
                                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                                            pausebutton1.setVisibility(View.VISIBLE);

                                                                                        } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            paynow1.setVisibility(View.VISIBLE);
                                                                                            payment_progress.setVisibility(View.GONE);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void failure(RetrofitError error) {

                                                                                        Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                                        payment_progress.setVisibility(View.GONE);
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onError(@NotNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                                                paynow1.setVisibility(View.VISIBLE);
                                                                                payment_progress.setVisibility(View.GONE);

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });

                                                            dialog1.show();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                                            startPayment();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                                            dialogppv.dismiss();
                                                            dialog.cancel();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            Intent intent = new Intent(MediaPlayerPageActivity.this, CheckoutActivity.class);
                                                            intent.putExtra("price", ppv_price);
                                                            intent.putExtra("id", audio_id);
                                                            intent.putExtra("url", "");
                                                            intent.putExtra("type", "audio_rent");
                                                            startActivity(intent);

                                                        } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                                            dialogppv.dismiss();

                                                            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                                    JSONResponse jsonResponse2 = response2.body();
                                                                    ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                                    for (int j = 0; j < payment_settingslist.size(); j++) {
                                                                        if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                                            blur_image.setVisibility(View.GONE);
                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                            Intent in = new Intent(MediaPlayerPageActivity.this, MyCinetPayActivity.class);
                                                                            in.putExtra("id", audio_id);
                                                                            in.putExtra("url", "");
                                                                            in.putExtra("type", "audio_rent");
                                                                            in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                                            in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                                            in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                                            in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                                            in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                                            in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Audio Purchase");
                                                                            in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                                            startActivity(in);

                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                                }
                                                            });
                                                        } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            getPayment();
                                                        }

                                                    }

                                                });

                                            }
                                        });
                                    }else {
                                        pausebutton1.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    pausebutton1.setVisibility(View.VISIBLE);
                                }

                            } else {

                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                mediaplayer.stop();
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });

                } else {
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getNextAudioDetail(user_id, audio_id);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                                audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));

                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                                editor.apply();
                                editor.commit();
                                audio_id = prefs.getString(sharedpreferences.audioid, "");

                                title_playlist.setText("Playing From " + jsonResponse.getMain_genre());
                                music_progress.setVisibility(View.GONE);//
                                pausebutton1.setVisibility(View.VISIBLE);//
                                playbutton1.setVisibility(View.GONE);//
                                nexturl = audiolist.get(0).getMp3_url();
                                AudioDetails(audiolist.get(0).getId());

                                mediaplayer.stop();
                                mediaplayer = MediaPlayer.create(MediaPlayerPageActivity.this, Uri.parse(audiolist.get(0).getMp3_url()));
                                try {
                                    finalTime = mediaplayer.getDuration();
                                    startTime = mediaplayer.getCurrentPosition();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                tx2.setText(String.format("%02d : %02d ",
                                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                        finalTime)))
                                );

                                seekbar.setProgress((int) startTime);
                                myHandler.postDelayed(UpdateSongTime, 1000);

                                try {
                                    mediaplayer.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                setMorelikethis();
                                if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                                } else if (audiolist.get(0).getAccess().equalsIgnoreCase("subscriber") || audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {//check this line

                                    if (!(audiolist.get(0).getPpv_status().equalsIgnoreCase("can_view"))) {
                                        mediaplayer.pause();
                                        pausebutton1.setVisibility(View.GONE);
                                        playbutton1.setVisibility(View.GONE);
                                        music_progress.setVisibility(View.GONE);
                                        blur_image.setVisibility(View.VISIBLE);
                                        audio_page.setVisibility(View.GONE);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                                        AlertDialog dialog = alert.create();
                                        dialog.setView(view);
                                        dialog.show();

                                        CardView subscribe = view.findViewById(R.id.subscribe);
                                        CardView rent = view.findViewById(R.id.rent);
                                        ImageView cross = view.findViewById(R.id.cancel);
                                        TextView content = view.findViewById(R.id.content);
                                        TextView desc_content = view.findViewById(R.id.desc_content);

                                        if (audiolist.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                                            content.setText("Subscription Content");
                                            desc_content.setText("You are not a subscribed user, Buy subscription to get access for the content.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                        } else if (audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                            content.setText("Pay-Per-View Content");
                                            desc_content.setText("Pay for and watch specific content without a subscription.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                            rent.setVisibility(View.VISIBLE);
                                        }

                                        cross.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        subscribe.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                                Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                                startActivity(in);
                                            }
                                        });
                                        rent.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ppv_price = audiolist.get(0).getPpv_price();

                                                dialogppv = new Dialog(MediaPlayerPageActivity.this);
                                                dialogppv.setCancelable(true);
                                                dialogppv.setContentView(R.layout.dialogbox);
                                                dialogppv.show();

                                                RadioButton radioButton = (RadioButton) dialogppv.findViewById(R.id.termsandcon);
                                                RadioButton radioButton1 = (RadioButton) dialogppv.findViewById(R.id.radiorazor);
                                                RadioButton radioButton2 = (RadioButton) dialogppv.findViewById(R.id.radiopaypal);
                                                RadioButton radioButton3 = (RadioButton) dialogppv.findViewById(R.id.radiostripe);
                                                RadioButton radioButton4 = (RadioButton) dialogppv.findViewById(R.id.termsandcons);
                                                Button btndialog = (Button) dialogppv.findViewById(R.id.proceed);
                                                RecyclerView wes = (RecyclerView) dialogppv.findViewById(R.id.wes);

                                                Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();

                                                        wes.setHasFixedSize(true);
                                                        wes.setLayoutManager(new GridLayoutManager(MediaPlayerPageActivity.this, 1));

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
                                                        new RecyclerItemClickListener(MediaPlayerPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(View view, int position) {

                                                                if (active_payment_settingsList.size() > position) {
                                                                    if (active_payment_settingsList.get(position) != null) {

                                                                        pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                                                    }

                                                                }
                                                            }
                                                        }));
                                                btndialog.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                                            View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                                            BottomSheetDialog dialog1 = new BottomSheetDialog(MediaPlayerPageActivity.this);
                                                            dialog1.setContentView(view);

                                                            CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                                            Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                                            ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                                            CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                                            paynow1.setText("Pay " + ppv_price + ".00");

                                                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                    JSONResponse jsonResponse = response.body();
                                                                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                                                    if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                        key = payment_settingslist.get(0).getTest_publishable_key();
                                                                    } else {
                                                                        key = payment_settingslist.get(0).getLive_publishable_key();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                }
                                                            });
                                                            paynow1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    paynow1.setVisibility(View.GONE);
                                                                    payment_progress.setVisibility(View.VISIBLE);

                                                                    if (!check.isChecked()) {
                                                                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                        payment_progress.setVisibility(View.GONE);
                                                                    } else {

                                                                        params = cardMultilineWidget.getPaymentMethodCreateParams();

                                                                        if (params == null) {
                                                                            return;
                                                                        }
                                                                        stripe = new Stripe(getApplicationContext(), key);
                                                                        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                                                            @Override
                                                                            public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                                                py_id = paymentMethod.id;
                                                                                Api.getClient().getAddPayperViewAudio(user_id, audio_id, py_id, "stripe", ppv_price,"Android", new Callback<Addpayperview>() {

                                                                                    @Override
                                                                                    public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                                                                                        dialog1.cancel();
                                                                                        addpayperview = addpayperview1;
                                                                                        if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            mediaplayer.start();
                                                                                            dialog.cancel();
                                                                                            blur_image.setVisibility(View.GONE);
                                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                                            pausebutton1.setVisibility(View.VISIBLE);

                                                                                        } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            paynow1.setVisibility(View.VISIBLE);
                                                                                            payment_progress.setVisibility(View.GONE);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void failure(RetrofitError error) {

                                                                                        Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                                        payment_progress.setVisibility(View.GONE);
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onError(@NotNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                                                paynow1.setVisibility(View.VISIBLE);
                                                                                payment_progress.setVisibility(View.GONE);

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });

                                                            dialog1.show();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                                            startPayment();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                                            dialogppv.dismiss();
                                                            dialog.cancel();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            Intent intent = new Intent(MediaPlayerPageActivity.this, CheckoutActivity.class);
                                                            intent.putExtra("price", ppv_price);
                                                            intent.putExtra("id", audio_id);
                                                            intent.putExtra("url", "");
                                                            intent.putExtra("type", "audio_rent");
                                                            startActivity(intent);

                                                        } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                                            dialogppv.dismiss();

                                                            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                                    JSONResponse jsonResponse2 = response2.body();
                                                                    ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                                    for (int j = 0; j < payment_settingslist.size(); j++) {
                                                                        if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                                            blur_image.setVisibility(View.GONE);
                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                            Intent in = new Intent(MediaPlayerPageActivity.this, MyCinetPayActivity.class);
                                                                            in.putExtra("id", audio_id);
                                                                            in.putExtra("url", "");
                                                                            in.putExtra("type", "audio_rent");
                                                                            in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                                            in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                                            in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                                            in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                                            in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                                            in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Audio Purchase");
                                                                            in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                                            startActivity(in);

                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                                }
                                                            });
                                                        } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            getPayment();
                                                        }

                                                    }

                                                });

                                            }
                                        });
                                    }else {
                                        pausebutton1.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    pausebutton1.setVisibility(View.VISIBLE);
                                }

                            } else {

                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                mediaplayer.stop();
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });
                }
            }
        });


        buttonpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                music_progress.setVisibility(View.VISIBLE);
                pausebutton1.setVisibility(View.GONE);
                playbutton1.setVisibility(View.GONE);


                if (repeatonce.getVisibility() == View.VISIBLE) {
                    mediaplayer.setLooping(true);
                }

                if (suffleMode) {
                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getSuffleAudios(audio_id);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                                ArrayList<audio_shufffle> suffle_audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio_shufffle()));


                                title_playlist.setText("Playing From " + jsonResponse.getMain_genre());
                                music_progress.setVisibility(View.GONE);
                                pausebutton1.setVisibility(View.VISIBLE);
                                playbutton1.setVisibility(View.GONE);

                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                //    editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.audioid, suffle_audiolist.get(0).getId());
                                editor.apply();
                                editor.commit();

                                audio_id = prefs.getString(sharedpreferences.audioid, "");


                                nexturl = suffle_audiolist.get(0).getMp3_url();
                                AudioDetails(suffle_audiolist.get(0).getId());
                                mediaplayer.stop();
                                mediaplayer = MediaPlayer.create(MediaPlayerPageActivity.this, Uri.parse(suffle_audiolist.get(0).getMp3_url()));

                                finalTime = mediaplayer.getDuration();
                                startTime = mediaplayer.getCurrentPosition();


                                tx2.setText(String.format("%02d : %02d ",
                                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                        finalTime)))
                                );

                                seekbar.setProgress((int) startTime);
                                myHandler.postDelayed(UpdateSongTime, 1000);
                                mediaplayer.start();
                                setMorelikethis();
                                if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                                } else if (suffle_audiolist.get(0).getAccess().equalsIgnoreCase("subscriber") || suffle_audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {//check this line

                                    if (!(suffle_audiolist.get(0).getPpv_status().equalsIgnoreCase("can_view"))) {
                                        mediaplayer.pause();
                                        pausebutton1.setVisibility(View.GONE);
                                        playbutton1.setVisibility(View.GONE);
                                        music_progress.setVisibility(View.GONE);
                                        blur_image.setVisibility(View.VISIBLE);
                                        audio_page.setVisibility(View.GONE);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                                        AlertDialog dialog = alert.create();
                                        dialog.setView(view);
                                        dialog.show();

                                        CardView subscribe = view.findViewById(R.id.subscribe);
                                        CardView rent = view.findViewById(R.id.rent);
                                        ImageView cross = view.findViewById(R.id.cancel);
                                        TextView content = view.findViewById(R.id.content);
                                        TextView desc_content = view.findViewById(R.id.desc_content);

                                        if (suffle_audiolist.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                                            content.setText("Subscription Content");
                                            desc_content.setText("You are not a subscribed user, Buy subscription to get access for the content.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                        } else if (suffle_audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                            content.setText("Pay-Per-View Content");
                                            desc_content.setText("Pay for and watch specific content without a subscription.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                            rent.setVisibility(View.VISIBLE);
                                        }

                                        cross.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        subscribe.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                                Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                                startActivity(in);
                                            }
                                        });
                                        rent.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ppv_price = suffle_audiolist.get(0).getPpv_price();

                                                dialogppv = new Dialog(MediaPlayerPageActivity.this);
                                                dialogppv.setCancelable(true);
                                                dialogppv.setContentView(R.layout.dialogbox);
                                                dialogppv.show();

                                                RadioButton radioButton = (RadioButton) dialogppv.findViewById(R.id.termsandcon);
                                                RadioButton radioButton1 = (RadioButton) dialogppv.findViewById(R.id.radiorazor);
                                                RadioButton radioButton2 = (RadioButton) dialogppv.findViewById(R.id.radiopaypal);
                                                RadioButton radioButton3 = (RadioButton) dialogppv.findViewById(R.id.radiostripe);
                                                RadioButton radioButton4 = (RadioButton) dialogppv.findViewById(R.id.termsandcons);
                                                Button btndialog = (Button) dialogppv.findViewById(R.id.proceed);
                                                RecyclerView wes = (RecyclerView) dialogppv.findViewById(R.id.wes);

                                                Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();

                                                        wes.setHasFixedSize(true);
                                                        wes.setLayoutManager(new GridLayoutManager(MediaPlayerPageActivity.this, 1));

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
                                                        new RecyclerItemClickListener(MediaPlayerPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(View view, int position) {

                                                                if (active_payment_settingsList.size() > position) {
                                                                    if (active_payment_settingsList.get(position) != null) {

                                                                        pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                                                    }

                                                                }
                                                            }
                                                        }));
                                                btndialog.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                                            View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                                            BottomSheetDialog dialog1 = new BottomSheetDialog(MediaPlayerPageActivity.this);
                                                            dialog1.setContentView(view);

                                                            CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                                            Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                                            ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                                            CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                                            paynow1.setText("Pay " + ppv_price + ".00");

                                                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                    JSONResponse jsonResponse = response.body();
                                                                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                                                    if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                        key = payment_settingslist.get(0).getTest_publishable_key();
                                                                    } else {
                                                                        key = payment_settingslist.get(0).getLive_publishable_key();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                }
                                                            });
                                                            paynow1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    paynow1.setVisibility(View.GONE);
                                                                    payment_progress.setVisibility(View.VISIBLE);

                                                                    if (!check.isChecked()) {
                                                                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                        payment_progress.setVisibility(View.GONE);
                                                                    } else {

                                                                        params = cardMultilineWidget.getPaymentMethodCreateParams();

                                                                        if (params == null) {
                                                                            return;
                                                                        }
                                                                        stripe = new Stripe(getApplicationContext(), key);
                                                                        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                                                            @Override
                                                                            public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                                                py_id = paymentMethod.id;
                                                                                Api.getClient().getAddPayperViewAudio(user_id, audio_id, py_id, "stripe", ppv_price,"Android", new Callback<Addpayperview>() {

                                                                                    @Override
                                                                                    public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                                                                                        dialog1.cancel();
                                                                                        addpayperview = addpayperview1;
                                                                                        if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            mediaplayer.start();
                                                                                            dialog.cancel();
                                                                                            blur_image.setVisibility(View.GONE);
                                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                                            pausebutton1.setVisibility(View.VISIBLE);

                                                                                        } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            paynow1.setVisibility(View.VISIBLE);
                                                                                            payment_progress.setVisibility(View.GONE);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void failure(RetrofitError error) {

                                                                                        Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                                        payment_progress.setVisibility(View.GONE);
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onError(@NotNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                                                paynow1.setVisibility(View.VISIBLE);
                                                                                payment_progress.setVisibility(View.GONE);

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });

                                                            dialog1.show();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                                            startPayment();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                                            dialogppv.dismiss();
                                                            dialog.cancel();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            Intent intent = new Intent(MediaPlayerPageActivity.this, CheckoutActivity.class);
                                                            intent.putExtra("price", ppv_price);
                                                            intent.putExtra("id", audio_id);
                                                            intent.putExtra("url", "");
                                                            intent.putExtra("type", "audio_rent");
                                                            startActivity(intent);

                                                        } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                                            dialogppv.dismiss();

                                                            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                                    JSONResponse jsonResponse2 = response2.body();
                                                                    ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                                    for (int j = 0; j < payment_settingslist.size(); j++) {
                                                                        if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                                            blur_image.setVisibility(View.GONE);
                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                            Intent in = new Intent(MediaPlayerPageActivity.this, MyCinetPayActivity.class);
                                                                            in.putExtra("id", audio_id);
                                                                            in.putExtra("url", "");
                                                                            in.putExtra("type", "audio_rent");
                                                                            in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                                            in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                                            in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                                            in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                                            in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                                            in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Audio Purchase");
                                                                            in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                                            startActivity(in);

                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                                }
                                                            });
                                                        } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            getPayment();
                                                        }

                                                    }

                                                });

                                            }
                                        });
                                    }else {
                                        pausebutton1.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    pausebutton1.setVisibility(View.VISIBLE);
                                }

                            } else {

                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                mediaplayer.stop();
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });
                } else {

                    Call<JSONResponse> res = ApiClient.getInstance1().getApi().getPrevAudioDetail(user_id, audio_id);
                    res.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                            JSONResponse jsonResponse = response.body();

                            if (jsonResponse.getStatus().equalsIgnoreCase("true")) {

                                title_playlist.setText("Playing From " + jsonResponse.getMain_genre());


                                audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));

                                music_progress.setVisibility(View.GONE);
                                pausebutton1.setVisibility(View.VISIBLE);
                                playbutton1.setVisibility(View.GONE);

                                preurl = audiolist.get(0).getMp3_url();
                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                                editor.apply();
                                editor.commit();

                                audio_id = prefs.getString(sharedpreferences.audioid, "");

                                AudioDetails(audiolist.get(0).getId());
                                mediaplayer.stop();
                                mediaplayer = MediaPlayer.create(MediaPlayerPageActivity.this, Uri.parse(audiolist.get(0).getMp3_url()));
                                finalTime = mediaplayer.getDuration();
                                startTime = mediaplayer.getCurrentPosition();

                                seekbar.setMax((int) finalTime);

                                tx2.setText(String.format("%02d : %02d ",
                                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                        finalTime)))
                                );

                                seekbar.setProgress((int) startTime);
                                myHandler.postDelayed(UpdateSongTime, 1000);
                                mediaplayer.start();
                                setMorelikethis();
                                if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                                } else if (audiolist.get(0).getAccess().equalsIgnoreCase("subscriber") || audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {//check this line

                                    if (!(audiolist.get(0).getPpv_status().equalsIgnoreCase("can_view"))) {
                                        mediaplayer.pause();
                                        pausebutton1.setVisibility(View.GONE);
                                        playbutton1.setVisibility(View.GONE);
                                        music_progress.setVisibility(View.GONE);
                                        blur_image.setVisibility(View.VISIBLE);
                                        audio_page.setVisibility(View.GONE);
                                        AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                                        View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                                        AlertDialog dialog = alert.create();
                                        dialog.setView(view);
                                        dialog.show();

                                        CardView subscribe = view.findViewById(R.id.subscribe);
                                        CardView rent = view.findViewById(R.id.rent);
                                        ImageView cross = view.findViewById(R.id.cancel);
                                        TextView content = view.findViewById(R.id.content);
                                        TextView desc_content = view.findViewById(R.id.desc_content);

                                        if (audiolist.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                                            content.setText("Subscription Content");
                                            desc_content.setText("You are not a subscribed user, Buy subscription to get access for the content.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                        } else if (audiolist.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                            content.setText("Pay-Per-View Content");
                                            desc_content.setText("Pay for and watch specific content without a subscription.!");
                                            subscribe.setVisibility(View.VISIBLE);
                                            rent.setVisibility(View.VISIBLE);
                                        }

                                        cross.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        subscribe.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                                blur_image.setVisibility(View.GONE);
                                                audio_page.setVisibility(View.VISIBLE);
                                                Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                                startActivity(in);
                                            }
                                        });
                                        rent.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ppv_price = audiolist.get(0).getPpv_price();

                                                dialogppv = new Dialog(MediaPlayerPageActivity.this);
                                                dialogppv.setCancelable(true);
                                                dialogppv.setContentView(R.layout.dialogbox);
                                                dialogppv.show();

                                                RadioButton radioButton = (RadioButton) dialogppv.findViewById(R.id.termsandcon);
                                                RadioButton radioButton1 = (RadioButton) dialogppv.findViewById(R.id.radiorazor);
                                                RadioButton radioButton2 = (RadioButton) dialogppv.findViewById(R.id.radiopaypal);
                                                RadioButton radioButton3 = (RadioButton) dialogppv.findViewById(R.id.radiostripe);
                                                RadioButton radioButton4 = (RadioButton) dialogppv.findViewById(R.id.termsandcons);
                                                Button btndialog = (Button) dialogppv.findViewById(R.id.proceed);
                                                RecyclerView wes = (RecyclerView) dialogppv.findViewById(R.id.wes);

                                                Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                    @Override
                                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                        JSONResponse jsonResponse = response.body();

                                                        wes.setHasFixedSize(true);
                                                        wes.setLayoutManager(new GridLayoutManager(MediaPlayerPageActivity.this, 1));

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
                                                        new RecyclerItemClickListener(MediaPlayerPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(View view, int position) {

                                                                if (active_payment_settingsList.size() > position) {
                                                                    if (active_payment_settingsList.get(position) != null) {

                                                                        pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                                                    }

                                                                }
                                                            }
                                                        }));
                                                btndialog.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                                            View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                                            BottomSheetDialog dialog1 = new BottomSheetDialog(MediaPlayerPageActivity.this);
                                                            dialog1.setContentView(view);

                                                            CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                                            Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                                            ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                                            CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                                            paynow1.setText("Pay " + ppv_price + ".00");

                                                            Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                    JSONResponse jsonResponse = response.body();
                                                                    payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                                                    if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                        key = payment_settingslist.get(0).getTest_publishable_key();
                                                                    } else {
                                                                        key = payment_settingslist.get(0).getLive_publishable_key();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                }
                                                            });
                                                            paynow1.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    paynow1.setVisibility(View.GONE);
                                                                    payment_progress.setVisibility(View.VISIBLE);

                                                                    if (!check.isChecked()) {
                                                                        Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                        payment_progress.setVisibility(View.GONE);
                                                                    } else {

                                                                        params = cardMultilineWidget.getPaymentMethodCreateParams();

                                                                        if (params == null) {
                                                                            return;
                                                                        }
                                                                        stripe = new Stripe(getApplicationContext(), key);
                                                                        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                                                            @Override
                                                                            public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                                                py_id = paymentMethod.id;
                                                                                Api.getClient().getAddPayperViewAudio(user_id, audio_id, py_id, "stripe", ppv_price,"Android", new Callback<Addpayperview>() {

                                                                                    @Override
                                                                                    public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                                                                                        dialog1.cancel();
                                                                                        addpayperview = addpayperview1;
                                                                                        if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            mediaplayer.start();
                                                                                            blur_image.setVisibility(View.GONE);
                                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                                            pausebutton1.setVisibility(View.VISIBLE);

                                                                                        } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                                            Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                            paynow1.setVisibility(View.VISIBLE);
                                                                                            payment_progress.setVisibility(View.GONE);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void failure(RetrofitError error) {

                                                                                        Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                                        payment_progress.setVisibility(View.GONE);
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onError(@NotNull Exception e) {
                                                                                Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                                                paynow1.setVisibility(View.VISIBLE);
                                                                                payment_progress.setVisibility(View.GONE);

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });

                                                            dialog1.show();
                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                                            startPayment();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                        } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                                            dialogppv.dismiss();
                                                            dialog.cancel();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            Intent intent = new Intent(MediaPlayerPageActivity.this, CheckoutActivity.class);
                                                            intent.putExtra("price", ppv_price);
                                                            intent.putExtra("id", audio_id);
                                                            intent.putExtra("url", "");
                                                            intent.putExtra("type", "audio_rent");
                                                            startActivity(intent);

                                                        } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                                            dialogppv.dismiss();

                                                            Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                            pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                @Override
                                                                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                                    JSONResponse jsonResponse2 = response2.body();
                                                                    ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                                    for (int j = 0; j < payment_settingslist.size(); j++) {
                                                                        if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                                            blur_image.setVisibility(View.GONE);
                                                                            audio_page.setVisibility(View.VISIBLE);
                                                                            Intent in = new Intent(MediaPlayerPageActivity.this, MyCinetPayActivity.class);
                                                                            in.putExtra("id", audio_id);
                                                                            in.putExtra("url", "");
                                                                            in.putExtra("type", "audio_rent");
                                                                            in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                                            in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                                            in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                                            in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                                            in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                                            in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Audio Purchase");
                                                                            in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                                            startActivity(in);

                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                                }
                                                            });
                                                        } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                                            dialog.cancel();
                                                            dialogppv.dismiss();
                                                            blur_image.setVisibility(View.GONE);
                                                            audio_page.setVisibility(View.VISIBLE);
                                                            getPayment();
                                                        }

                                                    }

                                                });

                                            }
                                        });
                                    }else {
                                        pausebutton1.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    pausebutton1.setVisibility(View.VISIBLE);
                                }

                            } else {

                                Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                                mediaplayer.stop();
                                finish();
                            }

                        }


                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });
                }

            }
        });

        buttonrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                repeatonce.setVisibility(View.VISIBLE);
                buttonrepeat.setVisibility(View.GONE);

                mediaplayer.setLooping(true);

            }
        });


        repeatonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                repeatonce.setVisibility(View.GONE);
                buttonrepeat.setVisibility(View.VISIBLE);

                mediaplayer.setLooping(false);
            }
        });

        greenshuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenshuffle.setVisibility(View.GONE);
                buttonShuffle.setVisibility(View.VISIBLE);
                suffleMode = false;
            }
        });

       /* mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(final MediaPlayer theMediaPlayer) {

                if (repeatonce.getVisibility() == View.VISIBLE) {
                    mediaplayer.setLooping(true);
                }

                music_progress.setVisibility(View.VISIBLE);
                pausebutton1.setVisibility(View.GONE);
                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getNextAudioDetail(user_id, audio_id);
                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                        JSONResponse jsonResponse = response.body();

                        if (jsonResponse.getStatus().equalsIgnoreCase("true")) {


                            audiolist = new ArrayList<>(Arrays.asList(jsonResponse.getAudio()));

                            music_progress.setVisibility(View.GONE);
                            pausebutton1.setVisibility(View.VISIBLE);

                            SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                            editor.putBoolean(sharedpreferences.login, true);
                            editor.putString(sharedpreferences.audioid, audiolist.get(0).getId());
                            editor.apply();
                            editor.commit();

                            audio_id = prefs.getString(sharedpreferences.audioid, "");

                            AudioDetails(audiolist.get(0).getId());

                            mediaplayer.stop();
                            mediaplayer = MediaPlayer.create(MediaPlayerPageActivity.this, Uri.parse(audiolist.get(0).getMp3_url()));


                            finalTime = mediaplayer.getDuration();
                            startTime = mediaplayer.getCurrentPosition();


                            seekbar.setMax((int) finalTime);

                            tx2.setText(String.format("%02d : %02d ",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                    finalTime)))
                            );


                            seekbar.setProgress((int) startTime);
                            myHandler.postDelayed(UpdateSongTime, 1000);
                            mediaplayer.start();

                            if (audiolist.get(0).getAccess().equalsIgnoreCase("subscriber") && !(user_role.equalsIgnoreCase("admin") ||user_role.equalsIgnoreCase("subscriber"))){

                                mediaplayer.pause();
                                pausebutton1.setVisibility(View.GONE);
                                playbutton1.setVisibility(View.GONE);
                                music_progress.setVisibility(View.GONE);
                                blur_image.setVisibility(View.VISIBLE);
                                audio_page.setVisibility(View.GONE);
                                AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                                AlertDialog dialog=alert.create();
                                dialog.setView(view);
                                dialog.show();

                                CardView subscribe=view.findViewById(R.id.subscribe);
                                ImageView cross=view.findViewById(R.id.cancel);

                                cross.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        blur_image.setVisibility(View.GONE);
                                        audio_page.setVisibility(View.VISIBLE);
                                    }
                                });
                                subscribe.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        blur_image.setVisibility(View.GONE);
                                        audio_page.setVisibility(View.VISIBLE);
                                        Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                        startActivity(in);
                                    }
                                });
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "" + jsonResponse.getMessage(), Toast.LENGTH_LONG).show();
                            mediaplayer.stop();
                            finish();

                        }

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });


            }


        });*/


        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                greenshuffle.setVisibility(View.VISIBLE);
                buttonShuffle.setVisibility(View.GONE);
                suffleMode = true;

            }
        });
        show_lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (lyrics.getVisibility()==View.VISIBLE){
                    //audio_thumb.setVisibility(View.VISIBLE);
                    lyrics.setVisibility(View.GONE);
                }else {
                    //audio_thumb.setVisibility(View.GONE);
                    lyrics.setVisibility(View.VISIBLE);
                }*/
                lyrics.setVisibility(View.VISIBLE);
                show_lyrics.setVisibility(View.GONE);
                showed_lyrics.setVisibility(View.VISIBLE);
            }
        });
        showed_lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (lyrics.getVisibility()==View.VISIBLE){
                    //audio_thumb.setVisibility(View.VISIBLE);
                    lyrics.setVisibility(View.GONE);
                }else {
                    //audio_thumb.setVisibility(View.GONE);
                    lyrics.setVisibility(View.VISIBLE);
                }*/
                lyrics.setVisibility(View.GONE);
                show_lyrics.setVisibility(View.VISIBLE);
                showed_lyrics.setVisibility(View.GONE);
            }
        });

        favouritelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Api.getClient().getAddFavouriteAudio(user_id, audio_id, new Callback<Addtowishlistmovie>() {

                        @Override
                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                                favouriteadd.setVisibility(View.VISIBLE);
                                favourite.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                                favouriteadd.setVisibility(View.GONE);
                                favourite.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });

        wishlistlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {

                    Api.getClient().getAddWishlistAudio(user_id, audio_id, new Callback<Addtowishlistmovie>() {

                        @Override
                        public void success(Addtowishlistmovie addwishmovie, retrofit.client.Response response) {

                            addwishmovie = addwishmovie;
                            if (addwishmovie.getStatus().equalsIgnoreCase("true")) {

                                wishlistadd.setVisibility(View.VISIBLE);
                                wishlist.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else if (addwishmovie.getStatus().equalsIgnoreCase("false")) {

                                wishlistadd.setVisibility(View.GONE);
                                wishlist.setVisibility(View.VISIBLE);

                                Toast.makeText(getApplicationContext(), "" + addwishmovie.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not registered user", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {

                            Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });


        likee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getAudioLikecount(user_id, audio_id, "1", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, retrofit.client.Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getLiked().equalsIgnoreCase("1")) {

                            unlikelayout.setVisibility(View.VISIBLE);
                            likee.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "You liked video", Toast.LENGTH_LONG).show();

                            if (addwishmovie.getDisliked().equalsIgnoreCase("0")) {

                                undislikelayout.setVisibility(View.GONE);
                                dislike.setVisibility(View.VISIBLE);
                            }
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your are Not registered user", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });


        unlikelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getAudioLikecount(user_id, audio_id, "0", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, retrofit.client.Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getLiked().equalsIgnoreCase("0")) {

                            likee.setVisibility(View.VISIBLE);
                            unlikelayout.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "You un liked video", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your not registered user ", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api.getClient().getAudioDislikecount(user_id, audio_id, "1", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, retrofit.client.Response response) {
                        //Toast.makeText(getApplicationContext(), " " + addwishmovie.getDisliked(), Toast.LENGTH_SHORT).show();

                        addwishmovie = addwishmovie;

                        if (addwishmovie.getDisliked().equalsIgnoreCase("1")) {

                            undislikelayout.setVisibility(View.VISIBLE);
                            dislike.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "You disliked video", Toast.LENGTH_LONG).show();

                            if (addwishmovie.getLiked().equalsIgnoreCase("0")) {

                                likee.setVisibility(View.VISIBLE);
                                unlikelayout.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your Not Registered user ", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


        undislikelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Api.getClient().getAudioDislikecount(user_id, audio_id, "0", new Callback<likeandunlike>() {

                    @Override
                    public void success(likeandunlike addwishmovie, retrofit.client.Response response) {

                        addwishmovie = addwishmovie;
                        if (addwishmovie.getDisliked().equalsIgnoreCase("0")) {

                            dislike.setVisibility(View.VISIBLE);
                            undislikelayout.setVisibility(View.GONE);

                            Toast.makeText(getApplicationContext(), "Your dislike removed", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Toast.makeText(getApplicationContext(), "Your not a registered user ", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });


        recommandedRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (recomendedaudioslist.size() > position) {
                            if (recomendedaudioslist.get(position) != null) {

                                Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, recomendedaudioslist.get(position).getId());
                                res.enqueue(new retrofit2.Callback<JSONResponse>() {
                                    @Override
                                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                        JSONResponse jsonResponse = response.body();
                                        movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                                        SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                        editor.putBoolean(sharedpreferences.login, true);
                                        editor.putString(sharedpreferences.audioid, movie_detaildata.get(0).getId());
                                        editor.apply();
                                        editor.commit();
                                        audio_id = prefs.getString(sharedpreferences.audioid, "");

                                        AudioDetails(movie_detaildata.get(0).getId());

                                        mediaplayer.stop();
                                        mediaplayer = MediaPlayer.create(MediaPlayerPageActivity.this, Uri.parse(movie_detaildata.get(0).getMp3_url()));


                                        finalTime = mediaplayer.getDuration();
                                        startTime = mediaplayer.getCurrentPosition();


                                        seekbar.setMax((int) finalTime);

                                        tx2.setText(String.format("%02d : %02d ",
                                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                                finalTime)))
                                        );


                                        seekbar.setProgress((int) startTime);
                                        myHandler.postDelayed(UpdateSongTime, 1000);

                                        mediaplayer.start();
                                        scrollView.fullScroll(View.FOCUS_UP);
                                        setMorelikethis();
                                        if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {

                                        } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber") || movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {//check this line

                                            if (!(movie_detaildata.get(0).getPpv_status().equalsIgnoreCase("can_view"))) {
                                                mediaplayer.pause();
                                                pausebutton1.setVisibility(View.GONE);
                                                playbutton1.setVisibility(View.GONE);
                                                music_progress.setVisibility(View.GONE);
                                                blur_image.setVisibility(View.VISIBLE);
                                                audio_page.setVisibility(View.GONE);
                                                AlertDialog.Builder alert = new AlertDialog.Builder(MediaPlayerPageActivity.this);
                                                View view = getLayoutInflater().inflate(R.layout.alert_content, null);
                                                AlertDialog dialog = alert.create();
                                                dialog.setView(view);
                                                dialog.show();

                                                CardView subscribe = view.findViewById(R.id.subscribe);
                                                CardView rent = view.findViewById(R.id.rent);
                                                ImageView cross = view.findViewById(R.id.cancel);
                                                TextView content = view.findViewById(R.id.content);
                                                TextView desc_content = view.findViewById(R.id.desc_content);

                                                if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("subscriber")) {
                                                    content.setText("Subscription Content");
                                                    desc_content.setText("You are not a subscribed user, Buy subscription to get access for the content.!");
                                                    subscribe.setVisibility(View.VISIBLE);
                                                } else if (movie_detaildata.get(0).getAccess().equalsIgnoreCase("ppv")) {
                                                    content.setText("Pay-Per-View Content");
                                                    desc_content.setText("Pay for and watch specific content without a subscription.!");
                                                    subscribe.setVisibility(View.VISIBLE);
                                                    rent.setVisibility(View.VISIBLE);
                                                }

                                                cross.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.cancel();
                                                        blur_image.setVisibility(View.GONE);
                                                        audio_page.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                                subscribe.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.cancel();
                                                        blur_image.setVisibility(View.GONE);
                                                        audio_page.setVisibility(View.VISIBLE);
                                                        Intent in = new Intent(MediaPlayerPageActivity.this, SubscribeActivity.class);
                                                        startActivity(in);
                                                    }
                                                });
                                                rent.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ppv_price = movie_detaildata.get(0).getPpv_price();

                                                        dialogppv = new Dialog(MediaPlayerPageActivity.this);
                                                        dialogppv.setCancelable(true);
                                                        dialogppv.setContentView(R.layout.dialogbox);
                                                        dialogppv.show();

                                                        RadioButton radioButton = (RadioButton) dialogppv.findViewById(R.id.termsandcon);
                                                        RadioButton radioButton1 = (RadioButton) dialogppv.findViewById(R.id.radiorazor);
                                                        RadioButton radioButton2 = (RadioButton) dialogppv.findViewById(R.id.radiopaypal);
                                                        RadioButton radioButton3 = (RadioButton) dialogppv.findViewById(R.id.radiostripe);
                                                        RadioButton radioButton4 = (RadioButton) dialogppv.findViewById(R.id.termsandcons);
                                                        Button btndialog = (Button) dialogppv.findViewById(R.id.proceed);
                                                        RecyclerView wes = (RecyclerView) dialogppv.findViewById(R.id.wes);

                                                        Call<JSONResponse> callimgg = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                        callimgg.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                            @Override
                                                            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                JSONResponse jsonResponse = response.body();

                                                                wes.setHasFixedSize(true);
                                                                wes.setLayoutManager(new GridLayoutManager(MediaPlayerPageActivity.this, 1));

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
                                                                new RecyclerItemClickListener(MediaPlayerPageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                                                                    @Override
                                                                    public void onItemClick(View view, int position) {

                                                                        if (active_payment_settingsList.size() > position) {
                                                                            if (active_payment_settingsList.get(position) != null) {

                                                                                pays.setText(active_payment_settingsList.get(position).getPayment_type());
                                                                            }

                                                                        }
                                                                    }
                                                                }));
                                                        btndialog.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (pays.getText().toString().equalsIgnoreCase("stripe")) {

                                                                    View view = getLayoutInflater().inflate(R.layout.activity_payperview_payment_page, null);
                                                                    BottomSheetDialog dialog1 = new BottomSheetDialog(MediaPlayerPageActivity.this);
                                                                    dialog1.setContentView(view);

                                                                    CardMultilineWidget cardMultilineWidget = view.findViewById(R.id.card_input_widget);
                                                                    Button paynow1 = (Button) view.findViewById(R.id.save_payment);
                                                                    ProgressBar payment_progress = (ProgressBar) view.findViewById(R.id.payment_progress);
                                                                    CheckBox check = (CheckBox) view.findViewById(R.id.check);
                                                                    paynow1.setText("Pay " + ppv_price + ".00");

                                                                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                                    call.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                        @Override
                                                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                                                                            JSONResponse jsonResponse = response.body();
                                                                            payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse.getPayment_settings()));
                                                                            if (payment_settingslist.get(0).getLive_mode().equalsIgnoreCase(String.valueOf(0))) {
                                                                                key = payment_settingslist.get(0).getTest_publishable_key();
                                                                            } else {
                                                                                key = payment_settingslist.get(0).getLive_publishable_key();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                                                                        }
                                                                    });
                                                                    paynow1.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            paynow1.setVisibility(View.GONE);
                                                                            payment_progress.setVisibility(View.VISIBLE);

                                                                            if (!check.isChecked()) {
                                                                                Toast.makeText(getApplicationContext(), "Accept Terms & Condition", Toast.LENGTH_LONG).show();
                                                                                paynow1.setVisibility(View.VISIBLE);
                                                                                payment_progress.setVisibility(View.GONE);
                                                                            } else {

                                                                                params = cardMultilineWidget.getPaymentMethodCreateParams();

                                                                                if (params == null) {
                                                                                    return;
                                                                                }
                                                                                stripe = new Stripe(getApplicationContext(), key);
                                                                                stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                                                                                    @Override
                                                                                    public void onSuccess(@NotNull PaymentMethod paymentMethod) {
                                                                                        py_id = paymentMethod.id;
                                                                                        Api.getClient().getAddPayperViewAudio(user_id, audio_id, py_id, "stripe", ppv_price,"Android", new Callback<Addpayperview>() {

                                                                                            @Override
                                                                                            public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                                                                                                dialog1.cancel();
                                                                                                addpayperview = addpayperview1;
                                                                                                if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                                                                                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                                    mediaplayer.start();
                                                                                                    dialog.cancel();
                                                                                                    blur_image.setVisibility(View.GONE);
                                                                                                    audio_page.setVisibility(View.VISIBLE);
                                                                                                    pausebutton1.setVisibility(View.VISIBLE);

                                                                                                } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                                                                                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                                                                                    paynow1.setVisibility(View.VISIBLE);
                                                                                                    payment_progress.setVisibility(View.GONE);
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void failure(RetrofitError error) {

                                                                                                Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
                                                                                                paynow1.setVisibility(View.VISIBLE);
                                                                                                payment_progress.setVisibility(View.GONE);
                                                                                            }
                                                                                        });
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(@NotNull Exception e) {
                                                                                        Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                                                        paynow1.setVisibility(View.VISIBLE);
                                                                                        payment_progress.setVisibility(View.GONE);

                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });

                                                                    dialog1.show();
                                                                    dialogppv.dismiss();
                                                                } else if (pays.getText().toString().equalsIgnoreCase("razorpay")) {

                                                                    startPayment();
                                                                    blur_image.setVisibility(View.GONE);
                                                                    audio_page.setVisibility(View.VISIBLE);
                                                                    dialog.cancel();
                                                                    dialogppv.dismiss();
                                                                } else if (pays.getText().toString().equalsIgnoreCase("paystack")) {

                                                                    dialogppv.dismiss();
                                                                    dialog.cancel();
                                                                    blur_image.setVisibility(View.GONE);
                                                                    audio_page.setVisibility(View.VISIBLE);
                                                                    Intent intent = new Intent(MediaPlayerPageActivity.this, CheckoutActivity.class);
                                                                    intent.putExtra("price", ppv_price);
                                                                    intent.putExtra("id", audio_id);
                                                                    intent.putExtra("url", "");
                                                                    intent.putExtra("type", "audio_rent");
                                                                    startActivity(intent);

                                                                } else if (pays.getText().toString().equalsIgnoreCase("CinetPay")) {

                                                                    dialogppv.dismiss();
                                                                    dialog.cancel();

                                                                    Call<JSONResponse> pay_api = ApiClient.getInstance1().getApi().getPaymentDetails();
                                                                    pay_api.enqueue(new retrofit2.Callback<JSONResponse>() {
                                                                        @Override
                                                                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response2) {

                                                                            JSONResponse jsonResponse2 = response2.body();
                                                                            ArrayList<payment_settings> payment_settingslist = new ArrayList<>(Arrays.asList(jsonResponse2.getPayment_settings()));

                                                                            for (int j = 0; j < payment_settingslist.size(); j++) {
                                                                                if (payment_settingslist.get(j).getPayment_type().equalsIgnoreCase("CinetPay")) {

                                                                                    blur_image.setVisibility(View.GONE);
                                                                                    audio_page.setVisibility(View.VISIBLE);
                                                                                    Intent in = new Intent(MediaPlayerPageActivity.this, MyCinetPayActivity.class);
                                                                                    in.putExtra("id", audio_id);
                                                                                    in.putExtra("url", "");
                                                                                    in.putExtra("type", "audio_rent");
                                                                                    in.putExtra(CinetPayActivity.KEY_API_KEY, payment_settingslist.get(j).getCinetPay_APIKEY());
                                                                                    in.putExtra(CinetPayActivity.KEY_SITE_ID, payment_settingslist.get(j).getCinetPay_SITE_ID());
                                                                                    in.putExtra(CinetPayActivity.KEY_TRANSACTION_ID, String.valueOf(new Date().getTime()));
                                                                                    in.putExtra(CinetPayActivity.KEY_AMOUNT, ppv_price);
                                                                                    in.putExtra(CinetPayActivity.KEY_CURRENCY, currency);
                                                                                    in.putExtra(CinetPayActivity.KEY_DESCRIPTION, "Audio Purchase");
                                                                                    in.putExtra(CinetPayActivity.KEY_CHANNELS, "MOBILE_MONEY,WALLET,CREDIT_CARD");
                                                                                    startActivity(in);

                                                                                }
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<JSONResponse> call, Throwable t) {

                                                                        }
                                                                    });
                                                                } else if (pays.getText().toString().equalsIgnoreCase("payPal")) {

                                                                    dialog.cancel();
                                                                    dialogppv.dismiss();
                                                                    blur_image.setVisibility(View.GONE);
                                                                    audio_page.setVisibility(View.VISIBLE);
                                                                    getPayment();
                                                                }

                                                            }

                                                        });

                                                    }
                                                });
                                            }else {
                                                pausebutton1.setVisibility(View.VISIBLE);
                                            }
                                        }else {
                                            pausebutton1.setVisibility(View.VISIBLE);
                                        }

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

    }

    private void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Runmawi");
            options.put("description", "Audio On Rent");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            String payment = ppv_price;
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);
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


    @Override
    public void onPaymentSuccess(String s) {

        Api.getClient().getAddPayperViewAudio(user_id, audio_id, "", "razorpay", ppv_price,"Android", new Callback<Addpayperview>() {
            @Override
            public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                addpayperview = addpayperview1;
                if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                    mediaplayer.start();
                    pausebutton1.setVisibility(View.VISIBLE);
                } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                    Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    public void getPayment() {
        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().currencyConverter("United States of America", ppv_price);
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (!(jsonResponse.getCurrency_Converted().isEmpty()) || jsonResponse.getCurrency_Converted() != null) {

                    String[] curencySplit = jsonResponse.getCurrency_Converted().split(" ");
                    String[] remCurr = curencySplit[1].split("");
                    String totalAmount = remCurr[0] + remCurr[1] + remCurr[2] + remCurr[3];
                    Double addTax = Double.parseDouble(totalAmount) + 0.2;
                    paypalamount = String.valueOf(addTax);
                    PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paypalamount)), "USD", "Audio on rent", PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(MediaPlayerPageActivity.this, PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error: ", t.getMessage());
            }
        });
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
                    //Toast.makeText(HomePageVideoActivity.this, "Payment " + state + "\n with payment id is " + payID, Toast.LENGTH_LONG).show();
                    Api.getClient().getAddPayperViewAudio(user_id, audio_id, payID, "PayPal", ppv_price,"Android", new Callback<Addpayperview>() {
                        @Override
                        public void success(Addpayperview addpayperview1, retrofit.client.Response response) {

                            addpayperview = addpayperview1;
                            if (addpayperview.getStatus().equalsIgnoreCase("true")) {
                                Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                                mediaplayer.start();
                                pausebutton1.setVisibility(View.VISIBLE);

                            } else if (addpayperview.getStatus().equalsIgnoreCase("false")) {
                                Toast.makeText(getApplicationContext(), addpayperview.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


                } catch (JSONException e) {
                    Toast.makeText(MediaPlayerPageActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MediaPlayerPageActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(MediaPlayerPageActivity.this, "Invalid Payment", Toast.LENGTH_LONG).show();
        } else if (requestCode == Activity.RESULT_CANCELED) {
            Toast.makeText(MediaPlayerPageActivity.this, "Payment Canceled", Toast.LENGTH_LONG).show();
        }
    }

    private void setMorelikethis() {

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        audio_id = prefs.getString(sharedpreferences.audioid, "");
        recomendedaudioslist.clear();
        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getRecommendedAudio(audio_id);
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                if (jsonResponse.getRecomendedaudios().length == 0) {
                    recommand_laylot.setVisibility(View.GONE);
                } else {
                    recommand_laylot.setVisibility(View.VISIBLE);
                    recomendedaudioslist = new ArrayList<>(Arrays.asList(jsonResponse.getRecomendedaudios()));
                    recommandedAdopter = new RecommandedAdopter(recomendedaudioslist);
                    recommandedRecycler.setAdapter(recommandedAdopter);
                    recommandedAdopter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void AudioDetails(String id) {

        Call<JSONResponse> res = ApiClient.getInstance1().getApi().getAudioDetail(user_id, id);
        res.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                movie_detaildata = new ArrayList<>(Arrays.asList(jsonResponse.getAudiodetail()));

                if (jsonResponse.getAudiodetail().length == 0) {

                } else {
                    audio_title.setText(movie_detaildata.get(0).getTitle());
                    audio_description.setText(movie_detaildata.get(0).getDescription());
                    Picasso.get().load(movie_detaildata.get(0).getImage()).into(audio_thumb);
                    album_id = movie_detaildata.get(0).getAlbum_id();

                }

                if (jsonResponse.getWatchlater().equalsIgnoreCase("true")) {

                    wishlistadd.setVisibility(View.VISIBLE);
                    wishlist.setVisibility(View.GONE);
                } else {
                    wishlistadd.setVisibility(View.GONE);
                    wishlist.setVisibility(View.VISIBLE);

                }

                if (jsonResponse.getFavorite().equalsIgnoreCase("true")) {

                    favouriteadd.setVisibility(View.VISIBLE);
                    favourite.setVisibility(View.GONE);
                } else {
                    favouriteadd.setVisibility(View.GONE);
                    favourite.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            try {
                startTime = mediaplayer.getCurrentPosition();
            } catch (Exception e) {
            }

            tx1.setText(String.format("%02d : %02d ",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaplayer.stop();
    }
}
