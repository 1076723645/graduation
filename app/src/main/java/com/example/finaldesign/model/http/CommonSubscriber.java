package com.example.finaldesign.model.http;


import android.util.Log;

import com.example.finaldesign.base.BaseView;

import io.reactivex.subscribers.ResourceSubscriber;

import static android.content.ContentValues.TAG;

/**
 * Created by ykf on 17/8/23.
 */

public abstract class CommonSubscriber<T> extends ResourceSubscriber<T> {
    private static final String unknown_error = "未知错误";
    private BaseView mView;
    protected CommonSubscriber(BaseView view) {
        mView = view;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable t) {
        if (mView == null) return;
        if (t instanceof ApiException) {
            mView.showErrorMsg(t.getMessage());
        } else {
            Log.d(TAG, "onError: "+t.getMessage());
            mView.showErrorMsg(t.getMessage());
        }
    }


}
