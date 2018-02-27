package com.example.finaldesign.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LocationUtils;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.PermissionUtil;
import com.example.finaldesign.util.Utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class GuideActivity extends AppCompatActivity {


    private static final String fileName = "code.json";
    private static final String ACTIVITY_TAG="GuideActivity";
    private String addressCity;//定位城市
    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Boolean permission = PermissionUtil.handPermission(this);
        if (permission)
            getLocation();
    }

    protected void onDestroy(){
        super.onDestroy();
        LocationUtils.unRegisterListener(this);
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
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(GuideActivity.this).toBundle());
            finish();
        }
    }

    @SuppressLint("StaticFieldLeak")
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
            boolean result;
            result = Utility.handleCityCodeResponce(json);
            if (result) {
                LogUtil.i(ACTIVITY_TAG,"第一次打开");
                setting.edit().putBoolean("FIRST", false).apply();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                intent.putExtra("city",addressCity);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(GuideActivity.this).toBundle());
                finish();
            }else {
                LogUtil.e(ACTIVITY_TAG,"读取失败");
            }
        }
    }

    private void getLocation(){
        Location location = LocationUtils.getBestLocation(this);
        if (location != null){
            getCityName(location);
        }
    }

    private void getCityName(Location location){
        String url = "http://maps.google.cn/maps/api/geocode/json?latlng="+location.getLatitude()+","+location.getLongitude()+"&sensor=true,language=zh-CN";
        LogUtil.e(ACTIVITY_TAG,url);
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GuideActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                        initData();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                addressCity = Utility.handleLocationResponse(responseText);
                if (addressCity != null) {
                    addressCity = addressCity.substring(0,2);
                    LogUtil.e(ACTIVITY_TAG,addressCity);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    });
                }
            }
        });
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 100:
                if (grantResults.length>0) {
                    for (int result:grantResults){
                        if (result!= PackageManager.PERMISSION_GRANTED) {
                            //没有同意的逻辑
                            Toast.makeText(this, "需要同意所有权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //同意后的逻辑
                    LogUtil.e(ACTIVITY_TAG,"权限申请成功");
                    getLocation();
                }else {
                    Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
