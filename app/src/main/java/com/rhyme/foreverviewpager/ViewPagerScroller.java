package com.rhyme.foreverviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by rhyme on 2017/12/29.
 */

public class ViewPagerScroller extends Scroller {
    private int sliding_duration=1000;//滑动速度
    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, sliding_duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, sliding_duration);
    }

    public void initViewPagerScroll(ViewPager viewPager){
            try {
                Field mSc=ViewPager.class.getDeclaredField("mScroller");
                mSc.setAccessible(true);
                mSc.set(viewPager,this);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public void setSlidingDuration(int duration){
        sliding_duration=duration;
    }
}
