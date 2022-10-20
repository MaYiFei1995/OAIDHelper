package com.mai.oaid.helper.system.device;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.ChecksSdkIntAtLeast;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;

/**
 * 努比亚 (Nubia)
 */
public class NubiaDevice implements BaseDevice {

    private static final String TAG = "NubiaDeviceIDHelper";

    private final Context context;

    public NubiaDevice(Context context) {
        this.context = context;
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    @Override
    public boolean isSupport() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        if (isSupport()) {
            Bundle bundle;
            Uri uri = Uri.parse("content://cn.nubia.identity/identity");
            ContentProviderClient contentProviderClient = this.context.getContentResolver().acquireContentProviderClient(uri);
            if (contentProviderClient == null) {
                Log.e(TAG, "content resolver not found");
                return new Pair<>(null, OAIDError.SERVICE_ERROR);
            }
            bundle = contentProviderClient.call("getOAID", null, null);
            if (Build.VERSION.SDK_INT >= 24) {
                contentProviderClient.close();
            } else {
                contentProviderClient.release();
            }
            if (bundle != null) {
                int i = bundle.getInt("code", -1);
                if (i == 0) {
                    String oaid = bundle.getString("id");
                    return new Pair<>(oaid, null);
                } else {
                    Log.e(TAG, "failedMsg:" + bundle.getString("message"));
                    return new Pair<>(null, OAIDError.NOT_SUPPORT);
                }
            } else {
                Log.e(TAG, "bundle is null");
                return new Pair<>(null, OAIDError.SERVICE_ERROR);
            }
        } else {
            Log.e(TAG, "Only supports Android 10.0 and above for Nubia");
            return new Pair<>(null, OAIDError.NOT_SUPPORT);
        }
    }

}
