package com.webstore.webstore.core;

import android.content.Context;

import com.webstore.webstore.entity.DeviceInfo;
import com.webstore.webstore.entity.UserInfo;
import com.webstore.webstore.storage.LocalStorageHelper;
import com.webstore.webstore.utils.DeviceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LAP10572-local on 7/13/2016.
 */
public class UserManager {
    public static UserInfo getUserInfo(Context context){
        UserInfo user = new UserInfo();
        String userId = LocalStorageHelper.getFromFile("userId");
        if(userId == null || userId.isEmpty()){
            //send device info to server to request a user id
            DeviceInfo dv = DeviceManager.getDeviceInfo(context);
            JSONObject data = new JSONObject();
            try {
                data.put("deviceId",dv.getDeviceId());
                data.put("simSerialNo",dv.getSimSerialNo());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
}
