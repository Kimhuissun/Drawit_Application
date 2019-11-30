package com.makeit.sunnycar.drawit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.data.Layer;


public class LayerView extends View{
    private Layer layer;
    private Paint mBitmapPaint;
    private float[] factor=new float[3];
    private int width,height;

    public LayerView(Context context) {
        super(context);init();
    }

    public LayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init();
    }

    public LayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        mBitmapPaint=new Paint(Paint.DITHER_FLAG);
    }


    public void setLayerBitmap(int position) {
        layer = Frame.currentFrame.layers.get(position);
        /*synchronized (layer) {
            layer.pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

            layer.pathCanvas.save();
            layer.pathCanvas.scale(layer.scaleFactor,layer.scaleFactor,layer.scalePointX,layer.scalePointY);
            layer.pathCanvas.translate(layer.mPosX,layer.mPosY);
            layer.pathCanvas.rotate(layer.mAngle,layer.scalePointX,layer.scalePointY);

            if(layer.isSketch&& Threshold.FINAL_IMAGE!=null){
                layer.pathCanvas.drawBitmap(Threshold.FINAL_IMAGE,0,0,DrawingContourView.mBitmapPaint);

            }
            synchronized (layer.pen){
                Iterator<Path> pathIterator=layer.pen.pathArrayList.iterator();
                Iterator<Paint> paintIterator=layer.pen.paintArrayList.iterator();
                while (pathIterator.hasNext()&&paintIterator.hasNext()){
                    layer.pathCanvas.drawPath(pathIterator.next(), paintIterator.next());

                }

            }
            layer.pathCanvas.restore();
        }*/
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(width==0)
            width=right-left;
        if(height==0)
            height=bottom-top;
        DrawingContourView.setCanvasToCenterFactor(width,height,factor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale(factor[0],factor[0],0,0);
        if(factor[2]==1)
            canvas.translate(0,factor[1]);
        else
            canvas.translate(factor[1],0);
        canvas.rotate(layer.mAngle,DrawingContourView.WIDTH/2,DrawingContourView.HEIGHT/2);
        //canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(layer.pathBitmap,0,0,null);
    }
}
