package com.makeit.sunnycar.drawit.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by gmltj on 2018-05-14.
 */

public class RecyclerViewListener {

    public interface OnStartDragListener{
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
    public interface ItemTouchHelperViewHolder{
        void onItemSelected();
        void onitemClear();
    }

    public interface ItemTouchHelperAdapter{
        boolean onItemMove(int fromPosition,int toPosition);
        void onItemDismiss(int position);
    }
}
