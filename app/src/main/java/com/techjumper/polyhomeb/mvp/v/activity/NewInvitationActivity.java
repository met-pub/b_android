package com.techjumper.polyhomeb.mvp.v.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.techjumper.corelib.mvp.factory.Presenter;
import com.techjumper.corelib.rx.tools.RxBus;
import com.techjumper.corelib.utils.common.JLog;
import com.techjumper.corelib.utils.common.RuleUtils;
import com.techjumper.corelib.utils.window.StatusbarHelper;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.adapter.NewInvitationActivityPhotoAdapter;
import com.techjumper.polyhomeb.mvp.p.activity.NewInvitationActivityPresenter;
import com.techjumper.polyhomeb.utils.SoftKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.event.PhotoPickerEvent;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/11
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
@Presenter(NewInvitationActivityPresenter.class)
public class NewInvitationActivity extends AppBaseActivity<NewInvitationActivityPresenter> {

    @Bind(R.id.et_title)
    EditText mEtTitle;   //帖子标题
    @Bind(R.id.et_content)
    EditText mEtContent;  //帖子正文
    @Bind(R.id.tv_input_number)
    TextView mTvInput;   //还可输入...
    @Bind(R.id.rv)
    RecyclerViewFinal mRv;  //图片
    @Bind(R.id.tv_type)
    TextView mTvType;     //栏目类型
    @Bind(R.id.iv_triangle)
    ImageView mIvTriangle;  //栏目类型的指示器
    @Bind(R.id.layout_root)
    View mRootView;   //页面跟布局,供popupwindow使用
    @Bind(R.id.layout)
    ScrollView mSv;
    @Bind(R.id.layout_static_head)
    View mStaticHead;
    @Bind(R.id.title_group)
    View mTitle;

    private ArrayList<String> mChoosedPhoto = new ArrayList<>();
    private NewInvitationActivityPhotoAdapter mAdapter;

    //屏幕高度
    private int mScreenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int mKeyHeight = 0;

    private boolean mIsIMEVisible = false;

    //软键盘高度
    private int mHeight1, mHeight2, mIMEHeight = 0;

    @Override
    protected View inflateView(Bundle savedInstanceState) {
        return inflate(R.layout.activity_new_invitation);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new NewInvitationActivityPhotoAdapter();
        mRv.setAdapter(mAdapter);
        mAdapter.loadData(getPresenter().getDatas());

        getIMEHeight();
        processScreenHeightAndIME();
        dispatchScrollEvent();

        RxTextView.textChangeEvents(mEtContent).subscribe(textViewTextChangeEvent -> {
            int length = mEtContent.getText().toString().length();
            if (length >= 800) {
                mTvInput.setVisibility(View.VISIBLE);
                mTvInput.setText(String.format(getResources().getString(R.string.input_limit_1000), length));
            } else {
                mTvInput.setVisibility(View.INVISIBLE);
            }
        });

        addSubscription(
                RxBus.INSTANCE.asObservable().subscribe(o -> {
                    if (o instanceof PhotoPickerEvent) {
                        PhotoPickerEvent event = (PhotoPickerEvent) o;
                        Intent intent = event.getIntent();
                        List<String> photos = null;
                        if (intent != null) {
                            photos = intent.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        }
                        mChoosedPhoto.clear();
                        if (photos != null) {
                            mChoosedPhoto.addAll(photos);
                        }

                        mAdapter.loadData(getPresenter().getDatas());
                        mAdapter.notifyDataSetChanged();
                    }
                }));
    }

    @Override
    public String getLayoutTitle() {
        return getString(R.string.new_invitation);
    }

    @Override
    protected boolean showTitleRight() {
        return true;
    }

    @Override
    protected boolean onTitleRightClick() {
        getPresenter().onTitleRightClick();
        return true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK &&
//                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
//            List<String> photos = null;
//            if (data != null) {
//                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
//            }
//            mChoosedPhoto.clear();
//            if (photos != null) {
//                mChoosedPhoto.addAll(photos);
//            }
//
//            mAdapter.loadData(getPresenter().getDatas());
//            mAdapter.notifyDataSetChanged();
//        }
//    }

    private void processScreenHeightAndIME() {
        //获取屏幕高度
        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyHeight = mScreenHeight / 3;
    }

    private void getIMEHeight() {
        SoftKeyboardUtil.observeSoftKeyboard(this, (softKeyboardHeight, visible) -> {
            if (!visible) {
                mHeight1 = softKeyboardHeight;
            } else {
                mHeight2 = softKeyboardHeight;
            }
            mIMEHeight = mHeight2 - mHeight1;

            if (mEtContent.hasFocus()) {  //只有当输入内容的时候才会进行以下操作,输入手机号码的时候则不会
                if (visible && !mIsIMEVisible) {
                    mStaticHead.setVisibility(View.GONE);
                    calculateHeight(false);
                    mSv.smoothScrollTo(0, 0);
                    mIsIMEVisible = true;
                } else if (!visible && mIsIMEVisible) {
                    mStaticHead.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = mEtContent.getLayoutParams();
                    layoutParams.height = RuleUtils.dp2Px(200);
                    mEtContent.setLayoutParams(layoutParams);
                    mIsIMEVisible = false;
                }
            }
        });
    }

    private void dispatchScrollEvent() {
        mEtContent.setOnTouchListener((v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mSv.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    mSv.requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return false;
        });
    }

    public void calculateHeight(boolean isReCalculate) {
        if (isReCalculate) {
            ViewGroup.LayoutParams layoutParams = mEtContent.getLayoutParams();
            layoutParams.height = mScreenHeight - mIMEHeight - mTitle.getHeight() - mRv.getHeight() - mTvInput.getHeight() + RuleUtils.dp2Px(12) - StatusbarHelper.getStatusBarHeightPx(this) + RuleUtils.dp2Px(70);
            mEtContent.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = mEtContent.getLayoutParams();
            layoutParams.height = mScreenHeight - mIMEHeight - mTitle.getHeight() - mRv.getHeight() - mTvInput.getHeight() + RuleUtils.dp2Px(12) - StatusbarHelper.getStatusBarHeightPx(this);
            mEtContent.setLayoutParams(layoutParams);
        }
    }

    public ArrayList<String> getPhotos() {
        return mChoosedPhoto;
    }

    public NewInvitationActivityPhotoAdapter getAdapter() {
        return mAdapter;
    }

    public EditText getEtTitle() {
        return mEtTitle;
    }

    public EditText getEtContent() {
        return mEtContent;
    }

    public ImageView getIvTriangle() {
        return mIvTriangle;
    }

    public View getRootView() {
        return mRootView;
    }

    public TextView getTvType() {
        return mTvType;
    }

}
