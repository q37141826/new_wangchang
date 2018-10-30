package com.qixiu.newoulingzhu.mvp.view.fragment.home.caculator;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qixiu.wanchang.R;
import com.qixiu.wigit.myedittext.MyEditTextView;

/**
 * Created by my on 2018/9/10.
 */

public class PopInput {
    PopupWindow popupWindow;
    private View contentView;
    private TextView textViewPopInputTitle,textViewCancleInput,textViewConfirmInput;
    private MyEditTextView edittextInputPop;
    ViewGroup layoutFather;



    public PopInput(Context context, String title, ClickListenner listenner) {
        contentView = View.inflate(context, R.layout.item_popinput, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setClippingEnabled(false);
        textViewPopInputTitle=contentView.findViewById(R.id.textViewPopInputTitle);
        textViewCancleInput=contentView.findViewById(R.id.textViewCancleInput);
        textViewConfirmInput=contentView.findViewById(R.id.textViewConfirmInput);
        edittextInputPop=contentView.findViewById(R.id.edittextInputPop);
        layoutFather=contentView.findViewById(R.id.layoutFather);
        textViewCancleInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenner!=null){
                    listenner.cancle();
                }
                popupWindow.dismiss();
            }
        });
        textViewPopInputTitle.setText(title);
        textViewConfirmInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenner!=null){
                    listenner.confirm(edittextInputPop.getText().toString());
                }
                dismiss();
            }
        });
        layoutFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public void show() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        }
    }

    public void setHint(String hint){
        edittextInputPop.setHint(hint);
    }
    public void setHint(int  res){
        edittextInputPop.setHint(res);
    }

    public interface ClickListenner{
        void cancle();
        void confirm(String input);
    }

}
