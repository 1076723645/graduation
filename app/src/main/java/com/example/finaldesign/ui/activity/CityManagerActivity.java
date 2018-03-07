package com.example.finaldesign.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.finaldesign.R;
import com.example.finaldesign.util.CircularAnimUtil;
import com.example.finaldesign.model.helper.DiffCallBack;
import com.example.finaldesign.ui.adapter.CityManagerAdapter;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity {

    private List<String> contentList = new ArrayList<>();
    private static final String ACTIVITY_TAG="CityManagerActivity";
    private static final String CONTENTLIST = "contentList";
    private CityManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
            Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(explode);
            getWindow().setReenterTransition(explode);
            getWindow().setReturnTransition(fade);
        }
        setContentView(R.layout.activity_city_managre);
        contentList =  getIntent().getStringArrayListExtra("list");
        LogUtil.d(ACTIVITY_TAG,contentList.toString());
        initView();
    }


    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new CityManagerAdapter(contentList,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityManagerActivity.this,CitySearchActivity.class);
                CircularAnimUtil.startActivity(CityManagerActivity.this,intent,fab,R.color.colorPrimary);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                Intent intent = new Intent(CityManagerActivity.this,CityDeleteActivity.class);
                intent.putStringArrayListExtra("list", (ArrayList<String>) contentList);
                startActivityForResult(intent,2,ActivityOptions.makeSceneTransitionAnimation(CityManagerActivity.this).toBundle());
                break;
            case android.R.id.home:
                if (contentList.size() == 0){
                    Intent intent1 = new Intent(CityManagerActivity.this,CitySearchActivity.class);
                    startActivity(intent1);
                    finish();
                }else {
                    finish();
                }
                break;
            default:
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if (resultCode == RESULT_OK){
                    final List<String> returnList = data.getStringArrayListExtra("return");
                    LogUtil.e(ACTIVITY_TAG,returnList.toString());
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(contentList, returnList), true);
                    // 把结果应用到 adapter
                    diffResult.dispatchUpdatesTo(adapter);
                    contentList = returnList;
                    adapter.setData(contentList);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(CityManagerActivity.this).edit();
                    editor.putString(CONTENTLIST, DataUtil.listToString(contentList));
                    editor.apply();
                }
                break;
            default:
        }
    }
}
