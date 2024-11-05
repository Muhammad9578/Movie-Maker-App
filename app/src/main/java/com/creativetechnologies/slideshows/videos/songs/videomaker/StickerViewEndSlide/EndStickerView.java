package com.creativetechnologies.slideshows.videos.songs.videomaker.StickerViewEndSlide;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;

import com.creativetechnologies.slideshows.videos.songs.videomaker.FontActivity;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * EndSticker view
 * Created by snowbean on 16-8-2.
 */

public class EndStickerView extends FrameLayout {
    private enum ActionMode {
        NONE,   //nothing
        DRAG,   //drag the sticker with your finger
        ZOOM_WITH_TWO_FINGER,   //zoom in or zoom out the sticker and rotate the sticker with two finger
        ZOOM_WITH_ICON,    //zoom in or zoom out the sticker and rotate the sticker with icon
        DELETE,  //delete the handling sticker
        FLIP_HORIZONTAL, //horizontal flip the sticker
        CLICK    //Click the EndSticker
    }

    private static final String TAG = "StickerView";

    private Paint mBorderPaint;

    private RectF mStickerRect;

    private Matrix mSizeMatrix;
    private Matrix mDownMatrix;
    private Matrix mMoveMatrix;

    public static EndBitmapStickerIcon mDeleteIcon;
    public static EndBitmapStickerIcon mZoomIcon;
    public static EndBitmapStickerIcon mFlipIcon;

    //the first point down position
    private float mDownX;
    private float mDownY;

    private float mOldDistance = 0f;
    private float mOldRotation = 0f;

    private PointF mMidPoint;

    private ActionMode mCurrentMode = ActionMode.NONE;

    public static List<EndSticker> mEndStickers = new ArrayList<>();
    public static EndSticker mHandlingSticker;

    public static boolean isTouchable = false;

    private boolean mLocked;

    private int mTouchSlop = 3;
    private Context context;

    private OnStickerOperationListener mOnStickerOperationListener;

    public EndStickerView(Context context) {
        this(context, null);
    }

    public EndStickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public EndStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setAlpha(128);

        mSizeMatrix = new Matrix();
        mDownMatrix = new Matrix();
        mMoveMatrix = new Matrix();

        mStickerRect = new RectF();

        mDeleteIcon = new EndBitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_close_white_18dp));
        mZoomIcon = new EndBitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_scale_white_18dp));
        mFlipIcon = new EndBitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_white_18dp));
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mStickerRect.left = left;
            mStickerRect.top = top;
            mStickerRect.right = right;
            mStickerRect.bottom = bottom;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (canvas != null) {

            drawStickers(canvas);
        }
    }

    private void drawStickers(Canvas canvas) {
        for (int i = 0; i < mEndStickers.size(); i++) {
            EndSticker sticker = mEndStickers.get(i);
            if (sticker != null) {
                sticker.draw(canvas);
            }
        }

        if (mHandlingSticker != null && !mLocked) {

            float[] bitmapPoints = getStickerPoints(mHandlingSticker);

            float x1 = bitmapPoints[0];
            float y1 = bitmapPoints[1];
            float x2 = bitmapPoints[2];
            float y2 = bitmapPoints[3];
            float x3 = bitmapPoints[4];
            float y3 = bitmapPoints[5];
            float x4 = bitmapPoints[6];
            float y4 = bitmapPoints[7];

            canvas.drawLine(x1, y1, x2, y2, mBorderPaint);
            canvas.drawLine(x1, y1, x3, y3, mBorderPaint);
            canvas.drawLine(x2, y2, x4, y4, mBorderPaint);
            canvas.drawLine(x4, y4, x3, y3, mBorderPaint);

            float rotation = calculateRotation(x3, y3, x4, y4);
            //draw delete icon
//            configIconMatrix(mDeleteIcon, x1, y1, rotation);
//            mDeleteIcon.draw(canvas, mBorderPaint);

            //draw zoom icon
//            configIconMatrix(mZoomIcon, x4, y4, rotation);
//            mZoomIcon.draw(canvas, mBorderPaint);

            //draw flip icon
//            configIconMatrix(mFlipIcon, x2, y2, rotation);
//            mFlipIcon.draw(canvas, mBorderPaint);

            if (mHandlingSticker.getTag().toString().equalsIgnoreCase("sticker")) {
                //draw delete icon
                Log.e("---------------", "sticker");
                configIconMatrix(mDeleteIcon, x1, y1, rotation);
                mDeleteIcon.draw(canvas, mBorderPaint);

                //draw zoom icon
                configIconMatrix(mZoomIcon, x4, y4, rotation);
                mZoomIcon.draw(canvas, mBorderPaint);

               /* //draw flip icon
                configIconMatrix(mFlipIcon, x2, y2, rotation);
                mFlipIcon.draw(canvas, mBorderPaint);*/
            } else if (mHandlingSticker.getTag().toString().equalsIgnoreCase("text_sticker")) {
                //draw delete icon
                configIconMatrix(mDeleteIcon, x1, y1, rotation);
                mDeleteIcon.draw(canvas, mBorderPaint);

                //draw flip icon
                configIconMatrix(mFlipIcon, x2, y2, rotation);
                mFlipIcon.draw(canvas, mBorderPaint);

                //draw zoom icon
                configIconMatrix(mZoomIcon, x4, y4, rotation);
                mZoomIcon.draw(canvas, mBorderPaint);
            }
        }
    }

    private void configIconMatrix(EndBitmapStickerIcon icon, float x, float y, float rotation) {
        icon.setX(x);
        icon.setY(y);
        icon.getMatrix().reset();

        icon.getMatrix().postRotate(
                rotation, icon.getWidth() / 2, icon.getHeight() / 2);
        icon.getMatrix().postTranslate(
                x - icon.getWidth() / 2, y - icon.getHeight() / 2);
    }

