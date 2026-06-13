package org.cn.google.hook_export.special;

import android.content.Context;
import android.os.Bundle;

import androidx.core.os.EnvironmentCompat;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SwlmHooker {
    private void hookDevice(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class findClass = XposedHelpers.findClass("org.cocos2dx.ext.Udid", loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod(findClass, "generateHighVersionUUID", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass1 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("generateHighVersionUUID = " + methodHookParam.getResult());
            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult("838097bf5dcd59e5d8b2adcbc36f3d10");
            }
        }});
        XposedHelpers.findAndHookMethod(findClass, "generateUUID", new Object[]{Context.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass2 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("generateUUID = " + methodHookParam.getResult());
            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult("emula_1c434d5122024c81a771ad17eea2f18f");
            }
        }});
        XposedHelpers.findAndHookMethod(findClass, "getDeviceJsonInfo", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass3 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("getDeviceJsonInfo = " + methodHookParam.getResult());
            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult("{\"BOARD\":\"walleye\",\"SERIAL\":\"unknown\",\"CPU_ABI2\":\"\",\"DEVICE\":\"walleye\",\"heightPixels\":\"1794\",\"MODEL\":\"Pixel 2\",\"MANUFACTURER\":\"Google\",\"CPU_ABI\":\"arm64-v8a\",\"widthPixels\":\"1080\",\"BRAND\":\"google\",\"densityDpi\":\"420\",\"cameraFalsh\":\"true\",\"FINGERPRINT\":\"google\\/walleye\\/walleye:9\\/PQ3A.190705.001\\/5565753:user\\/release-keys\",\"HARDWARE\":\"walleye\",\"PRODUCT\":\"walleye\",\"sensorNum\":\"29\"}");
            }
        }});
        XposedHelpers.findAndHookMethod(findClass, "getMacAddr", new Object[]{Context.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass4 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("getMacAddr = " + methodHookParam.getResult());
            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult("02:00:00:00:00:00");
            }
        }});
        XposedHelpers.findAndHookMethod(findClass, "getSUid", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass5 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("getSUid = " + methodHookParam.getResult());
            }
        }});
        XposedHelpers.findAndHookMethod(findClass, "getSerialId", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass6 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("getSerialId = " + methodHookParam.getResult());
            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult(EnvironmentCompat.MEDIA_UNKNOWN);
            }
        }});
        XposedHelpers.findAndHookMethod(findClass, "getUid", new Object[]{new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass7 */

            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                XposedBridge.log("getUid = " + methodHookParam.getResult());
            }

            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                methodHookParam.setResult("emula_751f54513faa43ddae7e3ce02b0deac5");
            }
        }});
    }

    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.dayzsurvival.of.kings.EmpireActivity", loadPackageParam.classLoader, "onCreate", new Object[]{Bundle.class, new XC_MethodHook() {
            /* class com.game.hook_export.special.SwlmHooker.AnonymousClass8 */

            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.afterHookedMethod(methodHookParam);
            }
        }});
    }

}
