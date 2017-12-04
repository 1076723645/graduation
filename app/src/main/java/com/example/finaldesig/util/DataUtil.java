package com.example.finaldesig.util;

import android.content.Context;

import com.example.finaldesig.R;
import com.example.finaldesig.db.CityCode;
import com.example.finaldesig.gson.HourlyForecast;
import com.example.finaldesig.gson.Weather;
import com.example.finaldesig.presenter.WeatherBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2017/11/6.
 */

public class DataUtil {

    /**
     * <pre>
     * 根据指定的日期字符串获取星期几
     * </pre>
     *
     * @param strDate 指定的日期字符串(yyyy-MM-dd 或 yyyy/MM/dd)
     * @return week
     * 星期几(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY)
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
        int weekIndex = c.get(Calendar.DAY_OF_WEEK) - 1;

        switch (weekIndex) {
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
            case 0:
                week = "周日";
                break;
        }
        return week;
    }

    public static int getWeatherBg(String s) {

        if (s.equals("晴")) {
            return R.drawable.bg_sunny;
        } else if (s.contains("雨")) {
            return R.drawable.bg_ice_rain;
        } else if (s.contains("多云"))
            return R.drawable.bg_cloudy;
        return R.drawable.bg_sunny_night;
    }

    public static int getWeatherPng(String s) {

        if (s.equals("晴")) {
            return R.drawable.daily_forecast_sunny;
        } else if (s.equals("雨夹雪")) {
            return R.drawable.daily_forecast_ice_rain;
        } else if (s.contains("多云")) {
            return R.drawable.daily_forecast_cloudy;
        } else if (s.equals("阴")) {
            return R.drawable.daily_forecast_overcast;
        } else if (s.contains("雨")) {
            return R.drawable.daily_forecast_rain;
        } else if (s.equals("雾")) {
            return R.drawable.daily_forecast_foggy;
        }
        return R.drawable.daily_forecast_cloudy;
    }

    public static String getWeather(String s) {
        String[] weathers = WeatherBean.getAllWeathers();//SUN, RAIN, CLOUDY, SUN_CLOUD, SNOW, THUNDER
        if (s.equals("晴")) {
            return weathers[0];
        } else if (s.contains("雨")) {
            return weathers[1];
        } else if (s.contains("阴")) {
            return weathers[2];
        } else if (s.contains("雪")) {
            return weathers[4];
        } else if (s.contains("雷")) {
            return weathers[5];
        }
        return weathers[3];
    }

    public static int getWeatherColourPng(String s) {
        if (s.equals("晴")) {
            return R.drawable.icon_sunny;
        } else if (s.contains("雨")) {
            return R.drawable.icon_rain;
        } else if (s.contains("阴")) {
            return R.drawable.icon_overcast;
        } else if (s.contains("雪")) {
            return R.drawable.icon_snow;
        } else if (s.contains("雷")) {
            return R.drawable.icon_t_storm;
        }
        return R.drawable.icon_sun_cloudy;
    }

    public static List<WeatherBean> getHourData(Weather weather) {//获取24小时天气预报
        List<WeatherBean> data = new ArrayList<>();
        HourlyForecast nowList = weather.hourlyForecast.get(0);
        int temp = Integer.parseInt(nowList.temperature);
        String info = DataUtil.getWeather(nowList.more.info);
        WeatherBean now = new WeatherBean(info, temp + 1, "现在");
        data.add(now);
        for (int i = 0; i < weather.hourlyForecast.size(); i++) {
            HourlyForecast hourly = weather.hourlyForecast.get(i);
            temp = Integer.parseInt(hourly.temperature);
            String time = hourly.time_data.substring(hourly.time_data.length() - 5, hourly.time_data.length() - 3);
            info = DataUtil.getWeather(hourly.more.info);
            Random random = new Random();
            for (int k = 0; k < 3; k++) {
                int intTime = Integer.parseInt(time);
                String finalTime = String.valueOf(intTime + k) + ":00";
                int rand = random.nextInt(2); //随机温度
                WeatherBean weatherBean = new WeatherBean(info, temp + rand, finalTime);
                data.add(weatherBean);
            }
        }
        return data;
    }

    public static String getAddressCityCode(String address){
        List<CityCode> cityCodes = DataSupport.where("cityName=?", address).find(CityCode.class);
        return cityCodes.get(0).getCityCode();
    }

    public static List<String> stringToList(String strs){
        String str[] = strs.split(",");
        return Arrays.asList(str);
    }

    public static String listToString(List<String> list){
        if(list==null){
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        //第一个前面不拼接","
        for(String string :list) {
            if(first) {
                first=false;
            }else{
                result.append(",");
            }
            result.append(string);
        }
        return result.toString();
    }
}
