package com.creativetechnologies.slideshows.videos.songs.videomaker.twoDandthreeDthemes;

import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.mask.MoreMaskBitmapEffect;

import java.util.ArrayList;

/**
 * Created by Vasundhara on 17-Aug-18.
 */

public enum MORETHEME {

    MORE("MORE") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> mEffects = new ArrayList();
            mEffects.add(MoreMaskBitmapEffect.EFFECT.MORE);
            return mEffects;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_5;
        }
    };

    /*Shine("Shine") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.PIN_WHEEL);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SKEW_RIGHT_SPLIT);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SKEW_LEFT_SPLIT);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SKEW_RIGHT_MEARGE);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SKEW_LEFT_MEARGE);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.FOUR_TRIANGLE);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SQUARE_IN);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SQUARE_OUT);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CIRCLE_LEFT_BOTTOM);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CIRCLE_IN);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.DIAMOND_OUT);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.HORIZONTAL_COLUMN_DOWNMASK);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.RECT_RANDOM);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CROSS_IN);
            arrayList.add(MoreMaskBitmapEffect.EFFECT.DIAMOND_IN);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    CIRCLE_IN("Circle In") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CIRCLE_IN);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    CIRCLE_LEFT_BOTTOM("Circle Left Bottom") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CIRCLE_LEFT_BOTTOM);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    CIRCLE_OUT("Circle Out") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CIRCLE_OUT);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    CIRCLE_RIGHT_BOTTOM("Circle Right Bottom") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.CIRCLE_RIGHT_BOTTOM);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    DIAMOND_IN("Diamond In") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.DIAMOND_IN);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    DIAMOND_OUT("Diamond out") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.DIAMOND_OUT);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    ECLIPSE_IN("Eclipse In") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.ECLIPSE_IN);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    FOUR_TRIANGLE("Four Triangle") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.FOUR_TRIANGLE);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    OPEN_DOOR("Open Door") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.OPEN_DOOR);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    PIN_WHEEL("Pin Wheel") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.PIN_WHEEL);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    RECT_RANDOM("Rect Random") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.RECT_RANDOM);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    SKEW_LEFT_MEARGE("Skew Left Mearge") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SKEW_LEFT_MEARGE);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    SKEW_RIGHT_MEARGE("Skew Right Mearge") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SKEW_RIGHT_MEARGE);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    SQUARE_OUT("Square Out") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SQUARE_OUT);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    SQUARE_IN("Square In") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.SQUARE_IN);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    },
    VERTICAL_RECT("Vertical Rect") {
        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme() {
            ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList = new ArrayList();
            arrayList.add(MoreMaskBitmapEffect.EFFECT.VERTICAL_RECT);
            return arrayList;
        }

        public ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList) {
            return new ArrayList();
        }

        public int getThemeDrawable() {
            return R.drawable.whatsapp_press;
        }

        public int getThemeMusic() {
            return R.raw.song_1;
        }
    };*/

    String name;

    public abstract ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme();

    public abstract ArrayList<MoreMaskBitmapEffect.EFFECT> getTheme(ArrayList<MoreMaskBitmapEffect.EFFECT> arrayList);

    public abstract int getThemeDrawable();

    public abstract int getThemeMusic();

    private MORETHEME(String str) {
        this.name = "";
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
