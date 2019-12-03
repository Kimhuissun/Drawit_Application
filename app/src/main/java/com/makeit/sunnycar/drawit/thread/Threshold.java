package com.makeit.sunnycar.drawit.thread;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.makeit.sunnycar.drawit.handler.ThreshHandler;


/**
 * Created by gmltj on 2018-04-23.
 */

public class Threshold extends Thread {
    private final ImageButton[] photoButtons;
    private final SeekBar kernerBar;

    private RelativeLayout relativeLayout;
    private ImageView imageView;

    private Context context;
    private int thickness;
    private boolean isBlackCountStarted;
    private ThreshHandler threshHandler;

    public Threshold(RelativeLayout relativeLayout, ImageView borderView, ImageButton[] photoButtons, SeekBar kernelBar, Context context) {
        this.relativeLayout = relativeLayout;

        this.imageView = borderView;
        this.context = context;
        this.photoButtons=photoButtons;
        this.kernerBar=kernelBar;

    }

    @Override
    public void run() {
        Looper.prepare();
        //Handler handler=new Handler();
        threshHandler=new ThreshHandler(relativeLayout,photoButtons,kernerBar,imageView,context);

        Looper.loop();
    }


    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void sendMessage() {
        Message message=new Message();
        threshHandler.sendMessage(message);
    }

    public void setForDraw(boolean forDraw) {
        threshHandler.setForDraw(forDraw);
    }

    public void setProgressBar(ProgressBar progressBar) {
        threshHandler.setProgressBar(progressBar);
    }

}
