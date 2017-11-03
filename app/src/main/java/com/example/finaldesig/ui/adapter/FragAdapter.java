package com.example.finaldesig.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.finaldesig.ui.fragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/1.
 */

public class FragAdapter extends FragmentStatePagerAdapter {

    private ArrayList<WeatherFragment> fragments;
    private FragmentManager fm;

    public FragAdapter(FragmentManager fm, ArrayList<WeatherFragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    public void setFragments(ArrayList<WeatherFragment> fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
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