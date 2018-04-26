package com.example.finaldesign.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.finaldesign.App;
import com.example.finaldesign.R;
import com.example.finaldesign.base.BaseFragment;
import com.example.finaldesign.model.bean.WeatherBean;
import com.example.finaldesign.model.bean.WeatherInfo;
import com.example.finaldesign.presenter.MainPresenter;
import com.example.finaldesign.ui.activity.AirDetailActivity;
import com.example.finaldesign.ui.activity.Main2Activity;
import com.example.finaldesign.ui.widget.CircleBar;
import com.example.finaldesign.ui.widget.CompatToolbar;
import com.example.finaldesign.ui.widget.MiuiWeatherView;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.NetUtils;
import com.example.finaldesign.util.SharePreferencesUtils;
import com.example.finaldesign.util.ToastUtil;
import com.example.finaldesign.view.MainView;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hui on 2018/3/12.
 */

public class WeatherFragment2 extends BaseFragment<MainPresenter> implements MainView, NestedScrollView.OnScrollChangeListener {

    @BindView(R.id.weather_temp)
    TextView tmp;
    @BindView(R.id.tv_qul)
    TextView qul;
    @BindView(R.id.tv_qul_count)
    TextView qulCount;
    @BindView(R.id.weather_city_name)
    TextView titleCity;
    @BindView(R.id.weather_info)
    TextView cityWeather;
    @BindView(R.id.weather_wind)
    TextView cityWind;
    @BindView(R.id.wind_lv)
    TextView windLv;
    @BindView(R.id.hum_lv)
    TextView humLv;
    @BindView(R.id.fl_lv)
    TextView flLv;

