/*
package com.makeit.sunnycar.drawit.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

*/
/**
 * Created by gmltj on 2018-05-20.
 *//*


public class LayerScaleAndRotateListener {
    private DrawingContourView imageView;
    public static final float MAX_SCALE_FACTOR=5.0f;
    public static final float MIN_SCALE_FACTOR=0.2f;
  */
/*  public static float scaleFactor=1;
    public static float scalePointX, scalePointY;
    public static float mPosX, mPosY;
    public static float mAngle=0;*//*


   // public static Layer layer;
    public int ptrCount;

    private float primSecStartTouchAngle=-1;
    //private float secStartTouchEventX=-1;
    //private float secStartTouchEventY=-1;

    private float[] primStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private float[] secStartTouchEventXY=new float[]{-1.0f,-1.0f};
   */
/* private float primStartTouchEventX=-1;
    private float primStartTouchEventY=-1;*//*

    private float primSecStartTouchDistance=0;
    private float viewScaledTouchSlop;
    private boolean dragGesture=true;
    private float tempScalePointY=-1;
    private float tempScalePointX=-1;

    public LayerScaleAndRotateListener(Context context, DrawingContourView imageView) {
        final ViewConfiguration viewConfiguration=ViewConfiguration.get(context);
        viewScaledTouchSlop=viewConfiguration.getScaledTouchSlop();
        this.imageView=imageView;
    }

 */
/*   public static void setLayer(Layer layer) {
        LayerScaleAndRotateListener.layer = layer;

    }*//*


    public boolean onTouchEvent(MotionEvent motionEvent) {

        int action = (motionEvent.getAction()&MotionEvent.ACTION_MASK);
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                ptrCount++;
                if (ptrCount == 1) {
                    primStartTouchEventXY[0] = motionEvent.getX(0);
                    primStartTouchEventXY[1] = motionEvent.getY(0);
                    return false;
                   // DrawingContourView.toCanvasCoordinate(primStartTouchEventXY);

                   // dragGesture=true;

                } else if (ptrCount == 2) {
                    secStartTouchEventXY[0] = motionEvent.getX(1);
                    secStartTouchEventXY[1] = motionEvent.getY(1);
                   // DrawingContourView.toCanvasCoordinate(secStartTouchEventXY);
                    primSecStartTouchDistance = distance(primStartTouchEventXY,secStartTouchEventXY);
                    primSecStartTouchAngle=angle(motionEvent);

                    //final float[] values=new float[]{primStartTouchEventXY[0],primStartTouchEventXY[1],
                    //        secStartTouchEventXY[0],secStartTouchEventXY[1]};

                    tempScalePointX= (primStartTouchEventXY[0]+primStartTouchEventXY[1])/2;//(values[0]+values[1])/2;
                    tempScalePointY=(secStartTouchEventXY[0]+secStartTouchEventXY[1])/2;//(values[2]+values[3])/2;

                //    dragGesture=false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                ptrCount--;

                if (ptrCount < 2) {
                    secStartTouchEventXY[0] = -1;
                    secStartTouchEventXY[1] = -1;
                    tempScalePointX=-1;
                    tempScalePointY=-1;
                    return true;
                }
                if (ptrCount < 1) {
                    primStartTouchEventXY[0] = -1;
                    primStartTouchEventXY[1] = -1;
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(ptrCount==2){
                    isPinchGesture(motionEvent);
                    //break;
                    return true;

                }

                */
/*
                0604
                if(ptrCount==2){
                    if (!isPinchGesture(motionEvent)) {
                        break;
                    }
                }
                else if(ptrCount==1&&dragGesture){
                    if(!isDragGesture(motionEvent)){
                        break;

                    }
                }*//*



        }
        return false;
    }



    private boolean isDragGesture(MotionEvent motionEvent) {

        if(motionEvent.getPointerCount()==1) {
            //final float[] getPrimStartXY=DrawingContourView.toCanvasCoordinate(motionEvent.getX(0),motionEvent.getY(0));

            final float dx = (motionEvent.getX(0) - primStartTouchEventXY[0])/Frame.currentFrame.currentLayer.scaleFactor;
            final float dy = (motionEvent.getY(0) - primStartTouchEventXY[1])/Frame.currentFrame.currentLayer.scaleFactor;

            //float[] layout = imageView.getCanvasLayout("onDrag", dx + layer.mPosX, dy + layer.mPosY);
         */
/*   if (DrawingContourView.MAX_POINTS[0] <= layout[0]
                    && DrawingContourView.MAX_POINTS[1] <= layout[1]
                    && DrawingContourView.MAX_POINTS[2] >= layout[2]
                    && DrawingContourView.MAX_POINTS[3] >= layout[3]) {*//*

            Frame.currentFrame.currentLayer.mPosX += dx;
            Frame.currentFrame.currentLayer.mPosY += dy;

            imageView.calculateDstRect();
            //Frame.currentFrame.drawBitmap();

            primStartTouchEventXY[0] = motionEvent.getX(0);
            primStartTouchEventXY[1] = motionEvent.getY(0);
                return true;
           // }
          */
