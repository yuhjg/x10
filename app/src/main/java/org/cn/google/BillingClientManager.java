package org.cn.google;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillingClientManager {

    private static final String TAG = BillingClientManager.class.getName();
    private static BillingClient billingClient;
    private static boolean mIsServiceConnected = false;
    private static BillingClientStateListener stateListener;


    public static void init(Activity activity, PurchasesUpdatedListener purchasesUpdatedListener, BillingClientStateListener billingClientStateListener) {
        billingClient = BillingClient.newBuilder(activity).setListener(purchasesUpdatedListener).enablePendingPurchases().build();
        stateListener = billingClientStateListener;
        connectionService();
    }
    public static void connectionService() {
        BillingClient billingClient2 = billingClient;
        if (billingClient2 == null || stateListener == null) {
            throw new IllegalArgumentException("Please call init(); first!");
        }
        billingClient2.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == 0) {
                    boolean unused = BillingClientManager.mIsServiceConnected = true;
                    BillingClientManager.stateListener.onBillingSetupFinished(billingResult);
                    return;
                }
                BillingClientManager.stateListener.onBillingServiceDisconnected();
            }

            @Override
            public void onBillingServiceDisconnected() {
                boolean unused = BillingClientManager.mIsServiceConnected = false;
                BillingClientManager.stateListener.onBillingServiceDisconnected();
            }
        });
    }

    public static void querySkuDetailsAsync(String str, SkuDetailsResponseListener skuDetailsResponseListener, String... strArr) {
        if (billingClient != null) {
            if (!mIsServiceConnected) {
                connectionService();
            }
            billingClient.querySkuDetailsAsync(SkuDetailsParams.newBuilder().setType(str).setSkusList(Arrays.asList(strArr)).build(), skuDetailsResponseListener);
            return;
        }
        throw new IllegalArgumentException("querySkuDetailsAsync(); error . Please call init(); first!");
    }

    public static BillingResult launchBillingFlow(Activity activity, SkuDetails skuDetails) {

        if (activity == null || billingClient == null) {
            throw new IllegalArgumentException("launchBillingFlow(); error . Please call init(); first!");
        }
        if (!mIsServiceConnected) {
            connectionService();
        }
        return billingClient.launchBillingFlow(activity, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
    }

    public static boolean isFeatureSupported(String str) {
        if (billingClient != null) {
            if (!mIsServiceConnected) {
                connectionService();
            }
            BillingResult isFeatureSupported = billingClient.isFeatureSupported(str);
            if (isFeatureSupported.getResponseCode() == 0) {
                return true;
            }
            Log.e(TAG, "isFeatureSupported: isFeatureSupported = false , errorMsg : " + isFeatureSupported.getDebugMessage());
            return false;
        }
        throw new IllegalArgumentException("isFeatureSupported(); error . Please call init(); first!");
    }

    public static void consumeAsync(Purchase purchase, ConsumeResponseListener consumeResponseListener) {
        if (billingClient != null) {
            if (!mIsServiceConnected) {
                connectionService();
            }
            if (purchase.getPurchaseState() != 1 || purchase.isAcknowledged()) {
                consumeResponseListener.onConsumeResponse(BillingResult.newBuilder().setResponseCode(0).build(), purchase.getPurchaseToken());
                return;
            }
            billingClient.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build(), consumeResponseListener);
            return;
        }
        throw new IllegalArgumentException("consumeAsync(); error . Please call init(); first!");
    }

    public static void acknowledgePurchase(Purchase purchase, AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener) {
        if (billingClient != null) {
            if (!mIsServiceConnected) {
                connectionService();
            }
            if (purchase.getPurchaseState() == 1 && !purchase.isAcknowledged()) {
                billingClient.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build(), acknowledgePurchaseResponseListener);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("acknowledgePurchase(); error . Please call init(); first!");
    }

    public static List<Purchase> queryPurchases(String str) {
        BillingClient billingClient2 = billingClient;
        if (billingClient2 != null) {
            Purchase.PurchasesResult queryPurchases = billingClient2.queryPurchases(str);
            if (queryPurchases.getResponseCode() == 0) {
                return queryPurchases.getPurchasesList();
            }
            return new ArrayList();
        }
        throw new IllegalArgumentException("acknowledgePurchase(); error . Please call init(); first!");
    }

    public static void queryPurchaseHistoryAsync(String str, PurchaseHistoryResponseListener purchaseHistoryResponseListener) {
        if (billingClient != null) {
            new ArrayList();
            billingClient.queryPurchaseHistoryAsync(str, purchaseHistoryResponseListener);
            return;
        }
        throw new IllegalArgumentException("acknowledgePurchase(); error . Please call init(); first!");
    }


}
