package com.mai.oaid.helper.system.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.base.BaseIInterface;
import com.mai.oaid.helper.system.impl.MSAIInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 中兴、HTC、Moto等
 */
public class MSADevice implements BaseDevice {

    private static final String TAG = "MSADeviceIDHelper";

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

    public MSADevice(Context context) {
        this.context = context;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.mdid.msa", 0);
            return pi != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        try {
            startMsaKlService();
            Intent intent = new Intent("com.bun.msa.action.bindto.service");
            intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaIdService");
            intent.putExtra("com.bun.msa.param.pkgname", context.getPackageName());
            boolean bool = this.context.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);
            if (bool) {
                try {
                    IBinder iBinder = this.iBinderQueue.take();
                    BaseIInterface getter = new MSAIInterface(iBinder);
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
        } catch (Throwable tr) {
            Log.e(TAG, "msa service not found", tr);
            return new Pair<>(null, OAIDError.SERVICE_ERROR);
        }
    }

    private void startMsaKlService() {
        try {
            Intent intent = new Intent("com.bun.msa.action.start.service");
            intent.setClassName("com.mdid.msa", "com.mdid.msa.service.MsaKlService");
            intent.putExtra("com.bun.msa.param.pkgname", context.getPackageName());
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent);
            } else {
                context.startForegroundService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
