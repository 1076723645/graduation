package com.example.finaldesign.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finaldesign.R;
import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.model.bean.WeatherInfo;
import com.example.finaldesign.util.SharePreferencesUtils;
import com.example.finaldesign.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class CityDeleteAdapter extends RecyclerView.Adapter<CityDeleteAdapter.ViewHolder>{

    private List<WeatherInfo> mWeatherList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        for (int i=0; i<list.size(); i++) {
            String responseText = SharePreferencesUtils.getString(mContext, list.get(i), "");
            WeatherInfo weather = WeatherInfo.objectFromData(responseText);
            mWeatherList.add(weather);
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_delete_city, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CityDeleteAdapter.ViewHolder holder, int position) {
        WeatherInfo weather = mWeatherList.get(position);
        holder.address.setText(weather.getHeWeather5().get(0).getBasic().getCity());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public CityDeleteAdapter(List<String> List){
        list = List;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView delete;
        TextView address;

       public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            delete = view.findViewById(R.id.iv_delete);
            address = view.findViewById(R.id.tv_name);
        }
    }
}
