package com.example.finaldesign.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.finaldesign.ui.fragment.WeatherFragment2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hui on 2017/11/1.
 */

public class Frag2Adapter extends FragmentStatePagerAdapter {

    private List<WeatherFragment2> fragments = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;

    public Frag2Adapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    public void setData(ArrayList<WeatherFragment2> lists) {
        this.fragments = lists;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}