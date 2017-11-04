package com.rhyme.foreverviewpager;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by rhyme on 2017/11/4.
 */

class LruCacheHelper {
    private static LruCache<String,Bitmap> lruCache;
    private static final long MAX_VALUE=Runtime.getRuntime().maxMemory()/8;

    public static void InitLruCache(){
        lruCache=new LruCache<String,Bitmap>((int) MAX_VALUE){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    public static synchronized void dump(String value,Bitmap bitmap){
        if (lruCache==null){
            return ;
        }
        if (value!=null&&bitmap!=null){
            lruCache.put(DigesterUtil.hashUp(value),bitmap);
        }
    }

    public static synchronized Bitmap load(String value){
        if (lruCache==null){
            return null;
        }
        if (value!=null){
            return lruCache.get(DigesterUtil.hashUp(value));
        }
        return null;
    }

    public static synchronized void clear(){
        if (lruCache==null){
            return;
        }
        lruCache.evictAll();
    }
}
