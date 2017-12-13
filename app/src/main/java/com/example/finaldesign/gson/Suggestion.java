package com.example.finaldesign.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Suggestion {

    @SerializedName("drsg")
    public Comfot comfot;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    @SerializedName("uv")
    public Ultraviolet ultraviolet;

    public class Comfot{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String msg;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String msg;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String msg;
    }

    public class Ultraviolet{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String msg;
    }

}

