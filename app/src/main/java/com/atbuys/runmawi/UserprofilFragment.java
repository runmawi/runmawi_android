package com.atbuys.runmawi;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserprofilFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    String user_id, user_role;
    private ArrayList<user_details> user_detailsdata;
    private ArrayList<sub_users> subuserlist;
    TextView username, userimagename, useremail,user_rolle;
    ImageView userprofile, backarrow, subuserprofile4;
    BottomSheetDialog dialog, dialog1;
    String child_type;
    EditText child_name;
    SubuserAdapter subuserAdapter;
    RecyclerView subserRecyclerView;
    private Bitmap bitmap, bitmap1;
    CircleImageView img, imgg;
    ImageView spinner_arrow,logo;
    private ArrayList<Site_theme_setting> Site_theme_setting;

    private RecyclerView.LayoutManager trendingManeger;
    int PICK_IMAGE_REQUEST = 111;

    RequestBody requestFile;
    MultipartBody.Part body2;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Switch dark_icon;
    String theme_change;


    String[] country = {"Normal", "Kids"};
    ProgressDialog progressDialog;

    LinearLayout appsettings, help_center, useraccount,delete_acc, privacy_poliicy,refund_policy,cancellation_policy, logout, notification, download, language,fav_layout,wish_layout,about,terms,ppv_layout;
    LinearLayout signout;
    private GoogleApiClient googleApiClient;
    CardView premium,cancel_premium;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            getActivity().setTheme(R.style.darktheme);
        }
        else {
            getActivity().setTheme(R.style.AppTheme);
        }
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_userprofil, null);

        subuserlist = new ArrayList<sub_users>();

        username = (TextView) root.findViewById(R.id.username);
        userimagename = (TextView) root.findViewById(R.id.userimagename);
        useremail = (TextView) root.findViewById(R.id.user_mail);
        user_rolle = (TextView) root.findViewById(R.id.user_rolle);
        userprofile = (ImageView) root.findViewById(R.id.userimage);
        appsettings = (LinearLayout) root.findViewById(R.id.settings);
        help_center = (LinearLayout) root.findViewById(R.id.help_center);
        fav_layout=(LinearLayout)root.findViewById(R.id.fav_layout);
        ppv_layout=(LinearLayout)root.findViewById(R.id.ppv_layout);
        premium=(CardView)root.findViewById(R.id.premium);
        cancel_premium=(CardView)root.findViewById(R.id.cancel_premium);
        wish_layout=(LinearLayout)root.findViewById(R.id.wish_layout);
        privacy_poliicy = (LinearLayout) root.findViewById(R.id.privacy);
        cancellation_policy=(LinearLayout)root.findViewById(R.id.cancellation_policy);
        refund_policy=(LinearLayout) root.findViewById(R.id.refund_policy);
        about=(LinearLayout)root.findViewById(R.id.aboutus);
        terms=(LinearLayout)root.findViewById(R.id.terms);
        logout = (LinearLayout) root.findViewById(R.id.logout);
        useraccount = (LinearLayout) root.findViewById(R.id.useraccount);
        delete_acc = (LinearLayout) root.findViewById(R.id.delete_acc);
        backarrow = (ImageView) root.findViewById(R.id.backarrow);
        notification = (LinearLayout) root.findViewById(R.id.notification);
        download = (LinearLayout) root.findViewById(R.id.download);
        language = (LinearLayout) root.findViewById(R.id.language);
        dark_icon = (Switch) root.findViewById(R.id.dark_icon);
        logo=(ImageView)root.findViewById(R.id.logo);
        Site_theme_setting = new ArrayList<Site_theme_setting>();

        progressDialog = new ProgressDialog(getActivity());
        subserRecyclerView = (RecyclerView) root.findViewById(R.id.subuser_profile_recycler);

        subuserAdapter = new SubuserAdapter(subuserlist, this.getContext());
        trendingManeger = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);


        subserRecyclerView.setHasFixedSize(true);
        subserRecyclerView.setLayoutManager(trendingManeger);
        subserRecyclerView.setAdapter(subuserAdapter);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        subuserprofile4 = (ImageView) root.findViewById(R.id.subuserprofile4);


        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);

        prefs = this.getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_id = prefs.getString(sharedpreferences.user_id, null);
        user_role = prefs.getString(sharedpreferences.role, null);
        theme_change=prefs.getString(sharedpreferences.theme,null);

        /*if (theme_change.equalsIgnoreCase("1")){
            dark_icon.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }*/

        dark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dark_icon.setChecked(dark_icon.isChecked());
                editor=prefs.edit();

                if (dark_icon.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putString(sharedpreferences.theme, String.valueOf(1));
                    //editor.putBoolean("theme_mode",true);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putString(sharedpreferences.theme, String.valueOf(0));
                    //editor.putBoolean("theme_mode",false);
                }
                editor.apply();
            }
        });//


        Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().getthemeSettings();
        callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();

                Site_theme_setting = new ArrayList<>(Arrays.asList(jsonResponse.getSite_theme_setting()));

                String x = Site_theme_setting.get(0).getImage_url();
                Picasso.get().load(x).into(logo);

            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        final Handler handler1 = new Handler();
        final Runnable Update1 = new Runnable() {
            public void run() {

                Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getChildProfile(user_id);
                callser.enqueue(new retrofit2.Callback<JSONResponse>() {
                    @Override
                    public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {


                        JSONResponse jsonResponse = response.body();

                        //   latesttitle.setText(jsonResponse.getDiaplay_name());

                       /* if (jsonResponse.getSub_users().length == 0) {


                        } else {

                            subuserlist = new ArrayList<>(Arrays.asList(jsonResponse.getSub_users()));
                            subuserAdapter = new SubuserAdapter(subuserlist);
                            subserRecyclerView.setAdapter(subuserAdapter);

                        }*/

                    }

                    @Override
                    public void onFailure(Call<JSONResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });

            }

        };
        Timer swipeTimerr = new Timer();
        swipeTimerr.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(Update1);
            }
        }, 1000, 1000);

        subserRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        if (subuserlist.size() > position) {
                            if (subuserlist.get(position) != null) {


                                View views = getLayoutInflater().inflate(R.layout.activity_childser_update, null);
                                dialog1 = new BottomSheetDialog(getActivity());
                                dialog1.setContentView(views);
                                dialog1.show();


                                ImageView check = views.findViewById(R.id.check);
                                EditText child_namee = (EditText) views.findViewById(R.id.namee1);
                                imgg = (CircleImageView) views.findViewById(R.id.userimage1);
                                EditText chilt_typee = (EditText) views.findViewById(R.id.names);


                                //        Toast.makeText(getActivity(),""+subuserlist.get(position).getId(),Toast.LENGTH_LONG).show();
                                child_namee.setText(subuserlist.get(position).getUser_name());
                                chilt_typee.setText(subuserlist.get(position).getUser_type());
                                Picasso.get().load("https://runmawi.com/public/uploads/avatars/" + subuserlist.get(position).getAvatar()).into(imgg);

                                child_namee.setInputType(InputType.TYPE_NULL);
                                chilt_typee.setInputType(InputType.TYPE_NULL);


                                imgg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_PICK);
                                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                                    }
                                });


                                check.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        if (bitmap == null) {

                                            Toast.makeText(getActivity(), "Select your avatar", Toast.LENGTH_LONG).show();

                                        } else {


                                            RequestBody user_idd = RequestBody.create(MediaType.parse("text/plain"), subuserlist.get(position).getId());
                                            // RequestBody type = RequestBody.create(MediaType.parse("text/plain"), child_type);


                                            Gson gson = new GsonBuilder()
                                                    .setLenient()
                                                    .create();

                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(Apiimage.BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                                    .build();

                                            Apiimage api = retrofit.create(Apiimage.class);

                                            Call<MyResponse> call = api.getChildupdate(body2, user_idd);

                                            call.enqueue(new retrofit2.Callback<MyResponse>() {
                                                @Override
                                                public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                                    Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                                                    dialog1.cancel();

                                                }


                                                @Override
                                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                                                }

                                            });

                                        }
                                    }
                                });


                            }
                        }
                    }
                }));

        appsettings.setVisibility(View.GONE);


        /*final Handler handler2 = new Handler();
        final Runnable Update2 = new Runnable() {
            public void run() {
                try {
                    prefs = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
                    user_role = prefs.getString(sharedpreferences.role, "guest");

                    Call<JSONResponse> call = ApiClient.getInstance1().getApi().getStripeOnetime();
                    call.enqueue(new retrofit2.Callback<JSONResponse>() {
                        @Override
                        public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                            if (response.body().getPlans().length == 0) {
                                premium.setVisibility(View.GONE);
                                cancel_premium.setVisibility(View.GONE);
                            } else {
                                if (user_role != null) {
                                    if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {
                                        premium.setVisibility(View.GONE);
                                        if (user_role.equalsIgnoreCase("subscriber")) {
                                            cancel_premium.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        premium.setVisibility(View.VISIBLE);
                                        cancel_premium.setVisibility(View.GONE);
                                    }
                                } else {
                                    premium.setVisibility(View.GONE);
                                    cancel_premium.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONResponse> call, Throwable t) {
                        }
                    });

                }catch (Exception e){
                }
            }

        };
        Timer swipeTimerr2 = new Timer();
        swipeTimerr2.schedule(new TimerTask() {
            @Override
            public void run() {
                handler2.post(Update2);
            }
        }, 1000, 5000);*/


        useraccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(in);
            }
        });
        delete_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutBottomSheet bottomSheet = new LogoutBottomSheet("Delete Account");
                bottomSheet.show(getChildFragmentManager(), "bottom sheet");
                bottomSheet.setCancelable(true);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), NotificationSettings.class);
                startActivity(in);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), DownloadSettings.class);
                startActivity(in);
            }
        });
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SecurityActivity.class);
                startActivity(in);
            }
        });

        help_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpCenterActivity.class);
                startActivity(intent);

            }
        });
        fav_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyFavouriteActivity.class);
                startActivity(intent);
            }
        });
        ppv_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), payperviewlistcopyActivity.class);
                startActivity(intent);
            }
        });
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SubscribeActivity.class);
                startActivity(intent);
            }
        });
        cancel_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutBottomSheet bottomSheet = new LogoutBottomSheet("Cancel Premium");
                bottomSheet.show(getChildFragmentManager(), "bottom sheet");
                bottomSheet.setCancelable(true);
            }
        });
        wish_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WatchlistActivity.class);
                startActivity(intent);
            }
        });
        privacy_poliicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent);
            }
        });
        cancellation_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CancellationPolicyActivity.class);
                startActivity(intent);
            }
        });
        refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RefundPolicyActivity.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Aboutus.class);
                startActivity(intent);
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutBottomSheet bottomSheet = new LogoutBottomSheet("Logout");
                bottomSheet.show(getChildFragmentManager(), "bottom sheet");
                bottomSheet.setCancelable(true);
            }
        });


        subuserprofile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.activity_childser, null);
                dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(view);
                dialog.show();

                spinner_arrow = (ImageView) view.findViewById(R.id.spinner_arrow);
                Spinner spin = (Spinner) view.findViewById(R.id.spinner);
                ImageView check = view.findViewById(R.id.check);
                child_name = (EditText) view.findViewById(R.id.nameee);
                img = (CircleImageView) view.findViewById(R.id.userimage);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);

                    }
                });


                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        child_type = String.valueOf(parent.getItemAtPosition(position));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

                ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, country);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spin.setAdapter(aa);

                spinner_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spin.performClick();
                    }
                });

                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), child_name.getText().toString());
                        RequestBody user_idd = RequestBody.create(MediaType.parse("text/plain"), user_id);
                        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), child_type);


                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Apiimage.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        Apiimage api = retrofit.create(Apiimage.class);

                        Call<MyResponse> call = api.getChildCreate(body2, user_idd, username, type);

                        call.enqueue(new retrofit2.Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_LONG).show();
                                dialog.cancel();

                            }


                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                            }

                        });

                    }
                });

            }
        });
        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();


                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        String name;

                        for (int k = 0; k < user_detailsdata.size(); k++) {

                            if (user_detailsdata.get(k).getUsername() == null) {
                                userimagename.setText("");

                            } else {
                                name = user_detailsdata.get(k).getUsername();
                                String[] splited = name.split("\\s+");
                                userimagename.setText(splited[0]);
                            }

                            // username.setText("Welcome "+splited[0]);
                            useremail.setText(user_detailsdata.get(k).getEmail());
                            user_rolle.setText(user_detailsdata.get(k).getRole());

                            String userprofilee = user_detailsdata.get(k).getProfile_url();
                            Picasso.get().load(userprofilee).into(userprofile);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });

        return root;
    }

    @Override
    public void onStart() {

        super.onStart();
        Call<JSONResponse> profileres = ApiClient.getInstance1().getApi().getUserprofile(user_id);
        profileres.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                if (response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        JSONResponse jsonResponse = response.body();


                        user_detailsdata = new ArrayList<>(Arrays.asList(jsonResponse.getUser_details()));

                        String name;

                        for (int k = 0; k < user_detailsdata.size(); k++) {

                            if (user_detailsdata.get(k).getUsername() == null) {
                                userimagename.setText("");

                            } else {
                                name = user_detailsdata.get(k).getUsername();
                                String[] splited = name.split("\\s+");
                                userimagename.setText(splited[0]);
                            }

                            // username.setText("Welcome "+splited[0]);
                            useremail.setText(user_detailsdata.get(k).getEmail());
                            user_rolle.setText(user_detailsdata.get(k).getRole());

                            String userprofilee = user_detailsdata.get(k).getProfile_url();

                            Picasso.get().load(userprofilee).into(userprofile);

                        }

                    }


                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
    }
    private void uploadFile(Uri selectedImage, String my_image) {
        File file = new File(getRealPathFromURI(selectedImage));
        requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(selectedImage)), file);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        body2 = MultipartBody.Part.createFormData("avatar", currentDateTimeString, requestFile);

    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }else {
            Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            uploadFile(filePath, "Myimage");

            try {

                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                InputStream inputStream1 = getActivity().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap1 = BitmapFactory.decodeStream(inputStream1);

                imgg.setImageBitmap(bitmap1);
                img.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        checkSub();
    }

    private void checkSub() {

        prefs = getContext().getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);
        user_role = prefs.getString(sharedpreferences.role, "guest");

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getStripeOnetime();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.body().getPlans().length == 0) {
                    premium.setVisibility(View.GONE);
                    cancel_premium.setVisibility(View.GONE);
                } else {
                    if (user_role != null) {
                        if (user_role.equalsIgnoreCase("admin") || user_role.equalsIgnoreCase("subscriber")) {
                            premium.setVisibility(View.GONE);
                            if (user_role.equalsIgnoreCase("subscriber")) {
                                cancel_premium.setVisibility(View.VISIBLE);
                            }
                        } else {
                            premium.setVisibility(View.VISIBLE);
                            cancel_premium.setVisibility(View.GONE);
                        }
                    } else {
                        premium.setVisibility(View.GONE);
                        cancel_premium.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });
    }
}