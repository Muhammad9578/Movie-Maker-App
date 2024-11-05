package com.creativetechnologies.slideshows.videos.songs.videomaker.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Utils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.AlbumImagesActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.CropActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.PhotoPickupActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.crop.FontUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.crop.ProgressDialogFragment;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainFragment extends Fragment {
    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private boolean isInForeGround;
    private ImageView iv_close;
    private CropImageView mCropView;
    private LinearLayout mRootLayout;
    public Button buttonFitImage, button1_1, button3_4, button4_3, button9_16, button16_9, buttonFree, buttonCustom, buttonCircle, buttonShowCircleButCropAsSquare;

    private List<String> listPermissionsNeeded = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 23;
    private MyApplication application = MyApplication.getInstance();

    // Note: only the system can call this constructor by reflection.
    public MainFragment() {
    }

    public static MainFragment getInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, null, false);

//        buttonDone = view.findViewById(R.id.buttonDone);
        buttonFitImage = view.findViewById(R.id.buttonFitImage);
        button1_1 = view.findViewById(R.id.button1_1);
        button3_4 = view.findViewById(R.id.button3_4);
        button4_3 = view.findViewById(R.id.button4_3);
        button9_16 = view.findViewById(R.id.button9_16);
        button16_9 = view.findViewById(R.id.button16_9);
        buttonFree = view.findViewById(R.id.buttonFree);
//        buttonRotateLeft = view.findViewById(R.id.buttonRotateLeft);
//        buttonRotateRight = view.findViewById(R.id.buttonRotateRight);
        buttonCustom = view.findViewById(R.id.buttonCustom);
        buttonCircle = view.findViewById(R.id.buttonCircle);
        buttonShowCircleButCropAsSquare = view.findViewById(R.id.buttonShowCircleButCropAsSquare);
        iv_close = view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // bind Views
        bindViews(view);
        // apply custom font
        FontUtils.setFont(mRootLayout);
