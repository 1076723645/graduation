package com.example.finaldesign.view;

import com.example.finaldesign.base.BaseView;
import com.example.finaldesign.model.bean.WeatherInfo;

/**
 * Created by hui on 2018/3/9.
 */

public interface MainView extends BaseView {
    void loadWeatherInfoSuccess(WeatherInfo weather);
}
