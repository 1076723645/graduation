package com.example.finaldesign.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finaldesign.R;
import com.example.finaldesign.model.bean.WeatherInfo;
import com.example.finaldesign.util.SharePreferencesUtils;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class CityWarnAdapter extends RecyclerView.Adapter<CityWarnAdapter.ViewHolder>{

    private List<String> cityList = new ArrayList<>();
    private List<WeatherInfo> mWeatherList = new ArrayList<>();
    private Context mContext;
    private SharedPreferences prefs = null;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        for (int i=0; i<cityList.size(); i++) {
            String response = SharePreferencesUtils.getString(mContext, cityList.get(i), null);
            WeatherInfo weatherInfo = WeatherInfo.objectFromData(response);
            mWeatherList.add(weatherInfo);
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_warnning, parent, false);
        final ViewHolder holder =  new ViewHolder(v);
        holder.switchButton.setOnCheckedChangeListener((view, isChecked) -> {
            int position = holder.getAdapterPosition();
            if (prefs.getString(cityList.get(position)+"warn","0").equals("0")) {
                editor.putString(cityList.get(position)+"warn","1");
                editor.apply();
            } else {
                editor.putString(cityList.get(position)+"warn","0");
                editor.apply();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherInfo weatherInfo = mWeatherList.get(position);
        holder.address.setText(weatherInfo.getHeWeather5().get(0).getBasic().getCity());
        if (prefs.getString(cityList.get(position)+"warn","0").equals("1")) {
            holder.switchButton.setChecked(true);
        }
    }

    public CityWarnAdapter(List<String> List){
        cityList = List;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView address;
        SwitchButton switchButton;

        ViewHolder(View view) {
            super(view);
            address = view.findViewById(R.id.tv_name);
            switchButton = view.findViewById(R.id.sb_warning);

        }
    }
}
