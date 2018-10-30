package com.qixiu.wigit.myedittext;

import android.text.Editable;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public interface TextWatcherAdapterInterface {
    void beforeTextChanged(int editTextId, CharSequence s, int start, int count, int after);


    void onTextChanged(int editTextId, CharSequence s, int start, int before, int count);


    void afterTextChanged(int editTextId, Editable s);
}
