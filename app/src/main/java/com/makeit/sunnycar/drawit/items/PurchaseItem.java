package com.makeit.sunnycar.drawit.items;

import android.content.Context;
import android.content.SharedPreferences;

import com.makeit.sunnycar.drawit.R;

import java.util.ArrayList;

/**
 * Created by gmltj on 2018-05-21.
 */

public class PurchaseItem {
    private static final String GET_MAX_SAVE_COUNT = "max_save_count";
    private static final String GET_LAYER_SUPPORTED = "supported";
    public static ArrayList<String> ids;
    public static final String GET_CURRENT_SAVE_COUNT = "current_save_count";
    public static final String DIGITALISED_LINES = "DIGITALISED LINES";
    private static final int ORG_MAX_SAVE_COUNT = 3;
    private static final int UPGRADE_SAVE_COUNT=5;

    public int MAX_SAVE_COUNT;
    public int CURRENT_SAVE_COUNT;
    public boolean LAYER_6_SUPPORTED=false;
    private Context context;
    private SharedPreferences sharedPreferences;

    public static PurchaseItem purchaseItem;

    public PurchaseItem(Context context) {

        this.context=context;
       sharedPreferences=context.getSharedPreferences(DIGITALISED_LINES,0);
       CURRENT_SAVE_COUNT=sharedPreferences.getInt(GET_CURRENT_SAVE_COUNT,0);
       MAX_SAVE_COUNT=sharedPreferences.getInt(GET_MAX_SAVE_COUNT,ORG_MAX_SAVE_COUNT);
       LAYER_6_SUPPORTED=sharedPreferences.getBoolean(GET_LAYER_SUPPORTED,false);

    }
    public PurchaseItem getInstance(Context context){
        if(purchaseItem==null)
            purchaseItem=new PurchaseItem(context);
        return purchaseItem;
    }

    public ArrayList<String> getIds(){
        ids=new ArrayList<>();
        ids.add(context.getResources().getString(R.string.layerPremium));
        ids.add(context.getResources().getString(R.string.saveCountPremium));
        ids.add(context.getResources().getString(R.string.noLimitPremium));
        return ids;
    }
    public void upgradeMaxSaveCount(){
        MAX_SAVE_COUNT+=UPGRADE_SAVE_COUNT;

    }

}
