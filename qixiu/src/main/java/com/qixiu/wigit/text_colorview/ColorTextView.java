package com.qixiu.wigit.text_colorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by my on 2018/9/20.
 */

public class ColorTextView extends android.support.v7.widget.AppCompatTextView {

    public ColorTextView(Context context) {
        super(context);
    }



    public  void  setTextColor(List<TextAndColor> text){

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    /**
     * 绘制Text
     * @param canvas
     * @param paint
     * @param start
     * @param end
     */
    private void drawText(Canvas canvas, Paint paint, int start, int end) {
        canvas.save();
        // 绘制不变色
        Rect rect = new Rect(start, 0, end, getHeight());
        canvas.clipRect(rect);
        String text = getText().toString();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        // 获取字体的宽度
        int x = getWidth() / 2 - bounds.width() / 2;
        // 基线baseLine
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        canvas.drawText(text, x, baseLine, paint);// 这么画其实还是只有一种颜色
        canvas.restore();
    }




    public class TextAndColor{
        private int corlor;
        private String text;

        public TextAndColor(int corlor, String text) {
            this.corlor = corlor;
            this.text = text;
        }
    }
}
