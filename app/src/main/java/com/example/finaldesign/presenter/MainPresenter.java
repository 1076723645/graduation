package com.example.finaldesign.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.finaldesign.base.BasePresenter;
import com.example.finaldesign.gson.Weather;
import com.example.finaldesign.model.DataManager;
import com.example.finaldesign.model.bean.WeatherInfo;
import com.example.finaldesign.model.http.CommonSubscriber;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.RxUtil;
import com.example.finaldesign.util.Utility;
import com.example.finaldesign.view.MainView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hui on 2018/3/9.
 */

public class MainPresenter extends BasePresenter<MainView> {

    private Context mContext;
    private DataManager dataManager;

    public MainPresenter (Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        dataManager = new DataManager(mContext);
    }

    public void getWeatherMessage(String cityName){
        addSubscribe(dataManager.getWeatherMessage(cityName)
                             .compose(RxUtil.transformScheduler())
                             .subscribeWith(new CommonSubscriber<WeatherInfo>(mView) {
                                 @Override
                                 public void onNext(WeatherInfo weather) {
                                     if (weather.getHeWeather5().get(0).getStatus().equals("ok"))
                                         mView.loadWeatherInfoSuccess(weather);
                                     else {
                                         mView.showErrorMsg(weather.getHeWeather5().get(0).getStatus());
                                     }
                                 }
                             }));
    }
}
