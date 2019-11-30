package com.makeit.sunnycar.drawit.asynctask;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.makeit.sunnycar.drawit.adapter.LayerAdapter;
import com.makeit.sunnycar.drawit.adapter.PlayAdapter;
import com.makeit.sunnycar.drawit.data.Frame;

import java.util.ListIterator;

/**
 * Created by gmltj on 2018-05-23.
 */

public class SetPlayBitmap extends AsyncTask<Void,Boolean,Boolean> {

    private final LayerAdapter layerAdapter;
    private Context context;

    private RecyclerView recyclerView;
    public PlayAdapter playAdapter;
    private Paint layerPaint;

    public SetPlayBitmap(Context context, RecyclerView recyclerView,PlayAdapter playAdapter,LayerAdapter layerAdapter) {

        this.context=context;

        this.recyclerView=recyclerView;
        this.playAdapter=playAdapter;
        this.layerAdapter=layerAdapter;
        layerPaint=new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        synchronized (layerAdapter.frames) {
            ListIterator<Frame> frameReverseIterator = layerAdapter.frames.listIterator(layerAdapter.frames.size());
            while (frameReverseIterator.hasPrevious()) {
                Frame frame = frameReverseIterator.previous();
                synchronized (frame) {
                    frame.drawBitmap();

                }
            }


        }

        return true;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(playAdapter);

    }
}
