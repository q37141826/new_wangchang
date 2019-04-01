package com.qixiu.newoulingzhu.mvp.view.activity.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qixiu.newoulingzhu.beans.home.HotInformationBean;
import com.qixiu.newoulingzhu.beans.home.cases.CasesListBean;
import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.engine.ShareLikeEngine;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.wanchang.R;


public class GoToActivity extends TitleActivity {
    private String url, filePath, title = "";
    private WebView webView_goto;
    private ZProgressHUD zProgressHUD;
    private boolean is_show_share;
    private Object bean;

    @Override
    protected void onInitViewNew() {
        zProgressHUD = new ZProgressHUD(this);
        try {
            url = getIntent().getStringExtra(ConstantString.URL);
            title = getIntent().getStringExtra(ConstantString.TITLE_NAME);
            filePath = getIntent().getStringExtra(ConstantString.FILEPATH);
            is_show_share = getIntent().getBooleanExtra(IntentDataKeyConstant.SHOW_SHARE,false);
            bean = getIntent().getParcelableExtra(IntentDataKeyConstant.DATA);
        } catch (Exception e) {
        }
        if(is_show_share){
            mTitleView.setRightImage(getContext(),R.mipmap.share2x);
            mTitleView.setRightTextVisible(View.VISIBLE);
            mTitleView.setRightListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareLikeEngine shareLikeEngine = new ShareLikeEngine();
                    if(bean instanceof  HotInformationBean.OBean.ListBean){
                        HotInformationBean.OBean.ListBean data= (HotInformationBean.OBean.ListBean) bean;
                        shareLikeEngine.setShareTitle(TextUtils.isEmpty(data.getTitle())? ConstantUrl.TITILE:data.getTitle());
                        shareLikeEngine .releaseShareData(getActivity(),data.getSmeta(),"欧伶猪法务咨询", url, null);
                    }
                    if(bean instanceof   CasesListBean.OBean.DataBean ){
                        CasesListBean.OBean.DataBean data= (CasesListBean.OBean.DataBean) bean;
                        shareLikeEngine.setShareTitle(TextUtils.isEmpty(data.getTitle())? ConstantUrl.TITILE:data.getTitle());
                        if(data.getType().equals("case")){
                            shareLikeEngine .releaseShareData(getActivity(),data.getThumbnail(),"欧伶猪法务咨询", url, null);
                        }else {
                            shareLikeEngine .releaseShareData(getActivity(),data.getThumbnail(),"欧伶猪法务咨询", url, null);
                        }
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(title)) {
            mTitleView.setTitle(title);
        } else {
            mTitleView.setTitle("不良行为公示详情");
        }
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView_goto.canGoBack()) {
                    webView_goto.goBack();
                    return;
                }
                finish();
            }
        });
        webView_goto = (WebView) findViewById(R.id.webView_goto);
        //设置webview禁止缩放
        WebSettings settings = webView_goto.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (url != null && !TextUtils.isEmpty(url)) {
            webView_goto.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.loadUrl(url.getUrl().toString());
                    } else {
                        view.loadUrl(url.toString());
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    zProgressHUD.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    imgReset();
                    zProgressHUD.dismiss();
                }
            });
        } else if (filePath != null && !TextUtils.isEmpty(filePath)) {
            if (filePath.contains("p")) {
                webView_goto.getSettings().setJavaScriptEnabled(true);
                webView_goto.getSettings().setSupportZoom(false);
                webView_goto.getSettings().setDomStorageEnabled(true);
                webView_goto.getSettings().setAllowFileAccess(true);
                webView_goto.getSettings().setPluginState(WebSettings.PluginState.ON);
                webView_goto.getSettings().setUseWideViewPort(true);
                webView_goto.getSettings().setBuiltInZoomControls(true);
                webView_goto.requestFocus();
                webView_goto.getSettings().setDefaultFontSize(20) ;
               // webView_goto.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                webView_goto.getSettings().setTextZoom(300);
                webView_goto.getSettings().setLoadWithOverviewMode(true);
                webView_goto.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//                webView_goto.loadUrl("http://docs.google.com/gview?embedded=true&url=" +filePath);
                filePath= HtmlFormat.getNewContent(filePath);
                String baseUrl = "file://" + filePath;
                webView_goto.loadDataWithBaseURL(baseUrl, filePath, "text/html", "utf-8", null);
//                CommonUtils.setWebview(webView_goto,filePath,true);
            }
        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        url = intent.getStringExtra(ConstantString.URL);
    }

    //设置回退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView_goto.canGoBack()) {
            webView_goto.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onInitData() {
        webView_goto.loadUrl(url);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_go_to;
    }
    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webView_goto.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
       webView_goto.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    public static class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            Log.i("TAG", "响应点击事件!");
            Intent intent = new Intent();
            intent.putExtra("image", img);
//            intent.setClass(context, Go.class);//BigImageActivity查看大图的类，自己定义就好
            context.startActivity(intent);
        }
    }
}
