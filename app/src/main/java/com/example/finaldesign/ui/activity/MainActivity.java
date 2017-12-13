package com.example.finaldesign.ui.activity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.drawable.BitmapDrawable;


import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.finaldesign.BuildConfig;
import com.example.finaldesign.R;

import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.presenter.Bean.DeleteCityEvent;
import com.example.finaldesign.presenter.SystemFit;
import com.example.finaldesign.ui.adapter.FragAdapter;
import com.example.finaldesign.ui.fragment.WeatherFragment;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.PermissionUtil;
import com.example.finaldesign.util.Utility;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{

    private static final String ACTIVITY_TAG="MainActivity";
    private static final String CONTENTLIST = "contentList";
    private PopupWindow popupWindow;
    private View mPopuView;
    private String addressCity;//定位城市
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
        if (content != null) {
            LogUtil.d(ACTIVITY_TAG,content);
            contentList = new ArrayList<>(DataUtil.stringToList(content));
            LogUtil.d(ACTIVITY_TAG,contentList.toString());
            initView();
        } else {
            Intent intent = getIntent();
            addressCity = intent.getStringExtra("city");
            LogUtil.i(ACTIVITY_TAG, addressCity);
            if (!addressCity.equals("null")){
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

    private void initView(){
        mPopuView = findViewById(R.id.view_line);
        ImageButton addCity = (ImageButton)findViewById(R.id.add_menu);
        viewPager = (ViewPager) findViewById(R.id.city_viewpager);
        for(int i=0;i<contentList.size();i++){
            WeatherFragment weatherFragment = WeatherFragment.newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter = new FragAdapter(getSupportFragmentManager(),this);
        adapter.setData((ArrayList<WeatherFragment>) fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(contentList.size());
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CityManagerActivity.class);
                intent.putStringArrayListExtra("list", (ArrayList<String>) contentList);
                startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });
        ImageButton setting = (ImageButton)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    viewPager.setCurrentItem(data.getIntExtra("data_return",0));
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

    public void requestWeather(final String weatherId) {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString(weatherId,responseText);
                            editor.apply();
                            initView();
                        } else {
                            LogUtil.e(ACTIVITY_TAG, "获取失败");
                        }
                    }
                });
            }
        });
    }

    public void showPopupWindow() {//显示she窗口

        View contentView = LayoutInflater.from(this).inflate(R.layout.popuw_setting, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow (contentView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        }
        //在控件上方显示
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.showPopupAnimation);
        popupWindow.showAsDropDown(mPopuView,0,- Utility.dip2px(this,135));
        TextView setting = (TextView) contentView.findViewById(R.id.tv_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                intent.putStringArrayListExtra("cityList", (ArrayList<String>) contentList);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }
}
