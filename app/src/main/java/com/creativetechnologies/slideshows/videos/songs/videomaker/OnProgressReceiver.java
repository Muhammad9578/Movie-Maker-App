package com.creativetechnologies.slideshows.videos.songs.videomaker;

public interface OnProgressReceiver {
    void onImageProgressFrameUpdate(float f);

    void onProgressFinish(String str);

    void onVideoProgressFrameUpdate(float f);
}
