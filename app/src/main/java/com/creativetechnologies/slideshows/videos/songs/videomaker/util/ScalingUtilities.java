package com.creativetechnologies.slideshows.videos.songs.videomaker.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;

public class ScalingUtilities {

    private static int[] iArr8;
    private static int i20;
    static int i17;
    private static int i14;
    private static int i15;
    private static int i16;
    private static int i18;
    private static int i19;
    private static int i13;
    public MyApplication application = MyApplication.getInstance();

    public static Bitmap m6853a(Bitmap bitmap, Bitmap bitmap2, int i, int i2, float f, float f2) {
//        Bitmap copy = bitmap2.copy(bitmap2.getConfig(), true);
        Bitmap cs = ConvetrSameSizeNew(bitmap, bitmap2, i, i2);
        new Canvas(m6845a(cs, 25, true)).drawBitmap(m6852a(bitmap, i, i2, f, f2), 0.0f, 0.0f, new Paint());
        return cs;
    }

    private static Bitmap m6852a(Bitmap bitmap, int i, int i2, float f, float f2) {
        float f3 = 0.0f;
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        Canvas canvas = new Canvas(createBitmap);
        float f4 = ((float) i) / width;
        float f5 = ((float) i2) / height;
        float f6 = (((float) i2) - (height * f4)) / 2.0f;
        if (f6 < 0.0f) {
            f4 = ((float) i2) / height;
            f6 = (((float) i) - (width * f5)) / 2.0f;
        } else {
            float f7 = f6;
            f6 = 0.0f;
            f3 = f7;
        }
        Matrix matrix = new Matrix();
        matrix.postTranslate(f6 * f, f3 + f2);
        matrix.preScale(f4, f4);
        canvas.drawBitmap(bitmap, matrix, new Paint());
        return createBitmap;
    }

