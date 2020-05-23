package com.apkcore.purejavalib;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Lenovo
 * @name BitmapYUV
 * @class name：com.apkcore.bitmapyuv
 * @class describe
 * @time 2020/5/23 11:17
 * @change describe
 * @chang time
 * <p>
 * https://mp.weixin.qq.com/s/S380iHfk7zyz9M2ll4u4bA
 * <p>
 * https://qiita.com/alzybaad/items/23b7a8db40f22614009d
 */
public class BitmapUtil {
    public static byte[] fetchNv21(@NonNull Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int size = w * h;
        int[] pixels = new int[size];

        //Bitmap中的像素数据将copy到pixels数组中，数组中每一个pixel都是按ARGB四个分量8位排列压缩而成的一个int值
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

        // NV21存储格式为：yyyyyyyy vuvuvuvu
        // 其中y占size，vu各占size/4

        byte[] nv21 = new byte[size * 3 / 2];
        // 全置为偶数
        int uvIndex = size;
        w &= ~1;
        h &= ~1;

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                // 第i行的第j个位置
                int yIndex = j * w + i;

                int argb = pixels[yIndex];
//                int a = (argb >> 24) & 0xff;  // unused
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = argb & 0xff;

                int y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                y = Math.max(0, Math.min(255, y));
                nv21[yIndex] = (byte) y;

                if ((i & 0x01) == 0 && (j & 0x01) == 0) {
                    int u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                    int v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                    v = Math.max(0, Math.min(255, v));
                    u = Math.max(0, Math.min(255, u));

                    // 前size长全部是y，后面都是v和u紧挨着并且各一半
                    nv21[uvIndex++] = (byte) v;
                    nv21[uvIndex++] = (byte) u;
                }
            }
        }
//        for (int i = 0; i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                // 第i行的第j个位置
//                int yIndex = i * w + j;
//
//                int argb = pixels[yIndex];
////                int a = (argb >> 24) & 0xff;  // unused
//                int r = (argb >> 16) & 0xff;
//                int g = (argb >> 8) & 0xff;
//                int b = argb & 0xff;
//
//                int y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
//                y = MathUtils.clamp(y, 16, 255);
//                nv21[yIndex] = (byte) y;
//
//                if ((i & 0x01) == 0 && (j & 0x01) == 0) {
//                    int u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
//                    int v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
//
//                    u = MathUtils.clamp(u, 0, 255);
//                    v = MathUtils.clamp(v, 0, 255);
//
//                    // 前size长全部是y，后面都是v和u紧挨着并且各一半
//                    nv21[size++] = (byte) v;
//                    nv21[size++] = (byte) u;
//
//                    //也就是w的for循环走一遍，
////                    nv21[size + i / 2 * w + j] = (byte) v;
////                    nv21[size + i / 2 * w + j + 1] = (byte) u;
//                }
//            }
//        }
        return nv21;
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

        rgbYuv(array, w, h, nv21);

        return nv21;
    }

    public static void rgbYuv(byte[] rgba, int width, int height, byte[] yuv) {
        int rgbIndex = 0;
        int yIndex = 0;
        int uvIndex = width * height;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                final int r = rgba[rgbIndex] & 0xFF;
                final int g = rgba[rgbIndex + 1] & 0xFF;
                final int b = rgba[rgbIndex + 2] & 0xFF;

                final int y = (int) (0.257 * r + 0.504 * g + 0.098 * b + 16);
                final int u = (int) (-0.148 * r - 0.291 * g + 0.439 * b + 128);
                final int v = (int) (0.439 * r - 0.368 * g - 0.071 * b + 128);

                yuv[yIndex++] = (byte) Math.max(0, Math.min(255, y));
                if ((i & 0x01) == 0 && (j & 0x01) == 0) {
                    yuv[uvIndex++] = (byte) Math.max(0, Math.min(255, v));
                    yuv[uvIndex++] = (byte) Math.max(0, Math.min(255, u));
                }

                rgbIndex += 4;
            }
        }
    }
}
