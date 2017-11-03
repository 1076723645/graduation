package com.example.finaldesig.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.example.finaldesig.util.LogUtil;
import com.example.finaldesig.util.PermissionUtil;
import com.example.finaldesig.util.Utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class GuideActivity extends AppCompatActivity {


    private final static String fileName = "code.json";
    private static final String ACTIVITY_TAG="LogDemo";
    private LocationClient mlocationClient;
    private String addressCity;//定位城市
    private  SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // setContentView(R.layout.activity_guide);
        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        PermissionUtil.requestPermission(this);//每次启动都请求权限
        LocationClientOption option =new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        mlocationClient.setLocOption(option);
        mlocationClient.start();
    }

    protected void onDestroy(){
        super.onDestroy();
        mlocationClient.stop();
    }

    private void initData(){
        setting = getSharedPreferences("IS_FIRST", 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        if(user_first){//第一次
            new FetchCodesTask().execute(fileName);
        }else{
            LogUtil.i(ACTIVITY_TAG,"不是第一次");
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            intent.putExtra("city",addressCity);
            startActivity(intent);
            finish();
        }
    }

    private class FetchCodesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            //获得assets资源管理器
            AssetManager assetManager = GuideActivity.this.getAssets();
            //使用IO流读取json文件内容
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        assetManager.open(fileName),"utf-8"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
        @Override
        protected void onPostExecute(String json) {
            boolean result = false;
            result = Utility.handleCityCodeResponce(json);
            if (result == true) {
                LogUtil.i(ACTIVITY_TAG,"第一次打开");
                setting.edit().putBoolean("FIRST", false).commit();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                intent.putExtra("city",addressCity);
                startActivity(intent);
                finish();
            }else {
                LogUtil.e(ACTIVITY_TAG,"读取失败");
            }
        }
    }

    public void onRequestPermissionsResult(final int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(GuideActivity.this, requestCode, permissions, grantResults);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder currentPosition = new StringBuilder();
            addressCity = currentPosition.append(location.getCity()).toString();
            addressCity = addressCity.replace("市","");
            addressCity = addressCity.replace("县","");
            LogUtil.i(ACTIVITY_TAG,addressCity);
            initData();
        }
    }

}