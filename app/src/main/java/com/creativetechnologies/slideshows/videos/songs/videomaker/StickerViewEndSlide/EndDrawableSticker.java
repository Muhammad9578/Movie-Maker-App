package com.creativetechnologies.slideshows.videos.songs.videomaker.StickerViewEndSlide;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by snowbean on 16-8-6.
 */
public class EndDrawableSticker extends EndSticker {

    private Drawable mDrawable;
    private Rect mRealBounds;

    public EndDrawableSticker(Drawable drawable) {
        mDrawable = drawable;
        mMatrix = new Matrix();
        mRealBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    @Override
    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.concat(mMatrix);
        mDrawable.setBounds(mRealBounds);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @Override
    public void release() {
        super.release();
        if (mDrawable != null) {
            mDrawable = null;
        }
    }
}
