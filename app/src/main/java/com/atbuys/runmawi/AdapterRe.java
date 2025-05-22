package com.atbuys.runmawi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRe extends RecyclerView.Adapter<AdapterRe.MyViewHolder> {

    private LayoutInflater inflater;
  //  private String[] myImageNameList;
    private List<Countrycode> myImageNameList;

    private static SharedPreferences prefs;
    String user_id;
    Context ctx;


    public AdapterRe(Context ctx, List<Countrycode> myImageNameList){

        inflater = LayoutInflater.from(ctx);
        this.myImageNameList = myImageNameList;

        prefs = ctx.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id=prefs.getString(sharedpreferences.user_id,null);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.country_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(myImageNameList.get(position).getName());
        holder.code.setText(myImageNameList.get(position).getDial_code());

    }

    @Override
    public int getItemCount() {
        return myImageNameList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,code;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            code = (TextView) itemView.findViewById(R.id.code);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (user_id != null) {

                        EditProfileActivity.country_text.setText(myImageNameList.get(getAdapterPosition()).getDial_code());
                        EditProfileActivity.dialog.dismiss();

                    }
                    else {
                        SignupActivityNew.countrycode.setText(myImageNameList.get(getAdapterPosition()).getDial_code());
                        SignupActivityNew.dialog.dismiss();
                    }
                }
            });

        }

    }
}