package com.atbuys.runmawi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;


public class ViewPagerAdapter2 extends PagerAdapter implements Adapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<sliders> sliderImg;
    private List<live_banner> live_img;
    private ImageLoader imageLoader;
    TextView viewpagerid;

    private static SharedPreferences prefs;
    String user_id;



    public ViewPagerAdapter2(List sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;


    }



    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        sliders utils = sliderImg.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        viewpagerid =(TextView)view.findViewById(R.id.view1);

       imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();

        imageLoader.get("https://runmawi.com/public/uploads/images/"+utils.getSlider(), ImageLoader.getImageListener(imageView, R.drawable.lands, android.R.drawable.ic_dialog_alert));


/*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent in = new Intent(context, BannerPlayerActivity.class);
                in.putExtra("url",sliderImg.get(position).getMp4_url());
                context.startActivity(in);

            }
        });
*/


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;




    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}

