package com.webstore.webstore.entity;

/**
 * Created by LAP10572-local on 7/13/2016.
 */
public class DeviceInfo {
    private String deviceId;
    private String simSerialNo;
    public DeviceInfo(String deviceId,String simSerialNo){
        this.setDeviceId(deviceId);
        this.setSimSerialNo(simSerialNo);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSimSerialNo() {
        return simSerialNo;
    }

    public void setSimSerialNo(String simSerialNo) {
        this.simSerialNo = simSerialNo;
    }
}
