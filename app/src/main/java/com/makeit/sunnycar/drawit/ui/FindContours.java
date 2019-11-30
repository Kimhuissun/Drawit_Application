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
    //private static final int LOAD_MENU=0;
    //private static final int TUNE_MENU=1;
    private static final int REDO_MENU=0;
    //private static final int EFFECT_MENU=2;
    private static final int NEXT_MENU=1;
    public static final String FROM_MAIN = "from_main";
    public static final int RESIZE = 1;
    //private static final int SAVE_MENU = 3;
/*    private static final String TAG="opencv";
    private static final String ADD="add";
    private static final String MINUS="minus";
    private static final String LINE_LENGTH="line_length";
    private static final String WATERSHED_LINE_LENGTH="watershed_line_length";
    private static final String ZOOM="zoom";
    private static final String BORDER="border";*/
    //private static final int DISK_SIZE=3500000;


    //private static Bitmap org_bitmap, final_bitmap;
    //public static InputStream bitmapInputStream;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
   // private BottomNavigationView bottomNavigationView;
    private TextView pictureInfo;
    private ImageButton[] imageButtons=new ImageButton[BOTTOM_MENU_NUM];
    private SeekBar kernelBar;
    private TextView kernelSize;
    private ImageButton[] photoButtons=new ImageButton[ThreshHandler.PHOTO_THRESH_COUNT];

    //private BottomNavigationView removeNavigationView;
    //private TabLayout tabLayout;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d("cameraactivity","opencv not loaded");
        }else{
            Log.d("cameraactivity","opencv loaded");

        }
    }

    /*private CheckBox dilate;
    private CheckBox erode;
    private CheckBox gaussian;
    private SeekBar kernelBar;
    private SeekBar gaussianSeekbar;*/

    // private boolean draw_contour;
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id=view.getId();
            switch (id){
               /* case R.id.load:
                    if(!hasPermissions(PERMISSIONS)){//퍼미션 허가했는지 여부
                        requestNessaryPermissions(PERMISSIONS);//허가 안되었으면 사용자에게 요청
                    }else {
                        //이미 사용자에게 퍼미션 허가 받음
                        //Log.e("oncreate","permission");
                        getImage();
                    }
                    break;*/

               /* case R.id.redo:

                    ThreshHandler.FINAL_IMAGE=null;
                    ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.ORIGINAL;
                    GetImage.copyBitmapByte=null;
                    imageView.invalidate();

                    break;
*/
                case R.id.next:
                    threshold(true);
                    //imageView.resetBitmapForDraw();
                    //finish();
                    break;
             /*   case R.id.save:
                    saveImage();
                    break;*/

            }

        }
    };
    private Threshold thresh;
    private ContourFragment contourFragment;
    //private ResizeFragment resizeFragment;
    //private OutlineFragmentPagerAdapter outlineFragmentPagerAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.border_fragment);
        //contourFragment=(ContourFragment)getSupportFragmentManager().findFragmentById(R.id.contour);
        //resizeFragment=new ResizeFragment();
        setView();
        resetCurrentMethod();


    }
    public void onFragmentChanged(int index){
        if(index==RESIZE){
            //getSupportFragmentManager().beginTransaction().replace(R.id.parentView,resizeFragment);
        }
    }

    private void resetCurrentMethod() {
        ThreshHandler.PHOTO_THRESH_METHOD=ThreshHandler.THRESH_BINARY;
    }

    private void setView() {

        //CropView cropView=new CropView(this);
        //addContentView(cropView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView=(ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(GetImage.imageViewBitmap);

        //cropView.setImageView(imageView);

        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);

        pictureInfo=(TextView)findViewById(R.id.pictureInfo);
        //imageButtons[LOAD_MENU]=(ImageButton)findViewById(R.id.load);
        imageButtons[REDO_MENU]=(ImageButton)findViewById(R.id.redo);
        imageButtons[NEXT_MENU]=(ImageButton)findViewById(R.id.next);
        //imageButtons[SAVE_MENU]=(ImageButton)findViewById(R.id.save);
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

         //photoButtons[ThreshHandler.ORIGINAL]=findViewById(R.id.original);
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

    /*private void setViewPager() {
        ViewPager viewPager=(ViewPager)findViewById(R.id.vPager);
        outlineFragmentPagerAdapter=new OutlineFragmentPagerAdapter(getSupportFragmentManager(),thresh);
        outlineFragmentPagerAdapter.setOnGetPhotoButtons(new OutlineFragmentPagerAdapter.OnGetPhotoButtons() {

            @Override
            public void onInitialized(ImageButton[] imageButtons) {
                photoButtons=imageButtons;
                if(!hasPermissions(PERMISSIONS)){//퍼미션 허가했는지 여부
                    requestNessaryPermissions(PERMISSIONS);//허가 안되었으면 사용자에게 요청
                }else {
                    //이미 사용자에게 퍼미션 허가 받음
                    //Log.e("oncreate","permission");
                    getImage();
                }
            }
        });

        viewPager.setAdapter(outlineFragmentPagerAdapter);
        viewPager.setCurrentItem(ThreshHandler.METHOD);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case ThreshHandler.PHOTO_OUTLINE:{
                        ThreshHandler.METHOD=ThreshHandler.PHOTO_OUTLINE;
                        break;
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }*/

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



 /*   private void tablistener(TabLayout.Tab tab) {
        String tag= (String) tab.getTag();

        switch (tag){
          *//*  case BORDER:
                DrawingContourView.CURRENT_MENU=DrawingContourView.BORDER_MENU;
                break;
            case ADD:
                DrawingContourView.CURRENT_MENU=DrawingContourView.ADD_MENU;
                // tabLayout.setEnabled(false);
                break;
            case MINUS:
                DrawingContourView.CURRENT_MENU=DrawingContourView.MINUS_MENU;
                //  tabLayout.setEnabled(false);
                break;*//*
            case LINE_LENGTH:
                showLinePopup(20);
                break;
            *//*case WATERSHED_LINE_LENGTH:
                DrawingContourView.CURRENT_MENU=DrawingContourView.WATERSHED_LINE_LENGTH_MENU;
                showLinePopup(50);
                break;*//*

        }
    }*/

  /*  public void saveImage(){

            if(hasPermissions(FindContours.PERMISSIONS)){
                new SaveImage(this,false,null).start();
            }

    }*/


    @Override
    protected void onResume() {
        super.onResume();

        if(!OpenCVLoader.initDebug()){
           // Log.d(TAG,"Onresume:: Internal opencv library not found");
            //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,mLoaderCallback);

        }else {
           // Log.d(TAG,"Onresume:: Internal opencv library found inside package. using it!");
            //mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
       /* if(Threshold.FINAL_IMAGE!=null) {
            imageView.setImageBitmap(Threshold.FINAL_IMAGE);
        }
        else {
            imageView.setImageBitmap(null);
            pictureInfo.setText("");
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try {
            thresh.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_LOAD_IMAGE:
                try {
                    if (resultCode == RESULT_OK && data != null) {
                        final Uri uri=data.getData();
                        String[] filePathColumn={MediaStore.Images.Media.DATA};
                        if (uri != null) {
                            //Cursor cursor=getContentResolver().query(uri,filePathColumn,null,null,null);
                            //cursor.moveToFirst();
                            //int columnIndex=cursor.getColumnIndexOrThrow(filePathColumn[0]);
                            //String picturePath=cursor.getString(columnIndex);
                            //org_bitmap=BitmapFactory.decodeFile(picturePath);
                            //cursor.close();

                            final TextView textView=new TextView(this);
                            textView.setText("Please wait...");
                            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            relativeLayout.addView(textView,layoutParams);

                            bitmapInputStream = getContentResolver().openInputStream(uri);

                            new GetImage(pictureInfo,borderView,relativeLayout,textView,this).start();
                        }

                    } else {
                       // Log.v("getImage", "You haven't picked image");
                    }
                }catch (Exception e){
                    //Log.v("getImage", e.getMessage());

                }
        }
    }
*/
//    private int caculateQuality(Bitmap bitmap) {
//
//        int bitmap_size=bitmap.getByteCount()/1000;
//        int quality=100;
//        Log.e("bitmap: getByteCount ",bitmap_size+"kb");
//
//        while (bitmap_size>100){
//            quality-=5;
//            bitmap_size*=0.5;
//        }
////        if(bitmap_size>25&&bitmap_size<125) {
////        }else if(bitmap_size>=125&&bitmap_size<1000){
////            quality=75;
////
////        }else if(bitmap_size>=3000&&bitmap_size<4500){
////            quality=50;
////
////        }else if(bitmap_size>=4500){
////            quality=20;
////        }
//        Log.e("bitmap: getByteCount ",bitmap_size+"kb");
//        return quality;
//    }



//    private Bitmap scaleToFill(Bitmap bitmap, int width, int height) {
//
//        int bitmap_width=bitmap.getWidth();
//        int bitmap_height=bitmap.getHeight();
//        float factorH=(float)height/bitmap_height;
//        float factorW=(float)width/bitmap_width;
//        float factorToUse=(factorH>factorW)?factorH:factorW;
//        return Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*factorToUse),
//                (int) (bitmap.getHeight()*factorToUse),true);
//    }
//    private Bitmap scaleToFillWindow(Bitmap bitmap) {
//
//        int bitmap_width=bitmap.getWidth();
//        int bitmap_height=bitmap.getHeight();
//        float factorToUse=1;
//        WindowManager windowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display display= null;
//        if (windowManager != null) {
//            display = windowManager.getDefaultDisplay();
//            android.graphics.Point window=new android.graphics.Point();
//            display.getSize(window);
//            float factorH=(float)window.y/bitmap_height;
//            float factorW=(float)window.x/bitmap_width;
//            factorToUse=(factorH>factorW)?factorH:factorW;
//        }
//
//        return Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*factorToUse),
//                (int) (bitmap.getHeight()*factorToUse),true);
//    }
//    public static int getDisplayDensity(Context context) {
//
//        int density = (int) context.getResources().getDisplayMetrics().density;
//        return density;
//    }
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
        //new Threshold(relativeLayout,textView,imageView,this,forDraw).start();

    }
/*

    public void getImage() {
        Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),RESULT_LOAD_IMAGE);
    }*/




  /*  private void showRemoveBackgroundPopup() {
        View removeView=getLayoutInflater().inflate(R.layout.remove_popup,null);
        BottomNavigationView removeNavi=removeView.findViewById(R.id.removeNavi);
        removeNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            }
        });
        PopupWindow popupWindow=new PopupWindow(removeView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        popupWindow.setBackgroundDrawable(getDrawable(R.color.colorPrimary));
        int location[]=new int[2];
        bottomNavigationView.getLocationOnScreen(location);
        popupWindow.showAtLocation(bottomNavigationView, Gravity.NO_GRAVITY,
                location[0],location[1]-bottomNavigationView.getHeight());

    }
*/
   /* private void showAutoMenu() {
        tabLayout.removeAllTabs();


    }

    private void showExtractMenu() {
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_border).setTag(BORDER));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_add).setTag(ADD));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_minus).setTag(MINUS));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_line_length).setTag(WATERSHED_LINE_LENGTH));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setClickable(true);
        //tabLayout.setFocusable(true);

        //tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_zoom).setTag(ZOOM));
    }*/

    /*private void showLinePopup() {
        View infoView=getLayoutInflater().inflate(R.layout.line_popup,null);
        PopupWindow popupWindow=new PopupWindow(infoView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        //final TextView sampleSize=infoView.findViewById(R.id.sampleSize);
        //final TextView quality=infoView.findViewById(R.id.quality);
        final TextView lineWidth=infoView.findViewById(R.id.size);

       // sampleSize.setText(String.valueOf(Threshold.SAMPLE_SIZE));
        //quality.setText(String.valueOf(Threshold.QUALITY));
        lineWidth.setText(String.valueOf(Threshold.PAINT_STROKE_WIDTH));

        //final SeekBar sizeSeekBar=infoView.findViewById(R.id.sizeSeekBar);
        //final SeekBar qualitySeekBar=infoView.findViewById(R.id.qualitySeekBar);
        final SeekBar lineWidthSeekbar=infoView.findViewById(R.id.contour_length);
        //sizeSeekBar.setMax(Threshold.MAX_SAMPLE_SIZE);
        //qualitySeekBar.setMax(Threshold.MAX_QUALITY);
        lineWidthSeekbar.setMax(Threshold.MAX_PAINT_STROKE_WIDTH);

        SeekBar.OnSeekBarChangeListener seekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int id=seekBar.getId();
                switch (id){
                 *//*   case R.id.sizeSeekBar:
                        Threshold.SAMPLE_SIZE= (int) Math.pow(Threshold.TWO,i);
                        sampleSize.setText("*1/"+String.valueOf(Threshold.SAMPLE_SIZE));
                        break;
                    case R.id.qualitySeekBar:
                        Threshold.QUALITY=i;
                        quality.setText(String.valueOf(Threshold.QUALITY));

                        break;*//*
                    case R.id.lineWidthSeekbar:
                        Threshold.PAINT_STROKE_WIDTH=i;
                        lineWidth.setText(String.valueOf(Threshold.PAINT_STROKE_WIDTH));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        //sizeSeekBar.setProgress(Threshold.SAMPLE_SIZE);
        //qualitySeekBar.setProgress(Threshold.QUALITY);
        lineWidthSeekbar.setProgress(Threshold.PAINT_STROKE_WIDTH);
        lineWidthSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);
        //dismiss when touched outside
        popupWindow.setBackgroundDrawable(getDrawable(R.color.colorPrimary));
        popupWindow.setOutsideTouchable(true);
        //int location[]=new int[2];
        //tabLayout.getLocationOnScreen(location);
        popupWindow.showAtLocation(pictureInfo, Gravity.NO_GRAVITY,
                0,pictureInfo.getHeight());

    }*/

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
        *//*    case R.id.manual:
                DrawingContourView.CURRENT_METHOD=DrawingContourView.MANUAL_METHOD;

                return true;
            case R.id.extract:
                DrawingContourView.CURRENT_METHOD=DrawingContourView.EXTRACT_METHOD;
                imageView.getImageBorder();
                imageView.invalidate();
                showExtractMenu();
                return true;
            case R.id.auto:
                showAutoMenu();
                return true;
            case R.id.zoom:
                DrawingContourView.CURRENT_METHOD=DrawingContourView.ZOOM_METHOD;
                imageView.invalidate();
                return true;*//*

           *//* case R.id.tune:
                return true;*//*

        }
        return false;

    }*/
}
