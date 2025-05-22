package com.atbuys.runmawi;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;

public class ContactusFragment extends Fragment {

    private Context context;
    LinearLayout customer_service, whatsapp, customer_options, whatsapp_options;

    CardView website, facebook, twitter, instagram;
    TextView customer_num3,customer_num2,customer_num1,whatsapp_num1,whatsapp_num2,whatsapp_num3;
    String num1,num2,num3;

    private ArrayList<socail_networl_setting> social_list;

    public ContactusFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_contactus, container, false);

        customer_service = (LinearLayout) root.findViewById(R.id.customer_service);
        customer_options = (LinearLayout) root.findViewById(R.id.customer_options);
        whatsapp = (LinearLayout) root.findViewById(R.id.whatsapp);
        whatsapp_options = (LinearLayout) root.findViewById(R.id.whatsapp_options);
        website = (CardView) root.findViewById(R.id.website);
        facebook = (CardView) root.findViewById(R.id.facebook);
        twitter = (CardView) root.findViewById(R.id.twitter);
        instagram = (CardView) root.findViewById(R.id.instagram);

        customer_num3 = (TextView) root.findViewById(R.id.customer_num3);
        customer_num2 = (TextView) root.findViewById(R.id.customer_num2);
        customer_num1 = (TextView) root.findViewById(R.id.customer_num1);
        whatsapp_num1 = (TextView) root.findViewById(R.id.whatsapp_num1);
        whatsapp_num2 = (TextView) root.findViewById(R.id.whatsapp_num2);
        whatsapp_num3 = (TextView) root.findViewById(R.id.whatsapp_num3);

        num1="+91 878 752 3506";
        num2="+91 883 709 0070";
        num3="+91 60093 49414";

        customer_num1.setText(num1);
        customer_num2.setText(num2);
        customer_num3.setText(num3);
        whatsapp_num1.setText(num1);
        whatsapp_num2.setText(num2);
        whatsapp_num3.setText(num3);

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getSocialNetworkSetting();
        call.enqueue(new retrofit2.Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                social_list = new ArrayList<>(Arrays.asList(jsonResponse.getSocail_networl_setting()));
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error1", t.getMessage());
            }
        });

        website.setOnClickListener(v -> {
            String url = social_list.get(0).getGoogle_page_id();
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(context, "unable to open website", Toast.LENGTH_SHORT).show();
            }
        });

        customer_service.setOnClickListener(v -> {
            if (customer_options.getVisibility()==View.GONE){
                customer_options.setVisibility(View.VISIBLE);
            }else{
                customer_options.setVisibility(View.GONE);
            }
           /* try {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+social_list.get(0).getEmail_page_id()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "unable to contact customer service", Toast.LENGTH_SHORT).show();
            }*/
        });
        customer_num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+num1));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(context, "unable to contact customer service", Toast.LENGTH_SHORT).show();
                }
            }
        });
        customer_num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+num2));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(context, "unable to contact customer service", Toast.LENGTH_SHORT).show();
                }
            }
        });
        customer_num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+num3));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(context, "unable to contact customer service", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whatsapp.setOnClickListener(v -> {
            if (whatsapp_options.getVisibility()==View.GONE){
                whatsapp_options.setVisibility(View.VISIBLE);
            }else{
                whatsapp_options.setVisibility(View.GONE);
            }
            /*String url = "https://api.whatsapp.com/send?phone="+social_list.get(0).getWhatsapp_page_id();
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(context, "unable to open whatsapp", Toast.LENGTH_SHORT).show();
            }*/
        });
        whatsapp_num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+num1;
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(context, "unable to open whatsapp", Toast.LENGTH_SHORT).show();
            }
            }
        });
        whatsapp_num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+num2;
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(context, "unable to open whatsapp", Toast.LENGTH_SHORT).show();
                }
            }
        });
        whatsapp_num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+num3;
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(context, "unable to open whatsapp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        facebook.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+social_list.get(0).getFacebook_page_id()));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id="+social_list.get(0).getFacebook_page_id()));
                startActivity(intent);
                //Toast.makeText(context, "unable to open facebook", Toast.LENGTH_SHORT).show();
            }
        });

        twitter.setOnClickListener(v -> {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + social_list.get(0).getTwitter_page_id())));

            } catch (Exception e) {
                Toast.makeText(context, "unable to open twitter", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "kannan")));
            }
        });

        instagram.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://instagram.com/_u/" + social_list.get(0).getInstagram_page_id()));
                intent.setPackage("com.instagram.android");
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "unable to open instagram", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}