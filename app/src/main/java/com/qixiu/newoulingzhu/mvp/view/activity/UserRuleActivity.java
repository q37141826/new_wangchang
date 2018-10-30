package com.qixiu.newoulingzhu.mvp.view.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.wanchang.R;

public class UserRuleActivity extends TitleActivity {
    private WebView webview_userRule;
    private ZProgressHUD zpro;
    public static final String REGISTER = "register";
    public static final String VIP = "register";
    private String type;

    @Override
    protected void onInitViewNew() {
        zpro = new ZProgressHUD(this);
        webview_userRule = (WebView) findViewById(R.id.webview_userRule);
        type = getIntent().getStringExtra(IntentDataKeyConstant.TYPE);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onInitData() {

        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置webview禁止缩放
        WebSettings settings = webview_userRule.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        webview_userRule.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                zpro.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                zpro.show();
                super.onPageStarted(view, url, favicon);

            }
        });
//        OkHttpUtils.get().url(ConstantUrl.UserRuleUrl)
//                .addParams("type", type)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String s, int i) {
//                        try {
//                            UserRuleBean userRuleBean = new Gson().fromJson(s, UserRuleBean.class);
//                            webview_userRule.loadDataWithBaseURL(null, userRuleBean.getO().getContents(), "text/html", "utf-8", null);
//                        } catch (Exception e) {
//                        }
//
//                    }
//                });
        String url;
        if(type.equals(REGISTER)){
            mTitleView.setTitle("用户使用协议");
            url= ConstantUrl.hosturl+"/detail/userAgreement.html?URL="+ ConstantUrl.hosturl;
        }else {
            mTitleView.setTitle("欧伶猪VIP会员服务协议");
            url= ConstantUrl.hosturl+"/detail/vipAgreement.html?URL="+ ConstantUrl.hosturl;
        }
        webview_userRule.loadUrl(url);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_user_rule;
    }

    @Override
    protected void onStop() {
        super.onStop();
        new Thread(new Runnable() {
            @Override
            public void run() {
// 清除cookie即可彻底清除缓存
                CookieSyncManager.createInstance(UserRuleActivity.this);
                CookieManager.getInstance().removeAllCookie();
            }
        }).start();
    }


    class UserRuleBean {

        /**
         * c : 1
         * m : 查询成功
         * o : {"post_content":"<p style=\"text-align: center;\"><span style=\"color: rgb(151, 72, 6); font-size: 18px;\">众筹用户须知<\/span><\/p><p style=\"text-indent: 2em;\">数据库和的房价安徽打卡机贷款啦巨大空间大家都会就卡萨大恒科技安徽打卡机安徽的是科技爱好打卡机了撒活动空间按户口.<\/p><p style=\"text-align: left; text-indent: 0em;\"><span style=\"color: rgb(151, 72, 6);\">第一条 <span style=\"color: rgb(0, 0, 0);\">尽快撒谎打卡机哈空间大哈空间华盛顿科技安徽萨克的哈克家和贷款家安山东矿机安徽省的科技哈空间技术很大空间和尽快撒谎开机的哈空间活动卡华盛顿科技安徽打卡机啊手机卡还贷款就安徽打卡机啊哈就是卡号的空间啊还贷款.<\/span><\/span><\/p><p style=\"text-align: left; text-indent: 0em;\"><span style=\"color: rgb(151, 72, 6);\">第二条 <span style=\"color: rgb(0, 0, 0);\">单独三点按到那说你到哪说每年都是<\/span><\/span><\/p><p style=\"text-align: left; text-indent: 0em;\"><span style=\"color: rgb(151, 72, 6);\"><span style=\"color: rgb(0, 0, 0);\">1.设计数据库时间时间时间时间时间时间<\/span><\/span><\/p><p style=\"text-align: left; text-indent: 0em;\"><span style=\"color: rgb(151, 72, 6);\"><span style=\"color: rgb(0, 0, 0);\">2.的健康三就打算你们是<\/span><\/span><\/p><p style=\"text-align: left; text-indent: 0em;\"><span style=\"color: rgb(151, 72, 6);\"><span style=\"color: rgb(0, 0, 0);\">3.的那啥把代码巴萨木地板萨达的集散！<\/span><\/span><br/><\/p>"}
         * e :
         */

        private int c;
        private String m;
        private OBean o;
        private String e;

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public String getM() {
            return m;
        }

        public void setM(String m) {
            this.m = m;
        }

        public OBean getO() {
            return o;
        }

        public void setO(OBean o) {
            this.o = o;
        }

        public String getE() {
            return e;
        }

        public void setE(String e) {
            this.e = e;
        }

        public class OBean {
            /**
             * post_content : <p style="text-align: center;"><span style="color: rgb(151, 72, 6); font-size: 18px;">众筹用户须知</span></p><p style="text-indent: 2em;">数据库和的房价安徽打卡机贷款啦巨大空间大家都会就卡萨大恒科技安徽打卡机安徽的是科技爱好打卡机了撒活动空间按户口.</p><p style="text-align: left; text-indent: 0em;"><span style="color: rgb(151, 72, 6);">第一条 <span style="color: rgb(0, 0, 0);">尽快撒谎打卡机哈空间大哈空间华盛顿科技安徽萨克的哈克家和贷款家安山东矿机安徽省的科技哈空间技术很大空间和尽快撒谎开机的哈空间活动卡华盛顿科技安徽打卡机啊手机卡还贷款就安徽打卡机啊哈就是卡号的空间啊还贷款.</span></span></p><p style="text-align: left; text-indent: 0em;"><span style="color: rgb(151, 72, 6);">第二条 <span style="color: rgb(0, 0, 0);">单独三点按到那说你到哪说每年都是</span></span></p><p style="text-align: left; text-indent: 0em;"><span style="color: rgb(151, 72, 6);"><span style="color: rgb(0, 0, 0);">1.设计数据库时间时间时间时间时间时间</span></span></p><p style="text-align: left; text-indent: 0em;"><span style="color: rgb(151, 72, 6);"><span style="color: rgb(0, 0, 0);">2.的健康三就打算你们是</span></span></p><p style="text-align: left; text-indent: 0em;"><span style="color: rgb(151, 72, 6);"><span style="color: rgb(0, 0, 0);">3.的那啥把代码巴萨木地板萨达的集散！</span></span><br/></p>
             */

            private String post_content;
            private String contents;

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public String getPost_content() {
                return post_content;
            }

            public void setPost_content(String post_content) {
                this.post_content = post_content;
            }
        }
    }
}
