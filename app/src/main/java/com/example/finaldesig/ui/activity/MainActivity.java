package com.example.finaldesig.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;


import com.example.finaldesig.R;
import com.example.finaldesig.db.CityCode;

import com.example.finaldesig.ui.adapter.FragAdapter;
import com.example.finaldesig.ui.fragment.WeatherFragment;
import com.example.finaldesig.util.LogUtil;
import com.example.finaldesig.util.PermissionUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragAdapter adapter;
    private static final String ACTIVITY_TAG="LogDemo";
    private String addressCity;//定位城市
    private List<String> contentList = new ArrayList<String>(); //内容链表
    private List<WeatherFragment> fragmentList = new ArrayList<WeatherFragment>(); //碎片链表
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
        Intent intent = getIntent();
        addressCity = intent.getStringExtra("city");
        LogUtil.i(ACTIVITY_TAG,addressCity);
        initList();
        initView();
    }

    public void initView(){
        ImageButton addCity = (ImageButton)findViewById(R.id.add_menu);
        viewPager = (ViewPager)findViewById(R.id.city_viewpager);
        for(int i=0;i<contentList.size();i++){
            WeatherFragment weatherFragment = new WeatherFragment().newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter = new FragAdapter(getSupportFragmentManager(), (ArrayList<WeatherFragment>) fragmentList);
        viewPager.setAdapter(adapter);
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CityManagerActivity.class);
                startActivity(intent);
            }
        });
        ImageButton setting = (ImageButton)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CityManagerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initList(){
        if (addressCity!=null){
            List<CityCode> cityCodes = DataSupport.where("cityName=?",addressCity).find(CityCode.class);
            contentList.add(cityCodes.get(0).getCityCode());
        }
        contentList.add("页面一");
        contentList.add("页面二");
    }

    public void onRequestPermissionsResult(final int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }

}
