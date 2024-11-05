package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.fragment.PhotoFragment;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;


public class PhotoPickupActivity extends AppCompatActivity {
    private static final String TAG = PhotoPickupActivity.class.getSimpleName();

    private ImageView iv_close;
    private FrameLayout simpleFrameLayout;
    private TextView tv_title;
    public static ImageView iv_more_app, iv_blast;
    private AdView adView;

    public static PhotoPickupActivity activity;
    public static Boolean is_closed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(PhotoPickupActivity.this)) {
            setContentView(R.layout.activity_photo_pickup);
          //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(PhotoPickupActivity.this);
            setToolbar();
            activity = this;
            GlobalData.loadAdsBanner(PhotoPickupActivity.this, adView);
            initView();
            initViewAction();
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        tv_title = (TextView) toolbar.findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.Photo));

        iv_more_app = (ImageView) findViewById(R.id.iv_more_app);
        iv_blast = (ImageView) findViewById(R.id.iv_blast);

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

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {
            if (Share.isNeedToAdShow(getApplicationContext())) {
                if (is_closed) {
                    loadInterstialAd();
                }
            }
        }
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

    private void initView() {
        iv_close = (ImageView) findViewById(R.id.iv_close);
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViewAction() {
        Fragment fragment = null;
        fragment = PhotoFragment.newInstance();
        updateFragment(fragment);
        updateFragment(PhotoFragment.newInstance());
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void onCloseFace(View view) {
        onBackPressed();
    }

    public static PhotoPickupActivity getFaceActivity() {
        return activity;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Share.FROM_CUSTOM_SLIDE = false;
        Share.resume_flag = false;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}
