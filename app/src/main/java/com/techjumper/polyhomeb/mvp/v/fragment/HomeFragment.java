package com.techjumper.polyhomeb.mvp.v.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.HomePageAdapter;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeFragmentPresenter;
import com.techjumper.polyhomeb.user.UserManager;
import com.techjumper.polyhomeb.widget.HomePtrClassicFrameLayout;
import com.techjumper.ptr_lib.PtrDefaultHandler;
import com.techjumper.ptr_lib.PtrFrameLayout;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/6/30
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(HomeFragmentPresenter.class)
public class HomeFragment extends AppBaseFragment<HomeFragmentPresenter> {

    @Bind(R.id.rv)
    RecyclerViewFinal mRv;
    @Bind(R.id.ptr)
    HomePtrClassicFrameLayout mPtr;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.right_tv)
    TextView mTvRight;

    private HomePageAdapter mAdapter;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mTvRight.setVisibility(View.VISIBLE);
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new HomePageAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());
        mAdapter.setClickListener(new HomePageAdapter.IListener() {
            @Override
            public void onSmartHomeClick() {
                getPresenter().onSmartHomeClick();
            }
        });
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new Handler().postDelayed(() -> stopRefresh(""), 3000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    @Override
    public String getTitle() {
        return UserManager.INSTANCE.getCurrentTitle();
    }

    private void stopRefresh(String msg) {
        if (mPtr != null && mPtr.isRefreshing()) {
            if (!TextUtils.isEmpty(msg))
                showHint(msg);
            mPtr.refreshComplete();
        }
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public HomePageAdapter getAdapter() {
        return mAdapter;
    }
}
