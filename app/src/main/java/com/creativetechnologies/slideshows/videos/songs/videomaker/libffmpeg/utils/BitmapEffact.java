package com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

import androidx.core.view.MotionEventCompat;

import java.lang.reflect.Array;

public class BitmapEffact {
    @TargetApi(17)
    public static Bitmap fastblur(Context mContext, Bitmap bmp, int n) {
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Config.ARGB_8888);
        new Canvas(bitmap).drawBitmap(bmp, 0.0f, 0.0f, null);
        if (n < 1) {
            return bmp;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] array = new int[(width * height)];
        bitmap.getPixels(array, 0, width, 0, 0, width, height);
        int n2 = width - 1;
        int n3 = height - 1;
        int n4 = width * height;
        int n5 = (n + n) + 1;
        int[] array2 = new int[n4];
        int[] array3 = new int[n4];
        int[] array4 = new int[n4];
        int[] array5 = new int[Math.max(width, height)];
        int n6 = (n5 + 1) >> 1;
        int n7 = n6 * n6;
        int[] array6 = new int[(n7 * 256)];
        for (int i = 0; i < n7 * 256; i++) {
            array6[i] = i / n7;
        }
        int n8 = 0;
        int n9 = 0;
        int[][] array7 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{n5, 3});
        int n10 = n + 1;
        for (int j = 0; j < height; j++) {
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = 0;
            int n16 = 0;
            int n17 = 0;
            int n18 = 0;
            int n19 = 0;
            for (int k = -n; k <= n; k++) {
                int n20 = array[Math.min(n2, Math.max(k, 0)) + n8];
                int[] array8 = array7[k + n];
                array8[0] = (16711680 & n20) >> 16;
                array8[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & n20) >> 8;
                array8[2] = n20 & 255;
                int n21 = n10 - Math.abs(k);
                n13 += array8[0] * n21;
                n12 += array8[1] * n21;
                n11 += array8[2] * n21;
                if (k > 0) {
                    n19 += array8[0];
                    n18 += array8[1];
                    n17 += array8[2];
                } else {
                    n16 += array8[0];
                    n15 += array8[1];
                    n14 += array8[2];
                }
            }
            int n22 = n;
            for (int l = 0; l < width; l++) {
                array2[n8] = array6[n13];
                array3[n8] = array6[n12];
                array4[n8] = array6[n11];
                int n23 = n13 - n16;
                int n24 = n12 - n15;
                int n25 = n11 - n14;
                int[] array9 = array7[((n22 - n) + n5) % n5];
                int n26 = n16 - array9[0];
                int n27 = n15 - array9[1];
                int n28 = n14 - array9[2];
                if (j == 0) {
                    array5[l] = Math.min((l + n) + 1, n2);
                }
                int n29 = array[array5[l] + n9];
                array9[0] = (16711680 & n29) >> 16;
                array9[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & n29) >> 8;
                array9[2] = n29 & 255;
                int n30 = n19 + array9[0];
                int n31 = n18 + array9[1];
                int n32 = n17 + array9[2];
                n13 = n23 + n30;
                n12 = n24 + n31;
                n11 = n25 + n32;
                n22 = (n22 + 1) % n5;
                int[] array10 = array7[n22 % n5];
                n16 = n26 + array10[0];
                n15 = n27 + array10[1];
                n14 = n28 + array10[2];
                n19 = n30 - array10[0];
                n18 = n31 - array10[1];
                n17 = n32 - array10[2];
                n8++;
            }
            n9 += width;
        }
        for (int n33 = 0; n33 < width; n33++) {
            int n34 = 0;
            int n35 = 0;
            int n36 = 0;
            int n37 = 0;
            int n38 = 0;
            int n39 = 0;
            int n40 = 0;
            int n41 = 0;
            int n42 = 0;
            int n43 = width * (-n);
            for (int n44 = -n; n44 <= n; n44++) {
                int n45 = n33 + Math.max(0, n43);
                int[] array11 = array7[n44 + n];
                array11[0] = array2[n45];
                array11[1] = array3[n45];
                array11[2] = array4[n45];
                int n46 = n10 - Math.abs(n44);
                n36 += array2[n45] * n46;
                n35 += array3[n45] * n46;
                n34 += array4[n45] * n46;
                if (n44 > 0) {
                    n42 += array11[0];
                    n41 += array11[1];
                    n40 += array11[2];
                } else {
                    n39 += array11[0];
                    n38 += array11[1];
                    n37 += array11[2];
                }
                if (n44 < n3) {
                    n43 += width;
                }
            }
            int n47 = n33;
            int n48 = n;
            for (int n49 = 0; n49 < height; n49++) {
                array[n47] = (((-16777216 & array[n47]) | (array6[n36] << 16)) | (array6[n35] << 8)) | array6[n34];
                int n50 = n36 - n39;
                int n51 = n35 - n38;
                int n52 = n34 - n37;
                int[] array12 = array7[((n48 - n) + n5) % n5];
                int n53 = n39 - array12[0];
                int n54 = n38 - array12[1];
                int n55 = n37 - array12[2];
                if (n33 == 0) {
                    array5[n49] = Math.min(n49 + n10, n3) * width;
                }
                int n56 = n33 + array5[n49];
                array12[0] = array2[n56];
                array12[1] = array3[n56];
                array12[2] = array4[n56];
                int n57 = n42 + array12[0];
                int n58 = n41 + array12[1];
                int n59 = n40 + array12[2];
                n36 = n50 + n57;
                n35 = n51 + n58;
                n34 = n52 + n59;
                n48 = (n48 + 1) % n5;
                int[] array13 = array7[n48];
                n39 = n53 + array13[0];
                n38 = n54 + array13[1];
                n37 = n55 + array13[2];
                n42 = n57 - array13[0];
                n41 = n58 - array13[1];
                n40 = n59 - array13[2];
                n47 += width;
            }
        }
        bitmap.setPixels(array, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
