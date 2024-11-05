package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashMenuActivity extends Activity implements View.OnClickListener {

    private static final String TAG = SplashMenuActivity.class.getSimpleName();
    private List<String> listPermissionsNeeded = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 23;

    private RelativeLayout splash_rate, splash_gallery, splash_albums, splash_more;
    //    private FrameLayout frameLayout1, frameLayout2;
    private ImageView iv_logo, iv_ntvads;
    private String image_name = "";

    MyApplication application = MyApplication.getInstance();
    boolean isLoadImageSelection = false;
    boolean isLoadMyVideoActivity = false;
    boolean isOpenPermissionDialog = false;
    private InterstitialAd interstitialAd;
    public static final int RequestPermissionCode = 1;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu_splash);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);

        NativeAD();

//        GlobalData.loadAdsBanner(Splash_MenuActivity.this, adView);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
//        if(firstStart)
//        {
//            StartDailogue();
//        }

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        GlobalData.screenHeight = display.getHeight();
        GlobalData.screenWidth = display.getWidth();
        SharedPrefs.save(this, SharedPrefs.SHOWFACE, "false");
        SharedPrefs.removeKey(this, SharedPrefs.BACKGROUND_IMAGE);
        SharedPrefs.save(SplashMenuActivity.this, SharedPrefs.screen_hight, GlobalData.screenHeight);
        SharedPrefs.save(SplashMenuActivity.this, SharedPrefs.screen_width, GlobalData.screenWidth);

        //initFFMPEG();
        initView();
        if (checkPermission()) {

//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();

        } else {

            requestPermission();
        }


        // if user remove app from recent
        if (!SharedPrefs.getString(SplashMenuActivity.this, SharedPrefs.video_created_or_not).equals("")) {
            if (SharedPrefs.getString(SplashMenuActivity.this, SharedPrefs.video_created_or_not).equals("true")) {
                File file = new File(SharedPrefs.getString(SplashMenuActivity.this, SharedPrefs.video_path));
                file.delete();
            }
        }
