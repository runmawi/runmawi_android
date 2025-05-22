package com.atbuys.runmawi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditProfileActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final String URL_TO_SHARE = "welcome";
    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText name, username, mobile, id, email, mobile_num;
    public static Dialog dialog1;
    public static TextView countryedit,country_text, stateedit, cityedit;
    RequestBody requestFile;
    MultipartBody.Part body2;
    RelativeLayout doblayout;
    EditText dob;
    DatePickerDialog picker;
    CountryCodePicker country_picker;


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAGkp4oeo:APA91bFEmwMA2Wr_wUapjEbFz9DyrwAU5RTfo5oXrCcFZQY1o34EkLzpOpuhJkBWbd1vYZ4b6B7C1fQr-EBxwweKB4Zm5jXG1JCCAsDmbJDMuOQCW46wyRXWO0ftSJ5hy8IHDakRVuU3";

    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    String[] country = {"Normal", "Kids"};

    CircleImageView image;
    ImageView img, edit_image, backarrow;
    TextView ccode;
    CardView update, changepassword;
    int PICK_IMAGE_REQUEST = 111;
    int click_count;
    String xx;
    String URL = "https://runmawi.com/api/auth/updateProfile";
    ProgressDialog progressDialog;
    List<Countrycode> movieList;

    int year;
    EditText date1, date;
    int month;
    int dayOfMonth;
    Calendar calendar;


    private String username1, mobile1, id1, password1, name1, image11, ccode1, child_type, DateOB, gen;
    private Bitmap bitmap, xxxx;
    private String filePath;
    private Object Context;
    String tkn;
    Spinner spinner;
    ImageView spinner_arraw,down_arrow,up_arrow;

    private List<country> myImageNameList;
    private List<states> statelistdata;
    private List<cities> citieslistdata;

    TextView pass;
    TextView gender, male, female, other;
    LinearLayout gender_layout, gender_show;
    String a1;
    private ArrayList<user_details> user_detailsdata;
    public static String emailid, countryid, stateid, cityid;

    RelativeLayout progresslayout, country_layout;
    LinearLayout editaccount;
    Bitmap bitmap1;
    public static Dialog dialog;
    String[] gendersel = {"Male", "Female", "Others"};
    String user_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences prefs = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE);

        user_id = prefs.getString(sharedpreferences.user_id, null);
        final String email1 = prefs.getString(sharedpreferences.email, null);
        String usernamee = prefs.getString(sharedpreferences.username, null);

       // tkn = FirebaseInstanceId.getInstance().getToken();

        editaccount = (LinearLayout) findViewById(R.id.editaccount);
        progresslayout = (RelativeLayout) findViewById(R.id.progresslayout);
        doblayout = (RelativeLayout) findViewById(R.id.doblayout);
        dob = (EditText) findViewById(R.id.dob);
        backarrow = (ImageView) findViewById(R.id.backarrow);
        country_picker = (CountryCodePicker) findViewById(R.id.county_picker);
        mobile_num = (EditText) findViewById(R.id.mobile_num);
        down_arrow=(ImageView)findViewById(R.id.down_arrow);
        up_arrow=(ImageView)findViewById(R.id.up_arrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                picker = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                dob.setText(dayOfMonth + "-" + (month + 1) + "-" + year);


                            }
                        }, year, month, dayOfMonth);


                picker.show();
            }
        });

        spinner_arraw = (ImageView) findViewById(R.id.spinner_arrow);
        spinner = (Spinner) findViewById(R.id.spinner);

        progressDialog = new ProgressDialog(this);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.namee);
        gender = (TextView) findViewById(R.id.gender);
        gender = (TextView) findViewById(R.id.gender);
        gender_layout = (LinearLayout) findViewById(R.id.gender_layout);
        gender_show = (LinearLayout) findViewById(R.id.gender_show);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        other = (TextView) findViewById(R.id.other);


        // username.setInputType(InputType.TYPE_NULL);
        //email.setInputType(InputType.TYPE_NULL);
        dob.setInputType(InputType.TYPE_NULL);


        countryedit = (TextView) findViewById(R.id.countrycodeedit);
        country_text = (TextView) findViewById(R.id.country);
        mobile = (EditText) findViewById(R.id.mobile);
        country_layout = (RelativeLayout) findViewById(R.id.country_layout);


        image = (CircleImageView) findViewById(R.id.profile_image1);
        update = (CardView) findViewById(R.id.update);
        pass = (TextView) findViewById(R.id.pass);
        edit_image = (ImageView) findViewById(R.id.edit_image);


        gender_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((gender_show.getVisibility() == View.VISIBLE)) {
                    gender_show.setVisibility(View.GONE);
                    up_arrow.setVisibility(View.GONE);
                    down_arrow.setVisibility(View.VISIBLE);
                } else {
                    gender_show.setVisibility(View.VISIBLE);
                    up_arrow.setVisibility(View.VISIBLE);
                    down_arrow.setVisibility(View.GONE);
                }
            }
        });
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender.setText((male.getText().toString()));
                gender_show.setVisibility(View.GONE);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender.setText((female.getText().toString()));
                gender_show.setVisibility(View.GONE);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender.setText((other.getText().toString()));
                gender_show.setVisibility(View.GONE);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                child_type = String.valueOf(parent.getItemAtPosition(position));
                spinner.setVisibility(View.GONE);
                gender.setText(child_type);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, gendersel);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aa);



    /*    spinner_arraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

*/

        country_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(EditProfileActivity.this);

            }
        });
        country_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(EditProfileActivity.this);
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

                        editaccount.setVisibility(View.VISIBLE);
                        progresslayout.setVisibility(View.GONE);

                        for (int k = 0; k < user_detailsdata.size(); k++) {


                            if (user_detailsdata.get(k).getDOB() == null) {
                                dob.setText("");

                            }
                            if (user_detailsdata.get(k).getGender() == null) {
                                gender.setText("");
                            }

                            String namee = user_detailsdata.get(k).getUsername();
                            //String[] splited = namee.split("\\s+");

                            username.setText(namee);
                            email.setText(user_detailsdata.get(k).getEmail());
                            if (user_detailsdata.get(k).getCcode() == null || user_detailsdata.get(k).getCcode().isEmpty() || user_detailsdata.get(k).getCcode().equalsIgnoreCase("0")) {
                                country_picker.setCountryForPhoneCode(91);
                            } else {
                                country_picker.setCountryForPhoneCode(Integer.parseInt(user_detailsdata.get(k).getCcode()));
                            }
                            mobile_num.setText(user_detailsdata.get(k).getMobile());
                            name.setText(user_detailsdata.get(k).getName());
                            dob.setText(user_detailsdata.get(k).getDOB());
                            gender.setText(user_detailsdata.get(k).getGender());

                            String userprofile = user_detailsdata.get(k).getProfile_url();
                            Picasso.get().load(userprofile).placeholder(R.drawable.ic_baseline_account_circle_24).into(image);

                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });


       /* image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    click_count = 1;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                } else {
                    requestPermission();
                }
            }
        });*/

        image.setOnClickListener(view -> {

            if (checkPermission()) {
                openImagePicker();
            }
            else {
                if (shouldShowRequestPermissionRationale()) {
                    // User has denied permission earlier but not "Don't ask again"
                    requestPermission();
                } else {
                    // First-time permission request or "Don't ask again" not selected
                    requestPermission();
                }
            }
        });

        edit_image.setOnClickListener(v -> {

            if (checkPermission()) {
                openImagePicker();
            }
            else {
                if (shouldShowRequestPermissionRationale()) {
                    // User has denied permission earlier but not "Don't ask again"
                    requestPermission();
                } else {
                    // First-time permission request or "Don't ask again" not selected
                    requestPermission();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Please Wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setMax(100);
                progressDialog.show();

                emailid = email.getText().toString();
                id1 = id.getText().toString();
                username1 = username.getText().toString();
                ccode1 = country_picker.getSelectedCountryCode();
                mobile1 = mobile_num.getText().toString();
                name1 = name.getText().toString();
                DateOB = dob.getText().toString();
                gen = gender.getText().toString();


                RequestBody username = RequestBody.create(MediaType.parse("text/plain"), username1);
                RequestBody user_idd = RequestBody.create(MediaType.parse("text/plain"), user_id);
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailid);
                RequestBody ccode = RequestBody.create(MediaType.parse("text/plain"), "+"+ccode1);
                RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mobile1);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), name1);
                RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), DateOB);
                RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), gen);

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Apiimage.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                Apiimage api = retrofit.create(Apiimage.class);

                boolean isValid = PhoneNumberValidator.isValidPhoneNumber(country_picker.getSelectedCountryNameCode(), mobile_num.getText().toString());

                if (isValid) {
                    Call<MyResponse> call = api.getUpdate(body2, user_idd, username, email, ccode, mobile, name, dob, gender);
                    call.enqueue(new retrofit2.Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {
                            progressDialog.hide();
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                        }
                    });
                } else {
                    mobile_num.requestFocus();
                    mobile_num.setError("Phone number is invalid");
                    progressDialog.hide();
                }

            }
        });
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void showDialog(EditProfileActivity editProfileActivity) {
        dialog = new Dialog(editProfileActivity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_recycler);

        Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://runmawi.com/assets/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebAPIService service1 = retrofit1.create(WebAPIService.class);
        Call<List<Countrycode>> jsonCall = service1.readJson();

        jsonCall.enqueue(new retrofit2.Callback<List<Countrycode>>() {

            @Override
            public void onResponse(Call<List<Countrycode>> call, retrofit2.Response<List<Countrycode>> response) {

                RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
                movieList = response.body();

                AdapterRe adapterRe = new AdapterRe(EditProfileActivity.this, movieList);
                recyclerView.setAdapter(adapterRe);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                recyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.show();
            }

            @Override
            public void onFailure(Call<List<Countrycode>> call, Throwable t) {
                Log.e("happy", t.toString());
            }


        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            uploadFile(filePath, "Myimage");

            try {

                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();

            }

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

    /*private boolean checkPermission() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }*/

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private boolean shouldShowRequestPermissionRationale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)
                    || shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO);
        } else {
            return shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO
                    },
                    PERMISSION_REQUEST_CODE
            );
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE
            );
        }
    }


   /* private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, PERMISSION_REQUEST_CODE);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (allPermissionsGranted) {
                openImagePicker();
            } else {
                // Optional: Show a message to the user explaining why the permission is necessary
                Toast.makeText(this, "Allow permission from the app settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        click_count = 1;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED || grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
//
//                    click_count = 1;
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_PICK);
//                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
//                } else {
//                    requestPermission();
//                }
//                break;
//        }
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}




