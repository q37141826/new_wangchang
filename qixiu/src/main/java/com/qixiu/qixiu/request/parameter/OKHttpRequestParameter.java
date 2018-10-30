package com.qixiu.qixiu.request.parameter;

import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OkHttpRequestBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class OKHttpRequestParameter {

    public static PostFormBuilder addFilesParameter(PostFormBuilder postFormBuilder, Map<String, File> mapFiles) {
        if (mapFiles != null && mapFiles.size() > 0) {
            for (String key : mapFiles.keySet()) {
                postFormBuilder.addFile(key, mapFiles.get(key).getName(), mapFiles.get(key));

            }
        }
        return postFormBuilder;
    }
    public static PostFormBuilder addFilesParameter(PostFormBuilder postFormBuilder, List<Map<String, File>> files) {
        if (files != null && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                Map<String, File> map;
                map = files.get(i);
                for (String key : map.keySet()) {
                    postFormBuilder.addFile(key, map.get(key).getName(), map.get(key));
                }
            }
        }
        return postFormBuilder;
    }

    public static OkHttpRequestBuilder addStringParameter(OkHttpRequestBuilder okHttpRequestBuilder, Map<String, String> mapString) {
        GetBuilder getBuilder = null;
        PostFormBuilder postFormBuilder = null;
        if (okHttpRequestBuilder instanceof GetBuilder) {
            getBuilder = (GetBuilder) okHttpRequestBuilder;
        } else {
            postFormBuilder = (PostFormBuilder) okHttpRequestBuilder;
        }
        if (mapString != null && mapString.size() > 0) {
            for (String key : mapString.keySet()) {
                if (getBuilder != null) {
                    getBuilder.addParams(key, mapString.get(key));
                } else if (postFormBuilder != null) {
                    postFormBuilder.addParams(key, mapString.get(key));
                }
            }
        }
        return okHttpRequestBuilder;
    }
}
