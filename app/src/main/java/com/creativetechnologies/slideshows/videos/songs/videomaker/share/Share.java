package com.creativetechnologies.slideshows.videos.songs.videomaker.share;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.TEXT_MODEL;

import java.util.ArrayList;

/**
 * Created by Vishal2.vasundhara on 06-Jul-17.
 */
public class Share {

    public static boolean FirstTimePreviewActivity = false;
    public static boolean isPreviewActivity = false;
    public static boolean TwoDEffectPreviewActivity = false;
    public static boolean MoreEffectPreviewActivity = false;
    public static boolean Effect_Flag = false;
    public static boolean save_video_Flag = false;
    public static boolean twoD_Effect_Flag = false;
    public static boolean threeD_Effect_Flag = false;
    public static boolean more_Effect_Flag = false;
    public static boolean pause_video_flag = false;
    public static boolean from_my_video = false;

    public static int image_not_found = -1;

    // remove effect flag //
    public static boolean threeDEffectApply = false;
    public static boolean twoDEffectApply = false;
    public static boolean moreEffectApply = false;

    // step mark //
    public static boolean effect_step_flag = false;
    public static boolean resume_flag = false;

    public static int plus_button_click_position = 0;
    public static int temp_selected_crop_image_position = 0;
    public static int selected_crop_image_position = 0;
    public static int selected_theme_position = 0;

    public static int height_of_row = 0;
    public static int width_of_row = 0;

    public static ArrayList<ImageData> temp_2DEffect_selectedImages = new ArrayList();
    public static ArrayList<ImageData> temp_MoreEffect_selectedImages = new ArrayList();

    public static String audio_file = "";
    public static String selected_image_path = null;
    public static int selected_image_pos = 0;
    public static int selected_audio_pos = 0;
    public static String video_path = "";

    public static float buffer_progress = 0.0f;

    public static Uri BG_GALLERY = null;


    // font sticker
    public static String TEXT = "";
    public static String FONT_STYLE = "";
    public static boolean START_TEXT_EDIT_FLAG = false;
    public static Drawable EDIT_DAILOG_DRAWABLE = null;
    public static String tag_value = "text_sticker";
    public static int START_STICKER_POSITION;
    public static Drawable SYMBOL = null;
    public static boolean FLAG = false;
    public static String FONT_EFFECT = "6";
    public static int COLOR = Color.parseColor("#000000");
    public static Drawable FONT_TEXT_DRAWABLE = null;
    public static int COLOR_POS = 0;
    public static ArrayList<TEXT_MODEL> START_FONT_TEXT2 = new ArrayList<>();

    public static boolean TEXT_EDIT_FLAG = false;
    public static int STICKER_POSITION;
    public static ArrayList<TEXT_MODEL> FONT_TEXT2 = new ArrayList<>();

    // gallery flag //
    public static boolean FROM_CUSTOM_SLIDE = false;
    public static Bitmap GALLERY_BITMAP = null;

    // slide //
    public static boolean end_slide_selected_or_not = false;

    // for in-app purchase
    public static boolean isNeedToAdShow(Context context) {
        boolean isNeedToShow = true;

        if (!SharedPrefs.contain(context, SharedPrefs.IS_ADS_REMOVED)) {
            isNeedToShow = true;
        } else {
            if (!SharedPrefs.getBoolean(context, SharedPrefs.IS_ADS_REMOVED))
                isNeedToShow = true;

            else
                isNeedToShow = false;
        }
        return isNeedToShow;
    }

    public static void showAlert(final Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.MyTheme);
        try {
            if (dialog.isShowing()) {
                dialog.dismiss();
                createProgressDialog(mContext);
            } else {
                dialog.show();
            }
        } catch (Exception e) {
//			Log.e("dialog crash","crash"+e.getMessage());
            e.printStackTrace();
        }
        //dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setProgressDrawable((new ColorDrawable(Color.parseColor("#E45E46"))));
        dialog.setContentView(R.layout.progress_dialog_layout);
        return dialog;
    }

    public static Boolean RestartAppForOnlyStorage(Activity activity) {
        if (!Share.checkAndRequestPermissionsforstorage(activity, 1)) {
            resume_flag = true;
            Intent i = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAndRequestPermissionsforstorage(Activity act, int code) {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else {
            return true;
        }
    }

    public static Boolean RestartAppStorage(Activity activity) {
        if (!Share.checkAndRequestPermissions(activity, 1)) {
            resume_flag = true;
            Intent i = activity.getBaseContext().getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAndRequestPermissions(Activity act, int code) {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else if (ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else {
            return true;
        }
    }

//    getIntent().getExtras() != null && getIntent().getExtras().containsKey("avairy")
}
