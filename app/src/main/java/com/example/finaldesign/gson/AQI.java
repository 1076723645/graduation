package com.example.finaldesign.gson;

/**
 * Created by Administrator on 2017/10/26.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
        public String pm10;
        public String co;
        public String no2;
        public String o3;
        public String so2;
        public String qlty;
    }
}
