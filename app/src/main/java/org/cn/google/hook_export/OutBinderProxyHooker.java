package org.cn.google.hook_export;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class OutBinderProxyHooker {
    public static String developerPayload;
    public static String sku;

    public void hookConsumeAsync(int i, XC_MethodHook.MethodHookParam methodHookParam) {
        Parcel parcel = (Parcel) methodHookParam.args[2];
        XposedBridge.log("指针："+ parcel.dataPosition() + "");
        parcel.readException();
        parcel.readInt();
        Bundle.CREATOR.createFromParcel(parcel);
        XposedBridge.log("指针："+ parcel.dataPosition() + "");
        int dataPosition = parcel.dataPosition();
        parcel.writeNoException();
        if (i == 5) {
            parcel.writeInt(0);
            XposedBridge.log("修正消耗结果"+ "0");
        }
        if (i == 12) {
            parcel.writeInt(1);
            Bundle bundle = new Bundle();
            bundle.putInt("RESPONSE_CODE", 0);
            bundle.putString("DEBUG_MESSAGE", "success");
            bundle.writeToParcel(parcel, 0);
            XposedBridge.log("修正消耗结果"+ "12");
        }
        parcel.setDataPosition(dataPosition);
    }

    public void hookBuyIntent(Parcel parcel) {
        parcel.readInt();
        parcel.readString();
        sku = parcel.readString();
        parcel.readString();
        developerPayload = parcel.readString();
        XposedBridge.log("入库"+"档位id : " + sku);
        XposedBridge.log("入库"+ "developerPayload : " + developerPayload);
    }

    public void hookBinderProxy(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.os.BinderProxy", loadPackageParam.classLoader, "transact", new Object[]{Integer.TYPE, Parcel.class, Parcel.class, Integer.TYPE, new XC_MethodHook() {
            /* class com.game.hook_export.OutBinderProxyHooker.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.afterHookedMethod(methodHookParam);
                int intValue = ((Integer) methodHookParam.args[0]).intValue();
                if (intValue == 8 || intValue == 3 || intValue == 5 || intValue == 12) {
                    Parcel parcel = (Parcel) methodHookParam.args[1];
                    int dataPosition = parcel.dataPosition();
                    parcel.setDataPosition(4);
//                    parcel.readInt();
                    Log.d("出库xxx", parcel.readString() + "111");
                    if (TextUtils.equals(parcel.readString(), "com.android.vending.billing.IInAppBillingService")) {
                        if (intValue == 8 || intValue == 3) {
                            hookBuyIntent(parcel);
                        }
                        if (intValue == 5 || intValue == 12) {
                            hookConsumeAsync(intValue, methodHookParam);
                        }
                    }
                    parcel.setDataPosition(dataPosition);
                }
            }
        }});
    }

    public void hookBuyPendingIntent() {
        XposedHelpers.findAndHookMethod(Bundle.class, "getParcelable", new Object[]{String.class, new XC_MethodHook() {
            /* class com.game.hook_export.OutBinderProxyHooker.AnonymousClass2 */

            /* access modifiers changed from: protected */
            public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                super.beforeHookedMethod(methodHookParam);
                if (TextUtils.equals((String) methodHookParam.args[0], "BUY_INTENT")) {
                    Application currentApplication = AndroidAppHelper.currentApplication();
                    Intent intent = new Intent("org.cn.google.hook_export.view.PayActivity");
                    intent.putExtra("sku", sku);
                    intent.putExtra("packageName", currentApplication.getPackageName());
                    methodHookParam.setResult(PendingIntent.getActivity(currentApplication, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

                    XposedBridge.log("入库Intent"+"档位id : " + sku);
                }
            }
        }});
    }

    public void enter(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        hookBuyPendingIntent();
        hookBinderProxy(loadPackageParam);
    }

}
