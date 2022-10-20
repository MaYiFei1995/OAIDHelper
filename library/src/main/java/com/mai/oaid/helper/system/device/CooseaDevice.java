package com.mai.oaid.helper.system.device;

import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;

import java.util.Objects;

/**
 * 酷赛 (Coosea)
 */
public class CooseaDevice implements BaseDevice {

    private static final String TAG = "CooseaDeviceIDHelper";

    private final Context context;
    private final KeyguardManager keyguardManager;

    public CooseaDevice(Context context) {
        this.context = context;
        this.keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        if (keyguardManager == null) {
            return false;
        }
        try {
            Object obj = keyguardManager.getClass().getDeclaredMethod("isSupported").invoke(keyguardManager);
            return (Boolean) Objects.requireNonNull(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        if (keyguardManager == null) {
            Log.e(TAG, "KeyguardManager not found");
            return new Pair<>(null, OAIDError.NOT_SUPPORT);
        }
        Object obj = keyguardManager.getClass().getDeclaredMethod("obtainOaid").invoke(keyguardManager);
        if (obj == null) {
            Log.e(TAG, "OAID obtain failed");
            return new Pair<>(null, OAIDError.SERVICE_ERROR);
        }
        return new Pair<>(obj.toString(), null);
    }

}
