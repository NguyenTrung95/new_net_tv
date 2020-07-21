package com.eliving.tv.linktaro.launcher.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;

    public MyViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.list = fragments;
    }

    public Fragment getItem(int arg0) {
        return (Fragment) this.list.get(arg0);
    }

    public int getCount() {
        return this.list.size();
    }
}
