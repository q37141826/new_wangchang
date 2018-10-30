package com.qixiu.wigit.myedittext;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class IntelligentTextWatcher implements TextWatcher {

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    private final int editTextId;
    private final TextWatcherAdapterInterface textWatcherAdapterInterface;
    private boolean isEmojiEnable;
    private EditText editText;
    private Context context;

    public IntelligentTextWatcher(Context context,int editTextId, TextWatcherAdapterInterface textWatcherAdapterInterface) {
        this.editTextId = editTextId;
        this.context=context;
        this.textWatcherAdapterInterface = textWatcherAdapterInterface;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textWatcherAdapterInterface != null) {
            textWatcherAdapterInterface.beforeTextChanged(editTextId, s, start, count, after);
            if (isEmojiEnable && editText != null && !resetText) {
                cursorPos = editText.getSelectionEnd();
                // 这里用s.toString()而不直接用s是因为如果用s，
                // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                // inputAfterText也就改变了，那么表情过滤就失败了
                inputAfterText = s.toString();
            }


        }

    }

    public void setEmojiDisabled(boolean enable, EditText editText) {
        this.isEmojiEnable = enable;
        this.editText = editText;

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            if (textWatcherAdapterInterface != null) {
                textWatcherAdapterInterface.onTextChanged(editTextId, s, start, before, count);

                if (isEmojiEnable && editText != null && !resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (containsEmoji(input.toString())) {
                            resetText = true;
                            Toast.makeText(context, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            editText.setText(inputAfterText);
                            CharSequence text = editText.getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                        }
                    }
                } else {
                    resetText = false;
                }

            }
        } catch (Exception e) {
        }

    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (textWatcherAdapterInterface != null) {
            textWatcherAdapterInterface.afterTextChanged(editTextId, s);
        }
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }


    /**
     * 正则表达式
     *
     * @param string
     * @return
     */
    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
