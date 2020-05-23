package com.apkcore.jnilib;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Lenovo
 * @name BitmapYuvApp
 * @class name：com.apkcore.jnilib
 * @class describe
 * @time 2020/5/23 16:54
 * @change describe
 * @chang time
 */
public class BitmapNative {
    {
        System.loadLibrary("yuv-lib");
    }

    public static byte[] fetchNV21(@NonNull Bitmap bitmap) {
        // Bitmap中的像素数据将copy到buffer中，buffer中每一个pixel都是按RGBA四个分量的顺序进行排列的
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bitmap.getByteCount()).order(ByteOrder.nativeOrder());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byte[] array = byteBuffer.array();

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int area = w * h;

        int nv21Size = area * 3 / 2;
        byte[] nv21 = new byte[nv21Size];

        BitmapNative.rgbToYuv(array, w, h, nv21);

        return nv21;
    }

    public native static void rgbToYuv(byte[] rgba, int w, int h, byte[] nv21);
}
