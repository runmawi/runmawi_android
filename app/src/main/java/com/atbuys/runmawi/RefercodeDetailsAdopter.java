package com.atbuys.runmawi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RefercodeDetailsAdopter extends RecyclerView.Adapter<RefercodeDetailsAdopter.ViewHolder> {
    private ArrayList<myrefferals> plans;
    int row_index = -1;
    Context context;

    public RefercodeDetailsAdopter(ArrayList<myrefferals> plans) {
        this.plans = plans;
    }

    public RefercodeDetailsAdopter(ArrayList<myrefferals> channelvideosdata, ReferralDetailsActivity referralDetailsActivity) {
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.refer_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {



        viewHolder.tv_version.setText(plans.get(i).getId());
        viewHolder.tv_api_level.setText(plans.get(i).getName());
        viewHolder.tv_title.setText(plans.get(i).getUsername());
        viewHolder.tv_interval.setText(plans.get(i).getEmail());
        viewHolder.billing_interval.setText("For a "+plans.get(i).getUsername());


        
        viewHolder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=i;
                notifyDataSetChanged();

            }
        });

       /* if (row_index==i) {
            viewHolder.lin.setBackgroundResource(R.drawable.plansborder);
        } else {

          //  viewHolder.lin.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.edittextstyle) );
            viewHolder.lin.setBackgroundResource(R.drawable.plansborder1);
        }*/

    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level,tv_title,tv_interval,billing_interval;
        CardView cardView1;
        LinearLayout lin;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            tv_api_level = (TextView)view.findViewById(R.id.planamount);
            tv_title=(TextView)view.findViewById(R.id.aa);
            tv_interval=(TextView)view.findViewById(R.id.interval);
            cardView1=(CardView) view.findViewById(R.id.card_view);
            lin=(LinearLayout) view.findViewById(R.id.lin);
            billing_interval =(TextView)view.findViewById(R.id.billing_intervel);




        }
    }

}
