/*
package com.makeit.sunnycar.drawit.listener;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.view.BorderView;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

import org.opencv.core.Mat;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class BorderListener {
    public static final float MAX_SCALE_FACTOR=5.0f;
    public static final float MIN_SCALE_FACTOR=0.2f;

    public float bitmapX,bitmapY;
    public float scaleFactor=1;
    public float scalePointX, scalePointY;
    public float mPosX, mPosY;
    public float mAngle=0;
    private final Context context;
    private int mActivePointerId=INVALID_POINTER_ID;
    private float mLastTouchY=-1;
    private float mLastTouchX=-1;
    private ScaleGestureDetector scaleGestureDetector;
    private BorderView borderView;

    public BorderListener(Context context, BorderView borderView) {
        this.context=context;
        this.borderView=borderView;
        scaleGestureDetector=new ScaleGestureDetector(context,new ScaleListener());
    }

    public void onTouchEvent(MotionEvent motionEvent){

        //scaleGestureDetector.onTouchEvent(motionEvent);
        final int action = (motionEvent.getAction()&MotionEvent.ACTION_MASK);
        switch (action){
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = motionEvent.getActionIndex();
                final float x = motionEvent.getX(pointerIndex);
                final float y = motionEvent.getY(pointerIndex);

                mLastTouchX = x;
                mLastTouchY = y;

                mActivePointerId = motionEvent.getPointerId(pointerIndex);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                final int pointerIndex=motionEvent.findPointerIndex(mActivePointerId);
                final float x = motionEvent.getX(pointerIndex);
                final float y = motionEvent.getY(pointerIndex);

                final float dx=x-mLastTouchX;
                final float dy=y-mLastTouchY;
                bitmapX+=dx; bitmapY+=dy;
                translateBitmap();

                borderView.invalidate();
                */
/*if(borderView.BORDER_DST_RECT.contains((int)x,(int)y)){
                    bitmapX+=dx; bitmapY+=dy;
                    translateBitmap();
                }else if(borderView.RED_PAINT_BORDER.contains((int)x,(int)y)){
                    mPosX+=dx;
                    mPosY+=dy;
                    calculateRedBorderDstRect();
                }*//*



                mLastTouchX=x;
                mLastTouchY=y;
                break;
            }
            case MotionEvent.ACTION_UP:{
                mActivePointerId=INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                mActivePointerId=INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                final int pointerIndex=motionEvent.getActionIndex();
                final int pointerId=motionEvent.getPointerId(pointerIndex);
                if(pointerId==mActivePointerId){
                    final int newPointerIndex=pointerIndex==0?1:0;
                    mLastTouchX=motionEvent.getX(newPointerIndex);
                    mLastTouchY=motionEvent.getY(newPointerIndex);
                    mActivePointerId=motionEvent.getPointerId(newPointerIndex);
                }
                break;
            }
        }

    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Frame.currentFrame.currentLayer.scalePointX=detector.getFocusX();
            Frame.currentFrame.currentLayer.scalePointY=detector.getFocusY();
            return true;//super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor*=detector.getScaleFactor();
            scaleFactor= Math.max(MIN_SCALE_FACTOR,Math.min(scaleFactor,MAX_SCALE_FACTOR));
            calculateRedBorderDstRect();
            return true;//super.onScale(detector);
        }
    }
    private void translateBitmap() {
        if(GetImage.imageViewBitmap!=null){
            float[] layout=new float[]{0.0f,0.0f,GetImage.imageViewBitmap.getWidth(),GetImage.imageViewBitmap.getHeight() };
            Matrix scaledMatrix=new Matrix();
            scaledMatrix.reset();
            scaledMatrix.postTranslate(bitmapX,bitmapY);
            scaledMatrix.mapPoints(layout);
          */
/*  if(borderView.RED_PAINT_BORDER.contains((int) layout[0], (int) layout[1])
                    ||borderView.RED_PAINT_BORDER.contains((int) layout[0], (int) layout[3])
                    ||borderView.RED_PAINT_BORDER.contains((int) layout[2], (int) layout[1])
                    ||borderView.RED_PAINT_BORDER.contains((int) layout[2], (int) layout[3])){
                return;
            }*//*

            borderView.BORDER_DST_RECT.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);
            borderView.invalidate();
        }
    }

    private void calculateRedBorderDstRect() {
        float[] layout=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};
        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();

        //scaledMatrix.postRotate(Layer.mAngle,Layer.scalePointX,Layer.scalePointY);
        scaledMatrix.postScale(scaleFactor,scaleFactor,
                scalePointX,scalePointY);
        scaledMatrix.postTranslate(mPosX,mPosY);
        scaledMatrix.mapPoints(layout);
        borderView.RED_PAINT_BORDER.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);
        borderView.invalidate();
    }
}
*/
