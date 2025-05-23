package com.atbuys.runmawi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.atbuys.runmawi.R;

import java.util.List;

public class RunmawiTvBannerAdapter extends PagerAdapter {
    private final Context context;
    private final List<String> bannerPlaceholders;
    private final LayoutInflater inflater;

    public RunmawiTvBannerAdapter(Context context, List<String> bannerPlaceholders) {
        this.context = context;
        this.bannerPlaceholders = bannerPlaceholders;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bannerPlaceholders != null ? bannerPlaceholders.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.runmawi_tv_banner_item, container, false);
        TextView tvBannerPlaceholder = view.findViewById(R.id.tvBannerPlaceholder);
        tvBannerPlaceholder.setText(bannerPlaceholders.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
