package org.cn.google.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class ProviderBridge {

    public static int getHookStatus(Context context) throws Exception {
        return getProviderData(context, AppConstance.KEY_HOOK_STATUS, null, new Bundle()).getInt(AppConstance.KEY_HOOK_STATUS);
    }

    public static Bundle httpPutSkuDetails(Context context, Bundle bundle) throws Exception {
        return getProviderData(context, AppConstance.HTTP_PUT_SKU_DETAILS, null, bundle);
    }

    public static Bundle httpGetSkuList(Context context, Bundle bundle) throws Exception {
        return getProviderData(context, AppConstance.HTTP_GET_SKU_LIST, null, bundle);
    }

    public static Bundle httpInStore(Context context, Bundle bundle) throws Exception {
        return getProviderData(context, AppConstance.HTTP_IN_STORE, null, bundle);
    }

    public static Bundle getProviderData(Context context, String str, String str2, Bundle bundle) throws Exception {
        try {
            return context.getContentResolver().call(Uri.parse(AppConstance.AUTHORITIES), str, str2, bundle);
        } catch (Exception unused) {
            throw new Exception("插件未启动" + unused.getMessage());
        }
    }

    public static boolean checkResultCode(Bundle bundle) {
        return bundle.getInt("code") == 0;
    }

    public static String getResultMsg(Bundle bundle) {
        String string = bundle.getString("message");
        return TextUtils.isEmpty(string) ? "" : string;
    }

}
