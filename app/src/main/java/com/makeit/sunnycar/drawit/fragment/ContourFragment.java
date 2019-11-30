package com.makeit.sunnycar.drawit.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.thread.Threshold;

public class ContourFragment extends Fragment {
    private static final int BOTTOM_MENU_NUM=2;
    private static final int REDO_MENU=0;
    private static final int NEXT_MENU=1;

    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private TextView pictureInfo;
    private ImageButton[] imageButtons=new ImageButton[BOTTOM_MENU_NUM];
    private SeekBar kernelBar;
    private TextView kernelSize;
    private ImageButton[] photoButtons=new ImageButton[ThreshHandler.PHOTO_THRESH_COUNT];

    private void setOtherBackgroundColorTransparent() {
        for(int i = 0; i< ThreshHandler.PHOTO_THRESH_COUNT; i++){
            if(ThreshHandler.PHOTO_THRESH_METHOD==i)
                photoButtons[i].setBackgroundColor(Color.WHITE);
            else   photoButtons[i].setBackgroundColor(Color.TRANSPARENT);

        }
    }
    private void setTopMenu(View rootView) {
        kernelBar=(SeekBar)rootView.findViewById(R.id.kernel);
        kernelSize=(TextView)rootView.findViewById(R.id.kernelSize);
        changeSeekbar();
        kernelBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        //photoButtons[ThreshHandler.ORIGINAL]=findViewById(R.id.original);
        photoButtons[ThreshHandler.THRESH_BINARY]=rootView.findViewById(R.id.threshold_binary);
        photoButtons[ThreshHandler.THRESH_BINARY_INV]=rootView.findViewById(R.id.threshold_binary_inv);
        photoButtons[ThreshHandler.THRESH_TRUNC]=rootView.findViewById(R.id.threshold_binary_trunc);
        photoButtons[ThreshHandler.THRESH_TOZERO]=rootView.findViewById(R.id.threshold_binary_tozero);
        photoButtons[ThreshHandler.ADAPTIVE_THRESH_MEAN]=rootView.findViewById(R.id.adaptive_threshold_mean);
        photoButtons[ThreshHandler.ADAPTIVE_THRESH_GAUSSIAN]=rootView.findViewById(R.id.adaptive_threshold_gaussian);
        photoButtons[ThreshHandler.THRESH_OTSU]=rootView.findViewById(R.id.threshold_otsu);
        photoButtons[ThreshHandler.PHOTO_TO_COLOR_SKETCH]=rootView.findViewById(R.id.photo_to_soft);
        photoButtons[ThreshHandler.PHOTO_TO_BLACK_SKETCH]=rootView.findViewById(R.id.photo_to_sketch);
        photoButtons[ThreshHandler.BLACK_AND_WHITE]=rootView.findViewById(R.id.black_and_white);

        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++)
            photoButtons[i].setOnClickListener(onPhotoClickListener);

    }
    private View.OnClickListener onPhotoClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //ThreshHandler.METHOD=ThreshHandler.PHOTO_OUTLINE;
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
            //thresh.sendMessage(ThreshHandler.THRESH_PHOTO_VIEW);

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
            //thresh.sendMessage(ThreshHandler.THRESH_PHOTO_VIEW);
            //threshold(false);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //thresh.sendMessage(ThreshHandler.THRESH_PHOTO_VIEW);
            threshold(false);
        }
    };
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id=view.getId();
            switch (id){

                case R.id.next:
                    threshold(true);
                    //imageView.resetBitmapForDraw();
                    //finish();
                    break;


            }

        }
    };
    private Threshold thresh;
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
    public void threshold(boolean forDraw) {


/*
        Threshold.ERODE_METHOD=erode.isChecked();
        Threshold.DILATE_METHOD=dilate.isChecked();
        Threshold.GAUSSIAN_METHOD=gaussian.isChecked();*/
        thresh.setForDraw(forDraw);
        //if(forDraw){
           /* final TextView textView=new TextView(this);
            textView.setTextColor(Color.BLUE);
            textView.setText("Please wait...");
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativeLayout.addView(textView,layoutParams);
            thresh.setTextView(textView);*/



       /* }else {
            thresh.setProgressBar(null);
        }*/
        final ProgressBar progressBar=new ProgressBar(getContext());
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,layoutParams);
        thresh.setProgressBar(progressBar);
        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++)
            photoButtons[i].setEnabled(false);
        kernelBar.setEnabled(false);
        thresh.sendMessage();
        //new Threshold(relativeLayout,textView,imageView,this,forDraw).start();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.border_fragment,container,false);
        imageView=(ImageView) rootView.findViewById(R.id.imageView);
        imageView.setImageBitmap(GetImage.imageViewBitmap);

        relativeLayout=(RelativeLayout)rootView.findViewById(R.id.relativeLayout);

        pictureInfo=(TextView)rootView.findViewById(R.id.pictureInfo);
        //imageButtons[LOAD_MENU]=(ImageButton)findViewById(R.id.load);
        imageButtons[REDO_MENU]=(ImageButton)rootView.findViewById(R.id.redo);
        imageButtons[NEXT_MENU]=(ImageButton)rootView.findViewById(R.id.next);
        //imageButtons[SAVE_MENU]=(ImageButton)findViewById(R.id.save);
        for(int i=0;i<BOTTOM_MENU_NUM;i++)
            imageButtons[i].setOnClickListener(onClickListener);

        setTopMenu(rootView);

        thresh=new Threshold(relativeLayout,imageView,photoButtons,kernelBar,getContext());
        thresh.start();
        return rootView;//super.onCreateView(inflater, container, savedInstanceState);
    }
}
