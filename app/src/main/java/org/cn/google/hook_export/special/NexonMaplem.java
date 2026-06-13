package org.cn.google.hook_export.special;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NexonMaplem {
    public static String ObfuscatedAccountId;
    public static String ObfuscatedProfileId;

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.android.billingclient.api.BillingFlowParams$Builder", loadPackageParam.classLoader, "setObfuscatedAccountId", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.NexonMaplem.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                NexonMaplem.ObfuscatedAccountId = (String) methodHookParam.args[0];
            }
        }});
        XposedHelpers.findAndHookMethod("com.android.billingclient.api.BillingFlowParams$Builder", loadPackageParam.classLoader, "setObfuscatedProfileId", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.NexonMaplem.AnonymousClass2 */

            /* access modifiers changed from: protected */
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                NexonMaplem.ObfuscatedProfileId = (String) methodHookParam.args[0];
            }
        }});
        XposedHelpers.findAndHookMethod("com.nexon.platform.store.vendor.GooglePlayBillingPurchase", loadPackageParam.classLoader, "getDeveloperPayload", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.NexonMaplem.AnonymousClass3 */

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                methodHookParam.setResult("");
            }
        }});
        XposedHelpers.findAndHookMethod("com.nexon.platform.store.vendor.GooglePlayBillingPurchase", loadPackageParam.classLoader, "getPurchaseState", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.NexonMaplem.AnonymousClass4 */

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                methodHookParam.setResult(1);
            }
        }});
    }

}
