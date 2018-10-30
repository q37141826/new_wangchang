package com.hyphenate.easeui.widget.chatrow;

import android.text.TextUtils;

import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.model.EaseDefaultEmojiconDatas;

/**
 * Created by my on 2018/9/27.
 */

public class EmojiParse {


    public static int isHaveThis(String text) {
        if(TextUtils.isEmpty(text)){
            return 0;
        }
        EaseEmojicon[] emojis = EaseDefaultEmojiconDatas.getData();
        for (int i = 0; i < emojis.length; i++) {
            if (text.contains(emojis[i].getEmojiText())) {
                return emojis[i].getIcon();
            }
        }
        return 0;
    }

    public static String emojiCode(String text) {
        if(TextUtils.isEmpty(text)){
            return "";
        }
        EaseEmojicon[] emojis = EaseDefaultEmojiconDatas.getData();
        for (int i = 0; i < emojis.length; i++) {
            if (text.contains(emojis[i].getEmojiText())) {
                return emojis[i].getEmojiText();
            }
        }
        return "";
    }
}
