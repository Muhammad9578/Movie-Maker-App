package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.BackgroundAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.FrameAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.MoreEffectAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.ThreeDEffectAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.OnItemClickListner;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.TwoDEffectAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.MusicData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.service.MorePreviewCreatorService;
import com.creativetechnologies.slideshows.videos.songs.videomaker.service.SaveVideoService;
import com.creativetechnologies.slideshows.videos.songs.videomaker.service.ThreeDPreviewCreatorService;
import com.creativetechnologies.slideshows.videos.songs.videomaker.service.TwoDPreviewCreatorService;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.DisplayMetricsHandler;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.MORETHEME;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.THREEDTHEMES;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.THEMES2D;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.Utils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.view.ScaleCardLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils.deleteThemeDir;

public class FinalPreviewActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, OnProgressReceiver {

    private ImageView ivBack, ivFrame, ivGallery, ivEffect, iv_music, iv2DEffect, ivOtherEffect, iv3DEffect, ivBackground, ivFrameButton, ivOverly, ivSpeed, ivUp, ivDown;
    public static ImageView iv_save;
    public static ImageView iv_step1, iv_step2, iv_step3, iv_step5;
    public static ImageView iv_step1_effect, iv_step2_effect, iv_step_more, iv_step3_effect, iv_step4_effect, iv_step4;
    private LinearLayout ll_menu_view, ll_effect_view, ll_effect_menu, ll_row_view, ll2DEffect_View, llMoreEffect_View, ll3DEffect_View, llFrame_View, llBackground, llSpeed;
    private RelativeLayout rl_background, iv_cancel;
    public TextView tv_progress, tv_display_progress;
    public static TextView tvProgress, tvSaveProgress;
    private ScaleCardLayout scaleCard;
    private SeekBar sbSpeed;
    private Animation bottomDown;
    private RecyclerView rv2DEffect, rv3DEffect, rvMoreEffect, rvFrame, rvBackground;
    public static TwoDEffectAdapter twoDEffectAdapter;
    public static MoreEffectAdapter moreEffectAdapter;
    private ThreeDEffectAdapter themeAdapter;
    private FrameAdapter frameAdapter;
    private BackgroundAdapter backgroundAdapter;
    private MyApplication application;
    private ArrayList<THREEDTHEMES> list;
    private int frame;
    private int bg;
    private InterstitialAd interstitialAd;

    // video play code //
    public LockRunnable lockRunnable = new LockRunnable();
    private MediaPlayer mPlayer;
    private SeekBar seekBar;
    private ProgressBar progressBar;
    private boolean isFromTouch = false;
    private int f21i = 0;
    private View ivPlayPause;
    private float seconds = 2.0f;
    private RequestManager glide;
    private ArrayList<ImageData> arrayList;
    private TextView tvEndTime;
    private ImageView ivPreview;
    private View video_clicker;
    private RecyclerView rvDuration;
    public static View flLoader;
    private Handler handler = new Handler();
    private LinearLayout llEdit;
    private TextView tvTime;
    private LayoutInflater inflater;
    private int selected_3DTheme_position = 0;
    private boolean isPause = false;
    private int prog = 1;
    public static boolean playThreeDEffect = false;
    public static boolean playTwoDEffect = false;
    public static boolean playMoreEffect = false;

    private final float[] from = new float[3];
    private final float[] hsv = new float[3];
    private boolean isComplate = true;
    private float lastProg = 0.0f;
    private final float[] to = new float[3];
    private float prog_value = 0.0f;

    public static FinalPreviewActivity activity;
    private AdView adView;
   // private FirebaseAnalytics mFirebaseAnalytics;

    private Float[] duration = new Float[]{Float.valueOf(1.0f), Float.valueOf(1.5f), Float.valueOf(2.0f), Float.valueOf(2.5f), Float.valueOf(3.0f), Float.valueOf(3.5f), Float.valueOf(4.0f)};

    private Integer[] twoDeffect_array = new Integer[]{R.drawable.left, R.drawable.right, R.drawable.up, R.drawable.bottom,
            R.drawable.topleft, R.drawable.topright, R.drawable.bottomleft, R.drawable.bottomright};

    private Integer[] moreEffect_array = new Integer[]{R.drawable.daimond_out, R.drawable.daimond_in, R.drawable.circle_out, R.drawable.circle_in,
            R.drawable.square_out, R.drawable.square_in, R.drawable.skew_right_open, R.drawable.skew_left_close,
            R.drawable.circle_left_up, R.drawable.circle_right_bottom, R.drawable.pin_wheel, R.drawable.four_train, R.drawable.vertical_ran, R.drawable.rect_rand};

