package org.cn.google.hook_import.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingResult;

public class BillingActivity extends BaseSkuActivity {

    static final String KEY_RESULT_RECEIVER = "result_receiver";
    private static final int REQUEST_CODE_LAUNCH_ACTIVITY = 100;
    private static final String TAG = "ProxyBillingActivity";
    private ResultReceiver mResultReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleEvent(getIntent());
            return;
        }
        this.mResultReceiver = (ResultReceiver) savedInstanceState.getParcelable(KEY_RESULT_RECEIVER);

    }

    public void handleEvent(Intent intent) {
        PendingIntent pendingIntent;
        this.mResultReceiver = (ResultReceiver) intent.getParcelableExtra(KEY_RESULT_RECEIVER);
        if (intent.hasExtra("BUY_INTENT")) {
            pendingIntent = (PendingIntent) intent.getParcelableExtra("BUY_INTENT");
        } else {
            pendingIntent = intent.hasExtra("SUBS_MANAGEMENT_INTENT") ? (PendingIntent) intent.getParcelableExtra("SUBS_MANAGEMENT_INTENT") : null;
        }
        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(), 100, new Intent(), 0, 0, 0);
        } catch (Exception e) {
            ResultReceiver resultReceiver = this.mResultReceiver;
            if (resultReceiver != null) {
                resultReceiver.send(6, null);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        ResultReceiver resultReceiver = this.mResultReceiver;
        if (resultReceiver != null) {
            outState.putParcelable(KEY_RESULT_RECEIVER, resultReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
//            int responseCodeFromIntent = C0089BillingHelper.getResponseCodeFromIntent(intent, TAG);
//            if (!(resultCode == -1 && responseCodeFromIntent == 0)) {
//                C0089BillingHelper.logWarn(TAG, "Activity finished with resultCode " + i2 + " and billing's responseCode: " + responseCodeFromIntent);
//            }
            int responseCodeFromIntent = getResponseCodeFromIntent(data, TAG);
            this.mResultReceiver.send(responseCodeFromIntent, data == null ? null : data.getExtras());
        }

    }

    public static int getResponseCodeFromIntent(Intent intent, String tag) {
        return getBillingResultFromIntent(intent, tag).getResponseCode();
    }

    public static BillingResult getBillingResultFromIntent(Intent intent, String tag) {
        if (intent != null) {
            return BillingResult.newBuilder().setResponseCode(getResponseCodeFromBundle(intent.getExtras(), tag)).setDebugMessage(getDebugMessageFromBundle(intent.getExtras(), tag)).build();
        }
        return BillingResult.newBuilder().setResponseCode(6).setDebugMessage("An internal error occurred.").build();
    }

    public static int getResponseCodeFromBundle(Bundle bundle, String tag) {
        if (bundle == null) {
            return 6;
        }
        Object responseCode = bundle.get("RESPONSE_CODE");
        if (responseCode == null) {
            return 0;
        } else if (responseCode instanceof Integer) {
            return ((Integer) responseCode).intValue();
        } else {
            return 6;
        }
    }

    public static String getDebugMessageFromBundle(Bundle bundle, String tag) {
        if (bundle == null) {
            return "";
        }
        Object debugMessage = bundle.get("DEBUG_MESSAGE");
        if (debugMessage == null) {
            return "";
        } else if (debugMessage instanceof String) {
            return (String) debugMessage;
        } else {
            return "";
        }
    }


}
