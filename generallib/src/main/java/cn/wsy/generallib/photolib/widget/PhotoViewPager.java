package cn.wsy.generallib.photolib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by wsy on 2016/1/18.
 */
public class PhotoViewPager extends ViewPager {

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        try {
            return super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("qh", "onInterceptTouch 缩放错误 getX超出");
            return true;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        try {
            return super.onTouchEvent(arg0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("qh", "onTouch 缩放错误 getX超出");
            return true;
        }
    }
}
