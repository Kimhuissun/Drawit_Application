package com.makeit.sunnycar.drawit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.data.Layer;
import com.makeit.sunnycar.drawit.listener.RecyclerViewListener;
import com.makeit.sunnycar.drawit.view.LayerView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by gmltj on 2018-05-03.
 */

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.MyViewHolder>
        implements RecyclerViewListener.ItemTouchHelperAdapter {
    private static final int MAX_LAYER_COUNT = 3;

    public void setOnStartDragListener(RecyclerViewListener.OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    private RecyclerViewListener.OnStartDragListener onStartDragListener;
    private Context context;
    public ArrayList<Frame> frames=new ArrayList<>();

    public LayerAdapter( Context context) {
        this.context = context;
        if(frames.size()==0) {

            frames.add(new Frame());
            Frame.setCurrentFrame(0,this);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_layer,parent,false);
        return new MyViewHolder(view);// null;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(position==Frame.currentFrame.CURRENT_LAYER_POSITION)
            holder.relativeLayout.setBackgroundColor(context.getResources()
                    .getColor(R.color.colorPrimary));
        else {
            holder.relativeLayout.setBackgroundColor(context.getResources()
                    .getColor(R.color.transparent));
        }

        holder.eyeBt.setImageDrawable(Frame.currentFrame.layers.get(position).eyeOn?
                context.getDrawable(R.mipmap.ic_eye_on):
                context.getDrawable(R.mipmap.ic_eye_off));

        holder.imageView.setLayerBitmap(position);
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                    if(onStartDragListener!=null)
                        onStartDragListener.onStartDrag(holder);

                return false;
            }
        });
        holder.opacitySeekBar.setProgress(Frame.currentFrame.layers.get(position).layerOpacity);



    }

    @Override
    public int getItemCount() {
        return Frame.currentFrame.layers.size();
    }

    public synchronized void addLayer() {

        if(Frame.currentFrame.layers.size()<LayerAdapter.MAX_LAYER_COUNT)
        {
            Frame.currentFrame.addLayer();
            notifyItemInserted(Frame.currentFrame.layers.size()-1);

        }


    }

    public synchronized void removeLayer() {
        int removedPosition = Frame.currentFrame.CURRENT_LAYER_POSITION;

        if(Frame.currentFrame.removeLayer(removedPosition)){
            notifyItemRemoved(removedPosition);
            notifyItemRangeChanged(removedPosition, Frame.currentFrame.layers.size()-1);


        }



    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        //Log.v("","Log position"+fromPosition+" "+toPosition);
        if(fromPosition<Frame.currentFrame.layers.size()
                &&toPosition<Frame.currentFrame.layers.size()){
            if(fromPosition<toPosition){
                for(int i=fromPosition;i<toPosition;i++){
                    Collections.swap(Frame.currentFrame.layers,i,i+1);
                    for(int j=0;j<Frame.currentFrame.LAYER_HISTORY.size();j++){
                        if(Frame.currentFrame.LAYER_HISTORY.get(j)==i)
                            Frame.currentFrame.LAYER_HISTORY.set(j,i+1);
                        else if(Frame.currentFrame.LAYER_HISTORY.get(j)==i+1)
                            Frame.currentFrame.LAYER_HISTORY.set(j,i);

                    }
                }

            }else {
                for(int i=fromPosition;i>toPosition;i--){
                    Collections.swap(Frame.currentFrame.layers,i,i-1);
                    for(int j=0;j<Frame.currentFrame.LAYER_HISTORY.size();j++){
                        if(Frame.currentFrame.LAYER_HISTORY.get(j)==i)
                            Frame.currentFrame.LAYER_HISTORY.set(j,i-1);
                        else if(Frame.currentFrame.LAYER_HISTORY.get(j)==i-1)
                            Frame.currentFrame.LAYER_HISTORY.set(j,i);

                    }
                }
            }
            if(Frame.currentFrame.CURRENT_LAYER_POSITION==fromPosition)
                Frame.currentFrame.setCurrentLayer(toPosition);
            else if(Frame.currentFrame.CURRENT_LAYER_POSITION==toPosition)
                Frame.currentFrame.setCurrentLayer(fromPosition);

            notifyItemMoved(fromPosition,toPosition);
        }

        return true;
    }

    @Override
    public void onItemDismiss(int position) {

        Frame.currentFrame.layers.remove(position);
        notifyItemRemoved(position);
    }



public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            RecyclerViewListener.ItemTouchHelperViewHolder, SeekBar.OnSeekBarChangeListener {
        private ImageButton eyeBt;
        private RelativeLayout relativeLayout;
        private LayerView imageView;
        private SeekBar opacitySeekBar;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=(LayerView)itemView.findViewById(R.id.imageView);
            eyeBt=(ImageButton)itemView.findViewById(R.id.eye);
            eyeBt.setOnClickListener(this);
            relativeLayout=itemView.findViewById(R.id.relativeLayout);
            relativeLayout.setOnClickListener(this);
            opacitySeekBar=itemView.findViewById(R.id.opacity);
            opacitySeekBar.setOnSeekBarChangeListener(this);
            opacitySeekBar.setProgress(Layer.OPACITY_MAX);


        }

        @Override
        public void onClick(View view) {
            int id=view.getId();
            if(id==R.id.relativeLayout){
                int oldPosition=Frame.currentFrame.CURRENT_LAYER_POSITION;
                Frame.currentFrame.setCurrentLayer(getAdapterPosition());

                notifyItemChanged(oldPosition);
                view.setBackgroundColor(context.getResources()
                        .getColor(R.color.colorPrimary));


            }else if(id==R.id.eye){
                if(frames.get(Frame.CURRENT_FRAME_POSITION).layers.get(getAdapterPosition()).eyeOn){
                    eyeBt.setImageDrawable(context.getDrawable(R.mipmap.ic_eye_off));
                    frames.get(Frame.CURRENT_FRAME_POSITION).layers.get(getAdapterPosition()).eyeOn=false;
                }else {
                    eyeBt.setImageDrawable(context.getDrawable(R.mipmap.ic_eye_on));
                    frames.get(Frame.CURRENT_FRAME_POSITION).layers.get(getAdapterPosition()).eyeOn=true;
                }

            }
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onitemClear() {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if(fromUser) {
                Frame.currentFrame.resetLayerOpacity(getAdapterPosition(),progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


}
