package com.makeit.sunnycar.drawit.ui;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.view.TuneDialog;

import java.io.InputStream;

/**
 * Created by gmltj on 2018-05-03.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String[] PERMISSIONS={"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int PERMISSION_REQUES_CODE=1;
    public static final int RESULT_LOAD_IMAGE = 0;
    public static final int CROP_THE_IMAGE = 2;

    public static final String GET_PHOTO = "get_photo";
    public static InputStream bitmapInputStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();



    }



    private void setView() {
        ImageButton brush=(ImageButton)findViewById(R.id.brush);
        //ImageButton picture=(ImageButton)findViewById(R.id.picture);
        brush.setOnClickListener(this);
        //picture.setOnClickListener(this);

    }
    private void showPhotoDialog() {
        TuneDialog tuneDialog=new TuneDialog();

        android.app.FragmentManager fm=getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.add(tuneDialog,"tuneDialog");
        Bundle args=new Bundle();
        args.putBoolean(TuneDialog.NEW_CANVAS,true);
        tuneDialog.setArguments(args);
        tuneDialog.setOnSaveTuneListener(new TuneDialog.OnSaveTuneListener() {
            @Override
            public void onSave() {
                Intent intent=new Intent(getApplicationContext(),FillSketch.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.putExtra(GET_PHOTO,false);
                startActivity(intent);
            }
        });
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View v) {

        int id=v.getId();

        if(id==R.id.brush){
            showPhotoDialog();

        }
       /* else if(id==R.id.picture){
            //startActivity(new Intent(getApplicationContext(),FillSketch.class));
            if(!hasPermissions(PERMISSIONS)){//퍼미션 허가했는지 여부
                requestNessaryPermissions(PERMISSIONS);//허가 안되었으면 사용자에게 요청
            }else {
                //이미 사용자에게 퍼미션 허가 받음
                //Log.e("oncreate","permission");
                distpatchPhotoSelectionIntent();

            }
        }*/
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

    }
    private void requestNessaryPermissions(String[] permissions){
        ActivityCompat.requestPermissions(this,permissions,PERMISSION_REQUES_CODE);
    }
    private void distpatchPhotoSelectionIntent() {
        Intent intent=new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_REQUES_CODE:{
                //퍼미션을 거절했을 때 메시지 출력 후 종료
                if(!hasPermissions(PERMISSIONS)){
                    Toast.makeText(getApplicationContext(),"CAMERA PERMISSION FAIL",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    distpatchPhotoSelectionIntent();
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_LOAD_IMAGE:
                try {
                    if (resultCode == RESULT_OK && data != null) {
                        final Uri uri=data.getData();
                        //String[] filePathColumn={MediaStore.Images.Media.DATA};
                        if (uri != null) {

                            //bitmapInputStream = getContentResolver().openInputStream(uri);
                            //new GetImage(getApplicationContext()).start();
                            startActivity(
                                    new Intent(this, CropActivity.class)
                                            .setData(uri)
                            );
                        }

                    } else {
                        // Log.v("getImage", "You haven't picked image");
                    }
                }catch (Exception e){
                    //Log.v("getImage", e.getMessage());

                }
        }
    }

}
