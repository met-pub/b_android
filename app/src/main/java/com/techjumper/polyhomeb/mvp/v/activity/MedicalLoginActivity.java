package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.mvp.p.activity.MedicalLoginActivityPresenter;
import com.techjumper.polyhomeb.user.UserManager;

import butterknife.Bind;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(MedicalLoginActivityPresenter.class)
public class MedicalLoginActivity extends AppBaseActivity<MedicalLoginActivityPresenter> {

    @Bind(R.id.et_account)
    EditText mEtAccount;
    @Bind(R.id.et_password)
    EditText mEtPsw;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_medical_login);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        UserManager.INSTANCE.medicalLogout();  //在登录界面清空医疗相关的sp,这样在第二次点击医疗的时候,就不会出现  显示一下main 界面才去登录了
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.medical_login);
    }

    public EditText getEtAccount() {
        return mEtAccount;
    }

    public EditText getEtPsw() {
        return mEtPsw;
    }
}
