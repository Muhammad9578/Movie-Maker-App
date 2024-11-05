package com.creativetechnologies.slideshows.videos.songs.videomaker.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.OnProgressReceiver;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
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

public class TwoDPreviewCreatorService extends IntentService {
    public static final String EXTRA_SELECTED_THEME = "selected_theme";
    public static boolean isImageComplate = false;
    MyApplication application;
    ArrayList<ImageData> arrayList;
    private String selectedTheme;
    int totalImages;

    public TwoDPreviewCreatorService() {
        this(TwoDPreviewCreatorService.class.getName());
    }

    public TwoDPreviewCreatorService(String name) {
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
        MyApplication.towD_Service_On_Off_Flag = true;
        createImages();
    }

    private void createImages() {
        this.totalImages = this.arrayList.size();
        int i = 0;
        while (i < this.arrayList.size()) {
            if (!isSameTheme() || MyApplication.TwoDServiceisBreak) {
                break;
            }
            Bitmap newFirstBmp;
            File imgDir = FileUtils.getImageDirectory(this.application.selectedTheme2d.toString(), i);
            Bitmap firstBitmap = null;
            Bitmap temp = null;
            try {
                firstBitmap = ScalingUtilities.checkBitmap(this.arrayList.get(i).imagePath);
                temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            newFirstBmp = ScalingUtilities.ConvetrSameSizeNew(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            temp.recycle();
            firstBitmap.recycle();
            System.gc();
            TwoDMaskBitmapEffect.EFFECT effect = this.application.selectedTheme2d.getTheme2d().get(i % this.application.selectedTheme2d.getTheme2d().size());
            Log.e("effect", "effect" + effect.name() + "//" + application.selectedTheme2d.getTheme2d().size());
//            Utils.backgrnd = drawableToBitmap(getResources().getDrawable(R.drawable.bac));
            effect.initBitmaps(newFirstBmp, null, i);

            int j = 0;

            while (((float) j) < TwoDMaskBitmapEffect.ANIMATED_FRAME) {
                if (isSameTheme() && !MyApplication.TwoDServiceisBreak) {

                    Bitmap bitmap3 = effect.getMask(newFirstBmp, null, j, arrayList.get(i).getDirection());
                    if (!isSameTheme()) {
                        break;
                    }
                    File file = new File(imgDir, String.format("img%02d.jpg", Integer.valueOf(j)));
                    Log.e("TAG", "file ======>" + file.getAbsolutePath());
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        OutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap3.compress(CompressFormat.JPEG, 90, fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    boolean isBreak = false;
                    while (this.application.isEditModeEnable) {
                        if (this.application.min_pos != Integer.MAX_VALUE) {
                            i = this.application.min_pos;
                            isBreak = true;
                        }
                        if (MyApplication.TwoDServiceisBreak) {
                            isImageComplate = true;
                            stopSelf();
                            return;
                        }
                    }
                    if (isBreak) {
                        ArrayList<String> str = new ArrayList();
                        str.addAll(this.application.videoImages);
                        this.application.videoImages.clear();
                        int size = Math.min(str.size(), Math.max(0, i - i) * 30);
                        for (int p = 0; p < size; p++) {
                            this.application.videoImages.add(str.get(p));
                        }
                        this.application.min_pos = Integer.MAX_VALUE;
                    }
                    if (!isSameTheme() || MyApplication.TwoDServiceisBreak) {
                        break;
                    }
                    this.application.videoImages.add(file.getAbsolutePath());
                    updateImageProgress();
                    if (((float) j) == TwoDMaskBitmapEffect.ANIMATED_FRAME - 1.0f) {
                        for (int m = 0; m < 8 && isSameTheme() && !MyApplication.TwoDServiceisBreak; m++) {
                            this.application.videoImages.add(file.getAbsolutePath());
                            updateImageProgress();
                        }
                    }
                    j++;
                } else {
                    stopSelf();
                    break;
                }
            }
            i++;

            if (totalImages == i) {
                isImageComplate = true;
            }
        }

        Glide.get(this).clearDiskCache();
        isImageComplate = true;
        MyApplication.TwoDServiceisBreak = false;
        MyApplication.towD_Service_On_Off_Flag = false;
        stopSelf();
        isSameTheme();
    }

    private boolean isSameTheme() {
        return this.selectedTheme.equals(this.application.getCurrentTheme());
    }

    private void updateImageProgress() {
        final float progress = (100.0f * ((float) this.application.videoImages.size())) / ((float) ((this.totalImages) * 30));
        Share.buffer_progress = progress;
        Log.e("TAG", "onDestroy :" + "Two DDDDD =>  " + progress);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                OnProgressReceiver receiver = TwoDPreviewCreatorService.this.application.getOnProgressReceiver();
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
