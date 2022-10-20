package com.mai.oaid.helper.system.device;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;
import com.mai.oaid.helper.system.base.BaseIInterface;
import com.mai.oaid.helper.system.impl.OPPOIInterface;

import java.security.MessageDigest;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * oppo、realme、OnePlus
 */
public class OppoDevice implements BaseDevice {

    private static final String TAG = "OppoDeviceIDHelper";

    private final Context context;
    private String signature;

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

    public OppoDevice(Context paramContext) {
        this.context = paramContext;
    }

    @Override
    public boolean isSupport() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.heytap.openid", 0);
            return pi != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws Exception {
        Intent intent = new Intent();
        intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");
        intent.setComponent(new ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService"));
        boolean bool = this.context.bindService(intent, this.conn, Context.BIND_AUTO_CREATE);
        if (bool) {
            try {
                IBinder iBinder = this.iBinderQueue.take();
                BaseIInterface getter = new OPPOIInterface(iBinder);
                return getter.getSerID(this.context.getPackageName(), getAppSignatureSha1(), "OUID");
            } finally {
                this.context.unbindService(this.conn);
            }
        } else {
            Log.e(TAG, "bindService return false");
            return new Pair<>(null, OAIDError.SERVICE_ERROR);
        }
    }

    private String getAppSignatureSha1() {
        if (signature == null || signature.length() == 0) {
            try {
                Signature[] arrayOfSignature = (this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), PackageManager.GET_SIGNATURES)).signatures;
                if (arrayOfSignature != null && arrayOfSignature.length > 0) {
                    byte[] arrayOfByte1 = arrayOfSignature[0].toByteArray();
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    byte[] arrayOfByte2 = messageDigest.digest(arrayOfByte1);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (byte b : arrayOfByte2)
                        stringBuilder.append(Integer.toHexString(b & 0xFF | 0x100), 1, 3);
                    signature = stringBuilder.toString();
                }
            } catch (Throwable tr) {
                Log.e(TAG, "getAppSignatureSha1", tr);
            }
        }
        return signature;
    }

}
