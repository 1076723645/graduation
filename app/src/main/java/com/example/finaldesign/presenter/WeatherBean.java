package com.example.finaldesign.presenter;

/**
 * Created by Administrator on 2017/11/28.
 */

public class WeatherBean {

    static final String SUN = "晴";
    static final String CLOUDY = "阴";
    static final String SNOW = "雪";
    static final String RAIN = "雨";
    static final String SUN_CLOUD = "多云";
    static final String THUNDER = "雷";

    public String weather;  //天气，取值为上面6种
    int temperature; //温度值
    String temperatureStr; //温度的描述值
    String time; //时间值

    public WeatherBean(String weather, int temperature, String time) {
        this.weather = weather;
        this.temperature = temperature;
        this.time = time;
        this.temperatureStr = temperature + "°";
    }

    public WeatherBean(String weather, int temperature, String temperatureStr, String time) {
        this.weather = weather;
        this.temperature = temperature;
        this.temperatureStr = temperatureStr;
        this.time = time;
    }

    public static String[] getAllWeathers() {
        String[] str = {SUN, RAIN, CLOUDY, SUN_CLOUD, SNOW, THUNDER};
        return str;
    }
}