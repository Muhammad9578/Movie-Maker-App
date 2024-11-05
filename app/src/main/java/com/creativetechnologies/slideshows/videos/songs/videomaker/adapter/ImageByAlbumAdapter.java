package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;

public class ImageByAlbumAdapter extends RecyclerView.Adapter<ImageByAlbumAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;
    private Context context;

    public class Holder extends RecyclerView.ViewHolder {
        View clickableView;
        ImageView imageView, ivDone;
        View parent;
//        TextView textView;

        public Holder(View v) {
            super(v);
            this.parent = v;
            this.imageView = (ImageView) v.findViewById(R.id.imageView1);
            ivDone = (ImageView) v.findViewById(R.id.ivDone);
//            this.textView = (TextView) v.findViewById(R.id.textView1);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (ImageByAlbumAdapter.this.clickListner != null) {
                ImageByAlbumAdapter.this.clickListner.onItemClick(view, item);
            }
        }
    }

    public ImageByAlbumAdapter(Context activity) {
        this.context = activity;
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.application.getImageByAlbum(this.application.getSelectedFolderId()).size();
    }

    public ImageData getItem(int pos) {
        return (ImageData) this.application.getImageByAlbum(this.application.getSelectedFolderId()).get(pos);
    }

    public void onBindViewHolder(final Holder holder, final int pos) {
        CharSequence charSequence;
        int i;
        final ImageData data = getItem(pos);
        holder.ivDone.setSelected(true);
        if (data.imageCount == 0) {
            charSequence = "";
        } else {
            charSequence = String.format("%02d", new Object[]{Integer.valueOf(data.imageCount)});
        }
        this.glide.load(data.temp_imagePath).into(holder.imageView);
        if (!charSequence.equals("")) {
            holder.ivDone.setVisibility(View.VISIBLE);
        } else {
            holder.ivDone.setVisibility(View.GONE);
        }
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               // if (application.getSelectedImages().size() < 30) {
                    if (holder.imageView.getDrawable() == null) {
                        Toast.makeText(ImageByAlbumAdapter.this.application, "Image corrupted or not support.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (data.imageCount == 0) {

                        application.addSelectedImage(data);
                        notifyItemChanged(pos);

                        if (ImageByAlbumAdapter.this.clickListner != null) {
                            ImageByAlbumAdapter.this.clickListner.onItemClick(v, data);
                        }

                    } else {
                        Toast.makeText(context, "Already selected", Toast.LENGTH_SHORT).show();
                    }

//                } else {
//                    Toast.makeText(context, "Maximum 30 images can be select", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items_by_folder, parent, false));
    }
}
