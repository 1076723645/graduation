package com.example.finaldesign.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.example.finaldesign.App;
import com.example.finaldesign.util.SystemFit;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hui on 2017/8/11.
 */

public abstract class SimpleActivity extends AppCompatActivity {

    protected Context mContext;
    private Unbinder mUnbind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemFit.fitSystemWhite(this);
        setContentView(getLayoutId());
        mUnbind = ButterKnife.bind(this);
        mContext = this;
        onViewCreated();
        App.getInstance().addActivity(this);
        initData();
    }

    protected void onViewCreated() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().removeActivity(this);
        mUnbind.unbind();
    }

    protected abstract int getLayoutId();
    protected abstract void initData();


    protected void startActivity(Class<?> cls) {
        startActivity(getIntent(cls));
    }

    protected void startActivityFinish(Class<?> cls) {
        startActivity(getIntent(cls));
        finish();
    }

    protected Intent getIntent(Class<?> cls) {
        return new Intent(mContext, cls);
    }

}
