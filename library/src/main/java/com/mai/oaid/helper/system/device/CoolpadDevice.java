package com.mai.oaid.helper.system.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.base.BaseIInterface;
import com.mai.oaid.helper.system.impl.CoolpadIInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 酷派 (Coolpad)
 */
public class CoolpadDevice implements BaseDevice {

    private static final String TAG = "CoolpadDeviceIDHelper";

    private final Context context;

    private final LinkedBlockingQueue<IBinder> iBinderQueue = new LinkedBlockingQueue<>(1);

    private final ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
            try {
                Log.d(TAG, "onServiceConnected");
                iBinderQueue.put(param1IBinder);
            } catch (Throwable tr) {
                Log.d(TAG, "conn", tr);
            }
        }

        public void onServiceDisconnected(ComponentName param1ComponentName) {
        }
    };

    public CoolpadDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.coolpad.deviceidsupport", 0);
            return pi != null;
        } catch (Exception e) {
            Log.w(TAG, e);
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.coolpad.deviceidsupport", "com.coolpad.deviceidsupport.DeviceIdService");
        intent.setComponent(componentName);
        boolean bool = this.context.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);
        if (bool) {
            try {
                IBinder iBinder = this.iBinderQueue.take();
                BaseIInterface getter = new CoolpadIInterface(iBinder, context.getPackageName());
                return getter.getOAID();
            } finally {
                this.context.unbindService(this.conn);
            }
        } else {
            Log.e(TAG, "bindService return false");
            return new Pair<>(null, OAIDError.SERVICE_ERROR);
        }
    }

}
