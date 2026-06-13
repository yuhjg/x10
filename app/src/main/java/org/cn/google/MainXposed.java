package org.cn.google;

import android.app.Application;
import android.content.Context;
import android.os.Parcel;
import android.text.TextUtils;

import com.android.billingclient.BuildConfig;

import org.cn.google.app.ProviderBridge;
import org.cn.google.hook_collect.CollectionHooker;
import org.cn.google.hook_export.ExportHooker;
import org.cn.google.hook_export.OutBinderProxyHooker;
import org.cn.google.hook_import.ImportHooker;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Administrator
 */
public class MainXposed implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        String packageName = loadPackageParam.processName;

        if (!skipPackageName(packageName)) {
            XposedBridge.log("HOOK【" + packageName + "】主进程");
            //com.denachina.g23002013.android
            //com.igg.android.lordsmobile
            //com.gamania.lineagem
            //enterApplication(loadPackageParam);
            enter(loadPackageParam, packageName);
        }
    }


//    public void enterApplication(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
//        XposedHelpers.findAndHookMethod(Application.class, "attach", new Object[]{Context.class, new XC_MethodHook() {
//            /* class com.game.silkroads.HookApplication.AnonymousClass1 */
//
//            /* access modifiers changed from: protected */
//            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
//                super.afterHookedMethod(methodHookParam);
//                Application application = (Application) methodHookParam.thisObject;
//                int hookStatus = ProviderBridge.getHookStatus(application);
//                if (hookStatus == 1) {
//                    new CollectionHooker().hook(loadPackageParam);
//                } else if (hookStatus == 2) {
//                    new ImportHooker().hook(application, loadPackageParam);
//                } else if (hookStatus == 3 || hookStatus == 4) {
//                    new OutBinderProxyHooker().enter(loadPackageParam);
//                }
//                XposedBridge.log("HOOK STATUS ->" + ProviderBridge.getHookStatus(application));
//            }
//        }});
//    }

    private void enter(final XC_LoadPackage.LoadPackageParam loadPackageParam, String str) {
        if (str.equals("com.yalla.yallagames")) {
            XposedHelpers.findAndHookMethod("com.yalla.yallagames.FlyCheesApp", loadPackageParam.classLoader, "onCreate", new Object[]{new XC_MethodHook() {
                /* class com.game.silkroads.HookApplication.AnonymousClass1 */

                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    MainXposed.this.enterApplication(loadPackageParam, methodHookParam, true);
                }
            }});
            return;
        }
        XposedHelpers.findAndHookMethod(Application.class, "onCreate", new Object[]{new XC_MethodHook() {
            /* class com.game.silkroads.HookApplication.AnonymousClass2 */

            /* access modifiers changed from: protected */
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                MainXposed.this.enterApplication(loadPackageParam, methodHookParam, false);
            }
        }});
    }

    public void enterApplication(XC_LoadPackage.LoadPackageParam loadPackageParam, XC_MethodHook.MethodHookParam methodHookParam, boolean z) {
        Application application = (Application) methodHookParam.thisObject;
        int hookStatus = 0;
        try {
            hookStatus = ProviderBridge.getHookStatus(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hookStatus == 1) {
            new CollectionHooker().hook(loadPackageParam);
        } else if (hookStatus == 2) {
            new ImportHooker().hook(application, loadPackageParam);
            if (z) {
                methodHookParam.setResult((Object) null);
            }
        } else if (hookStatus == 3 || hookStatus == 4) {
            new ExportHooker().hook(loadPackageParam);
        }
        XposedBridge.log("HOOK STATUS ->" + hookStatus);
    }

    private boolean skipPackageName(String str) {
        return str.startsWith("org.cn.google") || str.startsWith("com.android") || str.startsWith("com.google") || str.contains("launcher3") || str.startsWith("com.samsung") || TextUtils.equals(str, "com.topjohnwu.magisk") || str.startsWith("com.facebook") || TextUtils.equals(str, BuildConfig.APPLICATION_ID) || TextUtils.equals(str, "com.ap");
    }


}
