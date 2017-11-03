package com.example.finaldesig.util;

import android.text.TextUtils;


import com.example.finaldesig.db.City;
import com.example.finaldesig.db.CityCode;
import com.example.finaldesig.db.County;
import com.example.finaldesig.db.Province;
import com.example.finaldesig.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Utility {
    //解析本地城市信息
    public static boolean handleCityCodeResponce(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray CityCode = new JSONArray(response);
                for (int i=0;i<CityCode.length();i++){
                    JSONObject citycodeObj = CityCode.getJSONObject(i);
                    CityCode cityCode = new CityCode();
                    cityCode.setCityName(citycodeObj.getString("市名"));
                    cityCode.setCityCode("CN"+citycodeObj.getString("编码"));
                    cityCode.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    //解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponce(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allprovince = new JSONArray(response);
                for (int i=0;i<allprovince.length();i++){
                    JSONObject provinceObj = allprovince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObj.getString("name"));
                    province.setProvinceCode(provinceObj.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    //解析和处理服务器返回的市级数据
    public static boolean handleCityResponce(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject provinceObj = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(provinceObj.getString("name"));
                    city.setCityCode(provinceObj.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    //解析和处理服务器返回的县级数据
    public static boolean handleCountyResponce(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject provinceObj = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(provinceObj.getString("name"));
                    county.setWeatherId(provinceObj.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    //解析和处理天气信息
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
