package org.cn.google.hook_import.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;

import org.cn.google.BillingClientManager;
import org.cn.google.app.ProviderBridge;
import org.cn.google.common.ProtoDialog;
import org.cn.google.util.GoogleCodeMsg;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class SkuListActivity extends BillingActivity implements PurchasesUpdatedListener, BillingClientStateListener {

    public int ka = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BillingClientManager.init(this, this, this);
        BillingClientManager.connectionService();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ProtoDialog.showLoadingDialog(this);
        BillingClientManager.querySkuDetailsAsync(skuDetailsModels.get(i).getType(), new SkuDetailsResponseListener() {
            @Override
            public final void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                int responseCode = billingResult.getResponseCode();
                if (responseCode != 0 || list == null) {
                    ProtoDialog.showMessageDialog((Context) SkuListActivity.this, GoogleCodeMsg.getResultMsg(responseCode), (dialogInterface, i1) ->
                            dialogInterface.dismiss());
                } else if (list.size() == 0) {
                    ProtoDialog.showMessageDialog((Context) SkuListActivity.this, "Google 返回商品SKU详情为空！\n\"Google Play 不适用于此应用的相关交易\"", (dialogInterface, i1) ->
                            dialogInterface.dismiss());
                } else {
                    BillingClientManager.launchBillingFlow(SkuListActivity.this, list.get(0));
                }

                ProtoDialog.dismissLoading();
            }
        }, skuDetailsModels.get(i).getProductId());

    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        setGoogleStart(true);
        checkUnConsume();
    }

    @Override
    public void onBillingServiceDisconnected() {
        setGoogleStart(false);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        int responseCode = billingResult.getResponseCode();
        if (responseCode == 0 && list != null) {
            ka = 0;
            handlePurchase(list);
        } else if (responseCode == 7) {
            checkUnConsume();
        } else if (responseCode != 1) {
            ProtoDialog.showMessageDialog((Context) this, "Google error:" + GoogleCodeMsg.getResultMsg(responseCode), null);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        handleEvent(intent);
    }

    public void checkUnConsume() {

        ka = 0;
        for (Purchase purchase : BillingClientManager.queryPurchases("inapp")) {
            if (!purchase.isAcknowledged()) {
                List<Purchase> arrayList = new ArrayList<>();
                arrayList.add(purchase);

                //List<Purchase> arrayLists = new ArrayList<>();
                //arrayLists.add(purchase);

                String hint = "检测到订单未完成,请恢复卡单";
                ProtoDialog.showMessageDialog((Context) this, hint, (dialogInterface, i) ->
                        handlePurchase(arrayList)
                );
            }
        }
        //if (arrayList.isEmpty())
        //    return;
        //String hint = "检测到" + arrayList.size() + "个订单未完成,请恢复卡单";
        //String hint = "检测到" + arrayList.size() + "个订单未完成,请恢复卡单" + arrayList.get(0).getOriginalJson();
        //ProtoDialog.showMessageDialog((Context) this, hint, (dialogInterface, i) ->
        //        handlePurchase(arrayList)
        //);
    }

    public void handlePurchase(final List<Purchase> list) {
        ProtoDialog.showLoadingDialog((Context) this);
        Flowable.fromIterable(list)
                .doOnNext(new Consumer<Purchase>() {
                    @Override
                    public void accept(Purchase purchase) throws Throwable {
                        //list.get(0).
                        //purchase.getOriginalJson();
                        Bundle bundle = new Bundle();
                        //bundle.putString("jsonPurchaseInfo", list.get(ka).getOriginalJson());
                        //bundle.putString("mSignature", list.get(ka).getSignature());
                        bundle.putString("jsonPurchaseInfo", purchase.getOriginalJson());
                        bundle.putString("mSignature", purchase.getSignature());
                        ka = ka + 1;
                        Bundle httpInStore = ProviderBridge.httpInStore((Context) SkuListActivity.this, bundle);
                        if (!ProviderBridge.checkResultCode(httpInStore))
                            throw new Exception(ProviderBridge.getResultMsg(httpInStore));
                    }
                })
                .concatMap(new Function<Purchase, Publisher<Purchase>>() {
                    @Override
                    public Publisher<Purchase> apply(Purchase purchase) throws Throwable {
                        return Flowable.create(emitter -> BillingClientManager.consumeAsync(purchase, (billingResult, s) -> {
                            int responseCode = billingResult.getResponseCode();
                            if (responseCode != 0) {
                                emitter.onError(new Exception("Google确认订单失败\n重新点击档位 恢复卡单\n" + GoogleCodeMsg.getResultMsg(responseCode)));
                            } else {
                                emitter.onNext(purchase);
                            }
                            emitter.onComplete();
                        }), BackpressureStrategy.BUFFER);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<Purchase>() {
                    @Override
                    public void onNext(Purchase purchase) {
                        if (list.size() > 1) {
                            String hint = "处理完成第" + (list.indexOf(purchase) + 1) + "个:" + ka;
                            Toast.makeText((Context) SkuListActivity.this, hint, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        ProtoDialog.dismissLoading();
                        ProtoDialog.showMessageDialog((Context) SkuListActivity.this, t.getMessage(), null);
                    }

                    @Override
                    public void onComplete() {
                        ProtoDialog.dismissLoading();
                        Toast.makeText((Context) SkuListActivity.this, "入库成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
