package com.makeit.sunnycar.drawit.thread;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.makeit.sunnycar.drawit.handler.ThreshHandler;


/**
 * Created by gmltj on 2018-04-23.
 */

public class Threshold extends Thread {
    private final ImageButton[] photoButtons;
    private final SeekBar kernerBar;


    //public static double DILATE_SIZE=3;

    // public static int QUALITY=100;
    //public static int SAMPLE_SIZE=1;

    // final int SKETCH_OUTLINE=0;
    //final int PHOTO_OUTLINE=1;
    //public static final String Sketch_Outline="Sketch Outline";
    //public static final String Photo_Outline="Photo Outline";
    //public static final String Photo_To_Sketch="Photo To Sketch";


    private RelativeLayout relativeLayout;
    private ImageView imageView;
    //private final ImageButton[] photoButtons;

    //private MenuItem effectMenuItem;
    private Context context;
    //private Mat thresh_result;

    //public static Bitmap ORG_IMAGE;
    private int thickness;
    private boolean isBlackCountStarted;
    private ThreshHandler threshHandler;
    //private boolean fromMain;


    //public static HashSet<Integer> FINAL_IMAGE_POINTS=new HashSet<>();
    //public static byte[] FINAL_IMAGE_ARRAY;

    //public static Mat waterShedMask;
    //public static Mat binMask;

    public Threshold(RelativeLayout relativeLayout, ImageView borderView, ImageButton[] photoButtons, SeekBar kernelBar, Context context) {
        this.relativeLayout = relativeLayout;

        this.imageView = borderView;
        this.context = context;
        this.photoButtons=photoButtons;
        this.kernerBar=kernelBar;
        //this.fromMain=fromMain;
    }

    @Override
    public void run() {
        Looper.prepare();
        //Handler handler=new Handler();
        threshHandler=new ThreshHandler(relativeLayout,photoButtons,kernerBar,imageView,context);
       /* handler.post(new Runnable() {
            @Override
            public void run() {

                //Thread.currentThread().interrupt();
            }


        });*/
        Looper.loop();
    }

