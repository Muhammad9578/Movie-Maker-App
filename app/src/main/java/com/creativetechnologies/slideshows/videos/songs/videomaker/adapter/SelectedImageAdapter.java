package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.PhotoPickupImageActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;

import java.util.ArrayList;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.Holder> {
    final int TYPE_BLANK = 1;
    final int TYPE_IMAGE = 0;
    PhotoPickupImageActivity activity;
    private MyApplication application;
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;
    public boolean isExpanded = false;

    public class Holder extends RecyclerView.ViewHolder {
        private View clickableView;
        private ImageView ivRemove;
        private ImageView ivThumb;
        View parent;

        public Holder(View v) {
            super(v);
            this.parent = v;
            this.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            this.ivRemove = (ImageView) v.findViewById(R.id.ivRemove);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (SelectedImageAdapter.this.clickListner != null) {
                SelectedImageAdapter.this.clickListner.onItemClick(view, item);
            }
        }
    }

    public SelectedImageAdapter(PhotoPickupImageActivity activity) {
        this.activity = activity;
        this.application = MyApplication.getInstance();
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with((FragmentActivity) activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        ArrayList<ImageData> list = this.application.getOrgSelectedImages();
        if (this.isExpanded) {
            return list.size();
        }
        return list.size() + 20;
    }

    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (!this.isExpanded && position >= this.application.getOrgSelectedImages().size()) {
            return 1;
        }
        return 0;
    }

    private boolean hideRemoveBtn() {
        return this.application.getOrgSelectedImages().size() <= 3 && this.activity.isFromPreview;
    }

    public ImageData getItem(int pos) {
        ArrayList<ImageData> list = this.application.getSelectedImages();
        if (list.size() <= pos) {
            return new ImageData();
        }
        return (ImageData) list.get(pos);
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        if (getItemViewType(pos) == 1) {
            holder.parent.setVisibility(View.INVISIBLE);
            return;
        }
        holder.parent.setVisibility(View.VISIBLE);

        final ImageData data = getItem(pos);
        this.glide.load(data.temp_imagePath).into(holder.ivThumb);
        if (hideRemoveBtn()) {
            holder.ivRemove.setVisibility(View.GONE);
        } else {
            holder.ivRemove.setVisibility(View.VISIBLE);
        }
        holder.ivRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int index = SelectedImageAdapter.this.application.getSelectedImages().indexOf(data);
                if (SelectedImageAdapter.this.activity.isFromPreview) {
                    SelectedImageAdapter.this.application.min_pos = Math.min(SelectedImageAdapter.this.application.min_pos, Math.max(0, pos - 1));
                }
                SelectedImageAdapter.this.application.removeSelectedImage(pos);
                if (SelectedImageAdapter.this.clickListner != null) {
                    SelectedImageAdapter.this.clickListner.onItemClick(v, data);
                }
                if (SelectedImageAdapter.this.hideRemoveBtn()) {
                    Toast.makeText(SelectedImageAdapter.this.activity, "At least 3 images require \nif you want to remove this images than add more images.", Toast.LENGTH_SHORT).show();
                }
                SelectedImageAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        View v = this.inflater.inflate(R.layout.grid_selected_item, parent, false);
        Holder holder = new Holder(v);
        if (getItemViewType(pos) == 1) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
        return holder;
    }
}
