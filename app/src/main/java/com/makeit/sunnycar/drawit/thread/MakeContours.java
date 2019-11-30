package com.makeit.sunnycar.drawit.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by gmltj on 2018-04-25.
 */

public class MakeContours extends Handler {
    public static final int MSG_MAKE_CONTOURS=1;
    public static final String TOUCHED_X="x";
    public static final String TOUCHED_Y="y";
    //public static Mat contourMask;
    //public static Bitmap tempOrgMapWithContourBitmap;
    //public static Mat tempOrgMapWithContour;

    public void handleMessage(Message message){
        switch (message.what){
            case MSG_MAKE_CONTOURS:
                Bundle bundle=message.getData();
                addContours(bundle.getInt(TOUCHED_X),bundle.getInt(TOUCHED_Y));

        }
    }

    private void addContours(int x, int y) {


      /*  if(contourMask==null){
            contourMask=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_8UC1);
        }
        Mat mask=Mat.zeros(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_8UC1);
        for(int i=0;i< DrawingContourView.add_points.size();i++) {
            android.graphics.Point point = DrawingContourView.add_points.get(i);
            Imgproc.circle(mask, new Point(point.x, point.y), 20, new Scalar(255), -1);
        }
        Core.add(contourMask,mask,contourMask);*/

        /*Mat redContour=new Mat(Threshold.org_bitmap.getHeight(),Threshold.org_bitmap.getWidth(), CvType.CV_8UC4);
        redContour.setTo(new Scalar(0,0,0,0));

        if(tempOrgMapWithContourBitmap==null)
            tempOrgMapWithContourBitmap=Bitmap.createBitmap(Threshold.org_bitmap.getWidth(),Threshold.org_bitmap.getHeight(),
                    Bitmap.Config.ARGB_4444);

        if(tempOrgMapWithContour==null) {
            tempOrgMapWithContour=new Mat();
            Utils.bitmapToMat(Threshold.org_bitmap, tempOrgMapWithContour);
        }
        Core.add(redContour,new Scalar(255,0,0,100),redContour,mask);
        Core.add(tempOrgMapWithContour,redContour,tempOrgMapWithContour);

        Utils.matToBitmap(tempOrgMapWithContour,tempOrgMapWithContourBitmap);*/
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Looper.prepare();
//                Handler mainHandler=new Handler(Looper.getMainLooper());
//                mainHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //imageView.setImageBitmap(tempOrgMapWithContourBitmap);
////                        Log.d("imageview", "getMeasuredWidth:"+String.valueOf(imageView.getMeasuredWidth())+" | "
////                                +"getMeasuredHeight:"+String.valueOf(imageView.getMeasuredHeight()));
////
////                        Log.d("imageview", "getWidth:"+String.valueOf(imageView.getWidth())+" | "
////                                +"getHeight:"+String.valueOf(imageView.getHeight()));
////
////                        Log.d("org_bitmap", "getWidth:"+String.valueOf(Threshold.org_bitmap.getWidth())+" | "
////                                +"getHeight:"+String.valueOf(Threshold.org_bitmap.getHeight()));
//
//                        // 다 동일
//                    }
//                });
//                Thread.currentThread().interrupt();
//            }
//        }).start();

    }
//    @Override
//    public void run() {
//        super.run();
//        Looper.prepare();
//        Handler handler=new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//
//               while (!Thread.currentThread().isInterrupted()){
//
//               }
//                Thread.currentThread().interrupt();
//            }
//        });
//        Looper.loop();
//    }

}
