package com.makeit.sunnycar.drawit.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;

import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gmltj on 2018-05-23.
 */

public class Frame {
    public Bitmap framesBitmap;
    public Canvas framesCanvas;
    public static int otherFrameOpacity=100;
    private Paint framesPaint;
    public static Frame currentFrame;
    public Layer currentLayer;
    public static int WIDTH,HEIGHT;

    public static int CURRENT_FRAME_POSITION;
    public int CURRENT_LAYER_POSITION;
    public ArrayList<Integer> LAYER_HISTORY=new ArrayList<Integer>();
    public ArrayList<Path> undoPath=new ArrayList<Path>();
    public ArrayList<Paint> undoPaint=new ArrayList<Paint>();
    public ArrayList<Integer> undoLayerHistory=new ArrayList<Integer>();

    public List<Layer> layers= Collections.synchronizedList(new ArrayList<Layer>());
    public Bitmap layersBitmap=Bitmap.createBitmap(DrawingContourView.WIDTH,DrawingContourView.HEIGHT, Bitmap.Config.ARGB_8888);;
    private Canvas layersCanvas=new Canvas(layersBitmap);
    public static final int MAX_FRAME_COUNT=10;
    private LayerAdapter layerAdapter;

    public Frame() {
        layers.add(new Layer());
        this.CURRENT_LAYER_POSITION=0;
        if(framesBitmap==null){
            framesBitmap=Bitmap.createBitmap(DrawingContourView.WIDTH,DrawingContourView.HEIGHT, Bitmap.Config.ARGB_8888);
            framesCanvas=new Canvas(framesBitmap);
        }
        framesPaint=new Paint();
        framesPaint.setDither(true);
        framesPaint.setFlags(Paint.DITHER_FLAG);
        framesPaint.setAlpha(100);

      if(WIDTH==0||HEIGHT==0){

          WIDTH=DrawingContourView.WIDTH;
          HEIGHT=DrawingContourView.HEIGHT;
      }
    }

    public synchronized static void setCurrentFrame(int position,LayerAdapter layerAdapter) {

        CURRENT_FRAME_POSITION=position;
        currentFrame= layerAdapter.frames.get(CURRENT_FRAME_POSITION);
        currentFrame.getCurrentLayer();
        currentFrame.setLayerAdapter(layerAdapter);

    }

    private void setLayerAdapter(LayerAdapter layerAdapter) {
        if(this.layerAdapter==null)
            this.layerAdapter=layerAdapter;

    }

    private void getCurrentLayer(){
        currentLayer=layers.get(CURRENT_LAYER_POSITION);

    }
    public synchronized void setCurrentLayer(int position){

        CURRENT_LAYER_POSITION=position;
        currentLayer=layers.get(currentFrame.CURRENT_LAYER_POSITION);
    }

    private void drawFrameBitmap() {

            Iterator<Layer> iterator=layers.iterator();
            layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

            while(iterator.hasNext()){
                Layer layer=iterator.next();
                layer.draw(layersCanvas);
            }
    }
    public synchronized void drawNotCurrentFramesBitmap(){

        synchronized (currentLayer){
            Iterator<Frame> frameIterator=layerAdapter.frames.iterator();
            framesCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
            framesPaint.setAlpha(otherFrameOpacity);

            while(frameIterator.hasNext()){
                Frame frame=frameIterator.next();

                if(!frame.equals(Frame.currentFrame)){
                    Iterator<Layer> layerIterator=frame.layers.iterator();
                    frame.layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                    while (layerIterator.hasNext()){
                        Layer layer=layerIterator.next();
                        layer.draw(frame.layersCanvas);

                    }
                    framesCanvas.drawBitmap(frame.layersBitmap,0,0,framesPaint);

                }

            }
        }

    }
    public synchronized void undo() {
        if(LAYER_HISTORY.size()>0&&layers.size()>0){

            int layer_pos=LAYER_HISTORY.remove(LAYER_HISTORY.size()-1);
            undoLayerHistory.add(layer_pos);
            Layer layer=layers.get(layer_pos);
            undoPath.add(layer.removeCurrentPath());
            undoPaint.add(layer.removeCurrentPaint());

            layer.undo();

        }



    }

    private Paint removeCurrentPaint() {
        return currentLayer.removeCurrentPaint();
    }

    private Path removeCurrentPath() {
        return currentLayer.removeCurrentPath();
    }

    public synchronized void redo() {
        if(undoPath.size()>0&&undoPaint.size()>0&&undoLayerHistory.size()>0&&layers.size()>0){
            LAYER_HISTORY.add(undoLayerHistory.remove(undoLayerHistory.size()-1));

            Layer layer=layers.get(LAYER_HISTORY.get(LAYER_HISTORY.size()-1));
                layer.redo(undoPath.remove(undoPath.size()-1),
                        undoPaint.remove(undoPaint.size()-1));

        }

    }

    public synchronized void redrawForSave(){

        Iterator<Layer> layerIterator=layers.iterator();
        layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

        while (layerIterator.hasNext()){
            Layer layer=layerIterator.next();
            layer.redrawForSave(layersCanvas);
        }

    }
    public void redrawOnBeforeCanvas() {
            currentLayer.redrawOnBeforeCanvas();

    }

