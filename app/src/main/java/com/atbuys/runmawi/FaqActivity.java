package com.atbuys.runmawi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FaqActivity extends AppCompatActivity {


    TextView toolbar_title,body;

    ProgressDialog progressDialog;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar_title = findViewById(R.id.toolbar_title);
        body = findViewById(R.id.body);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent in = getIntent();

        String html = in.getStringExtra("body");

        // Remove <p> tags
        String withoutPTags = html.replaceAll("<[/]?p>", "");

// Remove <br> tags
         String withoutBrTags = withoutPTags.replaceAll("<br[/]?>", "");
// Remove <strong> tags
        String withoutStrongTags = withoutBrTags.replaceAll("<[/]?strong>", "");
// Remove &nbsp;
        String withoutNbsp = withoutStrongTags.replaceAll("&nbsp;", " ");
// Remove <ul> tags
        String withoutUlTags = withoutNbsp.replaceAll("<[/]?ul>", "");
// Remove <li> tags
        String withoutLiTags = withoutUlTags.replaceAll("<[/]?li>", "");
        String removeAllTag = withoutLiTags.replaceAll("\\<.*?\\>", "");

        toolbar_title.setText(in.getStringExtra("title"));
        body.setText(removeAllTag);


    }
    }


