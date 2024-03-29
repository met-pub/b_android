package com.techjumper.polyhomeb.utils;

import android.graphics.Bitmap;

import com.techjumper.corelib.utils.file.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/2
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PicUtils {

    /**
     * 将bitmap保存为本地图片
     */
    public static String savePhoto(Bitmap photoBitmap, String path, String photoName) {

        String localPath = null;
        if (FileUtils.isSDCardMounted()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                            fileOutputStream)) {
                        localPath = photoFile.getPath();
                        fileOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                photoFile.delete();
                localPath = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return localPath;
    }
}
