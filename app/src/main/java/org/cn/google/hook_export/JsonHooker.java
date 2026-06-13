package org.cn.google.hook_export;

import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class JsonHooker {
    private void hookJson(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("org.json.JSONObject", loadPackageParam.classLoader, "getString", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.JsonHooker.AnonymousClass1 */

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                String str;
                super.beforeHookedMethod(methodHookParam);
                if (TextUtils.equals((String) methodHookParam.args[0], "developerPayload") && (str = OutBinderProxyHooker.developerPayload) != null) {
                    methodHookParam.setResult(str);
                }
            }
        }});
        XposedHelpers.findAndHookMethod("org.json.JSONObject", loadPackageParam.classLoader, "optString", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.JsonHooker.AnonymousClass2 */

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                String str;
                super.beforeHookedMethod(methodHookParam);
                if (TextUtils.equals((String) methodHookParam.args[0], "developerPayload") && (str = OutBinderProxyHooker.developerPayload) != null) {
                    methodHookParam.setResult(str);
                }
            }
        }});
        XposedHelpers.findAndHookMethod("org.json.JSONObject", loadPackageParam.classLoader, "optBoolean", new Object[]{String.class, Boolean.TYPE, new XC_MethodHook() {
            /* class com.game.hook_export.JsonHooker.AnonymousClass3 */

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                if (TextUtils.equals((String) methodHookParam.args[0], "acknowledged")) {
                    methodHookParam.setResult(false);
                }
            }
        }});
    }

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        hookJson(loadPackageParam);
    }

}
