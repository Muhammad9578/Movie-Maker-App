package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.MusicData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.MORETHEME;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.THREEDTHEMES;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.THEMES2D;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MyApplication extends MultiDexApplication {
    public static int VIDEO_HEIGHT = 480;
    public static int VIDEO_WIDTH = 720;
    private static MyApplication instance;

    public static boolean TwoDServiceisBreak = false;
    public static boolean ThreeDServiceisBreak = false;
    public static boolean MoreServiceisBreak = false;
    public static boolean threeD_Service_On_Off_Flag = false;
    public static boolean towD_Service_On_Off_Flag = false;
    public static boolean More_Service_On_Off_Flag = false;
    public static boolean Save_Service_On_Off_Flag = false;
    public static boolean changeBackground_Flag = false;
    public static boolean no_music_command = false;
    public static boolean frame_command = false;
    public static boolean error_in_save_video = false;

    public boolean isEditModeEnable = false;
    public boolean isFromSdCardAudio = false;

    private ArrayList<String> allFolder;
    private int frame = -1;
    private int bg = -1;
    private int startBgSlide = -1;
    private int endBgSlide = -1;
    private File file = null;
    public int min_pos = Integer.MAX_VALUE;
    private float second = 2.0f;
    private String selectedFolderId = "";

    public ArrayList<String> videoImages = new ArrayList();
    public ArrayList<ImageData> selectedImages = new ArrayList();
    public ArrayList<ImageData> org_selectedImages = new ArrayList();
    public ArrayList<ImageData> temp_org_selectedImages = new ArrayList();

    private OnProgressReceiver onProgressReceiver;

    public THREEDTHEMES selectedTheme = THREEDTHEMES.Shine;
    public THEMES2D selectedTheme2d = THEMES2D.Shine2d;
    public MORETHEME selectedThemeOther = MORETHEME.MORE;
    private MusicData musicData;
    public HashMap<String, ArrayList<ImageData>> allAlbum = new HashMap<>();

    public AdRequest ins_adRequest;
    public InterstitialAd mInterstitialAd;
    private static MyApplication f1674k;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        f1674k = this;
        LoadAds();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                // .autoPromptLocation(true)
                .init();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .diskCacheExtraOptions(480, 800, null)
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MyApplication m1436a() {
        return f1674k;
    }

    public void setMusicData(MusicData musicData) {
        this.isFromSdCardAudio = false;
        this.musicData = musicData;
    }

    public void clearAllSelection() {
        this.videoImages.clear();
        allAlbum = null;
        getSelectedImages().clear();
        getOrgSelectedImages().clear();
        getTempOrgSelectedImages().clear();
        System.gc();
        getFolderList();
    }

    public static Bitmap get_overlay(Bitmap bottom) {

        Drawable drawable_overlay = getInstance().getResources().getDrawable(R.drawable.threedframe);
        Bitmap b = ((BitmapDrawable) drawable_overlay).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, VIDEO_WIDTH, VIDEO_HEIGHT, false);
        drawable_overlay = new BitmapDrawable(getInstance().getResources(), bitmapResized);

        Drawable drawable = new BitmapDrawable(MyApplication.getInstance().getResources(), getRoundedCornerBitmap(bottom, 35));
        Drawable[] layers = new Drawable[2];
        layers[0] = drawable;
        layers[1] = drawable_overlay;
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        int width = layerDrawable.getIntrinsicWidth();
        int height = layerDrawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        layerDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void getFolderList() {
        allFolder = new ArrayList();
        allAlbum = new HashMap();
        String[] projection = new String[]{"_data", "_id", "bucket_display_name", "bucket_id", "datetaken"};
        String orderBy = "bucket_display_name";
        Cursor cur = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, "bucket_display_name DESC");

        if (cur.getCount() == 0) {
            Log.e("TAG", "cur : " + cur.getCount());
            Share.image_not_found = cur.getCount();
        } else {
            if (cur.moveToFirst()) {
                int bucketColumn = cur.getColumnIndex("bucket_display_name");
                int bucketIdColumn = cur.getColumnIndex("bucket_id");
                int dateColumn = cur.getColumnIndex("datetaken");
                setSelectedFolderId(cur.getString(bucketIdColumn));
                do {
                    ImageData data = new ImageData();
                    data.imagePath = cur.getString(cur.getColumnIndex("_data"));
                    data.temp_imagePath = cur.getString(cur.getColumnIndex("_data"));
                    data.temp_org_imagePath = cur.getString(cur.getColumnIndex("_data"));
                    if (!data.imagePath.endsWith(".gif")) {
                        String date = cur.getString(dateColumn);
                        String folderName = cur.getString(bucketColumn);
                        String folderId = cur.getString(bucketIdColumn);
                        if (!this.allFolder.contains(folderId)) {
                            this.allFolder.add(folderId);
                        }
                        ArrayList<ImageData> imagePath = (ArrayList) this.allAlbum.get(folderId);
                        if (imagePath == null) {
                            imagePath = new ArrayList();
                        }
                        data.folderName = folderName;
                        imagePath.add(data);
                        allAlbum.put(folderId, imagePath);
                    }
                } while (cur.moveToNext());
            }
        }
    }

    public HashMap<String, ArrayList<ImageData>> getAllAlbum() {
        return allAlbum;
    }

    public ArrayList<ImageData> getImageByAlbum(String folderId) {
        ArrayList<ImageData> imageDatas = (ArrayList) getAllAlbum().get(folderId);
        if (imageDatas == null) {
            return new ArrayList();
        }
        return imageDatas;
    }

    public void addSelectedImage(ImageData imageData) {
        Log.e("TAG", "imagePath :" + imageData.imagePath);

        if (Share.end_slide_selected_or_not) {

            selectedImages.add(selectedImages.size() - 1, imageData);
            org_selectedImages.add(org_selectedImages.size() - 1, imageData);
            temp_org_selectedImages.add(temp_org_selectedImages.size() - 1, imageData);
            imageData.imageCount++;

        } else {
            selectedImages.add(imageData);
            org_selectedImages.add(imageData);
            temp_org_selectedImages.add(imageData);
            imageData.imageCount++;
        }
    }


    public void removeSelectedImage(int imageData) {
        if (imageData <= this.selectedImages.size()) {
            Log.e("TAG", "image imagePath : ==>" + selectedImages.get(imageData).imagePath);
            ImageData imageData2 = (ImageData) selectedImages.remove(imageData);
            org_selectedImages.remove(imageData);
            temp_org_selectedImages.remove(imageData);
            imageData2.imageCount--;
        }
    }

    public MusicData getMusicData() {
        return this.musicData;
    }

    public float getSecond() {
        return this.second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    public void setSelectedFolderId(String selectedFolderId) {
        this.selectedFolderId = selectedFolderId;
    }

    public String getSelectedFolderId() {
        return this.selectedFolderId;
    }

    public Typeface getApplicationTypeFace() {
        return null;
    }


    public void setCurrentTheme(String currentTheme) {
        Editor editor = getSharedPreferences("theme", 0).edit();
        editor.putString("current_theme", currentTheme);
        editor.commit();
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        for (RunningServiceInfo service : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setFrame(int data) {
        this.frame = data;
    }

    public int getFrame() {
        return this.frame;
    }

    public void setBg(int data) {
        this.bg = data;
    }

    public int getStartBgSlide() {
        return startBgSlide;
    }

    public void setStartBgSlide(int startBgSlide) {
        this.startBgSlide = startBgSlide;
    }

    public int getEndBgSlide() {
        return endBgSlide;
    }

    public void setEndBgSlide(int endBgSlide) {
        this.endBgSlide = endBgSlide;
    }

    public int getBg() {
        return this.bg;
    }

    public void setEffect(File data) {
        this.file = data;
    }

    public File getEffect() {
        return this.file;
    }

    public void initArray() {
        this.videoImages = new ArrayList();
    }

    public ArrayList<ImageData> getSelectedImages() {
        return this.selectedImages;
    }

    public ArrayList<ImageData> getOrgSelectedImages() {
        return org_selectedImages;
    }

    public ArrayList<ImageData> getTempOrgSelectedImages() {
        return temp_org_selectedImages;
    }

    public void setOnProgressReceiver(OnProgressReceiver onProgressReceiver) {
        this.onProgressReceiver = onProgressReceiver;
    }

    public OnProgressReceiver getOnProgressReceiver() {
        return this.onProgressReceiver;
    }

    public String getCurrentTheme() {
        return getSharedPreferences("theme", 0).getString("current_theme", THREEDTHEMES.Shine.toString());
    }

    public class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

            String launchURL = result.notification.payload.launchURL;

            String smallIcon = result.notification.payload.smallIcon;
            Log.e("smallIcon", "---->" + smallIcon);
            Log.e("launchURL", "0------->" + launchURL);

            if (launchURL != null) {
                Log.d("", "Launch URL: " + launchURL);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(launchURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                            .FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } catch (android.content.ActivityNotFoundException anfe) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(launchURL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                            .FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        }
    }

    public void LoadAds() {

        try {
            mInterstitialAd = new InterstitialAd(this);

            mInterstitialAd.setAdUnitId(getApplicationContext().getResources().getString(R.string.inter_ad_unit_id));

            ins_adRequest = new AdRequest.Builder()
                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
        }
    }

    public boolean requestNewInterstitial() {

        try {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean isLoaded() {

        try {
            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
