package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.creativetechnologies.slideshows.videos.songs.videomaker.CropActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/18/2016.
 */
public class PhoneAlbumImagesAdapter extends RecyclerView.Adapter<PhoneAlbumImagesAdapter.ViewHolder> {
    private Activity activity;
    private List<String> al_album = new ArrayList<>();
    private DisplayImageOptions options;

    public PhoneAlbumImagesAdapter(Activity activity, ArrayList<String> al_album) {
        this.activity = activity;
        this.al_album = al_album;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.progress_animation)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_album_name;
        ImageView iv_album_image;
        ProgressBar progressBar_phone;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_album_name = (TextView) itemView.findViewById(R.id.tv_album_name);
            iv_album_image = (ImageView) itemView.findViewById(R.id.iv_album_image);
//            progressBar_phone = (ProgressBar)itemView.findViewById(R.id.progressBar_phone);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.tv_album_name.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage("file:///" + al_album.get(position), holder.iv_album_image, options);

        holder.iv_album_image.getLayoutParams().height = SharedPrefs.getInt(activity, SharedPrefs.screen_hight) / 3;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CropActivity.class);
                Log.e("TAG", "al_album.get(position) :" + al_album.get(position));
                Share.BG_GALLERY = Uri.parse("file:///" + al_album.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }
}
