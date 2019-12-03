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

    public static final int THRESH_VALUE_MAX = 126;

    public static final int KERNEL_SIZE_MAX = 150;
    private static final int BAR_UNIT = 2;
    private static final int BAR_START = 3;

    public static int KERNEL_SIZE=5;
    public static int THRESH_VALUE=24;

    public static int METHOD=0;

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

    public static Bitmap FINAL_IMAGE;
    private ProgressBar progressBar;
    private final ImageButton[] photoButtons;

    public ThreshHandler(RelativeLayout relativeLayout, ImageButton[] photoButtons, SeekBar kernerBar, ImageView imageView, Context context) {
        this.relativeLayout = relativeLayout;
        this.imageView = imageView;
        this.context = context;
        this.photoButtons=photoButtons;
        this.kernerBar=kernerBar;

    }

    public void handleMessage(Message message) {
      if (GetImage.imageViewBitmap != null) {
            Mat orgMap=new Mat();
            Utils.bitmapToMat(GetImage.imageViewBitmap, orgMap);

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


                    }else {

                        FINAL_IMAGE=Bitmap.createBitmap(GetImage.imageViewBitmap.getWidth(),
                                GetImage.imageViewBitmap.getHeight(), Bitmap.Config.ARGB_4444);
                        Utils.matToBitmap(orgMap,FINAL_IMAGE);

                    }
                }

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

                        }
                    });
                }
            }).start();

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
