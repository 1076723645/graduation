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
import com.example.finaldesign.model.bean.WeatherInfo;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.SharePreferencesUtils;
import com.example.finaldesign.util.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hui on 2017/11/1.
 */

public class CityManagerAdapter extends RecyclerView.Adapter<CityManagerAdapter.ViewHolder> {

    private List<WeatherInfo> mWeatherList = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private Activity activity;
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycle_item, parent, false);
        final ViewHolder holder =  new ViewHolder(v);
        holder.cardView.setOnClickListener(v1 -> {
            int position = holder.getAdapterPosition();
            Intent intent = new Intent();
            intent.putExtra("data_return",position);
            activity.setResult(Activity.RESULT_OK,intent);
            activity.finish();
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherInfo weather = mWeatherList.get(position);
        WeatherInfo.HeWeather5Bean weatherBean = weather.getHeWeather5().get(0);
        String windLv = weatherBean.getNow().getWind().getSc();
        if (windLv.equals("微风")){
            windLv = "1";
        }
        holder.temp.setText(weatherBean.getNow().getTmp() + "°");
        holder.max.setText(weatherBean.getDaily_forecast().get(0).getTmp().getMax() + "°");
        holder.min.setText(weatherBean.getDaily_forecast().get(0).getTmp().getMin() + "°");
        holder.wind.setText(weatherBean.getNow().getWind().getDir() + windLv + "级");
        holder.hum.setText("湿度" + weatherBean.getNow().getHum() + "%");
        if (weatherBean.getAqi() != null)
            holder.airQul.setText("空气" + weatherBean.getAqi().getCity().getQlty());
        holder.address.setText(weatherBean.getBasic().getCity());
        holder.cond.setImageResource(DataUtil.getWeatherColourPng(weatherBean.getNow().getCond().getTxt()));
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

        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            cond = view.findViewById(R.id.iv_cond);
            address = view.findViewById(R.id.tv_address);
            airQul = view.findViewById(R.id.tv_air_qul);
            hum = view.findViewById(R.id.tv_hum);
            wind = view.findViewById(R.id.tv_wind);
            max = view.findViewById(R.id.tv_max);
            min = view.findViewById(R.id.tv_min);
            temp = view.findViewById(R.id.tv_temp);
        }
    }
}
