package com.example.finaldesign.ui.fragment;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finaldesign.R;
import com.example.finaldesign.gson.Forecast;
import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.ui.widget.CircleBar;
import com.example.finaldesign.ui.widget.CompatToolbar;
import com.example.finaldesign.ui.widget.MiuiWeatherView;
import com.example.finaldesign.model.bean.WeatherBean;
import com.example.finaldesign.ui.activity.AirDetailActivity;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.NetUtils;
import com.example.finaldesign.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherFragment extends Fragment implements NestedScrollView.OnScrollChangeListener{

    private List<String> contentList = new ArrayList<>();
    private int flag;
    private static final String ActLog = "ActivityLog";
    public SwipeRefreshLayout swipeRefresh;
    protected boolean isVisible;

    private TextView tmp,qul,qulCount;
    private TextView titleCity;
    private TextView cityWeather;
    private TextView cityWind;
    private TextView windLv;
    private TextView humLv;
    private TextView flLv;
    private ImageView bg;

    private NestedScrollView scrollView;
    private LinearLayout forecastLayout;
    private MiuiWeatherView weatherView;
    private CompatToolbar toolbar;
    private ImageView ivCond;
    private TextView toolCity;
    private TextView toolTemp;

    private CircleBar aqiBar;
    private CircleBar pmBar;
    private TextView qulMore;
    private TextView airQul;
    private TextView updateTime;

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
        toolbar = (CompatToolbar) v.findViewById(R.id.toolbar);
        toolbar.setAlpha(0);
        scrollView = (NestedScrollView) v.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(this);
        swipeRefresh = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh);
        titleCity = (TextView)v.findViewById(R.id.weather_city_name);
        cityWeather = (TextView)v.findViewById(R.id.weather_info);
        cityWind = (TextView)v.findViewById(R.id.weather_wind);
        windLv = (TextView)v.findViewById(R.id.wind_lv);
        humLv = (TextView)v.findViewById(R.id.hum_lv);
        flLv = (TextView)v.findViewById(R.id.fl_lv);
        tmp = (TextView)v.findViewById(R.id.weather_temp);
        bg = (ImageView)v.findViewById(R.id.weather_bg);
        qul = (TextView)v.findViewById(R.id.tv_qul);
        qulCount = (TextView)v.findViewById(R.id.tv_qul_count);

        aqiBar = (CircleBar)v.findViewById(R.id.AQI_bar);
        pmBar = (CircleBar)v.findViewById(R.id.PM25_bar);
        qulMore = (TextView)v.findViewById(R.id.qul_more);
        airQul = (TextView)v.findViewById(R.id.tv_air_qul);
        updateTime = (TextView) v.findViewById(R.id.tv_time);

        comfortText = (TextView)v.findViewById(R.id.tv_comfortable);
        comfortInfo = (TextView)v.findViewById(R.id.tv_comfortable_info);
        carWashText = (TextView)v.findViewById(R.id.tv_wash);
        carWashInfo = (TextView)v.findViewById(R.id.tv_wash_info);
        sportText = (TextView)v.findViewById(R.id.tv_sport);
        sportInfo = (TextView)v.findViewById(R.id.tv_sport_info);
        uvText = (TextView)v.findViewById(R.id.tv_uv);
        uvInfo = (TextView)v.findViewById(R.id.tv_uv_info);

        toolCity = (TextView)v.findViewById(R.id.tv_header_title);
        toolTemp = (TextView)v.findViewById(R.id.tv_header_tmp);
        ivCond = (ImageView) v.findViewById(R.id.iv_header_cond);
    }

    public void initData(){
        if (contentList.size()!=0){
            weatherId = contentList.get(flag);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            String responseText = prefs.getString(weatherId, null);
            if (!NetUtils.isConnected(getContext())){//判断网络是否连接
                Weather weather = Utility.handleWeatherResponse(responseText);
                showWeatherInfo(weather);
                Toast.makeText(getContext(), "无网络连接，请检查网络", Toast.LENGTH_SHORT).show();
            }else {
                if (responseText!=null){
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    showWeatherInfo(weather);
                }
                requestWeather(weatherId);
            }
        }
        swipeRefresh.setColorSchemeResources(R.color.btn_blue);
        swipeRefresh.setOnRefreshListener(() -> {
            if (NetUtils.isConnected(getContext())) {
                requestWeather(weatherId);
            }else {
                Toast.makeText(getContext(), "无网络连接，请检查网络", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
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
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(getContext(), "网络连接异常，请稍后重试", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(() -> {
                    if (weather!=null&&"ok".equals(weather.status)){
                        //Log.i("weather",weather.toString());
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString(weatherId,responseText);
                        editor.apply();
                        showWeatherInfo(weather);
                    }else {
                        Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefresh.setRefreshing(false);
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
        String pm25 = weather.aqi.city.pm25;
        String quality = weather.aqi.city.qlty;
        String aqi = weather.aqi.city.aqi;
       // String sunRise = null,sunSet = null;
        if (windlv.equals("微风")){
            windlv = "1";
        }
        String humlv = weather.now.humidity;
        String fllv = weather.now.bodytemp;
        bg.setImageResource(DataUtil.getWeatherBg(weatherInfo));
        if (Integer.parseInt(pm25)>150){
            bg.setImageResource(R.drawable.bg_pmdirt);
        }
        qul.setText("空气"+quality);
        qulCount.setText(aqi);
        titleCity.setText(cityName);
        tmp.setText(degree);
        cityWind.setText(wind);
        cityWeather.setText(weatherInfo);
        windLv.setText(windlv+"级");
        humLv.setText(humlv+"%");
        flLv.setText(fllv+"°");//基本信息

        ivCond.setImageResource(DataUtil.getWeatherColourPng(weatherInfo));
        toolCity.setText(cityName);
        toolTemp.setText(degree+"°");
        toolbar.setBackgroundResource(R.color.colorPrimary);

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
            /*  sunRise = forecast.sun.sunRise;
                sunSet = forecast.sun.sunSet;*/
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

        airQul.setText(quality);
        String time = weather.basic.update.updateTime;
        updateTime.setText(time.substring(time.length()-5, time.length())+" 发布");
        aqiBar.setText("AQI");
        LogUtil.i("aqi", aqi);
        LogUtil.i("pm25", pm25);
        aqiBar.setDesText(aqi);
        pmBar.setText("PM25");
        pmBar.setDesText(pm25);
        pmBar.setShowText("首要污染物");
        qulMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AirDetailActivity.class);
                intent.putExtra("id",weatherId);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });//空气质量

        comfortText.setText(weather.suggestion.comfot.msg);
        comfortInfo.setText(weather.suggestion.comfot.info);
        carWashText.setText(weather.suggestion.carWash.msg);
        carWashInfo.setText(weather.suggestion.carWash.info);
        sportText.setText(weather.suggestion.sport.msg);
        sportInfo.setText(weather.suggestion.sport.info);
        uvText.setText(weather.suggestion.ultraviolet.msg);
        uvInfo.setText(weather.suggestion.ultraviolet.info);//生活建议
    }

    public void fullScroll(){//滑动到顶部
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_UP));
    }

    @Override
    public void onScrollChange(NestedScrollView v, int l, int t, int oldl, int oldt) {
        //Y轴偏移量
        float scrollY = v.getScrollY();
        float headerHeight = getResources().getDimension(R.dimen.index_header);
        float minHeaderHeight = getResources().getDimension(R.dimen.header);
        //变化率
        float headerBarOffsetY = headerHeight - minHeaderHeight;//Toolbar与header高度的差值
        float offset = 1 - Math.max((headerBarOffsetY - scrollY) / headerBarOffsetY, 0f);
        //Toolbar背景色透明度
        toolbar.setAlpha(offset);
        if (scrollY <= headerBarOffsetY){
            toolbar.setAlpha(0);
        }
    }
}
