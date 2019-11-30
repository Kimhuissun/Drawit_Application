package com.makeit.sunnycar.drawit.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.ui.FillSketch;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ThreshHandler extends Handler {

    public static final String Sketch_Outline="Sketch Outline";
    public static final String Photo_Outline="Photo Outline";
    //public static final int ORIGINAL=0;
    public static final int THRESH_BINARY=0;
    public static final int THRESH_BINARY_INV=1;
    public static final int THRESH_TRUNC=2;
    public static final int THRESH_TOZERO=3;
    public static final int ADAPTIVE_THRESH_MEAN=4;
    public static final int ADAPTIVE_THRESH_GAUSSIAN=5;
    public static final int THRESH_OTSU=6;
    public static final int PHOTO_TO_COLOR_SKETCH = 7;
    public static final int PHOTO_TO_BLACK_SKETCH = 8;

    public static final int BLACK_AND_WHITE=9;

    public static final int PHOTO_THRESH_COUNT=10;
    //public static final int CREATE_PHOTO_VIEW = 0;

    public static final int THRESH_VALUE_MAX = 126;//255;
    //public static final int THRESH_PHOTO_VIEW = 1;
    public static final int KERNEL_SIZE_MAX = 150;//300;
    private static final int BAR_UNIT = 2;
    private static final int BAR_START = 3;

    public static int KERNEL_SIZE=5;
    public static int THRESH_VALUE=24;

    public static int METHOD=0;
    //public static final int ORIGINAL_PHOTO = 0;
    //public static final int PHOTO_OUTLINE=1;

   /* public static final int[] THRESHOLD_TYPE=new int[]{
            Imgproc.THRESH_BINARY,
            Imgproc.THRESH_BINARY_INV,
            Imgproc.THRESH_TRUNC,
            Imgproc.THRESH_TOZERO,
            Imgproc.THRESH_TOZERO_INV,
            Imgproc.THRESH_OTSU

    };
    public static final int[] ADAPTIVE_THRESHOLD=new int[]{

            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C

    };*/
    //public static final int POINT_MAX_LENTH=100;
    //public static final int MAX_SAMPLE_SIZE=3;
    //public static final int TWO=2;

    //public static final int MAX_QUALITY=100;

    //public static int MAX_PAINT_STROKE_WIDTH=20;
    //public static int PAINT_STROKE_WIDTH=1;
    public static int PHOTO_THRESH_METHOD=0;
    public static boolean ERODE_METHOD=false;
    public static boolean DILATE_METHOD=false;
    public static boolean GAUSSIAN_METHOD=false;

    public static int GAUSSIAN_SIZE=5;
    private final RelativeLayout relativeLayout;
    private final ImageView imageView;
    private final Context context;
    private final SeekBar kernerBar;
    private boolean forDraw;
   /* public static final String tabTitles[]=new String[]{
           // Sketch_Outline,
            Photo_Outline
            //Photo_To_Sketch

    };*/
    //public static final int PAGE_COUNT=1;
    //public static final int SKETCH_OUTLINE=0;
    //public static final int PHOTO_TO_SKETCH=2;
    public static Bitmap FINAL_IMAGE;
    private ProgressBar progressBar;
    private final ImageButton[] photoButtons;
    //private boolean fromMain;

    public ThreshHandler(RelativeLayout relativeLayout, ImageButton[] photoButtons, SeekBar kernerBar, ImageView imageView, Context context) {
        this.relativeLayout = relativeLayout;
        this.imageView = imageView;
        this.context = context;
        this.photoButtons=photoButtons;
        this.kernerBar=kernerBar;
       // this.fromMain=fromMain;
    }

    public void handleMessage(Message message) {
      if (GetImage.imageViewBitmap != null) {
            Mat orgMap=new Mat();
            Utils.bitmapToMat(GetImage.imageViewBitmap, orgMap);
            //Mat img_gray = new Mat();
            //Log.e("img_gray size",img_gray.rows()+" "+img_gray.cols());
            //Log.e("orgMap size",orgMap.rows()+" "+orgMap.cols());

            //if(METHOD==PHOTO_OUTLINE){
/*
                        //0615
                        Imgproc.Canny(img_gray,img_gray,100,200);
                        //Imgproc.GaussianBlur(thresh_result,thresh_result,new Size(5,5),0);
                        //Imgproc.erode(thresh_result,thresh_result,new Mat());
                        //Imgproc.dilate(thresh_result,thresh_result,new Mat());
                        List<MatOfPoint> contours=new ArrayList<>();
                        //Mat mask=Mat.zeros(img_gray.rows(),img_gray.cols(), CvType.CV_8UC1);
                        Mat crop=new Mat(img_gray.rows(),img_gray.cols(), CvType.CV_8UC4);
                        crop.setTo(new Scalar(0,0,0,0));
                        Imgproc.findContours(img_gray,contours,new Mat(),Imgproc.RETR_TREE,
                                Imgproc.CHAIN_APPROX_SIMPLE);
                        Scalar black =new Scalar(0,0,0,255);
                        for(int i=0;i<contours.size();i++){
                            Imgproc.drawContours(crop,contours,i,black, (int) KERNEL_SIZE);
                        }
                          FINAL_IMAGE=Bitmap.createBitmap(GetImage.imageViewBitmap.getWidth(),
                                GetImage.imageViewBitmap.getHeight(),
                                Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(crop,FINAL_IMAGE);
                        */

                //orgMap.copyTo(crop,mask);
                //Mat thresh_result = new Mat();

                int kernel_size=getKernelValue();
                int thresh_value=getThreshValue();
                switch (PHOTO_THRESH_METHOD){

                    case THRESH_BINARY: {
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);
                        Imgproc.threshold(orgMap,orgMap,thresh_value,255,Imgproc.THRESH_BINARY);

                        break;
                    }
                    case THRESH_BINARY_INV:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);
                        Imgproc.threshold(orgMap,orgMap,thresh_value,255,Imgproc.THRESH_BINARY_INV);

                        break;
                    }
                    case THRESH_TRUNC:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);
                        Imgproc.threshold(orgMap,orgMap,thresh_value,255,Imgproc.THRESH_TRUNC);
                        break;
                    }
                    case THRESH_TOZERO:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);

                        Imgproc.threshold(orgMap,orgMap,thresh_value,255,Imgproc.THRESH_TOZERO);

                        break;
                    }
                    case ADAPTIVE_THRESH_MEAN:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);

                        Imgproc.adaptiveThreshold(orgMap,orgMap,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,kernel_size,10);

                        break;
                    }
                    case ADAPTIVE_THRESH_GAUSSIAN:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);

                        Imgproc.adaptiveThreshold(orgMap,orgMap,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,kernel_size,10);

                        break;
                    }
                    case BLACK_AND_WHITE:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);

                        break;
                    }
                    case THRESH_OTSU:{
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_BGR2GRAY);

                        Imgproc.GaussianBlur(orgMap,orgMap,new Size(kernel_size,kernel_size),0);
                        Imgproc.threshold(orgMap,orgMap,0,255,Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);
                        break;
                    }
                    case PHOTO_TO_COLOR_SKETCH:{
                        Mat gray=new Mat();
                        Imgproc.cvtColor(orgMap, gray, Imgproc.COLOR_BGR2GRAY);

                        if(!forDraw){
                            Mat invert=new Mat();
                            Mat blur=new Mat();
                            Core.bitwise_not(gray,invert);
                            Imgproc.GaussianBlur(invert,blur,new Size(kernel_size,kernel_size),0);
                            int WIDTH = orgMap.cols();
                            int HEIGHT = orgMap.rows();
                            int STRIDE = orgMap.cols();
                            int[] colors = new int[HEIGHT * WIDTH];
                            double color[];
                            double org_color[];

                            for (int i = 0; i < HEIGHT; i++) {
                                for (int j = 0; j < WIDTH; j++) {

                                    color = blur.get(i, j);
                                    org_color=orgMap.get(i,j);
                                    if((int)color[0]==255) {
                                        color[0]=255;
                                    }else {
                                        color[0]=((float)gray.get(i,j)[0])*Math.pow(2,8)/(255-(float)color[0]);
                                        if(color[0]>255)
                                            color[0]=255;

                                    }

                                    int r= (int) ((1-org_color[0]/255)*color[0]+org_color[0]);
                                    int g= (int) ((1-org_color[1]/255)*color[0]+org_color[1]);
                                    int b= (int) ((1-org_color[2]/255)*color[0]+org_color[2]);
                                    //img_gray.put(i,j,color);

                                    colors[STRIDE * i + j] = ((int) (255-color[0]) << 24)
                                            | (r << 16)
                                            | (g << 8)
                                            | b ;
                                    //color[1]=color[0];

                                }
                            }
                            FINAL_IMAGE = Bitmap.createBitmap(colors, 0, STRIDE,
                                    WIDTH,
                                    HEIGHT,
                                    Bitmap.Config.ARGB_8888);
                        }

                        break;
                    }
                    case PHOTO_TO_BLACK_SKETCH:{
                        Mat gray=new Mat();
                        Imgproc.cvtColor(orgMap, gray, Imgproc.COLOR_BGR2GRAY);

                        if(!forDraw){
                            Mat invert=new Mat();
                            Mat blur=new Mat();
                            Core.bitwise_not(gray,invert);
                            Imgproc.GaussianBlur(invert,blur,new Size(kernel_size,kernel_size),0);
                            int WIDTH = orgMap.cols();
                            int HEIGHT = orgMap.rows();
                            int STRIDE = orgMap.cols();
                            int[] colors = new int[HEIGHT * WIDTH];
                            double color[];

                            for (int i = 0; i < HEIGHT; i++) {
                                for (int j = 0; j < WIDTH; j++) {

                                    color = blur.get(i, j);
                                    if((int)color[0]==255) {
                                        color[0]=255;
                                    }else {
                                        color[0]=((float)gray.get(i,j)[0])*Math.pow(2,8)/(255-(float)color[0]);
                                        if(color[0]>255)
                                            color[0]=255;

                                    }

                                    colors[STRIDE * i + j] = ((int) (255-color[0]) << 24)
                                            | ((int) (color[0]) << 16)
                                            | ((int) (color[0]) << 8)
                                            | (int) (color[0]) ;
                                    //color[1]=color[0];

                                }
                            }
                            FINAL_IMAGE = Bitmap.createBitmap(colors, 0, STRIDE,
                                    WIDTH,
                                    HEIGHT,
                                    Bitmap.Config.ARGB_8888);
                        }


                        break;
                    }
                }
               /* if(ERODE_METHOD)
                    Imgproc.erode(img_gray,img_gray,
                            Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new
                                    Size(2*KERNEL_SIZE+1,2*KERNEL_SIZE+1),new Point(KERNEL_SIZE,KERNEL_SIZE)));
                else if(DILATE_METHOD)
                    Imgproc.dilate(img_gray,img_gray,
                            Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new
                                    Size(2*KERNEL_SIZE+1,2*KERNEL_SIZE+1),new Point(KERNEL_SIZE,KERNEL_SIZE)));
                if(GAUSSIAN_METHOD)
                    Imgproc.GaussianBlur(img_gray,img_gray,new Size(GAUSSIAN_SIZE,GAUSSIAN_SIZE),0);
*/
                //Imgproc.adaptiveThreshold(img_gray,thresh_result,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,(int) KERNEL_SIZE,10);

                if(PHOTO_THRESH_METHOD!=PHOTO_TO_BLACK_SKETCH
                        &&PHOTO_THRESH_METHOD!=PHOTO_TO_COLOR_SKETCH){
                    if(forDraw){
                        Imgproc.cvtColor(orgMap, orgMap, Imgproc.COLOR_GRAY2RGBA);

                        int WIDTH = orgMap.cols();
                        int HEIGHT = orgMap.rows();
                        int[] colors = new int[HEIGHT * WIDTH];
                        int STRIDE = orgMap.cols();
                        double color[];

                        for (int i = 0; i < HEIGHT; i++) {
                            //int black_count=0;
                            for (int j = 0; j < WIDTH; j++) {
                                color = orgMap.get(i, j);
                                color[3]=255-color[0];
                                colors[STRIDE * i + j] = ((int) (color[3]) << 24)
                                        | ((int) (color[0]) << 16)
                                        | ((int) (color[1]) << 8)
                                        | (int) color[2];

                            }

                        }
                        FINAL_IMAGE = Bitmap.createBitmap(colors, 0, STRIDE,
                                WIDTH,
                                HEIGHT,
                                Bitmap.Config.ARGB_8888);

                        //Thread.currentThread().interrupt();

                    }else {
                        //synchronized (FINAL_IMAGE){
                        FINAL_IMAGE=Bitmap.createBitmap(GetImage.imageViewBitmap.getWidth(),
                                GetImage.imageViewBitmap.getHeight(), Bitmap.Config.ARGB_4444);
                        Utils.matToBitmap(orgMap,FINAL_IMAGE);
                        //}

                    }
                }


            //}
            /*else if(METHOD==SKETCH_OUTLINE){
                Mat thresh_result = new Mat();
*//*
                        if(TuneDialog.METHOD==0){
                            Imgproc.threshold(img_gray,thresh_result,
                                    (double) TuneDialog.THRESH_VALUE,255,TuneDialog.TYPE);*//*
                //}else if(TuneDialog.METHOD==1){
//
//                        if(adaptive_method==0) ADAPTIVE_METHOD=Imgproc.ADAPTIVE_THRESH_MEAN_C;
//                        else if(adaptive_method==1) ADAPTIVE_METHOD=Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C;

                           *//* Imgproc.adaptiveThreshold(img_gray,thresh_result,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,
                                    Imgproc.THRESH_BINARY,7,10);
*//*
                //}

                //Imgproc.GaussianBlur(img_gray,thresh_result,new Size(5,5),0);
                //Imgproc.threshold(thresh_result,thresh_result,127,255,Imgproc.THRESH_BINARY+Imgproc.THRESH_OTSU);

                Imgproc.adaptiveThreshold(img_gray,thresh_result,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,7,10);
                if(ERODE_METHOD)
                    Imgproc.erode(thresh_result,thresh_result,
                            Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new
                                    Size(2*KERNEL_SIZE+1,2*KERNEL_SIZE+1),new Point(KERNEL_SIZE,KERNEL_SIZE)));
                else if(DILATE_METHOD)
                    Imgproc.dilate(thresh_result,thresh_result,
                            Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new
                                    Size(2*KERNEL_SIZE+1,2*KERNEL_SIZE+1),new Point(KERNEL_SIZE,KERNEL_SIZE)));
                if(GAUSSIAN_METHOD)
                    Imgproc.GaussianBlur(thresh_result,thresh_result,new Size(GAUSSIAN_SIZE,GAUSSIAN_SIZE),0);

                if(forDraw){
                    Imgproc.cvtColor(thresh_result, thresh_result, Imgproc.COLOR_GRAY2RGBA);

                    int WIDTH = thresh_result.cols();
                    int HEIGHT = thresh_result.rows();
                    int[] colors = new int[HEIGHT * WIDTH];
                    int STRIDE = thresh_result.cols();
                    double color[];

                       *//* FINAL_IMAGE=Bitmap.createBitmap(ORG_IMAGE.getWidth(),ORG_IMAGE.getHeight(), Bitmap.Config.ARGB_4444);
                        Canvas canvas=new Canvas(FINAL_IMAGE);
                        Paint paint=new Paint();
                        paint.setColor(Color.BLACK);*//*
                    for (int i = 0; i < HEIGHT; i++) {
                        //int black_count=0;
                        for (int j = 0; j < WIDTH; j++) {
                            color = thresh_result.get(i, j);

                              *//*  if (color[0] == 255
                                        && color[1] == 255
                                        && color[2] == 255) {//white

                                    color[3] = 0;//투명
                                } else {
                                    //색에 따라 투명도도 조절
                                    //color[3] = 255;
                                    //canvas.drawCircle(j,i,Threshold.PAINT_STROKE_WIDTH,paint);
                                }*//*
                             *//* if(color[0]==0&&
                                      color[1]==0&&
                                      color[2]==0){
                                  if(!isBlackCountStarted){
                                      isBlackCountStarted=true;
                                      black_count=0;
                                  }
                                  black_count++;

                              }else if(color[0] == 255
                                      && color[1] == 255
                                      && color[2] == 255){

                                  if(isBlackCountStarted){
                                      if(black_count<KERNEL_SIZE){

                                          color[0]=0;
                                          color[1]=0;
                                          color[2]=0;
                                          color[3]=255;
                                          black_count++;

                                      }
                                      else {
                                          color[3]=0;
                                          //black_count=0;
                                          isBlackCountStarted=false;

                                      }
                                  }else color[3]=0;

                              }



                             *//*
                            color[3]=255-color[0];
                            colors[STRIDE * i + j] = ((int) (color[3]) << 24)
                                    | ((int) (color[0]) << 16)
                                    | ((int) (color[1]) << 8)
                                    | (int) color[2];

                        }

                    }
                    FINAL_IMAGE = Bitmap.createBitmap(colors, 0, STRIDE,
                            WIDTH,
                            HEIGHT,
                            Bitmap.Config.ARGB_8888);
                }else {
                    //synchronized (FINAL_IMAGE){
                        FINAL_IMAGE=Bitmap.createBitmap(GetImage.imageViewBitmap.getWidth(),
                                GetImage.imageViewBitmap.getHeight(), Bitmap.Config.ARGB_4444);
                        Utils.matToBitmap(thresh_result,FINAL_IMAGE);
                    //}

                }

                //MatOfByte buffer=new MatOfByte();
                //Imgcodecs.imencode(".png",thresh,buffer);
                //final_bitmap= BitmapFactory.decodeStream(
                //        new ByteArrayInputStream(buffer.toArray()));
//

                //imageView.performClick();
                //imageView.setOnTouchListener(FindContours.this);
//                            Log.d("img",
//                                    "width:"+String.valueOf(img.width())+" | "
//                                    +"height:"+String.valueOf(img.height())+" | "
//                                    +"rows:"+String.valueOf(img.rows())+"|"
//                                    +"cols:"+String.valueOf(img.cols()));
//
//                            Log.d("thresh_result",
//                                       "width:"+String.valueOf(thresh_result.width())+" | "
//                                            +"height:"+String.valueOf(thresh_result.height())
//                                            +"rows:"+String.valueOf(thresh_result.rows())+" | "
//                                            +"cols:"+String.valueOf(thresh_result.cols())+"|");
//                            Log.d("result", "width:"+String.valueOf(FINAL_IMAGE.getWidth())+" | "
//                                    +"height:"+String.valueOf(FINAL_IMAGE.getHeight()));
//                            Log.d("imageView", "width:"+String.valueOf(imageView.getWidth())+" | "
//                                    +"height:"+String.valueOf(imageView.getHeight()));

            }*//*else if(METHOD==PHOTO_TO_SKETCH){
                        Imgproc.cvtColor(img_gray, img_gray, Imgproc.COLOR_GRAY2RGBA);
                        int WIDTH = img_gray.cols();
                        int HEIGHT = img_gray.rows();
                        int[] colors = new int[HEIGHT * WIDTH];
                        int STRIDE = img_gray.cols();
                        double color[];

                       *//* FINAL_IMAGE=Bitmap.createBitmap(ORG_IMAGE.getWidth(),ORG_IMAGE.getHeight(), Bitmap.Config.ARGB_4444);
                        Canvas canvas=new Canvas(FINAL_IMAGE);
                        Paint paint=new Paint();
                        paint.setColor(Color.BLACK);*//*
                        for (int i = 0; i < HEIGHT; i++) {
                            for (int j = 0; j < WIDTH; j++) {
                                color = img_gray.get(i, j);

                              *//*  if (color[0] == 255
                                        && color[1] == 255
                                        && color[2] == 255) {//white

                                    color[3] = 0;//투명
                                } else {
                                    //색에 따라 투명도도 조절
                                    //color[3] = 255;
                                    //canvas.drawCircle(j,i,Threshold.PAINT_STROKE_WIDTH,paint);
                                }*//*
                                color[3]=255-color[0];
                                colors[STRIDE * i + j] = ((int) (color[3]) << 24)
                                        | ((int) (color[0]) << 16)
                                        | ((int) (color[1]) << 8)
                                        | (int) color[2];
                            }
                        }
                        FINAL_IMAGE = Bitmap.createBitmap(colors, 0, STRIDE,
                                WIDTH,
                                HEIGHT,
                                Bitmap.Config.ARGB_8888);


                    }*/
            setImage();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "select image", Toast.LENGTH_SHORT).show();
                            if(progressBar!=null)
                                relativeLayout.removeView(progressBar);
                            for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++)
                                photoButtons[i].setEnabled(true);
                            if(kernerBar!=null)
                                kernerBar.setEnabled(true);
                                   /* if(effectMenuItem!=null)
                                        effectMenuItem.setEnabled(true);*/
                            //saveBt.setText("convert");
                        }
                    });
                }
            }).start();

