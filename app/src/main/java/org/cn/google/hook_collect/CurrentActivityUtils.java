package org.cn.google.hook_collect;

import android.app.Activity;
import android.content.Intent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CurrentActivityUtils extends XC_MethodHook {
    private static volatile Activity currentActivity;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /* access modifiers changed from: protected */


    @Override
    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
        currentActivity = (Activity) methodHookParam.getResult();
    }

    public static void hookCurrentActivity(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.app.Instrumentation", loadPackageParam.classLoader, "newActivity", new Object[]{ClassLoader.class, String.class, Intent.class, new CurrentActivityUtils()});
    }

    public static void runOnUiThread(Runnable runnable) {
        if (currentActivity != null) {
            currentActivity.runOnUiThread(runnable);
        }
    }

}
