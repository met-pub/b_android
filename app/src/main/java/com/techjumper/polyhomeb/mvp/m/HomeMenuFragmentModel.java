package com.techjumper.polyhomeb.mvp.m;

import com.steve.creact.library.display.DisplayBean;
import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.recycler_Data.HomeMenuItemData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyPlacardDividerLongData;
import com.techjumper.polyhomeb.adapter.recycler_Data.PropertyRepairBigDividerData;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.HomeMenuItemBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyPlacardDividerLongBean;
import com.techjumper.polyhomeb.adapter.recycler_ViewHolder.databean.PropertyRepairBigDividerBean;
import com.techjumper.polyhomeb.mvp.p.fragment.HomeMenuFragmentPresenter;
import com.techjumper.polyhomeb.user.UserManager;

import java.util.ArrayList;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuFragmentModel extends BaseModel<HomeMenuFragmentPresenter> {

    public HomeMenuFragmentModel(HomeMenuFragmentPresenter presenter) {
        super(presenter);
    }

    public List<DisplayBean> getDatas() {
        List<DisplayBean> displayBeen = new ArrayList<>();

        //细长分割线
        PropertyPlacardDividerLongData propertyPlacardDividerLongData = new PropertyPlacardDividerLongData();
        PropertyPlacardDividerLongBean propertyPlacardDividerLongBean = new PropertyPlacardDividerLongBean(propertyPlacardDividerLongData);

        //短分割线
        PropertyPlacardDividerData propertyPlacardDividerData = new PropertyPlacardDividerData();
        propertyPlacardDividerData.setMarginLeft(RuleUtils.dp2Px(14));//和布局中文字的marginLeft相同
        PropertyPlacardDividerBean propertyPlacardDividerBean = new PropertyPlacardDividerBean(propertyPlacardDividerData);

        String rightText = UserManager.INSTANCE.getCurrentTitle();
        displayBeen.add(propertyPlacardDividerLongBean);//细长分割线
        displayBeen.add(newItem(Utils.appContext.getString(R.string.my_village_or_family), rightText, HomeMenuItemData.ItemType.FAMILY));
        displayBeen.add(propertyPlacardDividerBean);//短一些分割线
        //如果是处于家庭状态下，并且当前登录的用户是当前选中的这个家庭的管理员的话，才有权限进去管理  什么的.
//        if (UserManager.INSTANCE.isFamily()
//                && UserManager.INSTANCE.getUserInfo(UserManager.KEY_CURRENT_FAMILY_MANAGER_ID)
//                .equalsIgnoreCase(UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID))) {
//            displayBeen.add(newItem(Utils.appContext.getString(R.string.room_manage), "", HomeMenuItemData.ItemType.ROOM_MANAGE));
//            displayBeen.add(propertyPlacardDividerBean);//短一些分割线
//            displayBeen.add(newItem(Utils.appContext.getString(R.string.member_manage), "", HomeMenuItemData.ItemType.MEMBER_MANAGE));
//            displayBeen.add(propertyPlacardDividerBean);//短一些分割线
//        }
        displayBeen.add(newItem(Utils.appContext.getString(R.string.message_center), "", HomeMenuItemData.ItemType.MESSAGE));
        displayBeen.add(propertyPlacardDividerLongBean);//细长分割线

        //大分割线
        PropertyRepairBigDividerData propertyRepairBigDividerData = new PropertyRepairBigDividerData();
        propertyRepairBigDividerData.setColor(R.color.color_eaf1f5);
        PropertyRepairBigDividerBean propertyRepairBigDividerBean = new PropertyRepairBigDividerBean(propertyRepairBigDividerData);
        displayBeen.add(propertyRepairBigDividerBean);

//        //细长分割线
//        displayBeen.add(propertyPlacardDividerLongBean);
//
//        //我的积分
//        displayBeen.add(newItem(Utils.appContext.getString(R.string.my_points), "", HomeMenuItemData.ItemType.POINTS));
//
//        //细的分割线
//        displayBeen.add(propertyPlacardDividerLongBean);

        //大分割线
        displayBeen.add(propertyRepairBigDividerBean);

        //细的分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        //item
        displayBeen.add(newItem(Utils.appContext.getString(R.string.settings), "", HomeMenuItemData.ItemType.SETTING));

        //细长分割线
        displayBeen.add(propertyPlacardDividerLongBean);

        return displayBeen;
    }

    private HomeMenuItemBean newItem(String title, String rightText, HomeMenuItemData.ItemType itemType) {
        HomeMenuItemData homeMenuItemData = new HomeMenuItemData(title);
        homeMenuItemData.setRightText(rightText);
        homeMenuItemData.setItemType(itemType);
        return new HomeMenuItemBean(homeMenuItemData);
    }
}
