/*
package com.makeit.sunnycar.drawit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makeit.sunnycar.removevideobackground.R;
import com.makeit.sunnycar.drawit.handler.ThreshHandler;
import com.makeit.sunnycar.drawit.thread.Threshold;

public class SketchOutline extends Fragment {
    private LinearLayout kernelLayout;
    private LinearLayout blurLayout;
    //private RelativeLayout relativeLayout;
    private CheckBox erode,dilate,gaussian;
    private SeekBar kernelBar,gaussianSeekbar;
    private Threshold threshold;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    CheckBox.OnCheckedChangeListener onCheckedChangeListener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int id=compoundButton.getId();

            if(id==R.id.gaussian){
                if(b)  blurLayout.setVisibility(View.VISIBLE);
                else blurLayout.setVisibility(View.INVISIBLE);
            }else if(b){
                if(id==R.id.erode){
                    dilate.setChecked(false);
                }else if(id==R.id.dilate){
                    erode.setChecked(false);
                }
                kernelLayout.setVisibility(View.VISIBLE);
            }else if(!erode.isChecked()&&!dilate.isChecked()){
                kernelLayout.setVisibility(View.INVISIBLE);
            }
            ThreshHandler.ERODE_METHOD=erode.isChecked();
            ThreshHandler.DILATE_METHOD=dilate.isChecked();
            ThreshHandler.GAUSSIAN_METHOD=gaussian.isChecked();

        }
    };

    public static SketchOutline newInstance(){
        SketchOutline sketchOutline=new SketchOutline();
        return sketchOutline;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sketch_extract_layout,container,false);
        kernelLayout=(LinearLayout)view.findViewById(R.id.kernelLayout);
        blurLayout=(LinearLayout)view.findViewById(R.id.blurLayout);
        erode=(CheckBox)view.findViewById(R.id.erode);
        dilate=(CheckBox)view.findViewById(R.id.dilate);
        gaussian=(CheckBox)view.findViewById(R.id.gaussian);


        erode.setOnCheckedChangeListener(onCheckedChangeListener);
        dilate.setOnCheckedChangeListener(onCheckedChangeListener);
        gaussian.setOnCheckedChangeListener(onCheckedChangeListener);

        kernelBar=(SeekBar)view.findViewById(R.id.kernel);
        kernelBar.setProgress((int) ThreshHandler.KERNEL_SIZE);
        final TextView kernelSize=(TextView)view.findViewById(R.id.kernelSize);
        kernelSize.setText(String.valueOf((int) ThreshHandler.KERNEL_SIZE));

        gaussianSeekbar=(SeekBar)view.findViewById(R.id.gaussianSeekbar);
        gaussianSeekbar.setProgress((int) ThreshHandler.GAUSSIAN_SIZE);
        final TextView gaussianSize=(TextView)view.findViewById(R.id.gaussianSize);
        gaussianSize.setText(String.valueOf((int) ThreshHandler.GAUSSIAN_SIZE));


        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int id=seekBar.getId();
                switch (id){
                    case R.id.kernel:
                        ThreshHandler.KERNEL_SIZE=i;
                        kernelSize.setText(String.valueOf(i));
                        break;
                    case R.id.gaussianSeekbar:
                        ThreshHandler.GAUSSIAN_SIZE=i;
                        gaussianSize.setText(String.valueOf(i));
                        break;
                }
                threshold.sendMessage();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        kernelBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        gaussianSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        //LinearLayout checkboxLayout=(LinearLayout)view.findViewById(R.id.checkboxLayout);

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setThreshThread(Threshold threshold) {
        this.threshold=threshold;
    }
}
*/
