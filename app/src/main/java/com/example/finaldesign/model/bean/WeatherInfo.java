package com.example.finaldesign.model.bean;

import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by hui on 2018/3/9.
 */

public class WeatherInfo{

    private List<HeWeatherBean> HeWeather;

    public static WeatherInfo objectFromData(String str) {

        return new Gson().fromJson(str, WeatherInfo.class);
    }

    public List<HeWeatherBean> getHeWeather() {
        return HeWeather;
    }

    public void setHeWeather(List<HeWeatherBean> HeWeather) {
        this.HeWeather = HeWeather;
    }

    public static class HeWeatherBean {
        /**
         * aqi : {"city":{"aqi":"110","qlty":"轻度污染","pm25":"83","pm10":"105","no2":"29","so2":"11","co":"0.96","o3":"112"}}
         * basic : {"city":"杭州","cnty":"中国","id":"CN101210101","lat":"30.28745842","lon":"120.15357971","update":{"loc":"2018-03-09 16:47","utc":"2018-03-09 08:47"}}
         * daily_forecast : [{"astro":{"mr":"00:09","ms":"11:04","sr":"06:16","ss":"18:03"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2018-03-09","hum":"44","pcpn":"0.0","pop":"0","pres":"1028","tmp":{"max":"12","min":"2"},"uv":"6","vis":"16","wind":{"deg":"7","dir":"北风","sc":"1-2","spd":"6"}},{"astro":{"mr":"01:02","ms":"11:47","sr":"06:15","ss":"18:04"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2018-03-10","hum":"43","pcpn":"0.0","pop":"0","pres":"1026","tmp":{"max":"18","min":"3"},"uv":"7","vis":"20","wind":{"deg":"182","dir":"南风","sc":"1-2","spd":"8"}},{"astro":{"mr":"01:53","ms":"12:33","sr":"06:14","ss":"18:05"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2018-03-11","hum":"49","pcpn":"0.0","pop":"0","pres":"1024","tmp":{"max":"21","min":"10"},"uv":"7","vis":"20","wind":{"deg":"105","dir":"东南风","sc":"1-2","spd":"4"}}]
         * hourly_forecast : [{"cond":{"code":"100","txt":"晴"},"date":"2018-03-09 19:00","hum":"55","pop":"0","pres":"1027","tmp":"8","wind":{"deg":"353","dir":"北风","sc":"1-2","spd":"5"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-09 22:00","hum":"65","pop":"0","pres":"1028","tmp":"4","wind":{"deg":"331","dir":"西北风","sc":"1-2","spd":"4"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-10 01:00","hum":"60","pop":"0","pres":"1027","tmp":"4","wind":{"deg":"222","dir":"西南风","sc":"1-2","spd":"4"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-10 04:00","hum":"65","pop":"0","pres":"1027","tmp":"2","wind":{"deg":"186","dir":"南风","sc":"1-2","spd":"5"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-10 07:00","hum":"87","pop":"0","pres":"1027","tmp":"3","wind":{"deg":"174","dir":"南风","sc":"1-2","spd":"6"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-10 10:00","hum":"53","pop":"0","pres":"1027","tmp":"10","wind":{"deg":"152","dir":"东南风","sc":"1-2","spd":"6"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-10 13:00","hum":"38","pop":"0","pres":"1025","tmp":"16","wind":{"deg":"136","dir":"东南风","sc":"1-2","spd":"6"}},{"cond":{"code":"100","txt":"晴"},"date":"2018-03-10 16:00","hum":"42","pop":"0","pres":"1023","tmp":"15","wind":{"deg":"106","dir":"东南风","sc":"1-2","spd":"7"}}]
         * now : {"cond":{"code":"100","txt":"晴"},"fl":"6","hum":"35","pcpn":"0.0","pres":"1026","tmp":"12","vis":"10","wind":{"deg":"187","dir":"南风","sc":"1-2","spd":"7"}}
         * status : ok
         * suggestion : {"air":{"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"},"comf":{"brf":"较舒适","txt":"白天虽然天气晴好，但早晚会感觉偏凉，午后舒适、宜人。"},"cw":{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},"drsg":{"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},"flu":{"brf":"较易发","txt":"天凉，昼夜温差较大，较易发生感冒，请适当增减衣服，体质较弱的朋友请注意适当防护。"},"sport":{"brf":"较适宜","txt":"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。"},"trav":{"brf":"适宜","txt":"天气较好，温度适宜，是个好天气哦。这样的天气适宜旅游，您可以尽情地享受大自然的风光。"},"uv":{"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}}
         */

