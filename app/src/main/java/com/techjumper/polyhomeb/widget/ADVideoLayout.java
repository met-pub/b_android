package com.techjumper.polyhomeb.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.lib2.downloader.SimpleDownloadBuilder;
import com.techjumper.lib2.downloader.listener.IDownloadError;
import com.techjumper.lib2.downloader.listener.IDownloadState;
import com.techjumper.polyhomeb.Config;
import com.techjumper.polyhomeb.R;
import com.techjumper.polyhomeb.entity.ADEntity;

import java.io.File;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class ADVideoLayout extends RelativeLayout implements IDownloadError, IDownloadState
        , TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private ImageView mIvPlayView;
    private ImageView mIvPlaceHolder;

    private MediaPlayer mMediaPlayer;
    private Surface mSurface;

    private String mVideoFilePath = getExternalStorageDirectory().getAbsolutePath() + File.separator
            + Config.sParentDirName + File.separator + Config.sADVideoDirName;
    private String mVideoName;

    private String mFullPath;

    private ADEntity.DataBean.AdInfosBean mData;

    private boolean mIsPrepare = false;

    public ADVideoLayout(Context context) {
        super(context);
        init(context);
    }

    public ADVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ADVideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ADVideoLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_ad_texture_view, null);
        addView(inflate);
        mTextureView = (TextureView) findViewById(R.id.ad_video);
        mIvPlaceHolder = (ImageView) findViewById(R.id.iv_place_holder);
        mIvPlayView = (ImageView) findViewById(R.id.iv_play);
        this.mTextureView.setSurfaceTextureListener(this);
    }

    public void setData(ADEntity.DataBean.AdInfosBean data) {
        this.mData = data;
    }

    public void initVideo() {
        getVideoName();
    }

    public void stopWork() {
        try {
            if (mMediaPlayer == null || !mIsPrepare) return;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            stopVideo();
        } catch (Exception ignored) {

        }
    }

    private void getVideoName() {
        String media_url = mData.getMedia_url();
        if (TextUtils.isEmpty(media_url) || !media_url.contains("/")) {
            showPlaceHolderPic();
            return;
        }
        String[] split = media_url.split("/");
        if (split.length <= 0) {
            showPlaceHolderPic();
            return;
        }
        mVideoName = split[split.length - 1];
        boolean exists = checkVideoExists();
        if (exists && getVideoSizeOnSD() > 0) {
            mFullPath = "file://" + mVideoFilePath + File.separator + mVideoName;
            showVideoView();
            return;
        }
        showPlaceHolderPic();
        new SimpleDownloadBuilder()
                .setListener(null, this, this)
                .setName(mVideoName)
                .setNotifyPercent(2)
                .setPath(mVideoFilePath)
                .setUrl(Config.sHost + mData.getMedia_url())
                .download();
    }

    private boolean checkVideoExists() {
        return FileUtils.isFileExist(createFilePath() + File.separator + mVideoName);
    }

    private long getVideoSizeOnSD() {
        File f = new File(createFilePath() + File.separator + mVideoName);
        if (f.exists() && f.isFile())
            return f.length();
        else
            return 0;
    }

    private File createFilePath() {
        File videoFolder = new File(mVideoFilePath);
        if (!videoFolder.exists()) try {
            videoFolder.mkdirs();
        } catch (Exception ignored) {
        }
        return videoFolder;
    }

    private void showPlaceHolderPic() {
        if (mIvPlaceHolder != null) {
            mIvPlaceHolder.setVisibility(View.VISIBLE);
        }
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.GONE);
        }
        if (mTextureView != null) {
            mTextureView.setVisibility(View.GONE);
        }
    }

    private void showVideoView() {
        if (mTextureView != null) {
            mTextureView.setVisibility(View.VISIBLE);
        }
        if (mIvPlaceHolder != null) {
            mIvPlaceHolder.setVisibility(View.GONE);
        }
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.VISIBLE);
        }
    }

    private void playVideo() {
        showVideoView();
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.GONE);
        }
    }

    private void stopVideo() {
        if (mIvPlaceHolder != null) {
            mIvPlaceHolder.setVisibility(View.GONE);
        }
        if (mIvPlayView != null) {
            mIvPlayView.setVisibility(View.VISIBLE);
        }
        if (mTextureView != null) {
            mTextureView.setVisibility(View.VISIBLE);
        }
    }

    private void showFirstFrame() {
        if (mMediaPlayer == null) return;
        mMediaPlayer.seekTo(0);
    }

    private void initMediaPlayer() {
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setVolume(0, 0);//静音
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setDataSource(mFullPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(mp -> {
                mIsPrepare = true;
                showFirstFrame();
            });
            mMediaPlayer.prepareAsync();   //此处使用异步加载，可以防止首页滑动时候卡顿
//            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //注释的原因是为了让点击第一次就播放，再次点击则不暂停了，而是提取携带的URL，跳转
    public void onClick() {
        if (mMediaPlayer == null || !mIsPrepare) return;
//        if (mMediaPlayer.isPlaying()) {
//            stopVideo();
//            mMediaPlayer.pause();
//            return;
//        }
        playVideo();
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null || !mIsPrepare) return false;
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        initMediaPlayer();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        mTextureView = null;
        //在子线程释放资源
        Observable.just(null)
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (mMediaPlayer != null) {
                            mMediaPlayer.stop();
                            mMediaPlayer.release();
                            mMediaPlayer = null;
                        }
                    }
                });
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onDownloadError(Throwable e) {

    }

    @Override
    public void onDownloadStateChange(State state, File downloadFile) {
        switch (state) {
            case FINISH:
                mFullPath = "file://" + downloadFile.getAbsolutePath();
                showVideoView();
                break;
        }
    }
}
