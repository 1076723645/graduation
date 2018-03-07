package com.example.finaldesign.util;


import android.widget.Toast;

import com.example.finaldesign.App;


/**
 * Created by hui on 2017/8/4.
 */
public class ToastUtil {

    private static Toast toast;

    public static void shortShow(String msg) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public void toast(String s) {
        Toast.makeText(App.getInstance().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
