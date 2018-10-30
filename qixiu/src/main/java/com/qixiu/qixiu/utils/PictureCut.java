package com.qixiu.qixiu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;

import com.qixiu.qixiu.utils.luban.Luban;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/3.
 */
//用来做图片处理的类
public class PictureCut {
    private static String path = null;
    private static String defualtPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
    public static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(3) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // 重写此方法来衡量每张图片的大小，默认返回图片数量。
            return bitmap.getByteCount() / 1024;
        }
    };

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    //切圆之前先切成正方形
    public static Bitmap ImageCropSquere(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;
        //下面这句是关键
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    //切成圆的方法,第二个参数为2表示为圆形
    public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
        System.out.println("图片是否变成圆形模式了+++++++++++++");
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
                bitmap.getHeight() / ratio, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        System.out.println("pixels+++++++" + String.valueOf(ratio));
        return output;
    }

    //图片压缩
    //将Bitmap转换成Base64
    public static String getImgStr(Bitmap bit, int index) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, index, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //外部引用下面的图片压缩方法直接获取图片index 0~100为压缩比例
    public static Bitmap getLittleImg(Bitmap bitmap, int index) {
        String str = getImgStr(bitmap, index);
        byte[] bytes;
        bytes = Base64.decode(str, 0);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * @param imgPath
     * @param bitmap
     * @param imgFormat 图片格式
     * @return
     */
    //图片转化为base64格式的String
    public static String imgToBase64(String imgPath, Bitmap bitmap, String imgFormat) {
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if (bitmap == null) {
            //bitmap not found!!
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }

    /**
     * @param base64Data
     * @param imgName
     * @param imgFormat  图片格式
     */
    public static void base64ToBitmap(String base64Data, String imgName, String imgFormat) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        File myCaptureFile = new File("/sdcard/", imgName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myCaptureFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (isTu) {
            // fos.notifyAll();
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //把bitmap图片存到本地
    public static String saveBitmapToSdcard(Bitmap bm) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        file.mkdirs();
        File f = new File(file + "/", getTime() + "upload.jpg");
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f.getPath();
    }

    //把bitmap图片存到本地
    public static String saveBitmapToSdcard(Bitmap bm, String code) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        file.mkdirs();
        File f = new File(file + "/", code + "upload.jpg");
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return f.getPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return f.getPath();
        }
        return f.getPath();
    }

    //把bitmap图片存到本地
    public static String saveBitmapToSdcard(Bitmap bm, String code, int per) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath());
        file.mkdirs();
        File f = new File(file + "/", code + "upload.jpg");
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, per, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return f.getPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return f.getPath();
        }
        return f.getPath();
    }

    /*
    *
    * *获取缩略图**/
    private static String newPath;

    public static String getThubImage(String path) {
        double fileOrFilesSize = FileSizeUtil.getFileOrFilesSize(path, FileSizeUtil.SIZETYPE_KB);
        if (fileOrFilesSize < 300) {
            return path;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w > 720) {
            h = (int) (h / ((double) w / 720));
            w = 720;
        }
        Bitmap bitmap2 = getImageThumbnail(path, w, h);
        newPath = saveBitmapToSdcard(bitmap2);
//        if (FileSizeUtil.getFileOrFilesSize(newPath, FileSizeUtil.SIZETYPE_KB) > 300) {
//            getThubImage(newPath);
//        }
        return newPath;
    }

    public static String getThubImage(Bitmap bitmap) {
        String path = saveBitmapToSdcard(bitmap);
        double fileOrFilesSize = FileSizeUtil.getFileOrFilesSize(path, FileSizeUtil.SIZETYPE_KB);
        if (fileOrFilesSize < 300) {
            return path;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        if (w > 720) {
            h = (int) (h / ((double) w / 720));
            w = 720;
        }
        Bitmap imageThumbnail = getImageThumbnail(path, (int) (w / 1.1), (int) (h / 1.1));
        newPath = saveBitmapToSdcard(imageThumbnail);
        if (FileSizeUtil.getFileOrFilesSize(newPath, FileSizeUtil.SIZETYPE_KB) > 300) {
            getThubImage(newPath);
        }
        return newPath;
    }

    //下载图片的线程类
    public static class CompressImageWithThumb {
        //设计3个线程通道
        private static final ExecutorService executorService = Executors.newFixedThreadPool(9);
        private String path = "";

        public static void callpath(final String url,
                                    final CallBack call) {
            // 用于子线程与主线程通信的Handler
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 将返回值回调到callBack的参数中
                    Log.e("LOGCAT", "success");
                    call.callBack((String) msg.obj);
                }
            };
            // 开启线程去访问WebService
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = getThubImage(url);
                        Message mess = mHandler.obtainMessage(0, s);
                        mHandler.sendMessage(mess);
                    } catch (Exception e) {
                    }
                }
            });
        }

        public static void callpath(final Bitmap bitmap,
                                    final CallBack call) {
            // 用于子线程与主线程通信的Handler
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 将返回值回调到callBack的参数中
                    Log.e("LOGCAT", "success");
                    call.callBack((String) msg.obj);
                }
            };
            // 开启线程去访问WebService
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = getThubImage(bitmap);
                        Message mess = mHandler.obtainMessage(0, s);
                        mHandler.sendMessage(mess);
                    } catch (Exception e) {
                    }
                }
            });
        }

        public static void callBase64(final String path,
                                      final CallBackBase64 call) {
            // 用于子线程与主线程通信的Handler
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 将返回值回调到callBack的参数中
                    Log.e("LOGCAT", "success");
                    call.callBack((String) msg.obj);
                }
            };
            // 开启线程去访问WebService
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = getThubImage(path);
                        Bitmap bitmap1 = BitmapFactory.decodeFile(s);
                        String base64 = imgToBase64(s, bitmap1, "JPEG");
                        Message mess = mHandler.obtainMessage(0, base64);
                        mHandler.sendMessage(mess);
                    } catch (Exception e) {
                    }
                }
            });
        }

        public static void callBase64(final Bitmap bitmap,
                                      final CallBackBase64 call) {
            // 用于子线程与主线程通信的Handler
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 将返回值回调到callBack的参数中
                    Log.e("LOGCAT", "success");
                    call.callBack((String) msg.obj);
                }
            };
            // 开启线程去访问WebService
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = getThubImage(bitmap);
                        Bitmap bitmap1 = BitmapFactory.decodeFile(s);
                        String base64 = imgToBase64(s, bitmap1, "JPEG");
                        Message mess = mHandler.obtainMessage(0, base64);
                        mHandler.sendMessage(mess);
                    } catch (Exception e) {
                    }
                }
            });
        }

        public static void callBase64s(final List<String> pathlist,
                                       final CallBackBase64s call) {
            // 用于子线程与主线程通信的Handler
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 将返回值回调到callBack的参数中
                    Log.e("LOGCAT", "success");
                    call.callBack((List<String>) msg.obj);
                }
            };
            // 开启线程去访问WebService
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        List<String> base64List = new ArrayList<String>();
                        for (int i = 0; i < pathlist.size(); i++) {
                            String s = getThubImage(pathlist.get(i));
                            Bitmap bitmap1 = BitmapFactory.decodeFile(s);
//                            String base64 = imgToBase64(s, bitmap1, "JPEG");
                            String base64 = ImgHelper.bitmap2StrByBase64(bitmap1);
                            base64List.add(base64);
                        }
                        Message mess = mHandler.obtainMessage(0, base64List);
                        mHandler.sendMessage(mess);
                    } catch (Exception e) {
                    }
                }
            });
        }

        //返回图片的路径
        public interface CallBack {
            public void callBack(String path);
        }

        //返回图片的base64
        public interface CallBackBase64 {
            public void callBack(String base64);
        }

        //返回图片base64集合
        public interface CallBackBase64s {
            public void callBack(List<String> base64s);
        }
    }
    /*
    *
    * *获取缩略图**/

    /**
     * @param path          路径
     * @param displayWidth  需要显示的宽度
     * @param displayHeight 需要显示的高度
     * @return Bitmap
     */
    public static Bitmap ChangeImgeSize(String path, int displayWidth, int displayHeight) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
        Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
        // op.outWidth表示的是图像真实的宽度
        // op.inSamplySize 表示的是缩小的比例
        // op.inSamplySize = 4,表示缩小1/4的宽和高，1/16的像素，android认为设置为2是最快的。
        // 获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / (float) displayWidth);
        int hRatio = (int) Math.ceil(op.outHeight / (float) displayHeight);
        // 如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                // 如果太宽，我们就缩小宽度到需要的大小，注意，高度就会变得更加的小。
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        // 从原Bitmap创建一个给定宽高的Bitmap
        return Bitmap.createScaledBitmap(bmp, displayWidth, displayHeight, true);
    }


    public static String getTime() {
        String time = "";
        Time t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int minute = t.minute;
        int hour = t.hour;
        int second = t.second;
        String months = month + "";
        String days = day + "";
        if (month < 10) {
            months = 0 + "" + months;
        }
        if (day < 10) {
            days = 0 + "" + day;
        }
        time = "" + year + months + days + hour + second;
        return time;
    }

    //获取网络地址的图片或者视频的url标识
    public static String getCode(String url) {
//        url = url.replace(".mp4", "");
//        url = url.replace(".avi", "");
//        url = url.replace(".jpg", "");
//        url = url.replace(".png", "");
//        url = url.replace(".doc", "");
//        url = url.replace(".pdf", "");
        String tag = url.substring(url.lastIndexOf('.'), url.length());
        url = url.replace(tag, "");
        int posiotion = url.lastIndexOf("/");
        String code = url.substring(posiotion, url.length() - 1);
        return code.replace("/", "");
    }

    //默认查找图片目录
    public static String getPath(String code) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "");
