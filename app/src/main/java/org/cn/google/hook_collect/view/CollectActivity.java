package org.cn.google.hook_collect.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.lzy.okgo.OkGo;

import org.cn.google.R;
import org.cn.google.app.AppConstance;
import org.cn.google.common.DialogUtils;
import org.cn.google.common.ProtoDialog;
import org.cn.google.mode.BaseResponse;
import org.cn.google.mode.LoginResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import okhttp3.Response;

public class CollectActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        httpCollect();
    }

    private void httpCollect() {

        Intent intent = getIntent();
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setSkuJson(intent.getStringExtra("skuJson"));
        collectRequest.setGameName(intent.getStringExtra("gameName"));
        collectRequest.setPackageName(intent.getStringExtra("packageName"));
        DialogUtils.showLoadingDialog(CollectActivity.this);
        Flowable.just(collectRequest).map(new Function<CollectRequest, BaseResponse>() {
            @Override
            public BaseResponse apply(CollectRequest collectRequest) throws Throwable {
                if (TextUtils.isEmpty(collectRequest.getSkuJson()) || TextUtils.isEmpty(collectRequest.getSkuJson()) || TextUtils.isEmpty(collectRequest.getSkuJson())) {
                    throw new Exception("采集到的参数有误！");
                }
                String api = SPStaticUtils.getString(AppConstance.KEY_CONFIG_API, AppConstance.HTTP_DEFAULT_HOST);
                if (api.length() == 0) {
                    throw new Exception("API地址配置为空");
                }
                LoginResponse.UserInfo userInfo =
                        GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
                Response response = OkGo.<String>get(api + "/api/game/addGame")
                        .params("token", userInfo.getToken())
                        .params("skuJson", collectRequest.getSkuJson())
                        .params("gameName", collectRequest.getGameName())
                        .params("packageName", collectRequest.getPackageName())
                        .execute();
                if (response.code() != 200)
                    throw new Exception("Response-" + response.message() + "-" + response.code());
                BaseResponse baseResponse = GsonUtils.fromJson(response.body().string(), BaseResponse.class);
                if (baseResponse.getCode() != 200) {
                    throw new Exception("BaseResponse-" + baseResponse.getMsg() + "-" + baseResponse.getCode());
                }
                return baseResponse;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        Toast.makeText(CollectActivity.this, "采集成功", Toast.LENGTH_SHORT).show();
                        noticeGamePayCancel();
                    }

                    @Override
                    public void onError(Throwable t) {
                        DialogUtils.dismissLoading();
                        DialogUtils.showMessageDialogNotCancel(CollectActivity.this, "采集失败\n" + t.getMessage(), (dialogInterface, i) ->
                                noticeGamePayCancel());
                    }

                    @Override
                    public void onComplete() {
                        DialogUtils.dismissLoading();
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() != 4)
            return super.onTouchEvent(event);
        noticeGamePayCancel();
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 4) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void noticeGamePayCancel() {
        Intent intent = new Intent();
        intent.putExtra("RESPONSE_CODE", 1);
        intent.putExtra("DEBUG_MESSAGE", "用户取消");
        setResult(0, intent);
        finish();
    }

    private static class CollectRequest {
        private String skuJson;
        private String gameName;
        private String packageName;

        public String getSkuJson() {
            return skuJson;
        }

        public void setSkuJson(String skuJson) {
            this.skuJson = skuJson;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
    }
}
