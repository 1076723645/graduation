package com.example.finaldesig.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finaldesig.R;
import com.example.finaldesig.gson.Weather;

import java.util.List;


/**
 * Created by Administrator on 2017/11/1.
 */

public class CityManagerAdapter extends RecyclerView.Adapter<CityManagerAdapter.ViewHolder> {

    private List<Weather> mWeatherlist;

    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycle_item, parent, false);
        final ViewHolder holder = new ViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(CityManagerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cond;
        TextView fruitname;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            cond = (ImageView)view.findViewById(R.id.cond);
        }
    }
}
