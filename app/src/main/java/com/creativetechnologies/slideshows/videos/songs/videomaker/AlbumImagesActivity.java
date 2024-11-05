package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.PhoneAlbumImagesAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.util.ArrayList;

/**
 * Created by admin on 11/18/2016.
 */
public class AlbumImagesActivity extends AppCompatActivity {
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rcv_album_images;
    private PhoneAlbumImagesAdapter albumAdapter;
    public static ImageView iv_more_app, iv_blast;
  //  private FirebaseAnalytics mFirebaseAnalytics;
    AdView adView;
    public static AlbumImagesActivity activity;
    public static Boolean is_closed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(AlbumImagesActivity.this)) {
            setContentView(R.layout.activity_albumimages);
          //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(AlbumImagesActivity.this);

            setToolbar();
            activity = this;
            initView();
            GlobalData.loadAdsBanner(AlbumImagesActivity.this, adView);
        }
    }

    private void initView() {

        rcv_album_images = findViewById(R.id.rcv_album_images);
        gridLayoutManager = new GridLayoutManager(AlbumImagesActivity.this, 3);
        rcv_album_images.setLayoutManager(gridLayoutManager);

        ArrayList<String> al_album_images = getIntent().getStringArrayListExtra("image_list");

        albumAdapter = new PhoneAlbumImagesAdapter(this, al_album_images);
        rcv_album_images.setAdapter(albumAdapter);

        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();
        }

        iv_more_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                is_closed = false;
                iv_more_app.setVisibility(View.GONE);
                iv_blast.setVisibility(View.VISIBLE);
                ((AnimationDrawable) iv_blast.getBackground()).start();

                if (MyApplication.getInstance().requestNewInterstitial()) {

                    MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            iv_blast.setVisibility(View.GONE);
                            iv_more_app.setVisibility(View.GONE);
                            is_closed = true;
                        /*iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
                        ((AnimationDrawable) iv_more_app.getBackground()).start();*/
                            loadInterstialAd();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            iv_blast.setVisibility(View.GONE);
                            iv_more_app.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            is_closed = false;
                            iv_blast.setVisibility(View.GONE);
                            iv_more_app.setVisibility(View.GONE);
                        }
                    });
                } else {
                    iv_blast.setVisibility(View.GONE);
                    iv_more_app.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadInterstialAd() {
        try {
            if (MyApplication.getInstance().mInterstitialAd != null) {
                if (MyApplication.getInstance().mInterstitialAd.isLoaded()) {
                    iv_more_app.setVisibility(View.VISIBLE);
                } else {
                    MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                    MyApplication.getInstance().mInterstitialAd = null;
                    MyApplication.getInstance().ins_adRequest = null;
                    MyApplication.getInstance().LoadAds();
                    MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            iv_more_app.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            iv_more_app.setVisibility(View.GONE);
                            //ColoringApplication.getInstance().LoadAds();
                            loadInterstialAd();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToolbar() {
        try {
            String albumName = getIntent().getExtras().getString(GlobalData.KEYNAME.ALBUM_NAME);
            Toolbar toolbar = findViewById(R.id.toolbar);

            TextView tv_album_title = findViewById(R.id.tv_album_title);
            tv_album_title.setText(albumName);

            ImageView iv_back = findViewById(R.id.iv_back);
            setSupportActionBar(toolbar);

            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {

            if (Share.isNeedToAdShow(getApplicationContext()))
                if (is_closed) {
                    loadInterstialAd();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}