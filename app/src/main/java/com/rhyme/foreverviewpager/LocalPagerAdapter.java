package com.rhyme.foreverviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by rhyme on 2017/9/30.
 * 本地加载图片
 */

public class LocalPagerAdapter extends PagerAdapter{
    private Integer[] images;
    private Context context;
    private int type;

    LocalPagerAdapter(Context context, Integer[] images,int type){
        this.images=images;
        this.context=context;
        this.type=type;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView=new ImageView(context);
        imageView.setImageResource(images[position]);
        switch (type){
            case 0:
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case 1:
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                break;
            case 2:
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;
            case 3:
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }
}
