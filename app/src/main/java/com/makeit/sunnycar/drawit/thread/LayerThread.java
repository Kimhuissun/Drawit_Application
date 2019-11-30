/*
package com.makeit.sunnycar.drawit.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.handler.LayerHandler;

public class LayerThread extends Thread{
    public static final String TOUCH_XY = "touched_x_y";

    public static final int CANVAS_DST_LECT = 0 ;
    public static final int REMOVE_CURRENT_PATH = 1;
    public static final int REDRAW_LAYER = 2;
    public static final int SET_BEFORE_BITMAP = 3;
    public static final int UP_TO_DRAW = 4;
    public static final int MOVE_TO_DRAW = 5;
    public static final int ON_TOUCH_EVENT = 6;

    private LayerHandler layerHandler;
    private LayerAdapter layerAdapter;
    public LayerThread(LayerAdapter layerAdapter) {
        this.layerAdapter=layerAdapter;
    }

    @Override
    public void run() {
        super.run();
       // Looper.prepare();
        layerHandler=new LayerHandler(Looper.getMainLooper());

       // Looper.loop();
    }

    public void sendMessage(int what) {
        
        final Message msg = new Message();
        msg.what = what;
        layerHandler.sendMessage(msg);
    }

    public void sendMessage(int what, float[] primStartTouchEventXY) {
        final Message msg = new Message();
        msg.what = what;
        Bundle bundle=new Bundle();
        bundle.putFloatArray(TOUCH_XY,primStartTouchEventXY);
        msg.setData(bundle);
        layerHandler.sendMessage(msg);
    }
}
*/
