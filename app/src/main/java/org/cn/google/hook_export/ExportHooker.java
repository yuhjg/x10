package org.cn.google.hook_export;

import org.cn.google.hook_export.special.GarenaFctw;
import org.cn.google.hook_export.special.NexonMaplem;
import org.cn.google.hook_export.special.SYNN;
import org.cn.google.hook_export.special.SwlmHooker;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ExportHooker {
    public void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        new OutBinderProxyHooker().enter(loadPackageParam);
        String packageName = loadPackageParam.packageName;

        if (packageName.equals("com.igg.android.lordsmobile")) {
            return;
        }
        if (packageName.equals("com.garena.game.fctw")) {
            new GarenaFctw().hook(loadPackageParam);
        } else if (packageName.equals("com.more.dayzsurvival.gp")){
            new SwlmHooker().hook(loadPackageParam);
        } else if (!packageName.equals("com.nexon.nsc.maplem")) {
            if (packageName.equals("com.papegames.nn4.tw")
                    || packageName.equals("org.telegram.messenger")
                    //|| packageName.equals("com.roblox.client")
            ) {
                new SYNN().hook(loadPackageParam);
            }

            new JsonHooker().hook(loadPackageParam);
        } else {
            new NexonMaplem().hook(loadPackageParam);
        }

    }
}
