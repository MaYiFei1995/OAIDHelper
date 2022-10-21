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
import com.mai.oaid.helper.system.impl.LenovoIInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 联想 (ZUI)
 */
public class LenovoDevice implements BaseDevice {

    private static final String TAG = "LenovoDeviceIDHelper";

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

    public LenovoDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.zui.deviceidservice", 0);
            return pi != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        Intent intent = new Intent();
        intent.setClassName("com.zui.deviceidservice", "com.zui.deviceidservice.DeviceidService");
        boolean bool = this.context.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);
        if (bool) {
            try {
                IBinder iBinder = this.iBinderQueue.take();
                BaseIInterface getter = new LenovoIInterface(iBinder);
                if (!getter.isSupport()) {
                    Log.e(TAG, "is not support");
                    return new Pair<>(null, OAIDError.NOT_SUPPORT);
                }
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
