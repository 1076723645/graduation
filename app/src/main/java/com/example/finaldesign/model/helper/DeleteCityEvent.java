package com.example.finaldesign.model.helper;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class DeleteCityEvent {

    private List<String> mMsg;

    public DeleteCityEvent(List<String> msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }
    public List<String> getMsg(){
        return mMsg;
    }
}
