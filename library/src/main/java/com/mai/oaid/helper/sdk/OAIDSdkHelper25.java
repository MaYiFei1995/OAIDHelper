package com.mai.oaid.helper.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.mai.oaid.helper.OAIDHelper;

/**
 * 1.0.25及以下版本获取oaid的工具类
 */
public class OAIDSdkHelper25 {

    private static boolean mIsRequesting;
    private static boolean sGetOaidFail;
    private static final String TAG = "OAIDSdk";

    public static void getOAID(Context context, OAIDHelper.RetListener listener) {
        if (context != null && !sGetOaidFail) {
            if (!mIsRequesting) {
                mIsRequesting = true;
                try {
                    long t = System.currentTimeMillis();
                    int code = MdidSdkHelper.InitSdk(context.getApplicationContext(), true, new IIdentifierListener25(t, listener));
                    Log.i(TAG, "OAIDSdkHelper25:sdk init time=" + (System.currentTimeMillis() - t) + "--result=" + code);
                } catch (Throwable tr) {
                    Log.i(TAG, "OAIDSdkHelper25:oaid sdk not find");
                    mIsRequesting = false;
                    sGetOaidFail = true;
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean isSupport() {
        if (Build.VERSION.SDK_INT < 21) {
            sGetOaidFail = false;
        } else {
            try {
                IIdentifierListener iIdentifierListener = (b, idSupplier) -> {
                    // check sdk version
                };
                iIdentifierListener.OnSupport(true, null);
                try {
                    Class.forName("com.bun.miitmdid.core.MdidSdkHelper", false, OAIDSdkHelper25.class.getClassLoader());
                    sGetOaidFail = true;
                } catch (Throwable tr) {
                    Log.i(TAG, "OAIDSdkHelper25:com.bun.miitmdid.core.MdidSdkHelper oaid sdk not find");
                    sGetOaidFail = false;
                }
            } catch (Throwable tr2) {
                Log.i(TAG, "OAIDSdkHelper25:isSupport oaid sdk not find");
                sGetOaidFail = false;
            }
        }
        return sGetOaidFail;
    }

    static class IIdentifierListener25 implements IIdentifierListener {

        private final long mStartTime;
        private final OAIDHelper.RetListener mOaidListener;

        public IIdentifierListener25(long t, OAIDHelper.RetListener listener) {
            this.mStartTime = t;
            this.mOaidListener = listener;
        }

        public void OnSupport(boolean isSupport, IdSupplier idSupplier) {
            String ret = null;
            long t = System.currentTimeMillis() - this.mStartTime;
            if (idSupplier != null) {
                ret = idSupplier.getOAID();
                if (!TextUtils.isEmpty(ret)) {
                    Log.i(TAG, "OAIDSdkHelper25:oaid time=" + t + "--OAID:" + ret);
                } else {
                    sGetOaidFail = true;
                }
            }
            mIsRequesting = false;
            this.mOaidListener.onResult(ret);
        }
    }

}
