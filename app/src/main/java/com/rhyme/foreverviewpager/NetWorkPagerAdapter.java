package com.rhyme.foreverviewpager;

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
 * 网络加载图片
 */

public class NetWorkPagerAdapter extends PagerAdapter{
    private String[] images;
    private Context context;
    private  int placeholder;
    private  int errorholder;
    NetWorkPagerAdapter(Context context, String[] images,   int placeholder,   int errorholder){
        this.images=images;
        this.context=context;
        this.errorholder=errorholder;
        this.placeholder=placeholder;
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
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
            if (isCancelled()){
                return null;
            }
            if (DiskLruCacheHelper.load(strings[0])==null){
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    Bitmap bitmap=BitmapFactory.decodeStream(connection.getInputStream());
                    if (bitmap!=null){
                        DiskLruCacheHelper.dump(bitmap,strings[0]);
                    }
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                return DiskLruCacheHelper.load(strings[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (isCancelled()){
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
