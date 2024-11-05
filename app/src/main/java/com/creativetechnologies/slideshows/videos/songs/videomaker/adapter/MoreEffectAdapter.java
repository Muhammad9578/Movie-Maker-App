package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

import java.util.ArrayList;

/**
 * Created by Vasundhara on 12-May-18.
 */

public class MoreEffectAdapter extends RecyclerView.Adapter<MoreEffectAdapter.MyView> {

    private ArrayList<ImageData> list = new ArrayList<>();
    private Context context;
    private RequestManager glide;
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;

    public MoreEffectAdapter(Context context, ArrayList<ImageData> list) {
        this.context = context;
        this.list = list;
        this.glide = Glide.with(this.context);
    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public MyView(View itemView) {
            super(itemView);

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
        View view = layoutInflater.inflate(R.layout.two_d_effect_row, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {
        final ImageData data = getItem(position);
        if (Share.temp_MoreEffect_selectedImages.size() > 0) {
            if (position % 2 == 0) {
                Log.e("TAG", "data.imagePath if:=>" + Share.temp_MoreEffect_selectedImages.get(position).getImagePath());
                glide.load(Share.temp_MoreEffect_selectedImages.get(position).getImagePath()).asBitmap().override(200, 200).placeholder(R.drawable.appicon).into(holder.ivImage);
            } else {
                Log.e("TAG", "data.imagePath else:=>" + list.get(position).getDrawable());
                if (!Share.temp_MoreEffect_selectedImages.get(position).getMoredirection().equals("")) {
                    holder.ivImage.setImageDrawable(list.get(position).getDrawable());
                } else {
                    holder.ivImage.setImageDrawable(list.get(position).getDrawable());
                }
            }
        }

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position % 2 != 0) {

                    Share.plus_button_click_position = position;
                    if (clickListner != null) {
                        clickListner.onItemClick(v, data);
                    }
                }
            }
        });
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    @Override
    public int getItemCount() {
        return list.size() - 1;
//        return list.size();
    }

    public ImageData getItem(int pos) {
        ArrayList<ImageData> list = application.getSelectedImages();
        if (list.size() <= pos) {
            return new ImageData();
        }
        return (ImageData) list.get(pos);
    }
}
