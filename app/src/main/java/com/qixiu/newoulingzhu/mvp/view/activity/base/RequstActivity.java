package com.qixiu.newoulingzhu.mvp.view.activity.base;

import com.qixiu.newoulingzhu.mvp.wight.loading.ZProgressHUD;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.bean.C_CodeBean;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by my on 2018/8/23.
 */

public abstract class RequstActivity extends TitleActivity implements OKHttpUIUpdataListener<BaseBean> {
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);
    private ZProgressHUD zProgressHUD;


    public void post(String url, Map map, BaseBean bean) {
        okHttpRequestModel.okhHttpPost(url, map, bean);
        zProgressHUD.show();

    }

    public void get(String url, Map map, BaseBean bean) {
        okHttpRequestModel.okHttpGet(url, map, bean);
        zProgressHUD.show();
    }

    @Override
    protected void onInitView() {
        zProgressHUD = new ZProgressHUD(getContext());
        super.onInitView();
    }

    @Override
    public void onSuccess(BaseBean data, int i) {
        zProgressHUD.dismiss();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        zProgressHUD.dismiss();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        zProgressHUD.dismiss();
    }
}
