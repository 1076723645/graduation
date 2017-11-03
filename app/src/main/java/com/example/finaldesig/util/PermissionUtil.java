package com.example.finaldesig.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/2.
 */

public class PermissionUtil {

    public static boolean requestPermission(final Activity activity) {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }
        return true;
    }
    public static void onRequestPermissionsResult(final Activity activity,int requestCode,String[] permissions
            , int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0) {
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(activity, "需要同意所有权限", Toast.LENGTH_SHORT).show();
                          //没有同意的逻辑
                        }
                    }
                    //同意后的逻辑
                }else {
                    Toast.makeText(activity, "未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
