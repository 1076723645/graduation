package com.example.finaldesign.model.http;

import android.content.Context;

import com.example.finaldesign.model.BiSheApi;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hui on 2018/3/1.
 */

public class HttpModule {
    private Context mCntext;
    private static String BASE_URL = "http://guolin.tech/";
    private OkHttpClient client = createOkHttp();

    private GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static HttpModule instance = null;
    private Retrofit mRetrofit = null;
    public static HttpModule getInstance(Context context){
        if (instance == null){
            instance = new HttpModule(context);
        }
        return instance;
    }

    private HttpModule(Context mContext){
        mCntext = mContext;
        init();
    }

    private OkHttpClient createOkHttp(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new  OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    public BiSheApi getServer(){
        return mRetrofit.create(BiSheApi.class);
    }
}