//        mCropView.setDebug(true);
        // set bitmap to CropImageView

        if (mCropView.getImageBitmap() == null) {

            Glide.with(getContext())
                    .load(Share.BG_GALLERY)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(300, 300)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mCropView.setImageBitmap(resource);
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            showProgress();
            mCropView.startLoad(result.getData(), mLoadCallback);
        } else if (requestCode == REQUEST_SAF_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            showProgress();
            mCropView.startLoad(Utils.ensureUriPermission(getContext(), result), mLoadCallback);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        MainFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }

    private void bindViews(View view) {
        mCropView = (CropImageView) view.findViewById(R.id.cropImageView);
        view.findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        view.findViewById(R.id.button1_1).setOnClickListener(btnListener);
        view.findViewById(R.id.button3_4).setOnClickListener(btnListener);
        view.findViewById(R.id.button4_3).setOnClickListener(btnListener);
        view.findViewById(R.id.button9_16).setOnClickListener(btnListener);
        view.findViewById(R.id.button16_9).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonShowCircleButCropAsSquare).setOnClickListener(btnListener);
        mRootLayout = (LinearLayout) view.findViewById(R.id.layout_root);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE);
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void cropImage() {
        showProgress();
        mCropView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForPick(PermissionRequest request) {
        showRationaleDialog(R.string.permission_pick_rationale, request);
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForCrop(PermissionRequest request) {
        showRationaleDialog(R.string.permission_crop_rationale, request);
    }

    public void showProgress() {
        ProgressDialogFragment f = ProgressDialogFragment.getInstance();
        getFragmentManager()
                .beginTransaction()
                .add(f, PROGRESS_DIALOG)
                .commitAllowingStateLoss();
    }

    public void dismissProgress() {
        if (!isAdded()) return;
        FragmentManager manager = getFragmentManager();
        if (manager == null) return;
        ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }

    public Uri createSaveUri() {
        return Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    // Handle button event /////////////////////////////////////////////////////////////////////////

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.buttonDone:
                    //MainFragmentPermissionsDispatcher.cropImageWithCheck(MainFragment.this);
                    break;
                case R.id.buttonFitImage:
                    changecolor();
                    buttonFitImage.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                    break;
                case R.id.button1_1:
                    changecolor();
                    button1_1.findViewById(R.id.button1_1).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                    break;
                case R.id.button3_4:
                    changecolor();
                    button3_4.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    break;
                case R.id.button4_3:
                    changecolor();
                    button4_3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    break;
                case R.id.button9_16:
                    changecolor();
                    button9_16.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    break;
                case R.id.button16_9:
                    changecolor();
                    button16_9.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    break;
                case R.id.buttonCustom:
                    changecolor();
                    buttonCustom.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCustomRatio(7, 5);
                    break;
                case R.id.buttonFree:
                    changecolor();
                    buttonFree.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.FREE);
                    break;
                case R.id.buttonCircle:
                    changecolor();
                    buttonCircle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                    break;
                case R.id.buttonShowCircleButCropAsSquare:
                    changecolor();
                    buttonShowCircleButCropAsSquare.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
                    break;
                case R.id.buttonRotateLeft:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    break;
                case R.id.buttonRotateRight:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    break;
            }
        }
    };

    private void changecolor() {

        buttonFitImage.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        button1_1.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        button3_4.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        button4_3.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        button9_16.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        button16_9.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        buttonFree.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        buttonCustom.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        buttonCircle.setBackgroundColor(getResources().getColor(R.color.windowBackground));
        buttonShowCircleButCropAsSquare.setBackgroundColor(getResources().getColor(R.color.windowBackground));
    }

    private boolean checkAndRequestPermissions() {
        listPermissionsNeeded.clear();
        int storage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        Log.e("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        if (!listPermissionsNeeded.isEmpty()) {

            return false;
        }
        return true;
    }

    // Callbacks ///////////////////////////////////////////////////////////////////////////////////

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
            dismissProgress();
        }

        @Override
        public void onError() {
            dismissProgress();
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(final Bitmap cropped) {

            if (Share.FROM_CUSTOM_SLIDE) {


                if (Share.isNeedToAdShow(getActivity())) {
                    redirectActivityCustomSlide(cropped);
                } else {
                    redirectActivityCustomSlide(cropped);
                }


            } else {
                saveFaceInternalStorage(cropped);
            }
        }

        @Override
        public void onError() {
        }
    };

    private void redirectActivityCustomSlide(Bitmap cropped) {
        ((CropActivity) getActivity()).startResultActivity(cropped);

        if (PhotoPickupActivity.activity != null) {
            PhotoPickupActivity.activity.finish();
        }
        if (AlbumImagesActivity.activity != null) {
            AlbumImagesActivity.activity.finish();
        }
        getActivity().finish();
    }

    private void redirectActivity(Bitmap bitmapImage) {
        ((CropActivity) getActivity()).startResultActivity(bitmapImage);
        getActivity().finish();
    }

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            dismissProgress();
        }

        @Override
        public void onError() {
            dismissProgress();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (!MyApplication.getInstance().isLoaded())
            MyApplication.getInstance().LoadAds();

        if (mCropView.getImageBitmap() == null) {

            if (Share.BG_GALLERY != null) {
                Glide.with(getContext())
                        .load(Share.BG_GALLERY)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .override(300, 300)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mCropView.setImageBitmap(resource);
                            }
                        });
            } else {
                if (Share.RestartAppStorage(getActivity())) {

                }
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mCropView.setImageDrawable(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        isInForeGround = false;
    }

    public String saveFaceInternalStorage(final Bitmap bitmapImage) {
        File directory = new File(FileUtils.TEMP_DIRECTORY_IMAGES_CROP.getAbsolutePath());
        if (!directory.exists())
            directory.mkdirs();

        if (bitmapImage != null) {
            File mypath = new File(directory, "crop_image" + Share.temp_selected_crop_image_position + ".png");
            if (mypath.exists()) {
                mypath.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                MediaScannerConnection.scanFile(getActivity(), new String[]{mypath.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                    }

                    @Override
                    public void onScanCompleted(String path, final Uri uri) {

                    }
                });

                if (Share.isNeedToAdShow(getActivity())) {
                        redirectActivity(bitmapImage);
                } else {
                    redirectActivity(bitmapImage);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "Not Saved Image------------------------------------------------------->");
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Share.resume_flag = false;
        if (mCropView != null) {
            mCropView.setImageBitmap(null);
            mCropView.setImageDrawable(null);
        }
    }
}