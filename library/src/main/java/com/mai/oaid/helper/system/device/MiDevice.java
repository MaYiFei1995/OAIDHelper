package com.mai.oaid.helper.system.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseDevice;

import java.lang.reflect.Method;

/**
 * 小米、红米、黑鲨、MIUI
 */
public class MiDevice implements BaseDevice {

    private static final String TAG = "MIDeviceIDHelper";

    private final Context context;
    private Class<?> idProviderClass;
    private Object idProviderImpl;

    @SuppressLint("PrivateApi")
    public MiDevice(Context context) {
        this.context = context;
        try {
            idProviderClass = Class.forName("com.android.id.impl.IdProviderImpl");
            idProviderImpl = idProviderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSupport() {
        return idProviderImpl != null;
    }

    @Override
    public Pair<String, OAIDError> getOAID() {
        try {
            Method method = idProviderClass.getMethod("getOAID", Context.class);
            return new Pair<>((String) method.invoke(idProviderClass, new Object[]{this.context}), null);
        } catch (Throwable tr) {
            Log.e(TAG, "unknwon error", tr);
            return new Pair<>(null, OAIDError.UNKNOWN_ERROR);
        }
    }

}
