package com.makeit.sunnycar.drawit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.data.Frame;


public class ExampleView extends View {

    private static int width;
    private static int height;
    private Paint mBitmapPaint;
    private float[] factor=new float[3];
    private Frame frame;
    private Paint layerPaint;

    public ExampleView(Context context) {
        super(context);
        init();
    }

    public ExampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ExampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public ExampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {
        mBitmapPaint=new Paint(Paint.DITHER_FLAG);        layerPaint=new Paint(Paint.DITHER_FLAG);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(width==0)
            width=right-left;
        if(height==0)
            height=bottom-top;
        DrawingContourView.setCanvasToCenterFactor(width,height,factor);
       /* if(scaleMatrix==null){
            scaleMatrix=new Matrix();
            scaleMatrix.setScale((float)height/DrawingContourView.HEIGHT,
                        (float)height/DrawingContourView.HEIGHT);
        }

        if(paint_width_ratio==0)
            paint_width_ratio=(float)(right-left)/DrawingContourView.WIDTH;

        if(reverseScaleMatrix==null) {
            reverseScaleMatrix=new Matrix();
            reverseScaleMatrix.setScale((float)DrawingContourView.HEIGHT/height,(float)DrawingContourView.HEIGHT/height);
        }
        if(background==null)
            background=Bitmap.createScaledBitmap(DrawingContourView.background,width,height,false);
        if(sketchBitmap==null&&Threshold.FINAL_IMAGE!=null)
            sketchBitmap=Bitmap.createScaledBitmap(Threshold.FINAL_IMAGE,Threshold.FINAL_IMAGE.getWidth()*height/DrawingContourView.HEIGHT,Threshold.FINAL_IMAGE.getHeight()*height/DrawingContourView.HEIGHT,false);
     */   //setBitmap();
    }

    public void setBitmap(int position,LayerAdapter layerAdapter) {
        frame=layerAdapter.frames.get(position);

       /* frame.layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

        synchronized (frame){
            Iterator<Layer> iterator=frame.layers.iterator();
            while(iterator.hasNext()){
                Layer layer=iterator.next();
                synchronized (layer){
                    if(layer.eyeOn) {
                        frame.layersCanvas.save();
                        frame.layersCanvas.scale(layer.scaleFactor,layer.scaleFactor,layer.scalePointX,layer.scalePointY);
                        frame.layersCanvas.translate(layer.mPosX,layer.mPosY);
                        frame.layersCanvas.rotate(layer.mAngle,layer.scalePointX,layer.scalePointY);
                        layerPaint.setAlpha(layer.layerOpacity);
                        frame.layersCanvas.drawBitmap(layer.pathBitmap,0,0,layerPaint);//layout0,1이 window상의 원점을 가르키지 않는 이유
                        frame.layersCanvas.restore();
                    }
                }
            }
        } */
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         //if(paths!=null&&paints!=null&&exampleBitmap!=null){
            //canvas.drawBitmap(background,0,0,mBitmapPaint);

            //Canvas c=new Canvas(exampleBitmap);
           // c.rotate(ScaleAndRotateListener.mAngle,width/2,height/2);
           // c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
           /* if(isSketch&&sketchBitmap!=null)
                c.drawBitmap(sketchBitmap,0,0,mBitmapPaint);
            for(int i=0;i<paths.size();i++){

                Path p=paths.get(i);
                p.transform(scaleMatrix);
                Paint paint=paints.get(i);
                float width=paint.getStrokeWidth();
                paint.setStrokeWidth(width*paint_width_ratio);
                c.drawPath(p,paint);

                p.transform(reverseScaleMatrix);
                paint.setStrokeWidth(width);

            }
*/
            canvas.scale(factor[0],factor[0],0,0);
            if(factor[2]==1)
                canvas.translate(0,factor[1]);
            else
                canvas.translate(factor[1],0);

            canvas.drawBitmap(frame.layersBitmap,0,0,null);

       // }


    }



}
