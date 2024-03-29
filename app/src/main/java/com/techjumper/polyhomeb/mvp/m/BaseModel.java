package com.techjumper.polyhomeb.mvp.m;

import com.techjumper.corelib.mvp.interfaces.IPresenter;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/18
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class BaseModel<T extends IPresenter> {

    private T mPresenter;

    public BaseModel(T presenter) {
        this.mPresenter = presenter;
    }

    public T getPresenter() {
        return mPresenter;
    }
}

