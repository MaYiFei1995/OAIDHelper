package com.mai.oaid.helper.system.base;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;

/**
 * IInterface
 * <p>
 * {@see https://github.com/gzu-liyujiang/Android_CN_OAID/tree/master/library/src/main/java/repeackage/com}
 */
public abstract class BaseIInterface implements IInterface {

    protected final IBinder iBinder;

    public BaseIInterface(IBinder iBinder) {
        this.iBinder = iBinder;
    }

    @Override
    public IBinder asBinder() {
        return iBinder;
    }

    /**
     * 是否支持
     */
    public boolean isSupport() throws RemoteException {
        return true;
    }

    /**
     * 获取OAID
     */
    public abstract Pair<String, OAIDError> getOAID() throws RemoteException;

    /**
     * Oppo获取OAID
     *
     * @param packageName   应用包名
     * @param signatureSha1 应用签名sha1
     * @param key           "OUID"
     */
    public Pair<String, OAIDError> getSerID(String packageName, String signatureSha1, String key) throws RemoteException {
        return null;
    }

}
