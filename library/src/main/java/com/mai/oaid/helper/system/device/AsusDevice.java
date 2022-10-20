package com.mai.oaid.helper.system.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.util.Log;

import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.base.BaseIInterface;
import com.mai.oaid.helper.system.impl.AsusIInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 华硕 (ASUS)
 */
public class AsusDevice implements BaseDevice {

    private static final String TAG = "ASUSDeviceIDHelper";

    private final Context context;

    private final LinkedBlockingQueue<IBinder> iBinderQueue = new LinkedBlockingQueue<>(1);

    private final ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
            try {
                Log.d(TAG, "onServiceConnected");
                iBinderQueue.put(param1IBinder);
            } catch (Throwable t) {
                Log.d(TAG, "conn", t);
            }
        }

        public void onServiceDisconnected(ComponentName param1ComponentName) {
        }
    };

    public AsusDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.asus.msa.SupplementaryDID", 0);
            return pi != null;
        } catch (Exception e) {
            Log.w(TAG, e);
            return false;
        }
    }

    @Override
    public String getOAID() {
        String ret = "";
        try {
            Intent intent = new Intent();
            intent.setAction("com.asus.msa.action.ACCESS_DID");
            ComponentName componentName = new ComponentName("com.asus.msa.SupplementaryDID", "com.asus.msa.SupplementaryDID.SupplementaryDIDService");
            intent.setComponent(componentName);
            boolean bool = this.context.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);
            if (bool)
                try {
                    IBinder iBinder = this.iBinderQueue.take();
                    BaseIInterface getter = new AsusIInterface(iBinder);
                    ret = getter.getOAID();
                    Log.d(TAG, "oaid:" + ret);
                } catch (Throwable t) {
                    Log.w(TAG, t);
                } finally {
                    this.context.unbindService(this.conn);
                }
        } catch (Throwable t) {
            Log.d(TAG, "asus service not found", t);
        }
        return ret;
    }

}
