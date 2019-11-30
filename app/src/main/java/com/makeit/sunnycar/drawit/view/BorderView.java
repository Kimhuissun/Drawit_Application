/*
package com.makeit.sunnycar.drawit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.makeit.sunnycar.removevideobackground.R;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.listener.BorderListener;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.thread.Threshold;

public class BorderView extends View{


    public static Rect BORDER_DST_RECT=new Rect();
    private int WINDOW_WIDTH,WINDOW_HEIGHT;
    private static float[] CENTER_FACTOR=new float[3];
    private Paint paint,redPaint;
    private BorderListener borderListener;
    public Rect RED_PAINT_BORDER=new Rect();
    private Object context;
    //public float[] CANVAS_RECT;

    public BorderView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        paint=new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setFlags(Paint.DITHER_FLAG|Paint.FILTER_BITMAP_FLAG);

        redPaint=new Paint();
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(10.0f);
        borderListener=new BorderListener(context,this);
        //CANVAS_RECT=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};

    }

    public BorderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);  init(context);
    }

    public BorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);  init(context);
    }

    public BorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);  init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        WINDOW_WIDTH=right-left;
        WINDOW_HEIGHT=bottom-top;
        DrawingContourView.setCanvasToCenterFactor(WINDOW_WIDTH,WINDOW_HEIGHT,CENTER_FACTOR);
        if(CENTER_FACTOR[2]==1){
            RED_PAINT_BORDER.set(
                    0,
                    (int) CENTER_FACTOR[1],
                    (int) (DrawingContourView.WIDTH*CENTER_FACTOR[0]),
                    (int) (DrawingContourView.HEIGHT*CENTER_FACTOR[0]+CENTER_FACTOR[1]));

        }else {
            RED_PAINT_BORDER.set(
                    (int) CENTER_FACTOR[1],
                    0,
                    (int) (DrawingContourView.WIDTH*CENTER_FACTOR[0]+CENTER_FACTOR[1]),
                    (int) (DrawingContourView.HEIGHT*CENTER_FACTOR[0]));

        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
       */
/* canvas.scale(CENTER_FACTOR[0],CENTER_FACTOR[0],0,0);
        if(CENTER_FACTOR[2]==1)
            canvas.translate(0,CENTER_FACTOR[1]);
        else
            canvas.translate(CENTER_FACTOR[1],0);*//*


       */
/* if(ThreshHandler.METHOD==ThreshHandler.PHOTO_OUTLINE
                &&ThreshHandler.FINAL_IMAGE!=null) {
            canvas.drawBitmap(ThreshHandler.FINAL_IMAGE,null,BORDER_DST_RECT, paint);
        }else *//*

       if(GetImage.imageViewBitmap!=null){
            canvas.drawBitmap(GetImage.imageViewBitmap,null,BORDER_DST_RECT, paint);
        }
        canvas.drawRect(RED_PAINT_BORDER,redPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        borderListener.onTouchEvent(event);
        return true;
    }

    public void cropBorder(){
        Bitmap bitmap= Bitmap.createBitmap(RED_PAINT_BORDER.width(),RED_PAINT_BORDER.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        canvas.drawBitmap(GetImage.imageViewBitmap,(int)(borderListener.bitmapX-RED_PAINT_BORDER.left), (int)(borderListener.bitmapY-RED_PAINT_BORDER.top),paint);
        //Threshold.FINAL_IMAGE=Bitmap.createScaledBitmap(bitmap,DrawingContourView.WIDTH,DrawingContourView.HEIGHT,true);
        GetImage.imageViewBitmap=bitmap;

    }
   */
/* public void resetBitmapForDraw() {
       // RED_PAINT_BORDER.offset((int)(borderListener.bitmapX), (int)(borderListener.bitmapY));
        *//*
*/
/*if(ThreshHandler.METHOD==ThreshHandler.PHOTO_OUTLINE){

            Bitmap bitmap= Bitmap.createBitmap(RED_PAINT_BORDER.width(),RED_PAINT_BORDER.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
            canvas.drawBitmap(ThreshHandler.FINAL_IMAGE,(int)(borderListener.bitmapX-RED_PAINT_BORDER.left), (int)(borderListener.bitmapY-RED_PAINT_BORDER.top),paint);
            //Threshold.FINAL_IMAGE=Bitmap.createScaledBitmap(bitmap,DrawingContourView.WIDTH,DrawingContourView.HEIGHT,true);
            ThreshHandler.FINAL_IMAGE=bitmap;


        }else if(ThreshHandler.METHOD==ThreshHandler.ORIGINAL_PHOTO){
            Bitmap bitmap= Bitmap.createBitmap(RED_PAINT_BORDER.width(),RED_PAINT_BORDER.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
            canvas.drawBitmap(GetImage.imageViewBitmap,(int)(borderListener.bitmapX-RED_PAINT_BORDER.left), (int)(borderListener.bitmapY-RED_PAINT_BORDER.top),paint);
            //GetImage.imageViewBitmap=Bitmap.createScaledBitmap(bitmap,DrawingContourView.WIDTH,DrawingContourView.HEIGHT,true);
            GetImage.imageViewBitmap=bitmap;

        }*//*
*/
/*
        Frame.currentFrame.addSketch();
        if(ThreshHandler.FINAL_IMAGE!=null&&!ThreshHandler.FINAL_IMAGE.isRecycled()){
            ThreshHandler.FINAL_IMAGE.recycle();
            ThreshHandler.FINAL_IMAGE=null;
        }
        if(GetImage.imageViewBitmap!=null&&!GetImage.imageViewBitmap.isRecycled()){
            GetImage.imageViewBitmap.recycle();
            GetImage.imageViewBitmap=null;
        }
        ((Activity)context).finish();

    }*//*

}
*/
