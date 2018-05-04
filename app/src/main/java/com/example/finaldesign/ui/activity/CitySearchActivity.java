package com.example.finaldesign.ui.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finaldesign.App;
import com.example.finaldesign.R;
import com.example.finaldesign.db.City;
import com.example.finaldesign.db.County;
import com.example.finaldesign.db.Province;
import com.example.finaldesign.util.DataUtil;
import com.example.finaldesign.util.HttpUtil;
import com.example.finaldesign.util.LogUtil;
import com.example.finaldesign.util.NetUtils;
import com.example.finaldesign.util.SharePreferencesUtils;
import com.example.finaldesign.util.ToastUtil;
import com.example.finaldesign.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CitySearchActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private List<String> contentList = new ArrayList<>();

    private int currentLevel;//当前选中的级别

    private Province selectedProvince;//选中的省
    private City selectedCity;
    private ProgressDialog progressDialog;
    private TextView noNetwork;
    private TextView test;
    private ArrayAdapter<String> adapter;
    private ImageView backButton;
    private ListView listView;
    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
        getWindow().setReenterTransition(fade);
        getWindow().setReturnTransition(fade);
        setContentView(R.layout.activity_city_search);
        initView();
        initListener();
    }
    private void initView(){
        String content = SharePreferencesUtils.getString(this , "contentList", "");
        if (!content.equals("")) {
            contentList = new ArrayList<>(DataUtil.stringToList(content));
        }
        backButton = findViewById(R.id.iv_back);
        test = findViewById(R.id.tv_name);
        noNetwork = findViewById(R.id.tv_no_network);
        listView = findViewById(R.id.list);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        if (!NetUtils.isConnected(this)){
            noNetwork.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            test.setText("添加城市");
        }
        else {
            queryProvinces();
        }
    }

    private void initListener(){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (currentLevel == LEVEL_PROVINCE){
                selectedProvince = provinceList.get(position);
                queryCities();
            }else if (currentLevel == LEVEL_CITY){
                selectedCity = cityList.get(position);
                queryCounties();
            }else if (currentLevel == LEVEL_COUNTY){
                String cityName = countyList.get(position).getCountyName();
                if (cityName.equals("乌鲁木齐牧试站"))
                    cityName = "乌鲁木齐县";
                Intent intent = new Intent(CitySearchActivity.this, Main2Activity.class);
                intent.putExtra("cityName", cityName);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CitySearchActivity.this).toBundle());
                finish();
            }
        });
        noNetwork.setOnClickListener(v -> {
            if (NetUtils.isConnected(this)){
                noNetwork.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                queryProvinces();
            }else {
                ToastUtil.shortShow("没有网络，请稍后再试");
            }
        });
        backButton.setOnClickListener(v -> {
            if (currentLevel == LEVEL_COUNTY){
                queryCities();
            }else if (currentLevel == LEVEL_CITY){
                queryProvinces();
            }else {
                if (contentList == null || contentList.size() == 0){
                    App.getInstance().exitApp();
                }else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && contentList.size() == 0)) {
            App.getInstance().exitApp();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void queryProvinces(){
        test.setText("中国");
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province :provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities(){
        test.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid=?",
                                     String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    private void queryCounties(){
        test.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?",
                                       String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size()>0){
            dataList.clear();
            for (County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    closeProgressDialog();
                    LogUtil.d("error","加载失败");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;

                if ("province".equals(type)){
                    result = Utility.handleProvinceResponce(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponce(responseText, selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponce(responseText,selectedCity.getId());
                }
                if (result){
                    runOnUiThread(() -> {
                        closeProgressDialog();
                        if ("province".equals(type)){
                            queryProvinces();
                        }else if ("city".equals(type)){
                            queryCities();
                        }else if ("county".equals(type)){
                            queryCounties();
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
