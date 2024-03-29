package com.techjumper.polyhomeb.mvp.p.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.rx.tools.RxUtils;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.AcHelper;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.corelib.utils.window.DialogUtils;
import com.techjumper.corelib.utils.window.ToastUtils;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.UpdateInfoEntity;
import com.techjumper.polyhomeb.entity.event.ToggleMenuClickEvent;
import com.techjumper.polyhomeb.mvp.m.TabHomeActivityModel;
import com.techjumper.polyhomeb.mvp.v.activity.JSInteractionActivity;
import com.techjumper.polyhomeb.mvp.v.activity.TabHomeActivity;
import com.techjumper.polyhomeb.service.UpdateService;
import com.techjumper.polyhomeb.utils.ServiceUtil;
import com.techjumper.polyhomeb.widget.PolyTab;

import java.util.List;

import rx.Observer;
import rx.Subscription;

import static com.techjumper.polyhomeb.service.UpdateService.KEY_FILE_PATH;
import static com.techjumper.polyhomeb.service.UpdateService.KEY_URL;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class TabHomeActivityPresenter extends AppBaseActivityPresenter<TabHomeActivity>
        implements PolyTab.ITabClick {

    private boolean mCanExit;
    private Subscription mSubs1, mSubs2;

    private TabHomeActivityModel mModel = new TabHomeActivityModel(this);

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onViewInited(Bundle savedInstanceState) {
        checkOldApkIsExists();
        checkVersionInfo();
        requestPermission();
        onToggleMenuClick();
    }

    private void onToggleMenuClick() {
        RxUtils.unsubscribeIfNotNull(mSubs1);
        addSubscription(
                mSubs1 = RxBus.INSTANCE.asObservable()
                        .subscribe(o -> {
                            if (o instanceof ToggleMenuClickEvent) {
                                if (getView().getSlidingMenu() != null) {
                                    getView().toggleMenu();
                                }
                            }
                        }));
    }

    @Override
    public void onTabClick(int index) {
        if (getView().getHomeViewPager().getCurrentItem() == index) return;
        getView().getHomeViewPager().setCurrentItem(index, false);
    }

    public void onBackPressed() {

        if (getView().mSlidingMenu.isMenuShowing()) {
            getView().toggleMenu();
            return;
        }

        if (!mCanExit) {
            ToastUtils.show(getView().getString(R.string.exit_app));
            mCanExit = true;
            new Handler().postDelayed(() -> mCanExit = false, 2000);
            return;
        }

        getView().supportFinishAfterTransition();

    }

    /**
     * 首页广告点击之后跳转商城的逻辑
     *
     * @param url
     */
    public void adJump2Shopping(String url) {
        if (getView() == null || getView().getHomeViewPager() == null) return;
        getView().getHomeViewPager().setCurrentItem(2, false);
        getView().getTab().check(2);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        new AcHelper.Builder(getView()).extra(bundle).target(JSInteractionActivity.class).start();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 16) {
            addSubscription(
                    RxPermissions.getInstance(getView())
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    , Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe()
            );
        } else {
            addSubscription(
                    RxPermissions.getInstance(getView())
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe()
            );
        }
    }

    private void checkVersionInfo() {
        String pkgName = getView().getPackageName();
        String[] name = new String[]{pkgName};
        RxUtils.unsubscribeIfNotNull(mSubs2);
        addSubscription(
                mSubs2 = mModel.checkUpdate(name)
                        .subscribe(new Observer<UpdateInfoEntity>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(UpdateInfoEntity updateInfoEntity) {
                                if (!processNetworkResult(updateInfoEntity)) return;
                                if (updateInfoEntity.getData() != null
                                        && updateInfoEntity.getData().getResult() != null
                                        && updateInfoEntity.getData().getResult().size() != 0) {
                                    List<UpdateInfoEntity.DataBean.ResultBean> result = updateInfoEntity.getData().getResult();
                                    mModel.analyzeVersionCode(result);
                                }
                            }
                        }));
    }

    public void showDialog(String url) {
        DialogUtils.getBuilder(getView())
                .content(R.string.has_new_version)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onAny((dialog, which) -> {
                    switch (which) {
                        case POSITIVE:
                            downloadApk(url);
                            break;
                    }
                })
                .show();
    }

    private void downloadApk(String url) {
        if (TextUtils.isEmpty(url)) return;
        if (url.startsWith("/")) {
            downLoadFromServer(url);
        } else if (url.contains("http") || url.contains("https")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            getView().startActivity(intent);
            JLog.d("启动浏览器下载apk");
        }
    }

    private void downLoadFromServer(String url) {
        addSubscription(
                RxPermissions.getInstance(getView())
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                Intent intent = new Intent(getView(), UpdateService.class);
                                intent.putExtra(KEY_URL, Config.sHost + url);
                                intent.putExtra(KEY_FILE_PATH, Config.sUpdate_Apk_Path);
                                getView().startService(intent);
                                JLog.d("启动服务下载apk---SD卡");
                            } else {
                                Intent intent = new Intent(getView(), UpdateService.class);
                                intent.putExtra(KEY_URL, Config.sHost + url);
                                intent.putExtra(KEY_FILE_PATH, Utils.appContext.getFilesDir().getAbsolutePath());
                                getView().startService(intent);
                                JLog.d("启动服务下载apk---内部存储");
                            }
                        }));
    }

    /**
     * 检查xxx路径下是否有apk更新包，有的话就删除，没有就不管啦
     */
    private void checkOldApkIsExists() {
        if (ServiceUtil.isServiceRunning(getView(), UpdateService.class.getName())) {
            JLog.d("当前正通过服务下载apk，进入app的时候不删除apk");
        } else {
            JLog.d("当前没有通过服务下载apk，进入app的时候检查删除安装包");
            addSubscription(
                    RxPermissions.getInstance(getView())
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    , Manifest.permission.READ_EXTERNAL_STORAGE)
                            .subscribe(granted -> {
                                if (granted) {
                                    FileUtils.deleteFileIfExist(Config.sUpdate_Apk_Path, Config.sAPK_NAME);
                                    FileUtils.deleteFileIfExist(Utils.appContext.getFilesDir().getAbsolutePath()
                                            , Config.sAPK_NAME);
                                    JLog.d("删除SD卡apk和内部存储apk");
                                } else {
                                    FileUtils.deleteFileIfExist(Utils.appContext.getFilesDir().getAbsolutePath()
                                            , Config.sAPK_NAME);
                                    JLog.d("删除内部存储apk");
                                }
                            }));
        }
    }

}
