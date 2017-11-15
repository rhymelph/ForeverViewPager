package com.rhyme.foreverviewpager;

import android.annotation.SuppressLint;
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * Created by rhyme on 2017/10/9.
 */

public class NetOrLocalPagerAdapter extends PagerAdapter {
    private Object[] images;
    private Context context;
    private int placeholder;
    private int errorholder;
    private int type;
    private ForeverViewPager.OnItemClickListener clickListener=null;

    NetOrLocalPagerAdapter(Context context, Object[] images, int placeholder, int errorholder, int type, ForeverViewPager.OnItemClickListener clickListener) {
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
    public Object instantiateItem(ViewGroup container, final int position) {
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
        Object item = images[position];
        if (item instanceof String || item instanceof File) {
            new LoadImageAsync(imageView, placeholder, errorholder).executeOnExecutor(Executors.newCachedThreadPool(), String.valueOf(item));
        } else if (item instanceof Integer) {
            imageView.setImageResource((Integer) item);
        } else if (item instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) item);
        } else if (item instanceof Drawable) {
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
        protected Bitmap doInBackground(Object... objects) {
            if (isCancelled()) {
                return null;
            }
            String url = String.valueOf(objects[0]);
            Bitmap bitmap=null;
            if (url.contains("http")) {//please add network premission
                bitmap=LruCacheHelper.load(url);
                if (bitmap==null){
                    bitmap=DiskLruCacheHelper.load(url);
                    if (bitmap== null) {
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                            connection.setRequestMethod("GET");
                            connection.setReadTimeout(8000);
                            connection.setConnectTimeout(8000);
                            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                            if (bitmap != null) {
                               LruCacheHelper.dump(url,bitmap);
                                DiskLruCacheHelper.dump(bitmap, url);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (url.contains("emulated")) {//please add  read and  write premissions
                File file = new File(url);
                FileInputStream fis = null;
                bitmap=LruCacheHelper.load(url);
                if (bitmap==null){
                    bitmap=DiskLruCacheHelper.load(url);
                    if (bitmap==null){
                        try {
                            fis = new FileInputStream(file);
                            if (fis.available() > 1024 * 1024) {//大于1m自动压缩
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = fis.available()/(1024*1024);
                                options.inPurgeable=true;
                                options.inInputShareable=true;
                                options.inPreferredConfig= Bitmap.Config.RGB_565;

                                bitmap = BitmapFactory.decodeFile(url, options);
                                if (bitmap != null) {
                                    LruCacheHelper.dump(url,bitmap);
                                    DiskLruCacheHelper.dump(bitmap,url);
                                }
                            } else {
                                bitmap = BitmapFactory.decodeFile(url);
                                if (bitmap != null) {
                                    LruCacheHelper.dump( url,bitmap);
                                    DiskLruCacheHelper.dump(bitmap,url);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fis != null) {
                                    fis.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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
