/*
package com.makeit.sunnycar.drawit.listener;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

*/
/**
 * Created by gmltj on 2018-05-14.
 *//*


public class ScaleAndRotateListener {
    private DrawingContourView imageView;
    public static final float MAX_SCALE_FACTOR=5.0f;
    public static final float MIN_SCALE_FACTOR=0.2f;
    public static float scaleFactor=1;
    public static float scalePointX, scalePointY;
    public static float mPosX, mPosY;
    public static float mAngle=0;
    public int ptrCount;

    private float primSecStartTouchAngle=-1;

    private float[] primStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private float[] secStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private float primSecStartTouchDistance=0;
    private float viewScaledTouchSlop;
    private boolean dragGesture=true;
    private float tempScalePointY=-1;
    private float tempScalePointX=-1;
    private boolean isPinch;
    //private int primStartTouchPointerIndex=-1;
    //private int secStartTouchPointerIndex=-1;

    public ScaleAndRotateListener(Context context,DrawingContourView imageView) {
        final ViewConfiguration viewConfiguration=ViewConfiguration.get(context);
        viewScaledTouchSlop=viewConfiguration.getScaledTouchSlop();
        this.imageView=imageView;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {

        int action = (motionEvent.getAction()&MotionEvent.ACTION_MASK);

        //int pointerIndex=(action&MotionEvent.ACTION_POINTER_INDEX_MASK)
        //        >>MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                ptrCount++;
                if (ptrCount == 1) {
                    primStartTouchEventXY[0] = motionEvent.getX(0);
                    primStartTouchEventXY[1] = motionEvent.getY(0);

                    imageView.downToDraw(primStartTouchEventXY);

                    return false;
                    //DrawingContourView.toCanvasCoordinate(primStartTouchEventXY);

                    //dragGesture=true;

                } else if (ptrCount == 2) {
                    isPinch=true;
                    imageView.removeCurrentPen();
                    */
/*if(secStartTouchPointerIndex==-1){
                        secStartTouchPointerIndex=pointerIndex;
                        secStartTouchEventXY[0] = motionEvent.getX(secStartTouchPointerIndex);
                        secStartTouchEventXY[1] = motionEvent.getY(secStartTouchPointerIndex);
                        //DrawingContourView.toCanvasCoordinate(secStartTouchEventXY);
                    }else if(primStartTouchPointerIndex==-1){
                        primStartTouchPointerIndex=pointerIndex;
                        primStartTouchEventXY[0] = motionEvent.getX(primStartTouchPointerIndex);
                        primStartTouchEventXY[1] = motionEvent.getY(primStartTouchPointerIndex);

                    }*//*

                    secStartTouchEventXY[0] = motionEvent.getX(1);
                    secStartTouchEventXY[1] = motionEvent.getY(1);

                    primSecStartTouchDistance = distance(motionEvent, 0, 1);
                    primSecStartTouchAngle=angle(motionEvent,0,1);

                    //final float[] values=new float[]{primStartTouchEventX,primStartTouchEventY,
                    //        secStartTouchEventX,secStartTouchEventY};
                    //DrawingContourView.toCanvasCoordinate(values);
                    tempScalePointX= (primStartTouchEventXY[0]+secStartTouchEventXY[0])/2;//(values[0]+values[1])/2;
                    tempScalePointY=(primStartTouchEventXY[1]+secStartTouchEventXY[1])/2;//(values[2]+values[3])/2;
                    return true;
                    //dragGesture=false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                ptrCount--;
                if (ptrCount <2) {
                   */
