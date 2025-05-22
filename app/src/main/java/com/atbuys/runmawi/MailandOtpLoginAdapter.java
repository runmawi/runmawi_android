package com.atbuys.runmawi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MailandOtpLoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int tabCount;


    public MailandOtpLoginAdapter(Context context, FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.context = context;
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OTPLoginFragment otpLoginFragment = new OTPLoginFragment(context);
                return otpLoginFragment;
            case 1:
                MailLoginFragment mailLoginFragment = new MailLoginFragment(context);
                return mailLoginFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
