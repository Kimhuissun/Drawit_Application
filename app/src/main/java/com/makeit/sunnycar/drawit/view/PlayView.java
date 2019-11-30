/*
package com.makeit.sunnycar.drawit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.makeit.sunnycar.removevideobackground.R;
import com.makeit.sunnycar.drawit.adapter.LayerAdapter;

*/
/**
 * Created by gmltj on 2018-05-23.
 *//*


public class PlayView extends SurfaceView implements Runnable,SurfaceHolder.Callback  {

    public static final int MAX_FPS = 30;
    public static final int MIN_FPS = 1;
    private static int WIDTH,HEIGHT;
    private Context context;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private boolean playing=true;
    private long timeThisFrame;
    private Canvas canvas;
    private int FRAME_COUNT;
    //private Bitmap currentBitmap;
    private int currentFrame;
    private long lastFrameChangTime;
    public static long frameLengthInMilliSeconds=100;
    private Thread animThread=null;
    private Paint whitePaint;
    private float ratio;
    private float translateY;
    //private RectF whereTodraw;

    public PlayView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        paint=new Paint(Paint.DITHER_FLAG);
        whitePaint=new Paint();
        whitePaint.setColor(getResources().getColor(R.color.white));

        //setZOrderOnTop(true);
        FRAME_COUNT=LayerAdapter.frames.size();
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs); init(context);
    }

    public PlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init(context);
    }

    public PlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes); init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right ,bottom);
        WIDTH=right-left;
        HEIGHT=bottom-top;
        float ratioWidth=(float)WIDTH/DrawingContourView.WIDTH;
        float ratioHeight=(float)HEIGHT/DrawingContourView.HEIGHT;

        ratio=ratioWidth<ratioHeight? ratioWidth:ratioHeight;
        translateY=(HEIGHT/ratio-DrawingContourView.HEIGHT)/2;
        //whereTodraw=new RectF(0,0,D,HEIGHT);
    }


    @Override
    public void run() {
        while (playing){
            //long startFrameTime=System.currentTimeMillis();
            draw();

            //timeThisFrame = System.currentTimeMillis() - startFrameTime;
            //if (timeThisFrame >= 1) {
            //    fps = 1000 / timeThisFrame;
            //}

        }
    }

    private void draw() {

        canvas=surfaceHolder.lockCanvas();
        try {
            synchronized (surfaceHolder){
                getCurrentFrame();
                if(canvas!=null){
                    canvas.drawColor(Color.LTGRAY);//,PorterDuff.Mode.CLEAR);
                    canvas.scale(ratio,ratio,0,0);
                    canvas.translate(0,translateY);
                    canvas.drawRect(0,0,DrawingContourView.WIDTH,DrawingContourView.HEIGHT,whitePaint);

                    canvas.drawBitmap(LayerAdapter.frames.get(currentFrame).layersBitmap,
                            0,0,paint);
                }
            }
        }finally {
            if(canvas!=null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void getCurrentFrame() {
        long time=System.currentTimeMillis();
        if(time-lastFrameChangTime>frameLengthInMilliSeconds){
            lastFrameChangTime=time;
            currentFrame++;

        }
        if(currentFrame>=FRAME_COUNT){
            currentFrame=0;
        }
        */
/*if(currentBitmap!=null&&!currentBitmap.isRecycled())
            currentBitmap.recycle();*//*

        //currentBitmap= LayerAdapter.frames.get(currentFrame).layersBitmap;

    }

    public void pause(){
      //  if(playing){
        boolean retry=true;
        playing=false;
        while (retry){
            try {
                animThread.join();
                retry=false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //}

    }

    public void resume(){
        playing=true;
        animThread=new Thread(this);
        animThread.start();
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        resume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //resume();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        pause();
    }

}
*/