//

    public void setControlItemsHidden() {
        mHandlingSticker = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLocked) return super.onTouchEvent(event);

        int action = MotionEventCompat.getActionMasked(event);


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mCurrentMode = ActionMode.DRAG;
                Log.e(TAG, "Module ACTION_DOWN");
                mDownX = event.getX();
                mDownY = event.getY();

                if (checkIconTouched(mDeleteIcon)) {
                    mCurrentMode = ActionMode.DELETE;
                } else if (checkIconTouched(mFlipIcon)) {
                    mCurrentMode = ActionMode.FLIP_HORIZONTAL;
                } else if (checkIconTouched(mZoomIcon) && mHandlingSticker != null) {
                    mCurrentMode = ActionMode.ZOOM_WITH_ICON;
                    mMidPoint = calculateMidPoint();
                    mOldDistance = calculateDistance(mMidPoint.x, mMidPoint.y, mDownX, mDownY);
                    mOldRotation = calculateRotation(mMidPoint.x, mMidPoint.y, mDownX, mDownY);
                } else {
                    mHandlingSticker = findHandlingSticker();
                }

                if (mHandlingSticker != null) {
                    mDownMatrix.set(mHandlingSticker.getMatrix());
                }
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e(TAG, "Module ACTION_POINTER_DOWN");
                mOldDistance = calculateDistance(event);
                mOldRotation = calculateRotation(event);

                mMidPoint = calculateMidPoint(event);

                if (mHandlingSticker != null &&
                        isInStickerArea(mHandlingSticker, event.getX(1), event.getY(1)) &&
                        !checkIconTouched(mDeleteIcon))

                    mCurrentMode = ActionMode.ZOOM_WITH_TWO_FINGER;
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "Module ACTION_MOVE");
                handleCurrentMode(event);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "Module ACTION_UP");
                if (mCurrentMode == ActionMode.DELETE && mHandlingSticker != null) {
                    if (mOnStickerOperationListener != null) {
                        mOnStickerOperationListener.onStickerDeleted(mHandlingSticker);
                    }

                    Share.TEXT_EDIT_FLAG = false;

                    if (mHandlingSticker.getTag().toString().equalsIgnoreCase("text_sticker")) {
                        Share.FONT_TEXT2.remove(Share.STICKER_POSITION);
                        // Share.SELECT_CATEGORY_REQUEST_with_edit = 203;
                    }
                    if (mHandlingSticker.getTag().toString().equalsIgnoreCase("sticker")) {
                        Share.FONT_TEXT2.remove(Share.STICKER_POSITION);
                        // Share.SELECT_CATEGORY_REQUEST_with_edit = 203;
                    }

                    mEndStickers.remove(mHandlingSticker);
                    mHandlingSticker.release();
                    mHandlingSticker = null;
                    invalidate();
                }

                if (mCurrentMode == ActionMode.FLIP_HORIZONTAL && mHandlingSticker != null) {

                    if (mHandlingSticker.getTag().toString() != null) {
                        if (mHandlingSticker.getTag().toString().equalsIgnoreCase("text_sticker")) {
                            Share.tag_value = "text_sticker";
                            Share.TEXT_EDIT_FLAG = true;
                          /*  Bitmap b2_1_1 = createBitmapFromLayoutWithText(context, Share.FONT_TEXT2.get(Share.STICKER_POSITION).getName(), Share.FONT_TEXT2.get(Share.STICKER_POSITION).getColors(), Share.FONT_TEXT2.get(Share.STICKER_POSITION).getTypeface());
                            // Bitmap b2_1_1 = createBitmapFromLayoutWithText(context, mStickers.get(Share.STICKER_POSITION).toString(), -16777216, WeddingCardActivity.tv1.getTypeface());
                            Share.EDIT_DAILOG_DRAWABLE = new BitmapDrawable(getResources(), b2_1_1);*/

                            Intent i = new Intent(context, FontActivity.class);
                            context.startActivity(i);
                        }
                    } else {
                        Toast.makeText(context, "no action", Toast.LENGTH_SHORT).show();
                    }
                   /* mHandlingSticker.getMatrix().preScale(-1, 1,
                            mHandlingSticker.getCenterPoint().x, mHandlingSticker.getCenterPoint().y);

                    mHandlingSticker.setFlipped(!mHandlingSticker.isFlipped());
                    if (mOnStickerOperationListener != null) {
                        mOnStickerOperationListener.onStickerFlipped(mHandlingSticker);
                    }*/
                    invalidate();
                }


                if ((mCurrentMode == ActionMode.ZOOM_WITH_ICON || mCurrentMode == ActionMode.ZOOM_WITH_TWO_FINGER) && mHandlingSticker != null) {
                    if (mOnStickerOperationListener != null) {
                        mOnStickerOperationListener.onStickerZoomFinished(mHandlingSticker);
                    }
                }

                if (mCurrentMode == ActionMode.DRAG
                        && Math.abs(event.getX() - mDownX) < mTouchSlop
                        && Math.abs(event.getY() - mDownY) < mTouchSlop
                        && mHandlingSticker != null) {
                    mCurrentMode = ActionMode.CLICK;
                    if (mOnStickerOperationListener != null) {
                        mOnStickerOperationListener.onStickerClicked(mHandlingSticker);
                    }
                }

                if (mCurrentMode == ActionMode.DRAG && mHandlingSticker != null) {
                    if (mOnStickerOperationListener != null) {
                        mOnStickerOperationListener.onStickerDragFinished(mHandlingSticker);
                    }
                }

                mCurrentMode = ActionMode.NONE;
                break;

            case MotionEvent.ACTION_POINTER_UP:
