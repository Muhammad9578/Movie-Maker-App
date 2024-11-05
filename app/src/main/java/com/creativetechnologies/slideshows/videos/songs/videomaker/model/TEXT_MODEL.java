package com.creativetechnologies.slideshows.videos.songs.videomaker.model;

import android.graphics.Typeface;

/**
 * Created by soham on 9/8/2017.
 */
public class TEXT_MODEL {
    String name;
    Typeface typeface = null;
    Integer colors;

    public TEXT_MODEL(String name, Typeface typeface, Integer colors) {
        this.name = name;
        this.typeface = typeface;
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public Integer getColors() {
        return colors;
    }

    public void setColors(Integer colors) {
        this.colors = colors;
    }
}
