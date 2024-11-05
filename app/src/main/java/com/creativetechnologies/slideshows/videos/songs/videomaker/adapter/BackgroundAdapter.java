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
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.FinalPreviewActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;

/**
 * Created by Vasundhara on 29-May-18.
 */

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.MyView> {
    FinalPreviewActivity activity;
    private int[] bg_array = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6, R.drawable.bg7, R.drawable.bg8, R.drawable.bg9, R.drawable.bg10, R.drawable.bg11, R.drawable.bg12,R.drawable.bg13,R.drawable.bg14,R.drawable.bg15};
    private RequestManager glide;
    private MyApplication application;
    int lastPos = 0;
    private EventListener mEventListener;

    public BackgroundAdapter(FinalPreviewActivity activity) {
        this.activity = activity;
        this.glide = Glide.with((FragmentActivity) activity);
        this.application = MyApplication.getInstance();
    }

    public class MyView extends RecyclerView.ViewHolder {

        private ImageView ivBackground;
        private CheckBox cbSelect;
        private View clickableView;

        public MyView(View itemView) {
            super(itemView);
            this.cbSelect = (CheckBox) itemView.findViewById(R.id.cbSelect);
            ivBackground = (ImageView) itemView.findViewById(R.id.ivBackground);
            this.clickableView = itemView.findViewById(R.id.clickableView);
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
        View view = inflater.inflate(R.layout.background_row_items, parent, false);
        return new MyView(view);
    }

    public int getItem(int pos) {
        return bg_array[pos];
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {
        final int themes = getItem(position);
        Glide.with(application).load(Integer.valueOf(themes)).into(holder.ivBackground);
        holder.cbSelect.setChecked(themes == activity.getBg());
        holder.clickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (themes != activity.getBg()) {
                    activity.setBg(themes);
                    if (themes != -1) {
                        notifyItemChanged(lastPos);
                        notifyItemChanged(position);
                        lastPos = position;
                        onItemViewClicked(position);
//                        activity.reset();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bg_array.length;
    }
}
