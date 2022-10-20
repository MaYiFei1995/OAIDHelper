package com.mai.oaid.helper.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.mai.oaid.helper.OAIDCallback;

/**
 * 1.0.25版本以上获取oaid的工具类
 */
public class OAIDSdkHelper {

    private static boolean mIsRequesting;
    private static boolean sGetOaidFail;
    private static final String TAG = "OAIDSdk";

    public static void getOAID(Context context, OAIDCallback listener) {
        if (context != null && !sGetOaidFail) {
            if (!isSupport()) {
                sGetOaidFail = true;
            } else if (!mIsRequesting) {
                mIsRequesting = true;
                try {
                    long t = System.currentTimeMillis();
                    Log.i(TAG, "OAIDSdkHelper:sdk init time=" + (System.currentTimeMillis() - t) + "--result=" + MdidSdkHelper.InitSdk(context.getApplicationContext(), true, new IIdentifierListenerImpl(t, listener)));
                } catch (Throwable tr) {
                    Log.i(TAG, "OAIDSdkHelper:oaid sdk not find ");
                    mIsRequesting = false;
                    sGetOaidFail = true;
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean isSupport() {
        if (Build.VERSION.SDK_INT < 16) {
            return false;
        }
        try {
            try {
                Log.i(TAG, "OAIDSdkHelper:oaidVersion" + Class.forName("com.bun.miitmdid.e").getMethod("a").invoke(null));
                try {
                    Class.forName("com.bun.miitmdid.core.MdidSdkHelper", false, OAIDSdkHelper.class.getClassLoader());
                    return true;
                } catch (Throwable tr) {
                    Log.i(TAG, "OAIDSdkHelper:com.bun.miitmdid.core.MdidSdkHelper oaid sdk not find");
                    return false;
                }
            } catch (Throwable tr) {
                Log.i(TAG, "OAIDSdkHelper:oaidVersion fail");
                return false;
            }

        } catch (Throwable tr) {
            Log.i(TAG, "OAIDSdkHelper:isSupport oaid sdk not find");
        }
        return false;
    }

    static class IIdentifierListenerImpl implements IIdentifierListener {

        private final OAIDCallback mOaidListener;
        private final long mStartTime;

        public IIdentifierListenerImpl(long t, OAIDCallback oaidCallback) {
            this.mStartTime = t;
            this.mOaidListener = oaidCallback;
        }

        /**
         * 1.0.26-2.0.0版本的onSupport回调
         */
        @SuppressWarnings("unused")
        public void onSupport(IdSupplier idSupplier) {
            long l = System.currentTimeMillis() - this.mStartTime;
            if (idSupplier != null) {
                String ret = idSupplier.getOAID();
                if (!TextUtils.isEmpty(ret)) {
                    Log.i(TAG, "OAIDSdkHelper:oaid time=" + l + "--OAID:" + ret);
                    this.mOaidListener.onResult(ret);
                } else {
                    sGetOaidFail = true;
                }
            }

            mIsRequesting = false;
        }

        public void OnSupport(boolean b, IdSupplier idSupplier) {

        }

    }

}