    @BindView(R.id.weather_bg)
    ImageView bg;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.forest_layout)
    LinearLayout forecastLayout;
    @BindView(R.id.weather)
    MiuiWeatherView weatherView;
    @BindView(R.id.toolbar)
    CompatToolbar toolbar;
    @BindView(R.id.iv_header_cond)
    ImageView ivCond;
    @BindView(R.id.tv_header_title)
    TextView toolCity;
    @BindView(R.id.tv_header_tmp)
    TextView toolTemp;

    @BindView(R.id.AQI_bar)
    CircleBar aqiBar;
    @BindView(R.id.PM25_bar)
    CircleBar pmBar;
    @BindView(R.id.qul_more)
    TextView qulMore;
    @BindView(R.id.tv_air_qul)
    TextView airQul;
    @BindView(R.id.tv_time)
    TextView updateTime;

    @BindView(R.id.tv_comfortable)
    TextView comfortText;
    @BindView(R.id.tv_wash)
    TextView carWashText;
    @BindView(R.id.tv_sport)
    TextView sportText;
    @BindView(R.id.tv_uv)
    TextView uvText;
    @BindView(R.id.tv_comfortable_info)
    TextView comfortInfo;
    @BindView(R.id.tv_wash_info)
    TextView carWashInfo;
    @BindView(R.id.tv_sport_info)
    TextView sportInfo;
    @BindView(R.id.tv_uv_info)
    TextView uvInfo;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    private List<String> contentList = new ArrayList<>();
    private int flag;

    private String cityName;

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
    protected void initPresenter() {
        mPresenter = new MainPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weather;
    }

    /**
     * 初始化界面，读取缓存信息
     */
    @Override
    protected void initData() {
        toolbar.setAlpha(0);
        swipeRefresh.setColorSchemeResources(R.color.btn_blue);
        scrollView.setOnScrollChangeListener(this);
        cityName = contentList.get(flag);
        String responseText = SharePreferencesUtils.getString(mContext,cityName,"");
        if (!responseText.equals("")){
            showWeatherInfo(WeatherInfo.objectFromData(responseText));
        }
        swipeRefresh.setOnRefreshListener(() -> {
            if (NetUtils.isConnected(mContext)) {
                mPresenter.getWeatherMessage(cityName);
            }else {
                ToastUtil.shortShow("无网络连接，请检查网络");
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * 网络懒加载,更新数据
     */
    @Override
    public void lazyLoad() {
        mPresenter.getWeatherMessage(cityName);
    }

    @Override
    public void loadWeatherInfoSuccess(WeatherInfo weather) {
        Main2Activity activity = (Main2Activity) mActivity;
        activity.dismiss();
        swipeRefresh.setRefreshing(false);
        showWeatherInfo(weather);
        String responseText = new Gson().toJson(weather, WeatherInfo.class);
        SharePreferencesUtils.put(mContext, cityName, responseText);
    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtil.shortShow(msg);
    }

    public static WeatherFragment2 newInstance(List<String> cityList, int flag){
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        bundle.putStringArrayList("list", (ArrayList<String>) cityList);
        WeatherFragment2 weatherFragment = new WeatherFragment2();
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }

    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(WeatherInfo weather){
        WeatherInfo.HeWeather5Bean weatherBean = weather.getHeWeather5().get(0);
        String cityName = weatherBean.getBasic().getCity();
        String degree = weatherBean.getNow().getTmp();
        String weatherInfo = weatherBean.getNow().getCond().getTxt();
        String wind = weatherBean.getNow().getWind().getDir();
        String windlv = weatherBean.getNow().getWind().getSc();
        String pm25 = weatherBean.getAqi().getCity().getPm25();
        String quality = weatherBean.getAqi().getCity().getQlty();
        String aqi = weatherBean.getAqi().getCity().getAqi();
        // String sunRise = null,sunSet = null;
        if (windlv.equals("微风")){
            windlv = "1";
        }
        String humlv = weatherBean.getNow().getHum();
        String fllv = weatherBean.getNow().getFl();
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
        for (int i=0;i<weatherBean.getDaily_forecast().size();i++){
            WeatherInfo.HeWeather5Bean.DailyForecastBean forecast = weatherBean.getDaily_forecast().get(i);
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.forest_item, forecastLayout, false);
            TextView dataText = v.findViewById(R.id.forest_date);
            TextView infoText = v.findViewById(R.id.forest_info);
            TextView maxText = v.findViewById(R.id.max_temp);
            TextView minText = v.findViewById(R.id.min_temp);
            ImageView png = v.findViewById(R.id.forest_pic);
            if (i==0){
                dataText.setText("今天");
            /*  sunRise = forecast.sun.sunRise;
                sunSet = forecast.sun.sunSet;*/
            }else if (i==1){
                dataText.setText("明天");
            }else {
                dataText.setText(DataUtil.getWeekByDateStr(forecast.getDate()));
            }
            png.setImageResource(DataUtil.getWeatherPng(forecast.getCond().getTxt_d()));
            infoText.setText(forecast.getCond().getTxt_d());
            maxText.setText(forecast.getTmp().getMax());
            minText.setText(forecast.getTmp().getMin());
            forecastLayout.addView(v);
        }//未来3天天气预报


        List<WeatherBean> data = DataUtil.getHourData(weatherBean);
        weatherView.setData(data);//24小时天气预报

        airQul.setText(quality);
        String time = weatherBean.getBasic().getUpdate().getLoc();
        updateTime.setText(time.substring(time.length()-5, time.length())+" 发布");
        aqiBar.setText("AQI");
        LogUtil.i("aqi", aqi);
        LogUtil.i("pm25", pm25);
        aqiBar.setDesText(aqi);
        pmBar.setText("PM25");
        pmBar.setDesText(pm25);
        pmBar.setShowText("首要污染物");
        /*qulMore.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AirDetailActivity.class);
            intent.putExtra("id",cityName);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        });//空气质量*/

        comfortText.setText(weatherBean.getSuggestion().getComf().getBrf());
        comfortInfo.setText(weatherBean.getSuggestion().getComf().getTxt());
        carWashText.setText(weatherBean.getSuggestion().getCw().getBrf());
        carWashInfo.setText(weatherBean.getSuggestion().getCw().getTxt());
        sportText.setText(weatherBean.getSuggestion().getSport().getBrf());
        sportInfo.setText(weatherBean.getSuggestion().getSport().getTxt());
        uvText.setText(weatherBean.getSuggestion().getUv().getBrf());
        uvInfo.setText(weatherBean.getSuggestion().getUv().getTxt());//生活建议
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
