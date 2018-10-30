package com.qixiu.newoulingzhu.engine;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiu.newoulingzhu.application.BaseApplication;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.utils.BitmapDecodeUtil;
import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.wanchang.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * 此类实现社区分享，点赞功能抽取
 *
 * @author gyh
 */
public class ShareLikeEngine implements OnClickListener, IUiListener {
    public ShareResultListenner shareResultListenner;
    private FrameLayout framelayout_sharesdk;
    private ShareHandler handler;
    public static final String sinaPackageName = "com.sina.weibo";
    public static final String wxPackageName = "com.tencent.mm";
    public static final String qqPackageName = "com.tencent.mobileqq";
    public static final String qzonePackageName = "com.qzone";
    public static final int PLATFORM_NAME = 0;
    public static final int POPUPWINDOW = 1;
    private static String feedCreatorID;
    private static String feedID;


    private String imageUrl;
    private String text;
    private String shareUrl;

    private Platform platform;
    private OnClickSharePlatformListener onClickSharePlatformListener;


    public String shareTitle;
    public PopupWindow pw;
    private ImageView tv_qQshape;
    private ImageView tv_weixinshape;
    private ImageView tv_friendshape;
    private ImageView tv_weiboShape;
    private TextView tv_close;
    private ImageView tv_qzon;
    private TextView tv_clipbord;

    public void setShareResultListenner(ShareResultListenner shareResultListenner) {
        this.shareResultListenner = shareResultListenner;
    }

    public interface ShareResultListenner {
        void shareSuccess();
        void shareFailure();
        void shareCancle();
    }


    public ShareLikeEngine() {


        handler = new ShareHandler(this);

    }


    /**
     * @param
     * @param imageUrl 动态的图片url(可选择默认动态的第一张图片没有就为空)
     * @param text     动态的文本
     * @param shareUrl 分享的url连接
     */
    public void releaseShareData(Activity activity, String imageUrl, String text, String shareUrl
            , String name) {


        //显示分享布局
        //       showShare(activity, null, null, imageUrl, text, shareUrl, feedCreatorID, feedID);
        //shareToQQ(text, shareUrl, imageUrl);
        if (TextUtils.isEmpty(name)) {
            showSharePopuWindow(activity, imageUrl, shareUrl, text);
        } else {
            View view = new ImageView(activity);
            view.setTag(R.id.tag_platform_name, name + "");
            onClick(view);
        }
        //showDialog(activity, text, shareUrl, imageUrl);
    }


    public PopupWindow showSharePopuWindow(Activity activity, final String imageUrl,
                                           final String shareUrl, final String text) {
        this.shareUrl = shareUrl;
        this.imageUrl = imageUrl;
        this.text = text;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        final int wmHeight = wm.getDefaultDisplay().getHeight();
        View contentView = View.inflate(activity, R.layout.layout_share_popuwindowd_dialog, null);
        tv_qQshape = (ImageView) contentView.findViewById(R.id.imageView_share_qq);
        tv_weixinshape = (ImageView) contentView.findViewById(R.id.imageView_share_weichat);
        tv_friendshape = (ImageView) contentView.findViewById(R.id.imageView_share_weichatcore);
        tv_weiboShape = (ImageView) contentView.findViewById(R.id.imageView_share_sinna);
        tv_close = (TextView) contentView.findViewById(R.id.tv_close);
        tv_qzon = (ImageView) contentView.findViewById(R.id.imageView_share_qzon);
        tv_clipbord = (TextView) contentView.findViewById(R.id.imageView_share_clibord);
        framelayout_sharesdk = (FrameLayout) contentView.findViewById(R.id.framelayout_sharesdk);
        final View ll_share_community = contentView.findViewById(R.id.ll_share_community);
//        contentView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_bottom_in_2));

        if (pw == null) {
            pw = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                    true);
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            pw.setClippingEnabled(false);
            pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }


//        pw.setTouchInterceptor(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.w("LOGCAT", "触摸的点:" + event.getY());
//                        Log.w("LOGCAT", "屏幕的高度:" + wmHeight);
//                        int height = ll_share_community.getHeight();
//                        Log.w("LOGCAT", "NoScrollGridView的高度::" + height);
//                        if (pw.isShowing() && wmHeight - event.getY() > height) {
//                            pw.dismiss();
//
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//
//                return false;
//            }
//        });
        framelayout_sharesdk.setOnClickListener(this);
        tv_qQshape.setOnClickListener(this);
        tv_qQshape.setTag(R.id.tag_platform_name, QQ.NAME);

        tv_weixinshape.setOnClickListener(this);
        tv_weixinshape.setTag(R.id.tag_platform_name, Wechat.NAME);

        tv_friendshape.setOnClickListener(this);
        tv_friendshape.setTag(R.id.tag_platform_name, WechatMoments.NAME);

        tv_weiboShape.setOnClickListener(this);
        tv_weiboShape.setTag(R.id.tag_platform_name, SinaWeibo.NAME);