//        Point size = new Point();
//        display.getSize(size);
//        MyApplication.VIDEO_WIDTH = size.x;
//        MyApplication.VIDEO_HEIGHT = (int) (size.x / 1.5);

    }

    /*private void initFFMPEG() {
        try {
            if (ffmpeg == null) {
                Log.d("TAG", "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {
                    Log.e("TAG", "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("TAG", "EXception no controlada : " + e);
        }
    }*/

    private void initView() {

        splash_gallery = (RelativeLayout) findViewById(R.id.splash_gallery);
        splash_albums = (RelativeLayout) findViewById(R.id.splash_albums);

//        Glide.with(Splash_MenuActivity.this).load(GlobalData.ntv_img).override(300, 300).into(iv_ntvads);

//        iv_ntvads.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (GlobalData.ntv_img != null) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalData.ntv_inglink));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }
//        });
        splash_gallery.setOnClickListener(this);
        splash_albums.setOnClickListener(this);

    }

    private void setDefaultBG() {
        Bitmap b = ((BitmapDrawable) getResources().getDrawable(R.drawable.bg1)).getBitmap();
        Utils.backgrnd = Bitmap.createScaledBitmap(b, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, false);
    }

    private void imageSelection() {
        try {
            if (!isOpenPermissionDialog) {
                if (checkAndRequestPermissions(1) && !isLoadImageSelection) {
                    MyApplication.getInstance().getFolderList();
                    if (Share.image_not_found != 0) {
                        Intent intent = new Intent(SplashMenuActivity.this, PhotoPickupImageActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SplashMenuActivity.this, "Images not available.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void myVideoSelection() {
        try {
            if (!isOpenPermissionDialog) {
                if (checkAndRequestPermissions(2) && !isLoadMyVideoActivity) {
                    Intent intent = new Intent(SplashMenuActivity.this, MyVideoActivity.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteEffectFile() {

        try {
            FileUtils.TEMP_DIRECTORY_IMAGES.mkdirs();
            FileUtils.TEMP_DIRECTORY_IMAGES1.mkdirs();
            FileUtils.TEMP_DIRECTORY_IMAGES_CROP.mkdirs();
            FileUtils.TEMP_DIRECTORY_SLIDE.mkdirs();

            File[] contents = FileUtils.TEMP_DIRECTORY_IMAGES.listFiles();
            File[] contents_temp = FileUtils.TEMP_DIRECTORY_IMAGES1.listFiles();
            File[] crop_temp = FileUtils.TEMP_DIRECTORY_IMAGES_CROP.listFiles();
            File[] slide_temp = FileUtils.TEMP_DIRECTORY_SLIDE.listFiles();

            System.gc();

            if (contents_temp != null) {
                if (contents_temp.length != 0) {
                    for (int i = 0; i < contents_temp.length; i++) {
                        FileUtils.deleteFile(contents_temp[i].getAbsoluteFile());
                    }
                }
            }

            if (contents != null) {
                if (contents.length != 0) {
                    for (int i = 0; i < contents.length; i++) {
                        FileUtils.deleteFile(contents[i].getAbsoluteFile());
                    }
                }
            }

            if (crop_temp != null) {
                if (crop_temp.length != 0) {
                    for (int i = 0; i < crop_temp.length; i++) {
                        FileUtils.deleteFile(crop_temp[i].getAbsoluteFile());
                    }
                }
            }

            if (slide_temp != null) {
                if (slide_temp.length != 0) {
                    for (int i = 0; i < slide_temp.length; i++) {
                        FileUtils.deleteFile(slide_temp[i].getAbsoluteFile());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == splash_gallery) {

            GalleryImages();

        } else if (v == splash_albums) {

            MyVideos();
            // myVideoSelection();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent == null) {
            intent = new Intent();
        }
        super.startActivityForResult(intent, requestCode);
    }

    private boolean checkAndRequestPermissions(int code) {

        if (ContextCompat.checkSelfPermission(SplashMenuActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(SplashMenuActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashMenuActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                    code);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        if (!MyApplication.getInstance().isLoaded())
            MyApplication.getInstance().LoadAds();

        application.getSelectedImages().clear();
        application.getOrgSelectedImages().clear();
        application.getTempOrgSelectedImages().clear();

        deleteEffectFile();
        setDefaultBG();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Dialog dialog = new Dialog(SplashMenuActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.choose_category_alert1); //get layout from ExitDialog folder

            SmileRating smileRating = (SmileRating) dialog.findViewById(R.id.smile_rating);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);
            Button btn_no = dialog.findViewById(R.id.btn_no);

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    System.exit(0);
                    finish();

                }
            });

            smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                @Override
                public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                    switch (smiley) {
                        case SmileRating.BAD:
                            Toast.makeText(SplashMenuActivity.this, "Thanks for review", Toast.LENGTH_SHORT).show();
                            break;
                        case SmileRating.GOOD:
                            rate_app();
                            break;
                        case SmileRating.GREAT:
                            rate_app();
                            break;
                        case SmileRating.OKAY:
                            Toast.makeText(SplashMenuActivity.this, "Thanks for review", Toast.LENGTH_SHORT).show();
                            break;
                        case SmileRating.TERRIBLE:
                            Toast.makeText(SplashMenuActivity.this, "Thanks for review", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void rate_app() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(SplashMenuActivity.this, new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        INTERNET,
                        CAMERA,
                        VIBRATE

                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean INTERNET = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean CAMERA = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean VIBRATE = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE && INTERNET && CAMERA && VIBRATE) {

                        Toast.makeText(SplashMenuActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SplashMenuActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int FivePermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FivePermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public void GalleryImages() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    imageSelection();
                }
            });
        } else {
            imageSelection();
        }

    }

    public void MyVideos() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    myVideoSelection();
                }
            });
        } else {
            myVideoSelection();
        }

    }

    public void NativeAD() {

        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.native_ad_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        //the native ad will be available inside this method  (unifiedNativeAd)

                        UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                        mapUnifiedNativeAdToLayout(unifiedNativeAd, unifiedNativeAdView);

                        FrameLayout nativeAdLayout = findViewById(R.id.id_native_ad);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.addView(unifiedNativeAdView);
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {
        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));
        myAdView.setBodyView(myAdView.findViewById(R.id.ad_body));
        myAdView.setCallToActionView(myAdView.findViewById(R.id.ad_call_to_action));
        myAdView.setIconView(myAdView.findViewById(R.id.ad_icon));
        myAdView.setPriceView(myAdView.findViewById(R.id.ad_price));
        myAdView.setStarRatingView(myAdView.findViewById(R.id.ad_rating));
        myAdView.setStoreView(myAdView.findViewById(R.id.ad_store));
        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button) myAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) myAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null) {
            myAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) myAdView.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getStore() == null) {
            myAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);
    }
}
