package com.mai.oaid.helper.system.impl;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Pair;

import com.mai.oaid.helper.OAIDError;
import com.mai.oaid.helper.system.base.BaseIInterface;

/**
 * 华为、荣耀 (Huawei、Honor)
 */
@SuppressWarnings("unused")
public class HWIInterface extends BaseIInterface {

    private static final String DESCRIPTOR = "com.uodis.opendevice.aidl.OpenDeviceIdentifierService";

    public HWIInterface(IBinder iBinder) {
        super(iBinder);
    }

    @Override
    public boolean isSupport() throws RemoteException {
        return isOaidTrackLimited();
    }

    @Override
    public Pair<String, OAIDError> getOAID() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(1, _data, _reply, 0);
            if (!_status) {
                return new Pair<>(null, OAIDError.STATUS_ERROR);
            }
            _reply.readException();
            return new Pair<>(_reply.readString(), null);
        } finally {
            _data.recycle();
            _reply.recycle();
        }
    }

    public boolean isOaidTrackLimited() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            boolean _status = this.iBinder.transact(2, _data, _reply, 0);
            if (!_status) {
                return true;
            }
            _reply.readException();
            _result = _reply.readInt() == 0;
        } finally {
            _data.recycle();
            _reply.recycle();
        }
        return _result;
    }

}
