package com.qixiu.wanchang.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.qixiu.newoulingzhu.application.AppManager;
import com.qixiu.newoulingzhu.constant.IntentDataKeyConstant;
import com.qixiu.newoulingzhu.constant.PlatformConfigConstant;
import com.qixiu.newoulingzhu.mvp.view.activity.mine.pay.AliWeixinPayActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.select_dialog_item);
        api = WXAPIFactory.createWXAPI(this, PlatformConfigConstant.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {


    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Intent intent = new Intent(PlatformConfigConstant.PAY_ACTION);
            if (resp.errCode == Integer.valueOf(PlatformConfigConstant.WXPAY_SUCCESS)) {

                intent.putExtra(PlatformConfigConstant.WXPAY_SUCCESS_KEY, PlatformConfigConstant.WXPAY_SUCCESS);


                //成功应该处理的事情
                //自己调用自己家的接口,告诉卖东西的，老子付款成功了，赶紧把状态改成已经付款，否则他就不承认你付了钱
                startNoticeMarket(resp);
            } else if (resp.errCode == Integer.valueOf(PlatformConfigConstant.WXPAY_FAILURE)) {
                intent.putExtra(PlatformConfigConstant.WXPAY_FAILURE_KEY, PlatformConfigConstant.WXPAY_FAILURE);
            } else if (resp.errCode == Integer.valueOf(PlatformConfigConstant.WXPAY_CANCEL)) {
                intent.putExtra(PlatformConfigConstant.WXPAY_CANCEL_KEY, PlatformConfigConstant.WXPAY_CANCEL);
            }
            sendOrderedBroadcast(intent, null);
        }
        finish();
    }

    private void startNoticeMarket(BaseResp resp) {
        Intent intent =new Intent();
        intent.setAction(IntentDataKeyConstant.BROADCAST_WECHAT_PAY_OK);
        sendBroadcast(intent);
        try {
            AppManager.getAppManager().finishActivity(AliWeixinPayActivity.class);
        }catch (Exception e){

        }
    }

}