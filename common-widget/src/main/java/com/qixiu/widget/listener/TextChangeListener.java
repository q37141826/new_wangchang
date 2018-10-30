package com.qixiu.widget.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Long on 2016/11/14
 * <br/>
 * EditText 输入监听
 *
 * @see android.widget.EditText#addTextChangedListener(TextWatcher)
 */
public class TextChangeListener implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
