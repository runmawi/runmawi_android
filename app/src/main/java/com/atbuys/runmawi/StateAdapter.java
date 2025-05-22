package com.atbuys.runmawi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    private LayoutInflater inflater;
  //  private String[] myImageNameList;

    private List<videossubtitles> myImageNameList;

    private static SharedPreferences prefs;
    String user_id;
    Context ctx;


    public StateAdapter(Context ctx, List<videossubtitles> myImageNameList){

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

        holder.name.setText(myImageNameList.get(position).getSub_language());
        holder.radioButton.setText(myImageNameList.get(position).getShortcode());

    }

    @Override
    public int getItemCount() {
        return myImageNameList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        RadioButton radioButton;


        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            radioButton =(RadioButton) itemView.findViewById(R.id.sub);


        }

    }
}