package org.cn.google.hook_import;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FlyCheesApp {
    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.yalla.yallagames.FlyCheesApp", loadPackageParam.classLoader, "onCreate", new Object[]{new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                methodHookParam.setResult((Object) null);
            }
        }});
    }

}