   /* private void grabCut(Mat orgMap){
        int cols=orgMap.cols();
        int rows=orgMap.rows();
        int chn=orgMap.channels();
        if(chn!=3){
            switch (chn){
                case 1:
                    Imgproc.cvtColor(orgMap,orgMap,Imgproc.COLOR_GRAY2BGR);
                    orgMap.convertTo(orgMap,CvType.CV_8UC3);
                    break;
                case 4:
                    Imgproc.cvtColor(orgMap,orgMap,Imgproc.COLOR_BGRA2BGR);
                    orgMap.convertTo(orgMap,CvType.CV_8UC3);
                    break;
            }
        }
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();

        if(binMask==null) {
            binMask = Mat.zeros(Threshold.org_bitmap.getHeight(), Threshold.org_bitmap.getWidth(), CvType.CV_8UC1);

            Rect background = new Rect(new Point(DrawingContourView.BORDER_LEFT, DrawingContourView.BORDER_TOP),
                    new Point(DrawingContourView.BORDER_RIGHT, DrawingContourView.BORDER_BOTTOM));
            Imgproc.grabCut(orgMap, binMask, background, bgdModel, fgdModel, 5, Imgproc.GC_INIT_WITH_RECT);

        }else if(DrawingContourView.add_points.size()>0
                &&DrawingContourView.minus_points.size()>0) {
            for (int i = 0; ; ) {
                if(i<DrawingContourView.add_points.size()-1) {
                    RecPoint point = DrawingContourView.add_points.get(i);
                    RecPoint point_next = DrawingContourView.add_points.get(i + 1);
                    //Imgproc.circle(binMask, new Point(point.x, point.y), point.size, new Scalar(Imgproc.GC_FGD), -1);
                    Imgproc.line(binMask, new Point(point.x, point.y), new Point(point_next.x, point_next.y), new Scalar(Imgproc.GC_FGD), point.size);
                    i++;
                }else {
                    RecPoint point = DrawingContourView.add_points.get(i);
                    RecPoint point_before = DrawingContourView.add_points.get(i - 1);
                    Imgproc.line(binMask, new Point(point_before.x, point_before.y), new Point(point.x, point.y), new Scalar(Imgproc.GC_FGD), point.size);
                    break;
                }
            }
            //     break;
            //case DrawingContourView.MINUS_MENU:

            for (int i = 0; ; ) {
                if(i<DrawingContourView.minus_points.size()-1) {
                    RecPoint point = DrawingContourView.minus_points.get(i);
                    RecPoint point_next = DrawingContourView.minus_points.get(i + 1);
                    //Imgproc.circle(binMask, new Point(point.x, point.y), point.size, new Scalar(Imgproc.GC_FGD), -1);
                    Imgproc.line(binMask, new Point(point.x, point.y), new Point(point_next.x, point_next.y), new Scalar(Imgproc.GC_FGD), point.size);
                    i++;
                }else {
                    RecPoint point = DrawingContourView.minus_points.get(i);
                    RecPoint point_before = DrawingContourView.minus_points.get(i - 1);
                    Imgproc.line(binMask, new Point(point_before.x, point_before.y), new Point(point.x, point.y), new Scalar(Imgproc.GC_FGD), point.size);
                    break;
                }
            }
            Imgproc.grabCut(orgMap, binMask, new Rect(), bgdModel, fgdModel, 5, Imgproc.GC_INIT_WITH_MASK);

        }
        //Mat tempBinMask=new Mat(rows,cols,CvType.CV_8UC1);
        Core.compare(binMask,new Scalar(Imgproc.GC_PR_FGD),binMask,Core.CMP_EQ);//1,3
        //Core.compare(binMask,new Scalar(Imgproc.GC),binMask,Core.CMP_EQ);//1,3
        *//*double[] bin_color;
        double[] temp_bin_color;
        //int[] colors=new int[rows*cols];
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                temp_bin_color=binMask.get(i,j);
                bin_color=binMask.get(i,j);
                if(temp_bin_color[0]==Imgproc.GC_PR_FGD||temp_bin_color[0]==Imgproc.GC_FGD){
                    temp_bin_color[0]=255;
                    bin_color[0]=1;
                }else {
                    temp_bin_color[0]=0;
                    bin_color[0]=0;

                }
                binMask.put(i,j,bin_color);
                tempBinMask.put(i,j,temp_bin_color);
                //colors[cols*i+j]= (int) color[0];
            }
        }
*//*
        Mat foreground=new Mat(rows,cols,CvType.CV_8UC4);
        foreground.setTo(new Scalar(0,0,0,0));
        orgMap.copyTo(foreground,binMask);
        if(FINAL_IMAGE==null)
            FINAL_IMAGE=Bitmap.createBitmap(org_bitmap.getWidth(), org_bitmap.getHeight(), Bitmap.Config.ARGB_4444);

        Utils.matToBitmap(foreground,FINAL_IMAGE);
        DrawingContourView.cleanAllPath();
    }
    private void waterShed(Mat img_gray,Mat orgMap) {

        int chn=orgMap.channels();
        if(chn!=3){
            switch (chn){
                case 1:
                    Imgproc.cvtColor(orgMap,orgMap,Imgproc.COLOR_GRAY2BGR);
                    orgMap.convertTo(orgMap,CvType.CV_8UC3);
                    break;
                case 4:
                    Imgproc.cvtColor(orgMap,orgMap,Imgproc.COLOR_BGRA2BGR);
                    orgMap.convertTo(orgMap,CvType.CV_8UC3);
                    break;
            }
        }
//        chn=markers.channels();
//        if(chn!=1){
//            switch (chn){
//                case 3:
//                    Imgproc.cvtColor(markers,markers,Imgproc.COLOR_BGR2GRAY);
//                    markers.convertTo(markers,CvType.CV_32SC1);
//                    break;
//                case 4:
//                    Imgproc.cvtColor(markers,markers,Imgproc.COLOR_BGRA2GRAY);
//                    markers.convertTo(markers,CvType.CV_32SC1);
//                    break;
//            }
        //}


        //calcPoint();
        int cols=img_gray.cols();
        int rows=img_gray.rows();
        if(waterShedMask==null) {
            //double[] centercolor=img_gray.get(rows/2,cols/2);

            waterShedMask = Mat.zeros(Threshold.org_bitmap.getHeight(), Threshold.org_bitmap.getWidth(), CvType.CV_8UC1);

            // Imgproc.threshold(img_gray,img_gray,centercolor[0],255,Imgproc.THRESH_BINARY_INV);
            //Core.add(waterShedMask,img_gray,waterShedMask);
        }
        Mat mask=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_16UC1);

       // Mat maskAdd=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_16UC1);
       // if(DrawingContourView.addMask) {
            for (int i = 0; i < DrawingContourView.add_points.size(); i++) {
                RecPoint point = DrawingContourView.add_points.get(i);
                Imgproc.rectangle(mask, new Point(point.x - 10, point.y - 10),
                        new Point(point.x + 10, point.y + 10), new Scalar(1), point.size );

                //Imgproc.circle(mask, new Point(point.x, point.y), 20, new Scalar(1), -1);
            }
        Imgproc.rectangle(mask,new Point(5,5),new Point(cols-5,rows-5),new Scalar(255),3);

        mask.convertTo(mask,CvType.CV_32SC1);
        Imgproc.watershed(orgMap,mask);
        Mat getAddSegmentation=new Mat();
        mask.convertTo(getAddSegmentation,CvType.CV_8U);
        Imgproc.threshold(getAddSegmentation,getAddSegmentation,127,255,Imgproc.THRESH_BINARY_INV);
        Core.add(waterShedMask,getAddSegmentation,waterShedMask);

        //minus
        Mat maskMinus=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_16UC1);
        for (int i = 0; i < DrawingContourView.minus_points.size(); i++) {
                RecPoint point = DrawingContourView.minus_points.get(i);
                Imgproc.rectangle(maskMinus, new Point(point.x - 10, point.y - 10),
                        new Point(point.x + 10, point.y + 10), new Scalar(1), point.size);

                //Imgproc.circle(mask, new Point(point.x, point.y), 20, new Scalar(1), -1);
        }
        Imgproc.rectangle(maskMinus,new Point(5,5),new Point(cols-5,rows-5),new Scalar(255),3);
        maskMinus.convertTo(maskMinus,CvType.CV_32SC1);
        Imgproc.watershed(orgMap,maskMinus);
        Mat getMinusSegmentation=new Mat();
        maskMinus.convertTo(getMinusSegmentation,CvType.CV_8U);
        Imgproc.threshold(getMinusSegmentation,getMinusSegmentation,127,255,Imgproc.THRESH_BINARY_INV);
        Core.subtract(waterShedMask,getMinusSegmentation,waterShedMask);

        if(FINAL_IMAGE==null)
            FINAL_IMAGE=Bitmap.createBitmap(org_bitmap.getWidth(), org_bitmap.getHeight(), Bitmap.Config.ARGB_4444);

        //Imgproc.rectangle(waterShedMask,new Point(5,5),new Point(cols-5,rows-5),new Scalar(255),3);

        Mat crop=new Mat(rows,cols,CvType.CV_8UC4);
        crop.setTo(new Scalar(0,0,0,0));
        orgMap.copyTo(crop,waterShedMask);

        Utils.matToBitmap(crop,FINAL_IMAGE);
        DrawingContourView.cleanAllPath();

    }
*/
    /*private void calcPoint() {
        for(int i=0;i<DrawingContourView.add_points.size();i++){
            android.graphics.Point add_point=DrawingContourView.add_points.get(i);
            for(int j=0;j<DrawingContourView.minus_points.size();j++){
                android.graphics.Point minus_point=DrawingContourView.minus_points.get(j);
                if(Math.abs(Math.pow(add_point.x-minus_point.x,2)+Math.pow(add_point.y-minus_point.y,2))>POINT_MAX_LENTH){
                    DrawingContourView.add_points.remove(i);
                    break;
                }

            }
        }
    }
*/
   /* private void drawContour(Mat img_gray,Mat orgMap) {
        Mat contourMask=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_8UC1);
        //Mat mask=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_8UC1);
        if(DrawingContourView.add_points!=null) {
            for (int i = 0; i < DrawingContourView.add_points.size(); i++) {
                android.graphics.Point point = DrawingContourView.add_points.get(i);
                Imgproc.circle(contourMask, new Point(point.x, point.y), 20, new Scalar(255), -1);
            }
            Mat crop=new Mat(img_gray.rows(),img_gray.cols(), CvType.CV_8UC4);
            crop.setTo(new Scalar(0,0,0,0));
            orgMap.copyTo(crop,contourMask);

            //Imgproc.GaussianBlur(thresh_result,thresh_result,new Size(5,5),0);
            //Imgproc.erode(thresh_result,thresh_result,new Mat());
            //Imgproc.dilate(thresh_result,thresh_result,new Mat());
            List<MatOfPoint> contours=new ArrayList<>();
            Mat hierarcy=new Mat();
            Imgproc.cvtColor(crop,crop,Imgproc.COLOR_BGR2GRAY);
            Imgproc.Canny(crop,crop,100,200);
            Imgproc.findContours(crop,contours,hierarcy,Imgproc.RETR_TREE,
                    Imgproc.CHAIN_APPROX_SIMPLE);
            Mat mask=Mat.zeros(img_gray.rows(),img_gray.cols(), CvType.CV_8UC1);

            Scalar white =new Scalar(255);
            //int largest_area=0, largest_contour_index=0;
           *//* List<MatOfPoint> finalContours=new ArrayList<>();
            List<Point> finalPoint=new ArrayList<>();*//*
           *//* for(int i=0;i<contours.size();i++){
//                            double a =Imgproc.contourArea(contours.get(i),false);
//                            if(a>largest_area){
//                                largest_area= (int) a;
//                                largest_contour_index=i;
//
//                            }
                MatOfPoint matOfPoint=contours.get(i);
                List<Point> points=matOfPoint.toList();
                for(int k=0;k<DrawingContourView.points.size();k++){
                    android.graphics.Point point=DrawingContourView.points.get(k);

                    for(int j=0;j<points.size();j++) {
                        int x = (int) points.get(j).x;
                        int y = (int) points.get(j).y;
                        if(Math.abs(Math.pow(point.x-x,2)+Math.pow(point.y-y,2))<2500){
                            finalPoint.add(new Point(x,y));
                            break;
                        }
                    }
                }

            }
           *//*
            //Random random=new Random();
          *//*  int largest_area=0, largest_contour_index=0;

            for(int i=0;i<contours.size();i++){
               double a=Imgproc.contourArea(contours.get(i),false);
               //Scalar color = new Scalar(random.nextInt(255));
                if(a>largest_area){
                                largest_area= (int) a;
                                largest_contour_index=i;
                }

           }
            Imgproc.drawContours(mask,contours, largest_contour_index,white,2);
*//*
            Random random=new Random();

            for(int i=0;i<hierarcy.row(0).row(0).cols();i++){
               double[] contourInfo=hierarcy.row(0).row(0).get(0,i);
               if(contourInfo!=null&&contourInfo[2]==-1){
                   double a=Imgproc.contourArea(contours.get(i),false);
                   // if(a>100){
                        Scalar color = new Scalar(random.nextInt(255));

                        Imgproc.drawContours(mask,contours, i,color,2);

                    //}

               }

           }
//           MatOfPoint m=new MatOfPoint();
//            m.fromList(finalPoint);
//            finalContours.add(m);
//            Imgproc.drawContours(mask,finalContours,0,white,-1);

            Mat fianlCrop=new Mat(img_gray.rows(),img_gray.cols(), CvType.CV_8UC4);
            fianlCrop.setTo(new Scalar(0,0,0,0));
            orgMap.copyTo(fianlCrop,mask);
//        //if(FINAL_IMAGE==null){
            FINAL_IMAGE=Bitmap.createBitmap(org_bitmap.getWidth(),
                    org_bitmap.getHeight(),
                    Bitmap.Config.ARGB_4444);
//        //}

            Utils.matToBitmap(mask,FINAL_IMAGE);
        }


    }
*/


    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void sendMessage() {
        Message message=new Message();
        //message.what=what;//ThreshHandler.THRESH_PHOTO_VIEW;
        threshHandler.sendMessage(message);
    }

    public void setForDraw(boolean forDraw) {
        threshHandler.setForDraw(forDraw);
    }

    public void setProgressBar(ProgressBar progressBar) {
        threshHandler.setProgressBar(progressBar);
    }


    /*public void createPhotoView() {
        Message message=new Message();
        message.what=ThreshHandler.CREATE_PHOTO_VIEW;
        threshHandler.sendMessage(message);
    }*/

/*    public static void restart(Bitmap bitmap) {
        if(bitmap!=null)
            org_bitmap=bitmap;
        FINAL_IMAGE=null;
        //waterShedMask=null;
        //binMask=null;
    }*/
}
