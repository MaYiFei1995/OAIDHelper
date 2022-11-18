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

import java.lang.reflect.Method;

/**
 * 1.0.25版本以上获取oaid的工具类
 */
public class OAIDSdkHelper {

    private static boolean mIsRequesting;
    private static boolean sGetOaidFail;
    private static final String TAG = "OAIDSdk";

    public static void getOAID(Context context, OAIDHelper.RetListener listener) {
        if (context != null && !sGetOaidFail) {
            if (!mIsRequesting) {
                mIsRequesting = true;
                try {
                    long t = System.currentTimeMillis();
                    int code;
                    try {
                        // 仅获取OAID，1.2.0版本后新增方法
                        // InitSdk(cxt, isSDKLogOn, isGetOAID, isGetVAID, isGetAAID, IIdentifierListener);
                        // 建议按需获取 ID，如 VAID、AAID 等不需要可以不获取，获取 ID 是耗时操作，获取不需要的 ID 可能增加耗时风险，甚至导致获取失败（VAID 的获取部分终端涉及网络行为，耗时受网络影响较大）
                        Method initSdkMethod = MdidSdkHelper.class.getMethod("InitSdk", Context.class, boolean.class, boolean.class, boolean.class, boolean.class, IIdentifierListener.class);
                        code = (int) initSdkMethod.invoke(null, context.getApplicationContext(), true, true, false, false, new IIdentifierListenerImpl(t, listener));
                    } catch (Throwable tr) {
                        // 获取全部信息
                        code = MdidSdkHelper.InitSdk(context.getApplicationContext(), true, new IIdentifierListenerImpl(t, listener));
                    }
                    Log.i(TAG, "OAIDSdkHelper:sdk init time=" + (System.currentTimeMillis() - t) + "--result=" + code);
                    if (code != 1008610 && code != 1008614) {
                        // SDK不会回调onSupport
                        listener.onResult(null);
                    }
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
            sGetOaidFail = true;
        } else {
            try {
                try {
                    Log.i(TAG, "OAIDSdkHelper:oaidVersion" + Class.forName("com.bun.miitmdid.e").getMethod("a").invoke(null));
                    try {
                        Class.forName("com.bun.miitmdid.core.MdidSdkHelper", false, OAIDSdkHelper.class.getClassLoader());
                        sGetOaidFail = false;
                    } catch (Throwable tr) {
                        Log.i(TAG, "OAIDSdkHelper:com.bun.miitmdid.core.MdidSdkHelper oaid sdk not find");
                        sGetOaidFail = true;
                    }
                } catch (Throwable tr) {
                    Log.i(TAG, "OAIDSdkHelper:oaidVersion fail");
                    sGetOaidFail = true;
                }

            } catch (Throwable tr) {
                Log.i(TAG, "OAIDSdkHelper:isSupport oaid sdk not find");
                sGetOaidFail = true;
            }
        }
        return !sGetOaidFail;
    }

    static class IIdentifierListenerImpl implements IIdentifierListener {

        private final long mStartTime;
        private final OAIDHelper.RetListener mOaidListener;

        public IIdentifierListenerImpl(long t, OAIDHelper.RetListener listener) {
            this.mStartTime = t;
            this.mOaidListener = listener;
        }

        /**
         * 1.0.26-2.0.0版本的onSupport回调
         */
        @SuppressWarnings("unused")
        public void onSupport(IdSupplier idSupplier) {
            String ret = null;
            long t = System.currentTimeMillis() - this.mStartTime;
            if (idSupplier != null) {
                ret = idSupplier.getOAID();
                if (!TextUtils.isEmpty(ret)) {
                    Log.i(TAG, "OAIDSdkHelper:oaid time=" + t + "--OAID:" + ret);
                } else {
                    sGetOaidFail = true;
                }
            }
            mIsRequesting = false;
            this.mOaidListener.onResult(ret);
        }

        public void OnSupport(boolean b, IdSupplier idSupplier) {

        }

    }

}
