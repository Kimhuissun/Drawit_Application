package com.makeit.sunnycar.drawit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.data.Layer;
import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.listener.ResizeListener;
import com.makeit.sunnycar.drawit.thread.GetImage;

import java.util.Iterator;

/**
 * Created by gmltj on 2018-04-25.
 */

public class DrawingContourView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    public static final int MAX_FPS = 30;
    public static final int MIN_FPS = 1;

    private static final int CROP_UNIT_LENGTH = 80;
    public static float[] CENTER_FACTOR = new float[3] ;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        userFriendlyCanvasListener.onSizeChanged(w,h,oldw,oldh);
    }

    public static int WIDTH=TuneDialog._720;
    public static int HEIGHT=TuneDialog._720;
    private RectF CANVAS_DST_RECT=new RectF();

    public static int draw_mode = 0;
    public static final int pen_mode=0;
    public static final int eraser_mode=1;
    public static final int spoid_mode=2;
    private Paint drawingPaint;
    private Path currentPath;
    private boolean DRAWING_PROCESS=false;
    private float mX;
    private float mY;
    private static final int TOUCH_TOLERRANCE=4;

    public static int CURRENT_METHOD=0;

    /*  public static int draw_mode = 0;
      public static final int pen_mode=0;
      public static final int eraser_mode=1;
      public static final int spoid_mode=2;*/
    public static int LINE_KIND=0;
    public static final int ORG_LINE=0;
    public static final int GRADIENT_LINE=1;
    //private static final int TOUCH_TOLERRANCE=4;

    public static final int DRAW_METHOD=0;
    //public static final int LAYER_METHOD=1;
    public static final int ZOOM_METHOD=2;
    public static final int SELECT_METHOD=3;
    public static final int RESIZE_METHOD=4;
    public static final int CAMERA_METHOD = 5;

    public static final int PAINT_STROKE_MAX_WIDTH=100;
    public static int PAINT_STROKE_WIDTH=20;
    public static int BLUR_RADIUS=PAINT_STROKE_WIDTH/2;
    private static int CURRENT_COLOR=Color.BLACK;

    public Paint mBitmapPaint;
    public static Paint framePaint;
    public static Paint cropPaint;
    public static Paint whitePaint;
    //public static Layer CURRENT_RESIZE_LAYER;
    //public static int radioButtonId;
    //public static BlurMaskFilter blurMaskFilter;
    Context context;
    public Bitmap background;
    public static Bitmap left_top_crop,left_bottom_crop,right_top_crop,right_bottom_crop;


    //private ScaleGestureDetector scaleGestureDetector;
    private FillBottomMenu fillBottomMenu;
    private FillUpperMenu fillUpperMenu;

    private static boolean playing;
    private Thread animThread=null;

    //private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    public boolean animationPlaying=false;
    public static int WINDOW_WIDTH,WINDOW_HEIGHT;
    private float ratio=1;
    private float translateY;
    private int FRAME_COUNT;

    private int currentFrame;
    private long lastFrameChangTime;
    public static long frameLengthInMilliSeconds=100;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private LayerAdapter layerAdapter;
    private boolean isDrawingStarted=false;
    //private long timeThisFrame;
    //private long fps;
    //private TextView fpsText;
    //private CheckBox expensiveBox;
    //public static boolean isExpensive=false;
    public int CENTER_RECT_LEFT,CENTER_RECT_RIGHT,CENTER_RECT_TOP,CENTER_RECT_BOTTOM;
    //public static final Rect CENTER_RECT=new Rect();
    public static RectF CROP_LEFT_TOP_DST_RECT=new RectF();
    public static RectF CROP_LEFT_BOTTOM_DST_RECT=new RectF();
    public static RectF CROP_RIGHT_TOP_DST_RECT=new RectF();
    public static RectF CROP_RIGHT_BOTTOM_DST_RECT=new RectF();
    //private CanvasListener canvasListener;
    private UserFriendlyCanvasListener userFriendlyCanvasListener;
    private Matrix canvasMatrix=new Matrix();
    private Matrix pointMatrix=new Matrix();
    public static RectF CENTER_RECTF=new RectF();
    //public static boolean refreshing;
    private Matrix scaledMatrix=new Matrix();
    public static float[] center_layout=new float[4];

    public void setFillUpperMenu(FillUpperMenu fillUpperMenu) {
        this.fillUpperMenu = fillUpperMenu;
    }

    //private ScaleAndRotateListener scaleAndRotateListener;
    private ResizeListener resizeListener;
    public void setFillBottomMenu(FillBottomMenu fillBottomMenu) {
        this.fillBottomMenu = fillBottomMenu;
    }


    //public Canvas mCanvas;
    //public static int CURRENT_ACTION;
    //public static float[] MAX_POINTS;
    //public Matrix canvasMatrix=new Matrix();

    //private int ptrCount;
    //public float[] TRANSFORM_THE_ORIGIN_AGAINST_FIRST_COORDINATES=new float[]{0.0f,0.0f,WIDTH,HEIGHT};//바뀐 원점을 처음좌표를 기준의 좌표값으로 바꾸기
    //private Paint linepaint;
    //private float[] start_top,end_bottom;

    public DrawingContourView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        init(context);
    }

    public DrawingContourView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public DrawingContourView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        CURRENT_METHOD=DRAW_METHOD;
        draw_mode=pen_mode;
        WINDOW_WIDTH=right-left;
        WINDOW_HEIGHT=bottom-top;
        setCanvasToCenterFactor(WINDOW_WIDTH,WINDOW_HEIGHT,CENTER_FACTOR);
        scaledMatrix.reset();
        if(CENTER_FACTOR[2]==1)
            scaledMatrix.postTranslate(0,CENTER_FACTOR[1]);
        else
            scaledMatrix.postTranslate(CENTER_FACTOR[1],0);
        scaledMatrix.postScale(CENTER_FACTOR[0],CENTER_FACTOR[0],0,0);
        center_layout[0]=0;
        center_layout[1]=0;
        center_layout[2]=WIDTH;
        center_layout[3]=HEIGHT;
        scaledMatrix.mapPoints(center_layout);
        CENTER_RECTF.set(center_layout[0],center_layout[1],center_layout[2],center_layout[3]);
        CANVAS_DST_RECT.set(center_layout[0],center_layout[1],center_layout[2],center_layout[3]);

        //MAX_POINTS=setCanvasMaxPoints(1.0f,0.0f,0.0f,0.0f);

    }

  /*  private void setAnimCanvasFactor() {
        float ratioWidth=(float)WINDOW_WIDTH/WIDTH;
        float ratioHeight=(float)WINDOW_HEIGHT/HEIGHT;

        ratio=ratioWidth<ratioHeight? ratioWidth:ratioHeight;
        translateY=(WINDOW_HEIGHT/ratio-HEIGHT)/2;
    }*/

    private void init(Context context) {
        this.context=context;

        ///////////////////////////////
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        //////////////////////////////

        //currentPath=new Path();
        mBitmapPaint=new Paint();
        mBitmapPaint.setDither(true);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setFlags(Paint.DITHER_FLAG|Paint.FILTER_BITMAP_FLAG|Paint.ANTI_ALIAS_FLAG);


        whitePaint=new Paint();
        whitePaint.setColor(getResources().getColor(R.color.white));
       // whitePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        cropPaint=new Paint();
        cropPaint.setDither(true);
        cropPaint.setFlags(Paint.DITHER_FLAG);
        framePaint=new Paint();
        framePaint.setDither(true);
        framePaint.setFlags(Paint.DITHER_FLAG);

        //CANVAS_DST_RECT=new RectF(0,0,WIDTH,HEIGHT);

        if(left_top_crop==null){
            left_top_crop=BitmapFactory.decodeResource(context.getResources(),R.drawable.left_top_crop);
            left_top_crop=Bitmap.createScaledBitmap(left_top_crop,CROP_UNIT_LENGTH,CROP_UNIT_LENGTH,false);
        }
        if(left_bottom_crop==null){
            left_bottom_crop=BitmapFactory.decodeResource(context.getResources(),R.drawable.left_bottom_crop);
            left_bottom_crop=Bitmap.createScaledBitmap(left_bottom_crop,CROP_UNIT_LENGTH,CROP_UNIT_LENGTH,false);
        }
        if(right_top_crop==null){
            right_top_crop=BitmapFactory.decodeResource(context.getResources(),R.drawable.right_top_crop);
            right_top_crop=Bitmap.createScaledBitmap(right_top_crop,CROP_UNIT_LENGTH,CROP_UNIT_LENGTH,false);
        }
        if(right_bottom_crop==null){
            right_bottom_crop=BitmapFactory.decodeResource(context.getResources(),R.drawable.right_bottom_crop);
            right_bottom_crop=Bitmap.createScaledBitmap(right_bottom_crop,CROP_UNIT_LENGTH,CROP_UNIT_LENGTH,false);
        }
        background= BitmapFactory.decodeResource(getResources(),R.drawable.transparency);

        resizeListener=new ResizeListener(CANVAS_DST_RECT,context);
        //canvasListener=new CanvasListener(this,context);



        userFriendlyCanvasListener=new UserFriendlyCanvasListener(this,context.getResources().getDisplayMetrics().density,CANVAS_DST_RECT);
    }





    /*@Override
    protected void onDraw(Canvas canvas) {

        canvas.save();

        canvas.scale(ScaleAndRotateListener.scaleFactor,ScaleAndRotateListener.scaleFactor
                ,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);

        canvas.translate(ScaleAndRotateListener.mPosX,ScaleAndRotateListener.mPosY);
        canvas.rotate(ScaleAndRotateListener.mAngle,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
        canvas.drawRect(0,0,WIDTH,HEIGHT,whitePaint);

        Frame.currentFrame.layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

        for(int i = Frame.currentFrame.layers.size()-1; i>=0; i--){

            Layer layer=Frame.currentFrame.layers.get(i);
            if(layer.eyeOn) {
                createPathBitmap(layer,canvas);
            }
        }

        if(CURRENT_METHOD==RESIZE_METHOD&&CURRENT_RESIZE_LAYER!=null){
            canvas.save();
            canvas.scale(CURRENT_RESIZE_LAYER.scaleFactor,CURRENT_RESIZE_LAYER.scaleFactor,CURRENT_RESIZE_LAYER.scalePointX,CURRENT_RESIZE_LAYER.scalePointY);
            canvas.translate(CURRENT_RESIZE_LAYER.mPosX,CURRENT_RESIZE_LAYER.mPosY);
            canvas.rotate(CURRENT_RESIZE_LAYER.mAngle,CURRENT_RESIZE_LAYER.scalePointX,CURRENT_RESIZE_LAYER.scalePointY);

            //canvas.drawBitmap(resizeCrop,0,0,mBitmapPaint);
            canvas.drawBitmap(left_top_crop,0,0,mBitmapPaint);
            canvas.drawBitmap(right_top_crop,WIDTH-right_top_crop.getWidth(),0,mBitmapPaint);
            canvas.drawBitmap(left_bottom_crop,0,HEIGHT-left_bottom_crop.getHeight(),mBitmapPaint);//in dp//80
            canvas.drawBitmap(right_bottom_crop,WIDTH-right_bottom_crop.getWidth(),HEIGHT-right_bottom_crop.getHeight(),mBitmapPaint);
            canvas.restore();
        }

        super.onDraw(canvas);

        //mCanvas=canvas;
        canvas.restore();
       // fillBottomMenu.refreshRedoAndUndoImageBt();
    }
*/
  /*  public static void createPathBitmap(Layer layer, Canvas viewCanvas) {
        if(layer.pathBitmap==null) {
        layer.pathBitmap =
                Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_4444);
        layer.pathCanvas=new Canvas(layer.pathBitmap);

        }
        Bitmap pathBitmap = layer.pathBitmap;
        if(layer.pathCanvas==null){
            layer.pathCanvas=new Canvas(pathBitmap);
        }

        Canvas canvas=layer.pathCanvas;
        canvas.save();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);

        viewCanvas.save();
        viewCanvas.scale(layer.scaleFactor,layer.scaleFactor,layer.scalePointX,layer.scalePointY);
        viewCanvas.translate(layer.mPosX,layer.mPosY);
        viewCanvas.rotate(layer.mAngle,layer.scalePointX,layer.scalePointY);

        if(layer.getIsSketch()&&Threshold.FINAL_IMAGE!=null){
            viewCanvas.drawBitmap(Threshold.FINAL_IMAGE,0,0,mBitmapPaint);

        }
        synchronized (layer.pen){
            Iterator<Path> pathIterator=layer.pen.pathArrayList.iterator();
            Iterator<Paint> paintIterator=layer.pen.paintArrayList.iterator();
            while (pathIterator.hasNext()&&paintIterator.hasNext()){
                canvas.drawPath(pathIterator.next(), paintIterator.next());

            }

        }


        canvas.restore();
        layerPaint.setAlpha(layer.layerOpacity);
        viewCanvas.drawBitmap(pathBitmap,0,0,layerPaint);//layout0,1이 window상의 원점을 가르키지 않는 이유
        viewCanvas.restore();
    }
*/
    /*public static float[] setCanvasMaxPoints(float tempScaleFactor,float tempScalePointX,float tempScalePointY,float tempAngle) {

        //현재의 layout에서 중점기준으로 몇 배 더 크게 만들기
        float[] layout=new float[]{0.0f,0.0f,WIDTH,HEIGHT};
        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
       // if(tempScaleFactor>ScaleAndRotateListener.scaleFactor||tempScaleFactor>=){
            scaledMatrix.postScale(1+1/tempScaleFactor,1+1/tempScaleFactor,
                    tempScalePointX,tempScalePointY);
            scaledMatrix.postTranslate(-ScaleAndRotateListener.mPosX,-ScaleAndRotateListener.mPosY);
            scaledMatrix.postRotate(-tempAngle,tempScalePointX,tempScalePointY);
            scaledMatrix.mapPoints(layout);

        final float[] y_coord=new float[]{0,HEIGHT,layout[1],layout[3]};
        final float[] x_coord=new float[]{0,WIDTH,layout[0],layout[2]};
        //float min_x=0,min_y=0,max_x=0,max_y=0;

        float[] new_layout=new float[4];//{min_x,min_y,max_x,max_y};

            for(int i=0;i<y_coord.length;i++){
                if(new_layout[1]>y_coord[i]) {
                    new_layout[1] = y_coord[i];
                }
                if(new_layout[3]<y_coord[i]) {
                    new_layout[3]=y_coord[i];
                }
                if(new_layout[0]>x_coord[i]){
                    new_layout[0]=x_coord[i];
                }
                if(new_layout[2]<x_coord[i]){
                    new_layout[2]=x_coord[i];
                }
            }
     *//*   Log.d("setCanvasMaxPoints", "y_coord| "+ String.valueOf(y_coord[0]) + " | "
                + String.valueOf(y_coord[1])+ " | " +String.valueOf(y_coord[2]) + " | " + String.valueOf(y_coord[3]));
        Log.d("setCanvasMaxPoints", "x_coord| "+ String.valueOf(x_coord[0]) + " | "
                + String.valueOf(x_coord[1])+ " | " +String.valueOf(x_coord[2]) + " | " + String.valueOf(x_coord[3]));
        Log.d("setCanvasMaxPoints", "new layout| "+ String.valueOf(new_layout[0]) + " | "
                  + String.valueOf(new_layout[1])+ " | " +String.valueOf(new_layout[2]) + " | " + String.valueOf(new_layout[3]));
       *//*
        return new_layout;

    }
*//*

    public static float[] toCanvasCoordinate(float x, float y){

        float[] points=new float[]{x,y};
        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
        //setCanvasToCenter(scaledMatrix);
        scaledMatrix.postScale(1/CanvasListener.scaleFactor,1/CanvasListener.scaleFactor,
                CanvasListener.scalePointX,CanvasListener.scalePointY);
        scaledMatrix.postTranslate(-CanvasListener.mPosX,-CanvasListener.mPosY);
        scaledMatrix.postRotate(-CanvasListener.mAngle,CanvasListener.scalePointX,CanvasListener.scalePointY);

        scaledMatrix.mapPoints(points);
        return points;


    }
*/

   /* private static void setCanvasToCenter(Matrix scaledMatrix) {

        scaledMatrix.postScale(1/CENTER_FACTOR[0],1/CENTER_FACTOR[0],0,0);
        scaledMatrix.postTranslate(0,-CENTER_FACTOR[1]);

    }*/
    public static void setCanvasToCenterFactor(int width, int height, float[] factor) {
        float ratioWidth=(float)width/WIDTH;
        float ratioHeight=(float)height/HEIGHT;

        if(ratioWidth<ratioHeight){
            factor[0]=ratioWidth;
            factor[1]=(height/ratioWidth-HEIGHT)/2;
            factor[2]=1;//translateY
        }else {
            factor[0]=ratioHeight;
            factor[1]=(width/ratioHeight-WIDTH)/2;
            factor[2]=0;//translateX
        }



    }
    public static void drawWhiteCanvas(Canvas canvas){

        canvas.scale(CENTER_FACTOR[0],CENTER_FACTOR[0],0,0);
        if(CENTER_FACTOR[2]==1)
            canvas.translate(0,CENTER_FACTOR[1]);
        else
            canvas.translate(CENTER_FACTOR[1],0);
        canvas.drawRect(0,0,WIDTH,HEIGHT,whitePaint);

    }

