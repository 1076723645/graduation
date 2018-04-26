package com.example.finaldesign.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.finaldesign.R;
import com.example.finaldesign.base.SimpleActivity;
import com.example.finaldesign.model.helper.DeleteCityEvent;
import com.example.finaldesign.ui.adapter.Frag2Adapter;
import com.example.finaldesign.ui.fragment.WeatherFragment2;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.SharePreferencesUtils;
import com.example.finaldesign.util.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Main2Activity extends SimpleActivity{

    @BindView(R.id.city_viewpager)
    ViewPager viewPager;
    @BindView(R.id.add_menu)
    ImageView iv_add;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.text_menu)
    TextView mPopuView;
    @BindView(R.id.v_bg)
    View bgView;

    private List<String> contentList = new ArrayList<>();//内容表
    private List<WeatherFragment2> fragmentList = new ArrayList<>(); //碎片表
    private static final String CONTENTLIST = "contentList";
    private Frag2Adapter adapter;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 先判断缓存列表，为空则获取定位城市，定位城市也为空则跳转添加城市
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);//注册EventBus
        String content = SharePreferencesUtils.getString(mContext,CONTENTLIST,"");
        if (!content.equals("")){
            contentList = new ArrayList<>(DataUtil.stringToList(content));
            LogUtil.e(content);
            initView();
        }else {
            String addressCity = getIntent().getStringExtra("cityName");
            if (addressCity == null || addressCity.equals("")){
                startActivityFinish(CitySearchActivity.class);
            }else {
                LogUtil.e(addressCity);
                contentList.add(addressCity);
                SharePreferencesUtils.put(mContext, CONTENTLIST, DataUtil.listToString(contentList));
                showLoading();
                initView();
            }
        }
    }

    @Override
    protected void fitSystem() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @SuppressLint("RestrictedApi")
    private void initView(){
        for(int i=0;i<contentList.size();i++){
            WeatherFragment2 weatherFragment = WeatherFragment2.newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter = new Frag2Adapter(getSupportFragmentManager(), this);
        adapter.setData((ArrayList<WeatherFragment2>) fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        iv_add.setOnClickListener(view -> {
            Intent intent = new Intent(Main2Activity.this,CityManagerActivity.class);
            intent.putStringArrayListExtra("list", (ArrayList<String>) contentList);
            startActivityForResult(intent, 1, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });
        setting.setOnClickListener(view -> showPopupWindow());
    }

    /**
     * 添加城市
     * @param intent 城市名称
     */
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        String cityName = getIntent().getStringExtra("cityName");
        LogUtil.d(cityName);
        for (int i=0; i<contentList.size(); i++){
            if (contentList.get(i).contains(cityName)){
                viewPager.setCurrentItem(i);
                LogUtil.i("城市已经存在");
                return;
            }
        }
        contentList.add(cityName);
        WeatherFragment2 weatherFragment = WeatherFragment2.newInstance(contentList, contentList.size() - 1);
        fragmentList.add(weatherFragment);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(contentList.size()-1);
        weatherFragment.lazyLoad();
        SharePreferencesUtils.put(mContext, CONTENTLIST, DataUtil.listToString(contentList));
    }

    /**
     * 对应城市的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    final int page = data.getIntExtra("data_return",0);
                    viewPager.setCurrentItem(page);
                    WeatherFragment2 weatherFragment = fragmentList.get(page);
                    weatherFragment.fullScroll();
                }
                break;
            default:
        }
    }

    /**
     * 修改源数据后进行更新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteCityEvent event) {
        contentList = event.getMsg();
        fragmentList.clear();
        for(int i=0;i<contentList.size();i++){
            WeatherFragment2 weatherFragment = WeatherFragment2.newInstance(contentList, i);
            fragmentList.add(weatherFragment);
        }
        adapter.notifyDataSetChanged();
        SharePreferencesUtils.put(mContext, CONTENTLIST, DataUtil.listToString(contentList));
    }

    public void dismiss(){
        dismissLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow!=null && popupWindow.isShowing())
            popupWindow.dismiss();
        dismissLoading();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    private void showPopupWindow() {//显示窗口
        View contentView = LayoutInflater.from(this).inflate(R.layout.popuw_setting, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        //在控件上方显示
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.showPopupAnimation);
        popupWindow.showAsDropDown(mPopuView,0,- Utility.dip2px(this, 175));
        bgView.setVisibility(View.VISIBLE);
        TextView setting = contentView.findViewById(R.id.tv_setting);
        setting.setOnClickListener(v -> {
            Intent intent = new Intent(this,SettingActivity.class);
            intent.putStringArrayListExtra("cityList", (ArrayList<String>) contentList);
            startActivity(intent);
            popupWindow.dismiss();
            bgView.setVisibility(View.GONE);
        });
        popupWindow.setOnDismissListener(() -> bgView.setVisibility(View.GONE));
    }
}
