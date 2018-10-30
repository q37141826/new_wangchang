package com.qixiu.newoulingzhu.mvp.view.activity.mine;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiu.newoulingzhu.utils.ToastUtil;
import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.IntelligentTextWatcher;
import com.qixiu.wigit.myedittext.TextWatcherAdapterInterface;


/**
 * Created by HuiHeZe on 2017/8/8.
 */

public class MyPopForInPutAlterStyle {
    private PopupWindow popwindow;
    private EditText editText_edituser;
    private TextView textView_changetitle;
    private IntelligentTextWatcher mIntelligentTextWatcher;
    private PositiveClickListenner listenner;
    private View view;
    public MyPopForInPutAlterStyle(final Context context, String defultMessgae, final TextView textView, String title) {
         view = View.inflate(context, R.layout.popwindow_edituser2, null);
        try {
            popwindow = new PopupWindow(view);
            popwindow.setFocusable(true);
            popwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popwindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
        }
        RelativeLayout relativeLayout_pop_dismiss = (RelativeLayout) view.findViewById(R.id.relativeLayout_pop_dismiss);
        relativeLayout_pop_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
        editText_edituser = (EditText) view.findViewById(R.id.editText_edituser);
        editText_edituser.setText(defultMessgae);
        editText_edituser.setSelection(defultMessgae.length());
        textView_changetitle = (TextView) view.findViewById(R.id.textView_changetitle);
        textView_changetitle.setText(title);
        if (context instanceof TextWatcherAdapterInterface) {
            mIntelligentTextWatcher = new IntelligentTextWatcher(context,editText_edituser.getId(), (TextWatcherAdapterInterface) context);
            mIntelligentTextWatcher.setEmojiDisabled(true, editText_edituser);
        }
        editText_edituser.addTextChangedListener(mIntelligentTextWatcher);
        Button button_cancleEdit = (Button) view.findViewById(R.id.button_cancleEdit);
        Button button_confirmEdit = (Button) view.findViewById(R.id.button_confirmEdit);
        button_cancleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
        button_confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_edituser.getText().toString().length() > 20) {
                    ToastUtil.toast("输入字数不得超过20位");
                    return;
                }
                if (editText_edituser.getText().toString().trim().equals("")) {
                    ToastUtil.toast("没有传入");
                    return;
                }
                textView.setText(editText_edituser.getText());
                //万一外面要做别的事情
                if (listenner!=null) {
                    (listenner).myPopInputCall();
                }
                popwindow.dismiss();
            }
        });
    }

    public void removeTextWatcher() {
        if (mIntelligentTextWatcher != null && editText_edituser != null) {
            editText_edituser.removeTextChangedListener(mIntelligentTextWatcher);
        }
    }

    public void setPositiveClickListenner(PositiveClickListenner listenner) {
        this.listenner=listenner;
    }

    public interface PositiveClickListenner {
        void myPopInputCall();
    }
    public void show(){
        if(popwindow!=null){
            popwindow.showAtLocation(view, Gravity.CENTER, 0, 0);        }
    }
}
