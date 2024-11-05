package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.PhotoPickupImageActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vasundhara on 10-May-18.
 */

public class SelectedImageHorizontalAdapter extends RecyclerView.Adapter<SelectedImageHorizontalAdapter.MyView> {

    private ArrayList<ImageData> list = new ArrayList<>();
    private PhotoPickupImageActivity context;
    private RequestManager glide;
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    File[] contents;

    public SelectedImageHorizontalAdapter(PhotoPickupImageActivity context) {
        this.context = context;
        this.glide = Glide.with(this.context);

        FileUtils.TEMP_DIRECTORY_IMAGES.mkdirs();
        FileUtils.TEMP_DIRECTORY_IMAGES1.mkdirs();

    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView ivImage, ivDelete;

        public MyView(View itemView) {
            super(itemView);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }

        public void onItemClick(View view, Object item) {
            if (clickListner != null) {
                clickListner.onItemClick(view, item);
            }
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_gallery_view, parent, false);

        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {
        final ImageData data = getItem(position);
        this.glide.load(data.temp_imagePath).into(holder.ivImage);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context.isFromPreview) {
                    application.min_pos = Math.min(application.min_pos, Math.max(0, position - 1));
                }

                if (getItem(position).getSelected_image_count() == application.getSelectedImages().get(position).getSelected_image_count()) {
                    if (application.getSelectedImages().get(position).getImagePath().contains(".temp_images")) {
                        Log.e("TAG", "delete image :" + application.getSelectedImages().get(position).getImagePath());
                        FileUtils.deleteFile(application.getSelectedImages().get(position).getImagePath());
                    } else if (application.getSelectedImages().get(position).getImagePath().contains(".crop_images")) {
                        Log.e("TAG", "delete image :" + application.getSelectedImages().get(position).getImagePath());
                        FileUtils.deleteFile(application.getSelectedImages().get(position).getImagePath());
                    }
                }

                application.removeSelectedImage(position);

                if (clickListner != null) {
                    clickListner.onItemClick(v, data);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    @Override
    public int getItemCount() {
        return application.getOrgSelectedImages().size();
    }

    public ImageData getItem(int pos) {
        ArrayList<ImageData> list = application.getOrgSelectedImages();
        if (list.size() <= pos) {
            return new ImageData();
        }
        return (ImageData) list.get(pos);
    }
}