//                Log.e(TAG, "Module ACTION_POINTER_UP");
                if (mCurrentMode == ActionMode.ZOOM_WITH_TWO_FINGER && mHandlingSticker != null) {
                    if (mOnStickerOperationListener != null) {
                        mOnStickerOperationListener.onStickerDragFinished(mHandlingSticker);
                    }
                }
                mCurrentMode = ActionMode.NONE;
                break;

        }//end of switch(action)

        return true;
    }


    private void handleCurrentMode(MotionEvent event) {
        switch (mCurrentMode) {
            case NONE:
                break;
            case DRAG:
                if (mHandlingSticker != null) {
                    mMoveMatrix.set(mDownMatrix);
                    mMoveMatrix.postTranslate(event.getX() - mDownX, event.getY() - mDownY);
                    mHandlingSticker.getMatrix().set(mMoveMatrix);
                }
                break;
            case ZOOM_WITH_TWO_FINGER:
                if (mHandlingSticker != null) {
                    float newDistance = calculateDistance(event);
                    float newRotation = calculateRotation(event);

                    mMoveMatrix.set(mDownMatrix);
                    mMoveMatrix.postScale(
                            newDistance / mOldDistance, newDistance / mOldDistance, mMidPoint.x, mMidPoint.y);
                    mMoveMatrix.postRotate(newRotation - mOldRotation, mMidPoint.x, mMidPoint.y);
                    mHandlingSticker.getMatrix().set(mMoveMatrix);
                }

                break;

            case ZOOM_WITH_ICON:
                try {
                    if (mHandlingSticker != null) {
                        float newDistance = calculateDistance(mMidPoint.x, mMidPoint.y, event.getX(), event.getY());
                        float newRotation = calculateRotation(mMidPoint.x, mMidPoint.y, event.getX(), event.getY());

                        mMoveMatrix.set(mDownMatrix);
                        mMoveMatrix.postScale(
                                newDistance / mOldDistance, newDistance / mOldDistance, mMidPoint.x, mMidPoint.y);
                        mMoveMatrix.postRotate(newRotation - mOldRotation, mMidPoint.x, mMidPoint.y);
                        mHandlingSticker.getMatrix().set(mMoveMatrix);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }// end of switch(mCurrentMode)
    }

    private boolean checkIconTouched(EndBitmapStickerIcon icon) {
        float x = icon.getX() - mDownX;
        float y = icon.getY() - mDownY;
        float distance_pow_2 = x * x + y * y;
        return distance_pow_2 <= Math.pow(icon.getIconRadius() + icon.getIconRadius(), 2);
    }

    /**
     * find the touched EndSticker
     **/
    private EndSticker findHandlingSticker() {
        for (int i = mEndStickers.size() - 1; i >= 0; i--) {
            if (isInStickerArea(mEndStickers.get(i), mDownX, mDownY)) {

                Share.STICKER_POSITION = i;

                return mEndStickers.get(i);
            }
        }
        return null;
    }

    private EndSticker findHandlingSticker2() {
        Log.e("TAG Module", "findHandlingSticker");
        for (int i = mEndStickers.size() - 1; i >= 0; i--) {
            Log.e("TAG Module", "findHandlingSticker if");
            return mEndStickers.get(i);
        }
        return null;
    }


    private boolean isInStickerArea(EndSticker sticker, float downX, float downY) {
        return sticker.contains(downX, downY);
    }

    private PointF calculateMidPoint(MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) return new PointF();
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }

    private PointF calculateMidPoint() {
        if (mHandlingSticker == null) return new PointF();
        return mHandlingSticker.getMappedCenterPoint();
    }

    /**
     * calculate rotation in line with two fingers and x-axis
     **/
    private float calculateRotation(MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) return 0f;
        double x = event.getX(0) - event.getX(1);
        double y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(y, x);
        return (float) Math.toDegrees(radians);
    }

    private float calculateRotation(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double radians = Math.atan2(y, x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * calculate Distance in two fingers
     **/
    private float calculateDistance(MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) return 0f;
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        double x = x1 - x2;
        double y = y1 - y2;

        return (float) Math.sqrt(x * x);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < mEndStickers.size(); i++) {
            EndSticker sticker = mEndStickers.get(i);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }

    }

    /**
     * EndSticker's drawable will be too bigger or smaller
     * This method is to transform it to fit
     * step 1：let the center of the sticker image is coincident with the center of the View.
     * step 2：Calculate the zoom and zoom
     **/
    private void transformSticker(EndSticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }


        if (mSizeMatrix != null) {
            mSizeMatrix.reset();
        }

        //step 1
        float offsetX = (getWidth() - sticker.getWidth()) / 2;
        float offsetY = (getHeight() - sticker.getHeight()) / 2;

        mSizeMatrix.postTranslate(offsetX, offsetY);

        //step 2
        float scaleFactor;
        if (getWidth() < getHeight()) {
            scaleFactor = (float) getWidth() / sticker.getWidth();
        } else {
            scaleFactor = (float) getHeight() / sticker.getHeight();
        }

        mSizeMatrix.postScale(scaleFactor / 2, scaleFactor / 2,
                getWidth() / 2, getHeight() / 2);

        sticker.getMatrix().reset();
        sticker.getMatrix().set(mSizeMatrix);

        invalidate();
    }

    public void replace(EndSticker sticker) {
        replace(sticker, true);
    }

    public void replace(EndSticker sticker, boolean needStayState) {
        if (mHandlingSticker != null && sticker != null) {
            if (needStayState) {
                sticker.getMatrix().set(mHandlingSticker.getMatrix());
                sticker.setFlipped(mHandlingSticker.isFlipped());
            } else {
                mHandlingSticker.getMatrix().reset();
                // reset scale, angle, and put it in center
                float offsetX = (getWidth() - mHandlingSticker.getWidth()) / 2;
                float offsetY = (getHeight() - mHandlingSticker.getHeight()) / 2;
                sticker.getMatrix().postTranslate(offsetX, offsetY);

                float scaleFactor;
                if (getWidth() < getHeight()) {
                    scaleFactor = (float) getWidth() / mHandlingSticker.getDrawable().getIntrinsicWidth();
                } else {
                    scaleFactor = (float) getHeight() / mHandlingSticker.getDrawable().getIntrinsicHeight();
                }
                sticker.getMatrix().postScale(scaleFactor / 2, scaleFactor / 2, getWidth() / 2, getHeight() / 2);
            }
            int index = mEndStickers.indexOf(mHandlingSticker);
            mEndStickers.set(index, sticker);
//            Share.drawable_list.set(index, sticker);
            mHandlingSticker = sticker;
        }
        invalidate();
    }

    public void addSticker(EndSticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "EndSticker to be added is null!");
            return;
        }
        float offsetX = (getWidth() - sticker.getWidth()) / 2;
        float offsetY = (getHeight() - sticker.getHeight()) / 2;
        sticker.getMatrix().postTranslate(offsetX, offsetY);

        float scaleFactor;
        if (getWidth() < getHeight()) {
            scaleFactor = (float) getWidth() / sticker.getDrawable().getIntrinsicWidth();
        } else {
            scaleFactor = (float) getHeight() / sticker.getDrawable().getIntrinsicHeight();
        }
        sticker.getMatrix().postScale(scaleFactor / 2, scaleFactor / 2, getWidth() / 2, getHeight() / 2);

        mHandlingSticker = sticker;
        mEndStickers.add(sticker);
