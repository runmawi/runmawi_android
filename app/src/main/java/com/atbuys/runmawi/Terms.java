package com.atbuys.runmawi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class Terms extends AppCompatActivity {

    TextView tittle, desc;
    private ArrayList<pages> pageslist;
    ImageView back_arrow;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(  AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darktheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_terms);

        tittle = (TextView) findViewById(R.id.tittle);
        desc = (TextView) findViewById(R.id.desc);
        back_arrow=(ImageView)findViewById(R.id.backarrow);
        progress_bar=(ProgressBar) findViewById(R.id.progress_bar);
        pageslist = new ArrayList<>();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Call<JSONResponse> callser = ApiClient.getInstance1().getApi().getCmsPage();
        callser.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                pageslist = new ArrayList<>(Arrays.asList(jsonResponse.getPages()));

                for (int i = 0; i < pageslist.size(); i++) {
                    if (pageslist.get(i).getSlug().equalsIgnoreCase("terms-and-conditions")) {
                        progress_bar.setVisibility(View.GONE);
                        tittle.setText(pageslist.get(i).getTitle());
                        String body=pageslist.get(i).getBody();
                        String removeAllTag = body.replaceAll("\\<.*?\\>", "");
                        String withoutNbsp = removeAllTag.replaceAll("&nbsp;", " ");
                        desc.setText(withoutNbsp);
                    }
                }
            }
            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
            }
        });
    }
}