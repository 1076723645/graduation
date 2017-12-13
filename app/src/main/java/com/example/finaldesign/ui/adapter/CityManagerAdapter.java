package com.example.finaldesign.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/11/1.
 */

public class CityManagerAdapter extends RecyclerView.Adapter<CityManagerAdapter.ViewHolder> {

    private List<Weather> mWeatherList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private Activity activity;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        for (int i=0; i<list.size(); i++) {
            String response = prefs.getString(list.get(i),null);
            Weather weather = Utility.handleWeatherResponse(response);
            mWeatherList.add(weather);
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycle_item, parent, false);
        final ViewHolder holder =  new ViewHolder(v);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent();
                intent.putExtra("data_return",position);
                activity.setResult(Activity.RESULT_OK,intent);
                activity.finish();
            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CityManagerAdapter.ViewHolder holder, int position) {
        Weather weather = mWeatherList.get(position);
        String windLv = weather.now.windMore.how;
        if (windLv.equals("微风")){
            windLv = "1";
        }
        holder.temp.setText(weather.now.temperature+"°");
        holder.max.setText(weather.forecastList.get(0).temperature.max+"°");
        holder.min.setText(weather.forecastList.get(0).temperature.min+"°");
        holder.wind.setText(weather.now.windMore.where+windLv+"级");
        holder.hum.setText("湿度"+weather.now.humidity+"%");
        holder.airQul.setText("空气"+weather.aqi.city.qlty);
        holder.address.setText(weather.basic.cityName);
        holder.cond.setImageResource(DataUtil.getWeatherColourPng(weather.now.more.info));
    }

    public CityManagerAdapter(List<String> List, Activity activity){
        this.activity = activity;
        list = List;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<String> List){
        list = List;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cond;
        TextView address;
        TextView airQul;
        TextView hum;
        TextView wind;
        TextView max;
        TextView min;
        TextView temp;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            cond = (ImageView)view.findViewById(R.id.iv_cond);
            address = (TextView) view.findViewById(R.id.tv_address);
            airQul = (TextView) view.findViewById(R.id.tv_air_qul);
            hum = (TextView) view.findViewById(R.id.tv_hum);
            wind = (TextView) view.findViewById(R.id.tv_wind);
            max = (TextView) view.findViewById(R.id.tv_max);
            min = (TextView) view.findViewById(R.id.tv_min);
            temp = (TextView) view.findViewById(R.id.tv_temp);
        }
    }
}
