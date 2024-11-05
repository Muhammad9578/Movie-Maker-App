package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.creativetechnologies.slideshows.videos.songs.videomaker.AlbumImagesActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.PhotoPickupActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.PhoneAlbum;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by admin on 11/18/2016.
 */
public class PhoneAlbumAdapter extends RecyclerView.Adapter<PhoneAlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<String> al_image = new ArrayList<>();
    private List<PhoneAlbum> al_album = new ArrayList<>();
    private DisplayImageOptions options;

    public PhoneAlbumAdapter(Context context, Vector<PhoneAlbum> al_album) {
        this.context = context;
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

        ImageLoader.getInstance().displayImage("file:///" + al_album.get(position).getCoverUri(), holder.iv_album_image, options);
//        Picasso.with(context)
//                .load( "file:" + al_album.get(position).getCoverUri() )
//                .placeholder(R.drawable.progress_animation)
//                .into(holder.iv_album_image);

        holder.iv_album_image.getLayoutParams().height = SharedPrefs.getInt(context, SharedPrefs.screen_hight) / 3;
        holder.tv_album_name.setText(al_album.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al_image.clear();
                Log.e("TAG", "Images====>" + al_album.get(position).getAlbumPhotos().size());
                for (int i = 0; i < al_album.get(position).getAlbumPhotos().size(); i++) {
                    al_image.add(al_album.get(position).getAlbumPhotos().get(i).getPhotoUri());
                }
                PhotoPickupActivity.iv_more_app.setVisibility(View.GONE);
                PhotoPickupActivity.iv_blast.setVisibility(View.GONE);
                Intent intent = new Intent(context, AlbumImagesActivity.class);
                intent.putStringArrayListExtra("image_list", al_image);
                intent.putExtra(GlobalData.KEYNAME.ALBUM_NAME, al_album.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }
}
