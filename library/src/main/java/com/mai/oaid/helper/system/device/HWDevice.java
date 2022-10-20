package com.mai.oaid.helper.system.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.base.BaseIInterface;
import com.mai.oaid.helper.system.impl.HWIInterface;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 华为、荣耀 (Huawei、Honor)
 */
public class HWDevice implements BaseDevice {

    private static final String TAG = "HWDeviceIDHelper";

    private final Context context;
    private String packageName;

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

    public HWDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        boolean ret = false;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm.getPackageInfo("com.huawei.hwid", 0) != null) {
                packageName = "com.huawei.hwid";
                ret = true;
            } else if (pm.getPackageInfo("com.huawei.hwid.tv", 0) != null) {
                packageName = "com.huawei.hwid.tv";
                ret = true;
            } else {
                packageName = "com.huawei.hms";
                ret = pm.getPackageInfo(packageName, 0) != null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                String oaid = Settings.Global.getString(context.getContentResolver(), "pps_oaid");
                if (!TextUtils.isEmpty(oaid)) {
                    Log.e(TAG, "Get oaid from global settings: " + oaid);
                    return new Pair<>(oaid, null);
                }
            } catch (Exception e) {
                Log.e(TAG, "unknown exception when get form global settings", e);
            }
        }
        if (TextUtils.isEmpty(packageName) && !isSupport()) {
            Log.e(TAG, "Huawei Advertising ID not available");
            return new Pair<>(null, OAIDError.NOT_SUPPORT);
        }

        Intent intent = new Intent();
        intent.setAction("com.asus.msa.action.ACCESS_DID");
        intent.setPackage(packageName);
        boolean bool = this.context.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);
        if (bool) {
            try {
                IBinder iBinder = this.iBinderQueue.take();
                BaseIInterface getter = new HWIInterface(iBinder);
                if (!getter.isSupport()) {
                    // 实测在系统设置中关闭了广告标识符，将获取到固定的一大堆0
                    return new Pair<>(null, OAIDError.LIMITED);
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
