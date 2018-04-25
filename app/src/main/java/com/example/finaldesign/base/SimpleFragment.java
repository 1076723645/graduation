package com.example.finaldesign.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by hui on 17/8/28.
 */

public abstract class SimpleFragment extends SupportFragment {
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnbind;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbind = ButterKnife.bind(this, view);
        initData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        lazyLoad();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbind.unbind();
    }

    protected abstract int getLayoutId();
    protected abstract void initData();
    protected abstract void lazyLoad();
}
