package com.example.finaldesign.presenter;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Administrator on 2017/12/7.
 */

public class YolandaItemTouchHelper extends ItemTouchHelper {

    Callback callback;
    public YolandaItemTouchHelper(Callback callback) {
        super(callback);
        this.callback = callback;

    }

    public Callback getCallback() {
        return callback;
    }
}