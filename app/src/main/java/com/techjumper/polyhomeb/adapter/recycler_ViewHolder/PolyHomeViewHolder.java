package com.techjumper.polyhomeb.adapter.recycler_ViewHolder;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.steve.creact.annotation.DataBean;
import com.steve.creact.library.viewholder.BaseRecyclerViewHolder;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.PolyHomeData;
import com.techjumper.polyhomeb.mvp.v.activity.CheckInActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalLoginActivity;
import com.techjumper.polyhomeb.mvp.v.activity.MedicalMainActivity;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.widget.PolyModeView;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@DataBean(beanName = "PolyHomeDataBean", data = PolyHomeData.class)
public class PolyHomeViewHolder extends BaseRecyclerViewHolder<PolyHomeData> {

    public static final int LAYOUT_ID = R.layout.item_home_page_polyhome;

    public PolyHomeViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(PolyHomeData data) {
        if (data == null) return;

        ((PolyModeView) getView(R.id.hengwen)).setText(data.getSceneName1());
        ((PolyModeView) getView(R.id.anfang)).setText(data.getSceneName2());
        ((PolyModeView) getView(R.id.test3)).setText(data.getSceneName3());
        ((PolyModeView) getView(R.id.test4)).setText(data.getSceneName4());

        setOnClickListener(R.id.layout_check_in, v -> new AcHelper.Builder((Activity) getContext()).target(CheckInActivity.class).start());

        //如果已经登陆过医疗部分,那么直接去医疗首页,否则去医疗登录页面
        setOnClickListener(R.id.layout_medical, v -> {
            String medicalUserId = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_ID);
            String medicalUserToken = UserManager.INSTANCE.getUserInfo(UserManager.KEY_MEDICAL_CURRENT_USER_TOKEN);
            if (!TextUtils.isEmpty(medicalUserId) && !TextUtils.isEmpty(medicalUserToken)) {
                new AcHelper.Builder((Activity) getContext()).target(MedicalMainActivity.class).start();
            } else {
                new AcHelper.Builder((Activity) getContext()).target(MedicalLoginActivity.class).start();
            }
        });
    }

}