        private AqiBean aqi;
        private BasicBean basic;
        private NowBean now;
        private String status;
        private SuggestionBean suggestion;
        private List<DailyForecastBean> daily_forecast;
        private List<HourlyForecastBean> hourly_forecast;

        public static HeWeatherBean objectFromData(String str) {

            return new Gson().fromJson(str, HeWeatherBean.class);
        }

        public AqiBean getAqi() {
            return aqi;
        }

        public void setAqi(AqiBean aqi) {
            this.aqi = aqi;
        }

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public SuggestionBean getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(SuggestionBean suggestion) {
            this.suggestion = suggestion;
        }

        public List<DailyForecastBean> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public List<HourlyForecastBean> getHourly_forecast() {
            return hourly_forecast;
        }

        public void setHourly_forecast(List<HourlyForecastBean> hourly_forecast) {
            this.hourly_forecast = hourly_forecast;
        }

        public static class AqiBean {
            /**
             * city : {"aqi":"110","qlty":"轻度污染","pm25":"83","pm10":"105","no2":"29","so2":"11","co":"0.96","o3":"112"}
             */

            private CityBean city;

            public static AqiBean objectFromData(String str) {

                return new Gson().fromJson(str, AqiBean.class);
            }

            public CityBean getCity() {
                return city;
            }

            public void setCity(CityBean city) {
                this.city = city;
            }

            public static class CityBean {
                /**
                 * aqi : 110
                 * qlty : 轻度污染
                 * pm25 : 83
                 * pm10 : 105
                 * no2 : 29
                 * so2 : 11
                 * co : 0.96
                 * o3 : 112
                 */

                private String aqi;
                private String qlty;
                private String pm25;
                private String pm10;
                private String no2;
                private String so2;
                private String co;
                private String o3;

                public static CityBean objectFromData(String str) {

                    return new Gson().fromJson(str, CityBean.class);
                }

                public String getAqi() {
                    return aqi;
                }

                public void setAqi(String aqi) {
                    this.aqi = aqi;
                }

                public String getQlty() {
                    return qlty;
                }

                public void setQlty(String qlty) {
                    this.qlty = qlty;
                }

                public String getPm25() {
                    return pm25;
                }

                public void setPm25(String pm25) {
                    this.pm25 = pm25;
                }

                public String getPm10() {
                    return pm10;
                }

                public void setPm10(String pm10) {
                    this.pm10 = pm10;
                }

                public String getNo2() {
                    return no2;
                }

                public void setNo2(String no2) {
                    this.no2 = no2;
                }

                public String getSo2() {
                    return so2;
                }

                public void setSo2(String so2) {
                    this.so2 = so2;
                }

                public String getCo() {
                    return co;
                }

                public void setCo(String co) {
                    this.co = co;
                }

                public String getO3() {
                    return o3;
                }

                public void setO3(String o3) {
                    this.o3 = o3;
                }
            }
        }

        public static class BasicBean {
            /**
             * city : 杭州
             * cnty : 中国
             * id : CN101210101
             * lat : 30.28745842
             * lon : 120.15357971
             * update : {"loc":"2018-03-09 16:47","utc":"2018-03-09 08:47"}
             */

            private String city;
            private String cnty;
            private String id;
            private String lat;
            private String lon;
            private UpdateBean update;

