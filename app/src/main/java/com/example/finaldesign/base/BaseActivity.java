package com.example.finaldesign.base;





/**
 * Created by hui on 2018/2/27.
 */

public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView{

    protected T mPresenter;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.onCreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onStop();
    }
    protected abstract void initPresenter();
}