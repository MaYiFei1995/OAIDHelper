package com.mai.oaid.helper.system.device;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;

import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.base.BaseIInterface;

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
    public String getOAID() {
        if (context == null) {
            return "";
        }
        if (keyguardManager == null) {
            Log.w(TAG, "KeyguardManager not found");
            return "";
        }
        try {
            Object obj = keyguardManager.getClass().getDeclaredMethod("obtainOaid").invoke(keyguardManager);
            if (obj == null) {
                Log.w(TAG, "OAID obtain failed");
                return "";
            }
            String oaid = obj.toString();
            Log.d(TAG, "OAID obtain success: " + oaid);
            return oaid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
