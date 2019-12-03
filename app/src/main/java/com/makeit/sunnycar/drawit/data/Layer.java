package com.makeit.sunnycar.drawit.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;

import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

/**
 * Created by gmltj on 2018-05-03.
 */

public class Layer {
    public Pen pen=new Pen();
    public static final int OPACITY_MAX=255;
    public int layerOpacity=OPACITY_MAX;
    public Bitmap pathBitmap;
    private Canvas pathCanvas;
    public boolean eyeOn=true;

    public float scaleFactor=1;
    public float scalePointX, scalePointY;
    public float mPosX, mPosY;
    public  float mAngle;
    public RectF LAYER_DST_RECT=new RectF();
    public Matrix layerDstMtx=new Matrix();
    public Bitmap pathBitmapBefore=Bitmap.createBitmap(DrawingContourView.WIDTH,DrawingContourView.HEIGHT, Bitmap.Config.ARGB_8888);
    private Canvas beforPathCanvas=new Canvas(pathBitmapBefore);
    public Bitmap resizedBitmap=Bitmap.createBitmap(DrawingContourView.WIDTH,DrawingContourView.HEIGHT, Bitmap.Config.ARGB_8888);
    private Canvas resizedBitmapCanvas=new Canvas(resizedBitmap);


    private Paint layerPaint;
    private static Paint highBPPaint;
    private static Paint lowBPPaint;

    private Paint alphaPaint;
    private boolean isResized=false;

    public Layer() {
        init();
    }

    private void init() {
        if(highBPPaint==null){
            highBPPaint=new Paint();
            highBPPaint.setDither(true);
            highBPPaint.setAntiAlias(true);
            highBPPaint.setFilterBitmap(true);
            highBPPaint.setFlags(Paint.DITHER_FLAG|Paint.FILTER_BITMAP_FLAG|Paint.ANTI_ALIAS_FLAG);
        }
     if(lowBPPaint==null){
         lowBPPaint=new Paint();
         lowBPPaint.setDither(true);
         lowBPPaint.setFlags(Paint.DITHER_FLAG);
     }

      if(layerPaint==null){
          layerPaint=new Paint();
          layerPaint.setDither(true);
          layerPaint.setAntiAlias(true);
          layerPaint.setFlags(Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG);
      }
        if(alphaPaint==null){

            alphaPaint=new Paint();
            alphaPaint.setDither(true);
            alphaPaint.setFlags(Paint.DITHER_FLAG);
        }

        pathBitmap=Bitmap.createBitmap(DrawingContourView.WIDTH,DrawingContourView.HEIGHT, Bitmap.Config.ARGB_8888);//,exampleBitmap;
        pathCanvas=new Canvas(pathBitmap);

        LAYER_DST_RECT.set(DrawingContourView.CENTER_RECTF);
    }



    public synchronized void addSketch() {

        pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        Rect canvas_layout=new Rect(0,0, DrawingContourView.WIDTH,DrawingContourView.HEIGHT);

        if(!isResized){
            isResized=true;
            resizedBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        }
        float[] factor=new float[3];
        float image_width=ThreshHandler.FINAL_IMAGE.getWidth();
        float image_height=ThreshHandler.FINAL_IMAGE.getHeight();

        float ratioWidth=(float)DrawingContourView.WIDTH/image_width;
        float ratioHeight=(float)DrawingContourView.HEIGHT/image_height;

        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
        if(ratioWidth<ratioHeight){
            float y=(DrawingContourView.HEIGHT/ratioWidth-image_height)/2;
            scaledMatrix.postTranslate(0,y);
            scaledMatrix.postScale(ratioWidth,ratioWidth,0,0);

        }else{
            float x=(DrawingContourView.WIDTH/ratioHeight-image_width)/2;
            scaledMatrix.postTranslate(x,0);
            scaledMatrix.postScale(ratioHeight,ratioHeight,0,0);

        }
        float[] center_layout=new float[]{0,0,image_width,image_height};
        scaledMatrix.mapPoints(center_layout);
        RectF center_rectf=new RectF(center_layout[0],center_layout[1],center_layout[2],center_layout[3]);
        resizedBitmapCanvas.drawBitmap(ThreshHandler.FINAL_IMAGE,null,center_rectf,highBPPaint);
        pathCanvas.drawBitmap(resizedBitmap, 0,0,highBPPaint);

    }
    public synchronized void undo() {
        Rect canvas_layout=new Rect(0,0, DrawingContourView.WIDTH,DrawingContourView.HEIGHT);
        pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        if(isResized)
            pathCanvas.drawBitmap(resizedBitmap,null,canvas_layout,highBPPaint);

        pen.undo(pathCanvas);

    }
    public synchronized void redo(Path path, Paint paint){
        Rect canvas_layout=new Rect(0,0, DrawingContourView.WIDTH,DrawingContourView.HEIGHT);
        pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        if(isResized)
            pathCanvas.drawBitmap(resizedBitmap,null,canvas_layout,highBPPaint);

        pen.redo( path,  paint, pathCanvas);

    }

    public synchronized void redrawForSave(Canvas layersCanvas) {
        if(eyeOn) {

           layerPaint.setAlpha(layerOpacity);
           layersCanvas.drawBitmap(pathBitmap,0,0,layerPaint);//layout0,1이 window상의 원점을 가르키지 않는 이유

        }
    }

