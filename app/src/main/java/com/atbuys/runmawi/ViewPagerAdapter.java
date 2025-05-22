package com.atbuys.runmawi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;


public class ViewPagerAdapter extends PagerAdapter implements Adapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<video_banner> sliderImg;

    private ImageLoader imageLoader;
    TextView viewpagerid,tittle,genre;
    CardView playnow;

    private static SharedPreferences prefs;
    String user_id;


    public ViewPagerAdapter(List sliderImg, Context context) {
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
        video_banner utils = sliderImg.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        viewpagerid =(TextView)view.findViewById(R.id.view1);
        playnow=(CardView)view.findViewById(R.id.play_now);
        tittle=(TextView)view.findViewById(R.id.tittle);
        genre=(TextView)view.findViewById(R.id.genre);

       imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getPlayer_image(), ImageLoader.getImageListener(imageView, android.R.drawable.ic_dialog_alert, android.R.drawable.ic_dialog_alert));

        tittle.setText(utils.getTitle());

        playnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, HomePageVideoActivity.class);
                in.putExtra("id",utils.getId());
                in.putExtra("url","");
                in.putExtra("suburl","");
                in.putExtra("data","");
                in.putExtra("xtra","");
                context.startActivity(in);
            }
        });


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

