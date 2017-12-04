package com.example.finaldesig.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.finaldesig.R;

public class CitySearchActivity extends AppCompatActivity {

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        initView();
        initListener();
    }
    private void initView(){
        test = (TextView) findViewById(R.id.tv_test);
    }

    private void initListener(){
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CitySearchActivity.this,MainActivity.class);
                intent.putExtra("address","北京");
                startActivity(intent);
                finish();
            }
        });
    }
}
