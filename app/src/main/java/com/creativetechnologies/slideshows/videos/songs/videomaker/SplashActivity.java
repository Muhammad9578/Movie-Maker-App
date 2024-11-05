package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eyalbira.loadingdots.LoadingDots;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

public class SplashActivity extends AppCompatActivity {
    String TAG = "TAG";
    ProgressBar progressBar;
    LoadingDots process_dots;
    TextView tv_loading;

    private Handler timeoutHandler = new Handler();
    Runnable runnable;
    private boolean is_pause = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        progressBar = findViewById(R.id.progressBar);
        tv_loading = findViewById(R.id.tv_loading);
        process_dots = findViewById(R.id.process_dots);

//        if (Share.isNeedToAdShow(getApplicationContext())) {

            runnable = new Runnable() {
                public void run() {
                    nextScreen();
                }
            };
            timeoutHandler.postDelayed(runnable, 3000);
//        } else {
//
//            runnable = new Runnable() {
//                public void run() {
//                    nextScreen();
//                }
//            };
//            timeoutHandler.postDelayed(runnable, 10);
//        }
        }


    private void nextScreen() {

        if (Share.isNeedToAdShow(getApplicationContext())) {
            startActivity(new Intent(SplashActivity.this, SplashMenuActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!MyApplication.getInstance().isLoaded())
            MyApplication.getInstance().LoadAds();

        if (is_pause) {
            is_pause = false;
            nextScreen();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
        is_pause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        is_pause = true;
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_pause = true;
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
    }
}
