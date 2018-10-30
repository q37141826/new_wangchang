package com.qixiu.qixiu.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.qixiu.qixiu.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Long on 2017/3/24
 */
public class FileProviderUtil {

    private static final String COMPRESS_DIR = "COMPRESS";

    /**
     * FileProvider # authority
     *
     * @see FileProvider#getUriForFile(Context, String, File)
     */
    private static final String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileProvider";

    public static Uri getUriForFile(Context context, File file) {
//        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file);
      return   MyFileProvider.getUriForFile(context,"com.qixiu.fileprovider",file);
    }

    public static String getFilePath(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (scheme.startsWith("file")) {
            return uri.getEncodedPath();
        }
        ContentResolver cr = context.getContentResolver();
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = MediaStore.Images.Media.query(cr, uri, proj);
        int columnIndexOrThrow = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndexOrThrow);
        cursor.close();
        return s;
    }

    public static String getCompressTargetDir(Context context) {
        File cacheDir = context.getCacheDir();
        File dir = new File(cacheDir, COMPRESS_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static void clearCompressDir(Context context) {
        File cacheDir = context.getCacheDir();
        File dir = new File(cacheDir, COMPRESS_DIR);
        if (dir != null) {
            clearDirectory(dir);
        }
    }

    private static void clearDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    clearDirectory(file);
                }
                file.delete();
            }
        }
    }

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}
