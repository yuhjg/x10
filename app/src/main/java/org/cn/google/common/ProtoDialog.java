package org.cn.google.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ProtoDialog {

    private static ProgressDialog pd;

    public static void showLoadingDialog(Context context) {
        if (pd == null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            pd = progressDialog;
            progressDialog.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("加载中...");
        }
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    public static void dismissLoading() {
        ProgressDialog progressDialog = pd;
        if (progressDialog != null && progressDialog.isShowing()) {
            pd.dismiss();
        }
    }

    public static void showMessageDialog(Context context, String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setMessage(str).setNegativeButton("取消", null).setPositiveButton("确定", onClickListener).create().show();
    }

    public static void showMessageDialogNotCancel(Context context, String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setMessage(str).setPositiveButton("确定", onClickListener).create().show();
    }

}
