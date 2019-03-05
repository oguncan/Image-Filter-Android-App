package com.example.joousope.first.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class NonSwipeableViewPage extends ViewPager {
    //Ctrl+O
    ViewPager viewpager;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public NonSwipeableViewPage(@NonNull Context context) {
        super(context);
        setMyScroller();
    }

    private void setMyScroller() {
        try{
            Class<?> viewpage =ViewPager.class;
            Field scroller = viewpage.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this,new MyScroller(getContext()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NonSwipeableViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    public class MyScroller extends Scroller{
        public MyScroller(Context context) {
            super(context,new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy,duration=350);
        }
    }
}
