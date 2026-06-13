package org.cn.google.provider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.lzy.okgo.OkGo;

import org.cn.google.app.AppConstance;
import org.cn.google.mode.BaseResponse;
import org.cn.google.mode.LoginResponse;
import org.cn.google.mode.SkuDetailsModel;
import org.cn.google.util.JsonUtils;

import java.util.List;

import okhttp3.Response;

public class HookProvider extends BaseProvider {


    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle bundle = new Bundle();
        bundle.putInt("code", -1);
        if (extras == null) {
            bundle.putString("message", "参数错误");
        } else {
            dispatchMethod(method, extras, bundle);
        }
        return bundle;
    }
//    private void dispatchMethod(String str, Bundle bundle, Bundle bundle2) {
//        if (ProviderApi.HOOK_STATUS.equals(str)) {
//            bundle2.putInt(ProviderApi.HOOK_STATUS, this.hookRepository.getHookStatus());
//        } else if (ProviderApi.HTTP_COLL_SKU.equals(str)) {
//            new MethodCollection(bundle).getCollectionResult(bundle2);
//        } else if (ProviderApi.HTTP_GET_SKU_LIST.equals(str)) {
//            new MethodImport().getSkuDetail(bundle2, bundle);
//        } else if (ProviderApi.HTTP_IN_STORE.equals(str)) {
//            new MethodImport().importPurchase(bundle2, bundle);
//        }
//    }

    private void dispatchMethod(String method, Bundle extras, Bundle bundle) {
        if (AppConstance.KEY_HOOK_STATUS.equals(method)) {
            getHookStatus(bundle);
        } else if (AppConstance.HTTP_GET_SKU_LIST.equals(method)) {
            httpGetSkuList(extras, bundle);
        } else if (AppConstance.HTTP_PUT_SKU_DETAILS.equals(method)) {
            httpPutSkuDetails(extras, bundle);
        } else if (AppConstance.HTTP_IN_STORE.equals(method)) {
            httpInStore(extras, bundle);
        }
    }

    /**
     * 获取hook状态
     */
    private void getHookStatus(Bundle bundle) {
        bundle.putInt("code", 0);
        bundle.putInt(AppConstance.KEY_HOOK_STATUS, SPStaticUtils.getInt(AppConstance.KEY_HOOK_STATUS));
    }

    /**
     * 上传sku详情
     */
    private void httpPutSkuDetails(Bundle extras, Bundle bundle) {
        String skuJson = extras.getString("skuJson");
        String gameName = extras.getString("gameName");
        String packageName = extras.getString("packageName");
        //此处添加网络请求数据
        try {
            String api = SPStaticUtils.getString(AppConstance.KEY_CONFIG_API, AppConstance.HTTP_DEFAULT_HOST);
            if (api.length() == 0) {
                throw new Exception("API地址配置为空");
            }
            LoginResponse.UserInfo userInfo =
                    GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
            Response response = OkGo.<String>get(api + "/api/game/addGame")
                    .params("token", userInfo.getToken())
                    .params("skuJson", skuJson)
                    .params("gameName", gameName)
                    .params("packageName", packageName)
                    .execute();
            if (response.code() != 200)
                throw new Exception("Response-" + response.message() + "-" + response.code());
            BaseResponse baseResponse = GsonUtils.fromJson(response.body().string(), BaseResponse.class);
            if (baseResponse.getCode() != 200) {
                throw new Exception("BaseResponse-" + baseResponse.getMsg() + "-" + baseResponse.getCode());
            }
            bundle.putInt("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            bundle.putInt("code", -1);
            bundle.putString("message", e.getMessage());
        }
        //此处添加网络请求数据
    }

    /**
     * 获取sku列表
     */
    private void httpGetSkuList(Bundle extras, Bundle bundle) {
        String packageName = extras.getString("packageName");
        //此处添加网络请求数据

        try {
            String api = SPStaticUtils.getString(AppConstance.KEY_CONFIG_API, AppConstance.HTTP_DEFAULT_HOST);
            if (api.length() == 0) {
                throw new Exception("API地址配置为空");
            }
            LoginResponse.UserInfo userInfo =
                    GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
            Response response = OkGo.post(api + "/api/game/price")
                    .params("token", userInfo.getToken())
                    .params("packageName", packageName)
                    .execute();
            if (response.code() != 200)
                throw new Exception("Response-" + response.message() + "-" + response.code());
            BaseResponse baseResponse = GsonUtils.fromJson(response.body().string(), BaseResponse.class);
            if (baseResponse.getCode() != 200)
                throw new Exception("BaseResponse-" + baseResponse.getMsg() + "-" + baseResponse.getCode());
            bundle.putInt("code", 0);
            bundle.putString("data", GsonUtils.toJson(baseResponse.getData()));
        } catch (Exception e) {
            e.printStackTrace();
            bundle.putInt("code", -1);
            bundle.putString("message", e.getMessage());
        }
        //此处添加网络请求数据

        //一下测试数据
    }

    /**
     * 入库
     */
    private void httpInStore(Bundle extras, Bundle bundle) {
        String jsonPurchaseInfo = extras.getString("jsonPurchaseInfo");
        String mSignature = extras.getString("mSignature");
        try {
            String api = SPStaticUtils.getString(AppConstance.KEY_CONFIG_API, AppConstance.HTTP_DEFAULT_HOST);
            if (api.length() == 0) {
                throw new Exception("API地址配置为空");
            }
            LoginResponse.UserInfo userInfo =
                    GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
            Response response = OkGo.post(api + "/api/game/TransactionInput")
                    .params("token", userInfo.getToken())
                    .params("jsonPurchaseInfo", jsonPurchaseInfo)
                    .params("mSignature", mSignature)
                    .execute();
            if (response.code() != 200)
                throw new Exception("Response-" + response.message() + "-" + response.code());
            BaseResponse baseResponse = GsonUtils.fromJson(response.body().string(), BaseResponse.class);
            if (baseResponse.getCode() != 200)
                throw new Exception("BaseResponse-" + baseResponse.getMsg() + "-" + baseResponse.getCode());
            bundle.putInt("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            bundle.putInt("code", -1);
            bundle.putString("message", e.getMessage());
        }
    }

}
