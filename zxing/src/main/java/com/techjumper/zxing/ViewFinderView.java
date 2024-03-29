package com.techjumper.zxing;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class ViewFinderView extends View implements IViewFinder {
    private static final String TAG = "ViewFinderView";

    private Rect mFramingRect;
    private Rect mScanRect;

    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;

    private static final float LANDSCAPE_WIDTH_RATIO = 5f / 8;
    private static final float LANDSCAPE_HEIGHT_RATIO = 5f / 8;
    private static final int LANDSCAPE_MAX_FRAME_WIDTH = (int) (1920 * LANDSCAPE_WIDTH_RATIO); // = 5/8 * 1920
    private static final int LANDSCAPE_MAX_FRAME_HEIGHT = (int) (1080 * LANDSCAPE_HEIGHT_RATIO); // = 5/8 * 1080

    private static final float PORTRAIT_WIDTH_RATIO = 7f / 8;
    private static final float PORTRAIT_HEIGHT_RATIO = 3f / 8;
    private static final int PORTRAIT_MAX_FRAME_WIDTH = (int) (1080 * PORTRAIT_WIDTH_RATIO); // = 7/8 * 1080
    private static final int PORTRAIT_MAX_FRAME_HEIGHT = (int) (1920 * PORTRAIT_HEIGHT_RATIO); // = 3/8 * 1920

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private int scannerAlpha;
    private static final int POINT_SIZE = 10;
    private static final long ANIMATION_DELAY = 80l;

    private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
    private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
    private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
    private final int mDefaultBorderStrokeWidth = getResources().getInteger(R.integer.viewfinder_border_width);
    private final int mDefaultBorderLineLength = getResources().getInteger(R.integer.viewfinder_border_length);

    protected Paint mStrokePaint;
    protected Paint mLaserPaint;
    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;

    private Bitmap mScanBitmap;
    private ObjectAnimator mScannerAnimator;

    public ViewFinderView(Context context) {
        super(context);
        init();
    }

    public ViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mScanBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_line);

        mStrokePaint = new Paint();
        mStrokePaint.setColor(0x7FFFFFFF);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5F
                , getResources().getDisplayMetrics()));


        //set up laser paint
        mLaserPaint = new Paint();
        mLaserPaint.setColor(mDefaultLaserColor);
        mLaserPaint.setStyle(Paint.Style.FILL);

        //finder mask paint
        mFinderMaskPaint = new Paint();
        mFinderMaskPaint.setColor(mDefaultMaskColor);

        //border paint
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mDefaultBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);

