package com.creativetechnologies.slideshows.videos.songs.videomaker.mask;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.core.internal.view.SupportMenu;

import java.lang.reflect.Array;
import java.util.Random;

public class MoreMaskBitmapEffect {
    public static float f1918a = 22.0f;
    public static int f1919b = ((int) (f1918a - 1.0f));
    public static int f1920c = 8;
    public static int f1921d = 30;
    static final Paint f1922e = new Paint();
    static int[][] f1923f = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{20, 20}));
    static Random f1924g = new Random();

    public enum EFFECT {
        MORE("MORE") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                if (direction != null && direction.toString().equalsIgnoreCase("PIN_WHEEL")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    Path path = new Path();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo(0.0f, (float) i2);
                    path.lineTo(f, (float) i2);
                    path.close();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo((float) i, (float) i2);
                    path.lineTo((float) i, ((float) i2) - f2);
                    path.close();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo((float) i, 0.0f);
                    path.lineTo(((float) i) - f, 0.0f);
                    path.close();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo(0.0f, 0.0f);
                    path.lineTo(0.0f, f2);
                    path.close();
                    canvas.drawPath(path, paint);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("CIRCLE_LEFT_BOTTOM")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawCircle(0.0f, (float) i2, (((float) Math.sqrt((double) ((i * i) + (i2 * i2)))) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                    drawText(canvas);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("CIRCLE_RIGHT_BOTTOM")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawCircle((float) i, (float) i2, (((float) Math.sqrt((double) ((i * i) + (i2 * i2)))) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                    drawText(canvas);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("SKEW_RIGHT_MEARGE")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    Path path = new Path();
                    path.moveTo(0.0f, ((float) i2) - f2);
                    path.lineTo(f, (float) i2);
                    path.lineTo(0.0f, (float) i2);
                    path.close();
                    path.moveTo(((float) i) - f, 0.0f);
                    path.lineTo((float) i, f2);
                    path.lineTo((float) i, 0.0f);
                    path.close();
                    canvas.drawPath(path, paint);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("SKEW_LEFT_MEARGE")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    Path path = new Path();
                    path.moveTo(0.0f, f2);
                    path.lineTo(f, 0.0f);
                    path.lineTo(0.0f, 0.0f);
                    path.close();
                    path.moveTo(((float) i) - f, (float) i2);
                    path.lineTo((float) i, ((float) i2) - f2);
                    path.lineTo((float) i, (float) i2);
                    path.close();
                    canvas.drawPath(path, paint);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("CIRCLE_IN")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawCircle(((float) i) / 2.0f, ((float) i2) / 2.0f, (MoreMaskBitmapEffect.m1716a(i * 2, i2 * 2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                    drawText(canvas);
                    return createBitmap;

                } else if (direction != null && direction.toString().equalsIgnoreCase("CIRCLE_OUT")) {
                    Paint paint = new Paint(1);
//                    paint.setColor(-1);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float a = MoreMaskBitmapEffect.m1716a(i * 2, i2 * 2);
                    float f = (a / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    paint.setColor(-1);
                    canvas.drawColor(-1);
                    paint.setColor(0);
                    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                    canvas.drawCircle(((float) i) / 2.0f, ((float) i2) / 2.0f, a - f, paint);
                    drawText(canvas);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("FOUR_TRIANGLE")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                    float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                    Path path = new Path();
                    path.moveTo(0.0f, f2);
                    path.lineTo(0.0f, 0.0f);
                    path.lineTo(f, 0.0f);
                    path.lineTo((float) i, ((float) i2) - f2);
                    path.lineTo((float) i, (float) i2);
                    path.lineTo(((float) i) - f, (float) i2);
                    path.lineTo(0.0f, f2);
                    path.close();
                    path.moveTo(((float) i) - f, 0.0f);
                    path.lineTo((float) i, 0.0f);
                    path.lineTo((float) i, f2);
                    path.lineTo(f, (float) i2);
                    path.lineTo(0.0f, (float) i2);
                    path.lineTo(0.0f, ((float) i2) - f2);
                    path.lineTo(((float) i) - f, 0.0f);
                    path.close();
                    canvas.drawPath(path, paint);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("SQUARE_IN")) {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                    float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                    new Canvas(createBitmap).drawRect(new RectF(((float) (i / 2)) - f, ((float) (i2 / 2)) - f2, f + ((float) (i / 2)), f2 + ((float) (i2 / 2))), paint);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("SQUARE_OUT")) {

                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                    float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                    Path path = new Path();
                    path.moveTo(0.0f, 0.0f);
                    path.lineTo(0.0f, (float) i2);
                    path.lineTo(f, (float) i2);
                    path.lineTo(f, 0.0f);
                    path.moveTo((float) i, (float) i2);
                    path.lineTo((float) i, 0.0f);
                    path.lineTo(((float) i) - f, 0.0f);
                    path.lineTo(((float) i) - f, (float) i2);
                    path.moveTo(f, f2);
                    path.lineTo(f, 0.0f);
                    path.lineTo(((float) i) - f, 0.0f);
                    path.lineTo(((float) i) - f, f2);
                    path.moveTo(f, ((float) i2) - f2);
                    path.lineTo(f, (float) i2);
                    path.lineTo(((float) i) - f, (float) i2);
                    path.lineTo(((float) i) - f, ((float) i2) - f2);
                    canvas.drawPath(path, paint);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("DIAMOND_IN")) {

                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    Path path = new Path();
                    float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    path.moveTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                    path.lineTo((((float) i) / 2.0f) + f, ((float) i2) / 2.0f);
                    path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) + f2);
                    path.lineTo((((float) i) / 2.0f) - f, ((float) i2) / 2.0f);
                    path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                    path.close();
                    canvas.drawPath(path, paint);
                    return createBitmap;

                } else if (direction != null && direction.toString().equalsIgnoreCase("DIAMOND_OUT")) {
                    Paint paint = new Paint(1);
//                    paint.setColor(-1);
                    paint.setColor(0);
                    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawColor(-1);
                    Path path = new Path();
                    float f = ((float) i) - ((((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3));
                    float f2 = ((float) i2) - ((((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3));
                    path.moveTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                    path.lineTo((((float) i) / 2.0f) + f, ((float) i2) / 2.0f);
                    path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) + f2);
                    path.lineTo((((float) i) / 2.0f) - f, ((float) i2) / 2.0f);
                    path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                    path.close();
                    paint.setColor(0);
                    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                    canvas.drawPath(path, paint);
                    drawText(canvas);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("VERTICAL_RECT")) {
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    float f = ((float) i2) / 10.0f;
                    float f2 = (((float) i3) * f) / ((float) MoreMaskBitmapEffect.f1919b);
                    for (int i4 = 0; i4 < 10; i4++) {
                        canvas.drawRect(new Rect(0, (int) (((float) i4) * f), i, (int) ((((float) i4) * f) + f2)), paint);
                    }
                    drawText(canvas);
                    return createBitmap;
                } else if (direction != null && direction.toString().equalsIgnoreCase("RECT_RANDOM")) {
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    float f = (float) (i / MoreMaskBitmapEffect.f1919b);
                    float f2 = (float) (i2 / MoreMaskBitmapEffect.f1919b);
                    for (int i4 = 0; i4 < MoreMaskBitmapEffect.f1923f.length; i4++) {
                        int nextInt = MoreMaskBitmapEffect.f1924g.nextInt(MoreMaskBitmapEffect.f1923f[i4].length);
                        while (MoreMaskBitmapEffect.f1923f[i4][nextInt] == 1) {
                            nextInt = MoreMaskBitmapEffect.f1924g.nextInt(MoreMaskBitmapEffect.f1923f[i4].length);
                        }
                        MoreMaskBitmapEffect.f1923f[i4][nextInt] = 1;
                        for (nextInt = 0; nextInt < MoreMaskBitmapEffect.f1923f[i4].length; nextInt++) {
                            if (MoreMaskBitmapEffect.f1923f[i4][nextInt] == 1) {
                                canvas.drawRoundRect(new RectF(((float) i4) * f, ((float) nextInt) * f2, (((float) i4) + 1.0f) * f, (((float) nextInt) + 1.0f) * f2), 0.0f, 0.0f, paint);
                            }
                        }
                    }
                    drawText(canvas);
                    return createBitmap;
                } else {
                    Paint paint = new Paint();
//                    paint.setColor(-1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Style.FILL_AND_STROKE);
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                    Path path = new Path();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo(0.0f, (float) i2);
                    path.lineTo(f, (float) i2);
                    path.close();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo((float) i, (float) i2);
                    path.lineTo((float) i, ((float) i2) - f2);
                    path.close();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo((float) i, 0.0f);
                    path.lineTo(((float) i) - f, 0.0f);
                    path.close();
                    path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                    path.lineTo(0.0f, 0.0f);
                    path.lineTo(0.0f, f2);
                    path.close();
                    canvas.drawPath(path, paint);
                    return createBitmap;
                }
            }
        };
        /*CIRCLE_LEFT_TOP("CIRCLE LEFT TOP") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawCircle(0.0f, 0.0f, (((float) Math.sqrt((double) ((i * i) + (i2 * i2)))) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CIRCLE_RIGHT_TOP("Circle right top") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawCircle((float) i, 0.0f, (((float) Math.sqrt((double) ((i * i) + (i2 * i2)))) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CIRCLE_LEFT_BOTTOM("Circle left bottom") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawCircle(0.0f, (float) i2, (((float) Math.sqrt((double) ((i * i) + (i2 * i2)))) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CIRCLE_RIGHT_BOTTOM("Circle right bottom") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawCircle((float) i, (float) i2, (((float) Math.sqrt((double) ((i * i) + (i2 * i2)))) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CIRCLE_IN("Circle in") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint(1);
                paint.setColor(-1);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float a = MoreMaskBitmapEffect.m1716a(i * 2, i2 * 2);
                float f = (a / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                paint.setColor(-1);
                canvas.drawColor(-1);
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                canvas.drawCircle(((float) i) / 2.0f, ((float) i2) / 2.0f, a - f, paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CIRCLE_OUT("Circle out") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawCircle(((float) i) / 2.0f, ((float) i2) / 2.0f, (MoreMaskBitmapEffect.m1716a(i * 2, i2 * 2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3), paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CROSS_IN("Cross in") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(f, 0.0f);
                path.lineTo(f, f2);
                path.lineTo(0.0f, f2);
                path.lineTo(0.0f, 0.0f);
                path.close();
                path.moveTo((float) i, 0.0f);
                path.lineTo(((float) i) - f, 0.0f);
                path.lineTo(((float) i) - f, f2);
                path.lineTo((float) i, f2);
                path.lineTo((float) i, 0.0f);
                path.close();
                path.moveTo((float) i, (float) i2);
                path.lineTo(((float) i) - f, (float) i2);
                path.lineTo(((float) i) - f, ((float) i2) - f2);
                path.lineTo((float) i, ((float) i2) - f2);
                path.lineTo((float) i, (float) i2);
                path.close();
                path.moveTo(0.0f, (float) i2);
                path.lineTo(f, (float) i2);
                path.lineTo(f, ((float) i2) - f2);
                path.lineTo(0.0f, ((float) i2) - f2);
                path.lineTo(0.0f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        CROSS_OUT("Cross out") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                Path path = new Path();
                path.moveTo((((float) i) / 2.0f) + f, 0.0f);
                path.lineTo((((float) i) / 2.0f) + f, (((float) i2) / 2.0f) - f2);
                path.lineTo((float) i, (((float) i2) / 2.0f) - f2);
                path.lineTo((float) i, (((float) i2) / 2.0f) + f2);
                path.lineTo((((float) i) / 2.0f) + f, (((float) i2) / 2.0f) + f2);
                path.lineTo((((float) i) / 2.0f) + f, (float) i2);
                path.lineTo((((float) i) / 2.0f) - f, (float) i2);
                path.lineTo((((float) i) / 2.0f) - f, (((float) i2) / 2.0f) - f2);
                path.lineTo(0.0f, (((float) i2) / 2.0f) - f2);
                path.lineTo(0.0f, (((float) i2) / 2.0f) + f2);
                path.lineTo((((float) i) / 2.0f) - f, f2 + (((float) i2) / 2.0f));
                path.lineTo((((float) i) / 2.0f) - f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        DIAMOND_IN("Diamond in") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Path path = new Path();
                float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                path.moveTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                path.lineTo((((float) i) / 2.0f) + f, ((float) i2) / 2.0f);
                path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) + f2);
                path.lineTo((((float) i) / 2.0f) - f, ((float) i2) / 2.0f);
                path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        DIAMOND_OUT("Diamond out") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint(1);
                paint.setColor(-1);
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawColor(-1);
                Path path = new Path();
                float f = ((float) i) - ((((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3));
                float f2 = ((float) i2) - ((((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3));
                path.moveTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                path.lineTo((((float) i) / 2.0f) + f, ((float) i2) / 2.0f);
                path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) + f2);
                path.lineTo((((float) i) / 2.0f) - f, ((float) i2) / 2.0f);
                path.lineTo(((float) i) / 2.0f, (((float) i2) / 2.0f) - f2);
                path.close();
                paint.setColor(0);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
                canvas.drawPath(path, paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        ECLIPSE_IN("Eclipse in") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                float f2 = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                RectF rectF = new RectF(-f2, 0.0f, f2, (float) i2);
                RectF rectF2 = new RectF(0.0f, -f, (float) i, f);
                RectF rectF3 = new RectF(((float) i) - f2, 0.0f, f2 + ((float) i), (float) i2);
                RectF rectF4 = new RectF(0.0f, ((float) i2) - f, (float) i, f + ((float) i2));
                canvas.drawOval(rectF, MoreMaskBitmapEffect.f1922e);
                canvas.drawOval(rectF2, MoreMaskBitmapEffect.f1922e);
                canvas.drawOval(rectF3, MoreMaskBitmapEffect.f1922e);
                canvas.drawOval(rectF4, MoreMaskBitmapEffect.f1922e);
                drawText(canvas);
                return createBitmap;
            }
        },
        FOUR_TRIANGLE("Four triangle") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                Path path = new Path();
                path.moveTo(0.0f, f2);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(f, 0.0f);
                path.lineTo((float) i, ((float) i2) - f2);
                path.lineTo((float) i, (float) i2);
                path.lineTo(((float) i) - f, (float) i2);
                path.lineTo(0.0f, f2);
                path.close();
                path.moveTo(((float) i) - f, 0.0f);
                path.lineTo((float) i, 0.0f);
                path.lineTo((float) i, f2);
                path.lineTo(f, (float) i2);
                path.lineTo(0.0f, (float) i2);
                path.lineTo(0.0f, ((float) i2) - f2);
                path.lineTo(((float) i) - f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        HORIZONTAL_RECT("Horizontal rect") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                float f = ((float) i) / 10.0f;
                float f2 = ((float) i3) * (f / ((float) MoreMaskBitmapEffect.f1919b));
                for (int i4 = 0; i4 < 10; i4++) {
                    canvas.drawRect(new Rect((int) (((float) i4) * f), 0, (int) ((((float) i4) * f) + f2), i2), paint);
                }
                drawText(canvas);
                return createBitmap;
            }
        },
        HORIZONTAL_COLUMN_DOWNMASK("Horizontal column downmask") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = ((float) MoreMaskBitmapEffect.f1919b) / 2.0f;
                canvas.drawRoundRect(new RectF(0.0f, 0.0f, (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) / 2.0f)) * ((float) i3), ((float) i2) / 2.0f), 0.0f, 0.0f, paint);
                if (((float) i3) >= 0.5f + f) {
                    canvas.drawRoundRect(new RectF(((float) i) - (((float) ((int) (((float) i3) - f))) * (((float) i) / (((float) (MoreMaskBitmapEffect.f1919b - 1)) / 2.0f))), ((float) i2) / 2.0f, (float) i, (float) i2), 0.0f, 0.0f, paint);
                }
                return createBitmap;
            }
        },
        LEAF("LEAF") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setStrokeCap(Cap.BUTT);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                float f = (float) ((i / MoreMaskBitmapEffect.f1919b) * i3);
                float f2 = (float) ((i2 / MoreMaskBitmapEffect.f1919b) * i3);
                Canvas canvas = new Canvas(createBitmap);
                Path path = new Path();
                path.moveTo(0.0f, (float) i2);
                path.cubicTo(0.0f, (float) i2, (((float) i) / 2.0f) - f, (((float) i2) / 2.0f) - f2, (float) i, 0.0f);
                path.cubicTo((float) i, 0.0f, (((float) i) / 2.0f) + f, (((float) i2) / 2.0f) + f2, 0.0f, (float) i2);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        OPEN_DOOR("OPEN_DOOR") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setStrokeCap(Cap.BUTT);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                float f = (float) ((i / MoreMaskBitmapEffect.f1919b) * i3);
                float f2 = (float) ((i2 / MoreMaskBitmapEffect.f1919b) * i3);
                Canvas canvas = new Canvas(createBitmap);
                Path path = new Path();
                path.moveTo((float) (i / 2), 0.0f);
                path.lineTo(((float) (i / 2)) - f, 0.0f);
                path.lineTo(((float) (i / 2)) - (f / 2.0f), (float) (i2 / 6));
                path.lineTo(((float) (i / 2)) - (f / 2.0f), (float) (i2 - (i2 / 6)));
                path.lineTo(((float) (i / 2)) - f, (float) i2);
                path.lineTo(((float) (i / 2)) + f, (float) i2);
                path.lineTo(((float) (i / 2)) + (f / 2.0f), (float) (i2 - (i2 / 6)));
                path.lineTo(((float) (i / 2)) + (f / 2.0f), (float) (i2 / 6));
                path.lineTo(((float) (i / 2)) + f, 0.0f);
                path.lineTo(((float) (i / 2)) - f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                drawText(canvas);
                return createBitmap;
            }
        },
        PIN_WHEEL("PIN_WHEEL") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                Path path = new Path();
                path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                path.lineTo(0.0f, (float) i2);
                path.lineTo(f, (float) i2);
                path.close();
                path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                path.lineTo((float) i, (float) i2);
                path.lineTo((float) i, ((float) i2) - f2);
                path.close();
                path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                path.lineTo((float) i, 0.0f);
                path.lineTo(((float) i) - f, 0.0f);
                path.close();
                path.moveTo(((float) i) / 2.0f, ((float) i2) / 2.0f);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(0.0f, f2);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        RECT_RANDOM("RECT_RANDOM") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                float f = (float) (i / MoreMaskBitmapEffect.f1919b);
                float f2 = (float) (i2 / MoreMaskBitmapEffect.f1919b);
                for (int i4 = 0; i4 < MoreMaskBitmapEffect.f1923f.length; i4++) {
                    int nextInt = MoreMaskBitmapEffect.f1924g.nextInt(MoreMaskBitmapEffect.f1923f[i4].length);
                    while (MoreMaskBitmapEffect.f1923f[i4][nextInt] == 1) {
                        nextInt = MoreMaskBitmapEffect.f1924g.nextInt(MoreMaskBitmapEffect.f1923f[i4].length);
                    }
                    MoreMaskBitmapEffect.f1923f[i4][nextInt] = 1;
                    for (nextInt = 0; nextInt < MoreMaskBitmapEffect.f1923f[i4].length; nextInt++) {
                        if (MoreMaskBitmapEffect.f1923f[i4][nextInt] == 1) {
                            canvas.drawRoundRect(new RectF(((float) i4) * f, ((float) nextInt) * f2, (((float) i4) + 1.0f) * f, (((float) nextInt) + 1.0f) * f2), 0.0f, 0.0f, paint);
                        }
                    }
                }
                drawText(canvas);
                return createBitmap;
            }
        },
        SKEW_LEFT_MEARGE("SKEW_LEFT_MEARGE") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                Path path = new Path();
                path.moveTo(0.0f, f2);
                path.lineTo(f, 0.0f);
                path.lineTo(0.0f, 0.0f);
                path.close();
                path.moveTo(((float) i) - f, (float) i2);
                path.lineTo((float) i, ((float) i2) - f2);
                path.lineTo((float) i, (float) i2);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        SKEW_LEFT_SPLIT("SKEW_LEFT_SPLIT") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                Path path = new Path();
                path.moveTo(0.0f, f2);
                path.lineTo(0.0f, 0.0f);
                path.lineTo(f, 0.0f);
                path.lineTo((float) i, ((float) i2) - f2);
                path.lineTo((float) i, (float) i2);
                path.lineTo(((float) i) - f, (float) i2);
                path.lineTo(0.0f, f2);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        SKEW_RIGHT_SPLIT("SKEW_RIGHT_SPLIT") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                Path path = new Path();
                path.moveTo(((float) i) - f, 0.0f);
                path.lineTo((float) i, 0.0f);
                path.lineTo((float) i, f2);
                path.lineTo(f, (float) i2);
                path.lineTo(0.0f, (float) i2);
                path.lineTo(0.0f, ((float) i2) - f2);
                path.lineTo(((float) i) - f, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        SKEW_RIGHT_MEARGE("SKEW_RIGHT_MEARGE") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                float f2 = (((float) i2) / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                Path path = new Path();
                path.moveTo(0.0f, ((float) i2) - f2);
                path.lineTo(f, (float) i2);
                path.lineTo(0.0f, (float) i2);
                path.close();
                path.moveTo(((float) i) - f, 0.0f);
                path.lineTo((float) i, f2);
                path.lineTo((float) i, 0.0f);
                path.close();
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        SQUARE_IN("SQUARE_IN") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                Path path = new Path();
                path.moveTo(0.0f, 0.0f);
                path.lineTo(0.0f, (float) i2);
                path.lineTo(f, (float) i2);
                path.lineTo(f, 0.0f);
                path.moveTo((float) i, (float) i2);
                path.lineTo((float) i, 0.0f);
                path.lineTo(((float) i) - f, 0.0f);
                path.lineTo(((float) i) - f, (float) i2);
                path.moveTo(f, f2);
                path.lineTo(f, 0.0f);
                path.lineTo(((float) i) - f, 0.0f);
                path.lineTo(((float) i) - f, f2);
                path.moveTo(f, ((float) i2) - f2);
                path.lineTo(f, (float) i2);
                path.lineTo(((float) i) - f, (float) i2);
                path.lineTo(((float) i) - f, ((float) i2) - f2);
                canvas.drawPath(path, paint);
                return createBitmap;
            }
        },
        SQUARE_OUT("SQUARE_OUT") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                float f = (((float) i) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                float f2 = (((float) i2) / (((float) MoreMaskBitmapEffect.f1919b) * 2.0f)) * ((float) i3);
                new Canvas(createBitmap).drawRect(new RectF(((float) (i / 2)) - f, ((float) (i2 / 2)) - f2, f + ((float) (i / 2)), f2 + ((float) (i2 / 2))), paint);
                return createBitmap;
            }
        },
        VERTICAL_RECT("VERTICAL_RECT") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                float f = ((float) i2) / 10.0f;
                float f2 = (((float) i3) * f) / ((float) MoreMaskBitmapEffect.f1919b);
                for (int i4 = 0; i4 < 10; i4++) {
                    canvas.drawRect(new Rect(0, (int) (((float) i4) * f), i, (int) ((((float) i4) * f) + f2)), paint);
                }
                drawText(canvas);
                return createBitmap;
            }
        },
        WIND_MILL("WIND_MILL") {
            public Bitmap getMask(int i, int i2, int i3, String direction) {
                float a = MoreMaskBitmapEffect.m1716a(i, i2);
                Paint paint = new Paint();
                paint.setColor(-1);
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL_AND_STROKE);
                Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                RectF rectF = new RectF();
                rectF.set((((float) i) / 2.0f) - a, (((float) i2) / 2.0f) - a, (((float) i) / 2.0f) + a, a + (((float) i2) / 2.0f));
                a = (90.0f / ((float) MoreMaskBitmapEffect.f1919b)) * ((float) i3);
                canvas.drawArc(rectF, 90.0f, a, true, paint);
                canvas.drawArc(rectF, 180.0f, a, true, paint);
                canvas.drawArc(rectF, 270.0f, a, true, paint);
                canvas.drawArc(rectF, 360.0f, a, true, paint);
                drawText(canvas);
                return createBitmap;
            }
        };*/

        String name;

        public abstract Bitmap getMask(int i, int i2, int i3, String direction);

        private EFFECT(String str) {
            this.name = "";
            this.name = str;
        }

        public void drawText(Canvas canvas) {
            Paint paint = new Paint();
            paint.setTextSize(50.0f);
            paint.setColor(SupportMenu.CATEGORY_MASK);
        }
    }

    static {
        f1922e.setColor(-1);
        f1922e.setAntiAlias(true);
        f1922e.setStyle(Style.FILL_AND_STROKE);
    }

    public static void m1717a() {
        f1923f = (int[][]) Array.newInstance(Integer.TYPE, new int[]{(int) f1918a, (int) f1918a});
        for (int i = 0; i < f1923f.length; i++) {
            for (int i2 = 0; i2 < f1923f[i].length; i2++) {
                f1923f[i][i2] = 0;
            }
        }
    }

    static float m1716a(int i, int i2) {
        return (float) Math.sqrt((double) (((i * i) + (i2 * i2)) / 4));
    }
}
