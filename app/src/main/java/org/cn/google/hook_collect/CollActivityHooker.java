package org.cn.google.hook_collect;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CollActivityHooker {
    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.app.Activity", loadPackageParam.classLoader, "startIntentSenderForResult", new Object[]{IntentSender.class, Integer.TYPE, Intent.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, new XC_MethodHook() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                Object[] objArr = methodHookParam.args;
                if (TextUtils.equals(((IntentSender) objArr[0]).getCreatorPackage(), "com.android.vending")) {
                    CollActivityHooker.this.noticeGamePayCancel((Activity) methodHookParam.thisObject, (Integer) objArr[1]);
                    methodHookParam.setResult((Object) null);
                }
            }
        }});
    }

    private void noticeGamePayCancel(Activity activity, Integer num) {
        try {
            Method declaredMethod = Activity.class.getDeclaredMethod("onActivityResult", Integer.TYPE, Integer.TYPE, Intent.class);
            declaredMethod.setAccessible(true);
            Intent intent = new Intent();
            intent.putExtra("RESPONSE_CODE", 1);
            intent.putExtra("DEBUG_MESSAGE", "用户取消");
            intent.putExtra("devil", false);
            declaredMethod.invoke(activity, num, 0, intent);
        } catch (Exception unused) {
        }
    }

}
