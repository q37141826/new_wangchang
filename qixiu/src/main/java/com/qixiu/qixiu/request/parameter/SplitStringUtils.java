package com.qixiu.qixiu.request.parameter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 该工具类主要功能截取服务器返回图片字符串集
 *
 * @author gyh
 * <p/>
 * 2016年3月7日 下午4:30:17
 */
public class SplitStringUtils {
    /**
     * 推荐使用StringTokenizer来进行截取，不占用内存， 性能高于split
     *
     * @param content
     * @param split
     * @return
     */
    public static List<String> startSplit(String content, String split) {
        List<String> list = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(content, split);
        while (stringTokenizer.hasMoreTokens()) {
            list.add((String) stringTokenizer.nextToken());
        }
        return list;

    }

    public static String splitJoint(String content, String split) {
        StringBuffer buffer = new StringBuffer();

        StringTokenizer stringTokenizer = new StringTokenizer(content, split);
        while (stringTokenizer.hasMoreTokens()) {
            buffer.append((String) stringTokenizer.nextToken());
        }
        return buffer.toString();


    }

    public static String cutStringEnd(String content, String stringEnd) {
        String resultString = content;
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(stringEnd)) {
            if (content.endsWith(stringEnd)) {
                resultString = content.substring(0, content.length() - 1);
            }
        }
        return resultString;
    }

    public static String cutStringPenult(String content, String target) {
        if (content == null && target == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        List<String> splitStrings = startSplit(content, target);
        int size = splitStrings.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1 || i == size - 2) {
                stringBuffer.append(splitStrings.get(i));
                if (i == size - 2) {
                    stringBuffer.append(target);
                }
            }
        }
        return stringBuffer.toString();
    }


    public static String cutStringPenult01(String content, String target) {
        if (content == null && target == null) {
            return null;
        }
        String str = content.substring(content.indexOf("m=") + 2, content.contains("type=") ? content.indexOf("type=") - 1 : content.length());
        return str.replace("m=", "").replace("a=", "").replace("&","/");
    }


    public static String replaceAllString(String[] regexs, String replacement, String content) {
        String mContent = content;
        if (!TextUtils.isEmpty(content)) {
            for (int i = 0; i < regexs.length; i++) {
                String regex = regexs[i];
                if (content.contains(regex)) {
                    mContent = mContent.replaceAll(regex, replacement);
                }
            }
        }
        return mContent;
    }


}
