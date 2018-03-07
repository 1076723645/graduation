package com.example.finaldesign.presenter;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.finaldesign.base.BasePresenter;
import com.example.finaldesign.model.DataManager;
import com.example.finaldesign.model.bean.CityMessage;
import com.example.finaldesign.model.http.ApiException;
import com.example.finaldesign.model.http.CommonSubscriber;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LocationUtils;
import com.example.finaldesign.util.RxUtil;
import com.example.finaldesign.view.SplashView;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

import static android.content.ContentValues.TAG;

/**
 * Created by hui on 2018/3/5.
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private Context mContext;
    private DataManager dataManager;

    public SplashPresenter (Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        dataManager = new DataManager(mContext);
    }

    public void getLocation(){
        addSubscribe(Flowable.create((FlowableOnSubscribe<Location>) emitter -> {
            Location location = LocationUtils.getBestLocation(mContext);
            if (location!=null) {
                emitter.onNext(location);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                             .compose(RxUtil.transformScheduler())
                             .subscribeWith(new CommonSubscriber<Location>(mView) {
                                 @Override
                                 public void onNext(Location location) {
                                     mView.onLocationSuccess(location);
                                 }
                             }));
    }

    public void getCityMessage(String latitude, String longitude){
        addSubscribe(dataManager.getCityMessage(latitude,longitude)
                             .compose(RxUtil.transformScheduler())
                             .compose(RxUtil.handleResult())
                             .subscribeWith(new CommonSubscriber<CityMessage>(mView){
                                 @Override
                                 public void onNext(CityMessage s) {
                                     mView.getCitySuccess(s);
                                 }
                             }));
    }

    public void loadCityCode(){
        addSubscribe(Flowable.create(emitter -> {
            HttpUtil.getJson(mContext);
            emitter.onNext("");
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                             .compose(RxUtil.transformScheduler())
                             .subscribeWith(new CommonSubscriber<Object>(mView) {
                         @Override
                         public void onNext(Object o) {
                             mView.loadSuccess();
                         }
                     }));
    }
}
