package com.example.finaldesign.db;

import org.litepal.crud.DataSupport;

/**
 * Created by hui on 2017/10/30.
 */

public class CityCode extends DataSupport {

    private int id;
    private String cityName;
    private String cityCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
