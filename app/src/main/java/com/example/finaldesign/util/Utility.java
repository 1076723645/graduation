package com.example.finaldesign.util;

import android.content.Context;
import android.text.TextUtils;


import com.example.finaldesign.db.City;
import com.example.finaldesign.db.CityCode;
import com.example.finaldesign.db.County;
import com.example.finaldesign.db.Province;
import com.example.finaldesign.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    //解析定位城市信息
    public static String handleLocationResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject jsonObject1 = results.getJSONObject(0);
            JSONArray address_components = jsonObject1.getJSONArray("address_components");
            JSONObject jsonObject2 = address_components.getJSONObject(4);
            return jsonObject2.getString("long_name");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /*public static boolean handleArea(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            String content = jsonArray.getJSONObject(0).toString();
            List<Area> provinceList = new Gson().fromJson(content,new TypeToken<List<Area>>(){}.getType());
            for (int i=0; i<provinceList.size(); i++){
                SearchCity city = new SearchCity();
                Area area = provinceList.get(i);
                city.setProvince(area.provinceName);
                for (int j=0; j<area.cityList.size(); j++){
                    Area.City shi = area.cityList.get(j);
                    city.setShi(shi.cityName);
                    for (int k=0; k<shi.countyList.size(); k++){
                        city.setXian(shi.countyList.get(k));
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }*/

    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
