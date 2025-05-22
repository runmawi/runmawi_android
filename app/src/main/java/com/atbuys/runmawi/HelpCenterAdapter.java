package com.atbuys.runmawi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HelpCenterAdapter extends FragmentPagerAdapter {

    private Context context;
    int tabCount;


    public HelpCenterAdapter(Context context, FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.context = context;
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FAQFragment faqFragment = new FAQFragment(context);
                return faqFragment;
            case 1:
                ContactusFragment contactusFragment = new ContactusFragment(context);
                return contactusFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
