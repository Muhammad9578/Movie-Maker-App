package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.creativetechnologies.slideshows.videos.songs.videomaker.EffectActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

import java.io.File;

/**
 * Created by Vasundhara on 16-May-18.
 */

public class TempSelectedImageAdapter extends RecyclerView.Adapter<TempSelectedImageAdapter.MyView> {
    EffectActivity activity;
    private RequestManager glide;
    private MyApplication application;
    private File[] contents;
    int lastPos = 0;

    public TempSelectedImageAdapter(EffectActivity activity, File[] contents) {
        this.contents = contents;
        this.activity = activity;
        this.glide = Glide.with((FragmentActivity) activity);
        this.application = MyApplication.getInstance();
    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView ivImages;
        CheckBox cbSelect;

        public MyView(View itemView) {
            super(itemView);
            ivImages = (ImageView) itemView.findViewById(R.id.ivImages);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cbSelect);
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.selected_image_row, parent, false);
        return new MyView(view);
    }

    public File getItem(int pos) {
        return contents[pos];
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView holder, final int position) {
        final File file = getItem(position);

        holder.ivImages.setImageBitmap(null);
        holder.ivImages.invalidate();

        Glide.with(application)
                .load(contents[position].getAbsoluteFile())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImages);

        holder.cbSelect.setChecked(file == activity.getEffect());
        holder.ivImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file != activity.getEffect()) {
                    activity.setEffect(file);
                    if (file != null) {
                        notifyItemChanged(lastPos);
                        notifyItemChanged(position);
                        lastPos = position;
                        Share.selected_image_pos = position;
                        Share.selected_image_path = contents[position].getAbsolutePath();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.length;
    }
}
