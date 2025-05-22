package com.atbuys.runmawi;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

;

public class LiveTabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    String videoId,user_id;

    public LiveTabAdapter(Context context, FragmentManager fm, int totalTabs, String videoId, String user_id) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.videoId=videoId;
        this.user_id=user_id;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                LiveTrailorfragment trailorFragment = new LiveTrailorfragment(videoId,user_id);
                return trailorFragment;
            case 1:
                CommentFragment commentFragment = new CommentFragment(videoId,user_id,"live");
                return commentFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}