        tv_close.setOnClickListener(this);
        tv_qzon.setOnClickListener(this);
        tv_qzon.setTag(R.id.tag_platform_name, QZone.NAME + "");
        //剪切板
        tv_clipbord.setOnClickListener(this);
        if (!pw.isShowing())
            pw.showAtLocation(
                    activity.getWindow().getDecorView().findViewById(android.R.id.content),
                    Gravity.BOTTOM, 0, 0);
//        //统计分享选择弹窗
//        StatisticsModel.saveNumberEvent(StatisticsEventID.COMMUNITY_SHARE_ACTIVITY,
//                activity.getApplicationContext());
        return pw;
    }


    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public void unRegister() {

    }

    /**
     * 分享图片
     *
     * @param
     * @param platformName
     * @param
     * @param text
     */
    public void shareSdkShare(Bitmap bmp, String imageUrl, String shareUrl,
                              final String platformName, String text, boolean local) {

        if (isAppInstalled(BaseApplication.getContext(),
                sinaPackageName) && SinaWeibo.NAME.equals(platformName)) {
            ToastUtil.toast(
                    BaseApplication.getContext().getString(
                            R.string.share_notsinaclient));
            return;
        }
        if (!isAppInstalled(BaseApplication.getContext(),
                wxPackageName) && (Wechat.NAME.equals(platformName) || WechatMoments.NAME.equals(
                platformName))) {
            ToastUtil.toast(
                    BaseApplication.getContext().getString(
                            R.string.share_notwxclient));
            return;
        }
        if (!isAppInstalled(BaseApplication.getContext(), qqPackageName) && QQ.NAME.equals(
                platformName)) {
            ToastUtil.toast(
                    BaseApplication.getContext().getString(R.string.share_notqqclient));
            return;
        }
//        if (!isAppInstalled(BaseApplication.getContext(),
//                qzonePackageName) && QZone.NAME.equals(platformName))
//        {
//            ArshowContextUtil.toast(
//                    ArshowApp.app.getApplicationContext().getApplicationContext().getString(
//                            R.string.share_notqzoneclient));
//            return;
//        }

        ShareParams sp = new ShareParams();
        if (shareTitle != null) {
            sp.setTitle(shareTitle);
        } else {
            sp.setTitle(BaseApplication.getContext().getString(R.string.app_name));
        }

        String resultText = "";
        if (SinaWeibo.NAME.equals(platformName)) {
            resultText = BaseApplication.getContext().getString(R.string.app_name) + "欢迎您";
        } else {
            resultText = BaseApplication.getContext().getString(R.string.app_name) + "欢迎您" + ConstantUrl.SHARE_CLICK_GO_URL;
        }

        if (!TextUtils.isEmpty(text)) {
            resultText = text;
        }
        String resultShareUrl = ConstantUrl.SHARE_CLICK_GO_URL;
        ;
        if (!TextUtils.isEmpty(shareUrl)) {
            resultShareUrl = shareUrl;
        }
        if (QQ.NAME.equals(platformName)) {
            if (resultText.length() > 40) {
                resultText = resultText.substring(0, 37) + "...";
            }

        }

//        if (QZone.NAME.equals(platformName))
//        {
//            if (resultText.length() > 600)
//            {
//                resultText = resultText.substring(0, 597) + "...";
//            }
//
//            sp.setSite("");
//            sp.setSiteUrl("");
//        }

        if (Wechat.NAME.equals(platformName) || WechatMoments.NAME.equals(platformName)) {
            if (resultText.length() >= 512) {
                resultText = resultText.substring(0, 508) + "...";
            }

        }

        /**
         * 这里判断微博的意义：ShareSDK分享 如果微博要分享需要将分享的连接内容写到setText中
         */
        if (SinaWeibo.NAME.equals(platformName)) {

            sp.setText(formatSinaText(resultText, resultShareUrl));
        } else {
            sp.setText(resultText);
        }
        /**
         * 微信分享链接设置方法是setUrl，QQ分享链接设置方法是setTitleUrl,这个必须明确区分
         */
        if (Wechat.NAME.equals(platformName) || WechatMoments.NAME.equals(platformName)) {
            sp.setUrl(resultShareUrl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
        } else if (QQ.NAME.equals(platformName) || QZone.NAME.equals(platformName))//|| QZone.NAME.equals(platformName)
        {
            sp.setTitleUrl(resultShareUrl);

        }


        if (bmp != null) {
            sp.setImageData(bmp);
        } else {


            if (!TextUtils.isEmpty(imageUrl)) {
                sp.setImageUrl(imageUrl);
            } else {
                Bitmap bitmap = BitmapDecodeUtil.decodeBitmap(BaseApplication.getContext(), R.mipmap.login_logo2x);
                sp.setImageData(bitmap);
            }
        }

//        if (QZone.NAME.equals(platformName))
//        {
//            platform = new QZone(ArshowApp.app);
//        } else
//        {
//            platform = ShareSDK.getPlatform(platformName);
//        }
        platform = ShareSDK.getPlatform(platformName);

        if (SinaWeibo.NAME.equals(platformName)) {
            platform.SSOSetting(false);
            if (platform.isValid()) {
                platform.removeAccount(true);
                ShareSDK.removeCookieOnAuthorize(true);
            }

        }

        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable t) {

                Message message = new Message();
                message.what = -1;
                message.obj = t;
                handler.sendMessage(message);
            }

            @Override
            public void onComplete(Platform platform, int arg1, HashMap<String, Object> arg2) { // 统计分享

                Message message = new Message();
                message.what = 1;
                message.obj = platform;
                handler.sendMessage(message);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("名称", platformName);
//                MobclickAgent.onEvent(ArshowApp.app,
//                        ResourceConstant.ANDROID_COMMUNITYFRAGMENTSHART, map);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
        platform.share(sp);
    }

    private String formatSinaText(String resultText, String resultShareUrl) {
        int resultTextLength = resultText.length();
        int resultShareUrlLength = resultShareUrl.length();
        int totalLength = resultShareUrlLength + resultTextLength;
        if (totalLength > 130) {
            String substring = resultText.substring(0, resultTextLength - (totalLength - 130 + 4));
            substring = substring + "...";
            String ultimatelyString = substring + resultShareUrl;

            return ultimatelyString;
        } else {
            return resultText + resultShareUrl;
        }

    }


    /**
     * 为了方便看，转换成中文
     *
     * @param name
     * @return
     */
    public static String getShareName(String name) {
        if (TextUtils.isEmpty(name))
            throw new RuntimeException("未知类型");
        if (Wechat.NAME.equals(name))
            return "微信";
        else if (WechatMoments.NAME.equals(name))
            return "朋友圈";
        else if (QQ.NAME.equals(name))
            return "QQ";
        else if (QZone.NAME.equals(name))
            return "QQ空间";
        else if (SinaWeibo.NAME.equals(name))
            return "新浪微博";
        return "未知类型";
    }


    @Override
    public void onCancel() {
        ToastUtil.toast("qq分享关闭");
    }

    @Override
    public void onComplete(Object arg0) {
        ToastUtil.toast("qq分享成功");
    }

    @Override
    public void onError(UiError arg0) {
        ToastUtil.toast("qq分享错误");
    }


    private class ShareHandler extends Handler {
        WeakReference<ShareLikeEngine> reference;

        public ShareHandler(ShareLikeEngine neg) {
            reference = new WeakReference<ShareLikeEngine>(neg);
        }

        @Override
        public void handleMessage(Message msg) {
            ShareLikeEngine object = reference.get();
            if (object != null) {
                switch (msg.what) {
                    case 1:
                        ToastUtil.toast(BaseApplication.getContext().getString(R.string.share_success));
                        if(shareResultListenner!=null){
                            shareResultListenner.shareSuccess();
                        }

                        break;
                    case 2:
                        Toast.makeText(BaseApplication.getContext(),
                                BaseApplication.getContext().getString(R.string.share_cancel),
                                Toast.LENGTH_SHORT).show();
                        if(shareResultListenner!=null){
                            shareResultListenner.shareCancle();
                        }

                        break;
                    case -1:
                        Toast.makeText(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.share_error),
                                Toast.LENGTH_SHORT).show();
                        if(shareResultListenner!=null){
                            shareResultListenner.shareFailure();
                        }
                        break;

                    default:
                        break;
                }
            }
        }

    }

    private void getShareCount(String feedCreatorID, String feedID, final Platform platform) {
        this.platform = platform;

    }


    /**
     * check the app is installed
     */
    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //("没有安装");
            return false;
        } else {
            //("已经安装");
            return true;
        }
    }


    public interface OnClickSharePlatformListener {
        void OnClickSharePlatform(String platformName);


    }


    public void setOnClickSharePlatformListener(
            OnClickSharePlatformListener onClickSharePlatformListener) {
        this.onClickSharePlatformListener = onClickSharePlatformListener;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageView_share_sinna:
            case R.id.imageView_share_qq:
            case R.id.imageView_share_weichat:
            case R.id.imageView_share_weichatcore:
            case R.id.imageView_share_qzon:
                String tag = (String) v.getTag(R.id.tag_platform_name);
                shareSdkShare(null, imageUrl, shareUrl, tag, text, false);
                if (onClickSharePlatformListener != null) {
                    onClickSharePlatformListener.OnClickSharePlatform(getShareName(tag));
                }
                break;
            case R.id.tv_close:

                break;

            default:
                break;
            case R.id.framelayout_sharesdk:
                pw.dismiss();
                break;
            case R.id.imageView_share_clibord:
                ClipboardManager clipboardManager = (ClipboardManager) BaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 从android系统中调用剪切板服务
                clipboardManager.setText(shareUrl);
                Toast.makeText(BaseApplication.getContext(), "已经复制到粘贴板", Toast.LENGTH_SHORT).show();
                break;
        }
        if (pw != null) {
            pw.dismiss();
        }
    }
}
