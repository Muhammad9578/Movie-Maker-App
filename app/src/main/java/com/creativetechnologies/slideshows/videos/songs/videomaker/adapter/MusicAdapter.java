package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.creativetechnologies.slideshows.videos.songs.videomaker.PickupAudioFile;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.Bg_Model;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

import java.util.ArrayList;

/**
 * Created by Vishal2.vasundhara on 10-Jul-17.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyView> {

    private Context context;
    private ArrayList<Bg_Model> list = new ArrayList<>();
    private EventListener mEventListener;

    public MusicAdapter(Context context, ArrayList<Bg_Model> list) {
        this.context = context;
        this.list = list;
    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView iv_music_img, iv_play, iv_stop;
        CheckBox cbSelect;

        public MyView(View itemView) {
            super(itemView);

            iv_music_img = (ImageView) itemView.findViewById(R.id.iv_music_img);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
            iv_stop = (ImageView) itemView.findViewById(R.id.iv_stop);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }

    public interface EventListener {
        void onItemViewClicked(int position, View view);

        void onItemViewClickedPlay(int position, View view, View view1);

        void onItemViewClickedPause(int position, View view, View view1);

        void onDeleteMember(int position, View view);
    }


    private void onItemViewClicked(int position, View view) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(position, view);
        }
    }

    private void onItemViewClickedPlay(int position, View view, View view1) {
        if (mEventListener != null) {
            mEventListener.onItemViewClickedPlay(position, view, view1);
        }
    }

    private void onItemViewClickedPause(int position, View view, View view1) {
        if (mEventListener != null) {
            mEventListener.onItemViewClickedPause(position, view, view1);
        }
    }

    public int getItem(int position) {
        return position;
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.music_row, parent, false);

        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        holder.iv_music_img.getLayoutParams().height = (int) (PickupAudioFile.height_of_view / 3.18);
        holder.cbSelect.getLayoutParams().height = (int) (PickupAudioFile.height_of_view / 3.18);

        holder.iv_music_img.setImageBitmap(list.get(position).getBitmap());

        if (Share.selected_audio_pos == position) {
            holder.cbSelect.setVisibility(View.VISIBLE);
        }

        holder.iv_music_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.selected_audio_pos = position;
                onItemViewClicked(position, v);
            }
        });
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemViewClickedPlay(position, holder.iv_play, holder.iv_stop);
            }
        });

        holder.iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemViewClickedPause(position, holder.iv_play, holder.iv_stop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
