package com.rhyme.foreverviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by rhyme on 2017/9/30.
 * 本地加载图片
 */

public class ViewPagerAdapter extends PagerAdapter{
    private static final String TAG="ViewPagerAdapter";
    private List<View> viewList;
    private Context context;
    ViewPagerAdapter(Context context, List<View> viewList){
        this.viewList=viewList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (viewList.get(position).getTag()!=null){
            final ImageView view= (ImageView) viewList.get(position);
            final View tagView= (View) view.getTag();
            tagView.post(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(loadBitmapFromViewBySystem(tagView));

                }
            });
            container.addView(view);
            return view;
        }else {
            View view=viewList.get(position);
            container.addView(view);
            return view;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    public static Bitmap loadBitmapFromViewBySystem(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return v.getDrawingCache();
    }
}
