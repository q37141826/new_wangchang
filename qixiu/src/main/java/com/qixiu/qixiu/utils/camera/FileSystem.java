package com.qixiu.qixiu.utils.camera;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Long on 2017/11/24
 * <p>文件系统</p>
 * <p>使用前确保有存储权限</p>
 */
public class FileSystem {

    public static File createImageTempFile(Context context) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, System.currentTimeMillis() + ".jpg");
    }

}
