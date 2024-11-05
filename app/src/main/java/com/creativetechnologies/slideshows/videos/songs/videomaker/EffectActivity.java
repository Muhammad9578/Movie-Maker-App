package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.EffectAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;

public class EffectActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvEffect;
    private MyApplication application = MyApplication.getInstance();
    private ArrayList<ImageData> temp_list = new ArrayList<>();
    private EffectAdapter effectAdapter;
    private ImageView ivNext, ivBack;

    private GPUImage gpu_image_filter;
    private FilterList filters;

    private Gallery gallery;
    private ImageAdapter imageAdapter;
    BitmapFactory.Options options = new BitmapFactory.Options();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Share.RestartAppForOnlyStorage(EffectActivity.this)) {
            setContentView(R.layout.activity_effect);
            initView();
        }
    }

    private void initView() {

        gpu_image_filter = new GPUImage(getApplicationContext());
        filters = new FilterList();

        filters.addFilter("Contrast", FilterType.CONTRAST);
        filters.addFilter("Posterize", FilterType.POSTERIZE);
        filters.addFilter("Vignette", FilterType.VIGNETTE);
        filters.addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN);
        filters.addFilter("Emboss", FilterType.EMBOSS);
        filters.addFilter("Sharpness", FilterType.SHARPEN);
        filters.addFilter("Sepia", FilterType.SEPIA);
        filters.addFilter("Grayscale", FilterType.GRAYSCALE);
        filters.addFilter("Sketch", FilterType.SKETCH);
        filters.addFilter("Invert", FilterType.INVERT);
        filters.addFilter("Hue", FilterType.HUE);

        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        ivNext.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        CopySelectedImages();
    }

    private void CopySelectedImages() {

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        temp_list.clear();
        temp_list.addAll(application.getSelectedImages());

        if (!FileUtils.TEMP_DIRECTORY_IMAGES.exists()) {
            FileUtils.TEMP_DIRECTORY_IMAGES.mkdirs();
            FileUtils.TEMP_DIRECTORY_IMAGES1.mkdirs();
        }

        System.gc();

        gallery = (Gallery) findViewById(R.id.gallery1);
        imageAdapter = new ImageAdapter(this);
        gallery.setAdapter(imageAdapter);

        rvEffect = (RecyclerView) findViewById(R.id.rvEffect);
        rvEffect.setLayoutManager(new GridLayoutManager(this, 3));

        effectAdapter = new EffectAdapter(EffectActivity.this);
        rvEffect.setAdapter(effectAdapter);

        effectAdapter.setEventListener(new EffectAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {
                new getEffectedBitmap(position).execute();
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });

        System.gc();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;

        public ImageAdapter(Context c) {
            context = c;
            // sets a grey background; wraps around the images
            TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
        }

        // returns the number of images
        public int getCount() {
            return application.getSelectedImages().size();
        }

        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }

        // returns the ID of an item
        public long getItemId(int position) {
            Share.selected_image_pos = (int) getItem(position);
            return position;
        }

        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView = new ImageView(context);

            Glide.with(application)
                    .load(temp_list.get(position).getImagePath())
                    .asBitmap()
                    .override(300, 300)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                        }
                    });

