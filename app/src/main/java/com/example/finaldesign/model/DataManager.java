package com.example.finaldesign.model;

import android.content.Context;

import com.example.finaldesign.model.bean.CityMessage;
import com.example.finaldesign.model.http.HttpModule;
import com.example.finaldesign.model.http.HttpResponse;

import io.reactivex.Flowable;


/**
 * Created by Administrator on 2017/10/17.
 */

public class DataManager {
    private BiSheApi mBiSheApi;
    public DataManager(Context context){
        this.mBiSheApi = HttpModule.getInstance(context).getServer();
    }

   /* public DataManager(Context context,String baseUrl){
        this.mBiSheApi = HttpModule.getInstance(context,baseUrl).getServer();
    }*/

    public Flowable<HttpResponse<CityMessage>> getCityMessage(String latitude, String longitude){
        String it = latitude + "," + longitude;
        return mBiSheApi.getCityMessage(it,"json", "EYxgumHxHBG8PekTiM9OncYwZ3rxU8QX","64:28:4D:24:0B:5C:28:21:EF:7B:1E:F3:66:51:E7:72:5D:59:9B:FD;com.example.finaldesign");
    }
}
