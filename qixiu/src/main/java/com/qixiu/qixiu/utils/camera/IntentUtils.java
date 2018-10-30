package com.qixiu.qixiu.utils.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.qixiu.qixiu.utils.FileProviderUtil;

import java.io.File;

/**
 * Created by Long on 2017/11/24
 */
public class IntentUtils {

    /**
     * 相册选择图片
     *
     * @param context     context
     * @param requestCode range(from = 1,to = any)
     */
    public static void startActionPick(@NonNull Activity context, @IntRange(from = 1) int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动相机拍照
     *
     * @param context     context
     * @param requestCode range(from = 1,to = any)
     * @param temp        拍照后保存文件位置
     */
    public static void startCapture(@NonNull Activity context,
                                    @IntRange(from = 1) int requestCode,
                                    @NonNull File temp) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProviderUtil.getUriForFile(context, temp);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
        }
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动拨号界面
     *
     * @param context context
     * @param number  拨号号码
     */
    public static void startDial(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

}
