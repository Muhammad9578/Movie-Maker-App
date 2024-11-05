package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mvc.imagepicker.ImagePicker;
import com.creativetechnologies.slideshows.videos.songs.videomaker.StickerViewEndSlide.EndDrawableSticker;
import com.creativetechnologies.slideshows.videos.songs.videomaker.StickerViewEndSlide.EndStickerView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.StickerViewStartSlide.DrawableSticker;
import com.creativetechnologies.slideshows.videos.songs.videomaker.StickerViewStartSlide.StickerView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.EndSlideBackgroundAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.StartSlideBackgroundAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.TEXT_MODEL;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.Manifest.permission.CAMERA;


public class CustomSlideActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llStart, llEnd, llStartView, llEndView;
    private TextView tvStart, tvEnd;
    private ImageView ivStart, ivEnd, ivBack, ivNext, ivSkip;
    private RelativeLayout rl_main_start, rl_main_end, rlMain;
    private ImageView ivSticker, ivTextSticker, ivGalleryMenu, ivCamera;
    /// new sticker variable //
    private AssetManager assetManager;
    public static int pos;
    int finalHeight, finalWidth;
    private RecyclerView rvStartBackground, rvEndBackground;
    private StartSlideBackgroundAdapter startSlideBackgroundAdapter;
    private EndSlideBackgroundAdapter endSlideBackgroundAdapter;
    private boolean setEndSize = false;

    private MyApplication application = MyApplication.getInstance();

    private int[] start_bg_array = {R.drawable.ic_start_1, R.drawable.ic_start_2, R.drawable.ic_start_3, R.drawable.ic_start_4, R.drawable.ic_start_5, R.drawable.ic_start_6, R.drawable.ic_start_7, R.drawable.ic_start_8,
            R.drawable.ic_start_9, R.drawable.ic_start_10, R.drawable.ic_start_11, R.drawable.ic_start_12, R.drawable.ic_start_13, R.drawable.ic_start_14, R.drawable.ic_start_15, R.drawable.ic_start_16, R.drawable.ic_start_17,
            R.drawable.ic_start_18, R.drawable.ic_start_19};

    private int[] end_bg_array = {R.drawable.ic_end_1, R.drawable.ic_end_2, R.drawable.ic_end_3, R.drawable.ic_end_4, R.drawable.ic_end_5, R.drawable.ic_end_6, R.drawable.ic_end_7, R.drawable.ic_end_8,
            R.drawable.ic_end_9, R.drawable.ic_end_10, R.drawable.ic_end_11, R.drawable.ic_end_12, R.drawable.ic_end_13, R.drawable.ic_end_14, R.drawable.ic_end_15, R.drawable.ic_end_16, R.drawable.ic_end_17,
            R.drawable.ic_end_18, R.drawable.ic_end_19};

    int height = 0;

    public static StickerView stickerView;
    public static EndStickerView end_sticker_view;
    private ViewTreeObserver vto, vto1;

    public static boolean start_end_tab_flag = false;
    public boolean start_slide_select_flag = true;
    private boolean isOpenPermissionDialog = false;
    private boolean isLoadImageSelection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_slide);

        if (Share.RestartAppForOnlyStorage(CustomSlideActivity.this)) {
            initView();
            loadStartBg();
            loadEndBg();
        }
    }

    private void initView() {

        assetManager = getAssets();

        stickerView = findViewById(R.id.sticker_view);
        end_sticker_view = findViewById(R.id.end_sticker_view);

        ivStart = findViewById(R.id.ivStart);
        ivEnd = findViewById(R.id.ivEnd);
        ivTextSticker = findViewById(R.id.ivTextSticker);
        ivSticker = findViewById(R.id.ivSticker);
        ivGalleryMenu = findViewById(R.id.ivGalleryMenu);
        ivCamera = findViewById(R.id.ivCamera);
        ivBack = findViewById(R.id.ivBack);
        ivNext = findViewById(R.id.ivNext);
        ivSkip = findViewById(R.id.ivSkip);

        rl_main_start = findViewById(R.id.rl_main_start);
        rl_main_end = findViewById(R.id.rl_main_end);

        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);

        llStart = findViewById(R.id.llStart);
        llEnd = findViewById(R.id.llEnd);
        llStartView = findViewById(R.id.llStartView);
        llEndView = findViewById(R.id.llEndView);

        llStart.setOnClickListener(this);
        llEnd.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        ivEnd.setOnClickListener(this);
        ivTextSticker.setOnClickListener(this);
        ivSticker.setOnClickListener(this);
        ivGalleryMenu.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivSkip.setOnClickListener(this);

        setStartSlide(R.drawable.ic_start_1);
        ivStart.setImageResource(start_bg_array[0]);
        rvStartBackground = findViewById(R.id.rvStartBackground);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(CustomSlideActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvStartBackground.setLayoutManager(linearLayoutManager2);
        startSlideBackgroundAdapter = new StartSlideBackgroundAdapter(CustomSlideActivity.this);
        rvStartBackground.setAdapter(startSlideBackgroundAdapter);

        setEndSlide(R.drawable.ic_end_1);
        ivEnd.setImageResource(end_bg_array[0]);
        rvEndBackground = findViewById(R.id.rvEndBackground);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(CustomSlideActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvEndBackground.setLayoutManager(linearLayoutManager3);
        endSlideBackgroundAdapter = new EndSlideBackgroundAdapter(CustomSlideActivity.this);
        rvEndBackground.setAdapter(endSlideBackgroundAdapter);

        // change color of selection
        GradientDrawable original = (GradientDrawable) llStart.getBackground();
        original.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

        GradientDrawable suqare = (GradientDrawable) llEnd.getBackground();
        suqare.setColor(getApplicationContext().getResources().getColor(R.color.selection_color));

        // start and end view visibility
        llStartView.setVisibility(View.VISIBLE);
        llEndView.setVisibility(View.INVISIBLE);

        // true default select tab start
        start_end_tab_flag = true;

        // default end slide selection //
        Share.end_slide_selected_or_not = true;

        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {
            if (Share.EDIT_DAILOG_DRAWABLE != null) {

                if (start_end_tab_flag) {
                    try {
                        if (Share.START_TEXT_EDIT_FLAG) {
                            Share.START_TEXT_EDIT_FLAG = false;
                            DrawableSticker sticker = new DrawableSticker(Share.EDIT_DAILOG_DRAWABLE);
                            if (StickerView.mHandlingSticker.getTag() != null) {
                                if (StickerView.mHandlingSticker.getTag().equals("text_sticker")) {
                                    sticker.setTag("text_sticker");
                                }
                            }

                            stickerView.replace(sticker);
                        } else {

                            DrawableSticker sticker = new DrawableSticker(Share.EDIT_DAILOG_DRAWABLE);
                            sticker.setTag("text_sticker");
                            stickerView.addSticker(sticker);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (Share.TEXT_EDIT_FLAG) {
                            Share.TEXT_EDIT_FLAG = false;
                            EndDrawableSticker sticker = new EndDrawableSticker(Share.EDIT_DAILOG_DRAWABLE);
                            if (EndStickerView.mHandlingSticker.getTag() != null) {
                                if (EndStickerView.mHandlingSticker.getTag().equals("text_sticker")) {
                                    sticker.setTag("text_sticker");
                                }
                            }

                            end_sticker_view.replace(sticker);
                        } else {

                            EndDrawableSticker sticker = new EndDrawableSticker(Share.EDIT_DAILOG_DRAWABLE);
                            sticker.setTag("text_sticker");
                            end_sticker_view.addSticker(sticker);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (Share.SYMBOL != null) {
                if (start_end_tab_flag) {
                    DrawableSticker sticker = new DrawableSticker(Share.SYMBOL);
                    sticker.setTag("sticker");
                    TEXT_MODEL model = new TEXT_MODEL("Duplicate", null, -16777216);
                    Share.START_FONT_TEXT2.add(model);
                    stickerView.addSticker(sticker);
                } else {
                    EndDrawableSticker sticker = new EndDrawableSticker(Share.SYMBOL);
                    sticker.setTag("sticker");
                    TEXT_MODEL model = new TEXT_MODEL("Duplicate", null, -16777216);
                    Share.FONT_TEXT2.add(model);
                    end_sticker_view.addSticker(sticker);
                }
            }


            if (Share.GALLERY_BITMAP != null) {
                if (start_end_tab_flag) {

                    ivStart.invalidate();
                    ivStart.setImageBitmap(null);
                    ivStart.setImageDrawable(null);
                    ivStart.setImageBitmap(Share.GALLERY_BITMAP);

                    setStartSlide(R.drawable.no_selection); // remove selection when select image from gallery
                    if (startSlideBackgroundAdapter != null) {
                        startSlideBackgroundAdapter.notifyDataSetChanged();
                    }

                    if (Share.GALLERY_BITMAP.getHeight() > Share.GALLERY_BITMAP.getWidth()) {
                        Log.e("Image", "getHeight() > getWidth()");

                        ivStart.setScaleType(ImageView.ScaleType.FIT_XY);

                        height = (int) Math.ceil(GlobalData.screenWidth * (float) ivStart.getDrawable().getIntrinsicHeight() / ivStart.getDrawable().getIntrinsicWidth());
                        ivStart.getLayoutParams().height = height;

                        vto = ivStart.getViewTreeObserver();
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {

                                ivStart.getViewTreeObserver().removeOnPreDrawListener(this);
                                finalHeight = ivStart.getMeasuredHeight();
                                finalWidth = ivStart.getMeasuredWidth();

                                Log.e("TAG", "if finalHeight :" + finalHeight);
                                Log.e("TAG", "if finalWidth :" + finalWidth);

                                if (height > finalWidth) {
                                    Log.e("Image", "Taller");
                                    finalWidth = (int) Math.ceil(finalHeight * (float) ivStart.getDrawable().getIntrinsicWidth() / ivStart.getDrawable().getIntrinsicHeight());
                                }
                                ivStart.getLayoutParams().width = finalWidth;

//                                LinearLayout.LayoutParams llMain = new LinearLayout.LayoutParams(finalWidth, finalHeight);
//                                llMain.gravity = Gravity.CENTER;
//                                rl_main_start.setLayoutParams(llMain);

                                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                                rel_btn.addRule(RelativeLayout.CENTER_IN_PARENT);
                                stickerView.setLayoutParams(rel_btn);

                                return true;
                            }
                        });

                    } else {

                        Log.e("Image", "getWidth() > getHeight()");

                        ivStart.setScaleType(ImageView.ScaleType.FIT_XY);

                        height = (int) Math.ceil(GlobalData.screenWidth * (float) ivStart.getDrawable().getIntrinsicHeight() / ivStart.getDrawable().getIntrinsicWidth());
                        ivStart.getLayoutParams().height = height;

                        vto = ivStart.getViewTreeObserver();
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {
                                // Remove after the first run so it doesn't fire forever
                                ivStart.getViewTreeObserver().removeOnPreDrawListener(this);

                                finalHeight = ivStart.getMeasuredHeight();
                                finalWidth = ivStart.getMeasuredWidth();

                                Log.e("TAG", "else finalWidth :" + finalWidth + " else finalHeight :" + finalHeight);

                                if (height > finalWidth) {
                                    Log.e("Image", "Taller");
                                    finalWidth = (int) Math.ceil(finalHeight * (float) ivStart.getDrawable().getIntrinsicWidth() / ivStart.getDrawable().getIntrinsicHeight());
                                }
                                ivStart.getLayoutParams().width = finalWidth;

//                                LinearLayout.LayoutParams llMain = new LinearLayout.LayoutParams(finalWidth, finalHeight);
//                                llMain.gravity = Gravity.CENTER;
//                                rl_main_start.setLayoutParams(llMain);

                                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                                rel_btn.addRule(RelativeLayout.CENTER_IN_PARENT);
                                stickerView.setLayoutParams(rel_btn);

                                return true;
                            }
                        });
                    }

                    Share.GALLERY_BITMAP = null;
                } else {
                    ivEnd.invalidate();
                    ivEnd.setImageBitmap(null);
                    ivEnd.setImageDrawable(null);
                    ivEnd.setImageBitmap(Share.GALLERY_BITMAP);

                    setEndSlide(R.drawable.no_selection); // remove selection when select image from gallery
                    if (endSlideBackgroundAdapter != null) {
                        endSlideBackgroundAdapter.notifyDataSetChanged();
                    }

                    ivEnd.setScaleType(ImageView.ScaleType.FIT_XY);

                    if (Share.GALLERY_BITMAP.getHeight() > Share.GALLERY_BITMAP.getWidth()) {


                        height = (int) Math.ceil(GlobalData.screenWidth * (float) ivEnd.getDrawable().getIntrinsicHeight() / ivEnd.getDrawable().getIntrinsicWidth());
                        ivEnd.getLayoutParams().height = height;

                        vto = ivEnd.getViewTreeObserver();
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {

                                ivEnd.getViewTreeObserver().removeOnPreDrawListener(this);
                                finalHeight = ivEnd.getMeasuredHeight();
                                finalWidth = ivEnd.getMeasuredWidth();

                                Log.e("TAG", "finalHeight :" + finalHeight);
                                Log.e("TAG", "finalWidth :" + finalWidth);

                                if (height > finalWidth) {
                                    Log.e("Image", "Taller");
                                    finalWidth = (int) Math.ceil(finalHeight * (float) ivEnd.getDrawable().getIntrinsicWidth() / ivEnd.getDrawable().getIntrinsicHeight());
                                }
                                ivEnd.getLayoutParams().width = finalWidth;

//                                LinearLayout.LayoutParams llMain = new LinearLayout.LayoutParams(finalWidth, finalHeight);
//                                llMain.gravity = Gravity.CENTER;
//                                rl_main_end.setLayoutParams(llMain);

                                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                                rel_btn.addRule(RelativeLayout.CENTER_IN_PARENT);
                                end_sticker_view.setLayoutParams(rel_btn);

                                return true;
                            }
                        });

                    } else {

                        height = (int) Math.ceil(GlobalData.screenWidth * (float) ivEnd.getDrawable().getIntrinsicHeight() / ivEnd.getDrawable().getIntrinsicWidth());
                        ivEnd.getLayoutParams().height = height;

                        vto = ivEnd.getViewTreeObserver();
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {
                                // Remove after the first run so it doesn't fire forever
                                ivEnd.getViewTreeObserver().removeOnPreDrawListener(this);

                                finalHeight = ivEnd.getMeasuredHeight();
                                finalWidth = ivEnd.getMeasuredWidth();

                                Log.e("TAG", "Width :" + finalWidth + " Height :" + finalHeight);

                                if (height > finalWidth) {
                                    Log.e("Image", "Taller");
                                    finalWidth = (int) Math.ceil(finalHeight * (float) ivEnd.getDrawable().getIntrinsicWidth() / ivEnd.getDrawable().getIntrinsicHeight());
                                }
                                ivEnd.getLayoutParams().width = finalWidth;

//                                LinearLayout.LayoutParams llMain = new LinearLayout.LayoutParams(finalWidth, finalHeight);
//                                llMain.gravity = Gravity.CENTER;
//                                rl_main_end.setLayoutParams(llMain);

                                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                                rel_btn.addRule(RelativeLayout.CENTER_IN_PARENT);
                                end_sticker_view.setLayoutParams(rel_btn);

                                return true;
                            }
                        });
                    }

                    Share.GALLERY_BITMAP = null;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Share.SYMBOL = null;
        Share.FONT_TEXT_DRAWABLE = null;
        Share.EDIT_DAILOG_DRAWABLE = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;

        Share.FONT_TEXT_DRAWABLE = null;
        Share.SYMBOL = null;
        Share.EDIT_DAILOG_DRAWABLE = null;

        if (stickerView != null && StickerView.mStickers.size() > 0)
            stickerView.setControlItemsHidden();

        if (end_sticker_view != null && EndStickerView.mEndStickers.size() > 0)
            end_sticker_view.setControlItemsHidden();

        StickerView.mStickers.clear();
        EndStickerView.mEndStickers.clear();

        Share.COLOR = Color.parseColor("#000000");

        Share.START_STICKER_POSITION = 0;
        Share.STICKER_POSITION = 0;

        Share.START_FONT_TEXT2.clear();
        Share.FONT_TEXT2.clear();

        setStartSlide(R.drawable.ic_start_1);
        setEndSlide(R.drawable.ic_end_1);

        rvStartBackground.setAdapter(null);
        rvStartBackground.setLayoutManager(null);
        rvStartBackground.removeAllViews();
        rvStartBackground.getRecycledViewPool().clear();

        rvEndBackground.setAdapter(null);
        rvEndBackground.setLayoutManager(null);
        rvEndBackground.removeAllViews();
        rvEndBackground.getRecycledViewPool().clear();

        System.gc();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onClick(View v) {

        if (v == llStart) {

            // change color of selection
            GradientDrawable original = (GradientDrawable) llStart.getBackground();
            original.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

            GradientDrawable suqare = (GradientDrawable) llEnd.getBackground();
            suqare.setColor(getApplicationContext().getResources().getColor(R.color.selection_color));


            // start and end view visibility
            llStartView.setVisibility(View.VISIBLE);
            llEndView.setVisibility(View.GONE);

            rvStartBackground.setVisibility(View.VISIBLE);
            rvEndBackground.setVisibility(View.GONE);

            // true when select tab start
            start_end_tab_flag = true;

        } else if (v == llEnd) {

            // change color of selection
            GradientDrawable original = (GradientDrawable) llStart.getBackground();
            original.setColor(getApplicationContext().getResources().getColor(R.color.selection_color));

            GradientDrawable suqare = (GradientDrawable) llEnd.getBackground();
            suqare.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

            // start and end view visibility
            llEndView.setVisibility(View.VISIBLE);
            llStartView.setVisibility(View.GONE);

            rvEndBackground.setVisibility(View.VISIBLE);
            rvStartBackground.setVisibility(View.GONE);

            // false when select tab start
            start_end_tab_flag = false;

        } else if (v == ivTextSticker) {
            Share.FLAG = true;
            Intent intent = new Intent(CustomSlideActivity.this, FontActivity.class);
            startActivity(intent);
        } else if (v == ivSticker) {
            Intent intent = new Intent(CustomSlideActivity.this, StickerActivity.class);
            startActivity(intent);
        } else if (v == ivGalleryMenu) {
            Share.FROM_CUSTOM_SLIDE = true;
            Intent i = new Intent(CustomSlideActivity.this, PhotoPickupActivity.class);
            startActivity(i);
        } else if (v == ivCamera) {
            myCameraSelection();

        } else if (v == ivBack) {
            onBackPressed();
        } else if (v == ivNext) {
            new SaveCustomSlide().execute();
        } else if (v == ivSkip) {

            if (PhotoPickupImageActivity.activity != null) {
                PhotoPickupImageActivity.activity.finish();
            }

            if (SelectedPhotoArrangeActivity.activity != null) {
                SelectedPhotoArrangeActivity.activity.finish();
            }
            Share.FirstTimePreviewActivity = true;
            startActivity(new Intent(CustomSlideActivity.this, FinalPreviewActivity.class));
            finish();
        }
    }

    private void loadStartBg() {

        startSlideBackgroundAdapter.setEventListener(new StartSlideBackgroundAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                if (start_bg_array.length - 1 == position) {
                    ivStart.invalidate();
                    ivStart.setImageBitmap(null);
                    ivStart.setImageResource(start_bg_array[position]);
                    setImageSize();
                    start_slide_select_flag = false;
                } else {
                    start_slide_select_flag = true;
                    ivStart.invalidate();
                    ivStart.setImageBitmap(null);
                    ivStart.setImageResource(start_bg_array[position]);
                    setImageSize();
                }
            }

            @Override
            public void onDeleteMember(int position) {
            }
        });

        setImageSize();
    }

    private void setImageSize() {

        ivStart.setScaleType(ImageView.ScaleType.FIT_XY);

        height = (int) Math.ceil(GlobalData.screenWidth * (float) ivStart.getDrawable().getIntrinsicHeight() / ivStart.getDrawable().getIntrinsicWidth());
        ivStart.getLayoutParams().height = height;

        vto = ivStart.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // Remove after the first run so it doesn't fire forever
                ivStart.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = ivStart.getMeasuredHeight();
                finalWidth = ivStart.getMeasuredWidth();

                if (height > finalWidth) {
                    finalWidth = (int) Math.ceil(finalHeight * (float) ivStart.getDrawable().getIntrinsicWidth() / ivStart.getDrawable().getIntrinsicHeight());
                }
                Log.e("TAG", "end finalWidth " + finalWidth);
                ivStart.getLayoutParams().width = finalWidth;

//                LinearLayout.LayoutParams llMain = new LinearLayout.LayoutParams(finalWidth, finalHeight);
//                llMain.gravity = Gravity.CENTER;
//                rl_main_start.setLayoutParams(llMain);

                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                rel_btn.addRule(RelativeLayout.CENTER_IN_PARENT);
                stickerView.setLayoutParams(rel_btn);

                // set sticker size //
                if (!setEndSize) {
                    setEndSize = true;
                    setEndImageSize();
                }

                return true;
            }
        });
    }

    private void loadEndBg() {

        endSlideBackgroundAdapter.setEventListener(new EndSlideBackgroundAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                if (end_bg_array.length - 1 == position) {
                    ivEnd.invalidate();
                    ivEnd.setImageBitmap(null);
                    ivEnd.setImageResource(end_bg_array[position]);
                    setEndImageSize();
                    Share.end_slide_selected_or_not = false;
                } else {
                    Share.end_slide_selected_or_not = true;
                    ivEnd.invalidate();
                    ivEnd.setImageBitmap(null);
                    ivEnd.setImageResource(end_bg_array[position]);
                    setEndImageSize();
                }
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });
    }

    private void setEndImageSize() {

        ivEnd.setScaleType(ImageView.ScaleType.FIT_XY);

        height = (int) Math.ceil(GlobalData.screenWidth * (float) ivEnd.getDrawable().getIntrinsicHeight() / ivEnd.getDrawable().getIntrinsicWidth());
        ivEnd.getLayoutParams().height = height;

        vto = ivEnd.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // Remove after the first run so it doesn't fire forever
                ivEnd.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = ivEnd.getMeasuredHeight();
                finalWidth = ivEnd.getMeasuredWidth();

                if (height > finalWidth) {
                    finalWidth = (int) Math.ceil(finalHeight * (float) ivEnd.getDrawable().getIntrinsicWidth() / ivEnd.getDrawable().getIntrinsicHeight());
                }
                Log.e("TAG", "start finalWidth " + finalWidth);
                ivEnd.getLayoutParams().width = finalWidth;

//                LinearLayout.LayoutParams llMain = new LinearLayout.LayoutParams(finalWidth, finalHeight);
//                llMain.gravity = Gravity.CENTER;
//                rl_main_start.setLayoutParams(llMain);

                RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(finalWidth, finalHeight);
                rel_btn.addRule(RelativeLayout.CENTER_IN_PARENT);
                end_sticker_view.setLayoutParams(rel_btn);

                return true;
            }
        });

    }

    public class SaveCustomSlide extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(CustomSlideActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            stickerView.setControlItemsHidden();
            end_sticker_view.setControlItemsHidden();

            //save start slide
            rl_main_start.setDrawingCacheEnabled(true);
            Bitmap start_bitmap = Bitmap.createBitmap(rl_main_start.getDrawingCache());
            rl_main_start.setDrawingCacheEnabled(false);

            rl_main_end.setDrawingCacheEnabled(true);
            Bitmap end_bitmap = Bitmap.createBitmap(rl_main_end.getDrawingCache());
            rl_main_end.setDrawingCacheEnabled(false);


            for (int i = 0; i < 2; i++) {
                File imageFile;
                Bitmap bitmap;
                if (i == 0) {
                    imageFile = new File(FileUtils.TEMP_DIRECTORY_SLIDE, "slide_image" + ".jpg");
                    bitmap = start_bitmap;
                } else {
                    imageFile = new File(FileUtils.TEMP_DIRECTORY_SLIDE, "end_image" + ".jpg");
                    bitmap = end_bitmap;
                }
                if (imageFile.exists()) {
                    imageFile.delete();
                }

                OutputStream fos = null;
                try {
                    fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();

                    MediaScannerConnection.scanFile(CustomSlideActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                        @Override
                        public void onMediaScannerConnected() {

                        }

                        @Override
                        public void onScanCompleted(String path, final Uri uri) {

                        }
                    });

                    if (i == 0) {

                        if (start_slide_select_flag) {
                            start_slide_select_flag = false;
                            ImageData imageData = new ImageData();
                            imageData.setImagePath(imageFile.getAbsolutePath());
                            imageData.setTemp_imagePath(imageFile.getAbsolutePath());
                            imageData.setTemp_org_imagePath(imageFile.getAbsolutePath());

                            application.getSelectedImages().add(0, imageData);
                            application.getOrgSelectedImages().add(0, imageData);
                            application.getTempOrgSelectedImages().add(0, imageData);
                        }
                    } else {

                        if (Share.end_slide_selected_or_not) {
                            ImageData imageData = new ImageData();
                            imageData.setImagePath(imageFile.getAbsolutePath());
                            imageData.setTemp_imagePath(imageFile.getAbsolutePath());
                            imageData.setTemp_org_imagePath(imageFile.getAbsolutePath());

                            application.getSelectedImages().add(application.getSelectedImages().size(), imageData);
                            application.getOrgSelectedImages().add(application.getOrgSelectedImages().size(), imageData);
                            application.getTempOrgSelectedImages().add(application.getTempOrgSelectedImages().size(), imageData);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                bitmap.recycle();
            }
            start_bitmap.recycle();
            end_bitmap.recycle();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (Share.isNeedToAdShow(getApplicationContext())) {
                redirectActivity();
            }
        }
    }

    private void redirectActivity() {
        try {
            if (PhotoPickupImageActivity.activity != null) {
                PhotoPickupImageActivity.activity.finish();
            }

            if (SelectedPhotoArrangeActivity.activity != null) {
                SelectedPhotoArrangeActivity.activity.finish();
            }
            Share.FirstTimePreviewActivity = true;
            startActivity(new Intent(CustomSlideActivity.this, FinalPreviewActivity.class));
            finish();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void myCameraSelection() {
        try {
            if (!isOpenPermissionDialog) {
                if (checkAndRequestPermissionsCamera(1) && !isLoadImageSelection) {
                    ImagePicker.pickImage(CustomSlideActivity.this, "Select your image:");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri uri = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            Log.e("TAG", "Camera uri==>" + uri);
            if (uri != null) {
                Share.FROM_CUSTOM_SLIDE = true;
                Share.BG_GALLERY = null;
                Share.BG_GALLERY = uri;
                Intent intent = new Intent(CustomSlideActivity.this, CropActivity.class);
                startActivity(intent);
            }
        }
    }

    private boolean checkAndRequestPermissionsCamera(int code) {

        if (ContextCompat.checkSelfPermission(CustomSlideActivity.this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomSlideActivity.this, new String[]{CAMERA},
                    code);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }

        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                    if (requestCode == 1) {
                        ActivityCompat.requestPermissions(CustomSlideActivity.this, new String[]{CAMERA}, 1);
                    }
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }

            if (somePermissionsForeverDenied) {
                if (!isOpenPermissionDialog) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Permissions Required")
                            .setMessage("please allow permission for camera.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // dialog.dismiss();
                                    dialog.dismiss();
                                    isOpenPermissionDialog = false;
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isOpenPermissionDialog = false;
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                    isOpenPermissionDialog = true;
                }
            }
        } else {
            switch (requestCode) {
                case 1:
                    //act according to the request code used while requesting the permission(s).
                    myCameraSelection();
                    break;
                default:
                    break;
            }
        }
    }


    public int getStartSlide() {
        return application.getStartBgSlide();
    }

    public void setStartSlide(int data) {
        application.setStartBgSlide(data);
    }

    public int getEndSlide() {
        return application.getEndBgSlide();
    }

    public void setEndSlide(int data) {
        application.setEndBgSlide(data);
    }

    @Override
    public void onBackPressed() {

        Share.end_slide_selected_or_not = false;
        finish();
    }
}
