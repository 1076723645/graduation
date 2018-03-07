package com.example.finaldesign.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hui on 2018/2/27.
 */

public abstract class BaseFragment<T extends BasePresenter> extends SupportFragment{

    protected T mPresenter;
    protected View mView;
    protected Context mContext;
    private Unbinder mUnbind;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        mUnbind = ButterKnife.bind(this, view);
        mPresenter.onCreate();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbind.unbind();
        mPresenter.onStop();
    }

    protected abstract int getLayoutId();
    protected abstract void initData();
    protected abstract void initPresenter();
}
