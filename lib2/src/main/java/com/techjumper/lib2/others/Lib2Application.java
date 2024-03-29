package com.techjumper.lib2.others;

import com.techjumper.corelib.others.JumperApplication;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.lib2.utils.RxORM;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/2/15
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public abstract class Lib2Application extends JumperApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RxORM.setDefaultDbVersion(getDbVersion());
        Config.sDefaultBaseUrl = getDefaultBaseUrl();
        Config.sMedicalBaseUrl = getMedicalBaseUrl();
        Config.sCAPPBaseUrl = getCAPPBaseUrl();
        RetrofitHelper.sDefaultInterface = getDefaultInterfaceClass();

    }

    protected abstract int getDbVersion();

    protected abstract String getDefaultBaseUrl();

    protected abstract String getMedicalBaseUrl();

    protected abstract String getCAPPBaseUrl();

    protected abstract Class getDefaultInterfaceClass();

}
