package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;

import java.io.File;
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

/**
 * Created by Vasundhara on 01-Jun-18.
 */

public class EffectAdapter extends RecyclerView.Adapter<EffectAdapter.MyView> {

    private Context context;
    private File[] contents;
    private EventListener mEventListener;
    private RequestManager glide;
    //Image Filters - GPU Image
    private String filter_name[] = {"CONTRAST", "POSTERIZE", "VIGNETTE", "LEVELS_FILTER_MIN", "EMBOSS", "SHARPEN", "SEPIA", "GRAYSCALE", "SKETCH", "INVERT", "HUE"};

    private int pos = 0;

    /*private Integer[] ids = new Integer[]{
            R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01,
            R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01, R.drawable.frame_01,
            R.drawable.frame_01, R.drawable.frame_glass, R.drawable.frame_03, R.drawable.frame_04, R.drawable.frame_05,
            R.drawable.frame_06, R.drawable.frame_07, R.drawable.frame_08, R.drawable.frame_09, R.drawable.frame_10, R.drawable.frame_11,
            R.drawable.frame_13, R.drawable.frame_14, R.drawable.frame_15, R.drawable.frame_16, R.drawable.frame_17};*/

    GPUImage gpu_image_filter, rotated_gpu_image_filter;
    private FilterList filters;

    public EffectAdapter(Context context) {
        this.context = context;
        glide = Glide.with(context);
        gpu_image_filter = new GPUImage(context);
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


    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView ivEffect;

        public MyView(View itemView) {
            super(itemView);

            ivEffect = (ImageView) itemView.findViewById(R.id.ivEffect);
        }
    }

    public interface EventListener {
        void onItemViewClicked(int position);

        void onDeleteMember(int position);
    }

    private void onItemViewClicked(int position) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(position);
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_gpu_effect, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView holder, final int position) {
        holder.setIsRecyclable(false);

        Drawable drawable_overlay = context.getResources().getDrawable(R.drawable.ic_thums);
        Bitmap b = ((BitmapDrawable) drawable_overlay).getBitmap();
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(b);
        gpuImage.setFilter(createFilterForType(context, filters.filters.get(position)));

        holder.ivEffect.setImageBitmap(gpuImage.getBitmapWithFilterApplied());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filter_name.length;
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

    private static GPUImageFilter createFilterForType(final Context context, final FilterType type) {
        switch (type) {
            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case INVERT:
                return new GPUImageColorInvertFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
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
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case SKETCH:
                return new GPUImageSketchFilter();
            case LEVELS_FILTER_MIN:
                GPUImageLevelsFilter levelsFilter = new GPUImageLevelsFilter();
                levelsFilter.setMin(0.0f, 3.0f, 1.0f);
                return levelsFilter;
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

}
