package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;


public class FullScreenAdActivity extends Activity implements View.OnClickListener {
    private Intent intent;
    private Button btn_cancel, btn_download;
    private ImageView imageView;
    private RelativeLayout relative;
    private Animation zoomin, zoomout, buttonanim;
    private boolean is_back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.full_screen_ad);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        findParams();
        setListners();
        InitAction();
    }

    private void findParams() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_download = (Button) findViewById(R.id.btn_download);
        imageView = (ImageView) findViewById(R.id.cock);
        relative = (RelativeLayout) findViewById(R.id.relative);
    }

    private void setListners() {
        btn_download.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    private void InitAction() {
        try {
            // TODO: 2/27/2018 if full ad image from storage
            if (GlobalData.al_ad_full_image_from_storage != null && GlobalData.al_ad_full_image_from_storage.size() > 0) {
                Bitmap mBitmap = BitmapFactory.decodeFile(GlobalData.al_ad_full_image_from_storage.get(GlobalData.AD_index).toString());
                imageView.setImageBitmap(mBitmap);
            } else {
                // TODO: 2/27/2018 if no image in storage then load from direct api arraylist
                if (!GlobalData.al_ad_full_image_from_api.get(GlobalData.AD_index).getFull_img().equalsIgnoreCase("")) {
                    byte[] b = Base64.decode(GlobalData.al_ad_full_image_from_api.get(GlobalData.AD_index).getFull_img(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    imageView.setImageBitmap(bitmap);
                }
            }

            zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);
            buttonanim = AnimationUtils.loadAnimation(this, R.anim.shake);
            btn_download.setAnimation(buttonanim);

            buttonanim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    btn_download.startAnimation(buttonanim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            zoomin.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    imageView.startAnimation(zoomout);

                }
            });

            zoomout.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    imageView.startAnimation(zoomin);

                }
            });

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("TAG", "GlobalData.al_ad_package_name:==>" + GlobalData.al_ad_package_name);
                    Log.e("TAG", "GlobalData.AD_index:==>" + GlobalData.AD_index);
                    try {
                        if (GlobalData.al_ad_full_image_from_storage != null && GlobalData.al_ad_full_image_from_storage.size() > 0) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", new Object[]{GlobalData.al_ad_full_image_from_storage.get(GlobalData.AD_index).getName().replace("_", ".").replace(".jpg", "")})));
                            startActivity(intent);
                        } else {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", new Object[]{GlobalData.al_ad_full_image_from_api.get(GlobalData.AD_index).getPackage_name()})));
                            startActivity(intent);
                        }
//                        if (GlobalData.al_ad_package_name != null && GlobalData.al_ad_package_name.size() > 0) {
//                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", new Object[]{GlobalData.al_ad_package_name.get(GlobalData.AD_index)})));
//                            startActivity(intent);
//                        } else {
//                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", new Object[]{GlobalData.al_ad_package_name.get(GlobalData.AD_index)})));
//                            startActivity(intent);
//                        }


                    } catch (Exception e) {
                        String appPackageName = "";
                        if (GlobalData.al_ad_full_image_from_storage != null && GlobalData.al_ad_full_image_from_storage.size() > 0) {
                            appPackageName = GlobalData.al_ad_full_image_from_storage.get(GlobalData.AD_index).getName().replace("_", ".").replace(".jpg", "");
                        } else {
                            appPackageName = GlobalData.al_ad_full_image_from_api.get(GlobalData.AD_index).getPackage_name();
                        }
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            startActivity(intent);

                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                    finish();
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_cancel) {
            finish();
        } else if (v == btn_download) {
            try {
                if (GlobalData.al_ad_full_image_from_storage != null && GlobalData.al_ad_full_image_from_storage.size() > 0) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", new Object[]{GlobalData.al_ad_full_image_from_storage.get(GlobalData.AD_index).getName().replace("_", ".").replace(".jpg", "")})));
                    startActivity(intent);
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://details?id=%1$s", new Object[]{GlobalData.al_ad_full_image_from_api.get(GlobalData.AD_index).getPackage_name()})));
                    startActivity(intent);
                }

            } catch (Exception e) {
                String appPackageName = "";
                if (GlobalData.al_ad_full_image_from_storage != null && GlobalData.al_ad_full_image_from_storage.size() > 0) {
                    appPackageName = GlobalData.al_ad_full_image_from_storage.get(GlobalData.AD_index).getName().replace("_", ".").replace(".jpg", "");
                } else {
                    appPackageName = GlobalData.al_ad_full_image_from_api.get(GlobalData.AD_index).getPackage_name();
                }
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    startActivity(intent);

                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
