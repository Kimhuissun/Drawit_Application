/*
package com.makeit.sunnycar.drawit.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeit.sunnycar.removevideobackground.R;
import com.makeit.sunnycar.drawit.data.Frame;
import com.makeit.sunnycar.drawit.ui.FillSketch;
import com.makeit.sunnycar.drawit.ui.MainActivity;

public class ResizeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.content_resize,container,false);
        Frame.currentFrame.addSketch();
        Intent intent=new Intent(context, FillSketch.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(MainActivity.GET_PHOTO,true);
        context.startActivity(intent);
        return rootView;//super.onCreateView(inflater, container, savedInstanceState);

    }
}
*/