//            imageView.setImageResource(imageIDs[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams((int) getApplication().getResources().getDimension(R.dimen._200sdp), (int) getApplication().getResources().getDimension(R.dimen._200sdp)));
            imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }

    public class getEffectedBitmap extends AsyncTask<Void, Void, Void> {

        ProgressDialog apply_effect_progress;
        int position;

        public getEffectedBitmap(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            apply_effect_progress = Share.createProgressDialog(EffectActivity.this);
            apply_effect_progress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Share.selected_image_pos < application.getTempOrgSelectedImages().size()) {

//              String temp_file = loadCameraBitmap(new File(application.getTempOrgSelectedImages().get(Share.selected_image_pos).getTemp_org_imagePath()));

                Bitmap bitmap1 = ScaleBitmapFile(new File(application.getTempOrgSelectedImages().get(Share.selected_image_pos).getTemp_org_imagePath()));

                gpu_image_filter.setFilter(createFilterForType(getApplicationContext(), filters.filters.get(position)));
                gpu_image_filter.setImage(bitmap1);
                Bitmap bitmap = gpu_image_filter.getBitmapWithFilterApplied();

                Bitmap rotatedBitmap = null;
                try {
                    ExifInterface ei = new ExifInterface(application.getTempOrgSelectedImages().get(Share.selected_image_pos).getTemp_org_imagePath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (rotatedBitmap != null) {
                    File imageFile = new File(FileUtils.TEMP_DIRECTORY_IMAGES, "image_" + Share.selected_image_pos + ".jpg");
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                    OutputStream fos = null;
                    try {
                        fos = new FileOutputStream(imageFile);
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();

                        MediaScannerConnection.scanFile(EffectActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {

                            }

                            @Override
                            public void onScanCompleted(String path, final Uri uri) {

                            }
                        });

                        temp_list.get(Share.selected_image_pos).setImagePath(imageFile.getAbsolutePath());

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
                    rotatedBitmap.recycle();
                    if (!bitmap1.isRecycled()) {
                        bitmap1.recycle();
                    }

                } else {
                    Log.e("TAG", "Not Saved Image------------------------------------------------------->");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (apply_effect_progress != null && apply_effect_progress.isShowing()) {
                apply_effect_progress.dismiss();
            }

            imageAdapter.notifyDataSetChanged();
//            tempSelectedImageAdapter.notifyItemChanged(Share.selected_image_pos);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public Bitmap ScaleBitmapFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
//            file.createNewFile();
//            FileOutputStream outputStream = new FileOutputStream(file);

//            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return selectedBitmap;
        } catch (Exception e) {
            return null;
        }
    }


    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight,
                                           boolean isNecessaryToKeepOrig) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (!isNecessaryToKeepOrig) {
            bm.recycle();
        }
        return resizedBitmap;
    }

    private enum FilterType {
        CONTRAST, POSTERIZE, VIGNETTE, LEVELS_FILTER_MIN, EMBOSS, SHARPEN, SEPIA, GRAYSCALE, SKETCH, INVERT, HUE
    }

    private static class FilterList {
        public List<String> names = new LinkedList<String>();
        public List<FilterType> filters = new LinkedList<FilterType>();

        public void addFilter(final String name, final FilterType filter) {
            names.add(name);
            filters.add(filter);
        }
    }

    private static GPUImageFilter createFilterForType(final Context context,
                                                      final FilterType type) {
        switch (type) {
            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case INVERT:
                return new GPUImageColorInvertFilter();
//            case PIXELATION:
//                return new GPUImagePixelationFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
//            case GAMMA:
//                return new GPUImageGammaFilter(2.0f);
            case SEPIA:
                return new GPUImageSepiaFilter();
            case GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case SHARPEN:
                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(2.0f);
                return sharpness;
            case EMBOSS:
                return new GPUImageEmbossFilter();
//            case SOBEL_EDGE_DETECTION:
//                return new GPUImageSobelEdgeDetection();
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
//            case FILTER_GROUP:
//                List<GPUImageFilter> filters = new LinkedList<GPUImageFilter>();
//                filters.add(new GPUImageContrastFilter());
//                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
//                filters.add(new GPUImageGrayscaleFilter());
//                return new GPUImageFilterGroup(filters);
//            case SATURATION:
//                return new GPUImageSaturationFilter(1.0f);
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
//            case KUWAHARA:
//                return new GPUImageKuwaharaFilter();
            case SKETCH:
                return new GPUImageSketchFilter();
//            case TOON:
//                return new GPUImageToonFilter();
//            case HAZE:
//                return new GPUImageHazeFilter();
            case LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

    public File getEffect() {
        return application.getEffect();
    }

    public void setEffect(File data) {
        application.setEffect(data);
    }

    private class FinalArrayList extends AsyncTask<Void, Void, Void> {
        ProgressDialog apply_effect_progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            apply_effect_progress = Share.createProgressDialog(EffectActivity.this);
            apply_effect_progress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {


            for (int i = 0; i < temp_list.size(); i++) {
                application.getSelectedImages().get(i).setImagePath(temp_list.get(i).getImagePath());
                application.getOrgSelectedImages().get(i).setTemp_imagePath(temp_list.get(i).getImagePath());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (apply_effect_progress != null && apply_effect_progress.isShowing()) {
                apply_effect_progress.dismiss();
            }

            FinalPreviewActivity.iv_step4.setVisibility(View.VISIBLE);
            Share.Effect_Flag = true;

            // for save video //
            FinalPreviewActivity.playThreeDEffect = false;
            FinalPreviewActivity.playTwoDEffect = false;
            FinalPreviewActivity.playMoreEffect = false;

            // apply bg (without play video) apply 3d effect
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
        }

    }

    @Override
    public void onClick(View v) {

        if (v == ivNext) {
            new FinalArrayList().execute();
        } else if (v == ivBack) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ivNext != null) { // restart app => permission
            ivNext.setOnClickListener(null);
            ivBack.setOnClickListener(null);
            rvEffect.removeAllViews();
            rvEffect.getRecycledViewPool().clear();
            gallery = null;
            imageAdapter = null;
            effectAdapter = null;
            gpu_image_filter = null;
            filters = null;
        }
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();

    }
}
