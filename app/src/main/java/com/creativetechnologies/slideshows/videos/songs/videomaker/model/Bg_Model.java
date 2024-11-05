package com.creativetechnologies.slideshows.videos.songs.videomaker.model;

import android.graphics.Bitmap;

/**
 * Created by Vishal2.vasundhara on 08-Jul-17.
 */
public class Bg_Model {

    String bg_bitmap;
    Bitmap bitmap;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getBg_bitmap() {
        return bg_bitmap;
    }

    public void setBg_bitmap(String bg_bitmap) {
        this.bg_bitmap = bg_bitmap;
    }
}
