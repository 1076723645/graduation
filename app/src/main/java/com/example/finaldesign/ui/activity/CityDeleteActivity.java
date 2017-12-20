package com.example.finaldesign.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.finaldesign.R;
import com.example.finaldesign.presenter.Bean.DeleteCityEvent;
import com.example.finaldesign.presenter.DefaultItemTouchHelpCallback;
import com.example.finaldesign.presenter.DefaultItemTouchHelper;
import com.example.finaldesign.ui.adapter.CityDeleteAdapter;
import com.example.finaldesign.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CityDeleteActivity extends AppCompatActivity {

    private List<String> contentList = new ArrayList<>();
    private static final String ACTIVITY_TAG="CityDeleteActivity";
    private RecyclerView recyclerView;
    private CityDeleteAdapter adapter;
    private Boolean isChanged;
    private Button cancel,sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
        getWindow().setReenterTransition(fade);
        getWindow().setReturnTransition(fade);
        setContentView(R.layout.activity_city_delete);
        contentList =  getIntent().getStringArrayListExtra("list");
        initDate();
        initView();
        initListener();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_city);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new CityDeleteAdapter(contentList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DefaultItemTouchHelper itemTouchHelper = new DefaultItemTouchHelper(onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);
        cancel = (Button) findViewById(R.id.btn_cancel);
        sure = (Button) findViewById(R.id.btn_sure);
    }

    private void initDate(){
        if (contentList.size()!=0){
            isChanged = false;
            LogUtil.e(ACTIVITY_TAG,contentList.toString());
        }
    }

    private void initListener(){
        sure.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (isChanged) {
                    EventBus.getDefault().post(new DeleteCityEvent(contentList));
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("return", (ArrayList<String>) contentList);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener = new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
        @Override

        public void onSwiped(int adapterPosition) {
            // 滑动删除的时候，从数据源移除，并刷新这个Item。
            if (contentList != null) {
                contentList.remove(adapterPosition);
                isChanged = true;
                adapter.notifyItemRemoved(adapterPosition);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if (contentList != null) {
                // 更换数据源中的数据Item的位置
                Collections.swap(contentList, srcPosition, targetPosition);
                // 更新UI中的Item的位置，主要是给用户看到交互效果
                adapter.notifyItemMoved(srcPosition, targetPosition);
                isChanged = true;
                return true;
            }
            return false;
        }
    };
}
