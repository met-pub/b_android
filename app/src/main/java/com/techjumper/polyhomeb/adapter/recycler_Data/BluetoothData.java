package com.techjumper.polyhomeb.adapter.recycler_Data;

import com.techjumper.polyhomeb.entity.BluetoothLockDoorInfoEntity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/20
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class BluetoothData {

    private List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> getInfosBeen() {
        return infosBeen;
    }

    public void setInfosBeen(List<BluetoothLockDoorInfoEntity.DataBean.InfosBean> infosBeen) {
        this.infosBeen = infosBeen;
    }
}
