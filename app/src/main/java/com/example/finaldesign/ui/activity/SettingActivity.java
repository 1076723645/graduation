package com.example.finaldesign.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finaldesign.R;
import com.example.finaldesign.ui.adapter.CityWarnAdapter;
import com.suke.widget.SwitchButton;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private final String LOG = "SettingActivity";
    private List<TextView> mTextViewList = new ArrayList<>();
    private List<View> mViewList = new ArrayList<>();
    private ImageView backButton;
    private CityWarnAdapter adapter;
    private RecyclerView recyclerView;
    private SwitchButton switchButton;
    private List<String> cityList = new ArrayList<>();

    public SettingActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cityList = getIntent().getStringArrayListExtra("cityList");
        initView();
    }

    private void initView(){
        mTextViewList.add((TextView) findViewById(R.id.t));
        mTextViewList.add((TextView) findViewById(R.id.tt));
        mTextViewList.add((TextView) findViewById(R.id.textView5));
        mTextViewList.add((TextView) findViewById(R.id.tv_night));
        mTextViewList.add((TextView) findViewById(R.id.tv_1));
        mTextViewList.add((TextView) findViewById(R.id.tv_2));
        mTextViewList.add((TextView) findViewById(R.id.tv_3));
        mTextViewList.add((TextView) findViewById(R.id.tv_4));

        mViewList.add(findViewById(R.id.v1));
        mViewList.add(findViewById(R.id.v2));
        mViewList.add(findViewById(R.id.v3));
        mViewList.add(findViewById(R.id.v4));
        mViewList.add(findViewById(R.id.v5));
        mViewList.add(findViewById(R.id.v6));
        mViewList.add(findViewById(R.id.v7));
        mViewList.add(findViewById(R.id.v8));
        mViewList.add(findViewById(R.id.v9));
        mViewList.add(findViewById(R.id.v10));
        mViewList.add(findViewById(R.id.v11));
        mViewList.add(findViewById(R.id.v12));
        mViewList.add(findViewById(R.id.v13));
        mViewList.add(findViewById(R.id.v14));

        backButton = (ImageView) findViewById(R.id.iv_back);
        recyclerView= (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new CityWarnAdapter(cityList,this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        switchButton = (SwitchButton) findViewById(R.id.sb_night);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                changeTheme();
            }
        });
    }

    private void changeTheme() {
        showAnimation();
        toggleThemeSetting();
        refreshUI();
    }

    private void refreshUI() {
        TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        TypedValue lineColor = new TypedValue();//线颜色
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.TextColor, textColor, true);
        theme.resolveAttribute(R.attr.Background, background, true);
        theme.resolveAttribute(R.attr.Background, lineColor, true);
        LinearLayout layout = (LinearLayout) findViewById(R.id.ll);
        layout.setBackgroundResource(background.resourceId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(background.resourceId);
        for (TextView textView : mTextViewList) {
            textView.setTextColor(getResources().getColor(textColor.resourceId));
        }
        for (View view : mViewList) {
            view.setBackgroundResource(lineColor.resourceId);
        }
        int childCount = recyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) recyclerView.getChildAt(childIndex);
            childView.setBackgroundResource(background.resourceId);
            TextView nickName = (TextView) childView.findViewById(R.id.tv_name);
            nickName.setTextColor(getResources().getColor(textColor.resourceId));
            View motto = childView.findViewById(R.id.line);
            motto.setBackgroundResource(background.resourceId);
        }
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(recyclerView));
            RecyclerView.RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        refreshStatusBar();
    }

    private void refreshStatusBar() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
    }

    /**
     * 切换主题设置
     */
    private void toggleThemeSetting() {
        setTheme(R.style.NightTheme);
    }

    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                            ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
}
