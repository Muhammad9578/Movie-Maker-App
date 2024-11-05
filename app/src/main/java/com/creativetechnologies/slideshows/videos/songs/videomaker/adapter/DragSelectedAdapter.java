package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview.DragAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview.DragHolder;
import com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview.DragRecyclerView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

/**
 * Created by Vasundhara on 29-Jun-18.
 */

public class DragSelectedAdapter extends DragAdapter {

    private MyApplication application = MyApplication.getInstance();
    private Context context;

    public DragSelectedAdapter(Context context) {
        super(context);

        this.context = context;
    }

    private final class Holder extends DragHolder {
        ImageView ivImage, ivDelete, ivEdit;
        LinearLayout llBottomView;

        Holder(View view, int viewType) {
            super(view);

            ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
            ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            llBottomView = (LinearLayout) view.findViewById(R.id.llBottomView);
        }
    }

    @NonNull
    @Override
    public DragRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.drag_selected_image_row, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(DragRecyclerView.ViewHolder hol, final int position) {
        super.onBindViewHolder(hol, position);

        Holder holder = (Holder) hol;

        holder.ivImage.getLayoutParams().width = (GlobalData.screenWidth / 3);
        holder.ivImage.getLayoutParams().height = (GlobalData.screenWidth / 3);
        holder.llBottomView.getLayoutParams().width = (GlobalData.screenWidth / 3);

        Log.e("TAG", "path ===========> " + application.getSelectedImages().get(position).getImagePath());
        Glide.with(application).load(application.getSelectedImages().get(position).getImagePath()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.ivImage);
    }
}