/*
    public static void setCanvasMatrix() {

        CANVAS_DST_MATRIX.reset();
        CANVAS_DST_MATRIX.postScale(1/Layer.scaleFactor,1/Layer.scaleFactor,
                Layer.scalePointX,Layer.scalePointY);
        CANVAS_DST_MATRIX.postTranslate(-Layer.mPosX,-Layer.mPosY);
        CANVAS_DST_MATRIX.postRotate(-Layer.mAngle,Layer.scalePointX,Layer.scalePointY);

    }*/

    /*public static void toLayerCoordinate(float[] points) {

        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
        scaledMatrix.postTranslate(-ScaleAndRotateListener.mPosX,-ScaleAndRotateListener.mPosY);
        scaledMatrix.postScale(1/ScaleAndRotateListener.scaleFactor,1/ScaleAndRotateListener.scaleFactor,
                ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);

*//*

        //0601
         scaledMatrix.postScale(1/ScaleAndRotateListener.scaleFactor,1/ScaleAndRotateListener.scaleFactor,
                ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
        scaledMatrix.postTranslate(-ScaleAndRotateListener.mPosX,-ScaleAndRotateListener.mPosY);
        scaledMatrix.postRotate(-ScaleAndRotateListener.mAngle,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
*//*

        scaledMatrix.postTranslate(-Frame.currentFrame.currentLayer.mPosX,-Frame.currentFrame.currentLayer.mPosY);
        scaledMatrix.postScale(1/Frame.currentFrame.currentLayer.scaleFactor,1/Frame.currentFrame.currentLayer.scaleFactor,
                Frame.currentFrame.currentLayer.scalePointX,Frame.currentFrame.currentLayer.scalePointY);
       // scaledMatrix.postTranslate(-Frame.currentFrame.currentLayer.mPosX,-Frame.currentFrame.currentLayer.mPosY);
        //scaledMatrix.postRotate(-Frame.currentFrame.currentLayer.mAngle,Frame.currentFrame.currentLayer.scalePointX,Frame.currentFrame.currentLayer.scalePointY);

        scaledMatrix.mapPoints(points);
    }*/


