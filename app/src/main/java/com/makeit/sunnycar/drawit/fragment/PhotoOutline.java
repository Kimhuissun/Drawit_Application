/*
package com.makeit.sunnycar.drawit.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makeit.sunnycar.removevideobackground.R;
import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.thread.Threshold;
import com.makeit.sunnycar.drawit.view.BorderView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PhotoOutline extends Fragment {

    public interface OnPhotoButtonsInitializedListener{
        void onInitialized(ImageButton[] imageButtons);
    }
    private OnPhotoButtonsInitializedListener onPhotoButtonsInitializedListener;
    public void setOnPhotoButtonsInitializedListener(OnPhotoButtonsInitializedListener onPhotoButtonsInitializedListener){
        this.onPhotoButtonsInitializedListener=onPhotoButtonsInitializedListener;
    }
    //public static final String PHOTO_OUTLINE="Photo Outline";
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            int id = seekBar.getId();
            switch (id) {
                case R.id.kernel: {
                    if (ThreshHandler.PHOTO_THRESH_METHOD >= ThreshHandler.THRESH_BINARY
                            && ThreshHandler.PHOTO_THRESH_METHOD <= ThreshHandler.THRESH_TOZERO) {
                        ThreshHandler.THRESH_VALUE = i;
                    } else if (ThreshHandler.PHOTO_THRESH_METHOD == ThreshHandler.ADAPTIVE_THRESH_MEAN
                            || ThreshHandler.PHOTO_THRESH_METHOD == ThreshHandler.ADAPTIVE_THRESH_GAUSSIAN) {
                        ThreshHandler.KERNEL_SIZE = i;
                    }
                    kernelSize.setText(String.valueOf(i));

                    break;
                }

            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            threshold.sendMessage();

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            threshold.sendMessage();

        }
    };
    private Threshold threshold;

    private void setOtherBackgroundColorTransparent() {
        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++){
            if(ThreshHandler.PHOTO_THRESH_METHOD==i)
                imageButtons[i].setBackgroundColor(Color.WHITE);
            else   imageButtons[i].setBackgroundColor(Color.TRANSPARENT);

        }
    }

    private SeekBar kernelBar;
    private TextView kernelSize;
    private ImageButton[] imageButtons=new ImageButton[ThreshHandler.PHOTO_THRESH_COUNT];
    private View.OnClickListener onClickListener=new View.OnClickListener() {
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
                case R.id.black_and_white:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.BLACK_AND_WHITE;

                    break;
                }
                case R.id.threshold_otsu:{
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_OTSU;

                    break;
                }
            }
            setOtherBackgroundColorTransparent();
            changeSeekbar();
            threshold.sendMessage();

        }
    };

    private void changeSeekbar() {
        if(ThreshHandler.PHOTO_THRESH_METHOD>=ThreshHandler.THRESH_BINARY
                &&ThreshHandler.PHOTO_THRESH_METHOD<=ThreshHandler.THRESH_TOZERO){
            kernelBar.setVisibility(View.VISIBLE);
            kernelSize.setVisibility(View.VISIBLE);

            kernelBar.setMax(ThreshHandler.THRESH_VALUE_MAX);
            kernelBar.setProgress((int) ThreshHandler.THRESH_VALUE);
            kernelSize.setText(String.valueOf((int) ThreshHandler.THRESH_VALUE));

        }else if(ThreshHandler.PHOTO_THRESH_METHOD==ThreshHandler.ADAPTIVE_THRESH_MEAN
                ||ThreshHandler.PHOTO_THRESH_METHOD==ThreshHandler.ADAPTIVE_THRESH_GAUSSIAN){
            kernelBar.setVisibility(View.VISIBLE);
            kernelSize.setVisibility(View.VISIBLE);
            kernelBar.setMax(ThreshHandler.KERNEL_SIZE_MAX);

            kernelBar.setProgress((int) ThreshHandler.KERNEL_SIZE);
            kernelSize.setText(String.valueOf((int) ThreshHandler.KERNEL_SIZE));
        }else {
            kernelBar.setVisibility(View.INVISIBLE);
            kernelSize.setVisibility(View.INVISIBLE);

        }
    }

    public static Fragment newInstance() {

        //Bundle args=new Bundle();
        PhotoOutline photoOutline=new PhotoOutline();
        //photoOutline.setArguments();
        return photoOutline;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view=inflater.inflate(R.layout.photo_extract_layout,container,false);
        kernelBar=(SeekBar)view.findViewById(R.id.kernel);
        kernelSize=(TextView)view.findViewById(R.id.kernelSize);
        changeSeekbar();
        kernelBar.setOnSeekBarChangeListener(onSeekBarChangeListener);


        imageButtons[ThreshHandler.THRESH_BINARY]=view.findViewById(R.id.threshold_binary);
        imageButtons[ThreshHandler.THRESH_BINARY_INV]=view.findViewById(R.id.threshold_binary_inv);
        imageButtons[ThreshHandler.THRESH_TRUNC]=view.findViewById(R.id.threshold_binary_trunc);
        imageButtons[ThreshHandler.THRESH_TOZERO]=view.findViewById(R.id.threshold_binary_tozero);
        imageButtons[ThreshHandler.ADAPTIVE_THRESH_MEAN]=view.findViewById(R.id.adaptive_threshold_mean);
        imageButtons[ThreshHandler.ADAPTIVE_THRESH_GAUSSIAN]=view.findViewById(R.id.adaptive_threshold_gaussian);
        imageButtons[ThreshHandler.BLACK_AND_WHITE]=view.findViewById(R.id.black_and_white);
        imageButtons[ThreshHandler.THRESH_OTSU]=view.findViewById(R.id.threshold_otsu);

        for(int i=0;i<ThreshHandler.PHOTO_THRESH_COUNT;i++)
            imageButtons[i].setOnClickListener(onClickListener);

        if(onPhotoButtonsInitializedListener!=null) {
            onPhotoButtonsInitializedListener.onInitialized(imageButtons);
        }
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setThreshThread(Threshold threshold) {
        this.threshold=threshold;
    }


}
*/
