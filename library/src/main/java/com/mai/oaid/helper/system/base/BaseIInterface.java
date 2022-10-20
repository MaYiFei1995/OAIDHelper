package com.mai.oaid.helper.system.base;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/**
 * @see {https://github.com/gzu-liyujiang/Android_CN_OAID/tree/master/library/src/main/java/repeackage/com}
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
     * 获取OAID
     */
    public abstract String getOAID() throws RemoteException;

    /**
     * 华为获取是否限制广告追踪
     */
    public boolean isOaidTrackLimited() throws RemoteException {
        return false;
    }

    /**
     * Oppo获取OAID
     *
     * @param packageName   应用包名
     * @param signatureSha1 应用签名sha1
     * @param key           "OUID"
     */
    public String getSerID(String packageName, String signatureSha1, String key) throws RemoteException {
        return null;
    }

}
