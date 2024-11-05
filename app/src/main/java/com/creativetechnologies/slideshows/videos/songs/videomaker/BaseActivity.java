package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by india on 06-03-2016.
 */
public class BaseActivity extends AppCompatActivity {

    public ProgressDialog dialog;

    public void ShowProgressDialog(Activity activity, String message) {
        /*if (dialog != null) {
            dialog.dismiss();
        }*/
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(activity);
                dialog.setMessage(message);
                dialog.setCancelable(false);

                if (!activity.isFinishing())
                    dialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
