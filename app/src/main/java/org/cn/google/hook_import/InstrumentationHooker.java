package org.cn.google.hook_import;

import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;

import java.lang.reflect.Field;

import de.robv.android.xposed.XposedBridge;

public class InstrumentationHooker {
    public void hook(Context context, ClassLoader classLoader) {
        hookActivityThreadInstrumentation(context, classLoader);
    }

    private void hookActivityThreadInstrumentation(Context context, ClassLoader classLoader) {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Field declaredField = cls.getDeclaredField("sCurrentActivityThread");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(null);
            Field declaredField2 = cls.getDeclaredField("mInstrumentation");
            declaredField2.setAccessible(true);
            declaredField2.set(obj, new InstrumentationProxy(context, classLoader));
            XposedBridge.log("aaaaaaaaaaaaaaaaaaaaaa：hookActivityThreadInstrumentation");
        } catch (Exception e) {
            e.printStackTrace();
//            ToastUtils.showShort("挂载失败");
            XposedBridge.log("aaaaaaaaaaaaaaaaaaaaaa：挂载失败" + e.getMessage());
        }
    }

}