//        Log.e("picpath", Environment.getExternalStorageDirectory().getPath() + "/sdcard/DCIM/");
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            Log.e("picpath", file.getPath());
        } catch (Exception e) {
            Log.e("picpath", e.toString());
        }
        getFilePath(file, code);
        return path;
    }

    public static String creatPath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "");
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            Log.e("picpath", file.getPath());
        } catch (Exception e) {
            Log.e("picpath", e.toString());
        }
        return file.getPath();
    }

    public static String getFilePath(File file, String code) {
        try {
            File files[] = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().contains(code)) {
                        path = f.getPath();
                        Log.e("picpath", f.getPath());
                        return f.getPath();
                    }
                } else {
                    getFilePath(f, code);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return path;
    }

    public static void deleteFileWith(File file, String code) {
        try {
            File files[] = file.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().contains(code)) {
                        f.delete();
                    }
                } else {
                    deleteFileWith(f, code);
                }
            }
        } catch (Exception e) {
        }
    }

    //清除缓存的时候调用
    public static void deleteAllImage() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "");
        deleteFileWith(file, "upload");
    }

    //获取指定尺寸的缩略图
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    //下载图片的线程类
    public static class InitBitmap {
        //设计3个线程通道
        private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
        private String path = "";

        public static void callBitmap(final String url,
                                      final BitmapCallBack bitmapCallBack) {
            // 用于子线程与主线程通信的Handler
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // 将返回值回调到callBack的参数中
                    Log.e("LOGCAT", "success");
                    bitmapCallBack.callBack((String) msg.obj);
                }
            };

            // 开启线程去访问WebService
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("LOGCAT", "run");
                        //从网络获取缩略图，先看看本地有没有这号文件
                        String code = PictureCut.getCode(url);
                        String path = PictureCut.getPath(code);
                        if (path != null && new File(path).length() > 200) {
                            //如果有，直接返回本地路径
                            Message mess = mHandler.obtainMessage(0, path);
                            mHandler.sendMessage(mess);
                            return;
                        }
                        //如果没有，存储到本地，并且把本地路径传过去
                        Bitmap bitmap = PictureCut.returnNetBitmap(url);
                        String s = PictureCut.saveBitmapToSdcard(bitmap, code);
                        Message mess = mHandler.obtainMessage(0, s);
                        mHandler.sendMessage(mess);
                    } catch (Exception e) {

                    }
                }
            });
        }

        /**
         * @author xiaanming
         */
        //返回图片的路径
        public interface BitmapCallBack {
            public void callBack(String path);
        }
    }

    //吧网络图片放入缓存中
    public static Bitmap returnNetBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //输入流解读成字符流
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;

    }

    //将bitmap对象转为流
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //吧流转为bitmap
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    //文件剪切
    public static void cutFile(File oraginalFile, File toBeFile) {
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        byte[] bytes = new byte[1024];
        int temp = 0;
        try {
            inputStream = new FileInputStream(oraginalFile);
            fileOutputStream = new FileOutputStream(toBeFile);
            while ((temp = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, temp);
                fileOutputStream.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    oraginalFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> compressResults = new ArrayList<>();

    public interface CallBack<T> {
        void call(T t);
    }

    public static class CompressRxjava {

        public static void compress(Context context, List<String> paths, CallBack callBack) {
            compressResults.clear();
            if (paths.size() == 0) {
                callBack.call(compressResults);
                return;
            }
            Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    for (int i = 0; i < paths.size(); i++) {
                        e.onNext(paths.get(i));
                        if (i == paths.size() - 1) {
                            e.onComplete();//结束next业务循环
                        }
                    }
                }
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());
            Observer observer = new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String value) {
                    //业务内容
                    String thubImage = getThubImage(value);
                    String base64 = ImgHelper.bitmap2StrByBase64(BitmapFactory.decodeFile((new File(thubImage).getPath())));
                    compressResults.add(base64);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    callBack.call(compressResults);
                }
            };
            observable.subscribe(observer);
        }
    }

    public static class CompressLuban {
        public static void comPress(Context context, List<String> paths,CallBack callBack) {
            //todo 最新款压缩图片
            Flowable.just(paths)
                    .observeOn(Schedulers.io())
                    .map(new Function<List<String>, List<File>>() {
                        @Override
                        public List<File> apply(@NonNull List<String> list) throws Exception {
                            // 同步方法直接返回压缩后的文件
                            return Luban.with(context).load(list).get();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<File>>() {
                        @Override
                        public void accept(List<File> files) throws Exception {
                            callBack.call(files);
                        }
                    });
        }

        public static void toBase64s(List<File> files,CallBack callBack){
            Flowable.just(files)
                    .observeOn(Schedulers.io())
                    .map(new Function<List<File>, List<String>>() {
                        @Override
                        public List<String> apply(List<File> files) throws Exception {
                            List<String>  list=new ArrayList<>();
                            for (int i = 0; i < files.size(); i++) {
                               Bitmap bitmap= BitmapFactory.decodeFile(files.get(i).getPath());
                               String base64=ImgHelper.bitmap2StrByBase64(bitmap);
                                list.add(base64);
                            }
                            return list;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<String>>() {
                        @Override
                        public void accept(List<String> strings) throws Exception {
                            callBack.call(strings);
                        }
                    });
        }
    }

}
