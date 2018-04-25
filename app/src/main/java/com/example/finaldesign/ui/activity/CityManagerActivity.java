package com.example.finaldesign.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.finaldesign.R;
import com.example.finaldesign.base.SimpleActivity;
import com.example.finaldesign.ui.adapter.CityManagerAdapter;
import com.example.finaldesign.util.CircularAnimUtil;
import com.example.finaldesign.model.helper.DiffCallBack;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.SharePreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CityManagerActivity extends SimpleActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private List<String> contentList = new ArrayList<>();
    private static final String ACTIVITY_TAG="CityManagerActivity";
    private static final String CONTENTLIST = "contentList";
    private CityManagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_managre;
    }

    @Override
    protected void initData() {
        contentList = getIntent().getStringArrayListExtra("list");
        LogUtil.d(ACTIVITY_TAG, contentList.toString());
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        adapter = new CityManagerAdapter(contentList, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CityManagerActivity.this, CitySearchActivity.class);
            CircularAnimUtil.startActivity(CityManagerActivity.this, intent, fab, R.color.colorPrimary);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @SuppressLint("RestrictedApi")
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                Intent intent = new Intent(CityManagerActivity.this,CityDeleteActivity.class);
                intent.putStringArrayListExtra("list", (ArrayList<String>) contentList);
                startActivityForResult(intent,2,ActivityOptions.makeSceneTransitionAnimation(CityManagerActivity.this).toBundle());
                break;
            case android.R.id.home:
                if (contentList.size() == 0){
                    Intent intent1 = new Intent(CityManagerActivity.this, CitySearchActivity.class);
                    startActivity(intent1);
                    finish();
                }else {
                    onBackPressed();
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
