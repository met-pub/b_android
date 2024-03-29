package com.techjumper.polyhomeb.mvp.m;

import android.util.Base64;

import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.entity.medicalEntity.BaseArgumentsMedicalEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.MedicalUserLoginEntity;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalChangeLoginAccountActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalChangeLoginAccountActivityModel extends BaseModel<MedicalChangeLoginAccountActivityPresenter> {

    public MedicalChangeLoginAccountActivityModel(MedicalChangeLoginAccountActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<Result<MedicalUserLoginEntity>> medicalUserLogin(String username, String password) {
        KeyValuePair keyValuePair = KeyValueCreator.medicalUserLogin(1, 1);
        BaseArgumentsMedicalEntity medicalUserLoginArguments
                = NetHelper.createMedicalUserLoginArguments(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createMedicalConnection()
                .medicalUserLogin(encode(username, password), medicalUserLoginArguments)
                .compose(CommonWrap.wrap());
    }

    private String encode(String username, String password) {
        String encoded = Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        String authorization = "Basic" + " " + encoded;
        return authorization;
    }
}
