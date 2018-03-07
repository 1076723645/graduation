package com.example.finaldesign.util;


import android.util.Log;


import com.example.finaldesign.model.http.ApiException;
import com.example.finaldesign.model.http.HttpResponse;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hui on 17/8/21.
 */

public class RxUtil {

    /**
     * 统一线程处理请求
     * @param
     * @return
     */
    public static <T> FlowableTransformer<T, T> transformScheduler() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 统一返回结果处理
     * @param
     * @return
     */
    public static <T> FlowableTransformer<HttpResponse<T>, T> handleResult() {
        return upstream -> upstream.flatMap((Function<HttpResponse<T>, Flowable<T>>) tHttpResponse -> {
            if (tHttpResponse.getStatus()==0 && tHttpResponse.getResult()!=null) {
                return createData(tHttpResponse.getResult());
            } else {
                return Flowable.error(new ApiException(String.valueOf(tHttpResponse.getStatus())));
            }
        });
    }




    /**
     * 生成Flowable
     * @param
     * @return
     */
    public static <T> Flowable<T> createData(final T data) {
        return Flowable.create(e -> {
            try {
                Log.d("api", "subscribe: " + data.toString());
                e.onNext(data);
                e.onComplete();
            } catch (Exception exception) {
                e.onError(exception);
            }
        }, BackpressureStrategy.BUFFER);
    }
}
