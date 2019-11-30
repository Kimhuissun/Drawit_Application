package com.makeit.sunnycar.drawit.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.makeit.sunnycar.drawit.R;

/**
 * Created by gmltj on 2018-04-24.
 */

public class TuneDialog extends DialogFragment implements View.OnClickListener {
    private static final int _1280 = 1280;
    public static final int _720 = 720;
    private static final int _304 = 304;
    private static final int _540 = 540;
    private static final int _404 = 404;
    public static final String NEW_CANVAS = "new canvas";

   /* public static int TYPE=0;
    public static int METHOD=0;
    public static int ADAPTIVE_METHOD=0;
    public static boolean draw_contour=false;
    public static boolean draw_object=false;
    public static  int CONTOUR_LENGTH=3;
    public static int THRESH_VALUE=0;
    private CheckBox threshold,adaptive_threshold,find_contours,find_object;
    private CheckBox thresh_binary,thresh_binary_inv,
                        thresh_trunc,thresh_tozero,thresh_tozero_inv,thresh_otsu; //threshold_type;
    private SeekBar thresh_value,contour_length;

    private CheckBox adaptive_thresh_binary,adaptive_thresh_binary_inv;//adaptive_threshold_type

    private CheckBox mean,gaussian;//adaptive_threshold_method*/


    private Button saveBt;
    private RadioGroup radioGroup;
    private TextView widthText,heightText,sketchSizeText;
    private NumberPicker numberPicker;
    private static int radioButtonId;

    //private int widthPx,heightPx;
    public interface OnSaveTuneListener{
        void onSave();
    }
    private OnSaveTuneListener onSaveTuneListener;
    public void setOnSaveTuneListener(OnSaveTuneListener onSaveTuneListener){
        this.onSaveTuneListener=onSaveTuneListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.tune_dialog,container,false);
        saveBt=view.findViewById(R.id.save);
        saveBt.setOnClickListener(this);
        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);

        widthText=(TextView)view.findViewById(R.id.width);
        heightText=(TextView)view.findViewById(R.id.height);
        //setCanvasSize(_720,_720);//,R.id.instagram_1_1);
        if(getArguments().getBoolean(NEW_CANVAS,false)){
            radioGroup.check(R.id.instagram_1_1);
            setCanvasSize(_720,_720,R.id.instagram_1_1);
            //radioButtonId=0;
        }else {
            radioGroup.check(radioButtonId);
            setCanvasSizeText(DrawingContourView.WIDTH,DrawingContourView.HEIGHT);
            for(int i=0;i<radioGroup.getChildCount();i++)
                ((RadioButton)radioGroup.getChildAt(i)).setEnabled(false);
        }

        sketchSizeText=(TextView)view.findViewById(R.id.sketchSize);
        numberPicker=(NumberPicker)view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(DrawingContourView.MIN_FPS);
        numberPicker.setMaxValue(DrawingContourView.MAX_FPS);
        numberPicker.setValue((int) (1000/DrawingContourView.frameLengthInMilliSeconds));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                DrawingContourView.frameLengthInMilliSeconds=1000/newVal;
            }
        });
      /*  if(GetImage.imageViewBitmap!=null){
            sketchSizeText.setText(getString(R.string.current)+
                    String.valueOf(GetImage.imageViewBitmap.getWidth())
                    +" x "+String.valueOf(GetImage.imageViewBitmap.getHeight()));//getHeight unit is pixel

        }*/
         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id=radioGroup.getCheckedRadioButtonId();
                switch (id){
                    case R.id.youtube_720:
                        setCanvasSize(_1280,_720,id);
                        break;
                    case R.id.instagram_16_9:
                        setCanvasSize(_1280,_720,id);
                        break;
                    case R.id.instagram_1_1:
                        setCanvasSize(_720,_720,id);
                        break;
                    case R.id.facebook_720:
                        setCanvasSize(_1280,_720,id);
                        break;
                    case R.id.tumblr_16_9:
                        setCanvasSize(_540,_304,id);
                        break;
                    case R.id.tumblr_4_3:
                        setCanvasSize(_540,_404,id);
                        break;
                }
            }
        });
        return view;// super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setCanvasSize(int widthPx, int heightPx) {
        widthText.setText(String.valueOf(widthPx));
        heightText.setText(String.valueOf(heightPx));
    }

    private void setCanvasSize(int widthPx, int heightPx,int id) {
        widthText.setText(String.valueOf(widthPx));
        heightText.setText(String.valueOf(heightPx));
        DrawingContourView.WIDTH=widthPx;
        DrawingContourView.HEIGHT=heightPx;
        radioButtonId=id;
    }
    private void setCanvasSizeText(int widthPx, int heightPx) {
        widthText.setText(String.valueOf(widthPx));
        heightText.setText(String.valueOf(heightPx));
        //DrawingContourView.WIDTH=widthPx;
        //DrawingContourView.HEIGHT=heightPx;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.save){

          /*  int id=radioGroup.getCheckedRadioButtonId();
            switch (id){
                case R.id.sketchOutline:
                    break;
                case R.id.photoOutline:
                    break;

            }
*/
          if(radioButtonId!=0) {
              if (onSaveTuneListener != null)
                  onSaveTuneListener.onSave();
              dismiss();
          }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params=getDialog().getWindow().getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.MATCH_PARENT;

        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

    }

}
