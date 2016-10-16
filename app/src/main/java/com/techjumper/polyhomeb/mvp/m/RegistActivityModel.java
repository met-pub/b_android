package com.techjumper.polyhomeb.mvp.m;

import android.os.Bundle;
import android.text.TextUtils;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.corelib.utils.system.AppUtils;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.LoginEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.mvp.p.activity.LoginActivityPresenter;
import com.techjumper.polyhomeb.mvp.p.activity.RegistActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;

import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class RegistActivityModel extends BaseModel<RegistActivityPresenter> {

    public static final String TYPE_REGISTER = "1";

    public RegistActivityModel(RegistActivityPresenter presenter) {
        super(presenter);
    }

    public String getPhoneNumber() {
        String extraPhoneNumber = getExtraPhoneNumber();
        return TextUtils.isEmpty(extraPhoneNumber) ? AppUtils.getLine1Number() : extraPhoneNumber;
    }

    public String getExtraPhoneNumber() {
        Bundle bundle = getPresenter().getView().getIntent().getExtras();
        if (bundle == null) return "";
        return bundle.getString(LoginActivityPresenter.KEY_PHONE_NUMBER, "");
    }

    public Observable<TrueEntity> sendVerificationCode(String mobile) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .sendVerificationCode(NetHelper.createBaseArgumentsMap(
                        KeyValueCreator.sendVerificationCode(mobile, TYPE_REGISTER)))
                .compose(CommonWrap.wrap());
    }

    public Observable<LoginEntity> regist(String mobile, String sms_captcha, String password) {
        return RetrofitHelper.<ServiceAPI>createDefault()
                .regist(NetHelper.createBaseArguments(
                        KeyValueCreator.regist(mobile, sms_captcha, password)))
                .compose(CommonWrap.wrap());
    }
}