package com.makeit.sunnycar.drawit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.makeit.sunnycar.drawit.R;
import com.makeit.sunnycar.drawit.items.PurchaseItem;

import java.util.ArrayList;

/**
 * Created by gmltj on 2018-05-21.
 */

public class PremiumActivity extends AppCompatActivity implements View.OnClickListener,
        BillingProcessor.IBillingHandler
        //,PurchasesUpdatedListener
{
    private static final int MENU_NUM = 3;
    public static ArrayList<SkuDetails> products=new ArrayList<>();
    private String PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqaZQeQA5bnXSxJ5xMz+k6PA4NXt8XogvKJe7rGubYhu2RcuZ+nlCi5aLaGqCbFXg2jTduCwaid4sgHehOj6hqLLkN2F3A+m3bVcDM3092HVI7U0QjYCcXwNfx8Rb5VvdgPrwCXwmRTMJmrXa13lWTiMVp68NMbF5JI9nyLr8gphFOO/zuMODZTQ0ismsbSzMrpfj7LNz56CvOj+VFqB9iogdnMNGDZQeZBjOTWmRVq3qRDNjhczNt5Or1z041j3wSChhlzBB/vkBOm+Vsd2GLT0cnnAq/4UaXyZSdZZ9Cda7xMKrlpSateFlPGthcYZAxItHExIF4jLFxuRqCMiGXwIDAQAB";
  //  private BillingClient billingClient;
    private BillingProcessor billingProcessor;
    private Button[] purchaseButtons=new Button[MENU_NUM];
    private TextView[] purchaseTexts=new TextView[MENU_NUM];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
    }

    private void setView() {
        setContentView(R.layout.premium_activity);
        purchaseButtons[0]=findViewById(R.id.layerButton);
        purchaseButtons[1]=findViewById(R.id.saveButton);
        purchaseButtons[2]=findViewById(R.id.noLimitButton);
        for(int i=0;i<MENU_NUM;i++){
            purchaseButtons[i].setEnabled(false);
        }
       // connectToGooglePlay();
        billing();
    }

    private void billing() {
        billingProcessor=new BillingProcessor(this,PUBLIC_KEY,this);
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.layerButton:
                billingProcessor.purchase(this,products.get(0).productId);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!billingProcessor.handleActivityResult(requestCode,resultCode,data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
 /*
    * Called when requested PRODUCT ID was successfully purchased
    */
        SkuDetails sku=billingProcessor.getPurchaseListingDetails(productId);

        if(productId.equals(getString(R.string.layerPremium))){

        }else if(productId.equals(getString(R.string.saveCountPremium))){
            //setSharedPreferences();
        }else if(productId.equals(getString(R.string.noLimitPremium))){

        }
        String message=sku.title+" "+getString(R.string.purchase_succeed);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onPurchaseHistoryRestored() {
 /*
    * Called when purchase history was restored and the list of all owned PRODUCT ID's
    * was loaded from Google Play
    */

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
 /*
    * Called when some error occurred. See Constants class for more details
    *
    * Note - this includes handling the case where the user canceled the buy dialog:
    * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
    */

    }

    @Override
    public void onBillingInitialized() {

        products= (ArrayList<SkuDetails>) billingProcessor.getPurchaseListingDetails(new PurchaseItem(this).getIds());

        for(int i=0;i<MENU_NUM;i++){
            purchaseButtons[i].setEnabled(true);
            purchaseButtons[i].setOnClickListener(this);

            purchaseTexts[i].setText(products.get(i).title);
            purchaseButtons[i].setText(products.get(i).priceText);
        }

    }
    
}
