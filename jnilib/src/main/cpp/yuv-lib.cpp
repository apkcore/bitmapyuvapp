#include <jni.h>
#include <string>

extern "C"
JNIEXPORT void JNICALL
Java_com_apkcore_jnilib_BitmapNative_rgbToYuv(JNIEnv *env, jobject thiz, jbyteArray rgba, jint w,
                                              jint h, jbyteArray nv21) {
    jbyte *rgb = env->GetByteArrayElements(rgba, NULL);
    jbyte *yuv = env->GetByteArrayElements(nv21, NULL);

    int rgbIndex = 0;
    int yIndex = 0;
    int uvIndex = w * h;
    for (int j = 0; j < h; ++j) {
        for (int i = 0; i < w; ++i) {
            int r = rgb[rgbIndex] & 0xFF;
            int g = rgb[rgbIndex + 1] & 0xFF;
            int b = rgb[rgbIndex + 2] & 0xFF;

            int y = (int) (0.257 * r + 0.504 * g + 0.098 * b + 16);
            int u = (int) (-0.148 * r - 0.291 * g + 0.439 * b + 128);
            int v = (int) (0.439 * r - 0.368 * g - 0.071 * b + 128);

            yuv[yIndex++] = (jbyte) (y < 0 ? 0 : y > 255 ? 255 : y);
            if ((i & 0x01) == 0 && (j & 0x01) == 0) {
                yuv[uvIndex++] = (jbyte) (v < 0 ? 0 : v > 255 ? 255 : v);
                yuv[uvIndex++] = (jbyte) (u < 0 ? 0 : u > 255 ? 255 : u);
            }

            rgbIndex += 4;
        }
    }

    env->ReleaseByteArrayElements(nv21, yuv, 0);
    env->ReleaseByteArrayElements(rgba, rgb, 0);
}