/* if(pointerIndex==secStartTouchPointerIndex){
                        secStartTouchEventXY[0] = -1;
                        secStartTouchEventXY[1] = -1;
                        secStartTouchPointerIndex=-1;

                    }else if(pointerIndex==primStartTouchPointerIndex){

                        primStartTouchEventXY[0] = -1;
                        primStartTouchEventXY[1] = -1;
                        primStartTouchPointerIndex=-1;
                    }*//*

                    secStartTouchEventXY[0] = -1;
                    secStartTouchEventXY[1] = -1;
                    tempScalePointX=-1;
                    tempScalePointY=-1;

                    //return true;

                }
                if (ptrCount <1) {
                    primStartTouchEventXY[0] = -1;
                    primStartTouchEventXY[1] = -1;
                    //primStartTouchPointerIndex=-1;

                    if(ptrCount<1){
                        if(isPinch){
                            isPinch=false;
                            return true;
                        }
                        else {
                            imageView.upToDraw();
                            return false;
                        }
                    }


                }



            case MotionEvent.ACTION_MOVE:
                if(ptrCount==1&&!isPinch){
                    imageView.moveToDraw(new float[]{motionEvent.getX(0),motionEvent.getY(0)});
                    return false;
                }
                else if(ptrCount==2&&isPinch){
                    isPinchGesture(motionEvent);
                    //break;
                    return true;

                }
             */
/*   if(ptrCount==2){
                    if (!isPinchGesture(motionEvent)) {
                        break;
                    }
                }else if(ptrCount==1&&dragGesture){
                    if(!isDragGesture(motionEvent)){
                        break;

                    }
                }*//*


                //imageView.invalidate();
                break;


        }

        return false;
    }



  */
/*  private boolean isDragGesture(MotionEvent motionEvent) {

        if(motionEvent.getPointerCount()==1) {
            final float dx = (motionEvent.getX(0) - primStartTouchEventX)/scaleFactor;
            final float dy = (motionEvent.getY(0) - primStartTouchEventY)/scaleFactor;
            float[] layout = imageView.getCanvasLayout("onDrag", dx + mPosX, dy + mPosY);
            primStartTouchEventX = motionEvent.getX(0);
            primStartTouchEventY = motionEvent.getY(0);

            mPosX += dx;
            mPosY += dy;
            return true;
          *//*
*/
/*  if (DrawingContourView.MAX_POINTS[0] <= layout[0]
                    && DrawingContourView.MAX_POINTS[1] <= layout[1]
                    && DrawingContourView.MAX_POINTS[2] >= layout[2]
                    && DrawingContourView.MAX_POINTS[3] >= layout[3]) {
                mPosX += dx;
                mPosY += dy;


                return true;
            }
            //primStartTouchEventX = 0;
            //primStartTouchEventY = 0;
            Log.d("onDrag failed", "it's over max points");
            return false;*//*
*/
/*

        }else return false;

    }
*//*

    private boolean isPinchGesture(MotionEvent motionEvent){
        if(motionEvent.getPointerCount()==2&&primSecStartTouchDistance>0){
            final float angleCurrent=angle(motionEvent,0,1);
            final float tempAngle=mAngle+angleCurrent-primSecStartTouchAngle;
            primSecStartTouchAngle=angleCurrent;

            final float[] getPrimStartXY=new float[]{motionEvent.getX(0),motionEvent.getY(0)};
            final float[] getSecStartXY=new float[]{motionEvent.getX(1),motionEvent.getY(1)};

            final float distanceCurrent=distance(getPrimStartXY,getSecStartXY);
            final float diffPrimX=primStartTouchEventXY[0]-getPrimStartXY[0];
            final float diffPrimY=primStartTouchEventXY[1]-getPrimStartXY[1];
            final float diffSecX=secStartTouchEventXY[0]-getSecStartXY[0];
            final float diffSecY=secStartTouchEventXY[1]-getSecStartXY[1];


           */
