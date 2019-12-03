package com.makeit.sunnycar.drawit.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.makeit.sunnycar.drawit.data.Frame;


public class LayerHandler extends Handler {



    private Context context;


    public LayerHandler(Looper looper) {
        super(looper);
    }


    private void init(Context context) {
        this.context=context;



    }

    private void redrawOnBeforeCanvas() {

        Frame.currentFrame.redrawOnBeforeCanvas();

    }

    private void removeCurrentPen(){
        Frame.currentFrame.removeCurrentPen();
    }


}
