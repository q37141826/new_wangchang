package com.qixiu.wigit.picker;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiu.qixiu.R;

import java.util.List;

/**
 * Created by HuiHeZe on 2017/9/6.
 */

public class MyPopOneListPicker implements View.OnClickListener {
private Pop_selectedListenner finishiCall;
    private MyPopOneListPicker.Pop_itemSelectListener call;
    private Context context;
    private View contentView;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout_back_for_dismiss;
    private TextView textView_left_pop_picker, textView_right_pop_picker;
    private PickerView pickview_time_left, pickview_time_right;
    private String result = "", leftText = "";
    private TextView textView_picker_title;
    public MyPopOneListPicker(Context context, List<SelectedDataBean> listLeft, final MyPopOneListPicker.Pop_itemSelectListener call) {
        this.call = call;
        this.context = context;
        setAllView();
        setClick();
        pickview_time_left.setData(listLeft);
        pickview_time_left.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(SelectedDataBean bean) {
                call.getRightSelectData(bean);
            }
        });
        leftText = listLeft.get(0).getText();
    }
    public MyPopOneListPicker(Context context, List<SelectedDataBean> listLeft, Pop_selectedListenner finishiCall) {
        this.finishiCall=finishiCall;
        this.context = context;
        setAllView();
        setClick();
        pickview_time_left.setData(listLeft);
        /*pickview_time_left.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(SelectedDataBean bean) {
                call.getRightSelectData(bean);
            }
        });*/
        leftText = listLeft.get(0).getText();
    }

    private void setClick() {
        textView_left_pop_picker.setOnClickListener(this);
        textView_right_pop_picker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.textView_left_pop_picker) {
            dismiss();

        } else if (i == R.id.textView_right_pop_picker) {
            if (finishiCall != null) {
                finishiCall.getData(pickview_time_left.getmDataList().get(pickview_time_left.getmCurrentSelected()));
            }
            dismiss();

        }
    }

    public void setFinishiCall(Pop_selectedListenner finishiCall) {
        this.finishiCall = finishiCall;
    }

    public interface  Pop_selectedListenner{
        void  getData(SelectedDataBean data);
    }

    public interface Pop_itemSelectListener {
        void getRightSelectData(SelectedDataBean data);
    }

    private void setAllView() {
        contentView = View.inflate(context, R.layout.pop_picker_one_list, null);
        textView_picker_title= (TextView) contentView.findViewById(R.id.textView_picker_title);
        relativeLayout_back_for_dismiss = (RelativeLayout) contentView.findViewById(R.id.relativeLayout_back_for_dismiss);
        textView_left_pop_picker = (TextView) contentView.findViewById(R.id.textView_left_pop_picker);
        textView_right_pop_picker = (TextView) contentView.findViewById(R.id.textView_right_pop_picker);
        pickview_time_left = (PickerView) contentView.findViewById(R.id.pickview_time_left);
        setPop();
        showAtLocation(Gravity.CENTER, 0, 0);
        //设置一个空的监听事件，抢夺左侧的界面焦点

    }

    private void setPop() {
        //根据字数计算每个条目的长度
        popupWindow = new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);

        relativeLayout_back_for_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setClippingEnabled(false);
    }


    public void show() {
        showAtLocation(Gravity.CENTER, 0, 0);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void showAtLocation(int gravity, int x, int y) {
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(contentView, gravity, x, y);
        }
    }

    public  void  setTitle(String title){
        textView_picker_title.setText(title);
    }
    public boolean isShowing(){
        return popupWindow.isShowing();
    }
}
