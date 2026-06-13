package org.cn.google.hook_export.special;

import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SYNN {
    public static String ObfuscatedAccountId;
    public static String ObfuscatedProfileId;

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.android.billingclient.api.BillingFlowParams$Builder", loadPackageParam.classLoader, "setObfuscatedAccountId", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.SYNN.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                SYNN.ObfuscatedAccountId = (String) methodHookParam.args[0];
            }
        }});
        XposedHelpers.findAndHookMethod("com.android.billingclient.api.BillingFlowParams$Builder", loadPackageParam.classLoader, "setObfuscatedProfileId", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.SYNN.AnonymousClass2 */

            /* access modifiers changed from: protected */
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                SYNN.ObfuscatedProfileId = (String) methodHookParam.args[0];
            }
        }});
        XposedHelpers.findAndHookMethod("org.json.JSONObject", loadPackageParam.classLoader, "optString", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.SYNN.AnonymousClass3 */

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                String str = (String) methodHookParam.args[0];
                if (TextUtils.equals(str, "obfuscatedAccountId") && SYNN.ObfuscatedAccountId != null) {
                    methodHookParam.setResult(SYNN.ObfuscatedAccountId);
                }
                if (TextUtils.equals(str, "obfuscatedProfileId") && SYNN.ObfuscatedProfileId != null) {
                    methodHookParam.setResult(SYNN.ObfuscatedProfileId);
                }
            }
        }});
    }

}
