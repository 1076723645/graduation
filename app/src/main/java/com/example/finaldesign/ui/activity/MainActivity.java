package com.example.finaldesign.ui.activity;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;

import android.content.Intent;

import android.content.SharedPreferences;

import android.graphics.drawable.BitmapDrawable;


import android.preference.PreferenceManager;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finaldesign.R;

import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.model.helper.DeleteCityEvent;
import com.example.finaldesign.util.SystemFit;
import com.example.finaldesign.ui.adapter.FragAdapter;
import com.example.finaldesign.ui.fragment.WeatherFragment;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.Utility;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;


import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private static final String ACTIVITY_TAG="MainActivity";
    private static final String CONTENTLIST = "contentList";
    private PopupWindow popupWindow;
    private View bgView;
    private TextView mPopuView;
    private ViewPager viewPager;
    private FragAdapter adapter;
    private List<String> contentList = new ArrayList<>();//内容表
    private List<WeatherFragment> fragmentList = new ArrayList<>(); //碎片表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        SystemFit.fitSys(this);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);//注册EventBus
        initDate();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        String weatherId = getIntent().getStringExtra("weather_id");
        LogUtil.d(ACTIVITY_TAG,weatherId);
        Boolean isExist = false;
        for (int i=0; i<contentList.size(); i++){
            if (weatherId.equals(contentList.get(i))){
                viewPager.setCurrentItem(i);
                isExist = true;
                LogUtil.i(ACTIVITY_TAG,"城市已经存在");
            }
        }
        if (!isExist) {
            contentList.add(weatherId);
            WeatherFragment weatherFragment = WeatherFragment.newInstance(contentList, contentList.size() - 1);
            fragmentList.add(weatherFragment);
            adapter.notifyDataSetChanged();
            viewPager.setOffscreenPageLimit(contentList.size());
            viewPager.setCurrentItem(contentList.size() - 1);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            editor.putString(CONTENTLIST, DataUtil.listToString(contentList));
            editor.apply();
        }
    }

    private void initDate(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String content = prefs.getString(CONTENTLIST,null);
        if (content != null && !content.equals("")) {
            contentList = new ArrayList<>(DataUtil.stringToList(content));
            LogUtil.d(ACTIVITY_TAG,contentList.toString());
            initView();
        } else {
            Intent intent = getIntent();
            String addressCity = intent.getStringExtra("city");
            if (addressCity != null){
                LogUtil.i(ACTIVITY_TAG, addressCity);
                contentList.add(DataUtil.getAddressCityCode(addressCity));
                LogUtil.d(ACTIVITY_TAG,contentList.toString());
                requestWeather(contentList.get(0));
            }else {
                initView();
                Intent addIntent = new Intent(MainActivity.this,CitySearchActivity.class);
                startActivity(addIntent);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void initView(){
        bgView = findViewById(R.id.v_bg);
        mPopuView = findViewById(R.id.text_menu);
        mPopuView.bringToFront();
        ImageView addCity = findViewById(R.id.add_menu);
        viewPager = findViewById(R.id.city_viewpager);
        for(int i=0;i<contentList.size();i++){
            WeatherFragment weatherFragment = WeatherFragment.newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter = new FragAdapter(getSupportFragmentManager(),this);
        adapter.setData((ArrayList<WeatherFragment>) fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        addCity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,CityManagerActivity.class);
            intent.putStringArrayListExtra("list", (ArrayList<String>) contentList);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        });
        ImageView setting = findViewById(R.id.setting);
        setting.setOnClickListener(v -> showPopupWindow());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    final int page = data.getIntExtra("data_return",0);
                    viewPager.setCurrentItem(page);
                    WeatherFragment weatherFragment = fragmentList.get(page);
                    weatherFragment.fullScroll();
                }
                break;
            default:
        }
    }

    @Subscribe
    public void onEventMainThread(DeleteCityEvent event) {
        contentList = event.getMsg();
        LogUtil.e("main",contentList.toString());
        fragmentList.clear();
        for(int i=0;i<contentList.size();i++){
            WeatherFragment weatherFragment = WeatherFragment.newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter.notifyDataSetChanged();
    }

    private void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=9437b90c51624dd08de1707b34416f91";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "网络连接异常，请稍后重试", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(() -> {
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.putString(weatherId,responseText);
                        editor.putString(CONTENTLIST, DataUtil.listToString(contentList));
                        editor.apply();
                        initView();
                    } else {
                        LogUtil.e(ACTIVITY_TAG, "获取失败");
                    }
                });
            }
        });
    }

    private void showPopupWindow() {//显示窗口

        View contentView = LayoutInflater.from(this).inflate(R.layout.popuw_setting, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow (contentView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        }
        //在控件上方显示
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.showPopupAnimation);
        popupWindow.showAsDropDown(mPopuView,0,- Utility.dip2px(this,175));
        bgView.setVisibility(View.VISIBLE);
        TextView setting = contentView.findViewById(R.id.tv_setting);
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            intent.putStringArrayListExtra("cityList", (ArrayList<String>) contentList);
            startActivity(intent);
            popupWindow.dismiss();
            bgView.setVisibility(View.GONE);
        });
        popupWindow.setOnDismissListener(() -> bgView.setVisibility(View.GONE));
    }
}
