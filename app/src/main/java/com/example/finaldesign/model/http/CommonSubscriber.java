package com.example.finaldesign.model.http;

import com.example.finaldesign.base.BaseView;
import com.example.finaldesign.util.LogUtil;

import io.reactivex.subscribers.ResourceSubscriber;


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
            LogUtil.e("onError: "+t.getMessage());
            mView.showErrorMsg("服务器开小差了");
        }
    }


}
