package com.qixiu.qixiu.request;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/20.
 */
public class MD5Util {


    public final static String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            // 获得指定摘要算法的 MessageDigest对象，此处为MD5
            //MessageDigest类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //System.out.println(mdInst);
            //MD5 Message Digest from SUN, <initialized>

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);
            //System.out.println(mdInst);
            //MD5 Message Digest from SUN, <in progress>

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();
            //System.out.println(md);

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            //System.out.println(j);
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }

            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getToken(String Tag) {
        String time = "";
        Time t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int minute = t.minute;
        int hour = t.hour;
        String months = month + "";
        String days = day + "";
        if (month < 10) {
            months = 0 + "" + months;
        }
        if (day < 10) {
            days=0+""+day;
        }
        if(hour<10){
            time = "" + year + months + days + "0"+hour;
        }else {
            time = "" + year + months + days + hour;
        }

        String sentens = Tag + time + "wclsecret8532532jiaw3252jh32532532";
        String changgeSentences = sentens.toLowerCase();
        String token = md5(changgeSentences);//MD5加密字符串
        int len = token.length();
        Log.e("lenth", len + "");
        return token;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
