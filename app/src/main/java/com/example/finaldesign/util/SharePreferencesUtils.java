package com.example.finaldesign.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.finaldesign.App;


/**
 * Created by hui on 2017/5/23.
 */

public class SharePreferencesUtils {


    private final static String SP_NAME = App.class.getPackage().getName();
    public final static String SP_FIRST = "isFirst";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    //--------------------------Boolean
    public static void putBoolean(Context context, String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    //--------------------------String
    public static void put(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }


}
