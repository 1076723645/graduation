package com.example.finaldesign.base;



public interface Presenter<T extends BaseView>{

    void onCreate();

    //void onStart();//暂时没用到

    void onStop();

    //void pause();//暂时没用到

    void attachView(T view);

    //void attachIncomingIntent(Intent intent);//暂时没用到
}