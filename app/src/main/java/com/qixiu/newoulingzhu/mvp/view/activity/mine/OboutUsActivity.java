package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.view.View;
import android.webkit.WebView;

import com.qixiu.newoulingzhu.constant.ConstantString;
import com.qixiu.newoulingzhu.constant.ConstantUrl;
import com.qixiu.newoulingzhu.mvp.view.activity.base.TitleActivity;
import com.qixiu.newoulingzhu.mvp.wight.my_alterdialog.ArshowContextUtil;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;
import com.qixiu.qixiu.utils.CommonUtils;
import com.qixiu.wanchang.R;

import okhttp3.Call;

public class OboutUsActivity extends TitleActivity implements OKHttpUIUpdataListener {
    private OKHttpRequestModel okHttpRequestModel;
    private WebView webview_obout_us;

    @Override
    protected void onInitData() {

        String title = getIntent().getStringExtra(ConstantString.TITLE_NAME);
        mTitleView.setTitle(title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        okHttpRequestModel = new OKHttpRequestModel(this);
        okHttpRequestModel.okHttpGet(ConstantUrl.getInfo, null, new BaseBean());

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_obout_us;
    }

    @Override
    protected void onInitViewNew() {
        webview_obout_us = (WebView) findViewById(R.id.webview_obout_us);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(Object data, int i) {
        BaseBean bean = (BaseBean) data;
        int px = 18;
        if (windowWith < 720 && windowWith > 480) {
            px = 18;
        } else if ( windowWith >= 720&&windowWith < 1080) {
            px = 20;
        }else if(windowWith >= 1080&&windowWith < 1440){
            px = 24;
        }else if(windowWith >= 1440&&windowWith <= 1600){
            px = 28;
        }else if(windowWith > 1600){
            px=36;
        }
        String html5 = bean.getO().toString().replace("14px", px+"px");
        CommonUtils.setWebview(webview_obout_us, html5, true);
        webview_obout_us.getSettings().setDefaultFontSize(ArshowContextUtil.dp2px(px));

    }

    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }
}
