package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.BuildConfig;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
//import com.google.android.gms.ads.formats.UnifiedNativeAd;
//import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import java.io.File;

public class SaveVideoActivity extends AppCompatActivity implements UniversalVideoView.VideoViewCallback, View.OnClickListener {

    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    View mVideoLayout;
    private int mSeekPosition;
    private int cachedHeight;
    public boolean isFullscreen;
    private FrameLayout nativeAdLayout;
    private RelativeLayout ll_pager_indicator;
    private ImageView iv_facebook_share, iv_instagram_share, iv_email_share, iv_whatsup_share, iv_share_image, ivBack, ivDelete;
    private Toolbar toolbar_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if (Share.RestartAppForOnlyStorage(SaveVideoActivity.this)) {
            setContentView(R.layout.activity_save_video);

         //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(SaveVideoActivity.this);

            NativeAD();
            ll_pager_indicator = (RelativeLayout) findViewById(R.id.ll_pager_indicator);

            int height = (int) (getApplicationContext().getResources().getDisplayMetrics().heightPixels * 0.11);
            ll_pager_indicator.getLayoutParams().height = height;

            nativeAdLayout = (FrameLayout)findViewById(R.id.id_native_ad);
            iv_facebook_share = (ImageView) findViewById(R.id.iv_facebook_share);
            iv_instagram_share = (ImageView) findViewById(R.id.iv_instagram_share);
            iv_email_share = (ImageView) findViewById(R.id.iv_email_share);
            iv_whatsup_share = (ImageView) findViewById(R.id.iv_whatsup_share);
            iv_share_image = (ImageView) findViewById(R.id.iv_share_image);
            ivBack = (ImageView) findViewById(R.id.ivBack);
            ivDelete = (ImageView) findViewById(R.id.ivDelete);

            toolbar_top = (Toolbar) findViewById(R.id.toolbar_top);

            mVideoLayout = findViewById(R.id.video_layout);
            mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
            mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
            mVideoView.setMediaController(mMediaController);
            setVideoAreaSize();

            mVideoView.setVideoViewCallback(this);

            //********** Sharing Option ***********//

            iv_facebook_share.setOnClickListener(this);
            iv_instagram_share.setOnClickListener(this);
            iv_email_share.setOnClickListener(this);
            iv_whatsup_share.setOnClickListener(this);
            iv_share_image.setOnClickListener(this);
            ivBack.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }
//    }

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                mVideoView.setVideoPath(Share.video_path);
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }
    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            if (mVideoLayout != null) {
                mVideoView.setFullscreen(false);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            Log.e("TAG", "isFullscreen if :" + isFullscreen);
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            toolbar_top.setVisibility(View.GONE);
            ll_pager_indicator.setVisibility(View.GONE);
            nativeAdLayout.setVisibility(View.GONE);

        } else {
            Log.e("TAG", "isFullscreen else :" + isFullscreen);
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            nativeAdLayout.setVisibility(View.VISIBLE);
            ll_pager_indicator.setVisibility(View.VISIBLE);
            toolbar_top.setVisibility(View.VISIBLE);
        }

