package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.creativetechnologies.slideshows.videos.songs.videomaker.FinalPreviewActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.FileUtils;
import com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes.THREEDTHEMES;

import java.util.ArrayList;
import java.util.Arrays;

public class ThreeDEffectAdapter extends RecyclerView.Adapter<ThreeDEffectAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private LayoutInflater inflater;
    private ArrayList<THREEDTHEMES> list;
    private EventListener mEventListener;
    private FinalPreviewActivity previewActivity;

    public class Holder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        private ImageView ivThumb;
        private View mainView;
        private TextView tvThemeName;

        public Holder(View v) {
            super(v);
            cbSelect = (CheckBox) v.findViewById(R.id.cbSelect);
            ivThumb = (ImageView) v.findViewById(R.id.ivThumb);
            tvThemeName = (TextView) v.findViewById(R.id.tvThemeName);
            clickableView = v.findViewById(R.id.clickableView);
            mainView = v;
        }
    }

    public ThreeDEffectAdapter(FinalPreviewActivity previewActivity) {
        this.previewActivity = previewActivity;
        list = new ArrayList(Arrays.asList(THREEDTHEMES.values()));
        inflater = LayoutInflater.from(previewActivity);
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

    public Holder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new Holder(inflater.inflate(R.layout.movie_theme_items, paramViewGroup, false));
    }

    public void onBindViewHolder(Holder holder, final int pos) {

        THREEDTHEMES themes = (THREEDTHEMES) list.get(pos);
        Glide.with(application).load(Integer.valueOf(themes.getThemeDrawable())).into(holder.ivThumb);
        holder.tvThemeName.setText(themes.toString());
        holder.cbSelect.setChecked(themes == application.selectedTheme);
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                if (list.get(pos) != application.selectedTheme) {
                onItemViewClicked(pos);
//                }
            }
        });
    }

    private void deleteThemeDir(final String dir) {
        new Thread() {
            public void run() {
                FileUtils.deleteThemeDir(dir);
            }
        }.start();
    }

    public ArrayList<THREEDTHEMES> getList() {
        return list;
    }

    public int getItemCount() {
        return list.size();
    }
}
