package com.atbuys.runmawi;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Callback;


class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> implements SpinnerAdapter {



    private ArrayList<country> latestvideoslist;
    private Context context;

    public CountryAdapter(ArrayList<country> latestvideoslist, Context context) {
        this.latestvideoslist = latestvideoslist;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, url,id,ppvstatus;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.text);
           /* image = (ImageView) view.findViewById(R.id.newuploadimg);
             name = (TextView) view.findViewById(R.id.newuploadname);
            url = (TextView) view.findViewById(R.id.newuploadurl);
            ppvstatus=(TextView)view.findViewById(R.id.ppvstatus);
*/
        }
    }

    public CountryAdapter(ArrayList<country> latestvideoslist, Callback<JSONResponse> callback) {
        this.latestvideoslist = latestvideoslist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fruit_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.id.setText(latestvideoslist.get(position).getName());
    /*    holder.url.setText(latestvideoslist.get(position).getMp4_url());
        holder.name.setText(latestvideoslist.get(position).getTitle());
        holder.ppvstatus.setText(latestvideoslist.get(position).getPpv_status());*/




    }


    @Override
    public int getItemCount() {
        return latestvideoslist.size();
    }

}

