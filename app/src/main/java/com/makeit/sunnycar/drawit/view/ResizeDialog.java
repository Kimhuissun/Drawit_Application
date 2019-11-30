package com.makeit.sunnycar.drawit.view;

import android.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ResizeDialog extends DialogFragment implements View.OnClickListener {
    private OnSaveTuneListener onSaveTuneListener;

    @Override
    public void onClick(View v) {

    }

    public void setOnSaveTuneListener(OnSaveTuneListener onSaveTuneListener) {
        this.onSaveTuneListener = onSaveTuneListener;
    }

    public interface OnSaveTuneListener {
        void onSave();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params=getDialog().getWindow().getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.MATCH_PARENT;

        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

    }

}
