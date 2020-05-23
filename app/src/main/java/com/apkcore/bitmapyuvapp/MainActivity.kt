package com.apkcore.bitmapyuvapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apkcore.jnilib.BitmapNative
import com.apkcore.purejavalib.BitmapUtil
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 参考https://qiita.com/alzybaad/items/23b7a8db40f22614009d
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        sample_text.text = stringFromJNI()
        val bm = BitmapFactory.decodeResource(resources, R.drawable.abc)

        getBmFromJni(bm)?.let {
            sampleImg.setImageBitmap(it)
        }
    }

    // 使用纯java获取
    fun getBmFromJava(bm: Bitmap): Bitmap? {
        val w = bm.width
        val h = bm.height
        val nv21 = BitmapUtil.fetchNV21(bm)
        bm.recycle()
        return nv21?.let {
            Utils.yuv2Bitmap(it, w, h)
        }
    }

    // 使用C获取
    fun getBmFromJni(bm: Bitmap): Bitmap? {
        val w = bm.width
        val h = bm.height
        val nv21 = BitmapNative.fetchNV21(bm)
        bm.recycle()
        return nv21?.let {
            Utils.yuv2Bitmap(it, w, h)
        }
    }
//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    external fun stringFromJNI(): String
//
//    companion object {
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }
}
