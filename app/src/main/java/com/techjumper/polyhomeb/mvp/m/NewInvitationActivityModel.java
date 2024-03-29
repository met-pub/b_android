package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.rx.tools.CommonWrap;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.RetrofitHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewInvitationPhotoViewChoosedData;
import com.techjumper.polyhomeb.adapter.recycler_Data.NewInvitationPhotoViewDefaultPlusData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewInvitationPhotoViewChoosedBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.NewInvitationPhotoViewDefaultPlusBean;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.SectionsEntity;
import com.techjumper.polyhomeb.entity.TrueEntity;
import com.techjumper.polyhomeb.entity.UploadPicEntity;
import com.techjumper.polyhomeb.mvp.p.activity.NewInvitationActivityPresenter;
import com.techjumper.polyhomeb.net.KeyValueCreator;
import com.techjumper.polyhomeb.net.NetHelper;
import com.techjumper.polyhomeb.net.ServiceAPI;
import com.techjumper.polyhomeb.user.UserManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NewInvitationActivityModel extends BaseModel<NewInvitationActivityPresenter> {

    public NewInvitationActivityModel(NewInvitationActivityPresenter presenter) {
        super(presenter);
    }

    public Observable<SectionsEntity> getSections() {
        KeyValuePair keyValuePair = KeyValueCreator.getSections(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)
                , UserManager.INSTANCE.getTicket());
        Map<String, String> baseArgumentsMap = NetHelper.createBaseArgumentsMap(keyValuePair);
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .getSections(baseArgumentsMap)
                .compose(CommonWrap.wrap());
    }

    public List<DisplayBean> getDatas() {

        List<DisplayBean> displayBeen = new ArrayList<>();

        if (0 == getPresenter().getChoosedPhoto().size()) {
            //默认的相机图片
            NewInvitationPhotoViewDefaultPlusData newInvitationPhotoViewDefaultPlusData = new NewInvitationPhotoViewDefaultPlusData();
            newInvitationPhotoViewDefaultPlusData.setImageResource(R.mipmap.icon_camera);
            newInvitationPhotoViewDefaultPlusData.setSelectPhotoes(getPresenter().getChoosedPhoto());
            NewInvitationPhotoViewDefaultPlusBean newInvitationPhotoViewDefaultPlusBean = new NewInvitationPhotoViewDefaultPlusBean(newInvitationPhotoViewDefaultPlusData);
            displayBeen.add(newInvitationPhotoViewDefaultPlusBean);
        } else if (6 == getPresenter().getChoosedPhoto().size()) {
            for (int i = 0; i < 6; i++) {
                NewInvitationPhotoViewChoosedData newInvitationPhotoViewChoosedData = new NewInvitationPhotoViewChoosedData();
                newInvitationPhotoViewChoosedData.setMapPath(getPresenter().getChoosedPhoto().get(i));
                NewInvitationPhotoViewChoosedBean newInvitationPhotoViewChoosedBean = new NewInvitationPhotoViewChoosedBean(newInvitationPhotoViewChoosedData);
                displayBeen.add(newInvitationPhotoViewChoosedBean);
            }
        } else {
            for (int i = 0; i < getPresenter().getChoosedPhoto().size(); i++) {
                NewInvitationPhotoViewChoosedData newInvitationPhotoViewChoosedData = new NewInvitationPhotoViewChoosedData();
                newInvitationPhotoViewChoosedData.setMapPath(getPresenter().getChoosedPhoto().get(i));
                NewInvitationPhotoViewChoosedBean newInvitationPhotoViewChoosedBean = new NewInvitationPhotoViewChoosedBean(newInvitationPhotoViewChoosedData);
                displayBeen.add(newInvitationPhotoViewChoosedBean);
            }
            //默认的"加号"图片
            NewInvitationPhotoViewDefaultPlusData newInvitationPhotoViewDefaultPlusData = new NewInvitationPhotoViewDefaultPlusData();
            newInvitationPhotoViewDefaultPlusData.setImageResource(R.mipmap.icon_photo_add);
            newInvitationPhotoViewDefaultPlusData.setSelectPhotoes(getPresenter().getChoosedPhoto());
            NewInvitationPhotoViewDefaultPlusBean newInvitationPhotoViewDefaultPlusBean = new NewInvitationPhotoViewDefaultPlusBean(newInvitationPhotoViewDefaultPlusData);
            displayBeen.add(newInvitationPhotoViewDefaultPlusBean);
        }
        return displayBeen;
    }

    public Observable<UploadPicEntity> uploadPic(String filePath) {
//        File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20161108_161909_HDR.jpg");
        File file = new File(filePath);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), new File(filePath)))
                .setType(MultipartBody.FORM)
                .build();
        return RetrofitHelper
                .<ServiceAPI>createDefault()
                .uploadPicFile(multipartBody)
                .compose(CommonWrap.wrap());
    }

    public Observable<TrueEntity> newArticle(String forum_section_id, String title, String content, List<String> urls, String category, String price, String origin_price, String discount) {
        KeyValuePair keyValuePair = KeyValueCreator.newArticle(
                UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID)  //user_id
                , UserManager.INSTANCE.getTicket()   //ticket
                , getVillageId() //小区ID.小区,包含小区id,家庭,也包含小区id.所以这里直接用
                , forum_section_id  //板块ID
                , title         //标题
                , content       //内容
                , list2Array(urls)//图片数组
                , category     //闲置--非闲置
                , price        //现价
                , origin_price  //原价
                , discount);  //是否接受讲价
        BaseArgumentsEntity entity = NetHelper.createBaseArguments(keyValuePair);
        return RetrofitHelper.<ServiceAPI>createDefault()
                .newArticle(entity)
                .compose(CommonWrap.wrap());
    }

    private String[] list2Array(List<String> mUrls) {
        if (mUrls.size() == 0) return new String[0];
        String[] arrays = new String[mUrls.size()];
        for (int i = 0; i < mUrls.size(); i++) {
            arrays[i] = mUrls.get(i);
        }
        return arrays;
    }

    private String getVillageId() {
        if (UserManager.INSTANCE.isFamily()) {
            return UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_VILLAGE_ID);
        } else {
            return UserManager.INSTANCE.getCurrentId();
        }
    }

}
