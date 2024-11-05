package com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.mask.TwoDMaskBitmapEffect.EFFECT;

import java.util.ArrayList;

public enum THEMES2D {
    Shine2d("Shine2d") {
        public ArrayList<EFFECT> getTheme2d() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.From_Left);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme2d(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getTheme2dDrawable() {
            return R.drawable.shine;
        }

        public int getTheme2dMusic() {
            return R.raw.song_1;
        }
    },
    From_Left("From_Left") {
        public ArrayList<EFFECT> getTheme2d() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.From_Left);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme2d(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getTheme2dDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getTheme2dMusic() {
            return R.raw.song_5;
        }
    };

    String name;

    public abstract ArrayList<EFFECT> getTheme2d();

    public abstract ArrayList<EFFECT> getTheme2d(ArrayList<EFFECT> arrayList);

    public abstract int getTheme2dDrawable();

    public abstract int getTheme2dMusic();

    private THEMES2D(String string) {
        this.name = "";
        this.name = string;
    }
}
