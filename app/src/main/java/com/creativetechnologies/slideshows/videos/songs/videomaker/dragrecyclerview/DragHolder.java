package com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;

/**
 * Created by Shinhyo on 2016. 6. 13..
 */
public class DragHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public static int mHandleId;
    public static OnClickListener mClickListener;
    private View mHandle;

    public DragHolder(View view) {
        super(view);
        mHandle = view.findViewById(mHandleId);
        view.findViewById(R.id.ivDelete).setOnClickListener(this);
        view.findViewById(R.id.ivEdit).setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public View getHandle() {
        return mHandle;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            if (v == itemView.findViewById(R.id.ivDelete)) {
                mClickListener.onItemClick(v, getAdapterPosition(), 1);
            }
            else if (v == itemView.findViewById(R.id.ivEdit)){
                mClickListener.onItemClick(v, getAdapterPosition(), 2);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mClickListener != null) {
            mClickListener.onItemLongClick(v, getAdapterPosition());
        }
        return true;
    }

}
