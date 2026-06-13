package org.cn.google.hook_export.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import org.cn.google.R;
import org.cn.google.app.AppConstance;
import org.cn.google.common.DialogUtils;
import org.cn.google.common.ProtoDialog;
import org.cn.google.mode.BaseResponse;
import org.cn.google.mode.ExportDetails;
import org.cn.google.mode.LoginResponse;
import org.cn.google.mode.ProductDetails;
import org.cn.google.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import okhttp3.Response;

public class PayActivity extends Activity implements ExportAdapter.ItemOnClickInterface {

    private ListView mPayListView;
    private TextView mPayTextEmpty;
    protected ExportAdapter mExportAdapter;
    protected List<ExportDetails> exportDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initView();
        getExportListAsync();
    }

    private void initView() {

        mExportAdapter = new ExportAdapter((Context) this, exportDetailsList, this);
        mPayListView = findViewById(R.id.payListView);
        mPayListView.setAdapter(mExportAdapter);

        mPayTextEmpty = findViewById(R.id.payTextEmpty);
    }

    public void getExportListAsync() {
        String productId = getIntent().getStringExtra("sku");
        String packageName = getIntent().getStringExtra("packageName");
        int status = SPStaticUtils.getInt(AppConstance.KEY_HOOK_STATUS);
        ExportRequest exportRequest = new ExportRequest();
        if (status != 4) {
            exportRequest.setProductId(productId);
        }
        exportRequest.setPackageName(packageName);
        if (TextUtils.isEmpty(productId) && TextUtils.isEmpty(packageName)) {
            mPayTextEmpty.setText("productId||packageName参数为空");
            return;
        }

        Observable.just(exportRequest).map(new Function<ExportRequest, BaseResponse>() {
            @Override
            public BaseResponse apply(ExportRequest exportRequest) throws Throwable {

                String api = SPStaticUtils.getString(AppConstance.KEY_CONFIG_API, AppConstance.HTTP_DEFAULT_HOST);
                if (api.length() == 0) {
                    throw new Exception("API地址配置为空");
                }
                LoginResponse.UserInfo userInfo =
                        GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
                Response response = OkGo.post(api + "/api/game/TransactionTable")
                        .params("token", userInfo.getToken())
                        .params("productId", exportRequest.productId)
                        .params("packageName", exportRequest.packageName)
                        .execute();
                if (response.code() != 200)
                    throw new Exception("Response-" + response.message() + "-" + response.code());
                BaseResponse baseResponse = GsonUtils.fromJson(response.body().string(), BaseResponse.class);
                if (baseResponse.getCode() != 200)
                    throw new Exception("BaseResponse-" + baseResponse.getMsg() + "-" + baseResponse.getCode());
                return baseResponse;
            }
        }).map(new Function<BaseResponse, List<ExportDetails>>() {
            @Override
            public List<ExportDetails> apply(BaseResponse baseResponse) throws Throwable {
                if (baseResponse.getData() == null)
                    throw new Exception("无可用数据~");
                List<ExportDetails> exportDetailsList = JsonUtils.stringToList(baseResponse.getData(), ExportDetails.class);
                if (exportDetailsList.size() == 0)
                    throw new Exception("无可用数据~");
                return exportDetailsList;
            }
        })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ExportDetails>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mPayTextEmpty.setText("正在加载数据...");
                    }

                    @Override
                    public void onNext(@NonNull List<ExportDetails> exportDetails) {
                        mExportAdapter.setNewInstance(exportDetails);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mPayTextEmpty.setText(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mPayTextEmpty.setText("加载成功");
                        mPayTextEmpty.setVisibility(View.GONE);
                    }
                });
    }

    private void sentPayResult(String jsonPurchaseInfo, String mSignature) {
        Intent intent = new Intent();
        intent.putExtra("INAPP_PURCHASE_DATA", jsonPurchaseInfo);
        intent.putExtra("INAPP_DATA_SIGNATURE", mSignature);
        intent.putExtra("RESPONSE_CODE", 0);
        setResult(-1, intent);
        finish();
    }


    private void noticeGamePayCancel() {
        Intent intent = new Intent();
        intent.putExtra("RESPONSE_CODE", 1);
        intent.putExtra("DEBUG_MESSAGE", "用户取消");
        setResult(0, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() != 4) {
            return super.onTouchEvent(event);
        }
        noticeGamePayCancel();
        return true;
    }

    @Override // androidx.core.app.ComponentActivity, androidx.appcompat.app.AppCompatActivity
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 4) {
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override
    public void onItemClick(View view, int position) {

        ExportDetails details = exportDetailsList.get(position);
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setProductId(details.getProductId());
        exportRequest.setPackageName(details.getPackageName());

        DialogUtils.showLoadingDialog(PayActivity.this);
        Flowable.just(exportRequest).map(new Function<ExportRequest, BaseResponse>() {
            @Override
            public BaseResponse apply(ExportRequest exportRequest) throws Throwable {

                String api = SPStaticUtils.getString(AppConstance.KEY_CONFIG_API, AppConstance.HTTP_DEFAULT_HOST);
                if (api.length() == 0) {
                    throw new Exception("API地址配置为空");
                }
                LoginResponse.UserInfo userInfo =
                        GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
                Response response = OkGo.post(api + "/api/game/outbound")
                        .params("token", userInfo.getToken())
                        .params("productId", exportRequest.productId)
                        .params("packageName", exportRequest.packageName)
                        .execute();
                if (response.code() != 200)
                    throw new Exception("Response-" + response.message() + "-" + response.code());
                BaseResponse baseResponse = GsonUtils.fromJson(response.body().string(), BaseResponse.class);
                if (baseResponse.getCode() != 200)
                    throw new Exception("BaseResponse-" + baseResponse.getMsg() + "-" + baseResponse.getCode());
                return baseResponse;
            }
        }).map(new Function<BaseResponse, ProductDetails>() {
            @Override
            public ProductDetails apply(BaseResponse baseResponse) throws Throwable {
                if (baseResponse.getData() == null)
                    throw new Exception("支付数据为空");
                return JsonUtils.objectToClass(baseResponse.getData(), ProductDetails.class);
            }
        })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<ProductDetails>() {
                    @Override
                    public void onNext(ProductDetails productDetails) {
                        sentPayResult(productDetails.getJsonPurchaseInfo(), productDetails.getmSignature());
                        Toast.makeText((Context) PayActivity.this, "出库成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        DialogUtils.dismissLoading();
                        DialogUtils.showMessageDialog(PayActivity.this, t.getMessage(), null);
                    }

                    @Override
                    public void onComplete() {
                        DialogUtils.dismissLoading();
                    }
                });

    }


    private static class ExportRequest {
        private String packageName;
        private String productId;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }
}
