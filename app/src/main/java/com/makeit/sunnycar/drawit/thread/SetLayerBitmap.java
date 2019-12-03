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

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Handler mainHandler=new Handler(Looper.getMainLooper());
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {


                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                recyclerView.setLayoutManager(linearLayoutManager);
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
