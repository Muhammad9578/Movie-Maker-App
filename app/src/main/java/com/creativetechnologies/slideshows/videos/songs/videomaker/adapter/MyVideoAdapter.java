package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyVideoActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.io.File;

/**
 * Created by admin on 12/19/2016.
 */
public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    final Context context;
    private boolean blnTouch = false;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public MyVideoAdapter(final Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        mOnItemClickListener = onItemClickListener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumb_image, ivPlayButton;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_thumb_image = (ImageView) itemView.findViewById(R.id.iv_thumb_image);
            ivPlayButton = (ImageView) itemView.findViewById(R.id.ivPlayButton);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        holder.iv_thumb_image.getLayoutParams().width = (GlobalData.screenWidth / 3);
        holder.iv_thumb_image.getLayoutParams().height = (int) (GlobalData.screenHeight / 5.5);

        Glide.with(context)
                .load(MyVideoActivity.al_my_photos.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .listener(new RequestListener<File, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.iv_thumb_image.setVisibility(View.VISIBLE);
                        holder.iv_thumb_image.setImageDrawable(resource);
                        holder.ivPlayButton.setVisibility(View.VISIBLE);

                        return true;
                    }
                })
                .into(holder.iv_thumb_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return MyVideoActivity.al_my_photos.size();
    }
}