    public synchronized void redrawOnBeforeCanvas(Path currentPath, Paint drawingPaint) {
        if(eyeOn){
            if(pen.paintArrayList.size()>0&&
                    pen.pathArrayList.size()>0){
                pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                pathCanvas.drawBitmap(pathBitmapBefore,0,0, lowBPPaint);
                if(currentPath!=null&&drawingPaint!=null){
                    pathCanvas.drawPath(
                            currentPath,
                            drawingPaint
                    );
                }

            }
        }
    }
    public synchronized void redrawOnBeforeCanvas() {

                if(eyeOn){

                           pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                           pathCanvas.drawBitmap(pathBitmapBefore,0,0, lowBPPaint);

                }


    }


    public void draw(Canvas canvas){
        if(eyeOn){

            layerPaint.setAlpha(layerOpacity);
            canvas.drawBitmap(pathBitmap,0,0,layerPaint);//layout0,1이 window상의 원점을 가르키지 않는 이유

        }
    }
    public synchronized void exdraw(Canvas canvas, RectF CANVAS_RECT){
        if(eyeOn){

            layerPaint.setAlpha(layerOpacity);
            canvas.drawBitmap(pathBitmap,null,CANVAS_RECT,layerPaint);//layout0,1이 window상의 원점을 가르키지 않는 이유

        }
    }

    private void setCurrentPathBitmap(Path currentPath, Paint drawingPaint) {


        pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        pathCanvas.drawBitmap(pathBitmapBefore,0,0,lowBPPaint);
        pathCanvas.drawPath(currentPath, drawingPaint);

    }

    public synchronized void setPathBitmapBefore() {

        beforPathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

        beforPathCanvas.drawBitmap(pathBitmap,0,0,lowBPPaint);


    }



    public synchronized void removeCurrentPen() {

        pen.removeCurrentPen();
    }

    public Path getCurrentPath() {
       return pen.getCurrentPath();
    }

    public Paint getCurrentPaint() {
        return pen.getCurrentPaint();
    }

    public Path removeCurrentPath() {
        return pen.removeCurrentPath();
    }

    public Paint removeCurrentPaint() {
        return pen.removeCurrentPaint();
    }

    public synchronized void setLayerDstRect(float multiScaleFactor) {
        float[] layout=new float[]{
                0,0,DrawingContourView.WIDTH,DrawingContourView.HEIGHT};

        scaleFactor*=multiScaleFactor;
        scaleFactor= Math.max(0.1f,Math.min(scaleFactor,5.0f));



        Matrix scaledMatrix=new Matrix();

        scaledMatrix.reset();
        scaledMatrix.postScale(this.scaleFactor, this.scaleFactor, DrawingContourView.WIDTH/2,DrawingContourView.HEIGHT/2);
        scaledMatrix.postTranslate(mPosX,mPosY);

        //this part is for 4 'resize icons' to come along resized image
        if(DrawingContourView.CENTER_FACTOR[2]==1)
            scaledMatrix.postTranslate(0,DrawingContourView.CENTER_FACTOR[1]);
        else
            scaledMatrix.postTranslate(DrawingContourView.CENTER_FACTOR[1],0);
        scaledMatrix.postScale(DrawingContourView.CENTER_FACTOR[0],DrawingContourView.CENTER_FACTOR[0],0,0);
        scaledMatrix.mapPoints(layout);
        LAYER_DST_RECT.set((int)layout[0],(int)layout[1],(int)layout[2],(int)layout[3]);

        scaledMatrix.reset();
        scaledMatrix.postRotate(mAngle,DrawingContourView.WIDTH/2,DrawingContourView.HEIGHT/2);
        scaledMatrix.postScale(this.scaleFactor, this.scaleFactor, DrawingContourView.WIDTH/2,DrawingContourView.HEIGHT/2);
        scaledMatrix.postTranslate(mPosX,mPosY);

        redrawLayerCanvas(scaledMatrix);

    }

    private void redrawLayerCanvas(Matrix matrix) {

        pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        pathCanvas.drawBitmap(pathBitmapBefore,matrix,highBPPaint);

        resizedBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        resizedBitmapCanvas.drawBitmap(pathBitmapBefore,matrix,highBPPaint);

    }

    public synchronized void setBitmapBefore() {
        beforPathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        beforPathCanvas.drawBitmap(pathBitmap,0,0,highBPPaint);
        resetResizeFactor();

    }

    public void resetResizeFactor() {
        scaleFactor=1;scalePointY=0;scalePointX=0;
        mPosY=0;mPosX=0;
        mAngle=0;
        resetLayerDstRect();

    }

    private void resetLayerDstRect() {
        LAYER_DST_RECT.set( DrawingContourView.CENTER_RECTF.left,
                DrawingContourView.CENTER_RECTF.top,
                DrawingContourView.CENTER_RECTF.right,
                DrawingContourView.CENTER_RECTF.bottom);
    }


    public synchronized void setBitmapBeforeAndRemoveAllPen() {
        setBitmapBefore();
        removeAllPen();
        isResized=true;

    }

    public synchronized void removeAllPen() {
        pen.removeAllPen();
    }

    public void setResizedBitmapAndRemoveAllPens() {
        setResizedBitmap();
        removeAllPen();
    }

    private void setResizedBitmap() {
        resizedBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
        resizedBitmapCanvas.drawBitmap(pathBitmap,0,0,highBPPaint);


    }

}
