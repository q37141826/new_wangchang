package com.qixiu.wigit.five_star;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qixiu.qixiu.R;


/**
 * Created by my on 2018/6/6.
 */

public class FiveStarView extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {
    private ImageView imageView_star01, imageView_star02, imageView_star03,
            imageView_star04, imageView_star05;
    private LinearLayout linearlayout_father;
    private int selctedResource, notSelectedResorce;

    private int starNum = 0;
    private SelectedListenner listenner;

    public SelectedListenner getListenner() {
        return listenner;
    }

    public void setListenner(SelectedListenner listenner) {
        this.listenner = listenner;
    }

    public int getSelctedResource() {
        return selctedResource;
    }

    public void setSelctedResource(int selctedResource) {
        this.selctedResource = selctedResource;
    }

    public int getNotSelectedResorce() {
        return notSelectedResorce;
    }

    public void setNotSelectedResorce(int notSelectedResorce) {
        this.notSelectedResorce = notSelectedResorce;
    }

    public FiveStarView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.five_star, this);
        imageView_star01 = (ImageView) view.findViewById(R.id.imageView_star01);
        imageView_star02 = (ImageView) view.findViewById(R.id.imageView_star02);
        imageView_star03 = (ImageView) view.findViewById(R.id.imageView_star03);
        imageView_star04 = (ImageView) view.findViewById(R.id.imageView_star04);
        imageView_star05 = (ImageView) view.findViewById(R.id.imageView_star05);
        linearlayout_father = (LinearLayout) view.findViewById(R.id.linearlayout_father);

        //增加xml里面的可编辑属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FiveStar);
        selctedResource = array.getResourceId(R.styleable.FiveStar_selected_image, 0);
        notSelectedResorce = array.getResourceId(R.styleable.FiveStar_not_selected_image, 0);
        starNum = array.getInt(R.styleable.FiveStar_starNum, 0);
//        imageView_star01.setOnClickListener(this);
//        imageView_star02.setOnClickListener(this);
//        imageView_star03.setOnClickListener(this);
//        imageView_star04.setOnClickListener(this);
//        imageView_star05.setOnClickListener(this);
        setState();
        array.recycle();
        linearlayout_father.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.imageView_star01:
//                starNum = 1;
//                break;
//            case R.id.imageView_star02:
//                starNum = 2;
//                break;
//            case R.id.imageView_star03:
//                starNum = 3;
//                break;
//            case R.id.imageView_star04:
//                starNum = 4;
//                break;
//            case R.id.imageView_star05:
//                starNum = 5;
//                break;
        }
        setState();
    }

    private void setState() {
        for (int i = 0; i < linearlayout_father.getChildCount(); i++) {
            if (linearlayout_father.getChildAt(i) instanceof ImageView) {
                ImageView imageView = (ImageView) linearlayout_father.getChildAt(i);
                imageView.setImageResource(notSelectedResorce);
            }
        }
        for (int i = 0; i < starNum; i++) {
            if (linearlayout_father.getChildAt(i) instanceof ImageView) {
                ImageView imageView = (ImageView) linearlayout_father.getChildAt(i);
                imageView.setImageResource(selctedResource);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int width = linearlayout_father.getWidth();
        float start = linearlayout_father.getX();
        float x=0;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                 x = event.getX();
//                starNum= (int) ((x-start)*5 / width + 1);
//                if(starNum>5){
//                    starNum=5;
//                }
//                setState();
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//        }
        x=event.getX();
        starNum= (int) ((x-start)*5 / width + 1);
        if(starNum>5){
            starNum=5;
        }
        if(x<start){
            starNum=0;
        }
        setState();
        Log.e("x_",x+"");//拿起来之后就请求接口
        if(event.getAction()== MotionEvent.ACTION_UP){
            if (listenner != null) {
                listenner.seleted(starNum);
            }
        }
        return false;
    }


    public interface SelectedListenner {
        void seleted(int num);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        linearlayout_father.setEnabled(false);
    }

    public void setStarNum(int starNum){
        this.starNum=starNum;
        setState();
    }
}
