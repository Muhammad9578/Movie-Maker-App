package com.creativetechnologies.slideshows.videos.songs.videomaker.libffmpeg.utils;

import android.os.Handler;

import java.util.ArrayList;

public class VideoMaker {
    String audioPath = null;
    int frame_duration = 3;
    ArrayList<String> inputImages = null;
    Handler mHandler;
    private String outputVideoPath = null;

    public VideoMaker(ArrayList<String> inputImages, int frame_duration, String audioPath, String outputVideoPath) {
        this.outputVideoPath = outputVideoPath;
        this.audioPath = audioPath;
        this.frame_duration = frame_duration;
        this.inputImages = inputImages;
    }

    public String getAudioPath() {
        return this.audioPath;
    }

    public ArrayList<String> getInputImages() {
        return this.inputImages;
    }

    public String getOutputVideoPath() {
        return this.outputVideoPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setFrame_duration(int frame_duration) {
        this.frame_duration = frame_duration;
    }

    public void setInputImages(ArrayList<String> inputImages) {
        this.inputImages = inputImages;
    }

    public void setOutputVideoPath(String outputVideoPath) {
        this.outputVideoPath = outputVideoPath;
    }
}
