/*
package com.makeit.sunnycar.drawit.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.makeit.sunnycar.removevideobackground.R;
import com.makeit.sunnycar.drawit.thread.GetImage;

public class CropView extends LinearLayout {
    private ImageView imageView;

    public CropView(Context context) {
        super(context);
        init(context);
    }


    public CropView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CropView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {

        setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        setOrientation(VERTICAL);
        final CropView thisView=this;
        final ImageButton saveBt=new ImageButton(context);
        final BorderView borderView=new BorderView(context);

        saveBt.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        saveBt.setImageDrawable(context.getDrawable(R.mipmap.ic_next));
        saveBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                borderView.cropBorder();
                imageView.setImageBitmap(GetImage.imageViewBitmap);
                thisView.removeAllViews();
                thisView.setVisibility(GONE);
                //removeView(thisView);

            }
        });
        LayoutParams layoutParams=new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity= Gravity.END;
        addView(saveBt,layoutParams);

        LayoutParams layoutParam=new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        addView(borderView,layoutParam);

    }

    public void setImageView(ImageView imageView) {
        this.imageView=imageView;
    }
}
*/
