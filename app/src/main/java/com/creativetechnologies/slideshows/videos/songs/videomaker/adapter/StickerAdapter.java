package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.StickerModel;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.MyView> {

    private Context context;
    private EventListener mEventListener;
    private ArrayList<StickerModel> list = new ArrayList<>();

    public StickerAdapter(Context context, ArrayList<StickerModel> list) {
        this.context = context;
        this.list = list;
    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView ivSticker;

        public MyView(View itemView) {
            super(itemView);
            ivSticker = (ImageView) itemView.findViewById(R.id.ivStickerImage);
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
        View view = inflater.inflate(R.layout.sticker_row_item, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {

        holder.ivSticker.setImageBitmap(list.get(position).getBitmap());

        holder.ivSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
