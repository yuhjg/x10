package org.cn.google.hook_import;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import org.cn.google.BuildConfig;

import java.io.File;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.XposedBridge;

public class InstrumentationProxy extends Instrumentation {
    private ClassLoader classLoader;
    private Context mContext;

    public InstrumentationProxy(Context context, ClassLoader classLoader2) {
        this.mContext = context;
        this.classLoader = classLoader2.getParent();
    }

    @Override
    public Activity newActivity(ClassLoader classLoader2, String str, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            return super.newActivity(new PathClassLoader(new File(this.mContext.getPackageManager().getApplicationInfo(BuildConfig.APPLICATION_ID, 0).sourceDir).getAbsolutePath(), this.classLoader), "org.cn.google.hook_import.view.SkuListActivity", intent);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return super.newActivity(classLoader2, str, intent);
    }

}
