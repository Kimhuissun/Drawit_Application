package com.makeit.sunnycar.drawit.listener;

import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.makeit.sunnycar.drawit.data.Frame;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class ResizeListener {

    private final Context context;
    private final RectF CANVAS_DST_RECT;

    private int mFirstActivePointerId=-1;
    private int mSecActivePointerId;

    private float[] primStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private float[] secStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private ScaleGestureDetector scaleGestureDetector;
    private int ptrCount=0;
    private float primSecStartTouchAngle=-1;
    private float diffSecX,diffSecY=0;


    public ResizeListener(RectF CANVAS_DST_RECT, Context context) {
        this.context=context;
        this.CANVAS_DST_RECT = CANVAS_DST_RECT;
        scaleGestureDetector=new ScaleGestureDetector(context,new ScaleListener());
    }

    public void onTouchEvent(MotionEvent motionEvent){

        scaleGestureDetector.onTouchEvent(motionEvent);
        final int action = (motionEvent.getAction()&MotionEvent.ACTION_MASK);
        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                ptrCount++;
                if(ptrCount==1){
                    final int pointerIndex = motionEvent.getActionIndex();
                    final float x = motionEvent.getX(pointerIndex);
                    final float y = motionEvent.getY(pointerIndex);
                    primStartTouchEventXY[0] = x;//motionEvent.getX(0);
                    primStartTouchEventXY[1] = y;//motionEvent.getY(0);

                    mFirstActivePointerId = motionEvent.getPointerId(pointerIndex);
                }

                if(ptrCount==2){
                    primSecStartTouchAngle=angle(motionEvent);
                    final int pointerIndex = motionEvent.getActionIndex();
                    final float x=motionEvent.getX(pointerIndex);
                    final float y=motionEvent.getY(pointerIndex);

                    secStartTouchEventXY[0] = x ;
                    secStartTouchEventXY[1] = y;
                    mSecActivePointerId = motionEvent.getPointerId(pointerIndex);

                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if(ptrCount==1) {
                    final int pointerIndex = motionEvent.findPointerIndex(mFirstActivePointerId);
                    final float x = motionEvent.getX(pointerIndex);
                    final float y = motionEvent.getY(pointerIndex);

                    final float dx = x - primStartTouchEventXY[0];
                    final float dy = y - primStartTouchEventXY[1];

                    Frame.currentFrame.currentLayer.mPosX += dx;
                    Frame.currentFrame.currentLayer.mPosY += dy;

                    primStartTouchEventXY[0] = x;
                    primStartTouchEventXY[1] = y;
                }
                else if(ptrCount==2&&
                        motionEvent.getPointerCount()==2){
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

                        }

                    }

                        final float angleCurrent=angle(motionEvent);
                        Frame.currentFrame.currentLayer.mAngle+=angleCurrent-primSecStartTouchAngle;
                        primSecStartTouchAngle=angleCurrent;


                    primStartTouchEventXY[0] = primX;
                    primStartTouchEventXY[1] = primY;
                    secStartTouchEventXY[0] = secX;
                    secStartTouchEventXY[1] = secY;


                }
                calculateLayerDstRect(1.0f);

                break;
            }
            case MotionEvent.ACTION_UP:{
                ptrCount=0;//no pointer

                mFirstActivePointerId=INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                mFirstActivePointerId=INVALID_POINTER_ID;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                ptrCount--;
                mSecActivePointerId=INVALID_POINTER_ID;

                final int pointerIndex=motionEvent.getActionIndex();
                final int pointerId=motionEvent.getPointerId(pointerIndex);
                if(pointerId==mFirstActivePointerId){
                    final int newPointerIndex=pointerIndex==0?1:0;
                    primStartTouchEventXY[0]=motionEvent.getX(newPointerIndex);
                    primStartTouchEventXY[1]=motionEvent.getY(newPointerIndex);
                    mFirstActivePointerId=motionEvent.getPointerId(newPointerIndex);
                }
                break;
            }
        }

    }
    void calculateLayerDstRect(float scaleFactor){
        Frame.currentFrame.setLayerDstRect(scaleFactor);

    }
    private float angle(MotionEvent motionEvent) {

        if(motionEvent.getPointerCount()>=2){
            float delta_x=motionEvent.getX(0)-motionEvent.getX(1);
            float delta_y=motionEvent.getY(0)-motionEvent.getY(1);
            double radians=Math.atan2(delta_y,delta_x);
            return (float) Math.toDegrees(radians);


        }else return primSecStartTouchAngle;
    }
    private class ScaleListener
    extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
             return true;//super.onScaleBegin(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            calculateLayerDstRect(detector.getScaleFactor());
            return true;
    }
    }
}
