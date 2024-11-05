package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.StickerAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.StickerModel;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StickerActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvStickerView;
    private StickerAdapter stickerAdapter;
    private String[] imgPath;
    private AssetManager assetManager;
    private ArrayList<StickerModel> stickerList = new ArrayList<>();
    public static ImageView iv_more_app, iv_blast, ivBack;
    public static Boolean is_closed = true;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        if (Share.RestartAppForOnlyStorage(StickerActivity.this)) {
          //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(StickerActivity.this);
            GlobalData.loadAdsBanner(StickerActivity.this, adView);
            initView();
            loadSticker();
        }
    }

    private void initView() {

        rvStickerView = (RecyclerView) findViewById(R.id.rvStickerView);
        rvStickerView.setLayoutManager(new GridLayoutManager(this, 3));

        ivBack = (ImageView) findViewById(R.id.ivBack);

        iv_more_app = (ImageView) findViewById(R.id.iv_more_app);
        iv_blast = (ImageView) findViewById(R.id.iv_blast);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();
        }

        iv_more_app.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        // get lis of sticker form assets
        assetManager = getAssets();
    }

    private void loadSticker() {
        new LoadStickerFromAssets().execute();
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
                        Log.e("ad cloced", "ad closed");
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
                Log.e("else", "else");
                iv_blast.setVisibility(View.GONE);
                iv_more_app.setVisibility(View.GONE);
            }
        }

    }

    public class LoadStickerFromAssets extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(StickerActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            stickerList.clear();
            try {
                imgPath = assetManager.list("img");
                Log.e("TAG", "size===>" + imgPath.length);

                try {
                    for (int i = 0; i < imgPath.length; i++) {
                        InputStream is = assetManager.open("img/" + imgPath[i]);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        StickerModel stickerModel = new StickerModel();
                        stickerModel.setBitmap(bitmap);

                        stickerList.add(stickerModel);

                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialog.dismiss();

            stickerAdapter = new StickerAdapter(StickerActivity.this, stickerList);
            rvStickerView.setAdapter(stickerAdapter);

            stickerAdapter.setEventListener(new StickerAdapter.EventListener() {
                @Override
                public void onItemViewClicked(int position) {

                    try {
                        InputStream inputstream = StickerActivity.this.getAssets().open("img/" + imgPath[position]);
                        if (inputstream != null) {
                            Share.COLOR_POS = 0;
                            Share.SYMBOL = Drawable.createFromStream(inputstream, null);

                            if (Share.isNeedToAdShow(getApplicationContext())) {
                                if (!MyApplication.getInstance().requestNewInterstitial()) {

                                    finish();
                                } else {

                                    MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                                        @Override
                                        public void onAdClosed() {
                                            super.onAdClosed();
                                            MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                                            MyApplication.getInstance().mInterstitialAd = null;
                                            MyApplication.getInstance().ins_adRequest = null;
                                            MyApplication.getInstance().LoadAds();

                                            finish();
                                        }

                                        @Override
                                        public void onAdFailedToLoad(int i) {
                                            super.onAdFailedToLoad(i);
                                        }

                                        @Override
                                        public void onAdLoaded() {
                                            super.onAdLoaded();
                                        }
                                    });
                                }
                            } else {
                                finish();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDeleteMember(int position) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Share.isNeedToAdShow(getApplicationContext()))
            if (is_closed) {
                loadInterstialAd();
            }
    }

    private void loadInterstialAd() {
        try {
            if (MyApplication.getInstance().mInterstitialAd != null) {
                if (MyApplication.getInstance().mInterstitialAd.isLoaded()) {
                    Log.e("if", "if");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;

        stickerList.clear();
        rvStickerView.setAdapter(null);
        rvStickerView.setLayoutManager(null);
        rvStickerView.removeAllViews();
        rvStickerView.getRecycledViewPool().clear();

        System.gc();
        Runtime.getRuntime().gc();
    }
}