    public synchronized void resetLayerOpacity(int position,int progress){
        layers.get(position).layerOpacity=progress;

    }
    public synchronized void drawBitmap() {
        drawFrameBitmap();
    }
    public void setResizedBitmapAndRemoveAllPens() {
        currentLayer.setResizedBitmapAndRemoveAllPens();
        LAYER_HISTORY.clear();
        undoPath.clear();
        undoPaint.clear();
        undoLayerHistory.clear();
    }
    public synchronized void addSketch() {
        currentLayer.addSketch();
    }


    public void setOtherFrameOpacity(int progress) {
        otherFrameOpacity=progress;
        drawNotCurrentFramesBitmap();


    }

    public synchronized void addLayer() {
        synchronized (currentLayer){
            layers.add(new Layer());
        }

    }

    public synchronized boolean removeLayer(int removedPosition) {

        if (layers.size() > 1) {
            setCurrentLayer(0);

            synchronized (currentLayer){
                layers.remove(removedPosition);
                for(int i=0;i<LAYER_HISTORY.size();i++){
                    if(LAYER_HISTORY.get(i)==removedPosition)
                        LAYER_HISTORY.remove(i);

                }
                undoPath.clear();
                undoPaint.clear();
                undoLayerHistory.clear();
            }


            return true;
        } else return false;
    }

    public synchronized void drawLayers(Canvas canvas, RectF CANVAS_RECT){
        Iterator<Layer> iterator=layers.iterator();
        canvas.save();
        canvas.drawRect(CANVAS_RECT,DrawingContourView.whitePaint);
        canvas.drawBitmap(currentFrame.framesBitmap,null,CANVAS_RECT,DrawingContourView.framePaint);

        while(iterator.hasNext()){

            Layer layer=iterator.next();
            layer.exdraw(canvas,CANVAS_RECT);

        }
        canvas.restore();

        if(DrawingContourView.isCurrentResizeMethod()){

            Matrix matrix=new Matrix();
            matrix.reset();
            RectF rectF=currentLayer.LAYER_DST_RECT;

            matrix.postScale(0.25f,0.25f,rectF.left,rectF.top);
            matrix.mapRect(DrawingContourView.CROP_LEFT_TOP_DST_RECT,rectF);
            canvas.drawBitmap(DrawingContourView.left_top_crop,null,DrawingContourView.CROP_LEFT_TOP_DST_RECT,DrawingContourView.cropPaint);

            matrix.reset();
            matrix.postScale(0.25f,0.25f,rectF.right,rectF.top);
            matrix.mapRect(DrawingContourView.CROP_RIGHT_TOP_DST_RECT,rectF);
            canvas.drawBitmap(DrawingContourView.right_top_crop,null,DrawingContourView.CROP_RIGHT_TOP_DST_RECT,DrawingContourView.cropPaint);

            matrix.reset();
            matrix.postScale(0.25f,0.25f,rectF.left,rectF.bottom);
            matrix.mapRect(DrawingContourView.CROP_LEFT_BOTTOM_DST_RECT,rectF);
            canvas.drawBitmap(DrawingContourView.left_bottom_crop,null,DrawingContourView.CROP_LEFT_BOTTOM_DST_RECT,DrawingContourView.cropPaint);//in dp//80

            matrix.reset();
            matrix.postScale(0.25f,0.25f,rectF.right,rectF.bottom);
            matrix.mapRect(DrawingContourView.CROP_RIGHT_BOTTOM_DST_RECT,rectF);
            canvas.drawBitmap(DrawingContourView.right_bottom_crop,null,DrawingContourView.CROP_RIGHT_BOTTOM_DST_RECT,DrawingContourView.cropPaint);
            canvas.restore();
        }

    }

    public synchronized void removeCurrentPen() {

            currentLayer.removeCurrentPen();
            if(LAYER_HISTORY.size()>0)
                LAYER_HISTORY.remove(LAYER_HISTORY.size()-1);

    }

    public void touchStart() {

        undoPath.clear();
        undoPaint.clear();
        undoLayerHistory.clear();
        currentLayer.pen.addPen();
        LAYER_HISTORY.add(CURRENT_LAYER_POSITION);
    }

    public Path getCurrentPath() {

        return currentLayer.getCurrentPath();
    }

    public Paint getCurrentPaint() {
        return currentLayer.getCurrentPaint();
    }

    public void setPathBitmapBefore() {
        currentLayer.setPathBitmapBefore();

    }

    public void setLayerDstRect(float scaleFactor) {
        currentLayer.setLayerDstRect(scaleFactor);
    }


    public synchronized void setBitmapBeforeAndRemoveAllPen(){
        currentLayer.setBitmapBeforeAndRemoveAllPen();

        LAYER_HISTORY.clear();
        undoPath.clear();
        undoPaint.clear();
        undoLayerHistory.clear();


    }

    public synchronized void redrawOnBeforeCanvas(Path currentPath, Paint drawingPaint) {
        currentLayer.redrawOnBeforeCanvas(currentPath,drawingPaint);
    }


    public void resetResizeFactor() {
        currentLayer.resetResizeFactor();
    }
}
