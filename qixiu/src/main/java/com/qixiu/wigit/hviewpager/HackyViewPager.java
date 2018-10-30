package com.qixiu.wigit.hviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决viewpager和在它内部的控件的事件冲突
 *solve the conflict bettween  viewpager and  other  view  in it's
 * by dengpinbo
 */
public class HackyViewPager extends ViewPager {


//		// TODO Auto-generated constructor stub


	public HackyViewPager(Context context) {
		super(context);
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			return super.onTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		try {
			return super .dispatchTouchEvent(ev);
		} catch (IllegalArgumentException ignored) {
		} catch (ArrayIndexOutOfBoundsException e) {
		}catch (NullPointerException e){}
		return false ;
	}



}
