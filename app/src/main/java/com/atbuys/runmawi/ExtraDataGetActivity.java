package com.atbuys.runmawi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExtraDataGetActivity extends AppCompatActivity {

    String id, mobile, tkn, ccode, free_registration;
    TextView gender, male, female;
    EditText email, name;
    LinearLayout gender_layout, gender_show;
    CardView continue_layout;
    CircleImageView profile_image;
    ImageView edit_image, back_arrow;
    int PICK_IMAGE_REQUEST = 111;
    int click_count;
    private static final int PERMISSION_REQUEST_CODE = 1;
    RequestBody requestFile;
    MultipartBody.Part body2;
    private Bitmap bitmap;
    String URL = "https://gorplayer.com/api/auth/updateProfile";
    emailexists emailexistsresponse;
    registerresponse registerresponse1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_data_get);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        mobile = intent.getStringExtra("mobile");
        ccode = intent.getStringExtra("ccode");

        gender = (TextView) findViewById(R.id.gender);
        gender_layout = (LinearLayout) findViewById(R.id.gender_layout);
        gender_show = (LinearLayout) findViewById(R.id.gender_show);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        continue_layout = (CardView) findViewById(R.id.continue_layout);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        edit_image = (ImageView) findViewById(R.id.edit_image);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        progressDialog=new ProgressDialog(ExtraDataGetActivity.this);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gender_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((gender_show.getVisibility() == View.VISIBLE)) {
                    gender_show.setVisibility(View.GONE);
                } else {
                    gender_show.setVisibility(View.VISIBLE);
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
        profile_image.setOnClickListener(new View.OnClickListener() {
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
        });
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
        continue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().length() == 0 || email.getText().toString().trim().isEmpty()) {
                    email.setError("E-Mail not entered");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Enter valid email address");
                } else if (name.getText().toString().trim().isEmpty()) {
                    name.setError("Name not entered");
                    name.requestFocus();
                } else {
                    progressDialog.setMessage("Profile Updating...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setMax(100);
                    progressDialog.show();
                    if (bitmap == null && click_count != 0) {
                        progressDialog.hide();
                        @SuppressLint("UseCompatLoadingForDrawables")
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_account_circle_24);
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                        final Bitmap bitmap1 = bitmapDrawable.getBitmap();

                        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {
                                        try {

                                            JSONObject obj = new JSONObject(new String(response.data));

                                            Intent in = new Intent(getApplicationContext(), MyAccountActivity.class);
                                            startActivity(in);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                }) {


                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                return params;
                            }

                            @Override
                            protected Map<String, DataPart> getByteData() {
                                Map<String, DataPart> params = new HashMap<>();
                                long imagename = System.currentTimeMillis();

                                params.put("avatar", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap1)));
                                return params;
                            }
                        };


                        Volley.newRequestQueue(ExtraDataGetActivity.this).add(volleyMultipartRequest);


                    } else {
                        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString());
                        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), id);
                        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString());
                        RequestBody ccode1 = RequestBody.create(MediaType.parse("text/plain"), ccode);
                        RequestBody mobile1 = RequestBody.create(MediaType.parse("text/plain"), mobile);
                        RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), "");
                        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), "");
                        RequestBody gender1 = RequestBody.create(MediaType.parse("text/plain"), gender.getText().toString());

                        Gson gson = new GsonBuilder()
                                .setLenient()
                                .create();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Apiimage.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

                        Apiimage api = retrofit.create(Apiimage.class);

                        Call<MyResponse> call = api.getUpdate(body2, user_id, username, email1, ccode1, mobile1, name1, dob, gender1);

                        call.enqueue(new retrofit2.Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, retrofit2.Response<MyResponse> response) {

                                SharedPreferences.Editor editor = getSharedPreferences(sharedpreferences.My_preference_name, MODE_PRIVATE).edit();
                                editor.putBoolean(sharedpreferences.login, true);
                                editor.putString(sharedpreferences.user_id, id);
                                editor.putString(sharedpreferences.role, "registered");
                                editor.putString(sharedpreferences.email, email.getText().toString());
                                editor.putString(sharedpreferences.username, name.getText().toString());
                                editor.putString(sharedpreferences.profile, "10");
                                editor.putString(sharedpreferences.fingerprint, "1");
                                editor.putString(sharedpreferences.theme, "0");
                                editor.apply();

                                Intent in = new Intent(getApplicationContext(), HomePageActivitywithFragments.class);
                                startActivity(in);

                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                progressDialog.hide();
                            }

                        });
                    }
                }
            }

        });


    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED || grantResults[2] == PackageManager.PERMISSION_GRANTED)) {

                    click_count = 1;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
                } else {
                    requestPermission();
                }
                break;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            uploadFile(filePath, "Myimage");

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile_image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
}