/* Matrix scaledMatrix=new Matrix();
            scaledMatrix.reset();
            scaledMatrix.postScale(1/scaleFactor,1/scaleFactor,
                    scalePointX,scalePointY);
            scaledMatrix.postTranslate(-mPosX,-mPosY);
            scaledMatrix.postRotate(-mAngle,DrawingContourView.WIDTH/2,DrawingContourView.HEIGHT/2);

            final float[] values=new float[]{motionEvent.getX(0),motionEvent.getX(1),
                                    motionEvent.getY(0),motionEvent.getY(1)};
            scaledMatrix.mapPoints(values);
*//*



            final float pinchDistance=Math.abs(distanceCurrent-primSecStartTouchDistance);
            //final float tempScaleFactor=Math.max(MIN_SCALE_FACTOR,Math.min((distanceCurrent/primSecStartTouchDistance)*scaleFactor,MAX_SCALE_FACTOR));
            final float tempScaleFactor=Math.max(MIN_SCALE_FACTOR,Math.min((distanceCurrent/primSecStartTouchDistance)*scaleFactor,MAX_SCALE_FACTOR));

          //  if(diffPrimX*diffSecX>=0
          //          ||diffPrimY*diffSecY>=0){

                final float dx = (motionEvent.getX(1) - secStartTouchEventXY[0]);///Layer.scaleFactor;
                final float dy = (motionEvent.getY(1) - secStartTouchEventXY[1]);///Layer.scaleFactor;

                mPosX+=dx;
                mPosY+=dy;
                //DrawingContourView.calculateDstRect();
                secStartTouchEventXY[0] = motionEvent.getX(1);
                secStartTouchEventXY[1] = motionEvent.getY(1);
               // return true;


        //    }
        //    else
                if(pinchDistance>viewScaledTouchSlop
                    &&(diffPrimX*diffSecX)<=0
                    ||(diffPrimY*diffSecY)<=0){
                primStartTouchEventXY[0]=getPrimStartXY[0];
                primStartTouchEventXY[1]=getPrimStartXY[1];
                secStartTouchEventXY[0]=getSecStartXY[0];
                secStartTouchEventXY[1]=getSecStartXY[1];
                primSecStartTouchDistance=distanceCurrent;//distance(motionEvent,0,1);

                scalePointX=tempScalePointX;
                scalePointY=tempScalePointY;
                scaleFactor=tempScaleFactor;
                mAngle=tempAngle;

                }

            imageView.calculateDstRect();

            return true;



                //window can't over matx points
                //0530
               */
/*   float[] temp_MAX_POINTS=DrawingContourView.setCanvasMaxPoints(tempScaleFactor,tempScalePointX,tempScalePointY,tempAngle);
                 float[] layout=DrawingContourView.getCanvasLayout("onScale",
                        tempScaleFactor,tempScalePointX,tempScalePointY,tempAngle);
             if(temp_MAX_POINTS[0]>layout[0]
                        ||temp_MAX_POINTS[1]>layout[1]
                        ||temp_MAX_POINTS[2]<layout[2]
                        ||temp_MAX_POINTS[3]<layout[3]){
                    Log.d("onScale failed","it's over max points");
                    return false;
                }*//*





                //0530
               // DrawingContourView.MAX_POINTS=temp_MAX_POINTS;//DrawingContourView.setCanvasMaxPoints(scaleFactor,scalePointX,scalePointY,mAngle);



        }
        return false;
    }

    private float distance(float[] getPrimStartXY, float[] getSecStartXY) {
        final float x=getPrimStartXY[0]-getSecStartXY[0];
        final float y=getPrimStartXY[1]-getSecStartXY[1];

        return (float) Math.sqrt(x*x+y*y);
    }

    private float distance(MotionEvent motionEvent, int first, int second) {

        if(motionEvent.getPointerCount()>=2){
            final float x=motionEvent.getX(first)-motionEvent.getX(second);
            final float y=motionEvent.getY(first)-motionEvent.getY(second);

            return (float) Math.sqrt(x*x+y*y);

        }else return 0;
    }

    private float angle(MotionEvent motionEvent, int first, int second) {

        if(motionEvent.getPointerCount()>=2){
            float delta_x=motionEvent.getX(first)-motionEvent.getX(second);
            float delta_y=motionEvent.getY(first)-motionEvent.getY(second);
            double radians=Math.atan2(delta_y,delta_x);
            return (float) Math.toDegrees(radians);


        }else return primSecStartTouchAngle;
    }
}



*/
