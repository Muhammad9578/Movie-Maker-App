package com.creativetechnologies.slideshows.videos.songs.videomaker.model;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class ImageData {
    public String folderName;
    public long id;
    public String imageAlbum;
    public int imageCount = 0;
    public String imagePath;
    public String temp_imagePath;
    public String temp_org_imagePath;
    public int twoDEffect = 0;
    public Drawable drawable;
    public String direction = "";
    public int direction_position;
    public String moredirection = "";
    public int more_direction_position;
    public int selected_image_count = 0;

    public int getSelected_image_count() {
        return selected_image_count;
    }

    public void setSelected_image_count(int selected_image_count) {
        this.selected_image_count = selected_image_count;
    }

    public String getTemp_imagePath() {
        return temp_imagePath;
    }

    public void setTemp_imagePath(String temp_imagePath) {
        this.temp_imagePath = temp_imagePath;
    }

    public String getTemp_org_imagePath() {
        return temp_org_imagePath;
    }

    public void setTemp_org_imagePath(String temp_org_imagePath) {
        this.temp_org_imagePath = temp_org_imagePath;
    }

    public int getDirection_position() {
        return direction_position;
    }

    public void setDirection_position(int direction_position) {
        this.direction_position = direction_position;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getMoredirection() {
        return moredirection;
    }

    public void setMoredirection(String moredirection) {
        this.moredirection = moredirection;
    }

    public int getMore_direction_position() {
        return more_direction_position;
    }

    public void setMore_direction_position(int more_direction_position) {
        this.more_direction_position = more_direction_position;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getTwoDEffect() {
        return twoDEffect;
    }

    public void setTwoDEffect(int twoDEffect) {
        this.twoDEffect = twoDEffect;
    }

    public boolean isSupported = true;

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageAlbum() {
        return this.imageAlbum;
    }

    public void setImageAlbum(String imageAlbum) {
        this.imageAlbum = imageAlbum;
    }

    public int getImageCount() {
        return this.imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        if (TextUtils.isEmpty(this.imagePath)) {
            return super.toString();
        }
        return "ImageData { imagePath=" + this.imagePath + ",folderName=" + this.folderName + ",imageCount=" + this.imageCount + " }";
    }


}
