package com.example.finaldesign.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;



/**
 * Created by hui on 2018/2/27.
 */

public abstract class BaseFragment<T extends Presenter> extends SimpleFragment implements BaseView{

    protected T mPresenter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initPresenter();
        mPresenter.onCreate();
        mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onStop();
    }

    protected abstract void initPresenter();
}
