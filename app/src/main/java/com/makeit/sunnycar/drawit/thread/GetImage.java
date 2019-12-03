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


    private Context context;
    public static Bitmap imageViewBitmap;
    public static byte[] copyBitmapByte;

    public GetImage(Context context) {
        this.context = context;

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

                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                imageViewBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                copyBitmapByte=baos.toByteArray();

                BitmapFactory.decodeByteArray(copyBitmapByte,0,copyBitmapByte.length,options);

                options.inSampleSize=caculateInSampleSize(options);
                options.inJustDecodeBounds=false;

                imageViewBitmap= BitmapFactory.decodeByteArray(copyBitmapByte,0,copyBitmapByte.length,options);
                ThreshHandler.FINAL_IMAGE=null;


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Handler mainHandler=new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if(imageViewBitmap!=null) {
                                    Intent intent=new Intent(context, FindContours.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);

                                }
                            }
                        });
                    }
                }).start();
                Thread.currentThread().interrupt();

            }


        });
        Looper.loop();
    }

    public static byte[] clone(InputStream bitmapInputStream) {

        bitmapInputStream.mark(0);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int readLength=0;
        try {
            while ((readLength=bitmapInputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,readLength);
            }

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();

    }

    private int caculateInSampleSize(BitmapFactory.Options options) {
        final int height=options.outHeight;
        final int width=options.outWidth;

        int inSampleSize=1;

        WindowManager windowManager= (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display= null;
        if (windowManager != null) {
            display = windowManager.getDefaultDisplay();
            Point window=new Point();
            display.getSize(window);

            int reqWidth= (int) (window.x);
            int reqHeight=(int) (window.y);

            if(height>reqHeight||width>reqWidth){
                final int halfHeight=height/2;
                final int halfWidth=width/2;

                while ((halfHeight / inSampleSize) >= reqWidth
                        && (halfWidth / inSampleSize) >= reqWidth) {

                    inSampleSize *= 2;
                }

            }

        }
        return inSampleSize;

    }

}
