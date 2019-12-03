package com.makeit.sunnycar.drawit.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by gmltj on 2018-04-25.
 */

public class MakeContours extends Handler {
    public static final int MSG_MAKE_CONTOURS=1;
    public static final String TOUCHED_X="x";
    public static final String TOUCHED_Y="y";

    public void handleMessage(Message message){
        switch (message.what){
            case MSG_MAKE_CONTOURS:
                Bundle bundle=message.getData();
                addContours(bundle.getInt(TOUCHED_X),bundle.getInt(TOUCHED_Y));

        }
    }

    private void addContours(int x, int y) {


    }

}
