package com.example.finaldesig.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finaldesig.R;
import com.example.finaldesig.gson.Forecast;
import com.example.finaldesig.gson.Weather;
import com.example.finaldesig.presenter.CircleBar;
import com.example.finaldesig.presenter.MiuiWeatherView;
import com.example.finaldesig.presenter.WeatherBean;
import com.example.finaldesig.ui.activity.MainActivity;
import com.example.finaldesig.util.DataUtil;
import com.example.finaldesig.util.HttpUtil;
import com.example.finaldesig.util.LogUtil;
import com.example.finaldesig.util.Utility;

import java.io.IOException;;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class WeatherFragment extends Fragment {

    private List<String> contentList = new ArrayList<>();
    private String responseText;
    private int flag;
    private static final String ActLog = "ActivityLog";
    public SwipeRefreshLayout swipeRefresh;

    private TextView tmp;
    private TextView titleCity;
    private TextView cityWeather;
    private TextView cityWind;
    private TextView windLv;
    private TextView humLv;
    private TextView flLv;
    private ImageView bg;

    private LinearLayout forecastLayout;
    private MiuiWeatherView weatherView;

    private CircleBar aqiBar;
    private CircleBar pmBar;
    private TextView qulMore;
    private TextView airQul;

    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView uvText;
    private TextView comfortInfo;
    private TextView carWashInfo;
    private TextView sportInfo;
    private TextView uvInfo;

    private String weatherId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            flag = bundle.getInt("flag");
            contentList = bundle.getStringArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_weather, container,false);
        forecastLayout = (LinearLayout)view.findViewById(R.id.forest_layout);
        weatherView = (MiuiWeatherView)view.findViewById(R.id.weather);
        initView(view);
        initData();
        return view;
    }

    public void initView(View v){
        swipeRefresh = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh);
        titleCity = (TextView)v.findViewById(R.id.weather_city_name);
        cityWeather = (TextView)v.findViewById(R.id.weather_info);
        cityWind = (TextView)v.findViewById(R.id.weather_wind);
        windLv = (TextView)v.findViewById(R.id.wind_lv);
        humLv = (TextView)v.findViewById(R.id.hum_lv);
        flLv = (TextView)v.findViewById(R.id.fl_lv);
        tmp = (TextView)v.findViewById(R.id.weather_temp);
        bg = (ImageView)v.findViewById(R.id.weather_bg);
        aqiBar = (CircleBar)v.findViewById(R.id.AQI_bar);
        pmBar = (CircleBar)v.findViewById(R.id.PM25_bar);
        qulMore = (TextView)v.findViewById(R.id.qul_more);
        airQul = (TextView)v.findViewById(R.id.tv_air_qul);
        comfortText = (TextView)v.findViewById(R.id.tv_comfortable);
        comfortInfo = (TextView)v.findViewById(R.id.tv_comfortable_info);
        carWashText = (TextView)v.findViewById(R.id.tv_wash);
        carWashInfo = (TextView)v.findViewById(R.id.tv_wash_info);
        sportText = (TextView)v.findViewById(R.id.tv_sport);
        sportInfo = (TextView)v.findViewById(R.id.tv_sport_info);
        uvText = (TextView)v.findViewById(R.id.tv_uv);
        uvInfo = (TextView)v.findViewById(R.id.tv_uv_info);
    }

    public void initData(){
        if (contentList.size()!=0){
            weatherId = contentList.get(flag);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            responseText = prefs.getString(weatherId,null);
            if (responseText!=null) {
                Weather weather = Utility.handleWeatherResponse(responseText);
                showWeatherInfo(weather);
            }else {
                requestWeather(weatherId);
            }
        }
        swipeRefresh.setColorSchemeResources(R.color.btn_blue);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
    }

    public static WeatherFragment newInstance(List<String> weatherList, int flag){
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        bundle.putStringArrayList("list", (ArrayList<String>) weatherList);
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }

    public void requestWeather(final String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                +weatherId+"&key=9437b90c51624dd08de1707b34416f91";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(ActLog,"获取失败");
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            //Log.i("weather",weather.toString());
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString(weatherId,responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            LogUtil.e(ActLog,"获取失败");
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(Weather weather){
        String cityName = weather.basic.cityName;
        String degree = weather.now.temperature;
        String weatherInfo = weather.now.more.info;
        String wind = weather.now.windMore.where;
        String windlv = weather.now.windMore.how;
        if (windlv.equals("微风")){
            windlv = "1";
        }
        String humlv = weather.now.humidity;
        String fllv = weather.now.bodytemp;
        bg.setImageResource(DataUtil.getWeatherBg(weatherInfo));
        titleCity.setText(cityName);
        tmp.setText(degree);
        cityWind.setText(wind);
        cityWeather.setText(weatherInfo);
        windLv.setText(windlv+"级");
        humLv.setText(humlv+"%");
        flLv.setText(fllv+"°");//基本信息

        forecastLayout.removeAllViews();
        for (int i=0;i<weather.forecastList.size();i++){
            Forecast forecast = weather.forecastList.get(i);
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.forest_item,forecastLayout,false);
            TextView dataText = (TextView)v.findViewById(R.id.forest_date);
            TextView infoText = (TextView)v.findViewById(R.id.forest_info);
            TextView maxText = (TextView)v.findViewById(R.id.max_temp);
            TextView minText = (TextView)v.findViewById(R.id.min_temp);
            ImageView png = (ImageView)v.findViewById(R.id.forest_pic);
            if (i==0){
                dataText.setText("今天");
            }else if (i==1){
                dataText.setText("明天");
            }else {
                dataText.setText(DataUtil.getWeekByDateStr(forecast.time_data));
            }
            png.setImageResource(DataUtil.getWeatherPng(forecast.more.info));
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(v);
        }//未来3天天气预报

        List<WeatherBean> data = DataUtil.getHourData(weather);
        weatherView.setData(data);//24小时天气预报

        airQul.setText(weather.aqi.city.qlty);
        aqiBar.setText("AQI");
        LogUtil.i("aqi", weather.aqi.city.aqi);
        LogUtil.i("pm25", weather.aqi.city.pm25);
        aqiBar.setDesText(weather.aqi.city.aqi);
        pmBar.setText("PM25");
        pmBar.setDesText(weather.aqi.city.pm25);
        pmBar.setShowText("首要污染物");//空气质量

        comfortText.setText(weather.suggestion.comfot.msg);
        comfortInfo.setText(weather.suggestion.comfot.info);
        carWashText.setText(weather.suggestion.carWash.msg);
        carWashInfo.setText(weather.suggestion.carWash.info);
        sportText.setText(weather.suggestion.sport.msg);
        sportInfo.setText(weather.suggestion.sport.info);
        uvText.setText(weather.suggestion.ultraviolet.msg);
        uvInfo.setText(weather.suggestion.ultraviolet.info);//生活建议
    }
}
