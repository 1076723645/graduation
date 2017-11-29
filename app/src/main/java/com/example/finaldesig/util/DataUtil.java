package com.example.finaldesig.util;

import com.example.finaldesig.R;
import com.example.finaldesig.presenter.WeatherBean;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/11/6.
 */

public class DataUtil {

     String[] weathers = WeatherBean.getAllWeathers();//SUN, RAIN, CLOUDY, SUN_CLOUD, SNOW, THUNDER
    /**
     * <pre>
     * 根据指定的日期字符串获取星期几
     * </pre>
     *
     * @param strDate 指定的日期字符串(yyyy-MM-dd 或 yyyy/MM/dd)
     * @return week
     *         星期几(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY)
     */
    public static String getWeekByDateStr(String strDate) {
        int year = Integer.parseInt(strDate.substring(0, 4));
        int month = Integer.parseInt(strDate.substring(5, 7));
        int day = Integer.parseInt(strDate.substring(8, 10));

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        String week = "";
        int weekIndex = c.get(Calendar.DAY_OF_WEEK)-1;

        switch (weekIndex)
        {
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
            case 7:
                week = "周日";
                break;
        }
        return week;
    }
    public static int getWeatherBg(String s){

        if (s.equals("晴")){
            return R.drawable.bg_sunny;
        }else if (s.contains("雨")){
            return R.drawable.bg_ice_rain;
        }else if (s.contains("多云"))
            return R.drawable.bg_cloudy;
        return R.drawable.bg_sunny_night;
    }
    public static int getWeatherPng(String s){

        if (s.equals("晴")){
            return R.drawable.daily_forecast_sunny;
        }else if (s.equals("雨夹雪")){
            return R.drawable.daily_forecast_ice_rain;
        }else if (s.contains("多云")) {
            return R.drawable.daily_forecast_cloudy;
        }else if (s.equals("阴")){
            return R.drawable.daily_forecast_overcast;
        }else if (s.contains("雨")){
            return R.drawable.daily_forecast_rain;
        }else if (s.equals("雾")){
            return R.drawable.daily_forecast_foggy;
        }
        return R.drawable.daily_forecast_cloudy;
    }
    public static String getWeather(String s){
        String[] weathers = WeatherBean.getAllWeathers();//SUN, RAIN, CLOUDY, SUN_CLOUD, SNOW, THUNDER
        if (s.equals("晴")){
            return weathers[0];
        }else if (s.contains("雨")){
            return weathers[1];
        }else if (s.contains("阴")){
            return weathers[2];
        }else if (s.contains("雪")) {
            return weathers[4];
        }
        else if (s.contains("雷")) {
            return weathers[5];
        }
        return weathers[3];
    }
}
