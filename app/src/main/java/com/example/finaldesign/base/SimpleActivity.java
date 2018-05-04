package com.example.finaldesign.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;


import com.example.finaldesign.App;
import com.example.finaldesign.R;
import com.example.finaldesign.util.SystemFit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by hui on 2017/8/11.
 */

public abstract class SimpleActivity extends SupportActivity {

    protected Context mContext;
    private Unbinder mUnbind;
    protected Dialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fitSystem();
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
    protected void fitSystem(){}

    protected void startActivity(Class<?> cls) {
        startActivity(getIntent(cls));
    }

    protected void startActivityFinish(Class<?> cls) {
        startActivity(getIntent(cls));
        finish();
    }

    protected void showLoading(){
        if (loadingDialog==null){
            loadingDialog = new Dialog(mContext, R.style.CustomDialog);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        loadingDialog.setContentView(view);
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setCancelable(true);
        loadingDialog.show();
    }

    protected void dismissLoading(){
        if (loadingDialog!=null)
            loadingDialog.dismiss();
    }

    protected Intent getIntent(Class<?> cls) {
        return new Intent(mContext, cls);
    }

}
