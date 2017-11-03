package com.example.finaldesig.ui.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finaldesig.R;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    private List<String> list = new ArrayList<String>();
    private LinearLayout forecastLayout;
    private int flag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            list = bundle.getStringArrayList("content");
            flag = bundle.getInt("flag");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_weather, container,false);
        forecastLayout = (LinearLayout)view.findViewById(R.id.forest_layout);
        initView(view);
        return view;
    }

    public void initView(View view){
    }


    public static WeatherFragment newInstance(List<String> contentList, int flag){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("content", (ArrayList<String>) contentList);
        bundle.putInt("flag", flag);
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }
}