//        mBorderLineLength = mDefaultBorderLineLength;
        mBorderLineLength = (int) (getResources().getDisplayMetrics().heightPixels * 0.022488F);
    }

    public void setLaserColor(int laserColor) {
        mLaserPaint.setColor(laserColor);
    }

    public void setMaskColor(int maskColor) {
        mFinderMaskPaint.setColor(maskColor);
    }

    public void setBorderColor(int borderColor) {
        mBorderPaint.setColor(borderColor);
    }

    public void setBorderStrokeWidth(int borderStrokeWidth) {
        mBorderPaint.setStrokeWidth(borderStrokeWidth);
    }

    public void setBorderLineLength(int borderLineLength) {
        mBorderLineLength = borderLineLength;
    }

    public void setScannerTop(int top) {
        if (mScanRect == null) return;
        int height = mScanRect.height();
        mScanRect.top = top;
        mScanRect.bottom = top + height;
        invalidate();
    }

    public int getScannerTop() {
        if (mScanRect == null) return 0;
        return mScanRect.top;
    }

    @Override
    public void setupViewFinder() {
        updateFramingRect();
        invalidate();
    }

    @Override
    public Rect getFramingRect() {
        return mFramingRect;
    }

    @Override
    public void startAnim() {
        if (mFramingRect == null || mScanRect == null) return;
        if (mScannerAnimator != null && mScannerAnimator.isRunning()) {
            mScannerAnimator.cancel();
        }

        mScannerAnimator = ObjectAnimator.ofInt(this, "scannerTop", mFramingRect.bottom - mScanRect.height());
        mScannerAnimator.setRepeatMode(ValueAnimator.INFINITE);
        mScannerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mScannerAnimator.setDuration(2000);
        mScannerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mScannerAnimator.start();
    }

    @Override
    public void stopAnim() {
        if (mScannerAnimator != null && mScannerAnimator.isRunning()) {
            mScannerAnimator.cancel();
            mScannerAnimator = null;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mFramingRect == null) {
            return;
        }

        drawViewFinderMask(canvas);
        drawScanner(canvas);
        drawViewStroke(canvas);
        drawViewFinderBorder(canvas);
//        drawLaser(canvas);
    }

    private void drawScanner(Canvas canvas) {
        canvas.drawBitmap(mScanBitmap, null, mScanRect, null);
    }

    private void drawViewStroke(Canvas canvas) {
        canvas.drawRect(mFramingRect, mStrokePaint);
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float dp1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        canvas.drawRect(0, 0, width, mFramingRect.top, mFinderMaskPaint);
        canvas.drawRect(0, mFramingRect.top, mFramingRect.left, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 * 2, mFinderMaskPaint);
        canvas.drawRect(mFramingRect.right - mDefaultBorderStrokeWidth + dp1 * 2, mFramingRect.top, width, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 * 2, mFinderMaskPaint);
        canvas.drawRect(0, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 * 2, width, height, mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        float dp1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        canvas.drawLine(mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2 + mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2 + mBorderLineLength, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2, mBorderPaint);

        canvas.drawLine(mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2 - mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.left + mDefaultBorderStrokeWidth - dp1 - 2 + mBorderLineLength, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2, mBorderPaint);

        canvas.drawLine(mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2 + mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2, mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2 - mBorderLineLength, mFramingRect.top + mDefaultBorderStrokeWidth - dp1 - 2, mBorderPaint);

        canvas.drawLine(mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2 - mBorderLineLength, mBorderPaint);
        canvas.drawLine(mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2, mFramingRect.right - mDefaultBorderStrokeWidth + dp1 + 2 - mBorderLineLength, mFramingRect.bottom - mDefaultBorderStrokeWidth + dp1 + 2, mBorderPaint);
    }

    public void drawLaser(Canvas canvas) {
        // Draw a red "laser scanner" line through the middle to show decoding is active
        mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha - mDefaultBorderStrokeWidth) % SCANNER_ALPHA.length;
        int middle = mFramingRect.height() / 2 + mFramingRect.top;
        canvas.drawRect(mFramingRect.left + 2, middle + mDefaultBorderStrokeWidth, mFramingRect.right + mDefaultBorderStrokeWidth, middle + 2, mLaserPaint);

        postInvalidateDelayed(ANIMATION_DELAY,
                mFramingRect.left - POINT_SIZE,
                mFramingRect.top - POINT_SIZE,
                mFramingRect.right + POINT_SIZE,
                mFramingRect.bottom + POINT_SIZE);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
//        Point viewResolution = new Point(getWidth(), getHeight());
//        int width;
//        int height;
//        int orientation = DisplayUtils.getScreenOrientation(getContext());
//
//        if (orientation != Configuration.ORIENTATION_PORTRAIT) {
//            width = findDesiredDimensionInRange(LANDSCAPE_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, LANDSCAPE_MAX_FRAME_WIDTH);
//            height = findDesiredDimensionInRange(LANDSCAPE_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, LANDSCAPE_MAX_FRAME_HEIGHT);
//        } else {
//            width = findDesiredDimensionInRange(PORTRAIT_WIDTH_RATIO, viewResolution.x, MIN_FRAME_WIDTH, PORTRAIT_MAX_FRAME_WIDTH);
//            height = findDesiredDimensionInRange(PORTRAIT_HEIGHT_RATIO, viewResolution.y, MIN_FRAME_HEIGHT, PORTRAIT_MAX_FRAME_HEIGHT);
//        }
//
//        int leftOffset = (viewResolution.x - width) / 2;
//        int topOffset = (viewResolution.y - height) / 2;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        int left = (int) (screenWidth * 0.168F);
        int top = (int) (screenWidth * 0.3493F);
        int right = (int) (screenWidth * 0.8346F);
        int bottom = (int) (screenWidth * 1.016F);

        mFramingRect = new Rect(left, top, right, bottom);

        int bitmapHeight = (int) ((right - left) * 0.1916F);
        mScanRect = new Rect(left, top, right, top + bitmapHeight);
    }

    private static int findDesiredDimensionInRange(float ratio, int resolution, int hardMin, int hardMax) {
        int dim = (int) (ratio * resolution);
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }
}
