package org.cn.google.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cn.google.R;

public class DialogUtils {

    private static AlertDialog mAlertDialog;

    public static void showLoadingDialog(Context context) {
        mAlertDialog = new AlertDialog.Builder(context, R.style.CustomProgressDialog).create();
        View inflate = LayoutInflater.from(context).inflate(R.layout.cusstom_progress_dialog, (ViewGroup) null);
        mAlertDialog.setView(inflate, 0, 0, 0, 0);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
        ((TextView) inflate.findViewById(R.id.tvTip)).setText("加载中...");
        mAlertDialog.show();

    }

    public static void dismissLoading() {
        AlertDialog alertDialog = mAlertDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public static void showMessageDialog(Context context, String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setMessage(str).setNegativeButton("取消", null).setPositiveButton("确定", onClickListener).create().show();
    }


    public static void showMessageDialogNotCancel(Context context, String str, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setMessage(str).setPositiveButton("确定", onClickListener).create().show();
    }

}
