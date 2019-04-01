package com.qixiu.qixiu.request;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.qixiu.qixiu.request.bean.BaseBean;
import com.qixiu.qixiu.request.parameter.OKHttpRequestParameter;
import com.qixiu.qixiu.request.parameter.SplitStringUtils;
import com.qixiu.qixiu.request.parameter.StringConstants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

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
        okhHttpPost(url, map, baseBean, mapFiles, true);


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
        GetBuilder getBuilder = OkHttpUtils.get().url(url);
        if (isToken && !TextUtils.isEmpty(url)) {
            OkHttpUtils.get().url("http://sk.qixiuu.com/" + "/index.php?g=api&m=Base&a=time")
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {

                }

                @Override
                public void onResponse(String s, int i) {
                    Gson gson = new Gson();
                    BaseBean bean = gson.fromJson(s, BaseBean.class);
                    String time = bean.getO().toString();
                    if (url.contains("&")) {
                        paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult01(url, "&"),time));
                    } else {
                        paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult(url, "/"),time));
                    }
                    paramenterStringMap.put("token_type", StringConstants.STRING_2);
                    baseBean.setUrl(url);
                    OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(OKHttpRequestParameter.addFilesParameter(OkHttpUtils.post().url(url), mapFiles), paramenterStringMap), mOkHttpUIUpdataListener);                }
            });
        }

//        baseBean.setUrl(url);
//        OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(OKHttpRequestParameter.addFilesParameter(OkHttpUtils.post().url(url), mapFiles), paramenterStringMap), mOkHttpUIUpdataListener);

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

        okHttpGet(url, stringMap, baseBean, true);
    }

    public void okHttpGet(String url, Map<String, String> stringMap, final BaseBean<T> baseBean, boolean isToken) {

        Map<String, String> paramenterStringMap = new HashMap<>();
        if (stringMap != null) {
            paramenterStringMap.putAll(stringMap);
        }
        GetBuilder getBuilder = OkHttpUtils.get().url(url);
        if (isToken && !TextUtils.isEmpty(url)) {
            OkHttpUtils.get().url("http://sk.qixiuu.com/" + "/index.php?g=api&m=Base&a=time")
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {

                }

                @Override
                public void onResponse(String s, int i) {
                    Gson gson = new Gson();
                    BaseBean bean = gson.fromJson(s, BaseBean.class);
                    String time = bean.getO().toString();
                    if (url.contains("&")) {
                        paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult01(url, "&"),time));
                    } else {
                        paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult(url, "/"),time));
                    }
                    paramenterStringMap.put("token_type", StringConstants.STRING_2);
                    baseBean.setUrl(url);
                    OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(getBuilder, paramenterStringMap), mOkHttpUIUpdataListener);
                }
            });
        }
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
            OkHttpUtils.get().url("http://sk.qixiuu.com/" + "/index.php?g=api&m=Base&a=time")
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int i) {

                }

                @Override
                public void onResponse(String s, int i) {
                    Gson gson = new Gson();
                    BaseBean bean = gson.fromJson(s, BaseBean.class);
                    String time = bean.getO().toString();
                    if (url.contains("&")) {
                        paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult01(url, "&"),time));
                    } else {
                        paramenterStringMap.put("token", MD5Util.getToken(SplitStringUtils.cutStringPenult(url, "/"),time));
                    }
                    paramenterStringMap.put("token_type", StringConstants.STRING_2);
                    baseBean.setUrl(url);
                    OKHttpExecutor.okHttpExecut(baseBean, OKHttpRequestParameter.addStringParameter(builder, paramenterStringMap), mOkHttpUIUpdataListener);
                }
            });
        }
    }


}
