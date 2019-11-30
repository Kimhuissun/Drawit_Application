package com.makeit.sunnycar.drawit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.makeit.sunnycar.drawit.R;

/**
 * Created by gmltj on 2018-05-18.
 */

public class LineView extends View {

    private static int WIDTH,HEIGHT;
    private static final int PADDING=50;
    private static Path path;
    private Paint paint=new Paint();
    private static Bitmap background;
    private static Paint bitmapPaint;
    private static Paint borderPaint;
    private static Rect whereToDraw;
    private static RectF frameToDraw;
    public LineView(Context context) {
        super(context);
        init();
    }

    private void init() {
        bitmapPaint=new Paint(Paint.DITHER_FLAG);
        borderPaint=new Paint();
        borderPaint.setColor(getResources().getColor(R.color.gray));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        WIDTH=right-left;
        HEIGHT=bottom-top;
        if(path==null){
            path=new Path();
            path.reset();
            path.moveTo(PADDING,HEIGHT/2);
            path.quadTo(WIDTH/4,HEIGHT/3,WIDTH/2,HEIGHT/2);
            path.quadTo(3*WIDTH/4,2*HEIGHT/3,WIDTH-PADDING,HEIGHT/2);
            path.lineTo(WIDTH-PADDING,HEIGHT/2);
        }
        if(background==null){
            background= BitmapFactory.decodeResource(getResources(), R.drawable.transparency);
            //background=Bitmap.createScaledBitmap(background,WIDTH,background.get*(WIDTH/background.getWidth()),false);
        }
        if(whereToDraw==null) {
            whereToDraw = new Rect(0, 0, WIDTH, HEIGHT);
            frameToDraw = new RectF(0, 0, WIDTH, HEIGHT);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.reset();
        DrawingContourView.getPaint(paint, false);

        if(background!=null)
            canvas.drawBitmap(background,whereToDraw,frameToDraw,bitmapPaint);
        canvas.drawRect(0,0,WIDTH,HEIGHT,borderPaint);
        canvas.drawPath(path,paint);
    }
}
