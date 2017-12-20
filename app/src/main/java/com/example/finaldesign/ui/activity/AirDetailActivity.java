package com.example.finaldesign.ui.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.finaldesign.R;
import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.presenter.CircleBar;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.Utility;

public class AirDetailActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG="AirDetailActivity";
    private String weatherid;
    private Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_detail);
        weatherid = getIntent().getStringExtra("id");
        LogUtil.e(ACTIVITY_TAG,weatherid);
        initDate();
        initView();
    }

    public void initDate(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherInfo = pref.getString(weatherid,null);
        weather = Utility.handleWeatherResponse(weatherInfo);
    }

    public void initView(){
        TextView pm25 = (TextView) findViewById(R.id.tv_pm25);
        TextView pm10 = (TextView) findViewById(R.id.tv_pm10);
        TextView so2 = (TextView) findViewById(R.id.tv_SO2);
        TextView co = (TextView) findViewById(R.id.tv_CO);
        TextView no2 = (TextView) findViewById(R.id.tv_NO2);
        TextView o3 = (TextView) findViewById(R.id.tv_O3);
        TextView qul = (TextView) findViewById(R.id.tv_qul);
        TextView update = (TextView) findViewById(R.id.tv_update_time);
        TextView info = (TextView) findViewById(R.id.tv_info);
        CircleBar circleBar = (CircleBar) findViewById(R.id.circleBar);

        circleBar.setDesText(weather.aqi.city.aqi);
        pm25.setText(weather.aqi.city.pm25);
        pm10.setText(weather.aqi.city.pm10);
        so2.setText(weather.aqi.city.so2);
        o3.setText(weather.aqi.city.o3);
        no2.setText(weather.aqi.city.no2);
        co.setText(weather.aqi.city.co);
        qul.setText(weather.aqi.city.qlty);
        update.setText("刚刚更新");
        info.setText(weather.suggestion.carWash.info);
    }
}
