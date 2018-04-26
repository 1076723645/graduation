package com.example.finaldesign.ui.activity;


import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.example.finaldesign.R;
import com.example.finaldesign.base.BaseActivity;
import com.example.finaldesign.model.bean.CityMessage;
import com.example.finaldesign.presenter.SplashPresenter;
import com.example.finaldesign.util.LocationUtil;
import com.example.finaldesign.util.LocationUtils;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.SystemFit;
import com.example.finaldesign.util.ToastUtil;
import com.example.finaldesign.view.SplashView;
import com.tbruyelle.rxpermissions2.RxPermissions;



public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashView{

    private String addressCity;//定位城市

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    protected void initData() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        mPresenter.newLocation();
                    } else {
                        ToastUtil.shortShow("需要定位权限~");
                        Intent intent = new Intent(this, Main2Activity.class);
                        intent.putExtra("cityName", addressCity);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void fitSystem() {
        super.fitSystem();
        SystemFit.fitSystemWhite(this);
    }

    @Override
    public void onLocationSuccess(Location location) {
        ToastUtil.shortShow(location.getLatitude()+ "," +location.getLongitude());
        mPresenter.getCityMessage(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
    }

    @Override
    public void getCitySuccess(CityMessage cityMessage) {
        LogUtil.e(cityMessage.toString());
        addressCity = cityMessage.getAddressComponent().getDistrict();
        mPresenter.isChinese(addressCity);
    }

    @Override
    public void getChinese(String s) {
        ToastUtil.shortShow(addressCity);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("cityName",addressCity);
        startActivity(intent);
        finish();
    }

    @Override
    public void showErrorMsg(String msg) {
        if (msg.equals("notChinese")){
            ToastUtil.shortShow("不支持国外城市");
        }
        addressCity = "";
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("cityName", addressCity);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil.unregister();
    }
}
