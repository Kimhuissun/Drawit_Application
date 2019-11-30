package com.makeit.sunnycar.drawit.thread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.WindowManager;

import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.ui.FindContours;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gmltj on 2018-04-23.
 */

public class GetImage extends Thread {

    //private final boolean fromMain;
    //private final Threshold thresh;
    //private final ImageButton[] photoButtons;
   /* private BorderView imageView;
    private TextView pictureInfo;
    private RelativeLayout relativeLayout;
    private TextView textView;
  */
    private Context context;
    public static Bitmap imageViewBitmap;
    public static byte[] copyBitmapByte;

    public GetImage(Context context) {
        this.context = context;
        //this.fromMain=fromMain;

    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        Handler getImageHandler=new Handler();
        getImageHandler.post(new Runnable() {
            @Override
            public void run() {

                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
                //copyBitmapByte=GetImage.clone(MainActivity.bitmapInputStream);

                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                imageViewBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                copyBitmapByte=baos.toByteArray();

                BitmapFactory.decodeByteArray(copyBitmapByte,0,copyBitmapByte.length,options);

                options.inSampleSize=caculateInSampleSize(options);
                options.inJustDecodeBounds=false;

                imageViewBitmap= BitmapFactory.decodeByteArray(copyBitmapByte,0,copyBitmapByte.length,options);
                ThreshHandler.FINAL_IMAGE=null;

                //BitmapFactory.Options new_options=new BitmapFactory.Options();
                //int[] sizeAndQuality=caculateInSampleSizeAndQuality(options);
                //new_options.inSampleSize=sizeAndQuality[0];
                //new_options.inJustDecodeBounds=false;

                //int quality=sizeAndQuality[1];//70;//caculateQuality(org_bitmap);//in kb

                //ByteArrayOutputStream bos=new ByteArrayOutputStream();
                //Threshold.org_bitmap.compress(Bitmap.CompressFormat.PNG,Threshold.QUALITY,bos);
                //if(org_bitmap.getByteCount()>3000000)//:width*height*2//픽셀당 2바이트
               // byte[] bitmapData=bos.toByteArray();
                //Bitmap bitmap=BitmapFactory.decodeByteArray(bitmapData,0,bitmapData.length,new_options);
                //Threshold.restart(bitmap);
                //DrawingContourView.restart();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Handler mainHandler=new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {

                               // pictureInfo.setText(String.valueOf(imageViewBitmap.getWidth()+" * "
                               //                 +String.valueOf(imageViewBitmap.getHeight())));
                                if(imageViewBitmap!=null) {
                                    //BorderView.BORDER_DST_RECT.set(0, 0, imageViewBitmap.getWidth(), imageViewBitmap.getHeight());

                                    Intent intent=new Intent(context, FindContours.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);

                                    //context.startActivity(new Intent(context,CropActivity.class));

                                }
                            }
                        });
                    }
                }).start();
                Thread.currentThread().interrupt();

                //BitmapFactory.Options options=new BitmapFactory.Options();
                //options.inJustDecodeBounds=true;

            }


        });
        Looper.loop();
    }
/*
Threshold.ORG_IMAGE=BitmapFactory.decodeStream(FindContours.bitmapInputStream);

                BitmapFactory.Options options=new BitmapFactory.Options();

                options.inSampleSize=caculateInSampleSize(Threshold.ORG_IMAGE);

                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                Threshold.ORG_IMAGE.compress(Bitmap.CompressFormat.JPEG,100,bos);

                byte[] bitmapData=bos.toByteArray();
                imageViewBitmap=BitmapFactory.decodeByteArray(bitmapData,0,bitmapData.length,options);

*/
    public static byte[] clone(InputStream bitmapInputStream) {

        bitmapInputStream.mark(0);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int readLength=0;
        try {
            while ((readLength=bitmapInputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,readLength);
            }
            //bitmapInputStream.reset();//not supported
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();

    }

    private int caculateInSampleSize(BitmapFactory.Options options) {
        final int height=options.outHeight;
        final int width=options.outWidth;
        //sizeAndQuality[0]=1;
        //sizeAndQuality[1]=100;
        //int reqHeight=300, reqWidth=300;
        int inSampleSize=1;

        WindowManager windowManager= (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display= null;
        if (windowManager != null) {
            display = windowManager.getDefaultDisplay();
            Point window=new Point();
            display.getSize(window);
            //int reqWidth=imageView.getWidth();
            //int reqHeight=imageView.getHeight();
            int reqWidth= (int) (window.x);
            int reqHeight=(int) (window.y);
            //reqHeight=window.y;
            if(height>reqHeight||width>reqWidth){
                final int halfHeight=height/2;
                final int halfWidth=width/2;
                //inSampleSize=2;
                while ((halfHeight / inSampleSize) >= reqWidth
                        && (halfWidth / inSampleSize) >= reqWidth) {

                    inSampleSize *= 2;
                }

            }

        }
        return inSampleSize;

    }

    /*public int caculateInSampleSize(Bitmap bitmap) {

        //int[] sizeAndQuality=new int[2];
        final int height=bitmap.getHeight();//.outHeight;
        final int width=bitmap.getWidth();
        //sizeAndQuality[0]=1;
        //sizeAndQuality[1]=100;
        //int reqHeight=300, reqWidth=300;
        int reqWidth=imageView.getWidth();
        int reqHeight=imageView.getHeight();
      *//*  WindowManager windowManager= (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display= null;
        if (windowManager != null) {
            display = windowManager.getDefaultDisplay();
            Point window=new Point();
            display.getSize(window);
            reqWidth= (int) (window.x);
            //reqHeight=window.y;

        }*//*
      int inSampleSize=1;
        if(height>reqHeight||width>reqWidth){
            final int halfHeight=height/2;
            final int halfWidth=width/2;
            //inSampleSize=2;
                while ((halfHeight / inSampleSize) >= reqWidth
                        && (halfWidth / inSampleSize) >= reqWidth) {

                    inSampleSize *= 2;
                }

        }

      *//*  int bitmap_size=bitmap.getByteCount()/1000;

        if(reqWidth<width){
            int factorW=width/reqWidth;
            if(factorW>0) {
                sizeAndQuality[0] *= factorW;
                bitmap_size /= Math.pow(factorW, 2);
            }
        }*//*

     *//*
        while ((width/sizeAndQuality[0])>reqWidth){
            sizeAndQuality[0]*=2;
            bitmap_size/=4;

        }*//*


     *//*   while (bitmap_size>4000) {
            sizeAndQuality[0]*=2;
            bitmap_size/=4;

        }*//*

       *//* while (bitmap_size>2500){
            sizeAndQuality[1]-=10;
            bitmap_size/=2;
        }*//*

        return inSampleSize;

    }*/
}
