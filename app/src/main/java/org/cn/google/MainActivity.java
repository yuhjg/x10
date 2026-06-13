package org.cn.google;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.cn.google.app.AppConstance;
import org.cn.google.mode.LoginResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private TextView tvMainExitLogin, tvMainHookClear, tvMainUpdateTime, tvMainVersionCode;
    private RadioButton rbMainHookCollect, rbMainHookImport, rbMainHookExport, rbMainHookOffline;
    private RadioGroup rgMainHookGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        rgMainHookGroup = findViewById(R.id.rgMainHookGroup);
        rbMainHookCollect = findViewById(R.id.rbMainHookCollect);
        rbMainHookImport = findViewById(R.id.rbMainHookImport);
        rbMainHookExport = findViewById(R.id.rbMainHookExport);
        rbMainHookOffline = findViewById(R.id.rbMainHookOffline);
        tvMainExitLogin = findViewById(R.id.tvMainExitLogin);
        tvMainUpdateTime = findViewById(R.id.tvMainUpdateTime);
        tvMainVersionCode = findViewById(R.id.tvMainVersionCode);
        tvMainHookClear = findViewById(R.id.tvMainHookClear);

        rgMainHookGroup.setOnCheckedChangeListener(this);
        tvMainExitLogin.setOnClickListener(this);
        tvMainHookClear.setOnClickListener(this);


    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        LoginResponse.UserInfo userInfo = userInfo();
        if (userInfo != null) {
            tvMainExitLogin.setText(userInfo.getUsername() + " · 登出");
        }
        tvMainUpdateTime.setText("更新时间：" + TimeUtils.millis2String(System.currentTimeMillis(),"yyyy/MM/dd"));
        tvMainVersionCode.setText("版本号：V" + AppUtils.getAppVersionName());

        int status = SPStaticUtils.getInt(AppConstance.KEY_HOOK_STATUS);
        if (status == 1) {
            rbMainHookCollect.setChecked(true);
        } else if (status == 2) {
            rbMainHookImport.setChecked(true);
        } else if (status == 3) {
            rbMainHookExport.setChecked(true);
        } else if (status == 4) {
            rbMainHookOffline.setChecked(true);
        } else {
            rgMainHookGroup.clearCheck();
        }
    }

    private LoginResponse.UserInfo userInfo() {
        try {
            String userInfoString = SPStaticUtils.getString(AppConstance.KEY_USER_INFO, "");
            if (userInfoString.length() != 0) {
                return GsonUtils.fromJson(userInfoString, LoginResponse.UserInfo.class);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setClass((Context) this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvMainExitLogin) {
            SPStaticUtils.remove(AppConstance.KEY_USER_INFO);
            SPStaticUtils.remove(AppConstance.KEY_HOOK_STATUS);
            Intent intent = new Intent();
            intent.setClass((Context) this, LoginActivity.class);
            startActivity(intent);
            finish();
//            LoginResponse.UserInfo userInfo =
//                    GsonUtils.fromJson(SPStaticUtils.getString(AppConstance.KEY_USER_INFO), LoginResponse.UserInfo.class);
//            ToastUtils.showShort(userInfo.getToken());
        } else if (view.getId() == R.id.tvMainHookClear) {
            tvMainHookClear.setEnabled(false);
            rgMainHookGroup.clearCheck();
            SPStaticUtils.remove(AppConstance.KEY_HOOK_STATUS);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rbMainHookCollect && rbMainHookCollect.isChecked()) {
            tvMainHookClear.setEnabled(true);
            SPStaticUtils.put(AppConstance.KEY_HOOK_STATUS, 1);
        } else if (i == R.id.rbMainHookImport && rbMainHookImport.isChecked()) {
            tvMainHookClear.setEnabled(true);
            SPStaticUtils.put(AppConstance.KEY_HOOK_STATUS, 2);
        } else if (i == R.id.rbMainHookExport && rbMainHookExport.isChecked()) {
            tvMainHookClear.setEnabled(true);
            SPStaticUtils.put(AppConstance.KEY_HOOK_STATUS, 3);
        } else if (i == R.id.rbMainHookOffline && rbMainHookOffline.isChecked()) {
            tvMainHookClear.setEnabled(true);
            SPStaticUtils.put(AppConstance.KEY_HOOK_STATUS, 4);
        }
    }
}