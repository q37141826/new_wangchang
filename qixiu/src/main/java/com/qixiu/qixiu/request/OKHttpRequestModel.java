package com.qixiu.qixiu.request;

import android.text.TextUtils;

import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.parameter.OKHttpRequestParameter;
import com.qixiu.qixiu.request.parameter.SplitStringUtils;
import com.qixiu.qixiu.request.parameter.StringConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class OKHttpRequestModel<T> {
    public OKHttpUIUpdataListener mOkHttpUIUpdataListener;

    public OKHttpRequestModel(OKHttpUIUpdataListener<T> okHttpUIUpdataListener) {


        this.mOkHttpUIUpdataListener = okHttpUIUpdataListener;
    }

    public OKHttpRequestModel() {
    }

    public void okhHttpPost(String url, Map<String, String> map, final BaseBean<T> baseBean, Map<String, File> mapFiles) {


//        if (mapFiles != null && mapFiles.size() > 0) {
//            for (String key : mapFiles.keySet()) {
//                builder.addFile(key, mapFiles.get(key).getName(), mapFiles.get(key));
//
//            }
//        }
        okhHttpPost(url, map, baseBean, mapFiles, false);


    }

    public void okhHttpPost(String url, Map<String, String> map, final BaseBean<T> baseBean, List<Map<String, File>> mapFiles) {


//        if (mapFiles != null && mapFiles.size() > 0) {
//            for (String key : mapFiles.keySet()) {
//                builder.addFile(key, mapFiles.get(key).getName(), mapFiles.get(key));
//
//            }
//        }
        okhHttpPost(url, map, baseBean, mapFiles, false);


    }

    public void okhHttpPost(String url, Map<String, String> map, final BaseBean<T> baseBean, Map<String, File> mapFiles, boolean isToken) {

        Map<String, String> paramenterStringMap = new HashMap<>();
        if (map != null) {
            paramenterStringMap.putAll(map);
        }


        baseBean.setUrl(url);
        OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(OKHttpRequestParameter.addFilesParameter(OkHttpUtils.post().url(url), mapFiles), paramenterStringMap), mOkHttpUIUpdataListener);

    }

    public void okhHttpPost(String url, Map<String, String> map, final BaseBean<T> baseBean, List<Map<String, File>> files, boolean isToken) {
        Map<String, String> paramenterStringMap = new HashMap<>();
        if (map != null) {
            paramenterStringMap.putAll(map);
        }

        baseBean.setUrl(url);
        OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(OKHttpRequestParameter.addFilesParameter(OkHttpUtils.post().url(url), files), paramenterStringMap), mOkHttpUIUpdataListener);

    }


    public void okHttpGet(String url, Map<String, String> stringMap, final BaseBean<T> baseBean) {

        okHttpGet(url, stringMap, baseBean, false);
    }

    public void okHttpGet(String url, Map<String, String> stringMap, final BaseBean<T> baseBean, boolean isToken) {

        Map<String, String> paramenterStringMap = new HashMap<>();
        if (stringMap != null) {
            paramenterStringMap.putAll(stringMap);
        }
        GetBuilder getBuilder = OkHttpUtils.get().url(url);

        baseBean.setUrl(url);
        OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(getBuilder, paramenterStringMap), mOkHttpUIUpdataListener);


    }

    public void okhHttpPost(String url, Map<String, String> map, final BaseBean<T> baseBean) {

        okhHttpPost(url, map, baseBean, true);

    }

    public void okhHttpPost(String url, Map<String, String> map, final BaseBean<T> baseBean, boolean isToken) {
        Map<String, String> paramenterStringMap = new HashMap<>();
        if (map != null) {
            paramenterStringMap.putAll(map);
        }
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (isToken && !TextUtils.isEmpty(url)) {
            if(url.contains("&")){
                paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult01(url, "&")));
            }else {
                paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult(url, "/")));
            }
            paramenterStringMap.put("token_type", StringConstants.STRING_2);
        } else {

        }
        baseBean.setUrl(url);
        OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(builder, paramenterStringMap), mOkHttpUIUpdataListener);
    }


}
