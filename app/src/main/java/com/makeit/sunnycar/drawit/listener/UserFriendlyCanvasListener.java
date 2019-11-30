package com.makeit.sunnycar.drawit.listener;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.makeit.sunnycar.drawit.view.DrawingContourView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class UserFriendlyCanvasListener {

    private static final String TAG="zoomableImageView";
    private final DrawingContourView drawingContourView;
    private int containerWidth;
    private int containerHeight;

    private Matrix matrix=new Matrix();
    Matrix savedMatrix=new Matrix();

    PointF start=new PointF();

    float currentScale;
    float curX;
    float curY;

    //static final int NONE=0;
    //static final int DRAG=1;
    //static final int ZOOM=2;
    //int mode=NONE;

    float targetX,targetY;
    float targetScale,targetScaleX,targetScaleY;
    float scaleChange,targetRatio,transitionRatio;

    float easing=0.2f;
    boolean isAnimating =false;

    float scaleDampingFactor=0.5f;

    float oldDist=1f;
    PointF mid=new PointF();

    //private Handler mHandler=new Handler();

    float minScale;
    float maxScale=8.0f;

    float wpRadius=25.0f;
    float wpInnerRadius=20.0f;

    float screenDensity;

    private GestureDetector gestureDetector;

    public static final int DEFAULT_SCALE_FIT_INSIDE=0;
    public static final int DEFAULT_SCALE_ORIGINAL=1;

    private int defaultScale=DEFAULT_SCALE_FIT_INSIDE;
    private int ptrCount=0;
    private int mFirstActivePointerId=-1;
    private float[] primStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private float[] secStartTouchEventXY=new float[]{-1.0f,-1.0f};
    private int mSecActivePointerId;
    private boolean isPinch=false;
    private RectF CANVAS_DST_RECT;
    private Matrix firstMatrix;
    private Matrix firstInvertMatrix;

    public UserFriendlyCanvasListener(DrawingContourView drawingContourView, float density, RectF CANVAS_DST_RECT) {
        this.drawingContourView=drawingContourView;
        screenDensity=density;
        this.CANVAS_DST_RECT=CANVAS_DST_RECT;
        //initPaints();
        //gestureDetector=new GestureDetector(new MyGestureDetector());

    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        containerWidth=w;//DrawingContourView.WIDTH;
        containerHeight=h;//DrawingContourView.HEIGHT;
        int imgWidth=DrawingContourView.WIDTH;
        int imgHeight=DrawingContourView.HEIGHT;

        float scale;
        int initX=0;
        int initY=0;

        if(defaultScale==DEFAULT_SCALE_FIT_INSIDE){
            if(imgWidth>=containerWidth){
                scale=(float)containerWidth/imgWidth;
                float newHeight=imgHeight*scale;
                initY=(containerHeight-(int)newHeight)/2;

                matrix.setScale(scale,scale);
                matrix.postTranslate(0,initY);
            }else{
                scale=(float)containerHeight/imgHeight;
                float newWidth=imgWidth*scale;
                initX=(imgWidth-(int)newWidth)/2;

                matrix.setScale(scale,scale);
                matrix.postTranslate(initX,0);
            }

            curX=initX;
            curY=initY;

            currentScale=scale;
            minScale=scale;

        }else{
            if(imgWidth>containerWidth){
                initY=(containerHeight-(int)imgHeight)/2;
                matrix.postTranslate(0,initY);
            }else{
                initX=(containerWidth-(int)imgWidth)/2;
                matrix.postTranslate(initX,0);
            }

            curX=initX;
            curY=initY;

            currentScale=1.0f;
            minScale=1.0f;
        }
        firstMatrix=new Matrix();
        firstMatrix.set(matrix);
        firstInvertMatrix=new Matrix();
        matrix.invert(firstInvertMatrix);
        calculate();
    }

    private void checkImageConstraints(){

        float[] mvals=new float[9];
        matrix.getValues(mvals);

        currentScale=mvals[0];

        if(currentScale<minScale){
            float deltaScale=minScale/currentScale;
            float px=containerWidth/2;
            float py=containerHeight/2;
            matrix.postScale(deltaScale,deltaScale,px,py);
            float[] layout=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};
            matrix.mapPoints(layout);
            CANVAS_DST_RECT.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);


        }

        matrix.getValues(mvals);
        currentScale=mvals[0];
        curX=mvals[2];
        curY=mvals[5];

        int rangeLimitX=containerWidth-(int)(DrawingContourView.WIDTH*currentScale);
        int rangeLimitY=containerHeight-(int)(DrawingContourView.HEIGHT*currentScale);

        boolean toMoveX=false;
        boolean toMoveY=false;

        if(rangeLimitX<0){
            if(curX>0){
                targetX=0;
                toMoveX=true;
            }else if(curX<rangeLimitX){
                targetX=rangeLimitX;
                toMoveX=true;
            }
        }else{
            targetX=rangeLimitX/2;
            toMoveX=true;
        }

        if(rangeLimitY<0){
            if(curY>0){
                targetY=rangeLimitY;
                toMoveY=true;
            }else if(curY<rangeLimitY){
                targetY=rangeLimitY;
                toMoveY=true;
            }
        }else{
            targetY=rangeLimitY/2;
            toMoveY=true;
        }

        if(toMoveX ||toMoveY){
            if(!toMoveY) targetY=curY;
            if(!toMoveX) targetX=curX;

            isAnimating=true;

            //mHandler.removeCallbacks(mUpdateImagePositionTask);
            //mHandler.postDelayed(mUpdateImagePositionTask,100);
        }

    }
    private void dumpEvent(MotionEvent event){
        String name[]={"DOWN","UP","MOVE","CANCEL","OUTSIDE","POINTER_DOWN","POINTER_UP",
        "7?","8?", "9?"};
        StringBuilder sb=new StringBuilder();
        int action=event.getAction();
        int actionCode=action&MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(name[actionCode]);
        if(actionCode==MotionEvent.ACTION_POINTER_DOWN||actionCode==MotionEvent.ACTION_POINTER_UP){
            sb.append("(pid ").append(action>>MotionEvent.ACTION_POINTER_INDEX_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for(int i=0;i<event.getPointerCount();i++){
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int)event.getX(i));
            sb.append(",").append((int)event.getY(i));
            if(i+1<event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
    }
    public boolean onTouchEvent(MotionEvent event){
//        if(gestureDetector.onTouchEvent(event)){
//            return true;
//        }
        //dumpEvent(event);
        if(isAnimating) return true;

        float[] mvals=new float[9];
        switch (event.getAction()&MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                ptrCount++;
                if(!isAnimating&&ptrCount==1){
                    savedMatrix.set(matrix);
                    start.set(event.getX(),event.getY());
                    //mode=DRAG;
                    isPinch=false;

                    final int pointerIndex = event.getActionIndex();
                    primStartTouchEventXY[0] = event.getX(pointerIndex);
                    primStartTouchEventXY[1] = event.getY(pointerIndex);
                    mFirstActivePointerId = event.getPointerId(pointerIndex);

                    //
                    drawingContourView.friendlyDraw(primStartTouchEventXY,matrix);
                    //drawingContourView.downToDraw(primStartTouchEventXY);
                    return false;
                }else if(ptrCount==2){

                    drawingContourView.removeCurrentPen();

                    final int pointerIndex = event.getActionIndex();
                    final float x=event.getX(pointerIndex);
                    final float y=event.getY(pointerIndex);

                    secStartTouchEventXY[0] = x ;
                    secStartTouchEventXY[1] = y;
                    mSecActivePointerId = event.getPointerId(pointerIndex);

                    //primDistance=distance(primStartTouchEventXY,secStartTouchEventXY);
                    oldDist=spacing(event);
                    if(oldDist>10f){
                        savedMatrix.set(matrix);
                        midPoint(mid,event);
                        //mode=ZOOM;
                    }
                    isPinch=true;

                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:{
                ptrCount=0;
                mFirstActivePointerId = INVALID_POINTER_ID;
                if(isPinch) {
                    isPinch=false;
                    return true;
                }
                else {
                    drawingContourView.upToDraw();
                    //drawingContourView.friendlyUp();
                    return false;
                }
            }
            case MotionEvent.ACTION_POINTER_UP:{
                //mode=NONE;
                matrix.getValues(mvals);
                curX=mvals[2];
                curY=mvals[5];
                currentScale=mvals[0];

                ptrCount--;
                mSecActivePointerId=INVALID_POINTER_ID;
                // isPinch=false;
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mFirstActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    primStartTouchEventXY[0] = event.getX(newPointerIndex);
                    primStartTouchEventXY[1] = event.getY(newPointerIndex);
                    mFirstActivePointerId = event.getPointerId(newPointerIndex);
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                if(ptrCount==1&&!isAnimating){
                    final int pointerIndex = event.findPointerIndex(mFirstActivePointerId);

                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);

                    drawingContourView.friendlyMoveToDraw(new float[]{x,y},matrix);
                    return false;
                }else if(isPinch&&!isAnimating){
                    float newDist=spacing(event);
                    if(newDist>10f){
                        matrix.set(savedMatrix);
                        float diffX=event.getX(mFirstActivePointerId)-start.x;
                        float diffY=event.getY(mFirstActivePointerId)-start.y;

                        float scale=newDist/oldDist;
                        matrix.getValues(mvals);
                        currentScale=mvals[0];

//                        float[] _mid={(event.getX(0)+event.getX(1))/2,
//                                (event.getY(0)+event.getY(1))/2};
//                        matrix.mapPoints(_mid);

                        if(currentScale*scale<=minScale){
                            matrix.postScale(minScale/currentScale,minScale/currentScale,
                                    mid.x,mid.y);
                        }else if(currentScale*scale>=maxScale){
                            matrix.postScale(maxScale/currentScale,maxScale/currentScale,
                                    mid.x,mid.y);
                        }else{
                            matrix.postScale(scale,scale, mid.x,mid.y);
                        }
                        matrix.postTranslate(diffX,diffY);

                        matrix.getValues(mvals);
                        curX=mvals[2];
                        curY=mvals[5];
                        currentScale=mvals[0];
                        calculate();

                        return true;
                    }
                }
            }
        }
        //calculate();
        return true;
    }

    private float spacing(MotionEvent event){
        float x=event.getX(0)-event.getX(1);
        float y=event.getY(0)-event.getY(1);

        return (float) Math.sqrt(x*x+y*y);
    }

    private void midPoint(PointF point, MotionEvent event){
        float x=event.getX(0)+event.getX(1);
        float y=event.getY(0)+event.getY(1);
        point.set(x/2,y/2);
    }

    private Runnable mUpdateImagePositionTask=new Runnable() {
        @Override
        public void run() {
            float[] mvals;
            if(Math.abs(targetX-curX)<5&&Math.abs(targetY-curY)<5){
                isAnimating=false;
                //mHandler.removeCallbacks(mUpdateImagePositionTask);
                mvals=new float[9];
                matrix.getValues(mvals);

                currentScale=mvals[0];
                curX=mvals[2];
                curY=mvals[5];

                float diffX=(targetX-curX);
                float diffY=(targetX-curX);
                matrix.postTranslate(diffX,diffY);
            }else{
                isAnimating=true;
                mvals=new float[9];
                matrix.getValues(mvals);

                currentScale=mvals[0];
                curX=mvals[2];
                curY=mvals[5];

                float diffX=(targetX-curX)*0.3f;
                float diffY=(targetY-curY)*0.3f;

                matrix.postTranslate(diffX,diffY);
                //mHandler.postDelayed(this,25);


            }
            calculate();
        }
    };

    private Runnable mUpdateImageScale=new Runnable() {
        @Override
        public void run() {

            float transitionalRatio=targetScale/currentScale;
            float dx;
            if(Math.abs(transitionalRatio-1)>0.05){
                isAnimating=true;
                if(targetScale>currentScale){
                    dx=transitionalRatio-1;
                    scaleChange=1+dx*0.2f;

                    currentScale*=scaleChange;
                    if(currentScale>targetScale){
                        currentScale=currentScale/scaleChange;
                        scaleChange=1;
                    }

                }else{
                    dx=1-transitionalRatio;
                    scaleChange=1-dx*0.5f;
                    currentScale*=scaleChange;
                    if(currentScale<targetScale){
                        currentScale=currentScale/scaleChange;
                        scaleChange=1;
                    }
                }

                if(scaleChange!=1){
                    matrix.postScale(scaleChange,scaleChange,targetScaleX,targetScaleY);
                    //mHandler.postDelayed(mUpdateImageScale,15);
                    calculate();
                }else{
                    isAnimating=false;
                    scaleChange=1;
                    matrix.postScale(targetScale/currentScale,targetScale/currentScale,
                            targetScaleX,targetScaleY);
                    currentScale=targetScale;
                    //mHandler.removeCallbacks(mUpdateImageScale);
                    calculate();
                    //checkImageConstraints();
                }
            }else{
                isAnimating=false;
                scaleChange=1;
                matrix.postScale(targetScale/currentScale,targetScale/currentScale,
                        targetScaleX,targetScaleY);
                currentScale=targetScale;
                //mHandler.removeCallbacks(mUpdateImageScale);
                calculate();
                //checkImageConstraints();
            }
        }
    };
    private void calculate() {
        float[] layout=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};
       /* float[] layout=new float[]{DrawingContourView.center_layout[0],
                DrawingContourView.center_layout[1],DrawingContourView.center_layout[2],
                DrawingContourView.center_layout[3]};*/
        matrix.mapPoints(layout);
        CANVAS_DST_RECT.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);

    }

    public void reset() {
        matrix.set(firstMatrix);
        savedMatrix.set(firstMatrix);
        //DEFAULT_SCALE_FIT_INSIDE 혹은 DEFAULT_SCALE_ORIGINAL인지에 따라 matrix의 초기화값이 달라짐
        //그럼 matrix를 invert할 때
        //1)DEFAULT_SCALE_ORIGINAL -> 변환 성공
        //2)DEFAULT_SCALE_FIT_INSIDE -> matrix의 초기화값이 실제 canvas를 기준으로 변화되지 않기 때문에
        //inver값이 잘못됨
        calculate();
        //matrix.reset();

    }

    public Matrix getMatrix() {

        return matrix;
    }
//    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
//
//
//
//    }
}
