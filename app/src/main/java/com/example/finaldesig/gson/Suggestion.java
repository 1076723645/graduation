package com.example.finaldesig.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfot comfot;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfot{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }

}

