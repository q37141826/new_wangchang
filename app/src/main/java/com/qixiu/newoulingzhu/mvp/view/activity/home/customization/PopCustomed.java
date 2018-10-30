package com.qixiu.newoulingzhu.mvp.view.activity.home.customization;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qixiu.newoulingzhu.mvp.view.activity.home.customization.beans.PriceBean;
import com.qixiu.wanchang.R;

/**
 * Created by my on 2018/9/5.
 */

public class PopCustomed {
    PopupWindow popupWindow;
    private View contentView;
    private  TextView textPayPopHead,textViewServicePrice,textViewModifyTimes,textViewGotoMyMember,textViewGotoPay;
    PayPopClickListenner listenner;
    ViewGroup layoutFather;


    public PopCustomed(Context context, PriceBean bean, String title, String times, PayPopClickListenner listenner) {
        contentView = View.inflate(context, R.layout.pop_customed, null);
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setClippingEnabled(false);
        textPayPopHead = contentView.findViewById(R.id.textPayPopHead);
        textViewServicePrice = contentView.findViewById(R.id.textViewServicePrice);
        textViewModifyTimes = contentView.findViewById(R.id.textViewModifyTimes);
        textViewGotoMyMember = contentView.findViewById(R.id.textViewGotoMyMember);
        textViewGotoPay = contentView.findViewById(R.id.textViewGotoPay);
        layoutFather = contentView.findViewById(R.id.layoutFather);
        textPayPopHead.setText(title);
        textViewServicePrice.setText(bean.getO().getPrice());//目前显示的是原始价格
        textViewModifyTimes.setText(times);
        textViewGotoMyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenner!=null){
                    listenner.gotoMember();
                }
            }
        });
        textViewGotoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenner!=null){
                    listenner.gotoPay();
                }
            }
        });
        layoutFather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }

    public void show() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        }
    }


    public interface PayPopClickListenner{
        void gotoMember();
        void gotoPay();
    }
}
