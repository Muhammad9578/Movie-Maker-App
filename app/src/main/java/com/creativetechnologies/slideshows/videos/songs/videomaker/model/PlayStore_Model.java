package com.creativetechnologies.slideshows.videos.songs.videomaker.model;

/**
 * Created by Vasundhara on 21-May-18.
 */

public class PlayStore_Model {
    String name, image, package_name;

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
