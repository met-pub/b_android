package com.techjumper.polyhome.b.home.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.techjumper.polyhome.b.home.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kevin on 16/4/22.
 */
public class FamilyModelView extends RelativeLayout {

    @Bind(R.id.right_arc)
    ArcView rightArc;
    @Bind(R.id.lfm_mode)
    TextView lfmMode;
    @Bind(R.id.left_arc)
    ArcView leftArc;
    @Bind(R.id.arrow)
    ImageView arrow;

    private boolean isTouch = true;
    private boolean isToLeft;
    private boolean isToRight;
    float x;
    float moveX;
    static final int MIN_DISTANCE = 10;

    public FamilyModelView(Context context) {
        super(context);
        initView(context);
    }

    public FamilyModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FamilyModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FamilyModelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_family_mode, this, true);
        ButterKnife.bind(view, this);

//        arrow.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (!isTouch)
//                    return false;
//                x = event.getX();
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        x = event.getX();
//                        v.getX();
//                        getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        event.getX();
//                        v.getX();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        moveX = event.getX();
//                        v.getX();
//                        if (x > moveX) {
//                            isToLeft = true;
//                            isToRight = false;
//                        } else {
//                            isToLeft = false;
//                            isToRight = true;
//                        }
//                        setAnimation();
//                        break;
//                }
////                x = moveX;
//                return true;
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch)
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                moveX = event.getX();
                if (x - moveX > MIN_DISTANCE) {
                    isToLeft = true;
                    isToRight = false;
                    setAnimation();
                } else if (moveX - x > MIN_DISTANCE) {
                    isToLeft = false;
                    isToRight = true;
                    setAnimation();
                }
                break;
        }
        return true;
    }

    private void setAnimation() {
        isTouch = false;
        if (isToLeft) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(arrow, "translationX", 0F, -86F);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(arrow, "rotation", 0F, 180F);

            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(leftArc, "alpha", 1F, 0F);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(leftArc, "translationX", 0F, -46F);

            ObjectAnimator objectAnimator5 = ObjectAnimator.ofInt(rightArc, "alpha", 0, 255);
            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(rightArc, "translationX", 46F, 0F);

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    lfmMode.setText(R.string.family_mode_home);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isTouch = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            set.play(objectAnimator1).with(objectAnimator5).with(objectAnimator6).before(objectAnimator2).with(objectAnimator3).with(objectAnimator4);
            set.setDuration(200);
            set.start();
        } else if (isToRight) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(arrow, "translationX", -86F, 0F);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(arrow, "rotation", 180F, 360F);

            ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(leftArc, "alpha", 0F, 1F);
            ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(leftArc, "translationX", -46F, 0F);

            ObjectAnimator objectAnimator5 = ObjectAnimator.ofInt(rightArc, "alpha", 255, 0);
            ObjectAnimator objectAnimator6 = ObjectAnimator.ofFloat(rightArc, "translationX", 0F, 46F);

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    lfmMode.setText(R.string.family_mode_away);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isTouch = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            set.play(objectAnimator1).with(objectAnimator5).with(objectAnimator6).before(objectAnimator2).with(objectAnimator3).with(objectAnimator4);
            set.setDuration(300);
            set.start();
        }
    }
}