/*  primStartTouchEventX = 0;
            primStartTouchEventY = 0;
            Log.d("onDrag failed", "it's over max points");
            return false;*//*


        }else return false;


    }

    private boolean isPinchGesture(MotionEvent motionEvent){
        if(motionEvent.getPointerCount()==2&&primSecStartTouchDistance>0){
            final float angleCurrent=angle(motionEvent);
            final float tempAngle=Frame.currentFrame.currentLayer.mAngle+angleCurrent-primSecStartTouchAngle;
            primSecStartTouchAngle=angleCurrent;

            //final float[] getPrimStartXY=DrawingContourView.toCanvasCoordinate(motionEvent.getX(0),motionEvent.getY(0));
            //final float[] getSecStartXY=DrawingContourView.toCanvasCoordinate(motionEvent.getX(1),motionEvent.getY(1));

            final float[] getPrimStartXY=new float[]{motionEvent.getX(0),motionEvent.getY(0)};
            final float[] getSecStartXY=new float[]{motionEvent.getX(1),motionEvent.getY(1)};

            final float distanceCurrent= distance(getPrimStartXY,getSecStartXY);
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
            final float tempScaleFactor=Math.max(MIN_SCALE_FACTOR,Math.min((distanceCurrent/primSecStartTouchDistance)*Frame.currentFrame.currentLayer.scaleFactor,MAX_SCALE_FACTOR));

            if(diffPrimX*diffSecX>=0
                    ||diffPrimY*diffSecY>=0){

                    final float dx = (motionEvent.getX(0) - primStartTouchEventXY[0]);///Layer.scaleFactor;
                    final float dy = (motionEvent.getY(0) - primStartTouchEventXY[1]);///Layer.scaleFactor;

                    primStartTouchEventXY[0] = motionEvent.getX(0);
                    primStartTouchEventXY[1] = motionEvent.getY(0);

                Frame.currentFrame.currentLayer.mPosX+=dx;
                Frame.currentFrame.currentLayer.mPosY+=dy;

                imageView.calculateLayerDstRect(detector.getScaleFactor());

                return true;


            }
            else if(pinchDistance>viewScaledTouchSlop
                    &&(diffPrimX*diffSecX)<=0
                    ||(diffPrimY*diffSecY)<=0){

             */
/*   //window can't over matx points
                float[] temp_MAX_POINTS=DrawingContourView.setCanvasMaxPoints(tempScaleFactor,tempScalePointX,tempScalePointY,tempAngle);
                float[] layout=DrawingContourView.getCanvasLayout("onScale",
                        tempScaleFactor,tempScalePointX,tempScalePointY,tempAngle);
                if(temp_MAX_POINTS[0]>layout[0]
                        ||temp_MAX_POINTS[1]>layout[1]
                        ||temp_MAX_POINTS[2]<layout[2]
                        ||temp_MAX_POINTS[3]<layout[3]){
                    Log.d("onScale failed","it's over max points");
                    return false;
                }*//*

                primStartTouchEventXY[0]=getPrimStartXY[0];
                primStartTouchEventXY[1]=getPrimStartXY[1];
                secStartTouchEventXY[0]=getSecStartXY[0];
                secStartTouchEventXY[1]=getSecStartXY[1];
                primSecStartTouchDistance=distanceCurrent;//distance(motionEvent,0,1);

                Frame.currentFrame.currentLayer.scalePointX=tempScalePointX;
                Frame.currentFrame.currentLayer.scalePointY=tempScalePointY;
                Frame.currentFrame.currentLayer.scaleFactor=tempScaleFactor;
                Frame.currentFrame.currentLayer.mAngle=tempAngle;
                imageView.calculateLayerDstRect(detector.getScaleFactor());
                //Frame.currentFrame.drawBitmap();

*/
/*
                DrawingContourView.MAX_POINTS=new float[]{(float) (-scaleFactor*0.5*DrawingContourView.WIDTH),
                        (float) (-scaleFactor*0.5*DrawingContourView.HEIGHT),
                        (float) (DrawingContourView.WIDTH*(1+0.5*scaleFactor)),
                        (float) (DrawingContourView.HEIGHT*(1+0.5*scaleFactor))};*//*

               // DrawingContourView.MAX_POINTS=temp_MAX_POINTS;



                return true;
            }
        }
        return false;
    }

    private float distance(float[] getPrimStartXY, float[] getSecStartXY) {
        final float x=getPrimStartXY[0]-getSecStartXY[0];
        final float y=getPrimStartXY[1]-getSecStartXY[1];

        return (float) Math.sqrt(x*x+y*y);
    }

   */
/* private float distance(MotionEvent motionEvent, int first, int second) {

        if(motionEvent.getPointerCount()>=2){
            final float x=motionEvent.getX(first)-motionEvent.getX(second);
            final float y=motionEvent.getY(first)-motionEvent.getY(second);

            return (float) Math.sqrt(x*x+y*y);

        }else return 0;
    }
*//*

    private float angle(MotionEvent motionEvent) {

        if(motionEvent.getPointerCount()>=2){
            float delta_x=motionEvent.getX(0)-motionEvent.getX(1);
            float delta_y=motionEvent.getY(0)-motionEvent.getY(1);
            double radians=Math.atan2(delta_y,delta_x);
            return (float) Math.toDegrees(radians);


        }else return primSecStartTouchAngle;
    }
}
*/
