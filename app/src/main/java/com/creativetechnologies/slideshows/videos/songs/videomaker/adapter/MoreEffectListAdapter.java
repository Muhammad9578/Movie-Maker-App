package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

/**
 * Created by Vasundhara on 12-May-18.
 */

public class MoreEffectListAdapter extends RecyclerView.Adapter<MoreEffectListAdapter.MyView> {

    private Integer[] ids = new Integer[]{R.drawable.daimond_out, R.drawable.daimond_in, R.drawable.circle_out, R.drawable.circle_in,
            R.drawable.square_out, R.drawable.square_in, R.drawable.skew_right_open, R.drawable.skew_left_close,
            R.drawable.circle_left_up, R.drawable.circle_right_bottom, R.drawable.pin_wheel, R.drawable.four_train, R.drawable.vertical_ran, R.drawable.rect_rand};

    private Context context;
    private EventListener mEventListener;

    public MoreEffectListAdapter(Context context) {
        this.context = context;
    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public MyView(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
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

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.two_d_list_effect_row, parent, false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {

        Drawable drawable_overlay = context.getResources().getDrawable(ids[position]);
//        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.20);
//        holder.itemView.getLayoutParams().height = height;
        holder.ivImage.setImageDrawable(drawable_overlay);

        holder.itemView.getLayoutParams().height = (int) ((GlobalData.screenHeight / 4));
//        holder.ivImage.getLayoutParams().width = (int) ((GlobalData.screenWidth / 2.5));

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ids.length;
    }
}
