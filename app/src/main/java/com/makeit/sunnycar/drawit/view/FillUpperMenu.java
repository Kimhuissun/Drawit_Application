package com.makeit.sunnycar.drawit.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.adapter.PlayAdapter;
import com.makeit.sunnycar.drawit.asynctask.SetPlayBitmap;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.data.Layer;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by gmltj on 2018-05-16.
 */

public class FillUpperMenu extends LinearLayout implements View.OnClickListener {
    private static final int MENU_NUMBER = 7;
    private static final int MENU_PEN = 0;
    private static final int MENU_ERASER = 1;
    private static final int MENU_SPOID = 2;
    private static final int MENU_COLOR = 3;
    private static final int MENU_SIZE = 4;
    private static final int MENU_PLAY = 5;
    private static final int MENU_RETURN = 6;
    //  private final int UNIT_LENGTH=80;
    private Context context;
    public LineView strokeWidth;
    //private ImageButton[] editButton=new ImageButton[3];
    private FillBottomMenu fillBottomMenu;
    private DrawingContourView drawingContourView;
    private View playPopup;
    private FillUpperMenu fillUpperMenu;
    //private SetPlayBitmap setPlayBitmap;
    //private boolean animStart=false;
    //private View animView;
    //private FrameLayout frameLayout;
    private RelativeLayout menuLayout;
    private ImageButton animStop;
    private LayerAdapter layerAdapter;
    private FrameLayout frameLayout;
    private View sizeView;

    public void setDrawingContourView(DrawingContourView drawingContourView) {
        this.drawingContourView = drawingContourView;
    }

    private boolean isPlay=false;
    private ImageButton[] imageButtons=new ImageButton[MENU_NUMBER];
    private LinearLayout linearLayout;
   // private RelativeLayout relativeLayout;
    private PlayAdapter playAdapter;

    public void setPlayAdapter(PlayAdapter playAdapter) {
        this.playAdapter = playAdapter;
    }

    public void setRelativeLayout(RelativeLayout menuLayout) {
        //this.relativeLayout = relativeLayout;
        this.menuLayout=menuLayout;
    }
    // private static Bitmap[] imageBitmap=new Bitmap[MENU_NUMBER];
    public void setFillBottomMenu(FillBottomMenu fillBottomMenu) {
        this.fillBottomMenu = fillBottomMenu;
    }

