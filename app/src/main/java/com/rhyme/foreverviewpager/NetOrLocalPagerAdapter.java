package com.rhyme.foreverviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by rhyme on 2017/10/9.
 */

public class NetOrLocalPagerAdapter extends PagerAdapter{
    private Object[] images;
    private Context context;
    private  int placeholder;
    private  int errorholder;
    NetOrLocalPagerAdapter(Context context, Object[] images,int placeholder,int errorholder){
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
        Object item=images[position];
        if (item instanceof String || item instanceof File){
            new LoadImageAsync(imageView, placeholder, errorholder).executeOnExecutor(Executors.newCachedThreadPool(), String.valueOf(item));
        }else if (item instanceof Integer){
            imageView.setImageResource((Integer) item);
        }else if (item instanceof Bitmap){
            imageView.setImageBitmap((Bitmap) item);
        }else if (item instanceof Drawable){
            imageView.setImageDrawable((Drawable) item);
        }
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 加载图片线程
     */
    private static class LoadImageAsync extends AsyncTask<Object, Integer, Bitmap> {
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
        protected Bitmap doInBackground(Object... objects) {
            if (isCancelled()){
                return null;
            }
            if (objects[0].toString().contains("http")){//please add network premission
                String url= String.valueOf(objects[0]);
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (objects[0].toString().contains("emulated")){//please add  read and  write premissions
                File file= new File(objects[0].toString());
                try {
                    FileInputStream fis=new FileInputStream(file);
                    return BitmapFactory.decodeStream(fis);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
