package com.makeit.sunnycar.drawit.thread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.ui.PremiumActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by gmltj on 2018-04-23.
 */

public class SaveImage extends Thread {
    private final LayerAdapter layerAdapter;
    private Context context;
    private boolean hasLayer;
    //private SharedPreferences sharedPreferences;
    //private DrawingContourView drawingContourView;

    public SaveImage(Context context,boolean hasLayer,LayerAdapter layerAdapter) {

        this.context = context;
        this.hasLayer=hasLayer;
        this.layerAdapter=layerAdapter;
        //this.sharedPreferences=context.getSharedPreferences(MainActivity.DIGITALISED_LINES,0);
    }
   /* public boolean getSaveCount() {
        //SharedPreferences sf= context.getSharedPreferences(MainActivity.DIGITALISED_LINES,0);
        save_count=sharedPreferences.getInt(MainActivity.GET_SAVE_COUNT,0);
        if(save_count<MainActivity.SAVE_COUNT_MAX){
            return true;
        }else return false;

    }

    public void setSaveCount(int count){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(MainActivity.GET_SAVE_COUNT,count);
        editor.apply();
    }*/
    @Override
    public void run() {
        super.run();
        String state= Environment.getExternalStorageState();
        String toast_text="";

        if(Environment.MEDIA_MOUNTED.equals(state)){
            if(layerAdapter!=null) {//in fillsketch

                for(int i=0;i<layerAdapter.frames.size();i++){
                    Frame frame=layerAdapter.frames.get(i);
                    File newFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"outline");
                    if(!newFile.exists()){
                        if(!newFile.mkdirs()){
                            //   Log.e("newPath", " failed");
                        }
                    }
                    Calendar today=Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss"
                            ,java.util.Locale.getDefault());

                    String img_name= simpleDateFormat.format(today.getTime())+"_"+String.valueOf(i)+".png";//String.valueOf(System.currentTimeMillis());
                    File imageFile=new File(newFile,img_name);
                    if(!imageFile.exists()){
                        try {
                            if(!imageFile.createNewFile()){
                                // Log.e("newFile", " failed");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        FileOutputStream fo=new FileOutputStream(imageFile);
                        frame.redrawForSave();
                       /* if(frame.layersBitmap==null){
                            frame.layersBitmap =
                                    Bitmap.createBitmap(DrawingContourView.WIDTH, DrawingContourView.HEIGHT, Bitmap.Config.ARGB_4444);
                            frame.layersCanvas=new Canvas(frame.layersBitmap);
                        }
                        if(frame.layersCanvas==null){
                            frame.layersCanvas=new Canvas(frame.layersBitmap);
                        }
                        frame.redrawForSave();*/

                       /* frame.layersCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                            for(int j = frame.layers.size()-1; j>=0; j--){
                                Layer layer=frame.layers.get(j);
                                if(layer.eyeOn){
                                    DrawingContourView.createPathBitmap(layer,frame.layersCanvas);
                                    //frame.layersCanvas.drawBitmap(layer.pathBitmap,0,0,DrawingContourView.mBitmapPaint);
                                }

                            }
*/
                            bitmapToFile(fo,frame.layersBitmap);
                            toast_text="save";

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            } else toast_text="no image";
            /*else {
                if(ThreshHandler.FINAL_IMAGE!=null) {
                    File newFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"outline");
                    if(!newFile.exists()){
                        if(!newFile.mkdirs()){
                            //   Log.e("newPath", " failed");
                        }
                    }
                    Calendar today=Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmm"
                            ,java.util.Locale.getDefault());

                    String img_name= simpleDateFormat.format(today.getTime())+".png";//String.valueOf(System.currentTimeMillis());
                    File imageFile=new File(newFile,img_name);
                    if(!imageFile.exists()){
                        try {
                            if(!imageFile.createNewFile()){
                                // Log.e("newFile", " failed");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        FileOutputStream fo = new FileOutputStream(imageFile);
                        bitmapToFile(fo,ThreshHandler.FINAL_IMAGE);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    toast_text="save";
                    //setSaveCount(++save_count);

                }
                else toast_text="no image";


        }
*/
            //}

            Handler mainHandler=new Handler(Looper.getMainLooper());
            final String finalToast_text = toast_text;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, finalToast_text,Toast.LENGTH_SHORT).show();

                }
            });

        }else {
           // goPremiumActivity();
        }

    }

    private void bitmapToFile(FileOutputStream fo, Bitmap saveBitmap) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] FINAL_IMAGE_ARRAY = os.toByteArray();

        try {
            fo.write(FINAL_IMAGE_ARRAY);
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //FINAL_IMAGE.compress(Bitmap.CompressFormat.PNG,100,fo);


    }

    private void goPremiumActivity() {
        Handler mainHandler=new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
               context.startActivity(new Intent(context, PremiumActivity.class));
            }
        });
    }
}