//        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onClick(View v) {

        if (v == ivBack) {
            finish();
        } else if (v == ivDelete) {
            mVideoView.pause();

            if (Share.from_my_video) {
                deleteMyVideo();
            } else {
                deleteVideo();
            }
        } else if (v == iv_instagram_share) {
            boolean isAppInstalled = appInstalledOrNot("com.instagram.android");

            try {
                if (isAppInstalled) {
                    Uri fileImagePath =  Uri.fromFile(new File(Share.video_path));
                            //FileProvider.getUriForFile(SaveVideoActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(Share.video_path));
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("video/*");
                    shareIntent.setPackage("com.instagram.android");
                    //shareIntent.putExtra(Intent.EXTRA_TEXT, "Flower Crown Photo Editor is an photo editor app that that offers you a variety of “photo Flower stickers” to add to your photos and give a virtual makeover to your hair in a second. Download and give us a review." + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Make your video using different effect" + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileImagePath);

                    try {
                        startActivity(shareIntent);
                    } catch (android.content.ActivityNotFoundException anfe) {
                        anfe.printStackTrace();
                    }

                } else {
                    final String appPackageName = "com.instagram.android";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    // Toast.makeText(getApplicationContext(), "Instagram have not been installed", Toast.LENGTH_SHORT).show();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went to wrong.", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went to wrong.", Toast.LENGTH_SHORT).show();
            }
        } else if (v == iv_facebook_share) {
            try {
                boolean isAppInstalled = appInstalledOrNot("com.facebook.katana");
                if (isAppInstalled) {
                    //shareInFb();

                    Uri fileImagePath = FileProvider.getUriForFile(SaveVideoActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(Share.video_path));

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setPackage("com.facebook.katana");
                    //shareIntent.putExtra(Intent.EXTRA_TEXT, "Flower Crown Photo Editor is an photo editor app that that offers you a variety of “photo Flower stickers” to add to your photos and give a virtual makeover to your hair in a second. Download and give us a review." + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Make your video using different effect" + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileImagePath);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(shareIntent);

                } else {
                    // Toast.makeText(getApplicationContext(), "Facebook have been not installed", Toast.LENGTH_SHORT).show();
                    final String appPackageName = "com.facebook.katana";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Something went to wrong.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SaveVideoActivity.this, "Large video not share.", Toast.LENGTH_LONG).show();
            }
        } else if (v == iv_email_share) {
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setData(Uri.parse("mailto:"));
            i.putExtra(Intent.EXTRA_SUBJECT, getApplicationContext().getResources().getString(R.string.app_name));

            i.putExtra(Intent.EXTRA_TEXT, "Make your video using different effect" + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Share.video_path)));
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SaveVideoActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else if (v == iv_whatsup_share) {
            boolean isAppInstalled = appInstalledOrNot("com.whatsapp");

            try {
                if (isAppInstalled) {

                    Uri fileImagePath = Uri.fromFile(new File(Share.video_path));
                  //  Uri fileImagePath = FileProvider.getUriForFile(SaveVideoActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(Share.video_path));
                    Log.e("TAG", "iv_whatsup_share");
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.setPackage("com.whatsapp");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this awesome app to change your look using " + getApplicationContext().getResources().getString(R.string.app_name) + " app different social media.\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileImagePath);
                    startActivity(shareIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Whatsapp has not been installed", Toast.LENGTH_SHORT).show();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went to wrong.", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went to wrong.", Toast.LENGTH_SHORT).show();
            }
        } else if (v == iv_share_image) {
            try {
                Uri fileImagePath = Uri.fromFile(new File(Share.video_path));
               // Uri fileImagePath = FileProvider.getUriForFile(SaveVideoActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(Share.video_path));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_SUBJECT, getApplicationContext().getResources().getString(R.string.app_name));
                intent.setType("image/*");
                //intent.putExtra(Intent.EXTRA_TEXT, "Flower Crown Photo Editor is an photo editor app that that offers you a variety of “photo Flower stickers” to add to your photos and give a virtual makeover to your hair in a second. Download and give us a review." + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                intent.putExtra(Intent.EXTRA_TEXT, "Make your video using different effect" + "\n\n https://play.google.com/store/apps/details?id=" + getPackageName());
                intent.putExtra(Intent.EXTRA_STREAM, fileImagePath);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share Image"));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void deleteVideo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SaveVideoActivity.this, R.style.MyAlertDialog);
        builder.setMessage("Are you sure want to delete video ?");
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                File f = new File(Share.video_path);
                if (f.exists()) {
                    Log.e("TAG", "img:" + f);
                    f.delete();

                    finish();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteMyVideo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SaveVideoActivity.this, R.style.MyAlertDialog);
        builder.setMessage("Are you sure want to delete video ?");
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                File f = new File(Share.video_path);
                if (f.exists()) {
                    Log.e("TAG", "img:" + f);
                    f.delete();

                    if (MyVideoActivity.al_my_photos.size() != 0) {
                        MyVideoActivity.al_my_photos.remove(Share.selected_theme_position);
                    }

                    finish();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
