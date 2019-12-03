package com.makeit.sunnycar.drawit.ui;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.fragment.ContourFragment;
import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.thread.Threshold;

import com.makeit.sunnycar.drawit.view.TuneDialog;

import org.opencv.android.OpenCVLoader;

/**
 * Created by gmltj on 2018-04-03.
 */

public class FindContours extends AppCompatActivity {
    private static final int BOTTOM_MENU_NUM=2;
    private static final int REDO_MENU=0;
    private static final int NEXT_MENU=1;
    public static final String FROM_MAIN = "from_main";
    public static final int RESIZE = 1;

    private RelativeLayout relativeLayout;
    private ImageView imageView;

    private TextView pictureInfo;
    private ImageButton[] imageButtons=new ImageButton[BOTTOM_MENU_NUM];
    private SeekBar kernelBar;
    private TextView kernelSize;
    private ImageButton[] photoButtons=new ImageButton[ThreshHandler.PHOTO_THRESH_COUNT];

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d("cameraactivity","opencv not loaded");
        }else{
            Log.d("cameraactivity","opencv loaded");

        }
    }


    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id=view.getId();
            switch (id){

                case R.id.next:
                    threshold(true);
                    break;

            }

        }
    };
    private Threshold thresh;
    private ContourFragment contourFragment;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.border_fragment);

        setView();
        resetCurrentMethod();


    }
    public void onFragmentChanged(int index){
        if(index==RESIZE){

          }
    }

    private void resetCurrentMethod() {
        ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_BINARY;
    }

    private void setView() {

        imageView=(ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(GetImage.imageViewBitmap);


        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);

        pictureInfo=(TextView)findViewById(R.id.pictureInfo);
        imageButtons[REDO_MENU]=(ImageButton)findViewById(R.id.redo);
        imageButtons[NEXT_MENU]=(ImageButton)findViewById(R.id.next);
        for(int i=0;i<BOTTOM_MENU_NUM;i++)
            imageButtons[i].setOnClickListener(onClickListener);

        setTopMenu();

        thresh=new Threshold(relativeLayout,imageView,photoButtons,kernelBar,this);
        thresh.start();

    }
    private void setOtherBackgroundColorTransparent() {
        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++){
            if(ThreshHandler.PHOTO_THRESH_METHOD==i)
                photoButtons[i].setBackgroundColor(Color.WHITE);
            else   photoButtons[i].setBackgroundColor(Color.TRANSPARENT);

        }
    }
     private void setTopMenu() {
        kernelBar=(SeekBar)findViewById(R.id.kernel);
        kernelSize=(TextView)findViewById(R.id.kernelSize);
        changeSeekbar();
        kernelBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

         photoButtons[ThreshHandler.THRESH_BINARY]=findViewById(R.id.threshold_binary);
        photoButtons[ThreshHandler.THRESH_BINARY_INV]=findViewById(R.id.threshold_binary_inv);
        photoButtons[ThreshHandler.THRESH_TRUNC]=findViewById(R.id.threshold_binary_trunc);
        photoButtons[ThreshHandler.THRESH_TOZERO]=findViewById(R.id.threshold_binary_tozero);
        photoButtons[ThreshHandler.ADAPTIVE_THRESH_MEAN]=findViewById(R.id.adaptive_threshold_mean);
        photoButtons[ThreshHandler.ADAPTIVE_THRESH_GAUSSIAN]=findViewById(R.id.adaptive_threshold_gaussian);
         photoButtons[ThreshHandler.THRESH_OTSU]=findViewById(R.id.threshold_otsu);
         photoButtons[ThreshHandler.PHOTO_TO_COLOR_SKETCH]=findViewById(R.id.photo_to_soft);
         photoButtons[ThreshHandler.PHOTO_TO_BLACK_SKETCH]=findViewById(R.id.photo_to_sketch);
         photoButtons[ThreshHandler.BLACK_AND_WHITE]=findViewById(R.id.black_and_white);

        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++)
            photoButtons[i].setOnClickListener(onPhotoClickListener);

    }
    private View.OnClickListener onPhotoClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id=v.getId();
            switch (id){

                case R.id.threshold_binary:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_BINARY;
                    break;
                }
                case R.id.threshold_binary_inv:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_BINARY_INV;

                    break;
                }
                case R.id.threshold_binary_trunc:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_TRUNC;

                    break;
                }
                case R.id.threshold_binary_tozero:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_TOZERO;

                    break;
                }
                case R.id.adaptive_threshold_mean:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.ADAPTIVE_THRESH_MEAN;

                    break;
                }
                case R.id.adaptive_threshold_gaussian:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.ADAPTIVE_THRESH_GAUSSIAN;

                    break;
                }

                case R.id.threshold_otsu:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_OTSU;

                    break;
                }
                case R.id.photo_to_soft:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.PHOTO_TO_COLOR_SKETCH;
                    break;
                }
                case R.id.photo_to_sketch:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.PHOTO_TO_BLACK_SKETCH;

                    break;
                }
                case R.id.black_and_white:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.BLACK_AND_WHITE;

                    break;
                }
            }
            setOtherBackgroundColorTransparent();
            changeSeekbar();
            threshold(false);

        }
    };
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b) {
                int id = seekBar.getId();

                switch (id) {
                    case R.id.kernel: {
                        if (ThreshHandler.PHOTO_THRESH_METHOD >= ThreshHandler.THRESH_BINARY
                                && ThreshHandler.PHOTO_THRESH_METHOD <= ThreshHandler.THRESH_TOZERO) {
                            ThreshHandler.setThreshValue(i);
                            kernelSize.setText(String.valueOf(ThreshHandler.getThreshValue()));

                        } else if (ThreshHandler.PHOTO_THRESH_METHOD >= ThreshHandler.ADAPTIVE_THRESH_MEAN
                                && ThreshHandler.PHOTO_THRESH_METHOD <= ThreshHandler.PHOTO_TO_BLACK_SKETCH) {
                            ThreshHandler.setKernelValue(i);
                            kernelSize.setText(String.valueOf(ThreshHandler.getKernelValue()));

                        }

                        break;
                    }

                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            threshold(false);
        }
    };
    private void changeSeekbar() {
        if(ThreshHandler.PHOTO_THRESH_METHOD>=ThreshHandler.THRESH_BINARY
                &&ThreshHandler.PHOTO_THRESH_METHOD<=ThreshHandler.THRESH_TOZERO){
            kernelBar.setVisibility(View.VISIBLE);
            kernelSize.setVisibility(View.VISIBLE);

            kernelBar.setMax(ThreshHandler.THRESH_VALUE_MAX);
            kernelBar.setProgress(ThreshHandler.THRESH_VALUE);
            kernelSize.setText(String.valueOf(ThreshHandler.getThreshValue()));

        }else if(ThreshHandler.PHOTO_THRESH_METHOD>=ThreshHandler.ADAPTIVE_THRESH_MEAN
                &&ThreshHandler.PHOTO_THRESH_METHOD<=ThreshHandler.PHOTO_TO_BLACK_SKETCH){
            kernelBar.setVisibility(View.VISIBLE);
            kernelSize.setVisibility(View.VISIBLE);
            kernelBar.setMax(ThreshHandler.KERNEL_SIZE_MAX);

            kernelBar.setProgress(ThreshHandler.KERNEL_SIZE);
            kernelSize.setText(String.valueOf(ThreshHandler.getKernelValue()));
        }else {//black and white
            kernelBar.setVisibility(View.INVISIBLE);
            kernelSize.setVisibility(View.INVISIBLE);

        }
    }


    private void startTuneDialog() {
        TuneDialog tuneDialog=new TuneDialog();

        android.app.FragmentManager fm=getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.add(tuneDialog,"tuneDialog");
        Bundle args=new Bundle();
        args.putBoolean(TuneDialog.NEW_CANVAS,false);
        tuneDialog.setArguments(args);
        tuneDialog.setOnSaveTuneListener(new TuneDialog.OnSaveTuneListener() {
            @Override
            public void onSave() {
                startActivity(new Intent(getApplicationContext(),FillSketch.class));


            }
        });
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!OpenCVLoader.initDebug()){

        }else {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void threshold(boolean forDraw) {

        thresh.setForDraw(forDraw);

       final ProgressBar progressBar=new ProgressBar(this);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,layoutParams);
        thresh.setProgressBar(progressBar);
        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++)
            photoButtons[i].setEnabled(false);
        kernelBar.setEnabled(false);
        thresh.sendMessage();

    }
}
