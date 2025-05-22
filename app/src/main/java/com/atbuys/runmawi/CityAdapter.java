package com.atbuys.runmawi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    private LayoutInflater inflater;
  //  private String[] myImageNameList;

    private List<cities> myImageNameList;
    private static SharedPreferences prefs;
    String user_id;
    Context ctx;


    public CityAdapter(Context ctx, List<cities> myImageNameList){

        inflater = LayoutInflater.from(ctx);
        this.myImageNameList = myImageNameList;

        prefs = ctx.getSharedPreferences(sharedpreferences.My_preference_name, 0);
        user_id=prefs.getString(sharedpreferences.user_id,null);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(myImageNameList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return myImageNameList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(user_id != null) {
                        EditProfileActivity.cityedit.setText(myImageNameList.get(getAdapterPosition()).getName());
                        EditProfileActivity.cityid =myImageNameList.get(getAdapterPosition()).getId();
                        EditProfileActivity.dialog1.dismiss();
                    }
                    else {
                        /*SignupActivity.citytext.setText(myImageNameList.get(getAdapterPosition()).getName());
                        SignupActivity.city_id.setText(myImageNameList.get(getAdapterPosition()).getId());
                        SignupActivity.dialog.dismiss();*/
                    }
                }
            });

        }

    }
}