package com.example.finaldesign.presenter;

/**
 * Created by Administrator on 2017/12/7.
 */

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

public class FixNPopWindow extends PopupWindow {


    @Override
    public void showAsDropDown(View anchor,int x,int y) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor,x,y);
    }
}
