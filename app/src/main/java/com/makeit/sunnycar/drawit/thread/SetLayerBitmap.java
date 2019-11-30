package com.makeit.sunnycar.drawit.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.makeit.sunnycar.drawit.callback.EditItemTouchHelperCallback;
import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.listener.RecyclerViewListener;
import com.makeit.sunnycar.drawit.view.DrawingContourView;

/**
 * Created by gmltj on 2018-05-23.
 */

public class SetLayerBitmap extends Thread {
    private final FrameLayout frameLayout;
    private final View layerView;
    private Context context;
    private RecyclerView recyclerView;
    public LayerAdapter layerAdapter;
    private ItemTouchHelper itemTouchHelper;

    public SetLayerBitmap(Context context, RecyclerView recyclerView, LayerAdapter layerAdapter, FrameLayout frameLayout, View layerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.layerAdapter=layerAdapter;
        this.frameLayout=frameLayout;
        this.layerView=layerView;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();

        Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
              /*  synchronized (Frame.currentFrame.layers){
                    Iterator<Layer> iterator=Frame.currentFrame.layers.iterator();
                    while(iterator.hasNext()) {
                        Layer layer = iterator.next();
                        synchronized (layer){
                            layer.pathCanvas.save();
                            layer.pathCanvas.scale(layer.scaleFactor,layer.scaleFactor,layer.scalePointX,layer.scalePointY);
                            layer.pathCanvas.translate(layer.mPosX,layer.mPosY);
                            layer.pathCanvas.rotate(layer.mAngle,layer.scalePointX,layer.scalePointY);
                            layer.pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                            DrawingContourView.mBitmapPaint.setAlpha(layer.layerOpacity);
                            if(layer.isSketch&&Threshold.FINAL_IMAGE!=null){
                                layer.pathCanvas.drawBitmap(Threshold.FINAL_IMAGE,0,0,DrawingContourView.mBitmapPaint);

                            }

                            synchronized (layer.pen){
                                Iterator<Path> pathIterator=layer.pen.pathArrayList.iterator();
                                Iterator<Paint> paintIterator=layer.pen.paintArrayList.iterator();
                                while (pathIterator.hasNext()&&paintIterator.hasNext()){
                                    layer.pathCanvas.drawPath(pathIterator.next(), paintIterator.next());

                                }

                            }
                            layer.pathCanvas.restore();


                        }
                    }
                }*/
               /* for(int i=0;i< Frame.currentFrame.layers.size();i++){
                    Layer layer=Frame.currentFrame.layers.get(i);
                        //if(layer.pathBitmap==null){
                            //layer.pathBitmap =
                                  //  Bitmap.createBitmap(DrawingContourView.WIDTH, DrawingContourView.HEIGHT, Bitmap.Config.ARGB_4444);
                            //layer.pathCanvas=new Canvas(layer.pathBitmap);
                    layer.pathCanvas.scale(layer.scaleFactor,layer.scaleFactor,layer.scalePointX,layer.scalePointY);
                    layer.pathCanvas.translate(layer.mPosX,layer.mPosY);
                    layer.pathCanvas.rotate(layer.mAngle,layer.scalePointX,layer.scalePointY);

                    layer.pathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT);
                    DrawingContourView.mBitmapPaint.setAlpha(layer.layerOpacity);
                    if(layer.isSketch&&Threshold.FINAL_IMAGE!=null){
                        layer.pathCanvas.drawBitmap(Threshold.FINAL_IMAGE,0,0,DrawingContourView.mBitmapPaint);

                    }
                    for (int j = 0; j < layer.pen.pathArrayList.size(); j++) {
                        Path p = layer.pen.pathArrayList.get(j);
                        Paint paint = layer.pen.paintArrayList.get(j);
                        //scaled된 canvas translate 와 원래 그 크기의 canvas translate
                        layer.pathCanvas.drawPath(p, paint);
                    }
                        //}

                }
*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Handler mainHandler=new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {


                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                //layerAdapter=new LayerAdapter(context,drawingContourView);
                                ItemTouchHelper.Callback callback=new EditItemTouchHelperCallback(layerAdapter);
                                itemTouchHelper=new ItemTouchHelper(callback);
                                itemTouchHelper.attachToRecyclerView(recyclerView);
                                layerAdapter.setOnStartDragListener(new RecyclerViewListener.OnStartDragListener() {
                                    @Override
                                    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                                        itemTouchHelper.startDrag(viewHolder);

                                    }
                                });

                                recyclerView.setAdapter(layerAdapter);
                                /*FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutParams.gravity= Gravity.END;
                                frameLayout.addView(layerView,layoutParams);*/
                                PopupWindow popupWindow=new PopupWindow(context);
                                popupWindow.setHeight(frameLayout.getHeight());
                                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

                                popupWindow.setContentView(layerView);
                                popupWindow.setFocusable(true);

                                popupWindow.setBackgroundDrawable(context.getDrawable(R.color.transparent));
                                popupWindow.setOutsideTouchable(true);
                                popupWindow.showAtLocation(frameLayout, Gravity.NO_GRAVITY,DrawingContourView.WINDOW_WIDTH-layerView.getWidth(),0);


                            }
                        });
                    }
                }).start();
                Thread.currentThread().interrupt();
            }
        });
        Looper.loop();

    }
}
