package com.atbuys.runmawi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Active_Payment_settingsAdopter extends RecyclerView.Adapter<Active_Payment_settingsAdopter.ViewHolder> {
    private ArrayList<active_payment_settings> plans;

    private int lastPosition = -1;
    int row_index = 0;
    private Context context;

    public Active_Payment_settingsAdopter(ArrayList<active_payment_settings> plans) {
        this.plans = plans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {




        if(plans.get(i).getStatus().equalsIgnoreCase("1"))
        {
            viewHolder.tv_name.setText(plans.get(i).getPayment_type());

        }

        viewHolder.tv_version.setChecked(lastPosition == i);

        /*viewHolder.tv_version.setText(plans.get(i).getPrice());
        viewHolder.tv_api_level.setText("\u20b9"+plans.get(i).getPrice());
        viewHolder.tv_title.setText(plans.get(i).getType());
        viewHolder.tv_interval.setText(plans.get(i).getPlans_name());
        viewHolder.billing_interval.setText("For a "+plans.get(i).getBilling_interval());*/




       /* viewHolder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignupSubscribeActivity.payment_type.setText(plans.get(i).getPayment_type());

                row_index= i;
                notifyDataSetChanged();
            }
        });


        if (row_index== i)
        {
            viewHolder.lin.setBackgroundColor(Color.parseColor("#45c5e1"));
          // viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            viewHolder.lin.setBackgroundColor(Color.parseColor("#000000"));

            //viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
*/

    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_api_level,tv_title,tv_interval,billing_interval;
        CardView cardView1;
        LinearLayout lin;
        RadioButton tv_version ;
        RadioButton radio;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.text);
            tv_version = (RadioButton)view.findViewById(R.id.radio);

            cardView1=(CardView) view.findViewById(R.id.card_view);
            lin=(LinearLayout) view.findViewById(R.id.linear);

            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastPosition = getAdapterPosition();
                    notifyDataSetChanged();




                    //  Toast.makeText(Payment_settingsAdopter.this.context, ""+plans.get(lastPosition).getPayment_type(), Toast.LENGTH_SHORT).show();


                }
            });


            tv_version.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPosition = getAdapterPosition();
                    notifyDataSetChanged();



                }
            });




          /*  tv_api_level = (TextView)view.findViewById(R.id.amountt);
            tv_title=(TextView)view.findViewById(R.id.aa);
            tv_interval=(TextView)view.findViewById(R.id.interval);
            cardView1=(CardView) view.findViewById(R.id.card_view);
            lin=(LinearLayout) view.findViewById(R.id.linear);
            billing_interval =(TextView)view.findViewById(R.id.billing_intervel);
            radio = (RadioButton) view.findViewById(R.id.radio);*/



        }
    }

}
