package com.creativetechnologies.slideshows.videos.songs.videomaker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumAdapterById extends RecyclerView.Adapter<AlbumAdapterById.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private ArrayList<String> folderId = new ArrayList(application.getAllAlbum().keySet());
    private RequestManager glide;
    private LayoutInflater inflater;

    class C06281 implements Comparator<String> {
        C06281() {
        }

        public int compare(String s1, String s2) {
            if (s1 != null)
                return s1.compareToIgnoreCase(s2);
            return 0;
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        ImageView imageView;
        View parent;
        TextView textView;

        public Holder(View v) {
            super(v);
            this.parent = v;
            this.cbSelect = (CheckBox) v.findViewById(R.id.cbSelect);
            this.imageView = (ImageView) v.findViewById(R.id.imageView1);
            this.textView = (TextView) v.findViewById(R.id.textView1);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (AlbumAdapterById.this.clickListner != null) {
                AlbumAdapterById.this.clickListner.onItemClick(view, item);
            }
        }
    }

    public AlbumAdapterById(Context activity) {
        glide = Glide.with(activity);
        Collections.sort(folderId, new C06281());
        if (folderId.size() != 0) {
            application.setSelectedFolderId((String) folderId.get(0));
        }
        inflater = LayoutInflater.from(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.folderId.size();
    }

    public String getItem(int pos) {
        return (String) this.folderId.get(pos);
    }

    public void onBindViewHolder(Holder holder, int pos) {
        final String currentFolderId = getItem(pos);
        final ImageData data = (ImageData) this.application.getImageByAlbum(currentFolderId).get(0);
        Log.e("TAG", "getImageByAlbum :" + data.getImagePath());
        holder.textView.setSelected(true);
        holder.textView.setText(data.folderName);
        this.glide.load(data.temp_imagePath).into(holder.imageView);
        holder.cbSelect.setChecked(currentFolderId.equals(this.application.getSelectedFolderId()));
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AlbumAdapterById.this.application.setSelectedFolderId(currentFolderId);
                if (AlbumAdapterById.this.clickListner != null) {
                    AlbumAdapterById.this.clickListner.onItemClick(v, data);
                }
                AlbumAdapterById.this.notifyDataSetChanged();
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup parent, int pos) {
        return new Holder(inflater.inflate(R.layout.items, parent, false));
    }
}
