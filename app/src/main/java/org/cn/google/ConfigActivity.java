package org.cn.google;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.cn.google.app.AppConstance;
import org.cn.google.common.ProtoDialog;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etConfigApiPath;
    private Button btnConfigConfirm, btnConfigQuote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initView();
    }

    private void initView() {
        etConfigApiPath = findViewById(R.id.etConfigApiPath);
        etConfigApiPath.addTextChangedListener(this);
        btnConfigConfirm = findViewById(R.id.btnConfigConfirm);
        btnConfigQuote = findViewById(R.id.btnConfigQuote);
        btnConfigConfirm.setOnClickListener(this);
        btnConfigQuote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnConfigConfirm) {

            if (!RegexUtils.isURL(etConfigApiPath.getText())) {
                ToastUtils.showShort("API地址格式错误");
                return;
            }

            ProtoDialog.showMessageDialog((Context) this, "保存API地址，请务必检查API地址是否正确！", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SPStaticUtils.clear();
                    SPStaticUtils.put(AppConstance.KEY_CONFIG_API, etConfigApiPath.getText().toString());
                    Intent intent = new Intent();
                    intent.setClass((Context) ConfigActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else if (v.getId() == R.id.btnConfigQuote) {
            etConfigApiPath.setText("http://games.usbuydo.com");
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnConfigConfirm.setEnabled(!TextUtils.isEmpty(s));
    }

//    private List<String> getPackage() {
//        List<String> packages = new ArrayList<String>();
//        try {
//            List<PackageInfo> packageInfo = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES |
//                    PackageManager.GET_SERVICES);
//            for (PackageInfo info : packageInfo) {
//                String pk = info.packageName;
//                if (skipPackageName(pk)) {
//
//                    LogUtils.e(skipPackageNameSystemApp(info) + ",," + pk + info.applicationInfo.loadLabel(getPackageManager()));
//                    packages.add(pk);
//                }
//            }
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//        return packages;
//    }
//
//    private boolean skipPackageName(String str) {
//        return str.startsWith("com.android") || str.startsWith("com.google") || str.contains("launcher3") || str.startsWith("com.samsung") || TextUtils.equals(str, "com.topjohnwu.magisk") || str.startsWith("com.facebook") || TextUtils.equals(str, BuildConfig.APPLICATION_ID);
////        return true;
//    }
//
//    private boolean skipPackageNameSystemApp(PackageInfo packageInfo) {
//        boolean isSysApp = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
//        boolean isSysUpd = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1;
//        String packageName = packageInfo.packageName;
//        boolean isSkipPackage = packageName.startsWith("com.android") ||
//                packageName.startsWith("com.google") ||
//                packageName.contains("launcher3") ||
//                packageName.startsWith("com.samsung") ||
//                TextUtils.equals(packageName, "com.topjohnwu.magisk") ||
//                packageName.startsWith("com.facebook") ||
//                TextUtils.equals(packageName, BuildConfig.APPLICATION_ID);
//        return isSkipPackage && (!isSysApp || !isSysUpd);
//    }
}
