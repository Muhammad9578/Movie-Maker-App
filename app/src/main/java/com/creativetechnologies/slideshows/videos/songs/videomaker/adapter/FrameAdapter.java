package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.FinalPreviewActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.ScalingUtilities;

import java.io.FileOutputStream;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.Holder> {
    FinalPreviewActivity activity;
    private MyApplication application;
    private OnItemClickListner<Object> clickListner;
    private int[] drawable = new int[]{-1, R.drawable.frame1, R.drawable.frame2, R.drawable.frame3, R.drawable.frame4, R.drawable.frame5, R.drawable.frame6, R.drawable.frame7, R.drawable.frame8,
            R.drawable.frame9, R.drawable.frame10, R.drawable.frame11, R.drawable.frame12, R.drawable.frame13, R.drawable.frame14, R.drawable.frame15,
            R.drawable.frame16, R.drawable.frame17, R.drawable.frame18, R.drawable.frame19, R.drawable.frame20, R.drawable.frame21, R.drawable.frame22};
    private RequestManager glide;
    private LayoutInflater inflater;
    int lastPos = 0;

    public class Holder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        private ImageView ivThumb;
        private View mainView;
        private TextView tvThemeName;

        public Holder(View v) {
            super(v);
            this.cbSelect = (CheckBox) v.findViewById(R.id.cbSelect);
            this.ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            this.tvThemeName = (TextView) v.findViewById(R.id.tvThemeName);
            this.clickableView = v.findViewById(R.id.clickableView);
            this.mainView = v;
        }
    }

    public FrameAdapter(FinalPreviewActivity activity) {
        this.activity = activity;
        this.application = MyApplication.getInstance();
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.drawable.length;
    }

    public int getItem(int pos) {
        return this.drawable[pos];
    }

    public void onBindViewHolder(Holder holder, final int pos) {
        final int themes = getItem(pos);
        holder.ivThumb.setScaleType(ScaleType.FIT_XY);
        Glide.with(application).load(Integer.valueOf(themes)).into(holder.ivThumb);
        holder.cbSelect.setChecked(themes == this.activity.getFrame());
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (themes != FrameAdapter.this.activity.getFrame()) {
                    FrameAdapter.this.activity.setFrame(themes);
//                    if (themes != -1) {
                    FrameAdapter.this.notifyItemChanged(FrameAdapter.this.lastPos);
                    FrameAdapter.this.notifyItemChanged(pos);
                    FrameAdapter.this.lastPos = pos;
                    FileUtils.deleteFile(FileUtils.frameFile);
                    try {
                        Bitmap bm = ScalingUtilities.scaleCenterCrop(BitmapFactory.decodeResource(FrameAdapter.this.activity.getResources(), themes), MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                        FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                        bm.compress(CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();
                        bm.recycle();
                        System.gc();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (themes != -1) {
                        FinalPreviewActivity.iv_step4_effect.setVisibility(View.VISIBLE);
                    } else {
                        FinalPreviewActivity.iv_step4_effect.setVisibility(View.INVISIBLE);
                    }
//                    }
                }
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.frame_row_items, parent, false));
    }
}