            public static BasicBean objectFromData(String str) {

                return new Gson().fromJson(str, BasicBean.class);
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public UpdateBean getUpdate() {
                return update;
            }

            public void setUpdate(UpdateBean update) {
                this.update = update;
            }

            public static class UpdateBean {
                /**
                 * loc : 2018-03-09 16:47
                 * utc : 2018-03-09 08:47
                 */

                private String loc;
                private String utc;

                public static UpdateBean objectFromData(String str) {

                    return new Gson().fromJson(str, UpdateBean.class);
                }

                public String getLoc() {
                    return loc;
                }

                public void setLoc(String loc) {
                    this.loc = loc;
                }

                public String getUtc() {
                    return utc;
                }

                public void setUtc(String utc) {
                    this.utc = utc;
                }
            }
        }

        public static class NowBean {
            /**
             * cond : {"code":"100","txt":"晴"}
             * fl : 6
             * hum : 35
             * pcpn : 0.0
             * pres : 1026
             * tmp : 12
             * vis : 10
             * wind : {"deg":"187","dir":"南风","sc":"1-2","spd":"7"}
             */

            private CondBean cond;
            private String fl;
            private String hum;
            private String pcpn;
            private String pres;
            private String tmp;
            private String vis;
            private WindBean wind;

            public static NowBean objectFromData(String str) {

                return new Gson().fromJson(str, NowBean.class);
            }

            public CondBean getCond() {
                return cond;
            }

