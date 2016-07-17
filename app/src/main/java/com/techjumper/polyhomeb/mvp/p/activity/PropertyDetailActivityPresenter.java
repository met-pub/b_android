package com.techjumper.polyhomeb.mvp.p.activity;

import android.os.Bundle;

import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.mvp.m.PropertyDetailActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyDetailActivityPresenter extends AppBaseActivityPresenter<PropertyDetailActivity> {

    private PropertyDetailActivityModel mModel = new PropertyDetailActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {

    }

    public int comeFromWitchButton() {
        return mModel.comeFromWitchButton();
    }

    public void onTitleRightClick() {
        ToastUtils.show("在第" + getView().getViewPager().getCurrentItem() + "页点击了＋号");
    }
}