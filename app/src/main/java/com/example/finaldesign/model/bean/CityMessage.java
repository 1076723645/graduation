package com.example.finaldesign.model.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by hui on 2018/3/5.
 */

public class CityMessage {

    /**
     * location : {"lng":116.32899999999994,"lat":39.93400007551505}
     * formatted_address : 北京市海淀区增光路35-6号
     * business : 航天桥,甘家口,车公庄
     * addressComponent : {"country":"中国","country_code":0,"country_code_iso":"CHN","country_code_iso2":"CN","province":"北京市","city":"北京市","city_level":2,"district":"海淀区","town":"","adcode":"110108","street":"增光路","street_number":"35-6号","direction":"附近","distance":"13"}
     * pois : []
     * roads : []
     * poiRegions : []
     * sematic_description : 中国建设银行(增光路支行)附近8米
     * cityCode : 131
     */

    private LocationBean location;
    private String formatted_address;
    private String business;
    private AddressComponentBean addressComponent;
    private String sematic_description;
    private int cityCode;
    private List<?> pois;
    private List<?> roads;
    private List<?> poiRegions;

    public static CityMessage objectFromData(String str) {

        return new Gson().fromJson(str, CityMessage.class);
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public AddressComponentBean getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(AddressComponentBean addressComponent) {
        this.addressComponent = addressComponent;
    }

    public String getSematic_description() {
        return sematic_description;
    }

    public void setSematic_description(String sematic_description) {
        this.sematic_description = sematic_description;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public List<?> getPois() {
        return pois;
    }

    public void setPois(List<?> pois) {
        this.pois = pois;
    }

    public List<?> getRoads() {
        return roads;
    }

    public void setRoads(List<?> roads) {
        this.roads = roads;
    }

    public List<?> getPoiRegions() {
        return poiRegions;
    }

    public void setPoiRegions(List<?> poiRegions) {
        this.poiRegions = poiRegions;
    }

    public static class LocationBean {
        /**
         * lng : 116.32899999999994
         * lat : 39.93400007551505
         */

        private double lng;
        private double lat;

        public static LocationBean objectFromData(String str) {

            return new Gson().fromJson(str, LocationBean.class);
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class AddressComponentBean {
        /**
         * country : 中国
         * country_code : 0
         * country_code_iso : CHN
         * country_code_iso2 : CN
         * province : 北京市
         * city : 北京市
         * city_level : 2
         * district : 海淀区
         * town :
         * adcode : 110108
         * street : 增光路
         * street_number : 35-6号
         * direction : 附近
         * distance : 13
         */

        private String country;
        private int country_code;
        private String country_code_iso;
        private String country_code_iso2;
        private String province;
        private String city;
        private int city_level;
        private String district;
        private String town;
        private String adcode;
        private String street;
        private String street_number;
        private String direction;
        private String distance;

        public static AddressComponentBean objectFromData(String str) {

            return new Gson().fromJson(str, AddressComponentBean.class);
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getCountry_code() {
            return country_code;
        }

        public void setCountry_code(int country_code) {
            this.country_code = country_code;
        }

        public String getCountry_code_iso() {
            return country_code_iso;
        }

        public void setCountry_code_iso(String country_code_iso) {
            this.country_code_iso = country_code_iso;
        }

        public String getCountry_code_iso2() {
            return country_code_iso2;
        }

        public void setCountry_code_iso2(String country_code_iso2) {
            this.country_code_iso2 = country_code_iso2;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getCity_level() {
            return city_level;
        }

        public void setCity_level(int city_level) {
            this.city_level = city_level;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreet_number() {
            return street_number;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}
