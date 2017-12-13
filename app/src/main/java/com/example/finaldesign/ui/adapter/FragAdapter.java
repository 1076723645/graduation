package com.example.finaldesign.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.finaldesign.ui.fragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class FragAdapter extends FragmentStatePagerAdapter {

    private List<WeatherFragment> fragments = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;

    public FragAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    public void setData(ArrayList<WeatherFragment> lists) {
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