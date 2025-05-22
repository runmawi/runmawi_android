package com.atbuys.runmawi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class sidemennuAdopter extends RecyclerView.Adapter<sidemennuAdopter.MyViewHolder> {



    private List<Menus> sidemenulist;
    private Context context;

    private static int currentPosition = 0;

    public sidemennuAdopter(List<Menus> sidemenulist, Context context) {
        this.sidemenulist = sidemenulist;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name1, name2;
        public ImageView image;
        public RecyclerView menuitemrecycler;
        public LinearLayout full;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.sidemenuimg);
            name1 = (TextView) view.findViewById(R.id.name1);
            name2 = (TextView) view.findViewById(R.id.name2);
            menuitemrecycler = (RecyclerView) view.findViewById(R.id.menuitemrecycler);
            full = (LinearLayout) view.findViewById(R.id.full);
        }
    }

    public sidemennuAdopter(List<Menus> sidemenulist) {
        this.sidemenulist = sidemenulist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.side_mennu1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Menus sidemenu = sidemenulist.get(position);
        if (sidemenu.getIn_home().equalsIgnoreCase("0") ||sidemenu.getIn_home().isEmpty()||sidemenu.getIn_home()==null) {
            holder.full.setVisibility(View.GONE);
        } else {
            holder.full.setVisibility(View.VISIBLE);
            holder.name1.setText(sidemenu.getName());
            holder.name2.setText(sidemenu.getIn_menu());
            Picasso.get().load(sidemenu.getImage()).into(holder.image);

        }

    }


    @Override
    public int getItemCount() {
        return sidemenulist.size();
    }



}