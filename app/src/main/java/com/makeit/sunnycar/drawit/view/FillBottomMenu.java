package com.makeit.sunnycar.drawit.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.data.Frame;import com.makeit.sunnycar.drawit.listener.RecyclerViewListener;
import com.makeit.sunnycar.drawit.thread.SaveImage;
import com.makeit.sunnycar.drawit.thread.SetLayerBitmap;
import com.makeit.sunnycar.drawit.ui.MainActivity;

/**
 * Created by gmltj on 2018-05-11.
 */

public class FillBottomMenu extends LinearLayout
        implements View.OnClickListener, RecyclerViewListener.OnStartDragListener {
    private static final int MENU_NUMBER = 6;
    //private static final int MENU_EDIT = 0;
    private static final int MENU_LAYER = 0;
    //public static final int MENU_ZOOM = 1;
    private static final int MENU_UNDO = 1;
    private static final int MENU_REDO = 2;
    public static final int MENU_RESIZE = 3;
    private static final int MENU_SAVE = 4;
    private static final int MENU_READD_SKETCH = 5;

    //last
    //private static final int MENU_DRAW = 7;

    public ImageButton[] imageButtons=new ImageButton[MENU_NUMBER];
    //public static Bitmap[] imageBitmap=new Bitmap[MENU_NUMBER+1];
    public static Drawable zoomIcon,drawIcon;
    private int WIDTH;//,HEIGHT;
    //private final int UNIT_LENGTH=80;
    private Context context;
    private FrameLayout frameLayout;
    private View layerView;
    private RelativeLayout parentLayout;
    //public ImageView strokeWidth;
    //private ItemTouchHelper itemTouchHelper;
    //private PopupWindow editWindow;

    public void setLayerAdapter(LayerAdapter layerAdapter) {
        this.layerAdapter = layerAdapter;
    }

    private LayerAdapter layerAdapter;
    private DrawingContourView drawingContourView;
    private FillUpperMenu fillUpperMenu;

    public void setFillUpperMenu(FillUpperMenu fillUpperMenu) {
        this.fillUpperMenu = fillUpperMenu;
    }

    public void setDrawingContourView(DrawingContourView drawingContourView) {
        this.drawingContourView = drawingContourView;
    }

    public FillBottomMenu(Context context) {
        super(context);
        init(context);
    }

    public FillBottomMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public FillBottomMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public FillBottomMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        WIDTH=r-l;
        super.onLayout(changed, l, t, r, b);

    }



    private void init(Context context) {
        this.context=context;

      View bottomMenuView=LayoutInflater.from(context).inflate(R.layout.fill_bottom_layout,this,false);
      LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(bottomMenuView,layoutParams);
        zoomIcon=context.getDrawable(R.mipmap.ic_zoom);
        drawIcon=context.getDrawable(R.mipmap.ic_color);
        imageButtons[MENU_LAYER]=(ImageButton)bottomMenuView.findViewById(R.id.layer);
        //imageButtons[MENU_ZOOM]=(ImageButton)bottomMenuView.findViewById(R.id.zoom);
        imageButtons[MENU_UNDO]=(ImageButton)bottomMenuView.findViewById(R.id.undo);
        imageButtons[MENU_REDO]=(ImageButton)bottomMenuView.findViewById(R.id.redo);
        imageButtons[MENU_RESIZE]=(ImageButton)bottomMenuView.findViewById(R.id.origin);
        imageButtons[MENU_SAVE]=(ImageButton)bottomMenuView.findViewById(R.id.save);
        imageButtons[MENU_READD_SKETCH]=(ImageButton)bottomMenuView.findViewById(R.id.reSketch);
        for(int i=0;i<MENU_NUMBER;i++)
            imageButtons[i].setOnClickListener(this);

    }
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id=view.getId();
            switch (id){

                ////
                case R.id.add_layer:
                        layerAdapter.addLayer();
                  /*  else
                        context.startActivity(new Intent(context, PremiumActivity.class));
                  */  break;
                case R.id.delete:
                    layerAdapter.removeLayer();
                    break;

            }
        }
    };



    @Override
    public void onClick(View view) {

            if(view.equals(imageButtons[MENU_LAYER])){
                showLayerPopup();
            }
            else if(view.equals(imageButtons[MENU_UNDO])){
                drawingContourView.onClickUndo();
                //refreshRedoAndUndoImageBt();

            }else if(view.equals(imageButtons[MENU_REDO])){
                drawingContourView.onClickRedo();
                //refreshRedoAndUndoImageBt();
            }else if(view.equals(imageButtons[MENU_RESIZE])){

               /* 0601
               DrawingContourView.CURRENT_METHOD=DrawingContourView.RESIZE_METHOD;
                DrawingContourView.CURRENT_RESIZE_LAYER=Frame.currentFrame.currentLayer;
                LayerScaleAndRotateListener.setLayer(DrawingContourView.CURRENT_RESIZE_LAYER);*/
                //drawingContourView.setCenterRectF();
                Frame.currentFrame.setBitmapBeforeAndRemoveAllPen();

                //0605
                if(DrawingContourView.CURRENT_METHOD==DrawingContourView.DRAW_METHOD){
                    DrawingContourView.CURRENT_METHOD = DrawingContourView.RESIZE_METHOD;
                    imageButtons[MENU_RESIZE].setImageDrawable(drawIcon);
                    fillUpperMenu.setDrawingButtonBackgroundBlue(-1);
                }else {
                    DrawingContourView.CURRENT_METHOD = DrawingContourView.DRAW_METHOD;
                    imageButtons[MENU_RESIZE].setImageDrawable(zoomIcon);
                    fillUpperMenu.setDrawingButtonBackgroundBlue(DrawingContourView.draw_mode);

                }
            }else if(view.equals(imageButtons[MENU_SAVE])){

                saveImage();
            }else if(view.equals(imageButtons[MENU_READD_SKETCH])){
                Frame.currentFrame.setResizedBitmapAndRemoveAllPens();
                Frame.currentFrame.resetResizeFactor();
                if(!hasPermissions(MainActivity.PERMISSIONS)){//퍼미션 허가했는지 여부
                    requestNessaryPermissions(MainActivity.PERMISSIONS);//허가 안되었으면 사용자에게 요청
                }else {
                    //이미 사용자에게 퍼미션 허가 받음
                    //Log.e("oncreate","permission");
                    distpatchPhotoSelectionIntent();

                }

            }
    }

    private void requestNessaryPermissions(String[] permissions){
        ActivityCompat.requestPermissions((Activity)context,permissions,MainActivity.PERMISSION_REQUES_CODE);
    }
    private void distpatchPhotoSelectionIntent() {
        /*Intent intent=new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

*/
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        ((Activity)context).startActivityForResult(intent,MainActivity.RESULT_LOAD_IMAGE);
    }


    public void saveImage(){

        if(hasPermissions(MainActivity.PERMISSIONS)){
            new SaveImage(context,true,layerAdapter).start();
        }else{
            requestNessaryPermissions(MainActivity.PERMISSIONS);//허가 안되었으면 사용자에게 요청

        }

    }
    private boolean hasPermissions(String[] permissions){
        int result=-1;
        for(int i=0;i<permissions.length;i++){
            result= ContextCompat.checkSelfPermission(context,permissions[i]);

        }
        if(result== PackageManager.PERMISSION_GRANTED){//0
            return true;
        }else{
            return false;
        }

    }
    public void refreshRedoAndUndoImageBt() {
        if(Frame.currentFrame.LAYER_HISTORY.size()==0)
            imageButtons[MENU_UNDO].setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
        else
            imageButtons[MENU_UNDO].setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        if(Frame.currentFrame.undoLayerHistory.size()==0)
            imageButtons[MENU_REDO].setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_IN);
        else
            imageButtons[MENU_REDO].setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);

    }

    private void showLayerPopup() {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater != null) {
            layerView = layoutInflater.inflate(R.layout.layer_popup,null);
            RecyclerView recyclerView=(RecyclerView)layerView.findViewById(R.id.recyclerView);
            ImageButton add=(ImageButton)layerView.findViewById(R.id.add_layer);
            ImageButton delete=(ImageButton)layerView.findViewById(R.id.delete);
            add.setOnClickListener(onClickListener);
            delete.setOnClickListener(onClickListener);

            new SetLayerBitmap(context,recyclerView,layerAdapter,frameLayout,layerView).start();


           /* LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);

            layerAdapter.setOnStartDragListener(this);
            ItemTouchHelper.Callback callback=new EditItemTouchHelperCallback(layerAdapter);
            itemTouchHelper=new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(layerAdapter);*/




            //dismiss when touched outside
            //popupWindow.setBackgroundDrawable(context.getDrawable(R.color.transparent));
            //popupWindow.setOutsideTouchable(true);
            //popupWindow.setFocusable(true);
            //int location[]=new int[2];
            //this.getLocationOnScreen(location);


            //popupWindow.showAtLocation(frameLayout, Gravity.NO_GRAVITY,DrawingContourView.WINDOW_WIDTH,0);
            //popupWindow.showAsDropDown(view);


        }

    }
    private void showPhotoDialog() {
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

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    }

    public void setLayout(RelativeLayout parentLayout, FrameLayout frameLayout) {
        this.frameLayout=frameLayout;
        this.parentLayout=parentLayout;
    }

    public void addSketch() {
            Frame.currentFrame.addSketch();
    }
}
