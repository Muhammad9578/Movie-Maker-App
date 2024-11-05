package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.MyVideoAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

public class MyVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rcv_images;
    private GridLayoutManager gridLayoutManager;
    public static MyVideoAdapter videoAdapter;
    public static ArrayList<File> al_my_photos = new ArrayList<>();
    private File[] allFiles;
    private RelativeLayout rl_my_photos;
    private LinearLayout ll_no_photos;
    public int posi;
    private ImageView iv_all_delete, iv_back;
    private AdView adView;
   // private FirebaseAnalytics mFirebaseAnalytics;
    public static ProgressDialog dialog;

    private boolean file_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Share.RestartAppForOnlyStorage(MyVideoActivity.this)) {
            setContentView(R.layout.activity_my_video);

          //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(MyVideoActivity.this);
            rcv_images = (RecyclerView) findViewById(R.id.rcv_images);

            gridLayoutManager = new GridLayoutManager(this, 3);
            rl_my_photos = (RelativeLayout) findViewById(R.id.rl_my_photos);
            ll_no_photos = (LinearLayout) findViewById(R.id.ll_no_photos);
            rcv_images.setLayoutManager(gridLayoutManager);

            iv_all_delete = (ImageView) findViewById(R.id.iv_all_delete);
            iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_all_delete.setOnClickListener(this);
            iv_back.setOnClickListener(this);

            GlobalData.loadAdsBanner(MyVideoActivity.this, adView);

            new LoadMyVideo().execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {
            if (videoAdapter != null) {
                if (al_my_photos.size() != 0) {
                    videoAdapter.notifyDataSetChanged();
                    rl_my_photos.setVisibility(View.VISIBLE);
                    ll_no_photos.setVisibility(View.GONE);
                    iv_all_delete.setAlpha(1f);
                    iv_all_delete.setEnabled(true);
                } else {
                    al_my_photos.clear();
                    iv_all_delete.setAlpha(0.5f);
                    iv_all_delete.setEnabled(false);
                    rl_my_photos.setVisibility(View.GONE);
                    ll_no_photos.setVisibility(View.VISIBLE);
                }
            }
        }
//        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;
    }

    public class LoadMyVideo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MyVideoActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            File path = new File(Environment.getExternalStoragePublicDirectory("PhotoVideoMaker"), "");
            al_my_photos.clear();
            allFiles = null;
            if (path.exists()) {

                Log.e("if1", "if1");
                allFiles = path.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".mp4"));
                    }
                });
                Log.e("TAG", "array_size =>" + allFiles.length);
                if (allFiles.length > 0) {

                    file_flag = true;

                    for (int i = 0; i < allFiles.length; i++) {
                        al_my_photos.add(allFiles[i]);
                        ///Now set this bitmap on imageview
                    }

                    Collections.reverse(al_my_photos);

                } else {
                    file_flag = false;
                }
            } else {
                file_flag = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (file_flag) {
                rl_my_photos.setVisibility(View.VISIBLE);
                ll_no_photos.setVisibility(View.GONE);
                iv_all_delete.setAlpha(1f);
                iv_all_delete.setEnabled(true);
            } else {
                al_my_photos.clear();
                iv_all_delete.setAlpha(0.5f);
                iv_all_delete.setEnabled(false);
                rl_my_photos.setVisibility(View.GONE);
                ll_no_photos.setVisibility(View.VISIBLE);
            }

            dialog.dismiss();
            videoAdapter = new MyVideoAdapter(MyVideoActivity.this, new MyVideoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {

                    if (Share.isNeedToAdShow(getApplicationContext())) {
                        if (!MyApplication.getInstance().requestNewInterstitial()) {

                            Share.from_my_video = true;
                            Share.selected_theme_position = position;
                            Share.video_path = al_my_photos.get(position).getAbsolutePath();
                            go_on_fullscreenimage();

                        } else {

                            MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                                    MyApplication.getInstance().mInterstitialAd = null;
                                    MyApplication.getInstance().ins_adRequest = null;
                                    MyApplication.getInstance().LoadAds();

                                    Share.from_my_video = true;
                                    Share.selected_theme_position = position;
                                    Share.video_path = al_my_photos.get(position).getAbsolutePath();
                                    go_on_fullscreenimage();

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
                        Share.from_my_video = true;
                        Share.selected_theme_position = position;
                        Share.video_path = al_my_photos.get(position).getAbsolutePath();
                        go_on_fullscreenimage();
                    }
                }
            });
            rcv_images.setAdapter(videoAdapter);
        }
    }


    private void go_on_fullscreenimage() {
        Intent intent = new Intent(MyVideoActivity.this, SaveVideoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if (v == iv_all_delete) {
            deleteVideo();
        } else if (v == iv_back) {
            finish();
        }
    }

    private void deleteVideo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialog);
        //builder.setMessage(getResources().getString(R.string.delete_emoji_msg));
        builder.setMessage("Are you sure want to delete all videos ?");
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (int i = 0; i < al_my_photos.size(); i++) {
                    File f = new File(String.valueOf(al_my_photos.get(i)));
                    Log.e("images file 12345 :  ", " ==============" + al_my_photos.get(i) + "  -----------" + f.toString());

                    f.delete();
                    al_my_photos.get(i).delete();
                    //al_my_photos.remove(i);
                }
                al_my_photos.clear();

                if (al_my_photos.size() == 0) {
                    iv_all_delete.setAlpha(0.5f);
                    iv_all_delete.setEnabled(false);
                    rl_my_photos.setVisibility(View.GONE);
                    ll_no_photos.setVisibility(View.VISIBLE);
                }
//                setData();
                new LoadMyVideo().execute();
                videoAdapter.notifyDataSetChanged();
                al_my_photos.clear();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        builder.show();
    }
}
