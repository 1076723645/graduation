package com.example.finaldesign.view;

import android.location.Location;

import com.example.finaldesign.base.BaseView;
import com.example.finaldesign.model.bean.CityMessage;

/**
 * Created by hui on 2018/3/5.
 */

public interface SplashView extends BaseView {
    void onLocationSuccess(Location location);
    //void loadSuccess();
    void getCitySuccess(CityMessage cityMessage);
}
