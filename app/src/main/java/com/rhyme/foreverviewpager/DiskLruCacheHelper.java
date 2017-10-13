package com.rhyme.foreverviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/10/8.
 */
public class DiskLruCacheHelper {

    private final static String TAG = "DiskLruCacheHelper";

    private DiskLruCacheHelper() {
    }

    private static DiskLruCache mCache;

    /**
     * 打开DiskLruCache
     */
    public static void openCache(Context context, int appVersion, int maxSize) {
        if (mCache == null) {
            try {
                mCache = DiskLruCache.open(getDirectory(context, "image"), appVersion, 1, maxSize);
            } catch (IOException e) {
                Log.e(TAG, "open disk lru cache fail");
            }
        }
    }

    public static void openCache(DiskLruCache mCache) {
        DiskLruCacheHelper.mCache = mCache;
    }

    /**
     * 获取缓存文件目录
     */
    private static File getDirectory(Context context, String cache_type) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.isExternalStorageRemovable()) {//擁有sd卡
            file = new File(context.getExternalCacheDir(), cache_type);
        } else {//沒有sd卡
            file = new File(context.getCacheDir(), cache_type);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 写出缓存
     */
    public static void dump(Bitmap bitmap, String keyCache) throws IOException {
        if (mCache == null) {
            return;
        }
        DiskLruCache.Editor editor = mCache.edit(DigesterUtil.hashUp(keyCache));

        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            if (success) {
                editor.commit();
            } else {
                editor.abort();
            }
        }
    }

    /**
     * 读取缓存
     */
    public static Bitmap load(String keyCache) {
        if (mCache == null) {
            return null;
        }
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mCache.get(DigesterUtil.hashUp(keyCache));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }

        return null;
    }

    /**
     * 同步日志
     */
    public static void syncLog() {
        if (mCache == null) {
            return;
        }
        try {
            mCache.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 关闭DiskLruCache
     */
    public static void closeCache() {
        if (mCache == null) {
            return;
        }
        syncLog();
    }

    /**
     * 清空缓存 清空后需要重新创建一次
     */
    public static void clearCache() {
        if (mCache == null) {
            return;
        }
        try {
            mCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前缓存大小 byte
     */
    public static long CacheSize() {
        if (mCache == null) {
            return 0;
        }
        return mCache.size();
    }
}
