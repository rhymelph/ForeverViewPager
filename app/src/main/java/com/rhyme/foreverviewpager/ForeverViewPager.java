package com.rhyme.foreverviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhyme on 2017/9/30.
 * 有什么问题,请联系
 * QQ:708959817
 * WeiXin:bleachlph
 */

public class ForeverViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ForeverViewPager";
    private static final int ALIGN_LEFT = 1;
    private static final int ALIGN_RIGHT = 2;
    private static final int ALIGN_CENTER = 3;
    private int align = 3;
    private float dot_size = 50;
    private ViewPager view_pager;
    private LinearLayout radio_group;
    private boolean dot_visible = true;
    private boolean carousel = true;
    private int interval = 2000;
    private Drawable dot_sel;
    private Drawable dot_nor;
    private Context context;
    private int total = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            view_pager.setCurrentItem(view_pager.getCurrentItem() + 1);
            sendEmptyMessageDelayed(0, interval);
        }
    };

    public ForeverViewPager(Context context) {
        super(context);
        initView(context);
    }

    public ForeverViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForeverViewPager);
        dot_visible = a.getBoolean(R.styleable.ForeverViewPager_dot_visible, true);
        carousel = a.getBoolean(R.styleable.ForeverViewPager_carousel, true);
        int dot_select = a.getResourceId(R.styleable.ForeverViewPager_dot_select, R.drawable.ic_fiber_while);
        int dot_normal = a.getResourceId(R.styleable.ForeverViewPager_dot_normal, R.drawable.ic_fiber_black);
        align = a.getInt(R.styleable.ForeverViewPager_dot_align, ALIGN_CENTER);
        interval = a.getInt(R.styleable.ForeverViewPager_interval, 2000);
        dot_size = a.getDimension(R.styleable.ForeverViewPager_dot_size, 50);
        dot_sel = context.getResources().getDrawable(dot_select);
        dot_nor = context.getResources().getDrawable(dot_normal);
        a.recycle();
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View view = inflate(context, R.layout.forever_layout, null);
        view_pager = view.findViewById(R.id.rhy_viewpager);
        radio_group = view.findViewById(R.id.rhy_rgroup);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);
        view_pager.addOnPageChangeListener(this);
        switch (align) {
            case ALIGN_CENTER:
                radio_group.setGravity(Gravity.CENTER);
                break;
            case ALIGN_LEFT:
                radio_group.setGravity(Gravity.LEFT);
                break;
            case ALIGN_RIGHT:
                radio_group.setGravity(Gravity.RIGHT);
                break;
        }
    }

    /**
     * 载入小圆点
     */
    private void initdot(int count) {
        if (dot_visible) {
            radio_group.removeAllViews();
            total = count;
            for (int i = 0; i < total; i++) {
                View view = new View(context);
                view.setId(R.id.rhy_rgroup + 1 + i);
                if (i == 0) {
                    view.setBackgroundDrawable(dot_sel);
                } else {
                    view.setBackgroundDrawable(dot_nor);
                }
                view.setLayoutParams(new ViewGroup.LayoutParams((int) dot_size, (int) dot_size));
                radio_group.addView(view);
            }
        }
    }

    /**
     * res,File,url,Bitmap,Drawable
     */
    public void setAdapter(Object[] list, int resHolder, int resError) {
        if (list == null || list.length == 0) {
            return;
        }
        view_pager.setOffscreenPageLimit(list.length);

        initdot(list.length);

        List<Object> urls = new ArrayList<>();
        //前后添加视差
        if (total > 1) {
            Object firstView = list[0];
            Object lastView = list[list.length - 1];

            urls.add(lastView);
            for (int i = 0; i < list.length; i++) {
                urls.add(list[i]);
            }
            urls.add(firstView);

            view_pager.setAdapter(new NetOrLocalPagerAdapter(context, urls.toArray(new Object[urls.size()]), resHolder, resError));
            view_pager.setCurrentItem(1);

            Carousel();
        } else {
            view_pager.setAdapter(new NetOrLocalPagerAdapter(context, list, resHolder, resError));
        }
    }

    /**
     * 加入的是View
     */
    public void setAdapter(List<View> viewList) {
        if (viewList == null || viewList.size() == 0) {
            return;
        }
        initdot(viewList.size());
        if (total > 1) {
            ImageView first = new ImageView(context);
            first.setTag(viewList.get(0));
            first.setScaleType(ImageView.ScaleType.CENTER_CROP);
            first.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView last = new ImageView(context);
            last.setTag(viewList.get(viewList.size() - 1));
            last.setScaleType(ImageView.ScaleType.CENTER_CROP);
            last.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            viewList.add(first);
            viewList.add(0, last);
            view_pager.setOffscreenPageLimit(viewList.size());

            view_pager.setAdapter(new ViewPagerAdapter(context, viewList));
            view_pager.setCurrentItem(1);

            Carousel();
        } else {
            view_pager.setAdapter(new ViewPagerAdapter(context, viewList));
        }
    }
    /**
     * 初始化轮播
     */
    private void Carousel() {
        if (carousel) {
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, interval);
        }
    }
    /**
     * 网络图片
     */
    public void setAdapter(String[] urlList, int placeholder, int errorholder) {
        if (urlList == null || urlList.length == 0) {
            return;
        }
        view_pager.setOffscreenPageLimit(urlList.length);

        initdot(urlList.length);

        List<String> urls = new ArrayList<>();
        //前后添加视差
        if (total > 1) {
            String firstView = urlList[0];
            String lastView = urlList[urlList.length - 1];

            urls.add(lastView);
            for (int i = 0; i < urlList.length; i++) {
                urls.add(urlList[i]);
            }
            urls.add(firstView);

            view_pager.setAdapter(new NetWorkPagerAdapter(context, urls.toArray(new String[urls.size()]), placeholder, errorholder));
            view_pager.setCurrentItem(1);

            Carousel();
        } else {
            view_pager.setAdapter(new NetWorkPagerAdapter(context, urlList, placeholder, errorholder));
        }
    }

    /**
     * 本地图片
     */
    public void setAdapter(@IntegerRes int[] intList) {
        if (intList == null || intList.length == 0) {
            return;
        }
        view_pager.setOffscreenPageLimit(intList.length);

        initdot(intList.length);

        List<Integer> urls = new ArrayList<>();
        //前后添加视差
        if (total > 1) {
            int firstView = intList[0];
            int lastView = intList[intList.length - 1];

            urls.add(lastView);
            for (int i = 0; i < intList.length; i++) {
                urls.add(intList[i]);
            }
            urls.add(firstView);

            view_pager.setAdapter(new LocalPagerAdapter(context, urls.toArray(new Integer[urls.size()])));
            view_pager.setCurrentItem(1);
            Carousel();
        } else {
            for (int i = 0; i < intList.length; i++) {
                urls.add(intList[i]);
            }
            view_pager.setAdapter(new LocalPagerAdapter(context, urls.toArray(new Integer[urls.size()])));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset == 0) {
            reset();
            if (total > 1) {
                if (position == 0) {
                    view_pager.setCurrentItem(total, false);
                    radio_group.getChildAt(total - 1).setBackgroundDrawable(dot_sel);
                } else if (position == total + 1) {
                    view_pager.setCurrentItem(1, false);
                    radio_group.getChildAt(0).setBackgroundDrawable(dot_sel);
                } else {
                    radio_group.getChildAt(position - 1).setBackgroundDrawable(dot_sel);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void reset() {
        for (int i = 0; i < radio_group.getChildCount(); i++) {
            View view = radio_group.getChildAt(i);
            view.setBackgroundDrawable(dot_nor);
        }
    }

    /**
     * 取消轮播
     */
    public void stop() {
        handler.removeMessages(0);
    }

    /**
     * 重新开始
     */
    public void start() {
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0, interval);
    }
}
