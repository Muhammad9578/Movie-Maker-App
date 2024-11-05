package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.AlbumAdapterById;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.ImageByAlbumAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.OnItemClickListner;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.SelectedImageAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.SelectedImageHorizontalAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.DisplayMetricsHandler;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.view.EmptyRecyclerView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.view.ExpandIconView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.view.VerticalSlidingPanel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class PhotoPickupImageActivity extends AppCompatActivity implements VerticalSlidingPanel.PanelSlideListener, OnClickListener {
    public static final String EXTRA_FROM_PREVIEW = "extra_from_preview";
    private AlbumAdapterById albumAdapter;
    private ImageByAlbumAdapter albumImagesAdapter;
    private MyApplication application;
    private Button btnClear;
    private ImageView ivNext;
    private TextView tvCountImages;
    private ExpandIconView expandIcon;
    public boolean isFromPreview = false;
    boolean isPause = false;
    // private VerticalSlidingPanel panel;
    private View parent;
    private LinearLayout llDelete, llEdit;
    private RecyclerView rvAlbum;
    private RecyclerView rvAlbumImages, rvSelected;
    private EmptyRecyclerView rvSelectedImage;
    private SelectedImageAdapter selectedImageAdapter;
    private SelectedImageHorizontalAdapter imageHorizontalAdapter;
    private TextView tvImageCount;
    private InterstitialAd interstitialAd;
//    private FirebaseAnalytics mFirebaseAnalytics;

    public static PhotoPickupImageActivity activity;

    public static ImageView iv_more_app, iv_blast;
    public static Boolean is_closed = true;

    class C05822 implements OnClickListener {
        C05822() {
        }

        public void onClick(View v) {
            PhotoPickupImageActivity.this.clearData();
        }
    }


    class C10173 implements OnItemClickListner<Object> {
        C10173() {
        }

        public void onItemClick(View view, Object item) {
            PhotoPickupImageActivity.this.albumImagesAdapter.notifyDataSetChanged();
        }
    }

    class C10184 implements OnItemClickListner<Object> {
        C10184() {
        }

        public void onItemClick(View view, Object item) {
            PhotoPickupImageActivity.this.tvImageCount.setText(String.valueOf(PhotoPickupImageActivity.this.application.getOrgSelectedImages().size()));
            PhotoPickupImageActivity.this.selectedImageAdapter.notifyDataSetChanged();
            tvCountImages.setText("" + application.getOrgSelectedImages().size());
            imageHorizontalAdapter.notifyDataSetChanged();
            rvSelected.scrollToPosition(application.getOrgSelectedImages().size() - 1);

            if (application.getOrgSelectedImages().size() != 0) {
                llDelete.setEnabled(true);
                llDelete.setAlpha(1.0f);
            } else {
                llDelete.setEnabled(false);
                llDelete.setAlpha(0.5f);
            }
        }
    }

    private class DeleteSelectedImage implements OnItemClickListner<Object> {
        @Override
        public void onItemClick(View view, Object o) {
            albumImagesAdapter.notifyDataSetChanged();
            selectedImageAdapter.notifyDataSetChanged();
            tvCountImages.setText("" + application.getOrgSelectedImages().size());

            if (application.getOrgSelectedImages().size() != 0) {
                llDelete.setEnabled(true);
                llDelete.setAlpha(1.0f);
            } else {
                llDelete.setEnabled(false);
                llDelete.setAlpha(0.5f);
                Share.end_slide_selected_or_not = false;
            }
        }
    }

    class C10195 implements OnItemClickListner<Object> {
        C10195() {
        }

        public void onItemClick(View view, Object item) {
            PhotoPickupImageActivity.this.tvImageCount.setText(String.valueOf(PhotoPickupImageActivity.this.application.getOrgSelectedImages().size()));
            PhotoPickupImageActivity.this.albumImagesAdapter.notifyDataSetChanged();
            tvCountImages.setText("" + application.getOrgSelectedImages().size());

            if (application.getOrgSelectedImages().size() != 0) {
                llDelete.setEnabled(true);
                llDelete.setAlpha(1.0f);
            } else {
                llDelete.setEnabled(false);
                llDelete.setAlpha(0.5f);
            }
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Share.RestartAppForOnlyStorage(PhotoPickupImageActivity.this)) {
            setContentView(R.layout.activity_photo_pickup_image);

            //   mFirebaseAnalytics = FirebaseAnalytics.getInstance(PhotoPickupImageActivity.this);
            activity = this;
            this.application = MyApplication.getInstance();
            this.isFromPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);

            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            AdRequest request = new AdRequest.Builder().build();
            interstitialAd.loadAd(request);

            bindView();
            init();
            addListner();
        }
    }

    private void init() {
        //  Utils.setFont((Activity) this, mTitle);
        albumAdapter = new AlbumAdapterById(this);
        albumImagesAdapter = new ImageByAlbumAdapter(this);
        selectedImageAdapter = new SelectedImageAdapter(this);
        rvAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        rvAlbum.setItemAnimator(new DefaultItemAnimator());
        rvAlbum.setAdapter(albumAdapter);
        rvAlbumImages.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rvAlbumImages.setItemAnimator(new DefaultItemAnimator());
        rvAlbumImages.setAdapter(albumImagesAdapter);
        rvSelectedImage.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
        rvSelectedImage.setAdapter(selectedImageAdapter);
        rvSelectedImage.setEmptyView(findViewById(R.id.list_empty));
        rvSelected.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageHorizontalAdapter = new SelectedImageHorizontalAdapter(PhotoPickupImageActivity.this);
        rvSelected.setAdapter(imageHorizontalAdapter);
        tvImageCount.setText(String.valueOf(application.getOrgSelectedImages().size()));
        tvCountImages.setText(String.valueOf(application.getOrgSelectedImages().size()));
    }

    private void bindView() {
        tvImageCount = (TextView) findViewById(R.id.tvImageCount);
        expandIcon = (ExpandIconView) findViewById(R.id.settings_drag_arrow);
        rvAlbum = (RecyclerView) findViewById(R.id.rvAlbum);
        rvAlbumImages = (RecyclerView) findViewById(R.id.rvImageAlbum);
        rvSelectedImage = (EmptyRecyclerView) findViewById(R.id.rvSelectedImagesList);
        rvSelected = (RecyclerView) findViewById(R.id.rvSelected);
        parent = findViewById(R.id.default_home_screen_panel);
        btnClear = (Button) findViewById(R.id.btnClear);
        tvCountImages = (TextView) findViewById(R.id.tvCountImages);

        llDelete = (LinearLayout) findViewById(R.id.llDelete);
        llEdit = (LinearLayout) findViewById(R.id.llEdit);

        ivNext = (ImageView) findViewById(R.id.ivNext);

        iv_more_app = (ImageView) findViewById(R.id.iv_more_app);
        iv_blast = (ImageView) findViewById(R.id.iv_blast);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
        }

        ivNext.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llEdit.setOnClickListener(this);

        iv_more_app.setOnClickListener(this);
    }

    private void addListner() {
        btnClear.setOnClickListener(new C05822());
        albumAdapter.setOnItemClickListner(new C10173());
        albumImagesAdapter.setOnItemClickListner(new C10184());
        selectedImageAdapter.setOnItemClickListner(new C10195());
        imageHorizontalAdapter.setOnItemClickListner(new DeleteSelectedImage());
    }

    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {
            if (isPause) {
                isPause = false;
                tvCountImages.setText(String.valueOf(application.getOrgSelectedImages().size()));
                selectedImageAdapter.notifyDataSetChanged();
                albumImagesAdapter.notifyDataSetChanged();
                imageHorizontalAdapter.notifyDataSetChanged();
            }

            // disable delete icon when no image selected
            if (application.getOrgSelectedImages().size() != 0) {
                llDelete.setEnabled(true);
                llDelete.setAlpha(1.0f);
            } else {
                llDelete.setEnabled(false);
                llDelete.setAlpha(0.5f);
            }

            if (Share.isNeedToAdShow(getApplicationContext()))
                if (is_closed) {
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Share.resume_flag = false;
    }

    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    private void loadImageEdit() {
        Next();
    }

    private void redirectActivity() {

        if (Share.isPreviewActivity) {
            Intent intent = new Intent(PhotoPickupImageActivity.this, SelectedPhotoArrangeActivity.class);
            startActivity(intent);
        } else {
            Share.FirstTimePreviewActivity = true;
            Intent intent = new Intent(PhotoPickupImageActivity.this, SelectedPhotoArrangeActivity.class);
            startActivity(intent);
        }
    }

    public void onPanelSlide(View panel, float slideOffset) {
        if (expandIcon != null) {
            expandIcon.setFraction(slideOffset, false);
        }
        if (slideOffset >= 0.005f) {
            if (parent != null && parent.getVisibility() != View.VISIBLE) {
                parent.setVisibility(View.VISIBLE);
            }
        } else if (parent != null && parent.getVisibility() == View.VISIBLE) {
            parent.setVisibility(View.GONE);
        }
    }

    public void onPanelCollapsed(View panel) {
        if (parent != null) {
            parent.setVisibility(View.VISIBLE);
        }
        selectedImageAdapter.isExpanded = false;
        selectedImageAdapter.notifyDataSetChanged();
    }

    public void onPanelExpanded(View panel) {
        if (parent != null) {
            parent.setVisibility(View.GONE);
        }
        selectedImageAdapter.isExpanded = true;
        selectedImageAdapter.notifyDataSetChanged();
    }

    public void onPanelAnchored(View panel) {
    }

    public void onPanelShown(View panel) {
    }

    public void onBackPressed() {

        final Dialog dialog = new Dialog(PhotoPickupImageActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_editing);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button rate_app = (Button) dialog.findViewById(R.id.rate_app);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);

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

                if (FinalPreviewActivity.activity != null) {
                    FinalPreviewActivity.activity.finish();
                }
                Share.isPreviewActivity = false;
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

    private void clearData() {
        for (int i = application.getSelectedImages().size() - 1; i >= 0; i--) {
            application.removeSelectedImage(i);
        }
        tvCountImages.setText("0");
        llDelete.setEnabled(false);
        llDelete.setAlpha(0.5f);
        selectedImageAdapter.notifyDataSetChanged();
        albumImagesAdapter.notifyDataSetChanged();
        imageHorizontalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        if (v == ivNext) {

            if (application.getSelectedImages().size() > 2) {
                if (!isFromPreview) {
                    loadImageEdit();
                    return;
                }
                setResult(-1);
                finish();
            }
            Toast.makeText(this, "Select atleast 3 image to create video", Toast.LENGTH_SHORT).show();

        } else if (v == iv_more_app) {
            is_closed = false;
            iv_more_app.setVisibility(View.GONE);
            iv_blast.setVisibility(View.VISIBLE);
            ((AnimationDrawable) iv_blast.getBackground()).start();
            iv_blast.setVisibility(View.GONE);
            iv_more_app.setVisibility(View.GONE);

        } else if (v == llDelete) {

            setDeleteAlertDialog();

        } else if (v == llEdit) {

            if (application.getSelectedImages().size() > 2) {
                if (!isFromPreview) {
                    loadImageEdit();
                    return;
                }
                setResult(-1);
                finish();
            }
            Toast.makeText(this, "Select atleast 3 image to create video", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDeleteAlertDialog() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PhotoPickupImageActivity.this);
        } else {
            builder = new AlertDialog.Builder(PhotoPickupImageActivity.this);
        }
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete all photos?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Share.end_slide_selected_or_not = false;
                        clearData();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.appicon)
                .show();
    }

    public void Next() {
        if (interstitialAd.isLoaded()) {
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
        } else {
            redirectActivity();
        }

    }
}
