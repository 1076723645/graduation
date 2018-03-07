package com.example.finaldesign.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by hui on 2018/3/1.
 */

public abstract class BasePresenter<T extends BaseView> implements Presenter<T> {

    private CompositeDisposable mCompositeDisposable;
    protected T mView;

    private void unSubscribe() {
        if (mCompositeDisposable!=null)
        mCompositeDisposable.clear();
    }

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void onStop() {
        mView = null;
        unSubscribe();
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }
}
