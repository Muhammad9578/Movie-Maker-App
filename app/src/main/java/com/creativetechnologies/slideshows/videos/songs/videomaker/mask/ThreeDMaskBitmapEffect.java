package com.creativetechnologies.slideshows.videos.songs.videomaker.mask;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.core.internal.view.SupportMenu;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.Utils;

import java.lang.reflect.Array;

public class ThreeDMaskBitmapEffect {
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
    static int[][] randRect;
    public static EFFECT rollMode;
    private static float rotateDegree;

    public enum EFFECT {
        Roll2D_TB("Roll2D_TB") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollWhole3D(bottom, top, new Canvas(mask), true);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        Roll2D_BT("Roll2D_BT") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollWhole3D(top, bottom, new Canvas(mask), true);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        Roll2D_LR("Roll2D_LR") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollWhole3D(bottom, top, new Canvas(mask), true);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        Roll2D_RL("Roll2D_RL") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollWhole3D(top, bottom, new Canvas(mask), true);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        Whole3D_TB("Whole3D_TB") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.drawRollWhole3D(bottom, top, canvas, false);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.rollMode = this;
            }
        },
        Whole3D_BT("Whole3D_BT") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.drawRollWhole3D(top, bottom, canvas, false);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        Whole3D_LR("Whole3D_LR") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.drawRollWhole3D(bottom, top, canvas, false);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        Whole3D_RL("Whole3D_RL") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.drawRollWhole3D(top, bottom, canvas, false);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
            }
        },
        SepartConbine_TB("SepartConbine_TB") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        },
        SepartConbine_BT("SepartConbine_BT") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(top, bottom, this);
            }
        },
        SepartConbine_LR("SepartConbine_LR") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        },
        SepartConbine_RL("SepartConbine_RL") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawSepartConbine(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(top, bottom, this);
            }
        },
        RollInTurn_TB("RollInTurn_TB") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        },
        RollInTurn_BT("RollInTurn_BT") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(top, bottom, this);
            }
        },
        RollInTurn_LR("RollInTurn_LR") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        },
        RollInTurn_RL("RollInTurn_RL") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawRollInTurn(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(top, bottom, this);
            }
        },
        Jalousie_BT("Jalousie_BT") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(ThreeDMaskBitmapEffect.ANIMATED_FRAME_CAL - factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawJalousie(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.direction = 1;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(top, bottom, this);
            }
        },
        Jalousie_LR("Jalousie_LR") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.drawJalousie(new Canvas(mask));
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.partNumber = 6;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();
                ThreeDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        },
        From_Left("From_Left") {
            public Bitmap getMask(Bitmap bottom, Bitmap top, int factor, String arrow_direction) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.setRotateDegree(factor);
                Bitmap mask = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                ThreeDMaskBitmapEffect.fromLeft(bottom, top, new Canvas(mask), true, factor, arrow_direction);
                return mask;
            }

            public void initBitmaps(Bitmap bottom, Bitmap top, int i) {
                ThreeDMaskBitmapEffect.rollMode = this;
                ThreeDMaskBitmapEffect.partNumber = 8;
                ThreeDMaskBitmapEffect.direction = 0;
                ThreeDMaskBitmapEffect.camera = new Camera();
                ThreeDMaskBitmapEffect.matrix = new Matrix();

                ThreeDMaskBitmapEffect.initBitmaps(bottom, top, this);
            }
        };

        String name;

        public abstract Bitmap getMask(Bitmap bitmap, Bitmap bitmap2, int i, String arrow_direction);

        public abstract void initBitmaps(Bitmap bitmap, Bitmap bitmap2, int i);

        private EFFECT(String name) {
            this.name = "";
            this.name = name;
        }

        public void drawText(Canvas canvas) {
            Paint paint = new Paint();
            paint.setTextSize(50.0f);
            paint.setColor(SupportMenu.CATEGORY_MASK);
        }
    }

    static {
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);

    }

    public static void reintRect() {
        randRect = (int[][]) Array.newInstance(Integer.TYPE, new int[]{(int) ANIMATED_FRAME, (int) ANIMATED_FRAME});
        for (int i = 0; i < randRect.length; i++) {
            for (int j = 0; j < randRect[i].length; j++) {
                randRect[i][j] = 0;
            }
        }
    }

    public static Paint getPaint() {
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        return paint;
    }

    static float getRad(int w, int h) {
        return (float) Math.sqrt((double) (((w * w) + (h * h)) / 4));
    }

    static Bitmap getHorizontalColumnDownMask(int w, int h, int factor) {
        Paint paint = new Paint();
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        float factorX = ((float) ANIMATED_FRAME_CAL) / 2.0f;
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, (((float) w) / (((float) ANIMATED_FRAME_CAL) / 2.0f)) * ((float) factor), ((float) h) / 2.0f), 0.0f, 0.0f, paint);
        if (((float) factor) >= 0.5f + factorX) {
            canvas.drawRoundRect(new RectF(((float) w) - ((((float) w) / (((float) (ANIMATED_FRAME_CAL - 1)) / 2.0f)) * ((float) ((int) (((float) factor) - factorX)))), ((float) h) / 2.0f, (float) w, (float) h), 0.0f, 0.0f, paint);
        }
        return mask;
    }

    static Bitmap getRandomRectHoriMask(int w, int h, int factor) {
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        Paint paint = new Paint();
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        float wf = ((float) w) / 10.0f;
        float rectW = factor == 0 ? 0.0f : (((float) factor) * wf) / 9.0f;
        for (int i = 0; i < 10; i++) {
            canvas.drawRect(new Rect((int) (((float) i) * wf), 0, (int) ((((float) i) * wf) + rectW), h), paint);
        }
        return mask;
    }

    static Bitmap getNewMask(int w, int h, int factor) {
        Paint paint = new Paint();
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        float ratioX = (((float) w) / 18.0f) * ((float) factor);
        float ratioY = (((float) h) / 18.0f) * ((float) factor);
        Path path = new Path();
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) + ratioX, (float) (h / 2));
        path.lineTo((float) w, 0.0f);
        path.lineTo((float) (w / 2), ((float) (h / 2)) - ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) - ratioX, (float) (h / 2));
        path.lineTo(0.0f, 0.0f);
        path.lineTo((float) (w / 2), ((float) (h / 2)) - ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) - ratioX, (float) (h / 2));
        path.lineTo(0.0f, (float) h);
        path.lineTo((float) (w / 2), ((float) (h / 2)) + ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.moveTo((float) (w / 2), (float) (h / 2));
        path.lineTo(((float) (w / 2)) + ratioX, (float) (h / 2));
        path.lineTo((float) w, (float) h);
        path.lineTo((float) (w / 2), ((float) (h / 2)) + ratioY);
        path.lineTo((float) (w / 2), (float) (h / 2));
        path.close();
        canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, (((float) w) / 18.0f) * ((float) factor), paint);
        canvas.drawPath(path, paint);
        return mask;
    }

    public static Bitmap getRadialBitmap(int w, int h, int factor) {
        Bitmap mask = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        if (factor != 0) {
            Canvas canvas = new Canvas(mask);
            if (factor == 9) {
                canvas.drawColor(-16777216);
            } else {
                paint.setStyle(Style.STROKE);
                float radius = getRad(w, h);
                paint.setStrokeWidth((radius / 80.0f) * ((float) factor));
                for (int i = 0; i < 11; i++) {
                    canvas.drawCircle(((float) w) / 2.0f, ((float) h) / 2.0f, (radius / 10.0f) * ((float) i), paint);
                }
            }
        }
        return mask;
    }

    public static void setRotateDegree(int factor) {
        int i = 90;
        if (rollMode == EFFECT.RollInTurn_BT || rollMode == EFFECT.RollInTurn_LR || rollMode == EFFECT.RollInTurn_RL || rollMode == EFFECT.RollInTurn_TB) {
            rotateDegree = (((((float) (partNumber - 1)) * BitmapDescriptorFactory.HUE_ORANGE) + 90.0f) * ((float) factor)) / ((float) ANIMATED_FRAME_CAL);
        } else if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
            rotateDegree = (BitmapDescriptorFactory.HUE_CYAN * ((float) factor)) / ((float) ANIMATED_FRAME_CAL);
        } else {
            rotateDegree = (((float) factor) * 90.0f) / ((float) ANIMATED_FRAME_CAL);
        }
        if (direction == 1) {
            float f = rotateDegree;
            if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
                i = 180;
            }
            axisY = (f / ((float) i)) * ((float) MyApplication.VIDEO_HEIGHT);
            return;
        }
        float f = rotateDegree;
        if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
            i = 180;
        }
        axisX = (f / ((float) i)) * ((float) MyApplication.VIDEO_WIDTH);
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
                    if (rollMode == EFFECT.Jalousie_BT || rollMode == EFFECT.Jalousie_LR) {
                        if (direction == 1) {
                            rect = new Rect(0, averageHeight * j, MyApplication.VIDEO_WIDTH, (j + 1) * averageHeight);
                            if (i == 0) {
                                bitmap = bottom;
                            } else {
                                bitmap = top;
                            }
                            partBitmap = getPartBitmap(bitmap, 0, averageHeight * j, rect);
                        } else {
                            rect = new Rect(averageWidth * j, 0, (j + 1) * averageWidth, MyApplication.VIDEO_HEIGHT);
                            if (i == 0) {
                                bitmap = bottom;
                            } else {
                                bitmap = top;
                            }
                            partBitmap = getPartBitmap(bitmap, averageWidth * j, 0, rect);
                        }
                    } else if (direction == 1) {
                        rect = new Rect(averageWidth * j, 0, (j + 1) * averageWidth, MyApplication.VIDEO_HEIGHT);
                        if (i == 0) {
                            bitmap = bottom;
                        } else {
                            bitmap = top;
                        }
                        partBitmap = getPartBitmap(bitmap, averageWidth * j, 0, rect);
                    } else {
                        partBitmap = getPartBitmap(i == 0 ? bottom : top, 0, averageHeight * j, new Rect(0, averageHeight * j, MyApplication.VIDEO_WIDTH, (j + 1) * averageHeight));
                    }
                    bitmaps[i][j] = partBitmap;
                }
                i++;
            }
        }
    }

    private static Bitmap getPartBitmap(Bitmap bitmap, int x, int y, Rect rect) {
        return Bitmap.createBitmap(bitmap, x, y, rect.width(), rect.height());
    }

    private static void drawRollWhole3D(Bitmap bottom, Bitmap top, Canvas canvas, boolean draw2D) {
        Bitmap currWholeBitmap = MyApplication.get_overlay(bottom);
        Bitmap nextWholeBitmap = MyApplication.get_overlay(top);
        canvas.drawBitmap(Utils.backgrnd, 0, 0, paint);
        canvas.save();
        if (direction == 1) {
            camera.save();
            if (draw2D) {
                camera.rotateX(0.0f);
            } else {
                camera.rotateX(-rotateDegree);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) ((-MyApplication.VIDEO_WIDTH) / 2), 0.0f);
            matrix.postTranslate((float) (MyApplication.VIDEO_WIDTH / 2), axisY);
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
            camera.save();
            if (draw2D) {
                camera.rotateX(0.0f);
            } else {
                camera.rotateX(90.0f - rotateDegree);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) ((-MyApplication.VIDEO_WIDTH) / 2), (float) (-MyApplication.VIDEO_HEIGHT));
            matrix.postTranslate((float) (MyApplication.VIDEO_WIDTH / 2), axisY);
            canvas.drawBitmap(nextWholeBitmap, matrix, paint);
        } else {
            camera.save();
            if (draw2D) {
                camera.rotateY(0.0f);
            } else {
                camera.rotateY(rotateDegree);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(0.0f, (float) ((-MyApplication.VIDEO_HEIGHT) / 2));
            matrix.postTranslate(axisX, (float) (MyApplication.VIDEO_HEIGHT / 2));
            canvas.drawBitmap(currWholeBitmap, matrix, paint);
            camera.save();
            if (draw2D) {
                camera.rotateY(0.0f);
            } else {
                camera.rotateY(rotateDegree - 90.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) (-MyApplication.VIDEO_WIDTH), (float) ((-MyApplication.VIDEO_HEIGHT) / 2));
            matrix.postTranslate(axisX, (float) (MyApplication.VIDEO_HEIGHT / 2));
            canvas.drawBitmap(nextWholeBitmap, matrix, paint);
        }
        canvas.restore();
    }

    private static void fromLeft(Bitmap bottom, Bitmap top, Canvas canvas, boolean draw2D, int factor, String arrow_direction) {
        Bitmap currWholeBitmap = bottom;
        Bitmap nextWholeBitmap = top;
        canvas.save();
        if (direction == 1) {
            camera.save();
            if (draw2D) {
                camera.rotateX(0.0f);
            } else {
                camera.rotateX(-rotateDegree);
            }
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate((float) ((-MyApplication.VIDEO_WIDTH) / 2), 0.0f);
            matrix.postTranslate((float) (MyApplication.VIDEO_WIDTH / 2), axisY);
            canvas.drawBitmap(Utils.backgrnd, matrix, paint);
            camera.save();

        } else {
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

            if (arrow_direction.toString().equalsIgnoreCase("R.drawable.up")) {
                Matrix matrix1 = new Matrix();
                matrix1.preTranslate(0.0f, (float) (-MyApplication.VIDEO_HEIGHT));
                matrix1.postTranslate(axisX, (float) (axisX));
                canvas.drawBitmap(currWholeBitmap, matrix1, paint);
            } else {
                Matrix matrix1 = new Matrix();
                matrix1.preTranslate(axisX, (float) (0.0));
                matrix1.postTranslate(0.0f, (float) (0.0));
                canvas.drawBitmap(currWholeBitmap, matrix1, paint);
            }
        }
        canvas.restore();
    }

    private static void drawSepartConbine(Canvas canvas) {
        canvas.drawBitmap(Utils.backgrnd, 0, 0, paint);
        for (int i = 0; i < partNumber; i++) {
            Bitmap currBitmap = bitmaps[0][i];
            Bitmap nextBitmap = bitmaps[1][i];
            canvas.save();
            if (direction == 1) {
                camera.save();
                camera.rotateX(-rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) ((-currBitmap.getWidth()) / 2), 0.0f);
                matrix.postTranslate((float) ((currBitmap.getWidth() / 2) + (averageWidth * i)), axisY);
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateX(90.0f - rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) ((-nextBitmap.getWidth()) / 2), (float) (-nextBitmap.getHeight()));
                matrix.postTranslate((float) ((nextBitmap.getWidth() / 2) + (averageWidth * i)), axisY);
                canvas.drawBitmap(nextBitmap, matrix, paint);
            } else {
                camera.save();
                camera.rotateY(rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(0.0f, (float) ((-currBitmap.getHeight()) / 2));
                matrix.postTranslate(axisX, (float) ((currBitmap.getHeight() / 2) + (averageHeight * i)));
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateY(rotateDegree - 90.0f);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) (-nextBitmap.getWidth()), (float) ((-nextBitmap.getHeight()) / 2));
                matrix.postTranslate(axisX, (float) ((nextBitmap.getHeight() / 2) + (averageHeight * i)));
                canvas.drawBitmap(nextBitmap, matrix, paint);
            }
            canvas.restore();
        }
    }

    private static void drawRollInTurn(Canvas canvas) {
        canvas.drawBitmap(Utils.backgrnd, 0, 0, paint);
        for (int i = 0; i < partNumber; i++) {
            Bitmap currBitmap = bitmaps[0][i];
            Bitmap nextBitmap = bitmaps[1][i];
            float tDegree = rotateDegree - ((float) (i * 30));
            if (tDegree < 0.0f) {
                tDegree = 0.0f;
            }
            if (tDegree > 90.0f) {
                tDegree = 90.0f;
            }
            canvas.save();
            if (direction == 1) {
                float tAxisY = (tDegree / 90.0f) * ((float) MyApplication.VIDEO_HEIGHT);
                if (tAxisY > ((float) MyApplication.VIDEO_HEIGHT)) {
                    tAxisY = (float) MyApplication.VIDEO_HEIGHT;
                }
                if (tAxisY < 0.0f) {
                    tAxisY = 0.0f;
                }
                camera.save();
                camera.rotateX(-tDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) (-currBitmap.getWidth()), 0.0f);
                matrix.postTranslate((float) (currBitmap.getWidth() + (averageWidth * i)), tAxisY);
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateX(90.0f - tDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) (-nextBitmap.getWidth()), (float) (-nextBitmap.getHeight()));
                matrix.postTranslate((float) (nextBitmap.getWidth() + (averageWidth * i)), tAxisY);
                canvas.drawBitmap(nextBitmap, matrix, paint);
            } else {
                float tAxisX = (tDegree / 90.0f) * ((float) MyApplication.VIDEO_WIDTH);
                if (tAxisX > ((float) MyApplication.VIDEO_WIDTH)) {
                    tAxisX = (float) MyApplication.VIDEO_WIDTH;
                }
                if (tAxisX < 0.0f) {
                    tAxisX = 0.0f;
                }
                camera.save();
                camera.rotateY(tDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(0.0f, (float) ((-currBitmap.getHeight()) / 2));
                matrix.postTranslate(tAxisX, (float) ((currBitmap.getHeight() / 2) + (averageHeight * i)));
                canvas.drawBitmap(currBitmap, matrix, paint);
                camera.save();
                camera.rotateY(tDegree - 90.0f);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) (-nextBitmap.getWidth()), (float) ((-nextBitmap.getHeight()) / 2));
                matrix.postTranslate(tAxisX, (float) ((nextBitmap.getHeight() / 2) + (averageHeight * i)));
                canvas.drawBitmap(nextBitmap, matrix, paint);
            }
            canvas.restore();
        }
    }

    private static void drawJalousie(Canvas canvas) {
        canvas.drawBitmap(Utils.backgrnd, 0, 0, paint);
        for (int i = 0; i < partNumber; i++) {
            Bitmap currBitmap = bitmaps[0][i];
            Bitmap nextBitmap = bitmaps[1][i];
            canvas.save();
            if (direction == 1) {
                if (rotateDegree < 90.0f) {
                    camera.save();
                    camera.rotateX(rotateDegree);
                    camera.getMatrix(matrix);
                    camera.restore();
                    matrix.preTranslate((float) ((-currBitmap.getWidth()) / 2), (float) ((-currBitmap.getHeight()) / 2));
                    matrix.postTranslate((float) (currBitmap.getWidth() / 2), (float) ((currBitmap.getHeight() / 2) + (averageHeight * i)));
                    canvas.drawBitmap(currBitmap, matrix, paint);
                } else {
                    camera.save();
                    camera.rotateX(BitmapDescriptorFactory.HUE_CYAN - rotateDegree);
                    camera.getMatrix(matrix);
                    camera.restore();
                    matrix.preTranslate((float) ((-nextBitmap.getWidth()) / 2), (float) ((-nextBitmap.getHeight()) / 2));
                    matrix.postTranslate((float) (nextBitmap.getWidth() / 2), (float) ((nextBitmap.getHeight() / 2) + (averageHeight * i)));
                    canvas.drawBitmap(nextBitmap, matrix, paint);
                }
            } else if (rotateDegree < 90.0f) {
                camera.save();
                camera.rotateY(rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) ((-currBitmap.getWidth()) / 2), (float) ((-currBitmap.getHeight()) / 2));
                matrix.postTranslate((float) ((currBitmap.getWidth() / 2) + (averageWidth * i)), (float) (currBitmap.getHeight() / 2));
                canvas.drawBitmap(currBitmap, matrix, paint);
            } else {
                camera.save();
                camera.rotateY(BitmapDescriptorFactory.HUE_CYAN - rotateDegree);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate((float) ((-nextBitmap.getWidth()) / 2), (float) ((-nextBitmap.getHeight()) / 2));
                matrix.postTranslate((float) ((nextBitmap.getWidth() / 2) + (averageWidth * i)), (float) (nextBitmap.getHeight() / 2));
                canvas.drawBitmap(nextBitmap, matrix, paint);
            }
            canvas.restore();
        }
    }
}
