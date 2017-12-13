package com.example.finaldesign.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finaldesign.R;
import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.util.Utility;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class CityWarnAdapter extends RecyclerView.Adapter<CityWarnAdapter.ViewHolder>{

    private List<String> cityList = new ArrayList<>();
    private List<Weather> mWeatherList = new ArrayList<>();
    private Activity activity;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        for (int i=0; i<cityList.size(); i++) {
            String response = prefs.getString(cityList.get(i),null);
            Weather weather = Utility.handleWeatherResponse(response);
            mWeatherList.add(weather);
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_warnning, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CityWarnAdapter.ViewHolder holder, int position) {
        Weather weather = mWeatherList.get(position);
        holder.address.setText(weather.basic.cityName);
    }

    public CityWarnAdapter(List<String> List, Activity activity){
        this.activity = activity;
        cityList = List;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView address;
        SwitchButton switchButton;

        public ViewHolder(View view) {
            super(view);
            address = (TextView) view.findViewById(R.id.tv_name);
            switchButton = (SwitchButton) view.findViewById(R.id.sb_warning);

        }
    }
}