            public void setCond(CondBean cond) {
                this.cond = cond;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public WindBean getWind() {
                return wind;
            }

            public void setWind(WindBean wind) {
                this.wind = wind;
            }

            public static class CondBean {
                /**
                 * code : 100
                 * txt : 晴
                 */

                private String code;
                private String txt;

                public static CondBean objectFromData(String str) {

                    return new Gson().fromJson(str, CondBean.class);
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class WindBean {
                /**
                 * deg : 187
                 * dir : 南风
                 * sc : 1-2
                 * spd : 7
                 */

                private String deg;
                private String dir;
                private String sc;
                private String spd;

                public static WindBean objectFromData(String str) {

                    return new Gson().fromJson(str, WindBean.class);
                }

                public String getDeg() {
                    return deg;
                }

                public void setDeg(String deg) {
                    this.deg = deg;
                }

                public String getDir() {
                    return dir;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public String getSc() {
                    return sc;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public String getSpd() {
                    return spd;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }
            }
        }

        public static class SuggestionBean {
            /**
             * air : {"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"}
             * comf : {"brf":"较舒适","txt":"白天虽然天气晴好，但早晚会感觉偏凉，午后舒适、宜人。"}
             * cw : {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}
             * drsg : {"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"}
             * flu : {"brf":"较易发","txt":"天凉，昼夜温差较大，较易发生感冒，请适当增减衣服，体质较弱的朋友请注意适当防护。"}
             * sport : {"brf":"较适宜","txt":"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。"}
             * trav : {"brf":"适宜","txt":"天气较好，温度适宜，是个好天气哦。这样的天气适宜旅游，您可以尽情地享受大自然的风光。"}
             * uv : {"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}
             */

            private AirBean air;
            private ComfBean comf;
            private CwBean cw;
            private DrsgBean drsg;
            private FluBean flu;
            private SportBean sport;
            private TravBean trav;
            private UvBean uv;

            public static SuggestionBean objectFromData(String str) {

                return new Gson().fromJson(str, SuggestionBean.class);
            }

            public AirBean getAir() {
                return air;
            }

            public void setAir(AirBean air) {
                this.air = air;
            }

            public ComfBean getComf() {
                return comf;
            }

            public void setComf(ComfBean comf) {
                this.comf = comf;
            }

            public CwBean getCw() {
                return cw;
            }

            public void setCw(CwBean cw) {
                this.cw = cw;
            }

            public DrsgBean getDrsg() {
                return drsg;
            }

            public void setDrsg(DrsgBean drsg) {
                this.drsg = drsg;
            }

            public FluBean getFlu() {
                return flu;
            }

            public void setFlu(FluBean flu) {
                this.flu = flu;
            }

            public SportBean getSport() {
                return sport;
            }

            public void setSport(SportBean sport) {
                this.sport = sport;
            }

            public TravBean getTrav() {
                return trav;
            }

            public void setTrav(TravBean trav) {
                this.trav = trav;
            }

            public UvBean getUv() {
                return uv;
            }

            public void setUv(UvBean uv) {
                this.uv = uv;
            }

            public static class AirBean {
                /**
                 * brf : 中
                 * txt : 气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
                 */

                private String brf;
                private String txt;

                public static AirBean objectFromData(String str) {

                    return new Gson().fromJson(str, AirBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class ComfBean {
                /**
                 * brf : 较舒适
                 * txt : 白天虽然天气晴好，但早晚会感觉偏凉，午后舒适、宜人。
                 */

                private String brf;
                private String txt;

                public static ComfBean objectFromData(String str) {

                    return new Gson().fromJson(str, ComfBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class CwBean {
                /**
                 * brf : 较适宜
                 * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
                 */

                private String brf;
                private String txt;

                public static CwBean objectFromData(String str) {

                    return new Gson().fromJson(str, CwBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class DrsgBean {
                /**
                 * brf : 较冷
                 * txt : 建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。
                 */

                private String brf;
                private String txt;

                public static DrsgBean objectFromData(String str) {

                    return new Gson().fromJson(str, DrsgBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class FluBean {
                /**
                 * brf : 较易发
                 * txt : 天凉，昼夜温差较大，较易发生感冒，请适当增减衣服，体质较弱的朋友请注意适当防护。
                 */

                private String brf;
                private String txt;

                public static FluBean objectFromData(String str) {

                    return new Gson().fromJson(str, FluBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class SportBean {
                /**
                 * brf : 较适宜
                 * txt : 天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。
                 */

                private String brf;
                private String txt;

                public static SportBean objectFromData(String str) {

                    return new Gson().fromJson(str, SportBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class TravBean {
                /**
                 * brf : 适宜
                 * txt : 天气较好，温度适宜，是个好天气哦。这样的天气适宜旅游，您可以尽情地享受大自然的风光。
                 */

                private String brf;
                private String txt;

                public static TravBean objectFromData(String str) {

                    return new Gson().fromJson(str, TravBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class UvBean {
                /**
                 * brf : 中等
                 * txt : 属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。
                 */

                private String brf;
                private String txt;

                public static UvBean objectFromData(String str) {

                    return new Gson().fromJson(str, UvBean.class);
                }

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }
        }

        public static class DailyForecastBean {
            /**
             * astro : {"mr":"00:09","ms":"11:04","sr":"06:16","ss":"18:03"}
             * cond : {"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"}
             * date : 2018-03-09
             * hum : 44
             * pcpn : 0.0
             * pop : 0
             * pres : 1028
             * tmp : {"max":"12","min":"2"}
             * uv : 6
             * vis : 16
             * wind : {"deg":"7","dir":"北风","sc":"1-2","spd":"6"}
             */

            private AstroBean astro;
            private CondBeanX cond;
            private String date;
            private String hum;
            private String pcpn;
            private String pop;
            private String pres;
            private TmpBean tmp;
            private String uv;
            private String vis;
            private WindBeanX wind;

            public static DailyForecastBean objectFromData(String str) {

                return new Gson().fromJson(str, DailyForecastBean.class);
            }

            public AstroBean getAstro() {
                return astro;
            }

            public void setAstro(AstroBean astro) {
                this.astro = astro;
            }

            public CondBeanX getCond() {
                return cond;
            }

            public void setCond(CondBeanX cond) {
                this.cond = cond;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public TmpBean getTmp() {
                return tmp;
            }

            public void setTmp(TmpBean tmp) {
                this.tmp = tmp;
            }

            public String getUv() {
                return uv;
            }

            public void setUv(String uv) {
                this.uv = uv;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public WindBeanX getWind() {
                return wind;
            }

            public void setWind(WindBeanX wind) {
                this.wind = wind;
            }

            public static class AstroBean {
                /**
                 * mr : 00:09
                 * ms : 11:04
                 * sr : 06:16
                 * ss : 18:03
                 */

                private String mr;
                private String ms;
                private String sr;
                private String ss;

                public static AstroBean objectFromData(String str) {

                    return new Gson().fromJson(str, AstroBean.class);
                }

                public String getMr() {
                    return mr;
                }

                public void setMr(String mr) {
                    this.mr = mr;
                }

                public String getMs() {
                    return ms;
                }

                public void setMs(String ms) {
                    this.ms = ms;
                }

                public String getSr() {
                    return sr;
                }

                public void setSr(String sr) {
                    this.sr = sr;
                }

                public String getSs() {
                    return ss;
                }

                public void setSs(String ss) {
                    this.ss = ss;
                }
            }

            public static class CondBeanX {
                /**
                 * code_d : 100
                 * code_n : 100
                 * txt_d : 晴
                 * txt_n : 晴
                 */

                private String code_d;
                private String code_n;
                private String txt_d;
                private String txt_n;

                public static CondBeanX objectFromData(String str) {

                    return new Gson().fromJson(str, CondBeanX.class);
                }

                public String getCode_d() {
                    return code_d;
                }

                public void setCode_d(String code_d) {
                    this.code_d = code_d;
                }

                public String getCode_n() {
                    return code_n;
                }

                public void setCode_n(String code_n) {
                    this.code_n = code_n;
                }

                public String getTxt_d() {
                    return txt_d;
                }

                public void setTxt_d(String txt_d) {
                    this.txt_d = txt_d;
                }

                public String getTxt_n() {
                    return txt_n;
                }

                public void setTxt_n(String txt_n) {
                    this.txt_n = txt_n;
                }
            }

            public static class TmpBean {
                /**
                 * max : 12
                 * min : 2
                 */

                private String max;
                private String min;

                public static TmpBean objectFromData(String str) {

                    return new Gson().fromJson(str, TmpBean.class);
                }

                public String getMax() {
                    return max;
                }

                public void setMax(String max) {
                    this.max = max;
                }

                public String getMin() {
                    return min;
                }

                public void setMin(String min) {
                    this.min = min;
                }
            }

            public static class WindBeanX {
                /**
                 * deg : 7
                 * dir : 北风
                 * sc : 1-2
                 * spd : 6
                 */

                private String deg;
                private String dir;
                private String sc;
                private String spd;

                public static WindBeanX objectFromData(String str) {

                    return new Gson().fromJson(str, WindBeanX.class);
                }

                public String getDeg() {
                    return deg;
                }

                public void setDeg(String deg) {
                    this.deg = deg;
                }

                public String getDir() {
                    return dir;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public String getSc() {
                    return sc;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public String getSpd() {
                    return spd;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }
            }
        }

        public static class HourlyForecastBean {
            /**
             * cond : {"code":"100","txt":"晴"}
             * date : 2018-03-09 19:00
             * hum : 55
             * pop : 0
             * pres : 1027
             * tmp : 8
             * wind : {"deg":"353","dir":"北风","sc":"1-2","spd":"5"}
             */

            private CondBeanXX cond;
            private String date;
            private String hum;
            private String pop;
            private String pres;
            private String tmp;
            private WindBeanXX wind;

            public static HourlyForecastBean objectFromData(String str) {

                return new Gson().fromJson(str, HourlyForecastBean.class);
            }

            public CondBeanXX getCond() {
                return cond;
            }

            public void setCond(CondBeanXX cond) {
                this.cond = cond;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public WindBeanXX getWind() {
                return wind;
            }

            public void setWind(WindBeanXX wind) {
                this.wind = wind;
            }

            public static class CondBeanXX {
                /**
                 * code : 100
                 * txt : 晴
                 */

                private String code;
                private String txt;

                public static CondBeanXX objectFromData(String str) {

                    return new Gson().fromJson(str, CondBeanXX.class);
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class WindBeanXX {
                /**
                 * deg : 353
                 * dir : 北风
                 * sc : 1-2
                 * spd : 5
                 */

                private String deg;
                private String dir;
                private String sc;
                private String spd;

                public static WindBeanXX objectFromData(String str) {

                    return new Gson().fromJson(str, WindBeanXX.class);
                }

                public String getDeg() {
                    return deg;
                }

                public void setDeg(String deg) {
                    this.deg = deg;
                }

                public String getDir() {
                    return dir;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public String getSc() {
                    return sc;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public String getSpd() {
                    return spd;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }
            }
        }
    }
}
