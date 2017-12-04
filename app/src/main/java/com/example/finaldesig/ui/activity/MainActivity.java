package com.example.finaldesig.ui.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.ImageButton;



import com.example.finaldesig.R;
import com.example.finaldesig.db.CityCode;

import com.example.finaldesig.gson.Weather;
import com.example.finaldesig.ui.adapter.FragAdapter;
import com.example.finaldesig.ui.fragment.WeatherFragment;
import com.example.finaldesig.util.DataUtil;
import com.example.finaldesig.util.HttpUtil;
import com.example.finaldesig.util.LogUtil;
import com.example.finaldesig.util.PermissionUtil;
import com.example.finaldesig.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG="LogDemo";
    private static final String CONTENTLIST = "contentList";
    private String addressCity;//定位城市
    private ViewPager viewPager;
    private FragAdapter adapter;
    private List<String> contentList = new ArrayList<>();//内容表
    private List<WeatherFragment> fragmentList = new ArrayList<>(); //碎片表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        initDate();
    }


    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        LogUtil.d(ACTIVITY_TAG,"NewIntent");
        setIntent(intent);
        String newCity = getIntent().getStringExtra("address");
        contentList.add(DataUtil.getAddressCityCode(newCity));
        WeatherFragment weatherFragment = WeatherFragment.newInstance(contentList, contentList.size()-1);
        fragmentList.add(weatherFragment);
        adapter.notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(contentList.size());
        viewPager.setCurrentItem(contentList.size()-1);
        LogUtil.d(ACTIVITY_TAG,contentList.toString());
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putString(CONTENTLIST,DataUtil.listToString(contentList));
        editor.apply();
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
            }
        }
    }

    private void initView(){
        ImageButton addCity = (ImageButton)findViewById(R.id.add_menu);
        viewPager = (ViewPager) findViewById(R.id.city_viewpager);
        for(int i=0;i<contentList.size();i++){
            WeatherFragment weatherFragment = WeatherFragment.newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter = new FragAdapter(getSupportFragmentManager(), (ArrayList<WeatherFragment>) fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(contentList.size());
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CityManagerActivity.class);
                intent.putStringArrayListExtra("list", (ArrayList<String>) contentList);
                startActivityForResult(intent,1);
            }
        });
        ImageButton setting = (ImageButton)findViewById(R.id.setting);
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="
                + weatherId + "&key=9437b90c51624dd08de1707b34416f91";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(ACTIVITY_TAG, "获取失败");
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
}
