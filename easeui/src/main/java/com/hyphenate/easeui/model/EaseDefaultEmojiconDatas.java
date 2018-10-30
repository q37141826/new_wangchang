package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseDefaultEmojiconDatas {
    //这里可以替换做自定义表情
    private static String[] emojis = new String[]{
        EaseSmileUtils.ee_1,
        EaseSmileUtils.ee_2,
        EaseSmileUtils.ee_3,
        EaseSmileUtils.ee_4,
        EaseSmileUtils.ee_5,
        EaseSmileUtils.ee_6,
        EaseSmileUtils.ee_7,
        EaseSmileUtils.ee_8,
        EaseSmileUtils.ee_9,
        EaseSmileUtils.ee_10,
        EaseSmileUtils.ee_11,
        EaseSmileUtils.ee_12,
        EaseSmileUtils.ee_13,
        EaseSmileUtils.ee_14,
        EaseSmileUtils.ee_15,
        EaseSmileUtils.ee_16,
        EaseSmileUtils.ee_17,
        EaseSmileUtils.ee_18,
        EaseSmileUtils.ee_19,
        EaseSmileUtils.ee_20,
        EaseSmileUtils.ee_21,
        EaseSmileUtils.ee_22,
        EaseSmileUtils.ee_23,
        EaseSmileUtils.ee_24,
//        EaseSmileUtils.ee_25,
//        EaseSmileUtils.ee_26,
//        EaseSmileUtils.ee_27,
//        EaseSmileUtils.ee_28,
//        EaseSmileUtils.ee_29,
//        EaseSmileUtils.ee_30,
//        EaseSmileUtils.ee_31,
//        EaseSmileUtils.ee_32,
//        EaseSmileUtils.ee_33,
//        EaseSmileUtils.ee_34,
//        EaseSmileUtils.ee_35,
       
    };
    
    private static int[] icons = new int[]{
        R.drawable.new_ee_no,
        R.drawable.new_ee_ok,
        R.drawable.new_ee_anzhongguancha,
        R.drawable.new_ee_baonu,
        R.drawable.new_ee_bixin,
        R.drawable.new_ee_chongbai,
        R.drawable.new_ee_dazhaohu,
        R.drawable.new_ee_daku,
        R.drawable.new_ee_dianzan,
        R.drawable.new_ee_angry,
        R.drawable.new_ee_hanyan,
        R.drawable.new_ee_haohuang,
        R.drawable.new_ee_jiayou,
        R.drawable.new_ee_jingya,
        R.drawable.new_ee_kun,
        R.drawable.new_ee_sujing,
        R.drawable.new_ee_touxiao,
        R.drawable.new_ee_tuxue,
        R.drawable.new_ee_wa,
        R.drawable.new_ee_wolaile,
        R.drawable.new_ee_wozoule,
        R.drawable.new_ee_wuyu,
        R.drawable.new_ee_yiwen,
        R.drawable.new_ee_yun,
//        R.drawable.ee_25,
//        R.drawable.ee_26,
//        R.drawable.ee_27,
//        R.drawable.ee_28,
//        R.drawable.ee_29,
//        R.drawable.ee_30,
//        R.drawable.ee_31,
//        R.drawable.ee_32,
//        R.drawable.ee_33,
//        R.drawable.ee_34,
//        R.drawable.ee_35,
    };
    
    
    private static final EaseEmojicon[] DATA = createData();
    
    private static EaseEmojicon[] createData(){
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.NORMAL);
        }
        return datas;
    }
    
    public static EaseEmojicon[] getData(){
        return DATA;
    }
}
