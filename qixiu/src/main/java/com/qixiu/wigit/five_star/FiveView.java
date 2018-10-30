package com.qixiu.wigit.five_star;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class FiveView extends View {

    int colorResours = Color.WHITE;


    public FiveView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub  
    }

    public FiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub  
    }

    public FiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub  
    }

    @SuppressLint("NewApi")
    public FiveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub  
    }


    public void setBackColorResourse(int resourse) {
        this.colorResours = resourse;

    }


    private void addCoordinateAndMark(Canvas canvas) {
        // TODO Auto-generated method stub  


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();//获取整个控件的宽度
        float y=  getY();//顶点位置是参照坐标
        float x=getX();
        float r = width / 2 / sin(72);//计算出半径
        float a_edge = r * cos(72);            //中心点到垂直位置的距离
        float edge  =(r-a_edge)/sin(72);       //五角星外部边缘的边长
        //计算出第一个点的坐标，顶点最高为Y位置



        Paint mPaint = new Paint();
        /**
         * //Point point1(200,0),point2(390,138),point3(318,362),point4(82,362),point5(10,138);
         按1，3，5，2，4排列就是五角星的位置。
         */
        Path path = new Path();
        path.moveTo(200, 0);
        path.lineTo(318, 362);
        path.lineTo(10, 138);
        path.lineTo(390, 138);
        path.lineTo(82, 362);
        path.close();
        canvas.drawPath(path, mPaint);

    }

    /***
     * 外点 </br>
     x=Rcos(72°*k) </br>
     y=Rsin(72°*k) </br>
     k=0,1,2,3,4 </br>
     </br></br>
     内点 </br>
     r=Rsin(18)/sin(180-36-18) </br>
     x=rcos(72°*k+36°)</br>
     y=rsin(72°*k+36°) </br>
     k=0,1,2,3,4 </br>

     * @param canvas
     */
    private void ff2(Canvas canvas) {

    }

    float cos(int num) {
        return (float) Math.cos(num * Math.PI / 180);
    }

    float sin(int num) {
        return (float) Math.sin(num * Math.PI / 180);
    }
}  