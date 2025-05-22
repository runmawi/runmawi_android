package com.atbuys.runmawi;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class FAQFragment extends Fragment {

    private Context context;
    CardView que_flick,que_wish,que_pre,que_down,que_unsub;
    LinearLayout ans_flick,ans_wish,ans_pre,ans_down,ans_unsub;
    ImageView down_arrow,up_arrow,sec_down_arrow,sec_up_arrow,thi_down_arrow,thi_up_arrow,four_down_arrow,four_up_arrow,five_down_arrow,five_up_arrow;


    public FAQFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_f_a_q, container, false);
        que_flick=(CardView)root.findViewById(R.id.que_flick);
        que_wish=(CardView)root.findViewById(R.id.que_wish);
        que_pre=(CardView)root.findViewById(R.id.que_pre);
        que_down=(CardView)root.findViewById(R.id.que_down);
        que_unsub=(CardView)root.findViewById(R.id.que_unsub);
        ans_flick=(LinearLayout) root.findViewById(R.id.ans_flick);
        ans_wish=(LinearLayout)root.findViewById(R.id.ans_wish);
        ans_pre=(LinearLayout)root.findViewById(R.id.ans_pre);
        ans_down=(LinearLayout)root.findViewById(R.id.ans_down);
        ans_unsub=(LinearLayout)root.findViewById(R.id.ans_unsub);

        down_arrow=(ImageView)root.findViewById(R.id.down_arrow);
        up_arrow=(ImageView)root.findViewById(R.id.up_arrow);
        sec_down_arrow=(ImageView)root.findViewById(R.id.sec_down_arrow);
        sec_up_arrow=(ImageView)root.findViewById(R.id.sec_up_arrow);
        thi_down_arrow=(ImageView)root.findViewById(R.id.thi_down_arrow);
        thi_up_arrow=(ImageView)root.findViewById(R.id.thi_up_arrow);
        four_down_arrow=(ImageView)root.findViewById(R.id.four_down_arrow);
        four_up_arrow=(ImageView)root.findViewById(R.id.four_up_arrow);
        five_down_arrow=(ImageView)root.findViewById(R.id.five_down_arrow);
        five_up_arrow=(ImageView)root.findViewById(R.id.five_up_arrow);

        que_flick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans_flick.getVisibility()==View.GONE){
                    ans_flick.setVisibility(View.VISIBLE);
                    up_arrow.setVisibility(View.VISIBLE);
                    down_arrow.setVisibility(View.GONE);
                }else {
                    ans_flick.setVisibility(View.GONE);
                    up_arrow.setVisibility(View.GONE);
                    down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });

        que_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans_wish.getVisibility()==View.GONE){
                    ans_wish.setVisibility(View.VISIBLE);
                    sec_up_arrow.setVisibility(View.VISIBLE);
                    sec_down_arrow.setVisibility(View.GONE);
                }else {
                    ans_wish.setVisibility(View.GONE);
                    sec_up_arrow.setVisibility(View.GONE);
                    sec_down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });

        que_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans_pre.getVisibility()==View.GONE){
                    ans_pre.setVisibility(View.VISIBLE);
                    thi_up_arrow.setVisibility(View.VISIBLE);
                    thi_down_arrow.setVisibility(View.GONE);
                }else {
                    ans_pre.setVisibility(View.GONE);
                    thi_up_arrow.setVisibility(View.GONE);
                    thi_down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });

        que_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans_down.getVisibility()==View.GONE){
                    ans_down.setVisibility(View.VISIBLE);
                    four_up_arrow.setVisibility(View.VISIBLE);
                    four_down_arrow.setVisibility(View.GONE);
                }else {
                    ans_down.setVisibility(View.GONE);
                    four_up_arrow.setVisibility(View.GONE);
                    four_down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });
        que_unsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ans_unsub.getVisibility()==View.GONE){
                    ans_unsub.setVisibility(View.VISIBLE);
                    five_up_arrow.setVisibility(View.VISIBLE);
                    five_down_arrow.setVisibility(View.GONE);
                }else {
                    ans_unsub.setVisibility(View.GONE);
                    five_up_arrow.setVisibility(View.GONE);
                    five_down_arrow.setVisibility(View.VISIBLE);
                }
            }
        });


        return root;
    }
}