    public static Bitmap ConvetrSameSizeNew(Bitmap originalImage, Bitmap bgBitmap, int mDisplayWidth, int mDisplayHeight) {
        try {
            Bitmap cs = BlurClass.doBlur(bgBitmap, 25, true);
            Canvas comboImage = new Canvas(cs);
            Paint paint = new Paint();
            float originalWidth = (float) originalImage.getWidth();
            float originalHeight = (float) originalImage.getHeight();
            float scale = ((float) mDisplayWidth) / originalWidth;
            float scaleY = ((float) mDisplayHeight) / originalHeight;
            float xTranslation = 0.0f;
            float yTranslation = (((float) mDisplayHeight) - (originalHeight * scale)) / 2.0f;
            if (yTranslation < 0.0f) {
                yTranslation = 0.0f;
                scale = ((float) mDisplayHeight) / originalHeight;
                xTranslation = (((float) mDisplayWidth) - (originalWidth * scaleY)) / 2.0f;
            }
            Matrix transformation = new Matrix();
            transformation.postTranslate(xTranslation, yTranslation);
            transformation.preScale(scale, scale);
            comboImage.drawBitmap(originalImage, transformation, paint);
            return cs;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    public static Bitmap m6844a(Bitmap bitmap, int i, Context context) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript create = RenderScript.create(context);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        Allocation createFromBitmap = Allocation.createFromBitmap(create, bitmap);
        Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
        create2.setRadius((float) i);
        create2.setInput(createFromBitmap);
        create2.forEach(createFromBitmap2);
        createFromBitmap2.copyTo(createBitmap);
        create.destroy();
        return createBitmap;
    }

    public static Bitmap m6845a(Bitmap bitmap, int i, boolean z) {
        if (Build.VERSION.SDK_INT >= 17) {
            return m6844a(bitmap, i, MyApplication.m1436a());
        }
        Bitmap bitmap2;
        if (z) {
            bitmap2 = bitmap;
        } else {
            bitmap2 = bitmap.copy(bitmap.getConfig(), true);
        }
        if (i < 1) {
            return null;
        }
        int i2;
        int i3;
        int i4;
        int width = bitmap2.getWidth();
        int height = bitmap2.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap2.getPixels(iArr, 0, width, 0, 0, width, height);
        int i5 = width - 1;
        int i6 = height - 1;
        int i7 = width * height;
        int i8 = (i + i) + 1;
        int[] iArr2 = new int[i7];
        int[] iArr3 = new int[i7];
        int[] iArr4 = new int[i7];
        int[] iArr5 = new int[Math.max(width, height)];
        i7 = (i8 + 1) >> 1;
        int i9 = i7 * i7;
        int[] iArr6 = new int[(i9 * 256)];
        for (i7 = 0; i7 < i9 * 256; i7++) {
            iArr6[i7] = i7 / i9;
        }
        int[][] iArr7 = (int[][]) Array.newInstance(Integer.TYPE, new int[]{i8, 3});
        int i10 = i + 1;
        int i11 = 0;
        int i12 = 0;
        for (i2 = 0; i2 < height; i2++) {
            i9 = 0;
            int i13 = 0;
            int i14 = 0;
            int i15 = 0;
            int i16 = 0;
            int i17 = 0;
            int i18 = 0;
            int i19 = 0;
            int i20 = 0;
            for (i3 = -i; i3 <= i; i3++) {
                i4 = iArr[Math.min(i5, Math.max(i3, 0)) + i12];
                int[] iArr8 = iArr7[i3 + i];
                iArr8[0] = (16711680 & i4) >> 16;
                iArr8[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & i4) >> 8;
                iArr8[2] = i4 & 255;
                i4 = i10 - Math.abs(i3);
                i19 += iArr8[0] * i4;
                i18 += iArr8[1] * i4;
                i17 += i4 * iArr8[2];
                if (i3 > 0) {
                    i13 += iArr8[0];
                    i20 += iArr8[1];
                    i9 += iArr8[2];
                } else {
                    i16 += iArr8[0];
                    i15 += iArr8[1];
                    i14 += iArr8[2];
                }
            }
            i4 = i19;
            i19 = i18;
            i18 = i17;
            i3 = i12;
            i12 = i;
            for (i17 = 0; i17 < width; i17++) {
                iArr2[i3] = iArr6[i4];
                iArr3[i3] = iArr6[i19];
                iArr4[i3] = iArr6[i18];
                i4 -= i16;
                i19 -= i15;
                i18 -= i14;
                iArr8 = iArr7[((i12 - i) + i8) % i8];
                i16 -= iArr8[0];
                i15 -= iArr8[1];
                i14 -= iArr8[2];
                if (i2 == 0) {
                    iArr5[i17] = Math.min((i17 + i) + 1, i5);
                }
                int i21 = iArr[iArr5[i17] + i11];
                iArr8[0] = (16711680 & i21) >> 16;
                iArr8[1] = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & i21) >> 8;
                iArr8[2] = i21 & 255;
                i13 += iArr8[0];
                i20 += iArr8[1];
                i9 += iArr8[2];
                i4 += i13;
                i19 += i20;
                i18 += i9;
                i12 = (i12 + 1) % i8;
                iArr8 = iArr7[i12 % i8];
                i16 += iArr8[0];
                i15 += iArr8[1];
                i14 += iArr8[2];
                i13 -= iArr8[0];
                i20 -= iArr8[1];
                i9 -= iArr8[2];
                i3++;
            }
            i11 += width;
            i12 = i3;
        }

        for (i17 = 0; i17 < width; i17++) {
            i20 = 0;
            i9 = (-i) * width;
            i14 = 0;
            i15 = 0;
            i16 = 0;
            i12 = 0;
            i4 = -i;
            i3 = 0;
            i18 = 0;
            i19 = 0;
            i13 = 0;
            while (i4 <= i) {
                i2 = Math.max(0, i9) + i17;
                int[] iArr9 = iArr7[i4 + i];
                iArr9[0] = iArr2[i2];
                iArr9[1] = iArr3[i2];
                iArr9[2] = iArr4[i2];
                int abs = i10 - Math.abs(i4);
                i11 = (iArr2[i2] * abs) + i19;
                i19 = (iArr3[i2] * abs) + i18;
                i18 = (iArr4[i2] * abs) + i3;
                if (i4 > 0) {
                    i14 += iArr9[0];
                    i13 += iArr9[1];
                    i20 += iArr9[2];
                } else {
                    i12 += iArr9[0];
                    i16 += iArr9[1];
                    i15 += iArr9[2];
                }
                if (i4 < i6) {
                    i9 += width;
                }
                i4++;
                i3 = i18;
                i18 = i19;
                i19 = i11;
            }
            i4 = i18;
            i11 = i19;
            i19 = i3;
            i3 = i17;
            i9 = i20;
            i20 = i13;
            i13 = i14;
            i14 = i15;
            i15 = i16;
            i16 = i12;
            i12 = i;
            for (i18 = 0; i18 < height; i18++) {
                iArr[i3] = (((ViewCompat.MEASURED_STATE_MASK & iArr[i3]) | (iArr6[i11] << 16)) | (iArr6[i4] << 8)) | iArr6[i19];
                i11 -= i16;
                i4 -= i15;
                i19 -= i14;
                int[] iArr10 = iArr7[((i12 - i) + i8) % i8];
                i16 -= iArr10[0];
                i15 -= iArr10[1];
                i14 -= iArr10[2];
                if (i17 == 0) {
                    iArr5[i18] = Math.min(i18 + i10, i6) * width;
                }
                i5 = iArr5[i18] + i17;
                iArr10[0] = iArr2[i5];
                iArr10[1] = iArr3[i5];
                iArr10[2] = iArr4[i5];
                i13 += iArr10[0];
                i20 += iArr10[1];
                i9 += iArr10[2];
                i11 += i13;
                i4 += i20;
                i19 += i9;
                i12 = (i12 + 1) % i8;
                iArr10 = iArr7[i12];
                i16 += iArr10[0];
                i15 += iArr10[1];
                i14 += iArr10[2];
                i13 -= iArr10[0];
                i20 -= iArr10[1];
                i9 -= iArr10[2];
                i3 += width;
            }
        }
        bitmap2.setPixels(iArr, 0, width, 0, 0, width, height);
        return bitmap2;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newWidth, int newHeight) {

        Bitmap dest = null;
        if (source != null) {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();
            if (sourceWidth == newWidth && sourceHeight == newHeight) {
                return source;
            }
            float scale = Math.max(((float) newWidth) / ((float) sourceWidth), ((float) newHeight) / ((float) sourceHeight));
            float scaledWidth = scale * ((float) sourceWidth);
            float scaledHeight = scale * ((float) sourceHeight);
            float left = (((float) newWidth) - scaledWidth) / 2.0f;
            float top = (((float) newHeight) - scaledHeight) / 2.0f;
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
            try {
                if (source.getConfig() != null) {
                    dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            new Canvas(dest).drawBitmap(source, null, targetRect, null);
        }
        return dest;
    }

    public static Bitmap scaleMoreCenterCrop(Bitmap source, int newWidth, int newHeight) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        if (sourceWidth == newWidth && sourceHeight == newHeight) {
            return source;
        }
        float scale = Math.max(((float) newWidth) / ((float) sourceWidth), ((float) newHeight) / ((float) sourceHeight));
        float scaledWidth = scale * ((float) sourceWidth);
        float scaledHeight = scale * ((float) sourceHeight);
        float left = (((float) newWidth) - scaledWidth) / 2.0f;
        float top = (((float) newHeight) - scaledHeight) / 2.0f;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        new Canvas(dest).drawBitmap(source, null, targetRect, null);
        return dest;

    }

    public static Bitmap checkBitmap(String path) {
        int orientation = 1;
        Options bounds = new Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);

        Bitmap bm = ScaleBitmapFile(new File(path));

//        Bitmap bm = BitmapFactory.decodeFile(path, new Options());
        try {
            String orientString = new ExifInterface(path).getAttribute("Orientation");
            if (orientString != null) {
                orientation = Integer.parseInt(orientString);
            }
            int rotationAngle = 0;
            if (orientation == 6) {
                rotationAngle = 90;
            }
            if (orientation == 3) {
                rotationAngle = 180;
            }
            if (orientation == 8) {
                rotationAngle = 270;
            }
            Matrix matrix = new Matrix();
            try {
                if (bm.getWidth() != 0)
                    matrix.setRotate((float) rotationAngle, ((float) bm.getWidth()) / 2.0f, ((float) bm.getHeight()) / 2.0f);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
//            return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        } catch (IOException e) {
            e.printStackTrace();
            return bm;
        }
    }

    public static Bitmap ScaleBitmapFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
//            file.createNewFile();
//            FileOutputStream outputStream = new FileOutputStream(file);

//            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return selectedBitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
