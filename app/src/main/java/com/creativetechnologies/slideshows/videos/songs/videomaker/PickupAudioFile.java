package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.MusicAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.Bg_Model;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PickupAudioFile extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_custom_music;
    private MyApplication application = MyApplication.getInstance();
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rv_music;
    public static ImageView iv_more_app, iv_blast, ivBack;
    private AssetManager assetManager;
    private ArrayList<Bg_Model> bg_list = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private String[] music_array = {"song_1", "song_2", "song_3", "song_4", "song_5", "song_6", "song_7", "song_8", "song_9"};
    private int temp_resID = 0;
    public static Boolean is_closed = true;
    public static Boolean isPause = false;
    private Toolbar toolbar;
    public static int height_of_view = 0;
    private int posPlay = -1;
    private int posPause = -1;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(PickupAudioFile.this)) {
            setContentView(R.layout.activity_pickup_audio_file);

          //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(PickupAudioFile.this);

            assetManager = getAssets();
            mediaPlayer = new MediaPlayer();

            initView();
        }
    }

    private void initView() {

        ll_custom_music = (LinearLayout) findViewById(R.id.ll_custom_music);

        rv_music = (RecyclerView) findViewById(R.id.rv_music);
        gridLayoutManager = new GridLayoutManager(PickupAudioFile.this, 3);
        rv_music.setLayoutManager(gridLayoutManager);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        iv_more_app = (ImageView) findViewById(R.id.iv_more_app);
        iv_blast = (ImageView) findViewById(R.id.iv_blast);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();
        }

        ivBack.setOnClickListener(this);
        ll_custom_music.setOnClickListener(this);
        iv_more_app.setOnClickListener(this);

        ViewTreeObserver vto = rv_music.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rv_music.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height_of_view = rv_music.getHeight();
            }
        });

        new AsyncTas_Bg_Load().execute();
    }


    @Override
    public void onClick(View v) {

        if (v == ivBack) {
            onBackPressed();
        } else if (v == ll_custom_music) {
            Intent intent_upload = new Intent();
            intent_upload.setType("audio/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent_upload, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    application.isFromSdCardAudio = true;
                    InputStream in = null;
                    FileOutputStream out = null;
                    try {

                        String extension = "";
                        try {
                            Share.audio_file = PathUtil.getPath(PickupAudioFile.this, uri);
                            Log.e("TAG", "Share.audio_file " + Share.audio_file + " extension :" + extension);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        if (Share.audio_file != null) {
                            if (Share.audio_file.contains(".mp3") || Share.audio_file.contains(".wav") || Share.audio_file.contains(".wma")) {
                                FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
                                File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");

                                Log.e("TAg", "tempFile = " + tempFile.getAbsolutePath());
                                Log.e("TAg", "audio_file = " + Share.audio_file);
                                if (tempFile.exists()) {
                                    FileUtils.deleteFile(tempFile);
                                }
                                in = new FileInputStream(Share.audio_file);
                                out = new FileOutputStream(tempFile);
                                byte[] buff = new byte[1024];
                                while (true) {
                                    int read = in.read(buff);
                                    if (read <= 0) {
                                        break;
                                    }
                                    out.write(buff, 0, read);
                                }

                                if (FinalPreviewActivity.iv_step3 != null) {
                                    FinalPreviewActivity.iv_step3.setVisibility(View.VISIBLE);
                                }

                                // save video //
                                FinalPreviewActivity.playThreeDEffect = false;
                                FinalPreviewActivity.playTwoDEffect = false;
                                FinalPreviewActivity.playMoreEffect = false;

                                // when user apply twoDeffect(without play) select audio
                                Share.TwoDEffectPreviewActivity = false;
                                Share.MoreEffectPreviewActivity = false;
                                MyApplication.changeBackground_Flag = false;

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
                            } else {
                                Toast.makeText(PickupAudioFile.this, "Audio file not supported", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(PickupAudioFile.this, "Audio file not supported", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(PickupAudioFile.this, "Audio file not supported", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class AsyncTas_Bg_Load extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PickupAudioFile.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bg_list.clear();
            try {
                final String[] imgPath = assetManager.list("music_bg");
                for (int j = 0; j < imgPath.length; j++) {

                    InputStream is = assetManager.open("music_bg/" + imgPath[j]);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Bg_Model bg_model = new Bg_Model();

                    bg_model.setBitmap(bitmap);

                    bg_list.add(bg_model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if ((pd != null) && pd.isShowing()) {
                pd.dismiss();
            }

            MusicAdapter music_adapter = new MusicAdapter(PickupAudioFile.this, bg_list);
            rv_music.setAdapter(music_adapter);

            music_adapter.setEventListener(new MusicAdapter.EventListener() {
                @Override
                public void onItemViewClicked(int position, View view) {

                    int resID = getResources().getIdentifier(music_array[position], "raw", getPackageName());

                    try {
                        if (FinalPreviewActivity.iv_step3 != null) {
                            FinalPreviewActivity.iv_step3.setVisibility(View.VISIBLE);
                        }

                        application.isFromSdCardAudio = true;

                        // save video //
                        FinalPreviewActivity.playThreeDEffect = false;
                        FinalPreviewActivity.playTwoDEffect = false;
                        FinalPreviewActivity.playMoreEffect = false;

                        // when user apply twoDeffect(without play) select audio
                        Share.TwoDEffectPreviewActivity = false;
                        Share.MoreEffectPreviewActivity = false;
                        MyApplication.changeBackground_Flag = false;

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

                        File dst = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");
                        if (dst.exists()) {
                            FileUtils.deleteFile(dst);
                        }
                        Share.audio_file = dst.getAbsolutePath();
                        CopyRAWtoSDCard(resID, dst.getAbsolutePath());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onItemViewClickedPlay(int position, View ivPlay, View ivPause) {

                    int resID = getResources().getIdentifier(music_array[position], "raw", getPackageName());
                    posPlay = position;
                    if (temp_resID == resID) {
                        if (isPause) {
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    viewVisibaleGonePause();
                                }
                            });

                            mediaPlayer.start();
                        } else {
                            mediaPlayer.release();
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);
                            mediaPlayer.start();
                        }
                    } else {
                        temp_resID = resID;
                        mediaPlayer.release();
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), resID);

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                viewVisibaleGonePause();
                            }
                        });
                        mediaPlayer.start();
                    }

                    viewVisibaleGonePlay();

                }

                @Override
                public void onItemViewClickedPause(int position, View ivPlay, View ivPause) {

                    posPause = position;

                    if (mediaPlayer.isPlaying()) {
                        isPause = true;
                        mediaPlayer.pause();
                    }

                    viewVisibaleGonePause();
                }

                @Override
                public void onDeleteMember(int position, View view) {

                }
            });
        }
    }

    private void viewVisibaleGonePause() {
        for (int i = 0; i < rv_music.getChildCount(); i++) {

            View view = rv_music.findViewHolderForAdapterPosition(i).itemView;

            ImageView ivPlay = (ImageView) view.findViewById(R.id.iv_play);
            ImageView ivPause = (ImageView) view.findViewById(R.id.iv_stop);

            if (posPause == i) {
                ivPause.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
            } else {
                ivPlay.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.GONE);
            }
        }
    }

    private void viewVisibaleGonePlay() {

        for (int i = 0; i < rv_music.getChildCount(); i++) {

            View view = rv_music.findViewHolderForAdapterPosition(i).itemView;

            ImageView ivPlay = (ImageView) view.findViewById(R.id.iv_play);
            ImageView ivPause = (ImageView) view.findViewById(R.id.iv_stop);

            if (posPlay == i) {
                ivPlay.setVisibility(View.GONE);
                ivPause.setVisibility(View.VISIBLE);
            } else {
                ivPause.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
            }
        }
    }

    private void CopyRAWtoSDCard(int id, String path) throws IOException {

        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();

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
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            viewVisibaleGonePause();
        }

        if ((pd != null) && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Share.resume_flag = false;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
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

            super.onBackPressed();
            finish();
    }
}
