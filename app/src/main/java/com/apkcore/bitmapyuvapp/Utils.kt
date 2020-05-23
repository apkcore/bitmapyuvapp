package com.apkcore.bitmapyuvapp

import android.graphics.*
import java.io.ByteArrayOutputStream

/**
 * @name BitmapYuvApp
 * @class name：com.apkcore.bitmapyuvapp
 * @class describe
 * @author Lenovo
 * @time 2020/5/23 16:34
 * @change describe

 * @chang time
 */
class Utils {

    companion object {
        @JvmStatic
        fun yuv2Bitmap(nv21: ByteArray, w: Int, h: Int): Bitmap? {
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, w, h, null)
            val outputStream = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, w, h), 100, outputStream)
            val array: ByteArray = outputStream.toByteArray()
            return BitmapFactory.decodeByteArray(array, 0, array.size)
        }
    }

}