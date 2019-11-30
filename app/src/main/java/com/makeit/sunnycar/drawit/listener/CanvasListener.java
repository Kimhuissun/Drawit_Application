/*
package com.makeit.sunnycar.drawit.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

import org.opencv.core.Mat;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class CanvasListener {
    public static final float MAX_SCALE_FACTOR=5.0f;
    public static final float MIN_SCALE_FACTOR=0.2f;
    public static float scaleFactor=1;
    public static float scalePointX, scalePointY;
    public static float mPosX, mPosY;
    public static float mAngle=0;
    private final Context context;
    private final int viewScaledTouchSlop;
    private final ScaleGestureDetector scaleGestureDetector;

    //private ScaleGestureDetector scaleGestureDetector;
    private DrawingContourView drawingContourView;
    //private int ptrCount;
    private int mFirstActivePointerId=-1;
    private float[] primStartTouchEventXY=new float[]{-1.0f,-1.0f};
   */
/* private float mLastTouchY=-1;
    private float mLastTouchX=-1;*//*

    private float[] secStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private int mSecActivePointerId;
    private boolean isPinch=false;
    ///private boolean isTranslated=false;

    private float primDistance=1.0f;
    private int ptrCount=0;
    private boolean isTranslated=false;
    private float diffSecX,diffSecY=0;
    //private float mLastX,mLastY;

    public CanvasListener(DrawingContourView drawingContourView, Context context) {
        this.context=context;
        this.drawingContourView = drawingContourView;
        scaleGestureDetector=new ScaleGestureDetector(context,new CanvasListener.ScaleListener());
        final ViewConfiguration viewConfiguration=ViewConfiguration.get(context);

        viewScaledTouchSlop=viewConfiguration.getScaledTouchSlop();

    }

    public boolean onTouchEvent(MotionEvent motionEvent){

        scaleGestureDetector.onTouchEvent(motionEvent);
        final int action = (motionEvent.getAction()&MotionEvent.ACTION_MASK);
        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                ptrCount++;
                if(ptrCount==1){
                    isPinch=false;
                    final int pointerIndex = motionEvent.getActionIndex();
                    primStartTouchEventXY[0] = motionEvent.getX(pointerIndex);
                    primStartTouchEventXY[1] = motionEvent.getY(pointerIndex);
                    mFirstActivePointerId = motionEvent.getPointerId(pointerIndex);

                    drawingContourView.downToDraw(primStartTouchEventXY);
                    return false;
                }else if(ptrCount==2){
                    drawingContourView.removeCurrentPen();

                    final int pointerIndex = motionEvent.getActionIndex();
                    final float x=motionEvent.getX(pointerIndex);
                    final float y=motionEvent.getY(pointerIndex);

                    secStartTouchEventXY[0] = x ;
                    secStartTouchEventXY[1] = y;
            */
/*mLastX=x;
            mLastY=y;*//*

                    mSecActivePointerId = motionEvent.getPointerId(pointerIndex);

                    primDistance=distance(primStartTouchEventXY,secStartTouchEventXY);
                    isPinch=true;
                    return true;
                    //dragGesture=false;
                }

            case MotionEvent.ACTION_MOVE:{
                //int ptrCount=motionEvent.getPointerCount();
                if(ptrCount==2&&
                        motionEvent.getPointerCount()==2&&isPinch){

                    float primX=-1,primY=-1,secX=-1,secY=-1,diffPrimX=-1,diffPrimY=-1;//diffSecX=-1,diffSecY=-1;//,dx=-1,dy=-1;
                    for(int i = 0; i<ptrCount; i++){
                        final int pointerId=motionEvent.getPointerId(i);
                        if(pointerId==mFirstActivePointerId){
                            final int primPointerIndex = motionEvent.findPointerIndex(mFirstActivePointerId);
                            primX = motionEvent.getX(primPointerIndex);
                            primY = motionEvent.getY(primPointerIndex);
                            diffPrimX = primX - primStartTouchEventXY[0];
                            diffPrimY = primY - primStartTouchEventXY[1];


                        }else if(pointerId==mSecActivePointerId){
                            final int secPointerIndex = motionEvent.findPointerIndex(mSecActivePointerId);
                            secX = motionEvent.getX(secPointerIndex);
                            secY = motionEvent.getY(secPointerIndex);
                            diffSecX=secX-secStartTouchEventXY[0];
                            diffSecY=secY-secStartTouchEventXY[1];
                            //dx=secX-mLastX;
                            //dy=secY-mLastY;

                        }
                    }

                    //double primRadians=Math.atan2(diffPrimX,diffPrimY);
                    //double secRadians=Math.atan2(diffSecX,diffSecY);
                    //double angle=Math.toDegrees(Math.abs(primRadians-secRadians));
                    final float distanceCurrent=distance(primX,primY,secX,secY);
                    //final float pinchDistance=Math.abs(distanceCurrent-primDistance);

                    if(diffPrimX*diffSecX>=0
                            ||diffPrimY*diffSecY>=0){
                        //drawingContourView.setCanvasDstRect(diffSecX,diffSecY);
                        mPosX += diffSecX;
                        mPosY += diffSecY;
                        drawingContourView.calculateDstRect();
                    }
                    */
