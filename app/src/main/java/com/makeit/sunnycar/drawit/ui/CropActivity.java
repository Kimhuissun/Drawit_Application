package com.makeit.sunnycar.drawit.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.thread.GetImage;

public class CropActivity extends AppCompatActivity {
    private CropImageView cropImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_content);
        cropImageView=(CropImageView) findViewById(R.id.cropImageView);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri=getIntent().getData();
        cropImageView.load(uri).execute(new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void setCropMode(View v){
        int id=v.getId();
        switch (id){
            case R.id.fit_image:{
                cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                break;
            }
            case R.id.ratio_4_3:{
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                break;
            }
            case R.id.ratio_3_4:{
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);

                break;
            }
            case R.id.square:{
                cropImageView.setCropMode(CropImageView.CropMode.SQUARE);

                break;
            }
            case R.id.ratio_16_9:{
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);

                break;
            }
            case R.id.ratio_9_16:{
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);

                break;
            }
            case R.id.free:{
                cropImageView.setCropMode(CropImageView.CropMode.FREE);

                break;
            }
            case R.id.custom:{
                cropImageView.setCropMode(CropImageView.CropMode.CUSTOM);

                break;
            }
            case R.id.save:{
                GetImage.imageViewBitmap=cropImageView.getCroppedBitmap();
                new GetImage(getApplicationContext()).start();
                break;
            }

        }
    }
}
