package com.creativetechnologies.slideshows.videos.songs.videomaker.dragrecyclerview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativetechnologies.slideshows.videos.songs.videomaker.MyApplication;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.ImageData;

import java.util.ArrayList;

/**
 * Created by Shinhyo
 */
public abstract class DragAdapter extends RecyclerView.Adapter implements ImpAdapter, OnDragListener {

    private final Context mContext;
    private ArrayList<ImageData> mData;
    private boolean isHandleDragEnabled = true;
    private OnDragListener mDragListener;
    private DragRecyclerView mRecyclerView;
    private MyApplication application = MyApplication.getInstance();

    public DragAdapter(Context context) {
        mContext = context;
//        mData = data;
    }

    @Override
    public void onBindViewHolder(DragRecyclerView.ViewHolder hol, int position) {
        final DragHolder holder = (DragHolder) hol;

        View handle = holder.getHandle();
        if (handle == null || !isHandleDragEnabled) {
            return;
        }
        handle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mRecyclerView.getItemTouchHelper().startDrag(holder);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return application.getSelectedImages().size();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {

//        Collections.swap(getData(), fromPosition, toPosition);

        if (mDragListener != null) {
            mDragListener.onMove(fromPosition, toPosition);
        }

        notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onSwiped(int position) {
        application.getSelectedImages().remove(position);
        application.getOrgSelectedImages().remove(position);
        application.getTempOrgSelectedImages().remove(position);
        notifyItemRemoved(position);

        if (mDragListener != null) {
            mDragListener.onSwiped(position);
        }
    }

    @Override
    public void onDrop(int fromPosition, int toPosition) {

        ArrayList<ImageData> list = getData();
        ArrayList<ImageData> list1 = getData1();
        ArrayList<ImageData> list2 = getData2();
        list.add(toPosition, list.remove(fromPosition));
        list1.add(toPosition, list1.remove(fromPosition));
        list2.add(toPosition, list2.remove(fromPosition));

        if (mDragListener != null) {
            mDragListener.onDrop(fromPosition, toPosition);
        }
    }

    public ArrayList<ImageData> getData() {
        return application.getSelectedImages();
    }

    public ArrayList<ImageData> getData1() {
        return application.getOrgSelectedImages();
    }

    public ArrayList<ImageData> getData2() {
        return application.getTempOrgSelectedImages();
    }

    public void setOnItemDragListener(OnDragListener dragListener) {
        mDragListener = dragListener;
    }

    public void setOnItemClickListener(OnClickListener clickListener) {
        DragHolder.mClickListener = clickListener;
    }

    public void setHandleId(int handleId) {
        DragHolder.mHandleId = handleId;
    }

    public void setHandleDragEnabled(boolean dragEnabled) {
        isHandleDragEnabled = dragEnabled;
    }

    public void setRecycleView(DragRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setLongPressDragEnabled(boolean set) {
        mRecyclerView.getTouchHelperCallback().setLongPressDragEnabled(set);
    }

    public void setSwipeEnabled(boolean set) {
        mRecyclerView.getTouchHelperCallback().setItemViewSwipeEnabled(set);
    }
}