/*else if(pinchDistance>viewScaledTouchSlop
                            &&diffPrimX*diffSecX<0
                            ||diffPrimY*diffSecY<0){
                        scalePointX=(primX+secX)/2;
                        scalePointY=(secY+primY)/2;
                        final float tempScaleFactor=Math.max(MIN_SCALE_FACTOR,Math.min((distanceCurrent/primDistance)*scaleFactor,MAX_SCALE_FACTOR));
                        scaleFactor=tempScaleFactor;

                    }*//*


                    primStartTouchEventXY[0] = primX;
                    primStartTouchEventXY[1] = primY;
                    secStartTouchEventXY[0] = secX;
                    secStartTouchEventXY[1] = secY;
                    primDistance=distanceCurrent;

                    return true;
                }else if(ptrCount==1&&
                        motionEvent.getPointerCount()==1&&!isPinch){

                    final int pointerIndex = motionEvent.findPointerIndex(mFirstActivePointerId);

                    final float x = motionEvent.getX(pointerIndex);
                    final float y = motionEvent.getY(pointerIndex);

                    drawingContourView.moveToDraw(new float[]{x,y});
                    return false;

                }
            }
            case MotionEvent.ACTION_UP:{
                ptrCount=0;
                mFirstActivePointerId = INVALID_POINTER_ID;
                if(isPinch) {
                    isPinch=false;
                    return true;
                }
                else {
                    drawingContourView.upToDraw();
                    return false;
                }
            }
            case MotionEvent.ACTION_POINTER_UP:{
                ptrCount--;
                mSecActivePointerId=INVALID_POINTER_ID;
                // isPinch=false;
                final int pointerIndex = motionEvent.getActionIndex();
                final int pointerId = motionEvent.getPointerId(pointerIndex);
                if (pointerId == mFirstActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    primStartTouchEventXY[0] = motionEvent.getX(newPointerIndex);
                    primStartTouchEventXY[1] = motionEvent.getY(newPointerIndex);
                    mFirstActivePointerId = motionEvent.getPointerId(newPointerIndex);
                }
                return true;
            }

            default: return true;
        }
    }

    private float distance(float primX, float primY, float secX, float secY) {
        final float x=primX-secX;
        final float y=primY-secY;

        return (float) Math.sqrt(x*x+y*y);
    }

    private float distance(float[] primStartTouchEventXY, float[] secStartTouchEventXY) {
        final float x=primStartTouchEventXY[0]-primStartTouchEventXY[1];
        final float y=secStartTouchEventXY[0]-secStartTouchEventXY[1];

        return (float) Math.sqrt(x*x+y*y);
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener{

    */
/*    @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {


            return true;//super.onScaleBegin(detector);
        }*//*


        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            if(isPinch) {
                //final  float oldScaleFactor=scaleFactor;
                final float getScaleFactor=detector.getScaleFactor();
                //final float mFocusX=detector.getFocusX();
                //final float mFocusY=detector.getFocusY();
                if(getScaleFactor>0.95
                        &&getScaleFactor<1.05)
                    return true;


                //canvas 크기는 계속 변하고 있기 때문에 사용자가 보는 좌표를 계산해줘야 함
                float[] center=new float[]{detector.getFocusX(),detector.getFocusY()};
                DrawingContourView.toCanvasCoordinate(center);

                scalePointX=center[0];
                scalePointY=center[1];
                scaleFactor *= getScaleFactor;
                scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

               */
/* float dx=mFocusX-scalePointX;
                float dy=mFocusY-scalePointY;

                float dxSc=dx*scaleFactor/oldScaleFactor;
                float dySc=dy*scaleFactor/oldScaleFactor;
                scalePointX=mFocusX-dxSc;
                scalePointY=mFocusY-dySc;*//*




                drawingContourView.calculateDstRect();
                //drawingContourView.setCanvasDstRect(scalePointX,scalePointY,Math.max(0.1f, Math.min(getScaleFactor, 5.0f)));
            }
            return true;//super.onScale(detector);
        }
    }
}
*/
