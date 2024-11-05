package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.fragment.MainFragment;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.io.File;

/**
 * Created by admin on 1/11/2017.
 */
public class CropActivity extends AppCompatActivity {
  //  private FirebaseAnalytics mFirebaseAnalytics;
    private MyApplication application = MyApplication.getInstance();
    private AdView adView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        start_crop(savedInstanceState);

        GlobalData.loadAdsBanner(CropActivity.this, adView);
    }

    private void start_crop(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, MainFragment.getInstance()).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void startResultActivity(Bitmap bitmap) {

        if (Share.FROM_CUSTOM_SLIDE) {
            Share.FROM_CUSTOM_SLIDE = false;
            Share.GALLERY_BITMAP = bitmap;
        } else {
            String path = getTempBitmap();
            application.getSelectedImages().get(Share.selected_crop_image_position).setImagePath(path);
            application.getOrgSelectedImages().get(Share.selected_crop_image_position).setTemp_imagePath(path);
        }

        finish();
    }

    public String getTempBitmap() {
        File directory = new File(FileUtils.TEMP_DIRECTORY_IMAGES_CROP.getAbsolutePath());
        File f = new File(directory, "crop_image" + Share.temp_selected_crop_image_position + ".png");
        return f.getAbsolutePath();
    }
}