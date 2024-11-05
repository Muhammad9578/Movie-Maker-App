package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.TwoDEffectListAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.view.ItemOffsetDecoration;

public class TwoDEffectListActvity extends AppCompatActivity implements View.OnClickListener {

    private Integer[] ids = new Integer[]{R.drawable.left, R.drawable.right, R.drawable.up, R.drawable.bottom,
            R.drawable.topleft, R.drawable.topright, R.drawable.bottomleft, R.drawable.bottomright};
    private String[] names = new String[]{"right", "left", "bottom", "up", "bottomright", "bottomleft", "topright", "topleft"};
    private RecyclerView rvTwoDEffect;
    private TwoDEffectListAdapter twoDEffectListAdapter;
    private MyApplication application = MyApplication.getInstance();
    private ImageView ivBack;
    public static ImageView iv_more_app, iv_blast;
    public static Boolean is_closed = true;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(TwoDEffectListActvity.this)) {
            setContentView(R.layout.activity_two_deffect_list_actvity);

           // mFirebaseAnalytics = FirebaseAnalytics.getInstance(TwoDEffectListActvity.this);
            initView();
        }
    }

    private void initView() {

        rvTwoDEffect = (RecyclerView) findViewById(R.id.rvTwoDEffect);
        rvTwoDEffect.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvTwoDEffect.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.txt0);
        rvTwoDEffect.addItemDecoration(itemDecoration);

        ivBack = (ImageView) findViewById(R.id.ivBack);

        iv_more_app = (ImageView) findViewById(R.id.iv_more_app);
        iv_blast = (ImageView) findViewById(R.id.iv_blast);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();
        }

        ivBack.setOnClickListener(this);
        iv_more_app.setOnClickListener(this);

        GlobalData.loadAdsBanner(TwoDEffectListActvity.this, adView);


        load2DEffectList();
    }

    private void load2DEffectList() {

        twoDEffectListAdapter = new TwoDEffectListAdapter(TwoDEffectListActvity.this);
        rvTwoDEffect.setAdapter(twoDEffectListAdapter);

        twoDEffectListAdapter.setEventListener(new TwoDEffectListAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                // this flags are used for apply twoD effect
                Share.TwoDEffectPreviewActivity = true;
                Share.twoD_Effect_Flag = true;
                Share.threeD_Effect_Flag = false;
                Share.more_Effect_Flag = false;

                Share.pause_video_flag = true;

                // when user apply 3d effect this => flag used for remove 2d/more effect
                Share.threeDEffectApply = false;

                // when user apply more effect => this flag used for remove 2d effect
                Share.twoDEffectApply = false;

                // for save video //
                FinalPreviewActivity.playThreeDEffect = false;
                FinalPreviewActivity.playTwoDEffect = false;
                FinalPreviewActivity.playMoreEffect = false;

                if (MyApplication.towD_Service_On_Off_Flag) {
                    MyApplication.TwoDServiceisBreak = true;
                } else {
                    MyApplication.TwoDServiceisBreak = false;
                }

                if (MyApplication.threeD_Service_On_Off_Flag) {
                    MyApplication.ThreeDServiceisBreak = true;
                } else {
                    MyApplication.ThreeDServiceisBreak = false;
                }

                if (MyApplication.More_Service_On_Off_Flag) {
                    MyApplication.MoreServiceisBreak = true;
                } else {
                    MyApplication.MoreServiceisBreak = false;
                }

                FinalPreviewActivity.iv_step2_effect.setVisibility(View.VISIBLE);
                FinalPreviewActivity.iv_step1_effect.setVisibility(View.INVISIBLE);
                FinalPreviewActivity.iv_step_more.setVisibility(View.INVISIBLE);

                int temp = Share.plus_button_click_position - 1;

                ImageData imageData = new ImageData();
                imageData.setDrawable(getResources().getDrawable(ids[position]));
                Share.temp_2DEffect_selectedImages.set(Share.plus_button_click_position, imageData);
                Share.temp_2DEffect_selectedImages.get(temp).setDirection(String.valueOf(names[position]));
                FinalPreviewActivity.twoDEffectAdapter.notifyItemChanged(Share.plus_button_click_position);

                if (temp == 0) {
                    application.getSelectedImages().get(temp).setDirection(String.valueOf(names[position]));
                    application.getSelectedImages().get(temp).setDirection_position(position);
                } else {
                    temp = temp / 2;
                    application.getSelectedImages().get(temp).setDirection(String.valueOf(names[position]));
                    application.getSelectedImages().get(temp).setDirection_position(position);
                }

                finish();
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });
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
    public void onClick(View v) {

        if (v == ivBack) {
            onBackPressed();
        } else if (v == iv_more_app) {
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
    }

    private void loadInterstialAd() {
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;

        if (iv_more_app != null) {
            rvTwoDEffect.getRecycledViewPool().clear();
            twoDEffectListAdapter = null;
            ids = null;
            ivBack.setOnClickListener(null);
            iv_more_app.setOnClickListener(null);
        }

        Runtime.getRuntime().gc();
    }
}
