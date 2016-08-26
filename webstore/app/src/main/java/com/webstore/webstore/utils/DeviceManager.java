package com.webstore.webstore.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.webstore.webstore.entity.DeviceInfo;

/**
 * Created by LAP10572-local on 7/13/2016.
 */
public class DeviceManager {
    public static DeviceInfo getDeviceInfo(Context context){
        String deviceId,simSerialNo;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // device Id
        deviceId = telephonyManager.getDeviceId();
        // serial number
        simSerialNo = telephonyManager.getSimSerialNumber();
        DeviceInfo dv = new DeviceInfo(deviceId, simSerialNo);
        return dv;

    }
}
