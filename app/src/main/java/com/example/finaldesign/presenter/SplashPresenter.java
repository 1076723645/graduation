package com.example.finaldesign.presenter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.finaldesign.base.BasePresenter;
import com.example.finaldesign.model.DataManager;
import com.example.finaldesign.model.bean.CityMessage;
import com.example.finaldesign.model.http.ApiException;
import com.example.finaldesign.model.http.CommonSubscriber;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LocationUtil;
import com.example.finaldesign.util.LocationUtils;
import com.example.finaldesign.util.RxUtil;
import com.example.finaldesign.view.SplashView;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
            Location location = LocationUtils.getNetWorkLocation(mContext);
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

    public void newLocation(){
        Boolean location = LocationUtil.register(mContext, 0, 0, new LocationUtil.OnLocationChangeListener() {
            @Override
            public void getLastKnownLocation(Location location) {
                mView.onLocationSuccess(location);
            }

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
        if (!location){
            mView.showErrorMsg("");
        }
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

    public void isChinese(String s){
        char[] ch = s.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                mView.getChinese(s);
                return;
            }
        }
        mView.showErrorMsg("notChinese");
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
