package org.cn.google.hook_import;

import android.app.Application;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ImportHooker {
    public void hook(Application application, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        new InstrumentationHooker().hook(application, loadPackageParam.classLoader);
        String str = loadPackageParam.packageName;
//        if (((str.hashCode() == -1550719350 && str.equals("com.yalla.yallagames")) ? (char) 0 : 65535) == 0) {
//            new FlyCheesApp().hook(loadPackageParam);
//        }
    }

}
