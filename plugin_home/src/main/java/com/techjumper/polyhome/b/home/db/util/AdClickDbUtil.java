package com.techjumper.polyhome.b.home.db.util;

import android.util.Log;

import com.techjumper.commonres.entity.AdClickEntity;
import com.techjumper.commonres.util.CommonDateUtil;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhome.b.home.UserInfoManager;
import com.techjumper.polyhome.b.home.db.AdClickDbExecutor;
import com.techjumper.polyhome.b.home.sql.AdClick;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by kevin on 16/8/3.
 */

public class AdClickDbUtil {
    private static AdClickDbExecutor.BriteDatabaseHelper helper;
    private static String timeString;

    public static void insert(long adId, String position, long time) {
        helper = AdClickDbExecutor.getHelper();
        if (time == 0L) {
            timeString = CommonDateUtil.getCurrentTime();
            Log.d("submitOnline", "获取系统时间");
        } else {
            timeString = CommonDateUtil.getCurrentTime(time);
            Log.d("submitOnline", "获取心跳时间" + timeString);
        }
        helper.insert(adId, UserInfoManager.getLongFamilyId(), timeString, position)
                .flatMap(aLong -> helper.query(String.valueOf(aLong)))
                .subscribe(adClick -> {
                    if (adClick == null) {
                        Log.d("adclick", "查找ID为" + adId + "的广告失败");
                        return;
                    }
                    Log.d("adclick", "插入成功 id=" + adId + " familyid=" + adClick.family_id() + " time=" + adClick.time() + " position=" + adClick.position());
                });
    }

    public static boolean clear() {
        helper = AdClickDbExecutor.getHelper();
        return helper.deleteAll();
    }

    public static Observable<List<AdClick>> queryAll() {
        helper = AdClickDbExecutor.getHelper();
        return helper.queryAll()
                .filter(adClicks -> {
                    if (adClicks != null && adClicks.size() > 0) {
                        Log.d("adclick", "查询所有 有数据" + adClicks);
                        long time = CommonDateUtil.differHour(adClicks.get(0).time());
                        Log.d("adclick", "相差多少小时" + time);
                        if (time >= 0) {
                            return true;
                        }
                        return false;
                    } else {
                        Log.d("adclick", "查询所有 没数据");
                        return false;
                    }
                });
    }

    public static String createParamJson(List<AdClick> adClicks) {
        if (adClicks == null || adClicks.isEmpty())
            return "";

        AdClickEntity entity = new AdClickEntity();
        List<AdClickEntity.AdClickItemEntity> entities = new ArrayList<>();


        for (int i = 0; i < adClicks.size(); i++) {
            AdClick adClick = adClicks.get(i);
            AdClickEntity.AdClickItemEntity adClickItemEntity = new AdClickEntity.AdClickItemEntity();
            adClickItemEntity.setAd_id(String.valueOf(adClick.ad_id()));
            adClickItemEntity.setFamily_id(String.valueOf(adClick.family_id()));
            adClickItemEntity.setTime(String.valueOf(adClick.time()));
            adClickItemEntity.setPosition(adClick.position());
            entities.add(adClickItemEntity);
        }

        entity.setClicks(entities);

        return GsonUtils.toJson(entity);
    }
}
