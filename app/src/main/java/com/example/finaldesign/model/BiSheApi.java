package com.example.finaldesign.model;

import com.example.finaldesign.model.bean.CityMessage;
import com.example.finaldesign.model.http.HttpResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hui on 2018/3/5.
 */

public interface BiSheApi {

    /**
     * 根据定位结果获取城市信息
     */
    @GET("geocoder/v2/")
    Flowable<HttpResponse<CityMessage>> getCityMessage(@Query("location") String it, @Query("output") String output, @Query("ak") String ak, @Query("mcode") String code);

}
