package com.rhyme.foreverviewpager;

import android.content.Context;

/**
 * Created by rhyme on 2017/10/13.
 * 轮播图缓存
 */

public class ForeverCache {
    private static final int LOCAL_CACHE_SIZE_LIMIT = 10 * 1024 * 1024;

    /**
     * 新建一个缓存
     */
    public static void initCache(Context context, int appVersion) {
        DiskLruCacheHelper.openCache(context, appVersion, LOCAL_CACHE_SIZE_LIMIT);
    }

    /**
     * 自己应用缓存
     */
    public static void initCache(DiskLruCache diskLruCache) {
        DiskLruCacheHelper.openCache(diskLruCache);
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        DiskLruCacheHelper.clearCache();
    }

    /**
     * 同步日志
     */
    public static void syncLog() {
        DiskLruCacheHelper.syncLog();
    }

    /**
     * 获取当前缓存大小 byte
     */
    public static long CacheSize() {
        return DiskLruCacheHelper.CacheSize();
    }
}
