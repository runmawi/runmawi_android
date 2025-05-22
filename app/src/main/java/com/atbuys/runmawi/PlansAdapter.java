package com.atbuys.runmawi;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder> {
    private ArrayList<plans> plans;

    private int lastPosition = -1,cou=0;
    int row_index = -1;
    String currencySymbol;
    String type;
    Context context;

    public PlansAdapter(ArrayList<plans> plans, String currencySymbol) {
        this.plans = plans;
        this.currencySymbol=currencySymbol;
    }
    public PlansAdapter(ArrayList<plans> plans, String currencySymbol, String type, Context context) {
        this.plans = plans;
        this.currencySymbol=currencySymbol;
        this.type=type;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        if (type.equalsIgnoreCase("on")){
            Call<JSONResponse> callimg = ApiClient.getInstance1().getApi().currencyConverter(currencySymbol,plans.get(i).getPrice());
            callimg.enqueue(new retrofit2.Callback<JSONResponse>() {
                @Override
                public void onResponse(Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {

                    JSONResponse jsonResponse = response.body();
                    if (!(jsonResponse.getCurrency_Converted().isEmpty())|| jsonResponse.getCurrency_Converted()!=null){
                        viewHolder.tv_api_level.setText(jsonResponse.getCurrency_Converted());
                    }
                }

                @Override
                public void onFailure(Call<JSONResponse> call, Throwable t) {
                    Log.d("Error: ", t.getMessage());
                }
            });
        }else {
            viewHolder.tv_api_level.setText(currencySymbol+plans.get(i).getPrice());
        }

        viewHolder.tv_name.setText(plans.get(i).getPlans_name());
        viewHolder.tv_version.setText(plans.get(i).getPrice());

        viewHolder.tv_title.setText(plans.get(i).getType());
        viewHolder.tv_interval.setText(plans.get(i).getPlans_name());
        viewHolder.billing_interval.setText("For a "+plans.get(i).getBilling_interval());

        viewHolder.quality.setText("Video Quality : "+plans.get(i).getVideo_quality());
        viewHolder.resolution.setText("Video Resolution : "+plans.get(i).getResolution());
        viewHolder.device.setText("Support Devices : "+plans.get(i).getDevices());



        viewHolder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index= i;
                notifyDataSetChanged();
            }
        });


        if (row_index== i)
        {
            viewHolder.lin.setBackgroundColor(Color.parseColor("#ff0000"));
            // viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            viewHolder.lin.setBackgroundColor(Color.parseColor("#000000"));
            //viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }



    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level,tv_title,tv_interval,billing_interval;
        CardView cardView1;
        LinearLayout lin;
        RadioButton radio;
        private TextView quality, resolution, device;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.pricee);
            tv_version = (TextView)view.findViewById(R.id.tv_name);
            tv_api_level = (TextView)view.findViewById(R.id.amountt);
            tv_title=(TextView)view.findViewById(R.id.aa);
            tv_interval=(TextView)view.findViewById(R.id.interval);
            cardView1=(CardView) view.findViewById(R.id.card_view);
            lin=(LinearLayout) view.findViewById(R.id.linear);
            billing_interval =(TextView)view.findViewById(R.id.billing_intervel);
            radio = (RadioButton) view.findViewById(R.id.radio);
            quality = (TextView)view.findViewById(R.id.quality);
            resolution = (TextView)view.findViewById(R.id.resoultion);
            device=(TextView)view.findViewById(R.id.devices);





        }
    }

}
