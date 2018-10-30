package com.qixiu.newoulingzhu.mvp.presenter;


import com.qixiu.newoulingzhu.beans.SendCodeBean;
import com.qixiu.qixiu.request.OKHttpRequestModel;
import com.qixiu.qixiu.request.OKHttpUIUpdataListener;

import java.util.Map;

/**
 * Created by HuiHeZe on 2017/7/21.
 */

public class SendCodePresense {
    public SendCodePresense(OKHttpUIUpdataListener okHttpUIUpdataListener, Map<String, String> map, String url) {
        new OKHttpRequestModel( okHttpUIUpdataListener).okhHttpPost(url,map,new SendCodeBean(),true);
    }

}
