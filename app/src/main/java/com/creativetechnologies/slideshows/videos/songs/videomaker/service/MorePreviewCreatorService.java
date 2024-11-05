package com.creativetechnologies.slideshows.videos.songs.videomaker.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.OnProgressReceiver;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.mask.MoreMaskBitmapEffect;
import com.creativetechnologies.slideshows.videos.songs.videomaker.mask.TwoDMaskBitmapEffect;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.ScalingUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MorePreviewCreatorService extends IntentService {
    public static final String EXTRA_SELECTED_THEME = "selected_theme";
    public static boolean isImageComplate = false;
    MyApplication application;
    ArrayList<ImageData> arrayList;
    private String selectedTheme;
    int totalImages;
    int f1944e;

    public MorePreviewCreatorService() {
        this(MorePreviewCreatorService.class.getName());
    }

    public MorePreviewCreatorService(String name) {
        super(name);
    }

    public void onCreate() {
        super.onCreate();
        this.application = MyApplication.getInstance();
    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        this.selectedTheme = intent.getStringExtra(EXTRA_SELECTED_THEME);
        this.arrayList = this.application.getSelectedImages();
        this.application.initArray();
        isImageComplate = false;
        MyApplication.More_Service_On_Off_Flag = true;
        createImages();
    }

    private void createImages() {
        this.totalImages = this.arrayList.size();
        int i = 0;
        f1944e = arrayList.size();
        Bitmap bitmap = null;
        while (i < arrayList.size() - 1 && isSameTheme() && !MyApplication.MoreServiceisBreak) {
            Bitmap a = null;
            Bitmap bitmap2;
            File imgDir = FileUtils.getImageDirectory(this.application.selectedThemeOther.toString(), i);
            Bitmap a3;
            if (i == 0) {
                try {
                    bitmap = ScalingUtilities.checkBitmap(((ImageData) arrayList.get(i)).imagePath);
                    a = ScalingUtilities.scaleCenterCrop(bitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                a3 = ScalingUtilities.m6853a(bitmap, a, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
                a.recycle();
                bitmap.recycle();
                System.gc();
                bitmap2 = a3;
            } else {
                if (bitmap == null || bitmap.isRecycled()) {
                    try {
                        bitmap = ScalingUtilities.checkBitmap(((ImageData) arrayList.get(i)).imagePath);
                        a = ScalingUtilities.scaleCenterCrop(bitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    a3 = ScalingUtilities.m6853a(bitmap, a, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
                    a.recycle();
                    bitmap.recycle();
                    bitmap = a3;
                }
                bitmap2 = bitmap;
            }
            try {
                bitmap = ScalingUtilities.checkBitmap(((ImageData) this.arrayList.get(i + 1)).imagePath);
                a = ScalingUtilities.scaleCenterCrop(bitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Bitmap a4 = ScalingUtilities.m6853a(bitmap, a, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
            System.gc();
            MoreMaskBitmapEffect.m1717a();
            MoreMaskBitmapEffect.EFFECT effect = (MoreMaskBitmapEffect.EFFECT) application.selectedThemeOther.getTheme().get(i % this.application.selectedThemeOther.getTheme().size());
            Log.e("effect", "effect" + effect.name() + "//" + application.selectedThemeOther.getTheme().size());
            int i2 = 0;
            int i3 = i;

            while (((float) i2) < TwoDMaskBitmapEffect.ANIMATED_FRAME) {
                if (isSameTheme() && !MyApplication.MoreServiceisBreak) {

                    int i4;
                    Bitmap createBitmap = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                    Paint paint = new Paint(1);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.drawBitmap(bitmap2, 0.0f, 0.0f, null);
                    canvas.drawBitmap(effect.getMask(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, i2, arrayList.get(i).getMoredirection()), 0.0f, 0.0f, paint);
                    Bitmap createBitmap2 = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Bitmap.Config.ARGB_8888);
                    canvas = new Canvas(createBitmap2);
                    canvas.drawBitmap(a4, 0.0f, 0.0f, null);
                    canvas.drawBitmap(createBitmap, 0.0f, 0.0f, new Paint());
                    if (isSameTheme()) {
                        File file = new File(imgDir, String.format("img%02d.jpg", new Object[]{Integer.valueOf(i2)}));
                        Log.e("TAG", "file ======>" + file.getAbsolutePath());
                        try {
                            if (file.exists()) {
                                file.delete();
                            }
                            OutputStream fileOutputStream = new FileOutputStream(file);
                            createBitmap2.compress(CompressFormat.JPEG, 100, fileOutputStream);
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        i4 = i3;
                        boolean isBreak = false;
                        while (this.application.isEditModeEnable) {
                            if (this.application.min_pos != Integer.MAX_VALUE) {
                                i4 = this.application.min_pos;
                                isBreak = true;
                            }
                            if (MyApplication.MoreServiceisBreak) {
                                isImageComplate = true;
                                stopSelf();
                                return;
                            }
                        }
                        if (isBreak) {
                            ArrayList<String> str = new ArrayList();
                            str.addAll(this.application.videoImages);
                            this.application.videoImages.clear();
                            int size = Math.min(str.size(), Math.max(0, i4 - i4) * 30);
                            for (int p = 0; p < size; p++) {
                                this.application.videoImages.add((String) str.get(p));
                            }
                            this.application.min_pos = Integer.MAX_VALUE;
                        }
                        if (!isSameTheme()) {
                            i3 = i4;
                            break;
                        } else {
                            this.application.videoImages.add(file.getAbsolutePath());
                            updateImageProgress();
                            if (((float) i2) == TwoDMaskBitmapEffect.ANIMATED_FRAME - 1.0f) {
                                for (i3 = 0; i3 < 8 && isSameTheme() && !MyApplication.MoreServiceisBreak; i3++) {
                                    this.application.videoImages.add(file.getAbsolutePath());
                                    updateImageProgress();
                                }
                            }
                        }
                    } else {
                        i4 = i3;
                    }
                    i2++;
                    i3 = i4;
                } else {
                    stopSelf();
                    break;
                }
            }
            i = i3 + 1;
            bitmap = a4;
        }
        Glide.get(this).clearDiskCache();
        isImageComplate = true;
        MyApplication.MoreServiceisBreak = false;
        MyApplication.More_Service_On_Off_Flag = false;
        stopSelf();
        isSameTheme();
    }

    private boolean isSameTheme() {
        return this.selectedTheme.equals(application.getCurrentTheme());
    }

    private void updateImageProgress() {
        final float progress = (100.0f * ((float) this.application.videoImages.size())) / ((float) ((this.totalImages - 1) * 30));
        Share.buffer_progress = progress;
        Log.e("TAG", "onDestroy :" + "More DDDDD =>  " + progress);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                OnProgressReceiver receiver = MorePreviewCreatorService.this.application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onImageProgressFrameUpdate(progress);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
