package com.example.finaldesig.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Forecast {

    @SerializedName("date")
    public String time_data;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }
    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
