package com.techjumper.polyhomeb.mvp.v.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.polyhomeb.Constant;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.interfaces.IWebViewTitleClick;
import com.techjumper.polyhomeb.manager.WebTitleManager;
import com.techjumper.polyhomeb.mvp.p.activity.JSInteractionActivityPresenter;
import com.techjumper.polyhomeb.utils.WebTitleHelper;
import com.techjumper.polyhomeb.widget.AdvancedWebView;
import com.techjumper.polyhomeb.widget.PolyWebView;

import butterknife.Bind;

import static com.techjumper.polyhomeb.utils.WebTitleHelper.NATIVE_METHOD_DELETE_ARTICLE;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/17
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(JSInteractionActivityPresenter.class)
public class JSInteractionActivity extends AppBaseWebViewActivity<JSInteractionActivityPresenter>
        implements IWebViewTitleClick {

    @Bind(R.id.left_first_iv)
    ImageView iv;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_js_interaction);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String url = getPresenter().getUrl();
        new WebTitleManager(url, mViewRoot, this);
        initWebView((AdvancedWebView) findViewById(R.id.wb));
        getWebView().addJsInterface(this, Constant.JS_NATIVE_BRIDGE);
        getWebView().loadUrl(url);
    }

    @Override
    public PolyWebView getWebView() {
        return (PolyWebView) super.getWebView();
    }

    @Override
    protected boolean isWebViewActivity() {
        return true;
    }

    @Override
    public void onTitleLeftFirstClick(String mLeftFirstMethod) {
        switch (mLeftFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mLeftFirstMethod);
                break;
        }
    }

    @Override
    public void onTitleLeftSecondClick(String mLeftSecondMethod) {
        switch (mLeftSecondMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mLeftSecondMethod);
                break;
        }
    }

    @Override
    public void onTitleRightFirstClick(String mRightFirstMethod) {
        switch (mRightFirstMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            case NATIVE_METHOD_DELETE_ARTICLE:
                getPresenter().deleteArticle();
                break;
            default:
                onLineMethod(mRightFirstMethod);
                break;
        }
    }

    @Override
    public void onTitleRightSecondClick(String mRightSecondMethod) {
        switch (mRightSecondMethod) {
            case WebTitleHelper.NATIVE_METHOD_RETURN:
                onBackPressed();
                break;
            case WebTitleHelper.NATIVE_METHOD_MENU:
                RxBus.INSTANCE.send(new ToggleMenuClickEvent());
                break;
            case WebTitleHelper.NATIVE_METHOD_NEW_ARTICLE:
                break;
            default:
                onLineMethod(mRightSecondMethod);
                break;
        }
    }

    /**
     * 根据url传回来的method,调用页面的function
     */
    public void onLineMethod(String method) {
        if (TextUtils.isEmpty(method)) return;
        getWebView().loadUrl("javascript:" + method + "()");
    }

    /**
     * 用于支付之后，通知H5支付结束
     */
    public void refreshH5StateEvent(String order_number) {
        if (TextUtils.isEmpty(order_number)) return;
        getWebView().loadUrl("javascript:" + "refresh_order(" + order_number + ")");
    }
}