//        Share.drawable_list.add(sticker);
        invalidate();
    }

    public float[] getStickerPoints(EndSticker sticker) {
        if (sticker == null) return new float[8];
        return sticker.getMappedBoundPoints();
    }

    public void save(File file) {
        EndStickerUtils.saveImageToGallery(file, createBitmap());
        EndStickerUtils.notifySystemGallery(getContext(), file);
    }

    public Bitmap createBitmap() {
        mHandlingSticker = null;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }

    public boolean isLocked() {
        return mLocked;
    }

    public void setLocked(boolean locked) {
        mLocked = locked;
        invalidate();
    }

    public EndBitmapStickerIcon getFlipIcon() {
        return mFlipIcon;
    }

    public void setFlipIcon(EndBitmapStickerIcon flipIcon) {
        mFlipIcon = flipIcon;
        postInvalidate();
    }

    public EndBitmapStickerIcon getZoomIcon() {
        return mZoomIcon;
    }

    public void setZoomIcon(EndBitmapStickerIcon zoomIcon) {
        mZoomIcon = zoomIcon;
        postInvalidate();
    }

    public EndBitmapStickerIcon getDeleteIcon() {
        return mDeleteIcon;
    }

    public void setDeleteIcon(EndBitmapStickerIcon deleteIcon) {
        mDeleteIcon = deleteIcon;
        postInvalidate();
    }

    public void setOnStickerOperationListener(OnStickerOperationListener onStickerOperationListener) {
        mOnStickerOperationListener = onStickerOperationListener;
    }

    public interface OnStickerOperationListener {
        void onStickerClicked(EndSticker sticker);

        void onStickerDeleted(EndSticker sticker);

        void onStickerDragFinished(EndSticker sticker);

        void onStickerZoomFinished(EndSticker sticker);

        void onStickerFlipped(EndSticker sticker);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static Bitmap createBitmapFromLayoutWithText(Context context, String s, int color, Typeface face) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.row_bitmap, null);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_text1);

        int brack1 = 0;
        int n = 0;
        String temp[];

        Log.e("TAG", "String is = = = " + s + "--------" + s.length());
        String a[] = s.split(" ");

        String a1 = "";


        if (s.length() >= 35) {

            int E_S = 0;                                                      //   extra_space
            Log.e("s.length()", s.length() + "");
            Log.e("a.length()", a.length + "");
            while (tv.getText().toString().length() <= s.length() + E_S) {
                Log.e("length()", tv.getText().toString().length() + "-----out of--------" + s.length() + "+" + E_S + "====================");
                if (n < a.length) {
                    if (a[n].length() < 35) {
                        while (a1.toString().length() < 35) {

                            if (n < a.length) {

                                a1 = a1 + a[n].toString() + " ";

                                Log.e("a1.toString().length()", a1.toString().length() + "");
                                if (a1.toString().length() < 35) {
                                    tv.append(a[n] + " ");
//                                tv.append(",");
                                    Log.e("TAG", "length = = " + a1.length() + "--------" + tv.getText().toString() + " ---------------" + n);
                                    n++;
                                    if (n == a.length) {
                                        brack1 = 1;
                                        break;
                                    }
                                } else {

                                    //  E_S  = E_S + 1;
                                    break;
                                }
                            }
                        }
                    } else {
                        int l = a[n].length();
                        tv.append(a[n].toString().substring(0, 35));
                        a[n] = a[n].substring(36, l);
                    }

                    if (n == a.length) {
                        brack1 = 1;
                        break;
                    }
                }

                tv.append("\n");
                E_S = E_S + 1;
                a1 = "";
                tv.toString().trim();

                Log.e("tv text", tv.getText().toString() + "-----------------" + tv.length());
            }
        } else {
            tv.append(s);
        }

        Log.e("text", tv.getText().toString());

        tv.setTextColor(color);
        tv.setTypeface(face);
        tv.setTextSize(50);

        view.setLayoutParams(new LinearLayout.LayoutParams(400, 200));
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        //view.layout(0, 0, tv.getWidth(), tv.getHeight());
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());


        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }
}
