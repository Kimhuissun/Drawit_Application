/*
package com.makeit.sunnycar.drawit.listener;

import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.makeit.sunnycar.drawit.view.DrawingContourView;

*/
/**
 * Created by gmltj on 2018-04-27.
 *//*


public class ZoomListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
    private DrawingContourView imageView;
    public static final float MAX_SCALE_FACTOR=5.0f;
    public static final float MIN_SCALE_FACTOR=0.7f;
    public static float scaleFactor=1;
    public static float scalePointX, scalePointY;
    public static float mPosX, mPosY;
    public static float mLastTouchX,mLastTouchY;
    private Context context;
    public GestureDetector gestureDetector;
    private GestureDetector.OnGestureListener onGestureListener= new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };
    private GestureDetector.OnDoubleTapListener onDoubleTapListener=new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {

            int action = motionEvent.getAction();

            final float gx=(motionEvent.getX()-scalePointX)/scaleFactor;
            final float gy=(motionEvent.getY()-scalePointY)/scaleFactor;

            Log.d("onDoubleTap ", "mPosX:" + String.valueOf(gx) + " | "
                    + "mPosY:" + String.valueOf(gy));

            if(action==MotionEvent.ACTION_DOWN){
                mLastTouchX=gx;
                mLastTouchY=gy;
            }else if(action==MotionEvent.ACTION_MOVE){
                //if(!scaleGestureDetector.isInProgress()){
                    final float dx=gx-mLastTouchX;
                    final float dy=gy-mLastTouchY;
               // mPosX+=dx; mPosY+=dy;


                //translate(fx,fy,dx,dy);

               // }
                mLastTouchX=gx;
                mLastTouchY=gy;

            }else if(action==MotionEvent.ACTION_UP){
                mLastTouchX=0;
                mLastTouchY=0;
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            Log.d("onDoubleTapEvent ","");

            return false;
        }
    };
    private boolean isFirst=true;

    public ZoomListener(Context context,DrawingContourView imageView) {
        this.context=context;
        this.imageView = imageView;
        //matrix=new Matrix();
        gestureDetector=new GestureDetector(context,onGestureListener);
        gestureDetector.setOnDoubleTapListener(onDoubleTapListener);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(detector.getScaleFactor()<0.01){
            Log.d("onScale ","getScaleFactor()<0.01");
            return false;
        }


        float[] layout=DrawingContourView.getCanvasLayout("onScale",
                scaleFactor*detector.getScaleFactor(),detector.getFocusX(),detector.getFocusY());
        if(DrawingContourView.MAX_POINTS[0]>layout[0]
                ||DrawingContourView.MAX_POINTS[1]>layout[1]
                ||DrawingContourView.MAX_POINTS[2]<layout[2]
                ||DrawingContourView.MAX_POINTS[3]<layout[3]){
            Log.d("onScale failed","it's over max points");
            return false;
        }

        scaleFactor*=detector.getScaleFactor();
        scalePointX=detector.getFocusX();
        scalePointY=detector.getFocusY();
        scaleFactor=Math.max(MIN_SCALE_FACTOR,Math.min(scaleFactor,MAX_SCALE_FACTOR));

        DrawingContourView.MAX_POINTS=new float[]{(float) (-scaleFactor*0.5*DrawingContourView.WIDTH),
                (float) (-scaleFactor*0.5*DrawingContourView.HEIGHT),
                (float) (DrawingContourView.WIDTH*(1+0.5*scaleFactor)),
                (float) (DrawingContourView.HEIGHT*(1+0.5*scaleFactor))};

    */
/*    Log.d("onScale ", "scaleFactor:" + String.valueOf(scaleFactor) + " | "
                + "scalePointX:" + String.valueOf(scalePointX)
                + "scalePointY:" + String.valueOf(scalePointY));*//*

        imageView.invalidate();

        return true;//super.onScale(detector);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return super.onScaleBegin(detector);
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        super.onScaleEnd(detector);

    }

}
*/
