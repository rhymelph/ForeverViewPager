package com.rhyme.foreverviewpager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/10/8.
 */
public class DigesterUtil {
    /**
     * 对数据作MD5加密。
     */
    public static String hashUp(String src) {
        String hash = null;

        try {
            byte[] md5 = MessageDigest.getInstance("md5").digest(src.getBytes());

            StringBuilder builder = new StringBuilder();
            for (byte b : md5) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    builder.append('0');
                }

                builder.append(hex);
            }
            hash = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /*
     * @return 返回字节
     */
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static Bitmap qualityCompress(int quality, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
}