    public FillUpperMenu(Context context) {
        super(context);
        init(context);
    }
    public FillUpperMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public FillUpperMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public FillUpperMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }
    private void init(Context context) {
        this.context=context;
        this.fillUpperMenu=this;
        View upperMenuView=LayoutInflater.from(context).inflate(R.layout.fill_upper_layout,this,false);
        LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(upperMenuView,layoutParams);

        linearLayout=(LinearLayout)upperMenuView.findViewById(R.id.linearLayout);
        imageButtons[MENU_PEN]=(ImageButton)upperMenuView.findViewById(R.id.pen);
        imageButtons[MENU_ERASER]=(ImageButton)upperMenuView.findViewById(R.id.eraser);
        imageButtons[MENU_SPOID]=(ImageButton)upperMenuView.findViewById(R.id.spoid);
        imageButtons[MENU_COLOR]=(ImageButton)upperMenuView.findViewById(R.id.palette);
        imageButtons[MENU_SIZE]=(ImageButton)upperMenuView.findViewById(R.id.size);
        imageButtons[MENU_PLAY]=(ImageButton)upperMenuView.findViewById(R.id.play);
        imageButtons[MENU_RETURN]=(ImageButton)upperMenuView.findViewById(R.id.back);

        for(int i=0;i<MENU_NUMBER;i++)
            imageButtons[i].setOnClickListener(this);

    }

    private void showPlayPopup() {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(layoutInflater!=null){
            playPopup = layoutInflater.inflate(R.layout.play_popup, null);
            RecyclerView recyclerView=(RecyclerView)playPopup.findViewById(R.id.recyclerView);
            new SetPlayBitmap(context,recyclerView,playAdapter,layerAdapter).execute();

            ImageButton playBt=(ImageButton)playPopup.findViewById(R.id.animStart);
            ImageButton addBt=(ImageButton)playPopup.findViewById(R.id.add_frame);
            ImageButton deleteBt=(ImageButton)playPopup.findViewById(R.id.remove_frame);
            ImageButton editBt=(ImageButton)playPopup.findViewById(R.id.edit);
            ImageButton drawBt=(ImageButton)playPopup.findViewById(R.id.draw);
            playBt.setOnClickListener(this);
            addBt.setOnClickListener(this);
            deleteBt.setOnClickListener(this);
            editBt.setOnClickListener(this);
            drawBt.setOnClickListener(this);

            SeekBar frameSeekbar=(SeekBar)playPopup.findViewById(R.id.frameOpacity);
            playAdapter.setOpacityBar(frameSeekbar);
            frameSeekbar.setMax(Layer.OPACITY_MAX);
            frameSeekbar.setProgress(Frame.otherFrameOpacity);
            frameSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser)
                        Frame.currentFrame.setOtherFrameOpacity(progress);
                   /* synchronized (Frame.currentFrame){
                        Frame.currentFrame.drawFramesBitmap();
                    }*/


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //layoutParams.addRule(RelativeLayout.BELOW,R.id.fillUpperMenu);
            menuLayout.removeView(fillUpperMenu);
            menuLayout.removeView(fillBottomMenu);
            menuLayout.addView(playPopup,layoutParams);
        }
    }
    private void showSizePopup(){
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null) {
            sizeView = layoutInflater.inflate(R.layout.line_popup, null);
            strokeWidth=(LineView)sizeView.findViewById(R.id.strokeWidth);//new ImageView(context);
            //strokeWidth.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            /*strokeWidth.setMinimumWidth((int) (DrawingContourView.PAINT_STROKE_WIDTH* ScaleAndRotateListener.scaleFactor));
            strokeWidth.setMinimumHeight((int) (DrawingContourView.PAINT_STROKE_WIDTH*ScaleAndRotateListener.scaleFactor));
            if(DrawingContourView.LINE_KIND==DrawingContourView.ORG_LINE){
                strokeWidth.setImageDrawable(context.getDrawable(R.drawable.line_stroke_width));
            }else {
                strokeWidth.setImageDrawable(context.getDrawable(R.drawable.line_stroke_gradient));

            }
            strokeWidth.getDrawable().setColorFilter(DrawingContourView.CURRENT_COLOR, PorterDuff.Mode.SRC_IN);
*/
            //addView(strokeWidth);
            final TextView size = sizeView.findViewById(R.id.size);
            size.setText(String.valueOf(DrawingContourView.PAINT_STROKE_WIDTH));
            SeekBar sizeSeekBar = sizeView.findViewById(R.id.contour_length);
            sizeSeekBar.setProgress(DrawingContourView.PAINT_STROKE_WIDTH);
            sizeSeekBar.setMax(DrawingContourView.PAINT_STROKE_MAX_WIDTH-1);

            final LinearLayout blurLinearLayout=(LinearLayout)sizeView.findViewById(R.id.blurLinearLayout);
            if(DrawingContourView.LINE_KIND==DrawingContourView.ORG_LINE)
                blurLinearLayout.setVisibility(GONE);
            else {
                blurLinearLayout.setVisibility(VISIBLE);

            }
            final TextView blur = sizeView.findViewById(R.id.blur);
            blur.setText(String.valueOf(DrawingContourView.BLUR_RADIUS));
            SeekBar blurSeekBar = sizeView.findViewById(R.id.blurSeekBar);
            blurSeekBar.setProgress(DrawingContourView.BLUR_RADIUS);
            blurSeekBar.setMax(DrawingContourView.PAINT_STROKE_MAX_WIDTH/2-1);

            SeekBar.OnSeekBarChangeListener onSeekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    int id=seekBar.getId();
                    switch (id){
                        case R.id.contour_length:
                            DrawingContourView.changeStrokeWidth(i+1);
                            size.setText(String.valueOf(i+1));
                            /*strokeWidth.setMinimumWidth((int) (i * ScaleAndRotateListener.scaleFactor));
                            strokeWidth.setMinimumHeight((int) (i * ScaleAndRotateListener.scaleFactor));
*/
                            break;
                        case R.id.blurSeekBar:
                            DrawingContourView.changeStrokeGradientWidth(i+1);
                            blur.setText(String.valueOf(i+1));
                            break;

                    }
                    strokeWidth.invalidate();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };
            sizeSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
            blurSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

            ImageButton orgLine=sizeView.findViewById(R.id.orgLine);
            ImageButton gradientLine=sizeView.findViewById(R.id.gradientLine);
            View.OnClickListener onClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id=view.getId();
                    switch (id){
                        case R.id.orgLine:
                            DrawingContourView.LINE_KIND=DrawingContourView.ORG_LINE;
                            blurLinearLayout.setVisibility(GONE);

                            //strokeWidth.setImageDrawable(context.getDrawable(R.drawable.line_stroke_width));
                            break;

                        case R.id.gradientLine:
                            DrawingContourView.LINE_KIND=DrawingContourView.GRADIENT_LINE;
                            blurLinearLayout.setVisibility(VISIBLE);
                            //strokeWidth.setImageDrawable(context.getDrawable(R.drawable.line_stroke_gradient));
                            break;
                    }
                    //strokeWidth.getDrawable().setColorFilter(DrawingContourView.CURRENT_COLOR, PorterDuff.Mode.SRC_IN);
                    //strokeWidth.setMaxHeight((DrawingContourView.PAINT_STROKE_WIDTH));
                    //strokeWidth.setMaxWidth(DrawingContourView.PAINT_STROKE_WIDTH);
                    strokeWidth.invalidate();
                }
            };
            orgLine.setOnClickListener(onClickListener);
            gradientLine.setOnClickListener(onClickListener);

            FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            sizeView.setVisibility(GONE);
            frameLayout.addView(sizeView,layoutParams);

            /*PopupWindow sizeWindow=new PopupWindow(sizeView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT );
            //dismiss when touched outside
            sizeWindow.setBackgroundDrawable(context.getDrawable(R.color.colorPrimary));
            sizeWindow.setOutsideTouchable(true);
            sizeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    strokeWidth=null;
                }
            });
           // int location[]=new int[2];
           // this.getLocationOnScreen(location);
            //editWindow.showAsDropDown(this,0,-location[1]-this.getHeight(), Gravity.NO_GRAVITY);
            sizeWindow.showAtLocation(frameLayout, Gravity.NO_GRAVITY,
                    0,0);*/


        }
    }

    private void showColorPopup() {

        AmbilWarnaDialog dialog=new AmbilWarnaDialog(context,
                DrawingContourView.getColor(), true,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        DrawingContourView.setColor(color);
                        if(strokeWidth!=null)
                            strokeWidth.invalidate();

                        //strokeWidth.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    }
                }
        );
        dialog.show();
    }

    public void setDrawingButtonBackgroundBlue(int selected) {
        for(int i=0;i<3;i++){
            if(i!=selected)
                imageButtons[i].setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            else imageButtons[i].setColorFilter(context.getResources().getColor(R.color.sky), PorterDuff.Mode.SRC_IN);

        }
    }

    @Override
    public void onClick(View view) {
        fillBottomMenu.imageButtons[FillBottomMenu.MENU_RESIZE].setImageDrawable(FillBottomMenu.zoomIcon);
        DrawingContourView.CURRENT_METHOD=DrawingContourView.DRAW_METHOD;

        int id=view.getId();
        if(id==R.id.pen){
            DrawingContourView.draw_mode = DrawingContourView.pen_mode;
            setDrawingButtonBackgroundBlue(DrawingContourView.pen_mode);
        }else if(id==R.id.eraser){
            DrawingContourView.draw_mode = DrawingContourView.eraser_mode;
            setDrawingButtonBackgroundBlue(DrawingContourView.eraser_mode);

        }else if(id==R.id.spoid){
            DrawingContourView.draw_mode=DrawingContourView.spoid_mode;
            setDrawingButtonBackgroundBlue(DrawingContourView.spoid_mode);
        }else if(id==R.id.palette){
            showColorPopup();

        }else if(id==R.id.size){

           if(sizeView.getVisibility()==VISIBLE)
               sizeView.setVisibility(GONE);
           else sizeView.setVisibility(VISIBLE);
        }else if(id==R.id.play){
            showPlayPopup();

        }else if(id==R.id.draw){
            if(playPopup!=null){
                menuLayout.removeView(playPopup);
                addDrawMenu();
            }

        }else if(id==R.id.animStart){
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater!=null){
                //animView=layoutInflater.inflate(R.layout.anim_layout,null);
                //ImageButton animStop=animView.findViewById(R.id.animStop);
                animStop=new ImageButton(context);
                animStop.setImageDrawable(getResources().getDrawable(R.mipmap.ic_stop,null));
                animStop.setBackgroundColor(getResources().getColor(R.color.transparent));
                animStop.setOnClickListener(this);

                menuLayout.removeView(playPopup);

                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                menuLayout.addView(animStop,layoutParams);

                drawingContourView.animStart();
                /*RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.ABOVE,R.id.adView);
                relativeLayout.addView(animView,layoutParams);*/
            }
          //  LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //if(layoutInflater!=null){


               /* relativeLayout.removeView(menuLayout);
                animView=new PlayView(context);
                animView.setBackgroundColor(Color.LTGRAY);
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);

                relativeLayout.addView(animView,layoutParams);
                ((ImageButton)view).setImageDrawable(context.getDrawable(R.mipmap.ic_stop));*/



           // }


        }else if(view.equals(animStop)){
            ((ImageButton)view).setColorFilter(R.color.red, PorterDuff.Mode.SRC_ATOP);
            drawingContourView.animStop();
           /* RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ABOVE,R.id.adView);
            relativeLayout.addView(fillBottomMenu,layoutParams);

            RelativeLayout.LayoutParams thisLayoutParams=new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            thisLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
            relativeLayout.addView(fillUpperMenu,thisLayoutParams);*/
           menuLayout.removeView(view);
           showPlayPopup();
            //addDrawMenu();

        }else if(id==R.id.add_frame){
            if(layerAdapter.frames.size()< Frame.MAX_FRAME_COUNT)
                playAdapter.addFrame();
        }else if(id==R.id.remove_frame){
            playAdapter.removeFrame();
        }else if(id==R.id.edit){
            showEditDialog();
        }else if(id==R.id.back){
            drawingContourView.reset();
        }
    }

    private void addDrawMenu() {
        menuLayout.addView(fillUpperMenu);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.BELOW,R.id.fillUpperMenu);
        menuLayout.addView(fillBottomMenu,layoutParams);
    }

    private void showEditDialog() {
        TuneDialog tuneDialog=new TuneDialog();

        android.app.FragmentManager fm=((Activity)context).getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.add(tuneDialog,"tuneDialog");
        Bundle args=new Bundle();
        args.putBoolean(TuneDialog.NEW_CANVAS,false);
        tuneDialog.setArguments(args);
        tuneDialog.setOnSaveTuneListener(new TuneDialog.OnSaveTuneListener() {
            @Override
            public void onSave() {
                //drawingContourView.invalidate();
            }
        });
        fragmentTransaction.commit();
    }


    public void setLayerAdapter(LayerAdapter layerAdapter) {
        this.layerAdapter=layerAdapter;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
        showSizePopup();
    }


}
