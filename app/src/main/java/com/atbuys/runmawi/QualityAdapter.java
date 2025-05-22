package com.atbuys.runmawi;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.QualityViewHolder> {

    ArrayList<quality_free> qualities;
    ItemClickListener itemClickListener;
    int selectedPosition = -1;
    int row_index = -1;

    public QualityAdapter(ArrayList<quality_free> qualities, ItemClickListener itemClickListener) {
        this.qualities = qualities;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public QualityAdapter.QualityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qualitylayout, parent, false);
        return new QualityAdapter.QualityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QualityAdapter.QualityViewHolder holder, int position) {
        holder.quality.setText(qualities.get(position).getQuality());
        holder.quality_price.setText(qualities.get(position).getPrice());

        Call<JSONResponse> call = ApiClient.getInstance1().getApi().getStripeOnetime();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                holder.currency.setText(response.body().getCurrency_Symbol());
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {

            }
        });
        // Checked selected radio button
        holder.radioButton.setChecked(position == selectedPosition);

        // set listener on radio button
        holder.radioButton.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton compoundButton,
                            boolean b)
                    {
                        // check condition
                        if (b) {
                            // When checked
                            // update selected position
                            selectedPosition
                                    = holder.getAdapterPosition();
                            // Call listener
                            itemClickListener.onClick(
                                    holder.radioButton.getText()
                                            .toString());
                        }
                    }
                });

        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index= position;
                notifyDataSetChanged();
            }
        });


        if (row_index== position) {
            holder.lin.setBackgroundColor(Color.parseColor("#ff0000"));
            // viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            holder.lin.setBackgroundColor(Color.parseColor("#000000"));
            //viewHolder.id.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public long getItemId(int position) {
        // pass position
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        // pass position
        return position;
    }
    @Override
    public int getItemCount() {
        return qualities.size();
    }

    public class QualityViewHolder extends RecyclerView.ViewHolder{
        RadioButton radioButton;
        TextView quality,quality_price,currency;
        LinearLayout lin;
        public QualityViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton=itemView.findViewById(R.id.radio_btn);
            quality=itemView.findViewById(R.id.quality);
            quality_price=itemView.findViewById(R.id.quality_price);
            currency=itemView.findViewById(R.id.currency);
            lin=(LinearLayout) itemView.findViewById(R.id.linear);
        }


    }
}
