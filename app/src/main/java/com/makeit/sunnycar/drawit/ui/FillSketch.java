package com.makeit.sunnycar.drawit.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.adapter.PlayAdapter;
import com.makeit.sunnycar.drawit.thread.GetImage;
import com.makeit.sunnycar.drawit.view.DrawingContourView;
import com.makeit.sunnycar.drawit.view.FillBottomMenu;
import com.makeit.sunnycar.drawit.view.FillUpperMenu;

/**
 * Created by gmltj on 2018-05-01.
 */

public class FillSketch extends AppCompatActivity {
    private DrawingContourView drawingContourView;
    private FillBottomMenu fillBottomMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);

        //RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout);

        initializeAd();
        loadAd();
        RelativeLayout parentLayout=(RelativeLayout)findViewById(R.id.parent_layout);
        RelativeLayout menuLayout=(RelativeLayout)findViewById(R.id.menuLayout);
        FrameLayout frameLayout=(FrameLayout)findViewById(R.id.frameLayout);
        drawingContourView=(DrawingContourView)findViewById(R.id.image);
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        drawingContourView.setProgressBar(frameLayout,progressBar);
        //drawingContourView.setCanvasDefaultLength();

        //TextView fpsTextView=(TextView)findViewById(R.id.fps);
        //CheckBox expensiveBox=(CheckBox)findViewById(R.id.expensive);

        LayerAdapter layerAdapter = new LayerAdapter(this);
        PlayAdapter playAdapter=new PlayAdapter(this,layerAdapter);



        FillUpperMenu fillUpperMenu=(FillUpperMenu)findViewById(R.id.fillUpperMenu);
        fillBottomMenu = (FillBottomMenu) findViewById(R.id.fillBottomMenu);
        fillUpperMenu.setFillBottomMenu(fillBottomMenu);
        fillUpperMenu.setRelativeLayout(menuLayout);
        fillUpperMenu.setPlayAdapter(playAdapter);
        fillUpperMenu.setLayerAdapter(layerAdapter);
        fillUpperMenu.setFrameLayout(frameLayout);
        fillBottomMenu.setFillUpperMenu(fillUpperMenu);
        fillUpperMenu.setDrawingContourView(drawingContourView);
        fillBottomMenu.setLayerAdapter(layerAdapter);
        fillBottomMenu.setDrawingContourView(drawingContourView);
        fillBottomMenu.setLayout(parentLayout,frameLayout);

        drawingContourView.setFillBottomMenu(fillBottomMenu);
        drawingContourView.setFillUpperMenu(fillUpperMenu);
        drawingContourView.setLayerAdapter(layerAdapter);
        //drawingContourView.setFpsText(fpsTextView);
        //drawingContourView.setExpensiveBox(expensiveBox);
    }



    private void initializeAd() {
        MobileAds.initialize(this, String.valueOf(R.string.ad_app_id));
    }
    private void loadAd() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //fillBottomMenu.addSketch(getIntent().getBooleanExtra(MainActivity.GET_PHOTO,false));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case MainActivity.RESULT_LOAD_IMAGE: {
                try {
                    if (resultCode == RESULT_OK && data != null) {
                        final Uri uri = data.getData();
                        //String[] filePathColumn={MediaStore.Images.Media.DATA};
                        if (uri != null) {
                            startActivity(
                                    new Intent(this, CropActivity.class)
                                            .setData(uri)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                            );
                            //MainActivity.bitmapInputStream = getContentResolver().openInputStream(uri);
                            //new GetImage(getApplicationContext()).start();

                            /*

                            Intent intent = new Intent("com.android.camera.action.CROP");
                            intent.setDataAndType(uri, "image/*");
                            intent.putExtra("outputX", 200);
                            intent.putExtra("outputY", 200);
                            intent.putExtra("aspectX", 2);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", true);
                            if (intent.resolveActivity(getPackageManager()) != null)
                                startActivityForResult(intent, MainActivity.CROP_THE_IMAGE);*/
                        }

                    } else {
                        // Log.v("getImage", "You haven't picked image");
                    }
                } catch (Exception e) {
                    //Log.v("getImage", e.getMessage());

                }
                break;
            }
           /* case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:{
                CropImage.ActivityResult result=CropImage.getActivityResult(data);
                if(resultCode==RESULT_OK){
                    Uri resultUri=result.getUri();
                    try {
                        MainActivity.bitmapInputStream = getContentResolver().openInputStream(resultUri);
                        new GetImage(getApplicationContext()).start();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error=result.getError();
                }
                break;
            }*/
            case MainActivity.CROP_THE_IMAGE:{
                if (resultCode == RESULT_OK){
                    final Bundle extras=data.getExtras();
                    if(extras!=null){
                        GetImage.imageViewBitmap=extras.getParcelable("data");
                        new GetImage(getApplicationContext()).start();


                    }
                }
                break;
            }
        }

    }
/*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.fill_upper_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      *//*  if(item.getItemId()==R.id.save){
            saveImage();

        }*//*
        return super.onOptionsItemSelected(item);//true;//
    }*/

 /*   public void saveImage(){

        if(hasPermissions(FindContours.PERMISSIONS)){
            new SaveImage(this,drawingContourView).start();
        }

    }

    private boolean hasPermissions(String[] permissions){
        int result=-1;
        for(int i=0;i<permissions.length;i++){
            result= ContextCompat.checkSelfPermission(getApplicationContext(),permissions[i]);

        }
        if(result== PackageManager.PERMISSION_GRANTED){//0
            return true;
        }else{
            return false;
        }

    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startCheckDialog();
    }

    private void startCheckDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                //.setCancelable(false)
                .setPositiveButton("stay here", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("go out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawingContourView.removeAllFrames();
                        drawingContourView.reset();
                        finish();
                        //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                })
        .setMessage("if you go back, your drawing will be lost.");
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