    private int[] bg_array = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7, R.drawable.bg8,
            R.drawable.bg9, R.drawable.bg10, R.drawable.bg11, R.drawable.bg12,R.drawable.bg13,R.drawable.bg14,R.drawable.bg15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(FinalPreviewActivity.this)) {
            setContentView(R.layout.activity_preview);

      //      mFirebaseAnalytics = FirebaseAnalytics.getInstance(FinalPreviewActivity.this);
            application = MyApplication.getInstance();
            activity = this;
            GlobalData.loadAdsBanner(FinalPreviewActivity.this, adView);
            init();
            addListner();
            setUpThemeAdapter();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void init() {

        sbSpeed = findViewById(R.id.sbSpeed);

        tv_progress = findViewById(R.id.tv_progress);
        tv_display_progress = findViewById(R.id.tv_display_progress);

        progressBar = findViewById(R.id.progressBar);

        scaleCard = findViewById(R.id.scaleCard);
        seekBar = findViewById(R.id.sbPlayTime);
        ivPlayPause = findViewById(R.id.ivPlayPause);
        tvEndTime = findViewById(R.id.tvEndTime);
        ivPreview = findViewById(R.id.previewImageView1);
        video_clicker = findViewById(R.id.video_clicker);
        rvDuration = findViewById(R.id.rvDuration);
        flLoader = findViewById(R.id.flLoader);
        llEdit = findViewById(R.id.llEdit);
        tvTime = findViewById(R.id.tvTime);
        tvProgress = findViewById(R.id.tvProgress);
        tvSaveProgress = findViewById(R.id.tvSaveProgress);

        ll_menu_view = findViewById(R.id.ll_menu_view);
        ll_effect_view = findViewById(R.id.ll_effect_view);
        ll_effect_menu = findViewById(R.id.ll_effect_menu);
        ll_row_view = findViewById(R.id.ll_row_view);
        ll2DEffect_View = findViewById(R.id.ll2DEffect_View);
        llMoreEffect_View = findViewById(R.id.llMoreEffect_View);
        ll3DEffect_View = findViewById(R.id.ll3DEffect_View);
        llFrame_View = findViewById(R.id.llFrame_View);
        llBackground = findViewById(R.id.llBackground);
        llSpeed = findViewById(R.id.llSpeed);

        rl_background = findViewById(R.id.rl_background);
        iv_cancel = findViewById(R.id.iv_cancel);

        ivBack = findViewById(R.id.ivBack);
        ivFrame = findViewById(R.id.ivFrame);
        ivGallery = findViewById(R.id.ivGallery);
        ivEffect = findViewById(R.id.ivEffect);
        iv_music = findViewById(R.id.iv_music);
        ivOverly = findViewById(R.id.ivOverly);
        ivSpeed = findViewById(R.id.ivSpeed);
        iv3DEffect = findViewById(R.id.iv3DEffect);
        ivBackground = findViewById(R.id.ivBackground);
        ivFrameButton = findViewById(R.id.ivFrameButton);
        iv2DEffect = findViewById(R.id.iv2DEffect);
        ivOtherEffect = findViewById(R.id.ivOtherEffect);
        iv_save = findViewById(R.id.iv_save);
        ivUp = findViewById(R.id.ivUp);
        ivDown = findViewById(R.id.ivDown);

        iv_step1 = findViewById(R.id.iv_step1);
        iv_step2 = findViewById(R.id.iv_step2);
        iv_step3 = findViewById(R.id.iv_step3);
        iv_step4 = findViewById(R.id.iv_step4);
        iv_step5 = findViewById(R.id.iv_step5);

        iv_step1_effect = findViewById(R.id.iv_step1_effect);
        iv_step2_effect = findViewById(R.id.iv_step2_effect);
        iv_step_more = findViewById(R.id.iv_step_more);
        iv_step3_effect = findViewById(R.id.iv_step3_effect);
        iv_step4_effect = findViewById(R.id.iv_step4_effect);

        rv2DEffect = findViewById(R.id.rv2DEffect);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FinalPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv2DEffect.setLayoutManager(linearLayoutManager);

        rvMoreEffect = findViewById(R.id.rvMoreEffect);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FinalPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvMoreEffect.setLayoutManager(linearLayoutManager1);

        setBg(R.drawable.bg1); // set default selection
        rvBackground = findViewById(R.id.rvBackground);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(FinalPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvBackground.setLayoutManager(linearLayoutManager2);
        backgroundAdapter = new BackgroundAdapter(this);
        rvBackground.setAdapter(backgroundAdapter);

        ivGallery.setOnClickListener(this);
        ivEffect.setOnClickListener(this);
        iv_music.setOnClickListener(this);
        ivOverly.setOnClickListener(this);
        ivSpeed.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        iv3DEffect.setOnClickListener(this);
        ivBackground.setOnClickListener(this);
        ivFrameButton.setOnClickListener(this);
        iv2DEffect.setOnClickListener(this);
        ivOtherEffect.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);

        video_clicker.setOnClickListener(this);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);

        // init theme list
        list = new ArrayList(Arrays.asList(THREEDTHEMES.values()));

        iv_step1.setVisibility(View.VISIBLE);

        if (Share.twoD_Effect_Flag) {
            iv_step1_effect.setVisibility(View.INVISIBLE);
            iv_step2_effect.setVisibility(View.VISIBLE);
        } else {
            iv_step1_effect.setVisibility(View.VISIBLE);
            iv_step2_effect.setVisibility(View.INVISIBLE);
        }

        tv_progress.setText("" + 5.0f);
        tv_display_progress.setText("" + 2.0f);
        sbSpeed.setProgress(1);
        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iv_step5.setVisibility(View.VISIBLE);
                prog = progress + 1;
                tv_display_progress.setText("" + prog + ".0");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.gc();
                seconds = prog;
                application.setSecond(seconds);
                lockRunnable.stop();
            }
        });

        llEdit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llEdit.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.e("TAG", "height :" + llEdit.getHeight());
                Log.e("TAG", "width :" + llEdit.getWidth());

                Share.height_of_row = llEdit.getHeight();
                ll_row_view.getLayoutParams().height = Share.height_of_row;
            }
        });

        flLoader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("onTouch", "onTouch");
                return true;
            }
        });

        backgroundAdapter.setEventListener(new BackgroundAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                iv_step3_effect.setVisibility(View.VISIBLE);

                lockRunnable.stop();

                if (Share.twoD_Effect_Flag) {
                    Share.TwoDEffectPreviewActivity = false;
                }

                if (Share.more_Effect_Flag) {
                    Share.MoreEffectPreviewActivity = false;
                }

                // for save video //
                playThreeDEffect = false;
                playTwoDEffect = false;
                playMoreEffect = false;

                MyApplication.TwoDServiceisBreak = MyApplication.towD_Service_On_Off_Flag;

                MyApplication.ThreeDServiceisBreak = MyApplication.threeD_Service_On_Off_Flag;

                MyApplication.MoreServiceisBreak = MyApplication.More_Service_On_Off_Flag;

                if (Share.threeD_Effect_Flag) {

                    System.gc();
                    MyApplication.changeBackground_Flag = true;

                    Bitmap b = ((BitmapDrawable) getResources().getDrawable(bg_array[position])).getBitmap();
                    Utils.backgrnd = Bitmap.createScaledBitmap(b, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, false);

                } else if (Share.twoD_Effect_Flag) {

                    System.gc();

                    MyApplication.changeBackground_Flag = true;

                    Bitmap b = ((BitmapDrawable) getResources().getDrawable(bg_array[position])).getBitmap();
                    Utils.backgrnd = Bitmap.createScaledBitmap(b, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, false);

                } else if (Share.more_Effect_Flag) {
                    System.gc();

                    MyApplication.changeBackground_Flag = true;

                    Bitmap b = ((BitmapDrawable) getResources().getDrawable(bg_array[position])).getBitmap();
                    Utils.backgrnd = Bitmap.createScaledBitmap(b, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, false);
                }
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == ivBack) {
            onBackPressed();
        } else if (view == ivGallery) {
            Share.isPreviewActivity = true;
            MyApplication.getInstance().getFolderList();
            Intent intent = new Intent(FinalPreviewActivity.this, PhotoPickupImageActivity.class);
            startActivity(intent);
        } else if (view == ivEffect) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            ll_effect_view.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_effect_view.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_menu_view.startAnimation(bottomDown);
            ll_menu_view.setVisibility(View.GONE);

            iv_cancel.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            iv_cancel.startAnimation(bottomDown);

        } else if (view == iv_music) {

            Intent intent = new Intent(FinalPreviewActivity.this, PickupAudioFile.class);
            startActivity(intent);

        } else if (view == ivOverly) {
            Share.selected_image_pos = 0;
            Intent intent = new Intent(FinalPreviewActivity.this, EffectActivity.class);
            startActivity(intent);

        } else if (view == ivSpeed) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            llSpeed.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            llSpeed.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_menu_view.startAnimation(bottomDown);
            ll_menu_view.setVisibility(View.GONE);

            iv_cancel.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            iv_cancel.startAnimation(bottomDown);

        } else if (view == iv2DEffect) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            ll3DEffect_View.setVisibility(View.GONE);
            llFrame_View.setVisibility(View.GONE);
            llBackground.setVisibility(View.GONE);
            llMoreEffect_View.setVisibility(View.GONE);
            ll2DEffect_View.setVisibility(View.VISIBLE);

            ll_row_view.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_row_view.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_effect_menu.startAnimation(bottomDown);
            ll_effect_menu.setVisibility(View.GONE);

            loadTwoDEffect();

        } else if (view == ivOtherEffect) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            ll3DEffect_View.setVisibility(View.GONE);
            llFrame_View.setVisibility(View.GONE);
            llBackground.setVisibility(View.GONE);
            ll2DEffect_View.setVisibility(View.GONE);
            llMoreEffect_View.setVisibility(View.VISIBLE);

            ll_row_view.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_row_view.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_effect_menu.startAnimation(bottomDown);
            ll_effect_menu.setVisibility(View.GONE);

            loadMoreDEffect();

        } else if (view == iv3DEffect) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            ll2DEffect_View.setVisibility(View.GONE);
            llFrame_View.setVisibility(View.GONE);
            llBackground.setVisibility(View.GONE);
            llMoreEffect_View.setVisibility(View.GONE);
            ll3DEffect_View.setVisibility(View.VISIBLE);

            ll_row_view.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_row_view.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_effect_menu.startAnimation(bottomDown);
            ll_effect_menu.setVisibility(View.GONE);

        } else if (view == ivBackground) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            ll2DEffect_View.setVisibility(View.GONE);
            ll3DEffect_View.setVisibility(View.GONE);
            llMoreEffect_View.setVisibility(View.GONE);
            llFrame_View.setVisibility(View.GONE);
            llBackground.setVisibility(View.VISIBLE);

            ll_row_view.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_row_view.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_effect_menu.startAnimation(bottomDown);
            ll_effect_menu.setVisibility(View.GONE);

        } else if (view == ivFrameButton) {

            ivUp.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);

            ll2DEffect_View.setVisibility(View.GONE);
            ll3DEffect_View.setVisibility(View.GONE);
            llBackground.setVisibility(View.GONE);
            llMoreEffect_View.setVisibility(View.GONE);
            llFrame_View.setVisibility(View.VISIBLE);

            ll_row_view.setVisibility(View.VISIBLE);
            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
            ll_row_view.startAnimation(bottomDown);

            bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
            ll_effect_menu.startAnimation(bottomDown);
            ll_effect_menu.setVisibility(View.GONE);

        } else if (view == iv_cancel) {

            iv_step2.setVisibility(View.VISIBLE);

            if (ivUp.getVisibility() == View.VISIBLE) {

                ivUp.setVisibility(View.GONE);
                ivDown.setVisibility(View.VISIBLE);

                if (llSpeed.getVisibility() == View.VISIBLE) {
                    ll_menu_view.setVisibility(View.VISIBLE);
                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ll_menu_view.startAnimation(bottomDown);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    llSpeed.startAnimation(bottomDown);
                    llSpeed.setVisibility(View.GONE);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    iv_cancel.startAnimation(bottomDown);
                    iv_cancel.setVisibility(View.INVISIBLE);

                } else if (ll_row_view.getVisibility() == View.VISIBLE) {
                    ll_effect_menu.setVisibility(View.VISIBLE);
                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ll_effect_menu.startAnimation(bottomDown);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    ll_row_view.startAnimation(bottomDown);
                    ll_row_view.setVisibility(View.GONE);

                } else {
                    ll_menu_view.setVisibility(View.VISIBLE);
                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ll_menu_view.startAnimation(bottomDown);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    ll_effect_view.startAnimation(bottomDown);
                    ll_effect_view.setVisibility(View.GONE);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    iv_cancel.startAnimation(bottomDown);
                    iv_cancel.setVisibility(View.INVISIBLE);
                }

            } else {

                ivDown.setVisibility(View.VISIBLE);
                ivUp.setVisibility(View.GONE);

                if (llSpeed.getVisibility() == View.VISIBLE) {
                    ll_menu_view.setVisibility(View.VISIBLE);
                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ll_menu_view.startAnimation(bottomDown);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    llSpeed.startAnimation(bottomDown);
                    llSpeed.setVisibility(View.GONE);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    iv_cancel.startAnimation(bottomDown);
                    iv_cancel.setVisibility(View.INVISIBLE);

                } else if (ll_row_view.getVisibility() == View.VISIBLE) {
                    ll_effect_menu.setVisibility(View.VISIBLE);
                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ll_effect_menu.startAnimation(bottomDown);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    ll_row_view.startAnimation(bottomDown);
                    ll_row_view.setVisibility(View.GONE);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Share.threeD_Effect_Flag) {
                                // remove towDEffect and MoreEffect//
                                if (!Share.threeDEffectApply) {
                                    Share.threeDEffectApply = true;
                                    removeTwoDEffect();
                                    removeMoreEffect();
                                }
                            } else if (Share.twoD_Effect_Flag) {
                                // remove more effect only //
                                removeMoreEffect();
                            } else if (Share.more_Effect_Flag) {
                                // remove only 2d effect //
                                removeTwoDEffect();
                            }
                        }
                    }, 2000);

                } else {
                    Log.e("TAG", "else");
                    ll_menu_view.setVisibility(View.VISIBLE);
                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);
                    ll_menu_view.startAnimation(bottomDown);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    ll_effect_view.startAnimation(bottomDown);
                    ll_effect_view.setVisibility(View.GONE);

                    bottomDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_down);
                    iv_cancel.startAnimation(bottomDown);
                    iv_cancel.setVisibility(View.INVISIBLE);
                }
            }

        } else if (view == iv_save) {

            if(interstitialAd.isLoaded()){
                interstitialAd.show();
            }
            if (flLoader.getVisibility() == View.GONE) {

                lockRunnable.stop();
                handler.removeCallbacks(lockRunnable);

                if (playThreeDEffect) {
                    saveVideo();
                } else if (playTwoDEffect) {
                    saveVideo();
                } else if (playMoreEffect) {
                    saveVideo();
                } else {
                    alertSaveVideo();
                }

            } else {
                Toast.makeText(FinalPreviewActivity.this, "Please Wait!!", Toast.LENGTH_SHORT).show();
            }
        } else if (view == video_clicker) {

            if (MyApplication.changeBackground_Flag) {
                MyApplication.changeBackground_Flag = false;

                setProgressBarVisibility();

                // double war video create no thai teni mate
                Share.TwoDEffectPreviewActivity = false;
                Share.MoreEffectPreviewActivity = false;

                if (Share.threeD_Effect_Flag) {

                    // save video //
                    playThreeDEffect = true;
                    playTwoDEffect = false;
                    playMoreEffect = false;

                    deleteThemeDir(application.selectedTheme.toString());
                    application.videoImages.clear();
                    application.selectedTheme = list.get(selected_3DTheme_position);
                    application.setCurrentTheme(application.selectedTheme.toString());
                    loadVideoData();
                    reset();
                    Intent intent = new Intent(application, ThreeDPreviewCreatorService.class);
                    intent.putExtra(ThreeDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    startService(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flLoader.setVisibility(View.GONE);
                        }
                    }, 5000);
                } else if (Share.twoD_Effect_Flag) {

                    // save video //
                    playThreeDEffect = false;
                    playTwoDEffect = true;
                    playMoreEffect = false;

                    deleteThemeDir(application.selectedTheme2d.toString());
                    application.videoImages.clear();
                    application.selectedTheme2d = THEMES2D.From_Left;
                    application.setCurrentTheme(application.selectedTheme2d.toString());
                    loadVideoData();
                    reset();
                    Intent intent = new Intent(application, TwoDPreviewCreatorService.class);
                    intent.putExtra(TwoDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    startService(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flLoader.setVisibility(View.GONE);
                        }
                    }, 5000);
                } else if (Share.more_Effect_Flag) {

                    // save video //
                    playThreeDEffect = false;
                    playTwoDEffect = false;
                    playMoreEffect = true;

                    deleteThemeDir(application.selectedThemeOther.toString());
                    application.videoImages.clear();
                    application.selectedThemeOther = MORETHEME.MORE;
                    application.setCurrentTheme(application.selectedThemeOther.toString());
                    loadVideoData();
                    reset();
                    Intent intent = new Intent(application, MorePreviewCreatorService.class);
                    intent.putExtra(MorePreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    startService(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flLoader.setVisibility(View.GONE);
                        }
                    }, 5000);
                }
            } else {
                if (Share.twoD_Effect_Flag) {
                    if (Share.TwoDEffectPreviewActivity) {
                        Share.TwoDEffectPreviewActivity = false;

                        System.gc();

                        MyApplication.TwoDServiceisBreak = MyApplication.towD_Service_On_Off_Flag;

                        MyApplication.ThreeDServiceisBreak = MyApplication.threeD_Service_On_Off_Flag;

                        MyApplication.MoreServiceisBreak = MyApplication.More_Service_On_Off_Flag;

                        // for save video //
                        playThreeDEffect = false;
                        playTwoDEffect = true;
                        playMoreEffect = false;

                        // loader //
                        setProgressBarVisibility();

                        deleteThemeDir(application.selectedTheme2d.toString());
                        application.videoImages.clear();
                        application.selectedTheme2d = THEMES2D.From_Left;
                        application.setCurrentTheme(application.selectedTheme2d.toString());
                        loadVideoData();
                        reset();
                        Intent intent = new Intent(application, TwoDPreviewCreatorService.class);
                        intent.putExtra(TwoDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                        startService(intent);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                flLoader.setVisibility(View.GONE);
                            }
                        }, 5000);

                    } else {
                        if (lockRunnable.isPause()) {
                            lockRunnable.play();
                        } else {
                            lockRunnable.pause();
                        }
                    }
                } else if (Share.more_Effect_Flag) {

                    if (Share.MoreEffectPreviewActivity) {
                        Share.MoreEffectPreviewActivity = false;

                        System.gc();

                        MyApplication.TwoDServiceisBreak = MyApplication.towD_Service_On_Off_Flag;

                        MyApplication.ThreeDServiceisBreak = MyApplication.threeD_Service_On_Off_Flag;


                        MyApplication.MoreServiceisBreak = MyApplication.More_Service_On_Off_Flag;

                        // for save video //
                        playThreeDEffect = false;
                        playTwoDEffect = false;
                        playMoreEffect = true;

                        // loader //
                        setProgressBarVisibility();

                        deleteThemeDir(application.selectedThemeOther.toString());
                        application.videoImages.clear();
                        application.selectedThemeOther = MORETHEME.MORE;
                        application.setCurrentTheme(application.selectedThemeOther.toString());
                        loadVideoData();
                        reset();
                        Intent intent = new Intent(application, MorePreviewCreatorService.class);
                        intent.putExtra(MorePreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                        startService(intent);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                flLoader.setVisibility(View.GONE);
                            }
                        }, 5000);

                    } else {
                        if (lockRunnable.isPause()) {
                            lockRunnable.play();
                        } else {
                            lockRunnable.pause();
                        }
                    }
                } else {

                    if (lockRunnable.isPause()) {
                        lockRunnable.play();
                    } else {
                        lockRunnable.pause();
                    }
                }
            }
        }
    }

    private void alertSaveVideo() {

        final Dialog dialog = new Dialog(FinalPreviewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_save_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvOk = dialog.findViewById(R.id.tvOk);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    private void saveVideo() {
        flLoader.setVisibility(View.VISIBLE);
        tvProgress.setVisibility(View.GONE);
        tvSaveProgress.setVisibility(View.VISIBLE);

        Log.e("TAG", "saveVideo" + MyApplication.isMyServiceRunning(FinalPreviewActivity.this, SaveVideoService.class));
        Share.save_video_Flag = true;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "run()");
                if (!MyApplication.isMyServiceRunning(FinalPreviewActivity.this, SaveVideoService.class)) {
                    Intent intent = new Intent(FinalPreviewActivity.this, SaveVideoService.class);
                    startService(intent);
                } else {
                    Intent intent = new Intent(FinalPreviewActivity.this, SaveVideoService.class);
                    stopService(intent);

                    if (!MyApplication.isMyServiceRunning(FinalPreviewActivity.this, SaveVideoService.class)) {
                        Intent intent1 = new Intent(FinalPreviewActivity.this, SaveVideoService.class);
                        startService(intent1);
                    }
                }
            }
        });
    }

    private void addListner() {
        seekBar.setOnSeekBarChangeListener(this);
    }

    public void loadVideoData() {

        seconds = application.getSecond();
        Log.e("TAG", "second :" + seconds);
        inflater = LayoutInflater.from(this);
        Glide.get(this).clearMemory();
        glide = Glide.with(this);
        application = MyApplication.getInstance();
        arrayList = application.getSelectedImages();
        int total = 0;

        if (Share.twoD_Effect_Flag) {
            seekBar.setMax((arrayList.size()) * 30);
            total = (int) (((float) (arrayList.size())) * seconds);
        } else {
            seekBar.setMax((arrayList.size() - 1) * 30);
            total = (int) (((float) (arrayList.size() - 1)) * seconds);
        }
        tvTime.setText("00:00");
        tvEndTime.setText(String.format("%02d:%02d", Integer.valueOf(total / 60), Integer.valueOf(total % 60)));

        try {
            if (application.getSelectedImages().size() != 0) {
                glide.load((application.getSelectedImages().get(0)).getImagePath()).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(300, 300).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivPreview.setImageBitmap(null);
                        Log.e("TAG", "Load Successful");
                        ivPreview.setImageBitmap(resource);
                    }
                });
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private synchronized void displayImage() {
        try {
            if (f21i >= seekBar.getMax()) {
                f21i = 0;
                lockRunnable.stop();
            } else {
                if (f21i > 0 && flLoader.getVisibility() == View.VISIBLE) {
                    if (!(mPlayer == null || mPlayer.isPlaying())) {
                        mPlayer.start();
                    }
                }
                seekBar.setSecondaryProgress(application.videoImages.size());
                if (seekBar.getProgress() < seekBar.getSecondaryProgress()) {
                    f21i %= application.videoImages.size();
                    glide.load(application.videoImages.get(f21i)).asBitmap().signature(new MediaStoreSignature("image/*", System.currentTimeMillis(), 0)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new C12412());
                    f21i++;
                    if (!isFromTouch) {
                        seekBar.setProgress(f21i);
                    }
                    int j = (int) ((((float) f21i) / BitmapDescriptorFactory.HUE_ORANGE) * seconds);
                    int mm = j / 60;
                    int ss = j % 60;
                    int total;
                    if (Share.twoD_Effect_Flag) {
                        total = (int) (((float) (arrayList.size())) * seconds);
                    } else {
                        total = (int) (((float) (arrayList.size() - 1)) * seconds);
                    }
                    String starTime = String.format("%02d:%02d", Integer.valueOf(mm), Integer.valueOf(ss));
                    String endTime = String.format("%02d:%02d", Integer.valueOf(total / 60), Integer.valueOf(total % 60));
                    tvTime.setText(starTime);
                    tvEndTime.setText(endTime);
                }
            }
        } catch (Exception e) {
            glide = Glide.with(this);
        }
    }

    public void reset() {
//        MyApplication.isBreak = false;
        application.videoImages.clear();
        handler.removeCallbacks(lockRunnable);
        lockRunnable.stop();
        Glide.get(this).clearMemory();
        new C05864().start();
        FileUtils.deleteTempDir();
        glide = Glide.with(this);
        setTheme();
        System.gc();
    }

    private void setUpThemeAdapter() {
        rv3DEffect = findViewById(R.id.rv3DEffect);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FinalPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv3DEffect.setLayoutManager(linearLayoutManager1);
        themeAdapter = new ThreeDEffectAdapter(this);
        rv3DEffect.setAdapter(themeAdapter);

        themeAdapter.setEventListener(new ThreeDEffectAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                Share.threeD_Effect_Flag = true;
                Share.twoD_Effect_Flag = false;
                Share.more_Effect_Flag = false;

                // apply bg (without play video) apply 3d effect
                Share.TwoDEffectPreviewActivity = false;
                Share.MoreEffectPreviewActivity = false;
                MyApplication.changeBackground_Flag = false;

                // for save video //
                playThreeDEffect = true;
                playTwoDEffect = false;
                playMoreEffect = false;

                MyApplication.TwoDServiceisBreak = MyApplication.towD_Service_On_Off_Flag;

                MyApplication.ThreeDServiceisBreak = MyApplication.threeD_Service_On_Off_Flag;

                MyApplication.MoreServiceisBreak = MyApplication.More_Service_On_Off_Flag;

                FinalPreviewActivity.iv_step_more.setVisibility(View.INVISIBLE);
                FinalPreviewActivity.iv_step2_effect.setVisibility(View.INVISIBLE);
                FinalPreviewActivity.iv_step1_effect.setVisibility(View.VISIBLE);

                setProgressBarVisibility();

                selected_3DTheme_position = position;

                deleteThemeDir(application.selectedTheme.toString());
                application.videoImages.clear();
                application.selectedTheme = list.get(selected_3DTheme_position);
                application.setCurrentTheme(application.selectedTheme.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(application, ThreeDPreviewCreatorService.class);
                intent.putExtra(ThreeDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);
                themeAdapter.notifyDataSetChanged();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });

        rvFrame = findViewById(R.id.rvFrame);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(FinalPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvFrame.setLayoutManager(linearLayoutManager2);
        frameAdapter = new FrameAdapter(this);
        rvFrame.setAdapter(frameAdapter);

        rvDuration.setHasFixedSize(true);
        rvDuration.setLayoutManager(new LinearLayoutManager(this));
        rvDuration.setAdapter(new DurationAdapter());
    }

    public void setTheme() {
        if (application.isFromSdCardAudio) {
            lockRunnable.pause();
        } else {
            new C05906().start();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        f21i = progress;
        if (isFromTouch) {
            seekBar.setProgress(Math.min(progress, seekBar.getSecondaryProgress()));
            displayImage();
            seekMediaPlayer();
        }
    }

    private void seekMediaPlayer() {
        if (mPlayer != null) {
            try {
                mPlayer.seekTo(((int) (((((float) f21i) / BitmapDescriptorFactory.HUE_ORANGE) * seconds) * 1000.0f)) % mPlayer.getDuration());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isFromTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isFromTouch = false;
    }

    @Override
    public void onImageProgressFrameUpdate(final float f) {
        runOnUiThread(new Runnable() {
            public void run() {
                changePercentageOnText((f * 25.0f) / 100.0f);
            }
        });
    }

    @Override
    public void onProgressFinish(String str) {

        FinalPreviewActivity.flLoader.setVisibility(View.GONE);

        Share.from_my_video = false;
        SharedPrefs.save(application, SharedPrefs.video_created_or_not, "false");

        ThreeDPreviewCreatorService.isImageComplate = false;
        TwoDPreviewCreatorService.isImageComplate = false;
        MorePreviewCreatorService.isImageComplate = false;

        Share.save_video_Flag = false;

        Intent intent = new Intent(FinalPreviewActivity.this, ThreeDPreviewCreatorService.class);
        stopService(intent);

        Intent intent1 = new Intent(FinalPreviewActivity.this, TwoDPreviewCreatorService.class);
        stopService(intent1);

        Intent intent2 = new Intent(FinalPreviewActivity.this, MorePreviewCreatorService.class);
        stopService(intent2);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            if (!MyApplication.getInstance().requestNewInterstitial()) {

                if (!MyApplication.error_in_save_video) {
                    Intent intent_data = new Intent(getApplicationContext(), SaveVideoActivity.class);
                    intent_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent_data);

                    if (FinalPreviewActivity.activity != null) {
                        FinalPreviewActivity.activity.finish();
                    }

                } else {
                    MyApplication.error_in_save_video = false;
                    finish();
                }

            } else {

                        if (!MyApplication.error_in_save_video) {
                            Intent intent_data = new Intent(getApplicationContext(), SaveVideoActivity.class);
                            intent_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent_data);

                            if (FinalPreviewActivity.activity != null) {
                                FinalPreviewActivity.activity.finish();
                            }

                        } else {
                            MyApplication.error_in_save_video = false;
                            finish();
                        }
            }
        } else {
            if (!MyApplication.error_in_save_video) {
                Intent intent_data = new Intent(getApplicationContext(), SaveVideoActivity.class);
                intent_data.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_data);

                if (FinalPreviewActivity.activity != null) {
                    FinalPreviewActivity.activity.finish();
                }

            } else {
                MyApplication.error_in_save_video = false;
                finish();
            }
        }
    }

    @Override
    public void onVideoProgressFrameUpdate(final float f) {
        runOnUiThread(new Runnable() {
            public void run() {
                changePercentageOnText(((f * 75.0f) / 100.0f) + 25.0f);
            }
        });
    }

    private synchronized void changePercentageOnText(float f) {
        if (isComplate) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(lastProg, f);
            ofFloat.setDuration(300);
            ofFloat.setInterpolator(new LinearInterpolator());
            ofFloat.addUpdateListener(new C04416());
            ofFloat.addListener(new C04427());
            ofFloat.start();
            this.lastProg = f;
        }
    }

    class C04416 implements ValueAnimator.AnimatorUpdateListener {
        C04416() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            hsv[0] = from[0] + (((to[0] - from[0]) * ((Float) valueAnimator.getAnimatedValue()).floatValue()) / 100.0f);
            hsv[1] = from[1] + (((to[1] - from[1]) * ((Float) valueAnimator.getAnimatedValue()).floatValue()) / 100.0f);
            hsv[2] = from[2] + (((to[2] - from[2]) * ((Float) valueAnimator.getAnimatedValue()).floatValue()) / 100.0f);
            prog_value = ((Float) valueAnimator.getAnimatedValue()).floatValue();
//            tvSaveProgress.setText(String.format("           Please Wait\nVideo Creating..." + "%05.2f%%", new Object[]{Float.valueOf(prog_value)}));
            tvSaveProgress.setText(String.format("           %02d%%" + "\n" + "    Please Wait\n Video Creating", Integer.valueOf((int) prog_value)));
        }
    }

    class C04427 implements Animator.AnimatorListener {
        public void onAnimationRepeat(Animator animator) {
        }

        C04427() {
        }

        public void onAnimationStart(Animator animator) {
            isComplate = false;
        }

        public void onAnimationEnd(Animator animator) {
            isComplate = true;
        }

        public void onAnimationCancel(Animator animator) {
            isComplate = true;
        }
    }

    class C05906 extends Thread {

        class C05892 implements Runnable {
            C05892() {
            }

            public void run() {

                try {
                    if (application.getMusicData().getTrack_duration() == 0) {
                        application.getMusicData().setTrack_duration(player.getDuration());
                    }
                    reinitMusic();
                    lockRunnable.pause();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        C05906() {
        }

        MediaPlayer player;

        public void run() {
            try {
                FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
                File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");

                Log.e("TAg", "tempFile = " + tempFile.getAbsolutePath());
                InputStream in = null;
                if (Share.audio_file.equals("")) {
                    in = getResources().openRawResource(R.raw.song_1);
                    FileOutputStream out = new FileOutputStream(tempFile);
                    byte[] buff = new byte[1024];
                    while (true) {
                        int read = in.read(buff);
                        if (read <= 0) {
                            break;
                        }
                        out.write(buff, 0, read);
                    }
                }

                if (player != null)
                    player = null;
                player = new MediaPlayer();
                player.setDataSource(tempFile.getAbsolutePath());
                player.setAudioStreamType(3);
                player.prepare();
                final MusicData musicData = new MusicData();
                musicData.track_data = tempFile.getAbsolutePath();

                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {
                        musicData.track_duration = mp.getDuration();
                        Log.e("TAG", "musicData.track_duration ===> " + musicData.track_duration);
                        mp.stop();
                    }
                });

                musicData.track_Title = "temp";
                application.setMusicData(musicData);
            } catch (Exception e) {
            }
            runOnUiThread(new C05892());
        }
    }

    private void reinitMusic() {
        Exception e;
        MusicData musicData = application.getMusicData();
        if (musicData != null) {
            mPlayer = MediaPlayer.create(this, Uri.parse(musicData.track_data));
            if (mPlayer != null) {
                mPlayer.setLooping(true);
            }

        } else {
            return;
        }
    }

    class LockRunnable implements Runnable {

        class C05921 implements Animation.AnimationListener {
            C05921() {
            }

            public void onAnimationStart(Animation animation) {
                ivPlayPause.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                ivPlayPause.setVisibility(View.INVISIBLE);
            }
        }

        class C05932 implements Animation.AnimationListener {
            C05932() {
            }

            public void onAnimationStart(Animation animation) {
                ivPlayPause.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }
        }

        public void run() {
            displayImage();
            if (!isPause) {
                handler.postDelayed(lockRunnable, Math.round(50.0f * seconds));
            }
        }

        public boolean isPause() {
            return isPause;
        }

        public void play() {
            isPause = false;
            playMusic();
            handler.postDelayed(lockRunnable, Math.round(50.0f * seconds));
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            animation.setAnimationListener(new C05921());
            ivPlayPause.startAnimation(animation);
        }

        public void pause() {
            if (ivPlayPause != null) {
                isPause = true;
                pauseMusic();
                Animation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(500);
                animation.setFillAfter(true);
                ivPlayPause.startAnimation(animation);
                animation.setAnimationListener(new C05932());
            }
        }

        public void stop() {
            pause();
            f21i = 0;
            if (mPlayer != null) {
                mPlayer.stop();
            }
            reinitMusic();
            seekBar.setProgress(f21i);
            tvTime.setText("00:00");
            if (application.getSelectedImages().size() != 0)
                glide.load((application.getSelectedImages().get(0)).getImagePath()).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(300, 300).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivPreview.setImageBitmap(null);
                        ivPreview.setImageBitmap(resource);
                    }
                });
        }
    }

    private void playMusic() {
        if (flLoader.getVisibility() != View.VISIBLE && mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    private void pauseMusic() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }


    class C05864 extends Thread {
        C05864() {
        }

        public void run() {
            Glide.get(FinalPreviewActivity.this).clearDiskCache();
        }
    }


    class C12412 extends SimpleTarget<Bitmap> {
        C12412() {
        }

        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            ivPreview.setImageBitmap(resource);
        }
    }

    private class DurationAdapter extends RecyclerView.Adapter<DurationAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckedTextView checkedTextView;

            public ViewHolder(View view) {
                super(view);
                checkedTextView = view.findViewById(R.id.chk_textview);
            }
        }

        private DurationAdapter() {
        }

        public int getItemCount() {
            return duration.length;
        }

        public void onBindViewHolder(ViewHolder holder, int pos) {
            boolean z = true;
            final float dur = duration[pos].floatValue();
            holder.checkedTextView.setText(String.format("%.1f Second", Float.valueOf(dur)));
            CheckedTextView checkedTextView = holder.checkedTextView;
            if (dur != seconds) {
                z = false;
            }
            checkedTextView.setChecked(z);
            holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    seconds = dur;
                    application.setSecond(seconds);
                    notifyDataSetChanged();
                    lockRunnable.play();
                }
            });
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            return new ViewHolder(inflater.inflate(R.layout.duration_list_item, parent, false));
        }
    }

    private void removeTwoDEffect() {

        Share.temp_2DEffect_selectedImages.clear();
        int k = 0;
        int n = 0;
        for (int i = 0; i < application.getSelectedImages().size() * 2; i++) {
            System.gc();
            if (i % 2 != 0) {
                ImageData imageData = new ImageData();

                if (!application.getSelectedImages().get(n).getDirection().equals("")) {

                    application.getSelectedImages().get(n).setDirection("");
                    imageData.setDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    Share.temp_2DEffect_selectedImages.add(i, imageData);
                    n++;
                } else {
                    imageData.setDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    Share.temp_2DEffect_selectedImages.add(i, imageData);
                }
            } else {
                ImageData imageData = new ImageData();
                imageData.setImagePath(application.getSelectedImages().get(k).getImagePath());
                k++;
                Share.temp_2DEffect_selectedImages.add(i, imageData);
            }
        }

        twoDEffectAdapter = new TwoDEffectAdapter(FinalPreviewActivity.this, Share.temp_2DEffect_selectedImages);
        rv2DEffect.setAdapter(twoDEffectAdapter);
        twoDEffectAdapter.setOnItemClickListner(new Select2DEffect());
    }

    private class Select2DEffect implements OnItemClickListner<Object> {
        @Override
        public void onItemClick(View view, Object o) {
            if (!isPause)
                lockRunnable.pause();
            Intent intent = new Intent(FinalPreviewActivity.this, TwoDEffectListActvity.class);
            startActivity(intent);
        }
    }

    private void loadTwoDEffect() {

        Share.temp_2DEffect_selectedImages.clear();
        int k = 0;
        int n = 0;
        for (int i = 0; i < application.getSelectedImages().size() * 2; i++) {
            System.gc();
            if (i % 2 != 0) {
                ImageData imageData = new ImageData();

                if (!application.getSelectedImages().get(n).getDirection().equals("")) {

                    Drawable drawable_overlay = getResources().getDrawable(twoDeffect_array[application.getSelectedImages().get(n).getDirection_position()]);
                    Bitmap b = ((BitmapDrawable) drawable_overlay).getBitmap();
                    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 150, 150, false);
                    drawable_overlay = new BitmapDrawable(getResources(), bitmapResized);

                    imageData.setDrawable(drawable_overlay);
                    Share.temp_2DEffect_selectedImages.add(i, imageData);
                    n++;
                } else {
                    imageData.setDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    Share.temp_2DEffect_selectedImages.add(i, imageData);
                }
            } else {
                ImageData imageData = new ImageData();
                imageData.setImagePath(application.getSelectedImages().get(k).getImagePath());
                k++;
                Share.temp_2DEffect_selectedImages.add(i, imageData);
            }
        }

        twoDEffectAdapter = new TwoDEffectAdapter(FinalPreviewActivity.this, Share.temp_2DEffect_selectedImages);
        rv2DEffect.setAdapter(twoDEffectAdapter);
        twoDEffectAdapter.setOnItemClickListner(new Select2DEffect());
    }

    // load more effect //
    private void loadMoreDEffect() {

        Share.temp_MoreEffect_selectedImages.clear();
        int k = 0;
        int n = 0;
        for (int i = 0; i < application.getSelectedImages().size() * 2; i++) {
            System.gc();
            if (i % 2 != 0) {
                ImageData imageData = new ImageData();

                if (!application.getSelectedImages().get(n).getMoredirection().equals("")) {

                    Drawable drawable_overlay = getResources().getDrawable(moreEffect_array[application.getSelectedImages().get(n).getMore_direction_position()]);
                    Bitmap b = ((BitmapDrawable) drawable_overlay).getBitmap();
                    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 150, 150, false);
                    drawable_overlay = new BitmapDrawable(getResources(), bitmapResized);

                    imageData.setDrawable(drawable_overlay);
                    Share.temp_MoreEffect_selectedImages.add(i, imageData);
                    n++;
                } else {
                    imageData.setDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    Share.temp_MoreEffect_selectedImages.add(i, imageData);
                }
            } else {
                ImageData imageData = new ImageData();
                imageData.setImagePath(application.getSelectedImages().get(k).getImagePath());
                k++;
                Share.temp_MoreEffect_selectedImages.add(i, imageData);
            }
        }

        moreEffectAdapter = new MoreEffectAdapter(FinalPreviewActivity.this, Share.temp_MoreEffect_selectedImages);
        rvMoreEffect.setAdapter(moreEffectAdapter);
        moreEffectAdapter.setOnItemClickListner(new SelectMoreEffect());
    }

    private void removeMoreEffect() {

        Share.temp_MoreEffect_selectedImages.clear();
        int k = 0;
        int n = 0;
        for (int i = 0; i < application.getSelectedImages().size() * 2; i++) {
            System.gc();
            if (i % 2 != 0) {
                ImageData imageData = new ImageData();

                if (!application.getSelectedImages().get(n).getMoredirection().equals("")) {

                    application.getSelectedImages().get(n).setMoredirection("");
                    imageData.setDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    Share.temp_MoreEffect_selectedImages.add(i, imageData);
                    n++;
                } else {
                    imageData.setDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    Share.temp_MoreEffect_selectedImages.add(i, imageData);
                }
            } else {
                ImageData imageData = new ImageData();
                imageData.setImagePath(application.getSelectedImages().get(k).getImagePath());
                k++;
                Share.temp_MoreEffect_selectedImages.add(i, imageData);
            }
        }

        moreEffectAdapter = new MoreEffectAdapter(FinalPreviewActivity.this, Share.temp_MoreEffect_selectedImages);
        rvMoreEffect.setAdapter(moreEffectAdapter);
        moreEffectAdapter.setOnItemClickListner(new SelectMoreEffect());
    }

    private class SelectMoreEffect implements OnItemClickListner<Object> {
        @Override
        public void onItemClick(View view, Object o) {
            if (!isPause)
                lockRunnable.pause();
            Intent intent = new Intent(FinalPreviewActivity.this, MoreEffectListActvity.class);
            startActivity(intent);
        }

    }

    public int getFrame() {
        return application.getFrame();
    }

    public void setFrame(int data) {
        frame = data;
        if (data == -1) {
            ivFrame.setImageDrawable(null);
        } else {
            ivFrame.setImageResource(data);
        }
        application.setFrame(data);
    }


    public int getBg() {
        return application.getBg();
    }

    public void setBg(int data) {
        application.setBg(data);
    }

    private void setProgressBarVisibility() {
        flLoader.setVisibility(View.VISIBLE);
        tvProgress.setText("Please Wait...");
        tvSaveProgress.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {

            /*// stop video when user come from 3d effect to 2d effect apply
            if (Share.pause_video_flag) {
                Share.pause_video_flag = false;
                lockRunnable.stop();
                glide.load((application.getSelectedImages().get(0)).getImagePath()).asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).override(300, 300).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivPreview.setImageBitmap(null);
                        ivPreview.setImageBitmap(resource);
                    }
                });
            }*/

            if (application.isFromSdCardAudio) {
                application.isFromSdCardAudio = false;

                // loader //
                setProgressBarVisibility();

                if (Share.threeD_Effect_Flag) {
                    System.gc();

                    // save video //
                    playThreeDEffect = true;
                    playTwoDEffect = false;
                    playMoreEffect = false;

                    deleteThemeDir(application.selectedTheme.toString());
                    application.videoImages.clear();
                    application.selectedTheme = list.get(selected_3DTheme_position);
                    application.setCurrentTheme(application.selectedTheme.toString());
                    loadVideoData();
                    reset();
                    Intent intent = new Intent(getApplicationContext(), ThreeDPreviewCreatorService.class);
                    intent.putExtra(ThreeDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    startService(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flLoader.setVisibility(View.GONE);
                        }
                    }, 5000);

                } else if (Share.twoD_Effect_Flag) {
                    System.gc();

                    // save video //
                    playThreeDEffect = false;
                    playTwoDEffect = true;
                    playMoreEffect = false;

                    deleteThemeDir(application.selectedTheme2d.toString());
                    application.videoImages.clear();
                    application.selectedTheme2d = THEMES2D.From_Left;
                    application.setCurrentTheme(application.selectedTheme2d.toString());
                    reset();
                    Intent intent = new Intent(application, TwoDPreviewCreatorService.class);
                    intent.putExtra(TwoDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    startService(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flLoader.setVisibility(View.GONE);
                        }
                    }, 5000);
                } else if (Share.more_Effect_Flag) {

                    System.gc();

                    // save video //
                    playThreeDEffect = false;
                    playTwoDEffect = false;
                    playMoreEffect = true;

                    deleteThemeDir(application.selectedThemeOther.toString());
                    application.videoImages.clear();
                    application.selectedThemeOther = MORETHEME.MORE;
                    application.setCurrentTheme(application.selectedThemeOther.toString());
                    loadVideoData();
                    reset();
                    Intent intent = new Intent(application, MorePreviewCreatorService.class);
                    intent.putExtra(MorePreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    startService(intent);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flLoader.setVisibility(View.GONE);
                        }
                    }, 5000);
                }
            }
        }

        if (Share.FirstTimePreviewActivity) {

            System.gc();

            application.setOnProgressReceiver(this);

            // loader //
            setProgressBarVisibility();

            Share.FirstTimePreviewActivity = false;
            Share.threeD_Effect_Flag = true;
            Share.twoD_Effect_Flag = false;

            // save video //
            FinalPreviewActivity.playThreeDEffect = true;
            FinalPreviewActivity.playTwoDEffect = false;
            FinalPreviewActivity.playMoreEffect = false;

            application.videoImages.clear();
            Intent intent = new Intent(getApplicationContext(), ThreeDPreviewCreatorService.class);
            intent.putExtra(ThreeDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
            startService(intent);
            loadVideoData();
            setTheme();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flLoader.setVisibility(View.GONE);

                }
            }, 5000);
        }

        if (Share.isPreviewActivity) {

            System.gc();
            Share.isPreviewActivity = false;

            // loader //
            setProgressBarVisibility();

            // when user apply twoDeffect(without play) select audio
            Share.TwoDEffectPreviewActivity = false;
            Share.MoreEffectPreviewActivity = false;
            MyApplication.changeBackground_Flag = false;

            if (Share.threeD_Effect_Flag) {

                System.gc();

                // save video //
                FinalPreviewActivity.playThreeDEffect = true;
                FinalPreviewActivity.playTwoDEffect = false;
                FinalPreviewActivity.playMoreEffect = false;

                deleteThemeDir(application.selectedTheme.toString());
                application.videoImages.clear();
                application.selectedTheme = list.get(selected_3DTheme_position);
                application.setCurrentTheme(application.selectedTheme.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(getApplicationContext(), ThreeDPreviewCreatorService.class);
                intent.putExtra(ThreeDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);

            } else if (Share.twoD_Effect_Flag) {

                System.gc();

                // save video //
                FinalPreviewActivity.playThreeDEffect = false;
                FinalPreviewActivity.playTwoDEffect = true;
                FinalPreviewActivity.playMoreEffect = false;

                deleteThemeDir(application.selectedTheme2d.toString());
                application.videoImages.clear();
                application.selectedTheme2d = THEMES2D.From_Left;
                application.setCurrentTheme(application.selectedTheme2d.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(application, TwoDPreviewCreatorService.class);
                intent.putExtra(TwoDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);
            } else if (Share.more_Effect_Flag) {

                System.gc();

                // save video //
                FinalPreviewActivity.playThreeDEffect = false;
                FinalPreviewActivity.playTwoDEffect = false;
                FinalPreviewActivity.playMoreEffect = true;

                deleteThemeDir(application.selectedThemeOther.toString());
                application.videoImages.clear();
                application.selectedThemeOther = MORETHEME.MORE;
                application.setCurrentTheme(application.selectedThemeOther.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(application, MorePreviewCreatorService.class);
                intent.putExtra(MorePreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        }

        if (Share.Effect_Flag) {

            System.gc();
            Share.Effect_Flag = false;

            // loader //
            setProgressBarVisibility();

            if (Share.threeD_Effect_Flag) {

                // save video //
                FinalPreviewActivity.playThreeDEffect = true;
                FinalPreviewActivity.playTwoDEffect = false;
                FinalPreviewActivity.playMoreEffect = false;

                deleteThemeDir(application.selectedTheme.toString());
                application.videoImages.clear();
                application.selectedTheme = list.get(selected_3DTheme_position);
                application.setCurrentTheme(application.selectedTheme.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(getApplicationContext(), ThreeDPreviewCreatorService.class);
                intent.putExtra(ThreeDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);

            } else if (Share.twoD_Effect_Flag) {

                // save video //
                FinalPreviewActivity.playThreeDEffect = false;
                FinalPreviewActivity.playTwoDEffect = true;
                FinalPreviewActivity.playMoreEffect = false;

                deleteThemeDir(application.selectedTheme2d.toString());
                application.videoImages.clear();
                application.selectedTheme2d = THEMES2D.From_Left;
                application.setCurrentTheme(application.selectedTheme2d.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(application, TwoDPreviewCreatorService.class);
                intent.putExtra(TwoDPreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);
            } else if (Share.more_Effect_Flag) {

                System.gc();

                // save video //
                FinalPreviewActivity.playThreeDEffect = false;
                FinalPreviewActivity.playTwoDEffect = false;
                FinalPreviewActivity.playMoreEffect = true;

                deleteThemeDir(application.selectedThemeOther.toString());
                application.videoImages.clear();
                application.selectedThemeOther = MORETHEME.MORE;
                application.setCurrentTheme(application.selectedThemeOther.toString());
                loadVideoData();
                reset();
                Intent intent = new Intent(application, MorePreviewCreatorService.class);
                intent.putExtra(MorePreviewCreatorService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                startService(intent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flLoader.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lockRunnable != null) {
            if (!isPause)
                lockRunnable.pause();
        }
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        Share.resume_flag = false;
        System.gc();

        if (application != null) {
            finishActivityMethid();
        }
    }

    private void finishActivityMethid() {

        Log.e("TAG", " ******************** finishActivityMethod ********************");

        Share.effect_step_flag = false;
        Share.twoD_Effect_Flag = false;
        Share.threeD_Effect_Flag = false;
        Share.pause_video_flag = false;

        // remove effect flags //
        Share.threeDEffectApply = false;
        Share.twoDEffectApply = false;

        // this flag used for delete video (call different delete method)
        Share.from_my_video = false;

        MyApplication.TwoDServiceisBreak = false;
        MyApplication.ThreeDServiceisBreak = false;
        MyApplication.MoreServiceisBreak = false;
        MyApplication.towD_Service_On_Off_Flag = false;
        MyApplication.threeD_Service_On_Off_Flag = false;
        MyApplication.More_Service_On_Off_Flag = false;
        MyApplication.changeBackground_Flag = false;

        // slide //
        Share.end_slide_selected_or_not = false;

        // init value of frame -1
        application.setFrame(-1);

        // set default time
        application.setSecond(2.0f);

        // set default progress //
        application.setOnProgressReceiver(null);

        // set Default theme
        application.selectedTheme = list.get(0);
        application.setCurrentTheme(application.selectedTheme.toString());
        list.clear();

        try {
            rvBackground.setAdapter(null);
            rvBackground.setLayoutManager(null);
            rvBackground.removeAllViews();
            rvBackground.getRecycledViewPool().clear();

            rvFrame.setAdapter(null);
            rvFrame.setLayoutManager(null);
            rvFrame.removeAllViews();
            rvFrame.getRecycledViewPool().clear();

            rv3DEffect.setAdapter(null);
            rv3DEffect.setLayoutManager(null);
            rv3DEffect.removeAllViews();
            rv3DEffect.getRecycledViewPool().clear();

            rv2DEffect.setAdapter(null);

            rv2DEffect.setLayoutManager(null);
            rv2DEffect.removeAllViews();
            rv2DEffect.getRecycledViewPool().clear();

            rvMoreEffect.setAdapter(null);
            rvMoreEffect.setLayoutManager(null);
            rvMoreEffect.removeAllViews();
            rvMoreEffect.getRecycledViewPool().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(FinalPreviewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_editing);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button rate_app = dialog.findViewById(R.id.rate_app);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        lockRunnable.pause();

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                MyApplication.ThreeDServiceisBreak = true;
                MyApplication.TwoDServiceisBreak = true;
                MyApplication.MoreServiceisBreak = true;

                if (Share.save_video_Flag) {
                    Share.save_video_Flag = false;
                    MyApplication.Save_Service_On_Off_Flag = true;
                }

                // set default progress //
                application.setOnProgressReceiver(null);

                application.videoImages.clear();
                application.getSelectedImages().clear();
                application.getOrgSelectedImages().clear();
                application.getTempOrgSelectedImages().clear();

//                application.clearAllSelection();

                flLoader.setVisibility(View.GONE);

                Share.from_my_video = false;

                ThreeDPreviewCreatorService.isImageComplate = false;
                TwoDPreviewCreatorService.isImageComplate = false;
                MorePreviewCreatorService.isImageComplate = false;

                Intent intent = new Intent(FinalPreviewActivity.this, ThreeDPreviewCreatorService.class);
                stopService(intent);

                Intent intent1 = new Intent(FinalPreviewActivity.this, TwoDPreviewCreatorService.class);
                stopService(intent1);

                Intent intent2 = new Intent(FinalPreviewActivity.this, MorePreviewCreatorService.class);
                stopService(intent2);


                finish();
            }
        });

        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }
}