/*    public void calculateDstRect(){

        //layerThread.sendMessage(LayerThread.CANVAS_DST_LECT);
        synchronized (Frame.currentFrame) {
            layoutForCalculate[0]=0;
            layoutForCalculate[1]=0;
            layoutForCalculate[2]=WIDTH;
            layoutForCalculate[3]=HEIGHT;

            //layoutForCalculate=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};
            Matrix scaledMatrix=new Matrix();
            scaledMatrix.reset();

            //scaledMatrix.postRotate(Layer.mAngle,Layer.scalePointX,Layer.scalePointY);
            scaledMatrix.postScale(CanvasListener.scaleFactor,CanvasListener.scaleFactor,
                    CanvasListener.scalePointX,CanvasListener.scalePointY);
            scaledMatrix.postTranslate(CanvasListener.mPosX,CanvasListener.mPosY);
            scaledMatrix.mapPoints(layoutForCalculate);
            CANVAS_DST_RECT.set((int) layoutForCalculate[0], (int) layoutForCalculate[1], (int) layoutForCalculate[2], (int) layoutForCalculate[3]);

        }

      *//*  float[] layout=new float[]{0.0f,0.0f,WIDTH,HEIGHT};
        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();

        //scaledMatrix.postRotate(Layer.mAngle,Layer.scalePointX,Layer.scalePointY);
        scaledMatrix.postScale(CanvasListener.scaleFactor,CanvasListener.scaleFactor,
                CanvasListener.scalePointX,CanvasListener.scalePointY);
        scaledMatrix.postTranslate(CanvasListener.mPosX,CanvasListener.mPosY);
        scaledMatrix.mapPoints(layout);

        //synchronized (Frame.currentFrame.currentLayer){
            CANVAS_DST_RECT.set((int)layout[0],(int)layout[1],(int)layout[2],(int)layout[3]);


        //}

*//*
    }*/



   /* public static float[] getCanvasLayout(String tag, float scaleFactor, float focusX, float focusY,float angle) {
        float[] layout=new float[]{0.0f,0.0f,WIDTH,HEIGHT};//window size=canvas 와 동일

        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
        scaledMatrix.postScale(1/scaleFactor,1/scaleFactor,
                focusX,focusY);
        scaledMatrix.postTranslate(-ScaleAndRotateListener.mPosX,-ScaleAndRotateListener.mPosY);
        scaledMatrix.postRotate(-angle,focusX,focusY);

        scaledMatrix.mapPoints(layout);
     *//*   Log.d(tag, String.valueOf(layout[0]) + " | "
                + String.valueOf(layout[1])+ " | " +String.valueOf(layout[2]) + " | " + String.valueOf(layout[3]));
    *//*    return layout;
    }
*/
   /* public float[] getCanvasLayout(String tag, float mPosX, float mPosY) {
        float[] layout=new float[]{0.0f,0.0f,WIDTH,HEIGHT};//window size=canvas 와 동일

        Matrix scaledMatrix=new Matrix();
        scaledMatrix.reset();
        //setCanvasToCenter(scaledMatrix);
        scaledMatrix.postScale(1/ScaleAndRotateListener.scaleFactor,1/ScaleAndRotateListener.scaleFactor,
                ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
        scaledMatrix.postTranslate(-mPosX,-mPosY);
        scaledMatrix.postRotate(-ScaleAndRotateListener.mAngle,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);

        scaledMatrix.mapPoints(layout);
      *//*  Log.d(tag, String.valueOf(layout[0]) + " | "
                + String.valueOf(layout[1])+ " | " +String.valueOf(layout[2]) + " | " + String.valueOf(layout[3]));
     *//*   return layout;
    }*/

    public void upToDraw(){
        //layerThread.sendMessage(LayerThread.UP_TO_DRAW);
        if(DRAWING_PROCESS){

            touch_up();
            DRAWING_PROCESS=false;
        }
    }

    private void touch_up() {

        if(currentPath!=null)
            currentPath.lineTo(mX,mY);

    }
    public void friendlyDraw(float[] primStartTouchEventXY, Matrix matrix) {

        if (primStartTouchEventXY != null) {
            Matrix invertM=new Matrix();
            matrix.invert(invertM);
            invertM.mapPoints(primStartTouchEventXY);
            //firstInvertMatrix.mapPoints(primStartTouchEventXY);
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
   /* public void downToDraw(float[] primStartTouchEventXY) {

        //layerThread.sendMessage(LayerThread.SET_BEFORE_BITMAP,primStartTouchEventXY);
        if (primStartTouchEventXY != null) {
            toCanvasCoordinate(primStartTouchEventXY);
            //pointMatrix.mapPoints(primStartTouchEventXY);
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
    }*/
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

    public void friendlyMoveToDraw(float[] primStartTouchEventXY, Matrix matrix) {
        if(DRAWING_PROCESS){
            //toCanvasCoordinate(primStartTouchEventXY);
            Matrix invertM=new Matrix();
            matrix.invert(invertM);
            invertM.mapPoints(primStartTouchEventXY);
            final float fx=primStartTouchEventXY[0];//x*values[Matrix.MSCALE_X];
            final float fy=primStartTouchEventXY[1];
            touchMove(fx, fy);
        }
    }

   /* public void moveToDraw(float[] primStartTouchEventXY) {
        //layerThread.sendMessage(LayerThread.MOVE_TO_DRAW,primStartTouchEventXY);

        if(DRAWING_PROCESS){
            //pointMatrix.mapPoints(primStartTouchEventXY);
            toCanvasCoordinate(primStartTouchEventXY);
            final float fx=primStartTouchEventXY[0];//x*values[Matrix.MSCALE_X];
            final float fy=primStartTouchEventXY[1];
            touchMove(fx, fy);
        }
    }*/
    private void touchMove(float x, float y){

        //synchronized (Frame.currentFrame){
        float dx=Math.abs(x-mX);
        float dy=Math.abs(x-mY);
        if(dx>=TOUCH_TOLERRANCE||dy>=TOUCH_TOLERRANCE){
            if(currentPath!=null){
                currentPath.quadTo(mX,
                        mY,
                        ((x+mX)/2),
                        ((y+mY)/2));
                mX=x;
                mY=y;
            }

//            mPath.reset();
//            mPath.addCircle(mX,mY,30,Path.Direction.CW);
        }
        //}

    }
    /*
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

    }*/

    /*public void touchToDraw(int action, float[] primStartTouchEventXY) {

        //final float x=motionEvent.getX(0);
        //final float y=motionEvent.getY(0);
        //float[] values=new float[]{x,y};

        toCanvasCoordinate(primStartTouchEventXY);
        final float fx=primStartTouchEventXY[0];//x*values[Matrix.MSCALE_X];
        final float fy=primStartTouchEventXY[1];//y*values[Matrix.MSCALE_Y];
        if(draw_mode==pen_mode){

            switch (action){
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    touchStart(fx, fy,false);
                    //invalidate();
                    DRAWING_PROCESS=true;
                    Frame.currentFrame.redrawOnBeforeCanvas();

                    break;

                case MotionEvent.ACTION_MOVE:
                        if(DRAWING_PROCESS){
                            touchMove(fx, fy);
                            //invalidate();
                            Frame.currentFrame.redrawOnBeforeCanvas();

                        }
                     break;

                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    touch_up();
                    DRAWING_PROCESS=false;
                    Frame.currentFrame.redrawOnBeforeCanvas();
                    break;

            }

           *//* if (action == MotionEvent.ACTION_DOWN) {


            } else if (DRAWING_PROCESS&&action == MotionEvent.ACTION_MOVE) {


            } else if (DRAWING_PROCESS&&action == MotionEvent.ACTION_UP) {
                touch_up();//fx,fy);

                //invalidate();
                DRAWING_PROCESS=false;
                Frame.currentFrame.redrawOnBeforeCanvas(action,currentPath,drawingPaint);

            }*//*

        }else if(draw_mode==eraser_mode){
            if (action == MotionEvent.ACTION_DOWN) {
                touchStart(fx, fy,true);
                //invalidate();
                DRAWING_PROCESS=true;
                Frame.currentFrame.redrawOnBeforeCanvas();

            } else if (DRAWING_PROCESS&&action == MotionEvent.ACTION_MOVE) {
                touchMove(fx, fy);
                Frame.currentFrame.redrawOnBeforeCanvas();


                //invalidate();
            } else if (DRAWING_PROCESS&&action == MotionEvent.ACTION_UP) {
                touch_up();//fx,fy);
                //invalidate();
                DRAWING_PROCESS=false;
                Frame.currentFrame.redrawOnBeforeCanvas();

            }


        }else if(draw_mode==spoid_mode){
            int layer_size=Frame.currentFrame.layers.size();
            if(layer_size>0){
                for(int i=layer_size-1;i>=0;i--){
                      *//*  float[] layer_coord=new float[]{x,y};
                        toCanvasCoordinate(layer_coord);*//*
                    Bitmap bitmap=Frame.currentFrame.layers.get(i).pathBitmap;
                    //  float[] canvasValues=new float[]{x,y};
                    //  toCanvasCoordinate(canvasValues);
                    if(fx>=0&&fy>=0
                            &&fx<bitmap.getWidth()&&fy<bitmap.getHeight()){
                        int pixel=Frame.currentFrame.layers.get(i).pathBitmap.getPixel((int)fx,(int)fy);
                        int transparency=Color.alpha(pixel);//pixel&0xff000000>>24;
                        if(transparency!=0){
                            setColor(pixel);
                            if(fillUpperMenu.strokeWidth!=null) {
                                fillUpperMenu.strokeWidth.invalidate();
                            }
                            break;

                        }

                    }


                }

            }
        }*//*else if (CURRENT_METHOD == RESIZE_METHOD) {
                    scaleAndRotateListener.onTouchEvent(motionEvent);

                }*//*


    }*/
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {//window input x,y가 canvas기준의 x,y이다

        if(!animationPlaying){
            if(draw_mode==spoid_mode){
                //final float x=motionEvent.getX(0);
                //final float y=motionEvent.getY(0);
                float[] values=new float[]{motionEvent.getX(0),motionEvent.getY(0)};

                //instead of this
                //toCanvasCoordinate(values);
                //changed..
                Matrix invertM=new Matrix();

                userFriendlyCanvasListener.getMatrix().invert(invertM);
                invertM.mapPoints(values);
                final float fx=values[0];//x*values[Matrix.MSCALE_X];
                final float fy=values[1];
                int layer_size=Frame.currentFrame.layers.size();
                if(layer_size>0){
                    for(int i=layer_size-1;i>=0;i--){
                      /*  float[] layer_coord=new float[]{x,y};
                        toCanvasCoordinate(layer_coord);*/
                        Bitmap bitmap=Frame.currentFrame.layers.get(i).pathBitmap;
                        //  float[] canvasValues=new float[]{x,y};
                        //  toCanvasCoordinate(canvasValues);
                        if(fx>=0&&fy>=0
                                &&fx<bitmap.getWidth()&&fy<bitmap.getHeight()){
                            int pixel=Frame.currentFrame.layers.get(i).pathBitmap.getPixel((int)fx,(int)fy);
                            int transparency=Color.alpha(pixel);//pixel&0xff000000>>24;
                            if(transparency!=0){
                                setColor(pixel);
                                if(fillUpperMenu.strokeWidth!=null) {
                                    fillUpperMenu.strokeWidth.invalidate();
                                }
                                break;

                            }

                        }


                    }

                }
            }/*else if(scaleAndRotateListener.onTouchEvent(motionEvent)){
                Frame.currentFrame.resetBeforeCanvas();
                //return true;
            }*/else if (isCurrentResizeMethod()) {

               // layerScaleAndRotateListener.onTouchEvent(motionEvent);
                resizeListener.onTouchEvent(motionEvent);
            }
            else {
                //scaleAndRotateListener.onTouchEvent(motionEvent);
                if(userFriendlyCanvasListener.onTouchEvent(motionEvent))
                    Frame.currentFrame.redrawOnBeforeCanvas();
                else {

                    Frame.currentFrame.redrawOnBeforeCanvas(currentPath,drawingPaint);
                }
                //layerThread.sendMessage(LayerThread.REDRAW_LAYER);

            }
            /*else if(action == MotionEvent.ACTION_DOWN
                    //&&CURRENT_METHOD==LAYER_METHOD
                    &&CURRENT_METHOD==SELECT_METHOD){
                CURRENT_METHOD=DRAW_METHOD;

            } */
            /*else if (CURRENT_METHOD == ZOOM_METHOD) {
                scaleAndRotateListener.onTouchEvent(motionEvent);

            }*/



        }
            return true;

    }

 /*   public static void restartdrawingThread() {
       // resume();
        refreshing=false;
    }

    public static void stopdrawingThread() {
       // pause();
        refreshing=true;
    }*/




    public synchronized void onClickUndo(){

           /* List<Path> lastPaths = Frame.currentFrame.layers.get(Frame.currentFrame.LAYER_HISTORY.get(Frame.currentFrame.LAYER_HISTORY.size() - 1))
                    .pathArrayList;
            List<Paint> lastPaints =  Frame.currentFrame.layers.get(Frame.currentFrame.LAYER_HISTORY.get(Frame.currentFrame.LAYER_HISTORY.size() - 1))
                    .paintArrayList;*/
          /*  if (lastPaths.size() > 0 && lastPaints.size() > 0) {
                Frame.currentFrame.undoPath.add(lastPaths.remove(lastPaths.size() - 1));
                Frame.currentFrame.undoPaint.add(lastPaints.remove(lastPaints.size() - 1));
                Frame.currentFrame.undoLayerHistory.add(
                        Frame.currentFrame.LAYER_HISTORY.remove(Frame.currentFrame.LAYER_HISTORY.size() - 1));
                //invalidate();
            }*/
          Frame.currentFrame.undo();
                  //layers.get(Frame.currentFrame.LAYER_HISTORY.get(Frame.currentFrame.LAYER_HISTORY.size() - 1)).undo();//pen;




    }
    public synchronized void onClickRedo(){
        Frame.currentFrame.redo();

       /* if(Frame.currentFrame.undoPath.size()>0&&Frame.currentFrame.undoPaint.size()>0&&Frame.currentFrame.undoLayerHistory.size()>0){
            if( Frame.currentFrame.layers.size()>0) {

                Frame.currentFrame.LAYER_HISTORY.add(Frame.currentFrame.undoLayerHistory.remove(Frame.currentFrame.undoLayerHistory.size() - 1));

                Frame.currentFrame.layers.get(
                        Frame.currentFrame.LAYER_HISTORY.get(Frame.currentFrame.LAYER_HISTORY.size() - 1)).redo(
                        Frame.currentFrame.undoPath.remove(Frame.currentFrame.undoPath.size() - 1)
                       ,Frame.currentFrame.undoPaint.remove(Frame.currentFrame.undoPaint.size() - 1)
                );



            }
        }*/


    }


    public void reset() {
//        CanvasListener.scaleFactor=1;
//        CanvasListener.scalePointX=0;CanvasListener.scalePointY=0;
//        CanvasListener.mPosX=0;CanvasListener.mPosY=0;
//        CanvasListener.mAngle=0;

        synchronized (Frame.currentFrame){
            canvasMatrix.reset();
            pointMatrix.reset();
            userFriendlyCanvasListener.reset();

        }

        //Frame.currentFrame.reset();

        //invalidate();
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        resume();
        //this.setRunning

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        pause();
    }

    public void pause(){

        boolean retry=true;
        playing=false;
        while (retry){
            try {
                animThread.join();
                retry=false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resume(){
        playing=true;
        animThread=new Thread(this);
        animThread.start();

    }

    private void draw() {

        Canvas canvas=surfaceHolder.lockCanvas();
        try {
            synchronized (surfaceHolder){
                //getCurrentFrame();
                //invalidate();
                //onDraw(canvas);
                if(canvas!=null){
                    canvas.drawColor(Color.LTGRAY);

                    if(animationPlaying){
                        getCurrentFrame();

                       /* canvas.scale(ratio,ratio,0,0);
                        canvas.translate(0,translateY);
                        canvas.drawRect(0,0,WIDTH,HEIGHT,whitePaint);*/
                        //drawWhiteCanvas(canvas);
                        canvas.drawRect(CENTER_RECTF,whitePaint);
                        canvas.drawBitmap(layerAdapter.frames.get(currentFrame).layersBitmap, null,CENTER_RECTF,mBitmapPaint);
                    }else {


                       // Frame.currentFrame.layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                    //synchronized (layerAdapter.frames){
                       // synchronized (Frame.currentFrame){
                           // synchronized (Frame.currentFrame){
                                canvas.save();
                              /*
                              //0531
                               canvas.scale(ScaleAndRotateListener.scaleFactor,ScaleAndRotateListener.scaleFactor
                                        ,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);

                                canvas.translate(ScaleAndRotateListener.mPosX,ScaleAndRotateListener.mPosY);
                                canvas.rotate(ScaleAndRotateListener.mAngle,ScaleAndRotateListener.scalePointX,ScaleAndRotateListener.scalePointY);
                                */
                                //canvas.drawRect(0,0,WIDTH,HEIGHT,whitePaint);
                                //}

                        if(isCurrentResizeMethod())
                            Frame.currentFrame.drawLayers(canvas,CENTER_RECTF);
                        else
                            Frame.currentFrame.drawLayers(canvas,CANVAS_DST_RECT);


                        //0601
                            //canvas.drawBitmap(Frame.currentFrame.layersBitmap,0,0,framePaint);

                        //}
                        //mCanvas=canvas;
                    //}

                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                if(!isDrawingStarted){
                                    isDrawingStarted=true;
                                    //Frame.currentFrame.redrawForSave();

                                    try {
                                        Thread.sleep(2000);

                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                frameLayout.removeView(progressBar);
                                            }
                                        });

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fillBottomMenu.refreshRedoAndUndoImageBt();

                                    }
                                });
                            }
                        }).start();


                    }


                }

            }
        }finally {
            if(canvas!=null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void getCurrentFrame() {
        long time=System.currentTimeMillis();
        if(time-lastFrameChangTime>frameLengthInMilliSeconds){
            lastFrameChangTime=time;
            currentFrame++;

        }
        if(currentFrame>=FRAME_COUNT){
            currentFrame=0;
        }
        /*if(currentBitmap!=null&&!currentBitmap.isRecycled())
            currentBitmap.recycle();*/
        //currentBitmap= LayerAdapter.frames.get(currentFrame).layersBitmap;

    }

    @Override
    public void run() {
        while (playing){

            draw();
           /* long startFrameTime=System.currentTimeMillis();
                draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fpsText.setText(String.valueOf(fps));
                    }
                });
            }*/

        }
    }

    public void animStart() {
        FRAME_COUNT=layerAdapter.frames.size();
        animationPlaying=true;
        //setAnimCanvasFactor();
    }

    public void animStop() {
        animationPlaying=false;

    }

    public void setProgressBar(FrameLayout frameLayout, ProgressBar progressBar) {
        this.frameLayout=frameLayout;
        this.progressBar=progressBar;
    }

    public void removeAllFrames() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean retry=true;
                while(!animThread.isAlive()

                        &&retry){
                    synchronized (layerAdapter.frames){
                        if(Frame.currentFrame.framesBitmap!=null&&!Frame.currentFrame.framesBitmap.isRecycled()){
                            Frame.currentFrame.framesBitmap.recycle();
                            Frame.currentFrame.framesBitmap=null;
                        }

                        Iterator<Frame> iterator=layerAdapter.frames.iterator();
                        while(iterator.hasNext()){
                            Frame frame=iterator.next();
                            if(frame.layersBitmap!=null&&!frame.layersBitmap.isRecycled()){
                                frame.layersBitmap.recycle();
                                frame.layersBitmap=null;
                            }

                            synchronized (frame.layers){
                                Iterator<Layer> layerIterator=frame.layers.iterator();
                                while(layerIterator.hasNext()){
                                    Layer layer=layerIterator.next();
                                    if(layer.pathBitmap!=null&&!layer.pathBitmap.isRecycled()){
                                        layer.pathBitmap.recycle();
                                        layer.pathBitmap=null;
                                    }
                                    if(layer.pathBitmapBefore!=null&&!layer.pathBitmapBefore.isRecycled()){
                                        layer.pathBitmapBefore.recycle();
                                        layer.pathBitmapBefore=null;
                                    }
                                    if(layer.resizedBitmap!=null&&!layer.resizedBitmap.isRecycled()){
                                        layer.resizedBitmap.recycle();
                                        layer.resizedBitmap=null;
                                    }

                                }
                            }
                        }
                    }

                    layerAdapter.frames.clear();
                    if(GetImage.imageViewBitmap!=null&&!GetImage.imageViewBitmap.isRecycled()){
                        GetImage.imageViewBitmap.recycle();
                        GetImage.imageViewBitmap=null;

                    }
                    if(ThreshHandler.FINAL_IMAGE!=null&&!ThreshHandler.FINAL_IMAGE.isRecycled()){
                        ThreshHandler.FINAL_IMAGE.recycle();
                        ThreshHandler.FINAL_IMAGE=null;

                    }
                    retry=false;
                }
            }
        }).start();


    }

    public void setLayerAdapter(LayerAdapter layerAdapter) {
        this.layerAdapter=layerAdapter;
    }

    public void removeCurrentPen() {
        Frame.currentFrame.removeCurrentPen();
        currentPath=null;drawingPaint=null;

        //layerThread.sendMessage(LayerThread.REMOVE_CURRENT_PATH);
    }


    public void setCanvasDstRect(float scalePointX, float scalePointY, float scaleFactor) {

        synchronized (Frame.currentFrame){
            float[] layout=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};

            canvasMatrix.postScale(scaleFactor,scaleFactor,
                    scalePointX,scalePointY);
            //canvasMatrix.postTranslate(CanvasListener.mPosX,CanvasListener.mPosY);
            canvasMatrix.mapPoints(layout);

            CANVAS_DST_RECT.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);

            //pointMatrix.postTranslate(-CanvasListener.mPosX,-CanvasListener.mPosY);

            pointMatrix.preScale(1/scaleFactor,1/scaleFactor,
                    scalePointX,scalePointY);
        }


    }

    public void setCanvasDstRect(float diffSecX, float diffSecY) {
        synchronized (Frame.currentFrame){
            float[] layout=new float[]{0.0f,0.0f, DrawingContourView.WIDTH,DrawingContourView.HEIGHT};
            //float[] center=new float[]{scalePointX,scalePointY};
            //canvasMatrix.mapPoints(center);
            canvasMatrix.postTranslate(diffSecX,diffSecY);
            //canvasMatrix.postTranslate(CanvasListener.mPosX,CanvasListener.mPosY);
            canvasMatrix.mapPoints(layout);
            CANVAS_DST_RECT.set((int) layout[0], (int) layout[1], (int) layout[2], (int) layout[3]);

            //pointMatrix.postTranslate(-CanvasListener.mPosX,-CanvasListener.mPosY);
            pointMatrix.preTranslate(-diffSecX,-diffSecY);
        }

    }
    public static synchronized int getColor(){
        return CURRENT_COLOR;
    }
    public static synchronized void setColor(int color) {
        CURRENT_COLOR=color;
    }
    public static synchronized void changeStrokeWidth(int i) {
        PAINT_STROKE_WIDTH=i;
        // BLUR_RADIUS=PAINT_STROKE_WIDTH/2;

    }
    public static synchronized void changeStrokeGradientWidth(int i) {
        BLUR_RADIUS=i;
    }

    public synchronized int getCurrentMethod(){
        return CURRENT_METHOD;
    }
    public static synchronized boolean isCurrentResizeMethod(){
        return CURRENT_METHOD==RESIZE_METHOD;
    }

    public static void getPaint(Paint paint, boolean eraserMode) {
        paint.setDither(true);
        paint.setAntiAlias(true);

        paint.setFlags(Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG);
        //paint.setDither(true);
        // paint.setFilterBitmap(true);


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(PAINT_STROKE_WIDTH);
        if(LINE_KIND==GRADIENT_LINE&&BLUR_RADIUS!=0) {
            paint.setMaskFilter(new BlurMaskFilter(BLUR_RADIUS, BlurMaskFilter.Blur.NORMAL));
        }
        if(eraserMode) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            paint.setColor(Color.TRANSPARENT);
        }else {
            paint.setColor(CURRENT_COLOR);
        }
    }









   /* public void setCenterRectF() {
        CENTER_RECTF.set(CENTER_RECT);
    }*/




    /*public void setFpsText(TextView fpsText) {
        this.fpsText = fpsText;
    }*/
/*
    public void setExpensiveBox(CheckBox expensiveBox) {
        expensiveBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isExpensive=isChecked;
            }
        });
    }*/



   /* public static void cleanAllPath(){
        //add_points=new ArrayList<>();
        //minus_points=new ArrayList<>();
       // addPath.reset();
        minusPath.reset();

    }
    public static void restart(){
        add_points=new ArrayList<>();
        minus_points=new ArrayList<>();
        addPath.reset();
        minusPath.reset();
        ZoomListener.scaleFactor=1;
    }*/

   /* private void makeContour(final int x, final int y) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                MakeContours makeContours = new MakeContours();
                Message msg = new Message();
                msg.what = MakeContours.MSG_MAKE_CONTOURS;
                Bundle bundle = new Bundle();
                bundle.putInt(MakeContours.TOUCHED_X, x);
                bundle.putInt(MakeContours.TOUCHED_Y, y);
                msg.setData(bundle);
                makeContours.sendMessage(msg);
                Looper.loop();
            }
        }).start();



    }*/


}
