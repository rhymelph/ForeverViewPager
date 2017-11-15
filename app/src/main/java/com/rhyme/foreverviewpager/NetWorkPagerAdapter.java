package com.rhyme.foreverviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by rhyme on 2017/9/30.
 * 轮播网络加载图片
 */

public class NetWorkPagerAdapter extends PagerAdapter {
    private String[] images;
    private Context context;
    private int placeholder;
    private int errorholder;
    private int type;
    private ForeverViewPager.OnItemClickListener clickListener=null;
    NetWorkPagerAdapter(Context context, String[] images, int placeholder, int errorholder, int type, ForeverViewPager.OnItemClickListener clickListener) {
        this.images = images;
        this.context = context;
        this.errorholder = errorholder;
        this.placeholder = placeholder;
        this.type = type;
        this.clickListener=clickListener;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView imageView = new ImageView(context);
        switch (type) {
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
        final int pos=position-1;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener!=null){
                    clickListener.ClickItem(imageView,pos);
                }
            }
        });
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(imageView);
        new LoadImageAsync(imageView, placeholder, errorholder).executeOnExecutor(Executors.newCachedThreadPool(), images[position]);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 加载图片线程
     */
    private static class LoadImageAsync extends AsyncTask<String, Integer, Bitmap> {
        private int placeHolder;
        private int errorHolder;
        @SuppressLint("StaticFieldLeak")
        private ImageView image;

        private LoadImageAsync(ImageView image, int placeHolder, int errorHolder) {
            this.placeHolder = placeHolder;
            this.errorHolder = errorHolder;
            this.image = image;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            image.setImageResource(placeHolder);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            Bitmap bitmap=LruCacheHelper.load(strings[0]);
            if (bitmap==null){
                bitmap=DiskLruCacheHelper.load(strings[0]);
                if (bitmap == null) {
                    try {
                        BitmapFactory.Options options=new BitmapFactory.Options();
                        options.inPurgeable=true;
                        options.inInputShareable=true;
                        options.inPreferredConfig= Bitmap.Config.RGB_565;

                        HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        bitmap = BitmapFactory.decodeStream(connection.getInputStream(),null,options);
                        if (bitmap != null) {
//                            int bitmapSize=DigesterUtil.getBitmapSize(bitmap);
                            LruCacheHelper.dump(strings[0],bitmap);
                            DiskLruCacheHelper.dump(bitmap, strings[0]);
                        }
                        return bitmap;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (isCancelled()) {
                return;
            }
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            } else {
                image.setImageResource(errorHolder);
            }
        }
    }
}
