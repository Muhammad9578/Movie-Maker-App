package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.DragSelectedAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview.DragRecyclerView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview.OnClickListener;
import com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview.SimpleDragListener;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;


public class SelectedPhotoArrangeActivity extends AppCompatActivity implements View.OnClickListener {

    private DragRecyclerView drag_recyclerview;
    private MyApplication application = MyApplication.getInstance();
    private DragSelectedAdapter mAdapter;
    private ImageView ivBack, ivNext;
    private AdView adView;
    public static SelectedPhotoArrangeActivity activity;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(SelectedPhotoArrangeActivity.this)) {
            setContentView(R.layout.activity_selected_photo_arrange);
            //mFirebaseAnalytics = FirebaseAnalytics.getInstance(SelectedPhotoArrangeActivity.this);
            activity = this;

            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            AdRequest request = new AdRequest.Builder().build();
            interstitialAd.loadAd(request);

            initView();
            listener();

            GlobalData.loadAdsBanner(SelectedPhotoArrangeActivity.this, adView);
        }
    }

    private void initView() {

        ivNext = findViewById(R.id.ivNext);
        ivBack = findViewById(R.id.ivBack);

        drag_recyclerview = findViewById(R.id.drag_recyclerview);
        drag_recyclerview.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter = new DragSelectedAdapter(this);
        drag_recyclerview.setAdapter(mAdapter);

        mAdapter.setHandleDragEnabled(true); // default true
        mAdapter.setLongPressDragEnabled(true); // default true
        mAdapter.setSwipeEnabled(true); // default true

        mAdapter.setOnItemClickListener(new OnClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition, final int position) {

                Log.e("TAG", "adapterPosition =>" + adapterPosition);
                if (position == 1) {

                    application.removeSelectedImage(adapterPosition);
                    mAdapter.notifyDataSetChanged();

                    if (application.getOrgSelectedImages().size() == 0) {
                        finish();
                    }

                } else if (position == 2) {

                    Share.selected_crop_image_position = adapterPosition;
                    Share.temp_selected_crop_image_position = Share.temp_selected_crop_image_position + 1;
                    Share.BG_GALLERY = Uri.parse("file:///" + application.getSelectedImages().get(adapterPosition).getImagePath());
                    Intent intent = new Intent(SelectedPhotoArrangeActivity.this, CropActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Vibrator v1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v1.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v1.vibrate(200);
                }
            }
        });

        mAdapter.setOnItemDragListener(new SimpleDragListener() {
            @Override
            public void onDrop(int fromPosition, int toPosition) {
                super.onDrop(fromPosition, toPosition);

                Log.e("drag", "onDrop " + fromPosition + " -> " + toPosition);

            }

            @Override
            public void onSwiped(int pos) {
                super.onSwiped(pos);
                Log.e("drag", "onSwiped " + pos);
            }
        });
    }

    private void listener() {
        ivNext.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {
            if (!MyApplication.getInstance().isLoaded())
                MyApplication.getInstance().LoadAds();

            if (mAdapter != null) {
                mAdapter.notifyItemChanged(Share.selected_crop_image_position);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivNext) {
            if (application.getOrgSelectedImages().size() > 2) {
                Next();
            } else {
                Toast.makeText(this, "Select atleast 3 image to create video", Toast.LENGTH_SHORT).show();
            }
        } else if (v == ivBack) {
            finish();
        }
    }

    private void redirectActivity() {
        // if preview is running and user add more image (stop service) //
        MyApplication.TwoDServiceisBreak = MyApplication.towD_Service_On_Off_Flag;

        MyApplication.ThreeDServiceisBreak = MyApplication.threeD_Service_On_Off_Flag;

        MyApplication.MoreServiceisBreak = MyApplication.More_Service_On_Off_Flag;

        if (Share.isPreviewActivity) {
            if (PhotoPickupImageActivity.activity != null) {
                PhotoPickupImageActivity.activity.finish();
            }
            finish();
        } else {
            Share.FirstTimePreviewActivity = true;
            startActivity(new Intent(this, CustomSlideActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;

        if (ivNext != null) {
            ivNext.setOnClickListener(null);
            ivBack.setOnClickListener(null);
            drag_recyclerview.getRecycledViewPool().clear();
            drag_recyclerview = null;
            mAdapter = null;
        }
    }

    public void Next(){
        if(interstitialAd.isLoaded()){
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
                    redirectActivity();
                }
            });
        }else {
            redirectActivity();
        }

    }
}
