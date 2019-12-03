package com.makeit.sunnycar.drawit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.view.ExampleView;

/**
 * Created by gmltj on 2018-05-23.
 */

public class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.MyViewHolder> {

    private LayerAdapter layerAdapter;
    private Context context;
    //private DrawingContourView drawingContourView;
    private SeekBar opacityBar;
    public PlayAdapter(Context context,LayerAdapter layerAdapter) {
        this.context=context;
        this.layerAdapter = layerAdapter;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.play_content,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(position== Frame.CURRENT_FRAME_POSITION)
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
        else
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.imageView.setBitmap(position,layerAdapter);

    }

    @Override
    public int getItemCount() {
        return layerAdapter.frames.size();
    }

    public void setOpacityBar(SeekBar opacityBar) {
        this.opacityBar = opacityBar;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ExampleView imageView;
        private RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(ExampleView)itemView.findViewById(R.id.playImageView);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id=view.getId();
            if (id==R.id.relativeLayout){
                int oldPosition=Frame.CURRENT_FRAME_POSITION;
                Frame.setCurrentFrame(getAdapterPosition(),layerAdapter);
                notifyItemChanged(oldPosition);
                (view).setBackgroundColor(context.getResources()
                        .getColor(R.color.red));


                Frame.currentFrame.drawNotCurrentFramesBitmap();


            }
        }


    }
    public void removeFrame() {

        synchronized (layerAdapter.frames){
            if(layerAdapter.frames.size()>1){
                int removedPosition=Frame.CURRENT_FRAME_POSITION;
                Frame.setCurrentFrame(0,layerAdapter);
                layerAdapter.frames.remove(removedPosition);

                notifyItemRemoved(removedPosition);
                notifyItemRangeChanged(removedPosition,layerAdapter.frames.size()-1);
            }
        }
    }
    public void addFrame() {
        synchronized (layerAdapter.frames){
            layerAdapter.frames.add(new Frame());
            notifyItemInserted(layerAdapter.frames.size()-1);
        }


    }
}
