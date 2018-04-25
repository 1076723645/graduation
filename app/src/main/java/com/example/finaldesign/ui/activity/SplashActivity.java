package com.example.finaldesign.ui.activity;


import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.location.Location;

import com.example.finaldesign.R;
import com.example.finaldesign.base.BaseActivity;
import com.example.finaldesign.model.bean.CityMessage;
import com.example.finaldesign.presenter.SplashPresenter;
import com.example.finaldesign.util.LogUtil;
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
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        mPresenter.getLocation();
                    } else {
                        ToastUtil.shortShow("需要定位权限~");
                        finish();
                    }
                });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }


    @Override
    public void showErrorMsg(String msg) {
        ToastUtil.shortShow("定位失败");
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("city",addressCity);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @Override
    public void onLocationSuccess(Location location) {
        mPresenter.getCityMessage(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
    }

    @Override
    public void getCitySuccess(CityMessage cityMessage) {
        LogUtil.e(cityMessage.toString());
        addressCity = cityMessage.getAddressComponent().getCity();
        addressCity = addressCity.substring(0,addressCity.length()-1);
        if (addressCity!=null)
            LogUtil.e(addressCity);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("city",addressCity);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }
}
