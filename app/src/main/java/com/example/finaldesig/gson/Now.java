package com.example.finaldesig.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("hum")
    public String humidity;

    @SerializedName("fl")
    public String bodytemp;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("wind")
    public WindMore windMore;

    public class WindMore{
        @SerializedName("dir")
        public String where;

        @SerializedName("sc")
        public String how;
    }
}
