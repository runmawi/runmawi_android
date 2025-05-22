package com.atbuys.runmawi;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlanidApopter extends RecyclerView.Adapter<PlanidApopter.ViewHolder> {
    private ArrayList<plan> plans;

    private int lastPosition = -1;
    int row_index = 0;

    public PlanidApopter(ArrayList<plan> plans) {
        this.plans = plans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(plans.get(i).getBilling_interval());
        viewHolder.tv_version.setText(plans.get(i).getPrice());
        viewHolder.tv_api_level.setText("\u20b9"+plans.get(i).getPrice());
        viewHolder.tv_title.setText(plans.get(i).getType());
        viewHolder.tv_interval.setText(plans.get(i).getPlans_name());
        viewHolder.billing_interval.setText("For a "+plans.get(i).getBilling_interval());




        viewHolder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index= i;
                notifyDataSetChanged();
            }
        });


        if (row_index== i)
        {
            viewHolder.lin.setBackgroundColor(R.attr.colorPrimary);
          // viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            viewHolder.lin.setBackgroundColor(Color.parseColor("#45c5e1"));
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




        }
    }

}
