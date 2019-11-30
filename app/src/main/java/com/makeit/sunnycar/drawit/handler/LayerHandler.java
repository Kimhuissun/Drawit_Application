package com.makeit.sunnycar.drawit.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.makeit.sunnycar.drawit.data.Frame;


public class LayerHandler extends Handler {


    //private LayerAdapter layerAdapter;
    private Context context;
   /* private Paint drawingPaint;
    private Path currentPath;*/
   // private boolean DRAWING_PROCESS=false;
    /*private float mX;
    private float mY;*/

    public LayerHandler(Looper looper) {
        super(looper);
    }


    private void init(Context context) {
        this.context=context;
        //currentPath=new Path();


    }
 /*   public void handleMessage(Message message) {

        if (message.what == LayerThread.CANVAS_DST_LECT) {
            calculateDstRect();
        } else if (message.what == LayerThread.REMOVE_CURRENT_PATH) {
            removeCurrentPen();
        }else if (message.what == LayerThread.REDRAW_LAYER) {
            redrawOnBeforeCanvas();
        }else if (message.what == LayerThread.SET_BEFORE_BITMAP) {
            //setBeforeCanvas(message);
        }else if (message.what == LayerThread.UP_TO_DRAW) {
            //upToDraw();
        }else if (message.what == LayerThread.MOVE_TO_DRAW) {
            //moveToDraw(message);
        }
    }*/
   /* public void moveToDraw(Message message) {
        float[] primStartTouchEventXY=message.getData().getFloatArray(LayerThread.TOUCH_XY);

        toCanvasCoordinate(primStartTouchEventXY);
        final float fx=primStartTouchEventXY[0];//x*values[Matrix.MSCALE_X];
        final float fy=primStartTouchEventXY[1];
        if(DRAWING_PROCESS){
            touchMove(fx, fy);
        }

    }
    private void touchMove(float x, float y){

        //synchronized (Frame.currentFrame){
        float dx=Math.abs(x-mX);
        float dy=Math.abs(x-mY);
        if(dx>=TOUCH_TOLERRANCE||dy>=TOUCH_TOLERRANCE){
            currentPath.quadTo(mX,
                    mY,
                    ((x+mX)/2),
                    ((y+mY)/2));
            mX=x;
            mY=y;
//            mPath.reset();
//            mPath.addCircle(mX,mY,30,Path.Direction.CW);
        }
        //}

    }
    public static void toCanvasCoordinate(float[] points){

        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
        //0605
        scaledMatrix.postTranslate(-CanvasListener.mPosX,-CanvasListener.mPosY);
        scaledMatrix.postScale(1/CanvasListener.scaleFactor,1/CanvasListener.scaleFactor,
                CanvasListener.scalePointX,CanvasListener.scalePointY);

      *//*  scaledMatrix.postScale(1/ScaleAndRotateListener.scaleFactor,1/ScaleAndRotateListener.scaleFactor,
                ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
        scaledMatrix.postTranslate(-ScaleAndRotateListener.mPosX,-ScaleAndRotateListener.mPosY);
        scaledMatrix.postRotate(-ScaleAndRotateListener.mAngle,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
*//*
        scaledMatrix.mapPoints(points);

    }
    private void upToDraw() {
        if(DRAWING_PROCESS){

            touch_up();
            DRAWING_PROCESS=false;
        }
    }
    private void touch_up() {
        //circlePath.reset();
        //mPath.lineTo(mX,mY);
        //mPath.reset();
        // Paint paint=drawingPaint;
        //synchronized (Frame.currentFrame){
        currentPath.lineTo(mX,mY);
        //}
        //LayerAdapter.layers.get(Layer.CURRENT_LAYER_POSITION).pathArrayList.add(currentPath);
        //.add(paint);
        //currentPath=new Path();

    }
    private void setBeforeCanvas(Message message) {
        float[] primStartTouchEventXY=message.getData().getFloatArray(LayerThread.TOUCH_XY);
        toCanvasCoordinate(primStartTouchEventXY);
        if (primStartTouchEventXY != null) {
            final float fx = primStartTouchEventXY[0];
            final float fy=primStartTouchEventXY[1];//y*values[Matrix.MSCALE_Y];
            if(draw_mode==pen_mode){
                touchStart(fx, fy,false);
                //invalidate();
                DRAWING_PROCESS=true;
            }else if(draw_mode==eraser_mode){
                touchStart(fx, fy,true);
                //invalidate();
                DRAWING_PROCESS=true;
            }
            Frame.currentFrame.setPathBitmapBefore();
        }

    }
    private void touchStart(float x, float y,boolean eraserMode) {

        //synchronized (Frame.currentFrame){
        Frame.currentFrame.touchStart();
        currentPath= Frame.currentFrame.getCurrentPath();
        // currentLayer.pen.pathArrayList.get(Frame.currentFrame.currentLayer.pen.pathArrayList.size()-1);
        currentPath.reset();
        currentPath.moveTo(x,y);

        drawingPaint=  Frame.currentFrame.getCurrentPaint();
        //currentLayer.pen.paintArrayList.get(
        //Frame.currentFrame.currentLayer.pen.paintArrayList.size()-1);
        getPaint(drawingPaint,eraserMode);


        mX=x; mY=y;
        //}

    }
*/
    private void redrawOnBeforeCanvas() {

        Frame.currentFrame.redrawOnBeforeCanvas();

    }

   /* private void calculateDstRect() {
        synchronized (Frame.currentFrame) {
            float[] layout=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};
            Matrix scaledMatrix=new Matrix();
            scaledMatrix.reset();

            //scaledMatrix.postRotate(Layer.mAngle,Layer.scalePointX,Layer.scalePointY);
            scaledMatrix.postScale(CanvasListener.scaleFactor,CanvasListener.scaleFactor,
                    CanvasListener.scalePointX,CanvasListener.scalePointY);
            scaledMatrix.postTranslate(CanvasListener.mPosX,CanvasListener.mPosY);
            scaledMatrix.mapPoints(layout);
            DrawingContourView.CANVAS_DST_RECT.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);

        }
    }*/

    private void removeCurrentPen(){
        Frame.currentFrame.removeCurrentPen();
    }


}
