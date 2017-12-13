package com.example.finaldesign.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/28.
 */

public class HourlyForecast {

    @SerializedName("date")
    public String time_data;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }
}
