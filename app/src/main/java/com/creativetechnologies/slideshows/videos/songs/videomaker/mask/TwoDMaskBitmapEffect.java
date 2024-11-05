package com.creativetechnologies.slideshows.videos.songs.videomaker.mask;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.Utils;

import java.lang.reflect.Array;

public class TwoDMaskBitmapEffect {
    public static float ANIMATED_FRAME = 22.0f;
    public static int ANIMATED_FRAME_CAL = ((int) (ANIMATED_FRAME - 1.0f));
    private static int averageHeight;
    private static int averageWidth;
    private static float axisX;
    private static float axisY;
    private static Bitmap[][] bitmaps;
    private static Camera camera = new Camera();
    public static int direction = 0;
    private static Matrix matrix = new Matrix();
    static final Paint paint = new Paint();
    private static int partNumber = 8;
    public static EFFECT rollMode;
    private static float rotateDegree;

    public enum EFFECT {
        From_Left("From_Left") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                TwoDMaskBitmapEffect.rollMode = this;
                TwoDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                TwoDMaskBitmapEffect.fromLeft(bottom, top, new Canvas(mask), true, factor, arrow_direction);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                TwoDMaskBitmapEffect.rollMode = this;
                TwoDMaskBitmapEffect.partNumber = 8;
                TwoDMaskBitmapEffect.direction = 0;
                TwoDMaskBitmapEffect.camera = new Camera();
                TwoDMaskBitmapEffect.matrix = new Matrix();

                TwoDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        };

        String name;

        public abstract Bitmap getMask(Bitmap bitmap, Bitmap bitmap2, int i, String arrow_direction);

        public abstract void initBitmaps(Bitmap bitmap, Bitmap bitmap2, int i);

        private EFFECT(String name) {
            this.name = "";
            this.name = name;
        }
    }

    static {
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
    }

    public static void setRotateDegree(int factor) {
        int i = 90;

        rotateDegree = (((float) factor) * 90.0f) / ((float) ANIMATED_FRAME_CAL);

        float f = rotateDegree;
        axisY = (f / ((float) i)) * ((float) MyApplication.VIDEO_HEIGHT);

        float f1 = rotateDegree;
        axisX = (f1 / ((float) i)) * ((float) MyApplication.VIDEO_WIDTH);
    }

    public static void initBitmaps(Bitmap bottom, Bitmap top, EFFECT effect) {
        rollMode = effect;
        if (MyApplication.VIDEO_HEIGHT > 0 || MyApplication.VIDEO_WIDTH > 0) {
            bitmaps = (Bitmap[][]) Array.newInstance(Bitmap.class, new int[]{2, partNumber});
            averageWidth = MyApplication.VIDEO_WIDTH / partNumber;
            averageHeight = MyApplication.VIDEO_HEIGHT / partNumber;
            int i = 0;
            while (i < 2) {
                for (int j = 0; j < partNumber; j++) {
                    Bitmap partBitmap;
                    Rect rect;
                    Bitmap bitmap;
                    if (direction == 1) {
                        rect = new Rect(averageWidth * j, 0, (j + 1) * averageWidth, MyApplication.VIDEO_HEIGHT);
                        bitmap = bottom;
                        partBitmap = getPartBitmap(bitmap, averageWidth * j, 0, rect);
                    } else {
                        partBitmap = getPartBitmap(bottom, 0, averageHeight * j, new Rect(0, averageHeight * j, MyApplication.VIDEO_WIDTH, (j + 1) * averageHeight));
                    }
                    bitmaps[i][j] = bottom;
                }
                i++;
            }
        }
    }

    private static Bitmap getPartBitmap(Bitmap bitmap, int x, int y, Rect rect) {
        return Bitmap.createBitmap(bitmap, x, y, rect.width(), rect.height());
    }

    private static void fromLeft(Bitmap bottom, Bitmap top, Canvas canvas, boolean draw2D, int factor, String arrow_direction) {
        Bitmap currWholeBitmap = bottom;
        canvas.save();
        camera.save();
        if (draw2D) {
            camera.rotateY(0.0f);
        } else {
            camera.rotateY(rotateDegree);
        }
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(0.0f, (float) (0.0));
        matrix.postTranslate(0.0f, (float) (0.0));
        canvas.drawBitmap(Utils.backgrnd, matrix, paint);

        if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("left")) {
            matrix.reset();
            camera.save();
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(0.0f, (float) ((-MyApplication.VIDEO_HEIGHT) / 2));
            matrix.postTranslate(axisX, (float) (MyApplication.VIDEO_HEIGHT / 2));
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("right")) {
            matrix.reset();
            camera.save();
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) (-axisX), (float) ((-MyApplication.VIDEO_HEIGHT) / 2));
            matrix.postTranslate((float) 0, (float) (MyApplication.VIDEO_HEIGHT / 2));
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("up")) {
            matrix.reset();
            camera.save();
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) ((-MyApplication.VIDEO_WIDTH) / 2), (float) (axisY));
            matrix.postTranslate((float) (MyApplication.VIDEO_WIDTH / 2), (float) (0));
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("bottom")) {
            matrix.reset();
            camera.save();
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) ((-MyApplication.VIDEO_WIDTH) / 2), (float) (-axisY));
            matrix.postTranslate((float) (MyApplication.VIDEO_WIDTH / 2), (float) (0));
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("topleft")) {
            Matrix matrix1 = new Matrix();
            matrix1.preTranslate(0, (float) axisY);
            matrix1.postTranslate(axisX, (float) 0);
            canvas.drawBitmap(currWholeBitmap, matrix1, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("topright")) {
            Matrix matrix1 = new Matrix();
            matrix1.preTranslate(-axisX, (float) axisY);
            matrix1.postTranslate(0, (float) 0);
            canvas.drawBitmap(currWholeBitmap, matrix1, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("bottomleft")) {
            Matrix matrix1 = new Matrix();
            matrix1.preTranslate(0.0f, (float) (-axisY));
            matrix1.postTranslate(axisX, (float) 0);
            canvas.drawBitmap(currWholeBitmap, matrix1, paint);
        } else if (arrow_direction != null && arrow_direction.toString().equalsIgnoreCase("bottomright")) {
            Matrix matrix1 = new Matrix();
            matrix1.preTranslate(-axisX, (float) (-axisY));
            matrix1.postTranslate(0, (float) (0));
            canvas.drawBitmap(currWholeBitmap, matrix1, paint);
        } else {
            camera.save();
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(0.0f, (float) ((-MyApplication.VIDEO_HEIGHT) / 2));
            matrix.postTranslate(axisX, (float) (MyApplication.VIDEO_HEIGHT / 2));
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
        }
        canvas.restore();
    }
}