//                        Imgproc.cvtColor(img,img, Imgproc.COLOR_BGR2GRAY);
//                        Imgproc.GaussianBlur(img,img,new Size(5,5),0);
//                        Imgproc.threshold(img,thresh,45,255,Imgproc.THRESH_BINARY);
//                        Imgproc.erode(thresh,thresh,new Mat());
//                        Imgproc.dilate(thresh,thresh,new Mat());
//
//                        List<MatOfPoint> contours=new ArrayList<>();
//
//                        Imgproc.findContours(thresh,contours,new Mat(),Imgproc.RETR_EXTERNAL,
//                                Imgproc.CHAIN_APPROX_SIMPLE);

//                        double maxArea=0;
//                        MatOfPoint max_contour=new MatOfPoint();
//
//                        Iterator<MatOfPoint> iterator= contours.iterator();
//
//                        while (iterator.hasNext()){
//                            MatOfPoint contour=iterator.next();
//                            double area=Imgproc.contourArea(contour);
//                            if(area>maxArea){
//                                maxArea=area;
//                                max_contour=contour;
//                            }
//                        }
            //Mat contour_mat=new Mat();
            //contour_mat.create(thresh.rows(),thresh.cols(), CvType.CV_8UC3);

//                        Scalar red =new Scalar(255,0,0);
//                        for(int i=0;i<contours.size();i++){
//                            Imgproc.drawContours(thresh,contours,i,red);
//                        }

        }
    }
    private void setImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressBar!=null)
                            relativeLayout.removeView(progressBar);
                        if(kernerBar!=null)
                            kernerBar.setEnabled(true);
                        for(int i=0;i<PHOTO_THRESH_COUNT;i++)
                            photoButtons[i].setEnabled(true);
                        if(forDraw){
                            Frame.currentFrame.addSketch();
                            ((Activity)context).startActivity(
                                    new Intent(context,FillSketch.class)

                            );
                           /* new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                           ((FindContours)context).onFragmentChanged(FindContours.RESIZE);
                                        }
                                    });
                                }
                            }).start();*/


                        }else {
                            imageView.setImageBitmap(FINAL_IMAGE);
                        }
                    }
                });
            }
        }).start();
        if(forDraw) {
            boolean retry=true;
            while (retry){
                try {
                    Thread.currentThread().join();
                    retry=false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    private void setBitmapForDraw() {
      /*  if(ThreshHandler.FINAL_IMAGE!=null&&!ThreshHandler.FINAL_IMAGE.isRecycled()){
            ThreshHandler.FINAL_IMAGE.recycle();
            ThreshHandler.FINAL_IMAGE=null;
        }
        if(GetImage.imageViewBitmap!=null&&!GetImage.imageViewBitmap.isRecycled()){
            GetImage.imageViewBitmap.recycle();
            GetImage.imageViewBitmap=null;
        }*/
    }


    public void setForDraw(boolean forDraw) {
        this.forDraw=forDraw;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar=progressBar;
    }

    public static int getThreshValue() {
        return (BAR_UNIT*THRESH_VALUE+BAR_START);
    }

    public static int getKernelValue() {
        return BAR_UNIT*KERNEL_SIZE+BAR_START;
    }

    public static void setThreshValue(int i) {
        THRESH_VALUE=i;
    }

    public static void setKernelValue(int i) {
        KERNEL_SIZE=i;
    }
}
