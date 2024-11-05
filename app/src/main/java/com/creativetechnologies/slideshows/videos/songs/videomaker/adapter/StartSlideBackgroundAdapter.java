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
import com.creativetechnologies.slideshows.videos.songs.videomaker.CustomSlideActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;
import com.creativetechnologies.slideshows.videos.songs.videomaker.view.ScaleCardLayout;

/**
 * Created by Vasundhara on 29-May-18.
 */

public class StartSlideBackgroundAdapter extends RecyclerView.Adapter<StartSlideBackgroundAdapter.MyView> {
    CustomSlideActivity activity;
    private int[] bg_array = {R.drawable.ic_start_1, R.drawable.ic_start_2, R.drawable.ic_start_3, R.drawable.ic_start_4, R.drawable.ic_start_5, R.drawable.ic_start_6, R.drawable.ic_start_7, R.drawable.ic_start_8,
            R.drawable.ic_start_9, R.drawable.ic_start_10, R.drawable.ic_start_11, R.drawable.ic_start_12, R.drawable.ic_start_13, R.drawable.ic_start_14, R.drawable.ic_start_15, R.drawable.ic_start_16, R.drawable.ic_start_17,
            R.drawable.ic_start_18, R.drawable.ic_start_19};
    private RequestManager glide;
    private MyApplication application;
    int lastPos = 0;
    private EventListener mEventListener;

    public StartSlideBackgroundAdapter(CustomSlideActivity activity) {
        this.activity = activity;
        this.glide = Glide.with((FragmentActivity) activity);
        this.application = MyApplication.getInstance();
    }

    public class MyView extends RecyclerView.ViewHolder {

        private ImageView ivBackground;
        private CheckBox cbSelect;
        private View clickableView;
        private ScaleCardLayout slMain;

        public MyView(View itemView) {
            super(itemView);
            this.cbSelect = (CheckBox) itemView.findViewById(R.id.cbSelect);
            ivBackground = (ImageView) itemView.findViewById(R.id.ivBackground);
            this.clickableView = itemView.findViewById(R.id.clickableView);
            slMain = (ScaleCardLayout) itemView.findViewById(R.id.slMain);
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
        View view = inflater.inflate(R.layout.start_slide_background_row_items, parent, false);
        return new MyView(view);
    }

    public int getItem(int pos) {
        return bg_array[pos];
    }

    @Override
    public void onBindViewHolder(@NonNull final MyView holder, final int position) {
        final int themes = getItem(position);

        holder.slMain.getLayoutParams().height = SharedPrefs.getInt(activity, SharedPrefs.screen_hight) / 6;
        holder.slMain.getLayoutParams().width = SharedPrefs.getInt(activity, SharedPrefs.screen_width) / 3;

        Glide.with(application).load(Integer.valueOf(themes)).asBitmap().override(300, 300).into(holder.ivBackground);
        holder.cbSelect.setChecked(themes == activity.getStartSlide());
        holder.clickableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (themes != activity.getStartSlide()) {
                    activity.setStartSlide(themes);
                    notifyItemChanged(lastPos);
                    notifyItemChanged(position);
                    lastPos = position;
                    onItemViewClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bg_array.length;
    }
}
